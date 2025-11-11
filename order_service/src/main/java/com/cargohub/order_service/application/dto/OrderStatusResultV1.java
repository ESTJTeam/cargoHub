package com.cargohub.order_service.application.dto;

import com.cargohub.order_service.domain.vo.OrderStatus;

public record OrderStatusResultV1(
        OrderStatus status
) {

    public static OrderStatusResultV1 from(OrderStatus orderStatus){
        return new OrderStatusResultV1(
                orderStatus
        );
    }
}
