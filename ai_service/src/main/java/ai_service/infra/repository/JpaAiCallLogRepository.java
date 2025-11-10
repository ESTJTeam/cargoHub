package ai_service.infra.repository;

import ai_service.domain.entity.AiCallLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAiCallLogRepository extends JpaRepository<AiCallLog, UUID> {

    // 커스텀 조건 필요할 경우 쿼리 사용
//    @Query
//    Optional<AiCallLog> findById(UUID aiLogId);

}
