package com.cargohub.order_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReadOrderDetailResponseV1(
        UUID id,
        UUID supplierId,
        String supplierName,
        UUID receiverId,
        String receiverName,
        List<OrderProductResponseV1> products,
        OrderStatusResponseV1 status,
        String requestNote,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        UUID updatedBy,
        LocalDateTime deletedAt,
        UUID deletedBy
) {
}
