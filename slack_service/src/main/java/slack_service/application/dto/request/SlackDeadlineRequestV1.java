package slack_service.application.dto.request;

import java.time.LocalDateTime;
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
    private String receiverSlackId;

    // AI로 만든 전체 Text (있으면 그대로 전송)
    private String slackFormattedText;

    // AI로 만든 전체 Text 없을 때 사용할 최소 정보 (Fallback 용도)
    private String orderInfo;
    private LocalDateTime finalDeadline;

    // 추적 편의 위한 로그 id
    private String aiLogId;

    @Builder
    private SlackDeadlineRequestV1(
        String receiverSlackId,
        String slackFormattedText,
        String orderInfo,
        LocalDateTime finalDeadline,
        String aiLogId
    ) {
        this.receiverSlackId = receiverSlackId;
        this.slackFormattedText = slackFormattedText;
        this.orderInfo = orderInfo;
        this.finalDeadline = finalDeadline;
        this.aiLogId = aiLogId;
    }
}
