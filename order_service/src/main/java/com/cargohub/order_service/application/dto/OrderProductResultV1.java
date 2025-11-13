package com.cargohub.order_service.application.dto;

import com.cargohub.order_service.domain.entity.OrderProduct;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProductResultV1(
        UUID id,
        String name,
        Integer quantity,
        BigDecimal price
) {

    public static OrderProductResultV1 from(OrderProduct orderProduct) {
        return new OrderProductResultV1(
                orderProduct.getProductId().getId(),
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getProductPrice()
        );
    }
}
