package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaHubInfoRepository extends JpaRepository<HubInfo, UUID> {

    @Query("SELECT hi FROM HubInfo hi WHERE hi.deletedAt IS NULL")
    List<HubInfo> findAllActive();

    @Query("SELECT hi FROM HubInfo hi " +
            "WHERE hi.startHub.id = :startHubId " +
            "AND hi.endHub.id = :endHubId " +
            "AND hi.deletedAt IS NULL")
    Optional<HubInfo> findByStartAndEndHub(
            @Param("startHubId") UUID startHubId,
            @Param("endHubId") UUID endHubId
    );

    @Query("SELECT hi FROM HubInfo hi " +
            "WHERE (hi.startHub.id = :hubId OR hi.endHub.id = :hubId) " +
            "AND hi.deletedAt IS NULL")
    List<HubInfo> findAllByHubId(@Param("hubId") UUID hubId);

    @Query("SELECT CASE WHEN COUNT(hi) > 0 THEN TRUE ELSE FALSE END " +
            "FROM HubInfo hi " +
            "WHERE ((hi.startHub.id = :hubId1 AND hi.endHub.id = :hubId2) " +
            "OR (hi.startHub.id = :hubId2 AND hi.endHub.id = :hubId1)) " +
            "AND hi.deletedAt IS NULL")
    boolean existsConnection(
            @Param("hubId1") UUID hubId1,
            @Param("hubId2") UUID hubId2
    );
}
