package com.cargohub.order_service.application.dto;

import com.cargohub.order_service.domain.entity.OrderProduct;

import java.util.UUID;

public record OrderProductResultV1(
        UUID productId,
        String name,
        Integer quantity,
        Integer productPrice
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
