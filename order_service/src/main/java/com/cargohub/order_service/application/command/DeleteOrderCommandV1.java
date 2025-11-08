package com.cargohub.order_service.application.command;

import java.util.UUID;

public record DeleteOrderCommandV1(
        UUID id,
        UUID deletedBy
) {
}
