package com.cargohub.order_service.presentation.dto.request;

import com.cargohub.order_service.application.dto.FirmInfoResultV1;

import java.util.UUID;

public record FirmInfoResponseV1(
        UUID id,
        String name,
        String address
) {
    public static FirmInfoResponseV1 from(FirmInfoResultV1 result) {
        return new FirmInfoResponseV1(
                result.id(),
                result.name(),
                result.address()
        );
    }
}
