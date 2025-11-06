package com.cargohub.product_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadProductSummaryResponseV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        Integer stockQuantity,
        Integer price,
        boolean sellable,
        LocalDateTime createdAt
) {
}
