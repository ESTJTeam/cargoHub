package com.cargohub.order_service.application;

import com.cargohub.order_service.application.command.*;
import com.cargohub.order_service.application.dto.CreateOrderResultV1;
import com.cargohub.order_service.application.dto.FirmInfoResultV1;
import com.cargohub.order_service.application.dto.ReadOrderDetailResultV1;
import com.cargohub.order_service.application.dto.ReadOrderSummaryResultV1;
import com.cargohub.order_service.application.exception.OrderErrorCode;
import com.cargohub.order_service.application.exception.OrderException;
import com.cargohub.order_service.application.service.firm.FirmClient;
import com.cargohub.order_service.application.service.firm.FirmListResponseV1;
import com.cargohub.order_service.application.service.firm.FirmResponseV1;
import com.cargohub.order_service.application.service.hub.HubClient;
import com.cargohub.order_service.application.service.hub.HubManagerCheckResponseDto;
import com.cargohub.order_service.application.service.hub.HubResponseV1;
import com.cargohub.order_service.application.service.product.*;
import com.cargohub.order_service.application.service.user.UserClient;
import com.cargohub.order_service.application.service.user.UserInfoResponseV1;
import com.cargohub.order_service.common.JwtUtil;
import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.entity.OrderProduct;
import com.cargohub.order_service.domain.repository.OrderRepository;
import com.cargohub.order_service.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final JwtUtil jwtUtil;

    private final UserClient userClient;
    private final ProductClient productClient;
    private final HubClient hubClient;
    private final FirmClient firmClient;

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 createOrderCommandV1) {
        // 권한 체크 - 분리
//        UserInfoResponseV1 userInfo = userClient.getUser(createOrderCommandV1.createdBy());
//        UserRole role =userInfo.role();
//        if(!UserRole.MASTER.equals(role)
//            && !UserRole.SUPPLIER_MANAGER.equals(role)) {
//            throw new OrderException(OrderErrorCode.ORDER_ACCESS_DENIED);
//        }

        List<OrderProduct> productList = createOrderCommandV1.products().stream()
            .map(request -> {
                ProductId productId = ProductId.of(request.id());
                return new OrderProduct(
                    productId,
                    request.name(),
                    request.price(),
                    request.quantity()
                );
            })
            .toList();

        ReceiverId receiverId = ReceiverId.of(createOrderCommandV1.receiverId());
        SupplierId supplierId = SupplierId.of(createOrderCommandV1.supplierId());

        Order order = Order.ofNewOrder(
            productList,
            supplierId,
            receiverId,
            createOrderCommandV1.requestNote(),
            createOrderCommandV1.createdBy()
        );

        Order newOrder = orderRepository.save(order);

        return CreateOrderResultV1.from(newOrder);
    }

    @Transactional(readOnly = true)
    public Page<ReadOrderSummaryResultV1> readOrderPage(SearchOrderCommandV1 searchOrderCommandV1, Pageable pageable) {

        Page<Order> orderPage;

        switch (searchOrderCommandV1.user().role()) {
            case MASTER -> orderPage = orderRepository.findOrderPage(searchOrderCommandV1, pageable);
            case HUB_MANAGER -> {
                //  userId로 허브 찾기
                Page<HubResponseV1> hubInfo = hubClient.getHubByManger(searchOrderCommandV1.user().id(), 100);
                Set<UUID> hubIds = hubInfo.getContent().stream()
                        .map(HubResponseV1::id)
                        .collect(Collectors.toSet());

                Set<UUID> firmIdList = new HashSet<>();
                for (UUID hubId : hubIds) {
                    FirmListResponseV1 firmInfoList = firmClient.getFirmList(hubId, 1, 100);
                    firmIdList.addAll(
                            firmInfoList.firms().stream()
                                    .map(FirmListResponseV1.FirmResponse::id)
                                    .collect(Collectors.toSet())
                    );
                }

                orderPage = orderRepository.findOrderPageByFirmIdIn(firmIdList, searchOrderCommandV1, pageable);
            }
            case SUPPLIER_MANAGER -> {
                // firm client에서 userId로 업체 찾기
                UUID firmId = UUID.randomUUID();
                orderPage = orderRepository.findOrderPageByFirmId(firmId, searchOrderCommandV1, pageable);
            }
            case DELIVERY_MANAGER -> {
                // 배송 담당자 조회(업체 배송, 허브 배송)
                /**
                 * 1. 허브 배송인지, 업체 배송인지 확인
                 * 2. UserId로 업체 배송, 허브 배송 Client에서 배송 ID 조회
                 * 3. 업체 배송, 허브 배송 ID로 Page 조회
                 */

                orderPage = null;
            }
            default ->
                    throw new OrderException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }

        return orderPage.map(ReadOrderSummaryResultV1::from);
    }

    @Transactional(readOnly = true)
    public ReadOrderDetailResultV1 readOrder(UUID orderId) {
        Order order = findOrder(orderId);

        // 공급업체 정보 필요
        FirmResponseV1 supplierInfo = firmClient.getFirm(order.getSupplierId().getId());
        FirmInfoResultV1 supplier = new FirmInfoResultV1(
                supplierInfo.id(),
                supplierInfo.name(),
                supplierInfo.address().fullAddress()
        );

        // 수령 업체 정보
        FirmResponseV1 receiverInfo = firmClient.getFirm(order.getSupplierId().getId());
        FirmInfoResultV1 receiver = new FirmInfoResultV1(
                receiverInfo.id(),
                receiverInfo.name(),
                receiverInfo.address().fullAddress()
        );

        return ReadOrderDetailResultV1.from(order, supplier, receiver);
    }

    @Transactional
    public void updateOrderStatus(UpdateOrderStatusCommandV1 updateOrderStatusCommandV1, String accessToken) {
        // todo: 권한체크 - 분리
        UserInfoResponseV1 user = jwtUtil.parseJwt(accessToken);

        UserRole role = user.role();
        if(!UserRole.MASTER.equals(role)
                && !UserRole.HUB_MANAGER.equals(role)) {
            throw new OrderException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }

        Order order = findOrder(updateOrderStatusCommandV1.id());

        // 허브 담당자일 경우 담당 허브 주문이 맞는지 체크
        if (UserRole.HUB_MANAGER.equals(user.role())) {
            validateHubOrderOwnership(order, accessToken);
        }
        order.updateStatus(updateOrderStatusCommandV1.status(), user.userId());
    }

    public void cancelOrder(DeleteOrderCommandV1 deleteOrderCommandV1) {

        Order order = findOrder(deleteOrderCommandV1.id());

        UserRole role = deleteOrderCommandV1.user().role();

        if(UserRole.DELIVERY_MANAGER.equals(role)) {
            throw new OrderException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }

        // 업체 담당자일 경우
        if(UserRole.SUPPLIER_MANAGER.equals(role)) {
            // todo: 업체 담당자(createdBY)의 업체 ID 필요

            ReceiverId receiverId = ReceiverId.of(UUID.randomUUID());
            if(order.getReceiverId().equals(receiverId)) {
                throw new OrderException(OrderErrorCode.ORDER_ACCESS_DENIED);
            }
        }

        List<UpdateProductStockRequestV1.StockUpdateItemRequest> stockUpdateItems = order.getOrderProducts().stream()
                .map(p -> new UpdateProductStockRequestV1.StockUpdateItemRequest(
                        p.getProductId().getId(),
                        p.getQuantity()
                ))
                .toList();

        UpdateProductStockRequestV1 requestV1 = new UpdateProductStockRequestV1(stockUpdateItems);
        productClient.increaseStock(requestV1);
        order.cancel(deleteOrderCommandV1.user().id());

    }

    private Order findOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
    }

    private OrderProduct createOrderProduct(OrderProductCommandV1 request, BulkProductQueryResponseV1 productMap) {

        ProductId productId = ProductId.of(request.productId());

        BulkProductQueryResponseV1.ProductInfo product = productMap.products().get(request.productId());

        if(product == null) {
            throw new OrderException(OrderErrorCode.ORDER_NOT_FOUND);
        }

        if(!product.sellable()) {
            throw new OrderException(OrderErrorCode.ORDER_PRODUCT_NOT_AVAILABLE);
        }

        return new OrderProduct(
                productId,
                product.name(),
                product.price(),
                request.quantity()
        );
    }

    // 재고 차감
    private void decreaseStock(List<OrderProductCommandV1> products) {

        List<UpdateProductStockRequestV1.StockUpdateItemRequest> stockUpdateItems = products.stream()
                .map(p -> new UpdateProductStockRequestV1.StockUpdateItemRequest(
                        p.productId(),
                        p.quantity()
                ))
                .collect(Collectors.toList());

        productClient.decreaseStock(new UpdateProductStockRequestV1(stockUpdateItems));
    }
    // 주문 내 상품들의 공급업체/수령업체가 허브 소속인지 확인
    private void validateHubOrderOwnership(Order order, String accessToken) {

        SupplierId supplierId = order.getSupplierId();
        ReceiverId receiverId = order.getReceiverId();
        // 공급 업체/수령업체 찾기
        FirmResponseV1 supplierInfo = firmClient.getFirm(supplierId.getId());
        FirmResponseV1 receiverInfo = firmClient.getFirm(receiverId.getId());

        // 공급 업체/수령 업체 허브 담당자 확인
        HubManagerCheckResponseDto supplierHub =  hubClient.checkHubManager(supplierInfo.hubId(), accessToken);
        HubManagerCheckResponseDto receiverHub =  hubClient.checkHubManager(receiverInfo.hubId(), accessToken);

        if(!supplierHub.isManager() && !receiverHub.isManager()){
            throw new OrderException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
    }
}
