package cargohub.orchestratorservice.infrastructure.client.slack.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SlackDeadlineRequestV1 {

    // DM을 보낼 대상 (userSlackId)
    @NotBlank(message = "receiverSlackId는 필수입니다.")
    private String receiverSlackId;

    // AI로 만든 전체 Text (있으면 그대로 전송)
//    private String slackFormattedText;

    private String orderInfo;

    private LocalDateTime finalDeadline;

    // 추적 편의 위한 로그 id
    private UUID aiLogId;

    @Builder
    private SlackDeadlineRequestV1(
        String receiverSlackId,
        String orderInfo,
        LocalDateTime finalDeadline,
        UUID aiLogId
    ) {
        this.receiverSlackId = receiverSlackId;
        this.orderInfo = orderInfo;
        this.finalDeadline = finalDeadline;
        this.aiLogId = aiLogId;
    }
}
