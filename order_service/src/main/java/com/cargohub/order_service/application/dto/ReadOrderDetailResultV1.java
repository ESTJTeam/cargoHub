package com.cargohub.order_service.application.dto;

import com.cargohub.order_service.domain.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReadOrderDetailResultV1(
        UUID id,
        FirmInfoResultV1 supplier,
        FirmInfoResultV1 receiver,
        List<OrderProductResultV1> products,
        OrderStatusResultV1 status,
        String requestNote,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        UUID updatedBy,
        LocalDateTime deletedAt,
        UUID deletedBy
) {

    public static ReadOrderDetailResultV1 from(Order order, FirmInfoResultV1 supplier, FirmInfoResultV1 receiver) {
        return new ReadOrderDetailResultV1(
                order.getId(),
                supplier,
                receiver,
                order.getOrderProducts().stream()
                        .map(OrderProductResultV1::from)
                        .toList(),
                OrderStatusResultV1.from(order.getStatus()),
                order.getRequestNote(),
                order.getCreatedAt(),
                order.getCreatedBy(),
                order.getUpdatedAt(),
                order.getUpdatedBy(),
                order.getDeletedAt(),
                order.getDeletedBy()
        );
    }
}
