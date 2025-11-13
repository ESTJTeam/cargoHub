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

    @Builder
    public AiDeadlineResponseV1(
        UUID aiLogId,
        String orderInfo,
        LocalDateTime finalDeadline
    ) {
        this.aiLogId = aiLogId;
        this.orderInfo = orderInfo;
        this.finalDeadline = finalDeadline;
    }
}
