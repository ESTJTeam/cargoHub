package slack_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import slack_service.common.BaseEntity;

@Entity
@Getter
@Table(name = "p_slack_message_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SlackLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "slack_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "receiver_slack_id")
    private String receiverSlackId;

    @Column(name = "message")
    private String message;

    protected SlackLog(String receiverSlackId, String message) {

        this.receiverSlackId = receiverSlackId;
        this.message = message;
    }

    public static SlackLog of(String receiverSlackId, String message) {

        return new SlackLog(receiverSlackId, message);
    }
}
