package ai_service.presentation.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiDeadlineResponseV1 {

    private UUID aiLogId;
    private String orderInfo;
    private LocalDateTime finalDeadline;
//    private String slackFormattedText;

    @Builder
    public AiDeadlineResponseV1(
        UUID aiLogId,
        String orderInfo,
        LocalDateTime finalDeadline
//        String slackFormattedText
    ) {
        this.aiLogId = aiLogId;
        this.orderInfo = orderInfo;
        this.finalDeadline = finalDeadline;
//        this.slackFormattedText = slackFormattedText;
    }
}

/* TODO
 * 주석 처리된 미사용 코드 제거
 */