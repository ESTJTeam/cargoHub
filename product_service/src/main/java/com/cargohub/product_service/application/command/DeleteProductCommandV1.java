package com.cargohub.product_service.application.command;

import java.util.UUID;

public record DeleteProductCommandV1(
        UUID id,
        UUID deletedBy
) {
}
