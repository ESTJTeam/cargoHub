package com.cargohub.delivery_service.infrastructure.client.firm.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FirmListResponseV1(
        UUID hubId,
        int page,
        int size,
        long totalCount,
        List<FirmResponse> firms

) {
    public record FirmResponse(
            UUID id,
            String name,
            String type,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            AddressResponseV1 address
    ) {}

    public record AddressResponseV1(
            UUID id,
            String fullAddress,
            String city,
            String district
    ) {}
}
