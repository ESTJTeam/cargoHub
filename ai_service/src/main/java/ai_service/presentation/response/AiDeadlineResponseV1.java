package ai_service.presentation.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiDeadlineResponseV1 {

    private UUID aiLogId;
    private String orderInfo;
    private LocalDateTime finalDeadline;
    private String slackFormattedText;
}
