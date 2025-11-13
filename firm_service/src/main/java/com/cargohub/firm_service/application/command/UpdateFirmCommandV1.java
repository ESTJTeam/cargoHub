package com.cargohub.firm_service.application.command;

import com.cargohub.firm_service.domain.entity.FirmAddress;

import java.util.UUID;

public record UpdateFirmCommandV1(
        UUID firmId,
        String name,
        String type,
        UUID hubId,
        UUID userId,
        FirmAddress address
) {}
