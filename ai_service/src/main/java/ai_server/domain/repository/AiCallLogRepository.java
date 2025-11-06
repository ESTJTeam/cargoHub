package ai_server.domain.repository;

import ai_server.domain.entity.AiCallLog;
import java.util.Optional;
import java.util.UUID;

public interface AiCallLogRepository {

    void save(AiCallLog aiCallLog);

    Optional<AiCallLog> findById(UUID aiLogId);

}
