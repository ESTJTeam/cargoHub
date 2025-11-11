package com.cargohub.order_service.application;

import com.cargohub.order_service.application.command.*;
import com.cargohub.order_service.application.dto.CreateOrderResultV1;
import com.cargohub.order_service.application.dto.FirmInfoResultV1;
import com.cargohub.order_service.application.dto.ReadOrderDetailResultV1;
import com.cargohub.order_service.application.dto.ReadOrderSummaryResultV1;
import com.cargohub.order_service.application.exception.OrderErrorCode;
import com.cargohub.order_service.application.exception.OrderException;
import com.cargohub.order_service.application.service.ProductClient;
import com.cargohub.order_service.application.service.product.BilkProductQueryRequestV1;
import com.cargohub.order_service.application.service.product.BulkProductQueryResponseV1;
import com.cargohub.order_service.application.service.product.UpdateProductStockRequestV1;
import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.entity.OrderProduct;
import com.cargohub.order_service.domain.repository.OrderRepository;
import com.cargohub.order_service.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 createOrderCommandV1) {
        // todo: 1. 권한 체크 - 마스터, 업체 담당자가 아닐경우 403



        // todo: 재고 차감 로직 분리
        BilkProductQueryRequestV1 requestV1 = new BilkProductQueryRequestV1(
                createOrderCommandV1.products().stream()
                .map(OrderProductCommandV1::productId)
                .toList());

        BulkProductQueryResponseV1 productMap = productClient.getProducts(requestV1);

        // 3. 같은 공급 업체인지
        Set<SupplierId> supplierIds = productMap.products().values().stream()
                .map(product -> SupplierId.of(product.firmId()))
                .collect(Collectors.toSet());

        if(supplierIds.size() > 1) {
            throw new OrderException(OrderErrorCode.INVALID_ORDER_SUPPLIER);
        }

        // 4. 상품 재고 차감
        List<UpdateProductStockRequestV1.StockUpdateItemRequest> stockUpdateItems = createOrderCommandV1.products().stream()
                .map(p -> new UpdateProductStockRequestV1.StockUpdateItemRequest(
                        p.productId(),
                        p.quantity()
                ))
                .collect(Collectors.toList());

        productClient.decreaseStock(new UpdateProductStockRequestV1(stockUpdateItems));

        // 5. 주문 상품 생성
        List<OrderProduct> productList = createOrderCommandV1.products().stream()
                .map(request -> createOrderProduct(request, productMap))
                .toList();

        ReceiverId receiverId = ReceiverId.of(createOrderCommandV1.receiverId());
        SupplierId supplierId = supplierIds.iterator().next();

        Order order = Order.ofNewOrder(
                productList,
                supplierId,
                receiverId,
                createOrderCommandV1.requestNote(),
                createOrderCommandV1.createdBy()
        );

        Order newOrder = orderRepository.save(order);

        // todo: 배송 생성

        HubDeliveryId hubDeliveryId = HubDeliveryId.of(UUID.randomUUID());
        FirmDeliveryId firmDeliveryId = FirmDeliveryId.of(UUID.randomUUID());
        newOrder.ship(hubDeliveryId, firmDeliveryId);

        return CreateOrderResultV1.from(newOrder);
    }

    @Transactional(readOnly = true)
    public Page<ReadOrderSummaryResultV1> readOrderPage(SearchOrderCommandV1 searchOrderCommandV1, Pageable pageable, UserInfo userInfo) {

        Page<Order> orderPage;

        switch (userInfo.role()) {
            case MASTER -> orderPage = orderRepository.findOrderPage(searchOrderCommandV1, pageable);
            case HUB_MANAGER -> {
                /**
                 * todo:
                 * 1. hub client에서 담당자가 userId인 허브 찾기
                 * 2. firm client에서 hubId로 업체 찾기 -> 예) v1/hubs/{hubId}/firms
                 * 3. firm List??로 주문 찾기
                 */

                SupplierId receiverId = SupplierId.of(UUID.randomUUID());
                orderPage = orderRepository.findOrderPageBySupplierId(receiverId, searchOrderCommandV1, pageable);
            }
            case SUPPLIER_MANAGER -> {
                // todo: firm client에서 userId로 업체 찾기

                ReceiverId receiverId = ReceiverId.of(UUID.randomUUID());
                orderPage = orderRepository.findOrderPageByReceiverId(receiverId, searchOrderCommandV1, pageable);
            }
            case DELIVERY_MANAGER -> {
                // todo 배송 담당자 조회(업체 배송, 허브 배송)
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

        // todo 수령업체, 공급업체 정보 필요 - 이름, 주소
        FirmInfoResultV1 supplier = new FirmInfoResultV1(
                UUID.randomUUID(),
                "공급 업체 명",
                "인천광역시 연수구 테크노파크로 110 우지타워 2층"
        );

        FirmInfoResultV1 receiver = new FirmInfoResultV1(
                UUID.randomUUID(),
                "수령 업체 명",
                "인천광역시 연수구 테크노파크로 110 우지타워 2층"
        );

        return ReadOrderDetailResultV1.from(order, supplier, receiver);
    }

    @Transactional
    public void updateOrderStatus(UpdateOrderStatusCommandV1 updateOrderStatusCommandV1) {
        // todo: 권한체크

        Order order = findOrder(updateOrderStatusCommandV1.id());
        // todo: 허브 담당자일 경우 담당 허브 주문이 맞는지 체크

        order.updateStatus(updateOrderStatusCommandV1.status(), updateOrderStatusCommandV1.updatedBy());
    }

    public void cancelOrder(DeleteOrderCommandV1 deleteOrderCommandV1) {
        // todo: 본인만 취소 가능
        Order order = findOrder(deleteOrderCommandV1.id());

        // 업체 담당자일 경우
//        if(!role.equals(UserRole.MASTER) && !role.equals(UserRole.HUB_MANAGER)){
//            // todo: 업체 담당자(createdBY)의 업체 ID 필요
//            ReceiverId receiverId = ReceiverId.of(UUID.randomUUID());
//            if(order.getReceiverId().equals(receiverId)) {
//                throw new OrderException(OrderErrorCode.ORDER_ACCESS_DENIED);
//            }
//        }

        List<UpdateProductStockRequestV1.StockUpdateItemRequest> stockUpdateItems = order.getOrderProducts().stream()
                .map(p -> new UpdateProductStockRequestV1.StockUpdateItemRequest(
                        p.getProductId().getId(),
                        p.getQuantity()
                ))
                .toList();

        UpdateProductStockRequestV1 requestV1 = new UpdateProductStockRequestV1(stockUpdateItems);
        productClient.increaseStock(requestV1);
        order.delete(deleteOrderCommandV1.deletedBy());

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
}
