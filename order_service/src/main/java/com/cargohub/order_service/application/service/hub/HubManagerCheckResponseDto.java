package com.cargohub.order_service.application.service.hub;

import java.util.UUID;

public record HubManagerCheckResponseDto(
        UUID hubId,
        boolean isManager
) {
}
