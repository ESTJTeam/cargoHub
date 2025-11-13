package hub_server.hub_server.application.dto.vo;

import java.util.List;
import java.util.UUID;

/**
 * 최단 경로 계산 결과
 */
public record ShortestPathResult(
        UUID startHubId,
        UUID endHubId,
        Integer totalDuration,  // 총 소요 시간 (분)
        Double totalDistance,    // 총 거리 (km)
        List<UUID> path          // 경로 (허브 ID 리스트, 출발지부터 도착지까지)
) {
}
