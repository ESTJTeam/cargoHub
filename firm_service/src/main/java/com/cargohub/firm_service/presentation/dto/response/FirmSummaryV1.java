package com.cargohub.firm_service.presentation.dto.response;

import com.cargohub.firm_service.domain.entity.Firm;
import com.cargohub.firm_service.domain.entity.FirmAddress;

import java.time.LocalDateTime;
import java.util.UUID;

public record FirmSummaryV1(
        UUID id,
        String name,
        com.cargohub.firm_service.domain.entity.FirmType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AddressSummaryV1 address
) {

    public static FirmSummaryV1 from(Firm firm) {
        return new FirmSummaryV1(
                firm.getId(),
                firm.getName(),
                firm.getType(),
                firm.getCreatedAt(),
                firm.getUpdatedAt(),
                AddressSummaryV1.from(firm.getAddress())
        );
    }

    public record AddressSummaryV1(
            String fullAddress,
            String city,
            String district
    ) {
        public static AddressSummaryV1 from(FirmAddress addr) {
            // fullAddress는 대략 이렇게 구성 (필요하면 조합 로직 바꿔도 됨)
            String fullAddress =
                    addr.getCity() + " " +
                            addr.getDistrict() + " " +
                            addr.getRoadName() + " " +
                            addr.getBuildingName() + " " +
                            (addr.getDetailAddress() != null ? addr.getDetailAddress() : "");

            return new AddressSummaryV1(
                    fullAddress.trim(),
                    addr.getCity(),
                    addr.getDistrict()
            );
        }
    }
}