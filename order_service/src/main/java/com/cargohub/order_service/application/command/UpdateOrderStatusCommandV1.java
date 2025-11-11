package com.cargohub.order_service.application.command;

import com.cargohub.order_service.domain.vo.OrderStatus;

import java.util.UUID;

public record UpdateOrderStatusCommandV1(
        UUID id,
        OrderStatus status,
        UserInfo user
) {
}
