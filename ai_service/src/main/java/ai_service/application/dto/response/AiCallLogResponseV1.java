package ai_service.application.dto.response;

import ai_service.domain.entity.AiCallLog;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiCallLogResponseV1 {

    private UUID id;
    private String outputText;
    private LocalDateTime createdAt;

    public static AiCallLogResponseV1 from(AiCallLog aiCallLog) {
        return AiCallLogResponseV1.builder()
            .id(aiCallLog.getId())
            .outputText(aiCallLog.getOutputText())
            .createdAt(aiCallLog.getCreatedAt())
            .build();
    }
}
