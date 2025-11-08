package com.cargohub.product_service.presentation.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequestV1(
        String name,

        @PositiveOrZero(message = "재고 수량은 0개 이상이어야 합니다.")
        Integer stockQuantity,

        @PositiveOrZero( message = "가격은 0원 이상이어야 합니다.")
        BigDecimal price,

        Boolean sellable
) {
}
