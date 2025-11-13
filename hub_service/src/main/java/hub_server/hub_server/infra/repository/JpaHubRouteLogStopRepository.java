package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubRouteLogStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JpaHubRouteLogStopRepository extends JpaRepository<HubRouteLogStop, UUID> {

    @Modifying
    @Query("UPDATE HubRouteLogStop hrls " +
            "SET hrls.deletedAt = CURRENT_TIMESTAMP, hrls.deletedBy = :deletedBy " +
            "WHERE hrls.hub.id = :hubId " +
            "AND hrls.deletedAt IS NULL")
    void softDeleteByHubId(
            @Param("hubId") UUID hubId,
            @Param("deletedBy") UUID deletedBy
    );
}
