package com.cargohub.delivery_service.infrastructure.client.hub.dto;

import java.util.UUID;

public record HubManagerCheckResponseDto(
        boolean isManager
) {
}
