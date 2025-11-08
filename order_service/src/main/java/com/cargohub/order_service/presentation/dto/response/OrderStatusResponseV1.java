package com.cargohub.order_service.presentation.dto.response;

import com.cargohub.order_service.domain.vo.OrderStatus;

public record OrderStatusResponseV1(
        String code,
        String label
) {
    public static OrderStatusResponseV1 of(OrderStatus status) {
        return new OrderStatusResponseV1(status.getCode(), status.getLabel());
    }
}
