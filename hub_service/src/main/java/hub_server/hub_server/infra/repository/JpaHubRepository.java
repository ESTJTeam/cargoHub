package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

    @Query("SELECT h FROM Hub h LEFT JOIN FETCH h.hubAddress WHERE h.id = :id AND h.deletedAt IS NULL")
    Optional<Hub> findByIdWithAddress(@Param("id") UUID id);

    boolean existsByNameAndDeletedAtIsNull(String name);

    boolean existsByNameAndIdNotAndDeletedAtIsNull(String name, UUID id);

    @Query("SELECT DISTINCT h FROM Hub h LEFT JOIN FETCH h.hubAddress WHERE h.deletedAt IS NULL")
    Page<Hub> findAllWithAddress(Pageable pageable);

    @Query("SELECT h FROM Hub h WHERE h.deletedAt IS NULL")
    List<Hub> findAllActive();

    boolean existsByIdAndDeletedAtIsNull(UUID id);

    @Query("SELECT h FROM Hub h WHERE h.hubManagerId = :hubManagerId AND h.deletedAt IS NULL")
    List<Hub> findByHubManagerIdAndDeletedAtIsNull(@Param("hubManagerId") UUID hubManagerId);

    @Query("SELECT h FROM Hub h LEFT JOIN FETCH h.hubAddress WHERE h.hubManagerId = :hubManagerId AND h.deletedAt IS NULL")
    Page<Hub> findByHubManagerIdWithAddress(@Param("hubManagerId") UUID hubManagerId, Pageable pageable);
}
