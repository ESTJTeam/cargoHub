package com.cargohub.product_service.application.dto;

import com.cargohub.product_service.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReadProductSummaryResultV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        Integer stockQuantity,
        BigDecimal price,
        boolean sellable,
        LocalDateTime createdAt
) {

    public static ReadProductSummaryResultV1 from(Product product) {

        return new ReadProductSummaryResultV1(
                product.getId(),
                product.getName(),
                product.getFirmId().getId(),
                product.getHubId().getId(),
                product.getStockQuantity(),
                product.getPrice(),
                product.getSellable(),
                product.getCreatedAt()
        );
    }
}
