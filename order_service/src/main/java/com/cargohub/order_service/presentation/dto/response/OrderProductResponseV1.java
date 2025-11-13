package com.cargohub.order_service.presentation.dto.response;

import com.cargohub.order_service.application.dto.OrderProductResultV1;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProductResponseV1(
        UUID productId,
        String name,
        Integer quantity,
        BigDecimal productPrice
) {
    public static OrderProductResponseV1 from(OrderProductResultV1 result) {
        return new OrderProductResponseV1(
                result.id(),
                result.name(),
                result.quantity(),
                result.price()
        );
    }
}
