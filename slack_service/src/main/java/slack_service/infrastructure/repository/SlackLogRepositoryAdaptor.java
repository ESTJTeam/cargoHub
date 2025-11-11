package slack_service.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<SlackLog> findAllActive(Pageable pageable) {

        return jpaSlackLogRepository.findAllActive(pageable);
    }

    public Page<SlackLog> searchByMessageWithPaging(String receiverSlackId, String keyword,
        Pageable pageable) {

        return jpaSlackLogRepository.searchByMessageWithPaging(receiverSlackId, keyword, pageable);
    }
}
