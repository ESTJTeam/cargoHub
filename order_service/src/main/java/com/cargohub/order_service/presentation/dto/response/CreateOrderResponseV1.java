package com.cargohub.order_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderResponseV1(
        UUID id,
        UUID supplierId,
        String supplierName,
        UUID receiverId,
        String receiverName,
        List<OrderProductResponseV1> products,
        String requestNote,
        LocalDateTime createdAt
) {
}
