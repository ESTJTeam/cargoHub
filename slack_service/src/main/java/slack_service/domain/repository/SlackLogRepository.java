package slack_service.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import slack_service.domain.entity.SlackLog;

public interface SlackLogRepository {

    void save(SlackLog slackLog);

    Optional<SlackLog> findById(UUID slackLogId);

    Page<SlackLog> findAllActive(Pageable pageable);

    Page<SlackLog> searchByMessageWithPaging(String receiverSlackId, String keyword, Pageable pageable);
}
