package com.cargohub.firm_service.presentation.dto.response;

import com.cargohub.firm_service.domain.entity.Firm;
import com.cargohub.firm_service.domain.entity.FirmAddress;
import com.cargohub.firm_service.domain.entity.FirmType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FirmDetailResponseV1(
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

    public static FirmDetailResponseV1 from(Firm firm) {
        FirmAddress addr = firm.getAddress();

        return new FirmDetailResponseV1(
                firm.getId(),
                firm.getHubId().getHubId(),   // VO → UUID
                firm.getUserId().getUserId(),
                firm.getName(),
                firm.getType(),
                firm.getCreatedAt(),
                firm.getCreatedBy(),
                firm.getUpdatedAt(),
                firm.getUpdatedBy(),
                AddressDetailV1.from(addr)
        );
    }

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
    ) {
        public static AddressDetailV1 from(FirmAddress addr) {
            String fullAddress =
                    addr.getRegion() + " " +
                            addr.getCity() + " " +
                            addr.getRoadName() + " " +
                            addr.getBuildingName() + " " +
                            (addr.getDetailAddress() != null ? addr.getDetailAddress() : "");

            return new AddressDetailV1(
                    addr.getId(),
                    addr.getPostalCode(),
                    addr.getCountry(),
                    addr.getRegion(),
                    addr.getCity(),
                    addr.getDistrict(),
                    addr.getRoadName(),
                    addr.getBuildingName(),
                    addr.getDetailAddress(),
                    fullAddress.trim(),
                    addr.getLatitude(),
                    addr.getLongitude()
            );
        }
    }
}
