package com.cargohub.firm_service.presentation.dto.response;

import java.util.List;
import java.util.UUID;

public record FirmListResponseV1(
        UUID hubId,
        int page,
        int size,
        long totalCount,
        List<FirmSummaryV1> firms
) {}
