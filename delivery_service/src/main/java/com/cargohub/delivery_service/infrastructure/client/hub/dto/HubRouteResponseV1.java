package com.cargohub.delivery_service.infrastructure.client.hub.dto;

import java.util.List;
import java.util.UUID;

public record HubRouteResponseV1(
    UUID departureHubId,
    String departureHubName,
    UUID arrivalHubId,
    String arrivalHubName,
    Integer deliveryDuration,  // 총 소요 시간 (분)
    Integer distance,           // 총 거리 (km) - Integer로 변환
    List<HubRouteStopDto> hubRoutes  // 경유지 목록
) {
    /**
     * 경유지 정보 DTO
     */
    public record HubRouteStopDto(
        Integer sequenceNum,
        String hubName,
        UUID hubId
    ) {}
}
