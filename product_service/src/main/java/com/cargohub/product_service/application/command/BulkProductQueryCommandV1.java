package com.cargohub.product_service.application.command;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record BulkProductQueryCommandV1(
        List<UUID> ids
) {
}
