package com.cargohub.firm_service.application.command;

import com.cargohub.firm_service.domain.entity.FirmAddress;

import java.util.UUID;

public record CreateFirmCommandV1(
        String name,
        String type,
        UUID hubId,
        UUID userId,
        FirmAddress address
) { }
