package slack_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import slack_service.domain.entity.SlackLog;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SlackLogResponseV1 {

    private UUID id;
    private String receiverSlackId;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    private SlackLogResponseV1(UUID id, String receiverSlackId, String message,
        LocalDateTime createdAt) {

        this.id = id;
        this.receiverSlackId = receiverSlackId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static SlackLogResponseV1 from(SlackLog log) {

        return SlackLogResponseV1.builder()
            .id(log.getId())
            .receiverSlackId(log.getReceiverSlackId())
            .message(log.getMessage())
            .createdAt(log.getCreatedAt())
            .build();
    }
}
