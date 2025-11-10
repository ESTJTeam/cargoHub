package slack_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import slack_service.domain.entity.SlackLog;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlackLogResponseV1 {

    private UUID id;
    private String receiverSlackId;
    private String message;
    private LocalDateTime createdAt;

    public static SlackLogResponseV1 from(SlackLog log) {

        return SlackLogResponseV1.builder()
            .id(log.getId())
            .receiverSlackId(log.getReceiverSlackId())
            .message(log.getMessage())
            .createdAt(log.getCreatedAt())
            .build();
    }
}
