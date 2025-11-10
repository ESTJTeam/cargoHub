package com.cargohub.order_service.application;

import com.cargohub.order_service.application.command.CreateOrderCommandV1;
import com.cargohub.order_service.application.command.DeleteOrderCommandV1;
import com.cargohub.order_service.application.command.UpdateOrderStatusCommandV1;
import com.cargohub.order_service.application.dto.CreateOrderResultV1;
import com.cargohub.order_service.application.dto.FirmInfoResultV1;
import com.cargohub.order_service.application.dto.ReadOrderDetailResultV1;
import com.cargohub.order_service.application.exception.OrderErrorCode;
import com.cargohub.order_service.application.exception.OrderException;
import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.repository.OrderRepository;
import com.cargohub.order_service.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
//    private final ProductClient productClient;

    @Transactional
    public CreateOrderResultV1 createOrder(CreateOrderCommandV1 createOrderCommandV1) {
        // todo: 권한 체크 - 마스터, 업체 담당자가 아닐경우 403


        // todo: 상품 조회
        List<ProductId> productIds = createOrderCommandV1.products().stream()
                .map(request -> ProductId.of(request.productId()))
                .distinct()
                .toList();

        // todo: 상품 재고 차감

        // todo: order.save()

        ReceiverId receiverId = ReceiverId.of(createOrderCommandV1.receiverId());
        SupplierId supplierId = SupplierId.of(UUID.randomUUID()); // todo: 공급 업체 Id 수정

        Order order = Order.ofNewOrder(
                null, // productList
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

        order.delete(deleteOrderCommandV1.deletedBy());

    }

    private Order findOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
    }
}
