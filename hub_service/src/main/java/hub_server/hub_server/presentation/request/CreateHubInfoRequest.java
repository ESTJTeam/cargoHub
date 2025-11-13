package hub_server.hub_server.presentation.request;

import hub_server.hub_server.application.dto.command.CreateHubInfoCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * 허브 연결 정보 생성 요청
 */
public record CreateHubInfoRequest(
        @NotNull(message = "출발 허브 ID는 필수입니다")
        UUID startHubId,

        @NotNull(message = "도착 허브 ID는 필수입니다")
        UUID endHubId,

        @NotNull(message = "배송 소요 시간은 필수입니다")
        @Positive(message = "배송 소요 시간은 양수여야 합니다")
        Integer deliveryDuration,  // 분 단위

        @NotNull(message = "거리는 필수입니다")
        @Positive(message = "거리는 양수여야 합니다")
        Double distance             // km 단위
) {
    public CreateHubInfoCommand toCommand() {
        return new CreateHubInfoCommand(
                startHubId,
                endHubId,
                deliveryDuration,
                distance
        );
    }
}
