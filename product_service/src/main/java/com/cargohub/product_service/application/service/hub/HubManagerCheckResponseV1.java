package com.cargohub.product_service.application.service.hub;

import java.util.UUID;

public record HubManagerCheckResponseV1(
        UUID hubId,
        boolean isManager
) {
}
