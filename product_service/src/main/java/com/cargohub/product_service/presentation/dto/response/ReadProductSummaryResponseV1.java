package com.cargohub.product_service.presentation.dto.response;

import com.cargohub.product_service.application.dto.ReadProductSummaryResultV1;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReadProductSummaryResponseV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        Integer stockQuantity,
        BigDecimal price,
        boolean sellable,
        LocalDateTime createdAt
) {

    public static ReadProductSummaryResponseV1 from(ReadProductSummaryResultV1 result) {

        return new ReadProductSummaryResponseV1(
                result.id(),
                result.name(),
                result.firmId(),
                result.hubId(),
                result.stockQuantity(),
                result.price(),
                result.sellable(),
                result.createdAt()
        );
    }
}
