package com.cargohub.product_service.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductCommandV1(
        UUID id,
        String name,
        Integer stockQuantity,
        BigDecimal price,
        Boolean sellable,
        UUID updatedBy
) {
}
