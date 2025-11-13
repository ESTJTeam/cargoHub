package hub_server.hub_server.application.dto.query;

import hub_server.hub_server.domain.entity.HubInfo;

import java.util.UUID;

/**
 * 허브 연결 정보 응답 DTO
 */
public record HubInfoResponseDto(
        UUID id,
        UUID startHubId,
        String startHubName,
        UUID endHubId,
        String endHubName,
        Integer deliveryDuration,  // 분 단위
        Double distance             // km 단위
) {
    public static HubInfoResponseDto from(HubInfo hubInfo) {
        return new HubInfoResponseDto(
                hubInfo.getId(),
                hubInfo.getStartHub().getId(),
                hubInfo.getStartHub().getName(),
                hubInfo.getEndHub().getId(),
                hubInfo.getEndHub().getName(),
                hubInfo.getDeliveryDuration(),
                hubInfo.getDistance()
        );
    }
}
