package slack_service.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import slack_service.domain.entity.SlackLog;

@Repository
public interface JpaSlackLogRepository extends JpaRepository<SlackLog, UUID> {

    // 전체(soft-delete 제외) 페이지네이션
    @Query("""
        select l
          from SlackLog l
         where l.deletedAt is null
        """)
    Page<SlackLog> findAllActive(Pageable pageable);

    // 수신자 + 키워드 검색 페이지네이션
    @Query("""
        select l
          from SlackLog l
         where l.deletedAt is null
           and (:receiverSlackId is null or l.receiverSlackId = :receiverSlackId)
           and (:keyword is null or lower(l.message) like lower(concat('%', :keyword, '%')))
        """)
    Page<SlackLog> searchByMessageWithPaging(
        @Param("receiverSlackId") String receiverSlackId,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}
