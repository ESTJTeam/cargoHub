package com.cargohub.product_service.presentation.dto.response;

import com.cargohub.product_service.application.dto.CreateProductResultV1;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductResponseV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        Integer stockQuantity,
        BigDecimal price,
        boolean sellable,
        LocalDateTime createdAt
) {
    public static CreateProductResponseV1 from(CreateProductResultV1 result) {
        return new CreateProductResponseV1(
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
