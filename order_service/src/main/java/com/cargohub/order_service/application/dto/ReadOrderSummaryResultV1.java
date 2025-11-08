package com.cargohub.order_service.application.dto;

import com.cargohub.order_service.domain.entity.Order;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadOrderSummaryResultV1(
        UUID id,
        UUID supplierId,
        UUID receiverId,
        OrderStatusResultV1 status,
        LocalDateTime createdAt
) {

    public static ReadOrderSummaryResultV1 from(Order order) {
        return new ReadOrderSummaryResultV1(
                order.getId(),
                order.getSupplierId().getId(),
                order.getReceiverId().getId(),
                OrderStatusResultV1.from(order.getStatus()),
                order.getCreatedAt()
        );
    }
}
