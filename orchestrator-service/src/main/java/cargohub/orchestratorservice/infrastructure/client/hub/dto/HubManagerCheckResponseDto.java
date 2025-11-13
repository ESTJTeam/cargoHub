package cargohub.orchestratorservice.infrastructure.client.hub.dto;

import java.util.UUID;

public record HubManagerCheckResponseDto(
        UUID hubId,
        boolean isManager
) {
}
