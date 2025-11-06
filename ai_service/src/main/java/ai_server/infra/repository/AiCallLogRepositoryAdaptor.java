package ai_server.infra.repository;

import ai_server.domain.entity.AiCallLog;
import ai_server.domain.repository.AiCallLogRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiCallLogRepositoryAdaptor implements AiCallLogRepository {

    private final JpaAiCallLogRepository jpaAiCallLogRepository;

    @Override
    public void save(AiCallLog aiCallLog) {

        jpaAiCallLogRepository.save(aiCallLog);
    }

    @Override
    public Optional<AiCallLog> findById(UUID aiLogId) {

        return jpaAiCallLogRepository.findById(aiLogId);
    }
}
