package hub_server.hub_server.application.dto.query;

import hub_server.hub_server.domain.entity.HubRouteLog;

import java.util.List;
import java.util.UUID;

/**
 * 허브 간 경로 정보 응답 DTO
 */
public record HubRouteResponseDto(
        UUID departureHubId,
        String departureHubName,
        UUID arrivalHubId,
        String arrivalHubName,
        Integer deliveryDuration,  // 총 소요 시간 (분)
        Integer distance,           // 총 거리 (km) - Integer로 변환
        List<HubRouteStopDto> hubRoutes  // 경유지 목록
) {
    public static HubRouteResponseDto from(HubRouteLog routeLog) {
        return new HubRouteResponseDto(
                routeLog.getStartHub().getId(),
                routeLog.getStartHub().getName(),
                routeLog.getEndHub().getId(),
                routeLog.getEndHub().getName(),
                routeLog.getTotalDuration(),
                routeLog.getTotalDistance().intValue(),  // Double -> Integer 변환
                routeLog.getStops().stream()
                        .map(HubRouteStopDto::from)
                        .toList()
        );
    }

    /**
     * 경유지 정보 DTO
     */
    public record HubRouteStopDto(
            Integer sequenceNum,
            String hubName,
            UUID hubId
    ) {
        public static HubRouteStopDto from(hub_server.hub_server.domain.entity.HubRouteLogStop stop) {
            return new HubRouteStopDto(
                    stop.getSequenceOrder(),
                    stop.getHub().getName(),
                    stop.getHub().getId()
            );
        }
    }
}
