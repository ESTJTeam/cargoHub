package com.cargohub.order_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadOrderSummaryResponseV1(
        UUID id,
        UUID supplierId,
        String supplierName,
        UUID receiverId,
        String receiverName,
        OrderStatusResponseV1 status,
        LocalDateTime createdAt

) {
}
