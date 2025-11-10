package com.cargohub.order_service.application.dto;

import com.cargohub.order_service.domain.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderResultV1(
    UUID id,
    UUID supplierId,
    UUID receiverID,
    List<OrderProductResultV1> products,
    String requestNote,
    LocalDateTime createdAt
) {
    public static CreateOrderResultV1 from(Order order) {
        return new CreateOrderResultV1(
                order.getId(),
                order.getSupplierId().getId(),
                order.getReceiverId().getId(),
                order.getOrderProducts().stream()
                        .map(OrderProductResultV1::from)
                        .toList(),
                order.getRequestNote(),
                order.getCreatedAt()
        );
    }
}
