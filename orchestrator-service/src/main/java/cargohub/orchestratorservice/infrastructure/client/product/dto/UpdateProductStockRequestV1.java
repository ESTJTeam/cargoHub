package cargohub.orchestratorservice.infrastructure.client.product.dto;

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