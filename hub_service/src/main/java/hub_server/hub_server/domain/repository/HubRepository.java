package hub_server.hub_server.domain.repository;

import hub_server.hub_server.application.dto.query.HubSearchCondition;
import hub_server.hub_server.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findById(UUID id);

    Optional<Hub> findByIdWithAddress(UUID id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    Page<Hub> search(HubSearchCondition condition, Pageable pageable);

    Page<Hub> findAllWithAddress(Pageable pageable);
}
