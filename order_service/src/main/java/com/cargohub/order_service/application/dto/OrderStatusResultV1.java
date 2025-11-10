package com.cargohub.order_service.application.dto;

import com.cargohub.order_service.domain.vo.OrderStatus;

public record OrderStatusResultV1(
        OrderStatus code,
        String label
) {

    public static OrderStatusResultV1 from(OrderStatus orderStatus){
        return new OrderStatusResultV1(
                orderStatus,
                orderStatus.getLabel()
        );
    }
}
