package cargohub.orchestratorservice.infrastructure.client.hub.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record HubResponseV1(
        UUID id,
        String name,
        UUID hubManagerId,
        HubAddressDto address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record HubAddressDto(
            String postalCode,
            String country,
            String region,
            String city,
            String district,
            String roadName,
            String buildingName,
            String detailAddress,
            String fullAddress,
            BigDecimal latitude,
            BigDecimal longitude
    ) {}
}