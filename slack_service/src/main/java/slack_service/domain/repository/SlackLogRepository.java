package slack_service.domain.repository;

import java.util.Optional;
import java.util.UUID;
import slack_service.domain.entity.SlackLog;

public interface SlackLogRepository {

    void save(SlackLog slackLog);

    Optional<SlackLog> findById(UUID slackLogId);
}
