package cargohub.orchestratorservice.infrastructure.client.firm.dto;

import cargohub.orchestratorservice.domain.FirmType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FirmResponseV1(
        UUID id,
        UUID hubId,
        UUID userId,
        String name,
        FirmType type,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        UUID updatedBy,
        AddressDetailV1 address
) {
    public record AddressDetailV1(
            UUID id,
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
