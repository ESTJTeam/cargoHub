package com.cargohub.order_service.presentation.dto.response;


import com.cargohub.order_service.application.dto.OrderStatusResultV1;

public record OrderStatusResponseV1(
        String code,
        String label
) {
    public static OrderStatusResponseV1 from(OrderStatusResultV1 result) {
        return new OrderStatusResponseV1(
                result.status().getCode(),
                result.status().getLabel()
        );
    }
}
