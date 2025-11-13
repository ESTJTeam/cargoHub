package com.cargohub.order_service.application.service.product;

import java.util.List;
import java.util.UUID;

public record CheckProductStockRequestV1(
        List<ProductStockItem> products
) {
    public record ProductStockItem(UUID id, int quantity) {}
}
