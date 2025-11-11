package com.cargohub.product_service.application.command;

import com.cargohub.product_service.presentation.dto.request.CheckProductStockRequestV1;

import java.util.List;
import java.util.UUID;

public record CheckProductStockCommandV1(
        List<ProductStockItem> products
) {

    public static CheckProductStockCommandV1 from(CheckProductStockRequestV1 request) {
        List<ProductStockItem> items = request.products().stream()
                .map(item -> new ProductStockItem(item.id(), item.quantity()))
                .toList();

        return new CheckProductStockCommandV1(items);
    }

    public record ProductStockItem(UUID id, int quantity) {}
}
