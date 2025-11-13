package hub_server.hub_server.presentation.request;

import hub_server.hub_server.application.dto.command.UpdateHubInfoCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * 허브 연결 정보 수정 요청
 */
public record UpdateHubInfoRequest(
        @NotNull(message = "배송 소요 시간은 필수입니다")
        @Positive(message = "배송 소요 시간은 양수여야 합니다")
        Integer deliveryDuration,  // 분 단위

        @NotNull(message = "거리는 필수입니다")
        @Positive(message = "거리는 양수여야 합니다")
        Double distance             // km 단위
) {
    public UpdateHubInfoCommand toCommand(UUID hubInfoId) {
        return new UpdateHubInfoCommand(
                hubInfoId,
                deliveryDuration,
                distance
        );
    }
}
