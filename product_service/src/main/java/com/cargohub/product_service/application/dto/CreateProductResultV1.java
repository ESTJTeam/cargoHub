package com.cargohub.product_service.application.dto;

import com.cargohub.product_service.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductResultV1 (
        UUID id,
        String name,
        UUID firmId,
        String firmName,
        UUID hubId,
        String hubName,
        Integer stockQuantity,
        BigDecimal price,
        boolean sellable,
        LocalDateTime createdAt
) {
    public static CreateProductResultV1 from(Product product, String firmName, String hubName) {
        return new CreateProductResultV1(
                product.getId(),
                product.getName(),
                product.getFirmId().getId(),
                firmName,
                product.getHubId().getId(),
                hubName,
                product.getStockQuantity(),
                product.getPrice(),
                product.getSellable(),
                product.getCreatedAt()
        );
    }
}
