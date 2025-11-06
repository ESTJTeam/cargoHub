package ai_server.infra.repository;

import ai_server.domain.entity.AiCallLog;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAiCallLogRepository extends JpaRepository<AiCallLog, UUID> {

    // 커스텀 조건 필요할 경우 쿼리 사용
//    @Query
//    Optional<AiCallLog> findById(UUID aiLogId);

}
