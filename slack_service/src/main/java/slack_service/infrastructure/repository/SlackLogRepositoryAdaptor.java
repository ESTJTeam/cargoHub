package slack_service.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import slack_service.domain.entity.SlackLog;
import slack_service.domain.repository.SlackLogRepository;

@Component
@RequiredArgsConstructor
public class SlackLogRepositoryAdaptor implements SlackLogRepository {

    private final JpaSlackLogRepository jpaSlackLogRepository;

    @Override
    public void save(SlackLog slackLog) {

        jpaSlackLogRepository.save(slackLog);
    }

    @Override
    public Optional<SlackLog> findById(UUID slackLogId) {

        return jpaSlackLogRepository.findById(slackLogId);
    }
}
