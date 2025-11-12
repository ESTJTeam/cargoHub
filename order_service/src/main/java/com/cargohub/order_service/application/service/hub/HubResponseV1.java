package com.cargohub.order_service.application.service.hub;

import java.util.UUID;

public record HubResponseV1(
        UUID hubId,
        String name,
        UUID hubManagerId
) {
}
