package com.cargohub.product_service.presentation.dto.request;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record BulkProductQueryRequestV1(
        List<UUID> ids
) {
}
