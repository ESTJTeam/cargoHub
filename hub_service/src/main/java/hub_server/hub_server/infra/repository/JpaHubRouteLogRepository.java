package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubRouteLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRouteLogRepository extends JpaRepository<HubRouteLog, UUID> {
}
