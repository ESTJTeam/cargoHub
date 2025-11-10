package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubAddressRepository extends JpaRepository<HubAddress, UUID> {
}
