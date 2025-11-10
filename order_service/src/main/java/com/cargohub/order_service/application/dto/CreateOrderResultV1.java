package com.cargohub.order_service.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderResultV1(
    UUID id,
    UUID supplierId,
    String supplierName,
    UUID receiverID,
    String receiverName,
    List<OrderProductResultV1> products,
    String requestNote,
    LocalDateTime createdAt
) {
}
