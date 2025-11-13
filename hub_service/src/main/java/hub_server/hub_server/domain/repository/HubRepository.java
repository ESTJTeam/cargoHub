package hub_server.hub_server.domain.repository;

import hub_server.hub_server.application.dto.query.HubSearchCondition;
import hub_server.hub_server.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findById(UUID id);

    Optional<Hub> findByIdWithAddress(UUID id);

    boolean existsById(UUID id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    List<Hub> findAll();

    List<Hub> findAllById(Iterable<UUID> ids);

    Page<Hub> search(HubSearchCondition condition, Pageable pageable);

    Page<Hub> findAllWithAddress(Pageable pageable);

    List<Hub> findByHubManagerId(UUID hubManagerId);

    Page<Hub> findByHubManagerIdWithPaging(UUID hubManagerId, Pageable pageable);
}
