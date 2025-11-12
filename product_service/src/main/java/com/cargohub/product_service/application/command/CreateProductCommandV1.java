package com.cargohub.product_service.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductCommandV1(
        String name,
        UUID firmId,
        UUID hubId,
        Integer stockQuantity,
        BigDecimal price,
        Boolean sellable
//        UserInfo user
) {
}
