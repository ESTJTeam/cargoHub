package com.cargohub.delivery_service.infrastructure.client.firm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FirmResponseV1(
        UUID id,
        UUID hubId,
        UUID userId,
        String name,
        String type,
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
