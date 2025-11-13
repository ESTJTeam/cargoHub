package cargohub.orchestratorservice.infrastructure.client.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record BulkProductQueryResponseV1(
        Map<UUID, ProductInfo> products
) {
    public record ProductInfo(
            UUID id,
            String name,
            UUID firmId,
            UUID hubId,
            Integer stockQuantity,
            BigDecimal price,
            boolean sellable,
            LocalDateTime createdAt
    ){}
}
