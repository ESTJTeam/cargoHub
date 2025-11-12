package com.cargohub.product_service.application.service.hub;

import java.util.UUID;

public record HubResponseV1(
        UUID hubId,
        String name,
        UUID hubManagerId
) {
}
