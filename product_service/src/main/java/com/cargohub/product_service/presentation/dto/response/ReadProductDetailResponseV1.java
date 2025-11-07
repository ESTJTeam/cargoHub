package com.cargohub.product_service.presentation.dto.response;

import com.cargohub.product_service.application.dto.ReadProductDetailResultV1;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadProductDetailResponseV1(
     UUID id,
     String name,
     UUID firmId,
//     String firmName,
     UUID hubId,
//     String hubName,
     Integer stockQuantity,
     Integer price,
     boolean sellable,
     LocalDateTime createdAt,
     UUID createdBy,
     LocalDateTime updatedAt,
     UUID updatedBy,
     LocalDateTime deletedAt,
     UUID deletedBy
) {
    public static ReadProductDetailResponseV1 from(ReadProductDetailResultV1 result) {
        return new ReadProductDetailResponseV1(
                result.id(),
                result.name(),
                result.firmId(),
                result.hubId(),
                result.stockQuantity(),
                result.price(),
                result.sellable(),
                result.createdAt(),
                result.createdBy(),
                result.updatedAt(),
                result.updatedBy(),
                result.deletedAt(),
                result.deletedBy()
        );
    }

}
