package com.cargohub.order_service.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderProductRequestV1(
        @NotNull(message = "상품 ID는 필수입니다.")
        UUID id,

        @NotNull(message = "수량은 필수입니다.")
        Integer quantity
) {
}
