package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {
}
