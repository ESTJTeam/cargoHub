package com.cargohub.product_service.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequestV1(

        @NotBlank(message = "상품 명은 필수입니다.")
        String name,

        UUID firmId,

        UUID hubId,

        @PositiveOrZero(message = "재고 수량은 0개 이상이어야 합니다.")
        Integer stockQuantity,

        @PositiveOrZero( message = "가격은 0원 이상이어야 합니다.")
        BigDecimal price,

        Boolean sellable
) {
        public CreateProductRequestV1(String name, UUID firmId, UUID hubId, Integer stockQuantity, BigDecimal price) {
                this(name, firmId, hubId, stockQuantity, price, true);
        }
}
