package com.cargohub.product_service.application.command;

import java.util.UUID;

public record CreateProductCommandV1(
        String name,
        UUID firmId,
        UUID hubId,
        Integer stockQuantity,
        Integer price,
        Boolean sellable,
        UUID createdBy
) {
}
