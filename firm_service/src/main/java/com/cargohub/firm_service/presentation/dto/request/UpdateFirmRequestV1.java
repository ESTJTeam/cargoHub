package com.cargohub.firm_service.presentation.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateFirmRequestV1(

        String name,
        String type,
        UUID hubId,
        AddressRequestV1 address
) {
    public record AddressRequestV1(

            String postalCode,
            String country,
            String region,
            String city,
            String district,
            String roadName,
            String buildingName,
            String detailAddress,
            BigDecimal latitude,
            BigDecimal longitude
    ) {}
}
