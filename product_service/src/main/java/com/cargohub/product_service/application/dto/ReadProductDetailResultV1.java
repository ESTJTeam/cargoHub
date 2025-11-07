package com.cargohub.product_service.application.dto;

import com.cargohub.product_service.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReadProductDetailResultV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        Integer stockQuantity,
        BigDecimal price,
        boolean sellable,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        UUID updatedBy,
        LocalDateTime deletedAt,
        UUID deletedBy
) {
    public static ReadProductDetailResultV1 from(Product product) {
        return new ReadProductDetailResultV1(
                product.getId(),
                product.getName(),
                product.getFirmId().getId(),
                product.getHubId().getId(),
                product.getStockQuantity(),
                product.getPrice(),
                product.getSellable(),
                product.getCreatedAt(),
                product.getCreatedBy(),
                product.getUpdatedAt(),
                product.getUpdatedBy(),
                product.getDeletedAt(),
                product.getDeletedBy()
        );
    }
}
