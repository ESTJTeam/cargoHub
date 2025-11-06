package com.cargohub.product_service.application.command;

import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record UpdateProductStockCommandV1(
        UUID id,
        Integer quantity
) {
}
