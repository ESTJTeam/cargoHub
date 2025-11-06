package com.cargohub.product_service.presentation.dto.request;

import jakarta.validation.constraints.Positive;


public record UpdateProductStockRequestV1(
        @Positive(message = "수량은 0보다 커야 합니다.")
        Integer quantity
) {
}
