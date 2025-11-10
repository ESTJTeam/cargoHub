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
public class SlackLogListResponseV1 {

    private UUID id;
    private String receiverSlackId;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    private SlackLogListResponseV1(UUID id, String receiverSlackId, String message,
        LocalDateTime createdAt) {

        this.id = id;
        this.receiverSlackId = receiverSlackId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static SlackLogListResponseV1 from(SlackLog entity) {
        return SlackLogListResponseV1.builder()
            .id(entity.getId())
            .receiverSlackId(entity.getReceiverSlackId())
            .message(entity.getMessage())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
