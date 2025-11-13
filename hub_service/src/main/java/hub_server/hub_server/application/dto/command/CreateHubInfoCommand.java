package hub_server.hub_server.application.dto.command;

import java.util.UUID;

/**
 * 허브 연결 정보 생성 커맨드
 */
public record CreateHubInfoCommand(
        UUID startHubId,
        UUID endHubId,
        Integer deliveryDuration,  // 분 단위
        Double distance             // km 단위
) {
}
