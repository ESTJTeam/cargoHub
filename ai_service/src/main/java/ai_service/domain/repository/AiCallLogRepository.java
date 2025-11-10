package ai_service.domain.repository;

import ai_service.domain.entity.AiCallLog;
import java.util.Optional;
import java.util.UUID;

public interface AiCallLogRepository {

    void save(AiCallLog aiCallLog);

    Optional<AiCallLog> findById(UUID aiLogId);

}
