package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubRouteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaHubRouteLogRepository extends JpaRepository<HubRouteLog, UUID> {

    @Query("SELECT hrl FROM HubRouteLog hrl WHERE hrl.deletedAt IS NULL")
    List<HubRouteLog> findAllActive();

    @Query("SELECT hrl FROM HubRouteLog hrl " +
            "LEFT JOIN FETCH hrl.stops " +
            "WHERE hrl.startHub.id = :startHubId " +
            "AND hrl.endHub.id = :endHubId " +
            "AND hrl.deletedAt IS NULL")
    Optional<HubRouteLog> findByStartAndEndHubWithStops(
            @Param("startHubId") UUID startHubId,
            @Param("endHubId") UUID endHubId
    );

    @Query("SELECT hrl FROM HubRouteLog hrl " +
            "WHERE hrl.startHub.id = :startHubId " +
            "AND hrl.endHub.id = :endHubId " +
            "AND hrl.deletedAt IS NULL")
    Optional<HubRouteLog> findByStartAndEndHub(
            @Param("startHubId") UUID startHubId,
            @Param("endHubId") UUID endHubId
    );

    @Modifying
    @Query("UPDATE HubRouteLog hrl " +
            "SET hrl.deletedAt = CURRENT_TIMESTAMP, hrl.deletedBy = :deletedBy " +
            "WHERE (hrl.startHub.id = :hubId OR hrl.endHub.id = :hubId) " +
            "AND hrl.deletedAt IS NULL")
    void softDeleteByHubId(
            @Param("hubId") UUID hubId,
            @Param("deletedBy") UUID deletedBy
    );

    @Modifying
    @Query("UPDATE HubRouteLog hrl " +
            "SET hrl.deletedAt = CURRENT_TIMESTAMP, hrl.deletedBy = :deletedBy " +
            "WHERE hrl.deletedAt IS NULL")
    void softDeleteAll(@Param("deletedBy") UUID deletedBy);

    @Modifying
    @Query("UPDATE HubRouteLog hrl " +
        "SET hrl.deletedAt = CURRENT_TIMESTAMP " +
        "WHERE hrl.deletedAt IS NULL")
    void softDeleteAll();
}
