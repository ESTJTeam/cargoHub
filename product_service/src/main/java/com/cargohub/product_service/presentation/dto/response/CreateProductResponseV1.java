package com.cargohub.product_service.presentation.dto.response;



import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductResponseV1(
        UUID id,
        String name,
        UUID firmId,
        String firmName,
        UUID hubId,
        String hubName,
        Integer stockQuantity,
        Integer price,
        boolean sellable,
        LocalDateTime createdAt
) {

}
