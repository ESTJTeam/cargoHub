package com.cargohub.product_service.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;


public record UpdateProductStockRequestV1(

        @NotEmpty(message = "상품 목록은 비어 있을 수 없습니다.")
        List<@Valid StockUpdateItemRequest> items

) {

        public record StockUpdateItemRequest(

                @NotNull(message = "상품 ID는 필수입니다.")
                UUID id,

                @NotNull(message = "수량은 필수입니다.")
                @Positive(message = "수량은 0보다 커야 합니다.")
                Integer quantity

        ) { }
}