package com.cargohub.order_service.application.command;

import java.util.UUID;

public record OrderProductCommandV1(
        UUID productId,
        Integer quantity
) {
}
