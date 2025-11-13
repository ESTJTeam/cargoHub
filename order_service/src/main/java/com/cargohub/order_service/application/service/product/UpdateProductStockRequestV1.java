package com.cargohub.order_service.application.service.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;


public record UpdateProductStockRequestV1(
        List<StockUpdateItemRequest> items
) {
        public record StockUpdateItemRequest(
                UUID id,
                Integer quantity
        ) { }
}