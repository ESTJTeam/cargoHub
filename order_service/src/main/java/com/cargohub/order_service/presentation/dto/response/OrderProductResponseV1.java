package com.cargohub.order_service.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProductResponseV1(
        UUID productId,
        String name,
        Integer quantity,
        BigDecimal productPrice
) {
}
