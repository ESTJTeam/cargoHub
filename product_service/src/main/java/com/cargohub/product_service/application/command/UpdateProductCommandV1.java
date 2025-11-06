package com.cargohub.product_service.application.command;

import java.util.UUID;

public record UpdateProductCommandV1(
        UUID id,
        String name,
        Integer stockQuantity,
        Integer price,
        Boolean sellable,
        UUID updatedBy
) {
}
