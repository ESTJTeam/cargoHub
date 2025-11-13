package com.cargohub.order_service.application.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommandV1(
    UUID supplierId,
    UUID receiverId,
    List<OrderProductInfo> products,
    String requestNote,
    UserInfo user
) {
    public record OrderProductInfo(
        UUID id,
        String name,
        Integer quantity,
        BigDecimal price
    ){}
}
