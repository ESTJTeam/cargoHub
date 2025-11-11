package com.cargohub.product_service.application.command;

import com.cargohub.product_service.presentation.dto.request.UpdateProductStockRequestV1;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record UpdateProductStockCommandV1(
        Map<UUID, Integer> items
) {

    public static UpdateProductStockCommandV1 from(UpdateProductStockRequestV1 request) {

        Map<UUID, Integer> map = request.items().stream()
                .collect(Collectors.toMap(
                        UpdateProductStockRequestV1.StockUpdateItemRequest::id,
                        UpdateProductStockRequestV1.StockUpdateItemRequest::quantity
                ));

        return new UpdateProductStockCommandV1(map);
    }
}
