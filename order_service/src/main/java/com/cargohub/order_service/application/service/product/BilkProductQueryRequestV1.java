package com.cargohub.order_service.application.service.product;

import java.util.List;
import java.util.UUID;

public record BilkProductQueryRequestV1(
        List<UUID> ids
) {
}
