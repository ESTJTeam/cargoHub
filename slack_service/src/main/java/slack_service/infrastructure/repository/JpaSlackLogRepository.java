package slack_service.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slack_service.domain.entity.SlackLog;

@Repository
public interface JpaSlackLogRepository extends JpaRepository<SlackLog, UUID> {

}
