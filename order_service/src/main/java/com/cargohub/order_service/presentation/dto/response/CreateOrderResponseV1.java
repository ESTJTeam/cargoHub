package com.cargohub.order_service.presentation.dto.response;

import com.cargohub.order_service.application.dto.CreateOrderResultV1;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderResponseV1(
        UUID id,
        UUID supplierId,
        UUID receiverId,
        List<OrderProductResponseV1> products,
        String requestNote,
        LocalDateTime createdAt
) {
    public static CreateOrderResponseV1 from(CreateOrderResultV1 result) {
        List<OrderProductResponseV1> responseProducts = result.products().stream()
                .map(orderProductResult -> new OrderProductResponseV1(
                        orderProductResult.productId(),
                        orderProductResult.name(),
                        orderProductResult.quantity(),
                        orderProductResult.productPrice()
                ))
                .toList();

        return new CreateOrderResponseV1(
                result.id(),
                result.supplierId(),
                result.receiverId(),
                responseProducts,
                result.requestNote(),
                result.createdAt()
        );
    }
}
