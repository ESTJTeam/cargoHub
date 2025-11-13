package cargohub.orchestratorservice.infrastructure.client.product.dto;

import java.util.List;
import java.util.UUID;

public record BulkProductQueryRequestV1(
        List<UUID> ids
) {
}
