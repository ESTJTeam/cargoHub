package hub_server.hub_server.infra.repository;

import hub_server.hub_server.application.dto.query.HubSearchCondition;
import hub_server.hub_server.domain.entity.Hub;
import hub_server.hub_server.domain.repository.HubRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryAdaptor implements HubRepository {

    private final JpaHubRepository jpaHubRepository;
    private final EntityManager entityManager;

    @Override
    public Hub save(Hub hub) {
        return jpaHubRepository.save(hub);
    }

    @Override
    public Optional<Hub> findById(UUID id) {
        return jpaHubRepository.findById(id)
                .filter(hub -> hub.getDeletedAt() == null);
    }

    @Override
    public Optional<Hub> findByIdWithAddress(UUID id) {
        return jpaHubRepository.findByIdWithAddress(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaHubRepository.existsByNameAndDeletedAtIsNull(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, UUID id) {
        return jpaHubRepository.existsByNameAndIdNotAndDeletedAtIsNull(name, id);
    }

    @Override
    public Page<Hub> search(HubSearchCondition condition, Pageable pageable) {
        // 동적 쿼리 생성
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT h FROM Hub h LEFT JOIN FETCH h.hubAddress a WHERE h.deletedAt IS NULL");
        List<String> conditions = new ArrayList<>();

        if (condition.getName() != null && !condition.getName().isBlank()) {
            conditions.add("h.name LIKE :name");
        }
        if (condition.getRegion() != null && !condition.getRegion().isBlank()) {
            conditions.add("a.region LIKE :region");
        }
        if (condition.getCity() != null && !condition.getCity().isBlank()) {
            conditions.add("a.city LIKE :city");
        }

        if (!conditions.isEmpty()) {
            jpql.append(" AND ").append(String.join(" AND ", conditions));
        }

        // 정렬 추가
        String sortBy = condition.getSortBy() != null ? condition.getSortBy() : "createdAt";
        String sortDirection = condition.getSortDirection() != null && condition.getSortDirection().equalsIgnoreCase("asc") ? "ASC" : "DESC";
        jpql.append(" ORDER BY h.").append(sortBy).append(" ").append(sortDirection);

        TypedQuery<Hub> query = entityManager.createQuery(jpql.toString(), Hub.class);

        // 파라미터 바인딩
        if (condition.getName() != null && !condition.getName().isBlank()) {
            query.setParameter("name", "%" + condition.getName() + "%");
        }
        if (condition.getRegion() != null && !condition.getRegion().isBlank()) {
            query.setParameter("region", "%" + condition.getRegion() + "%");
        }
        if (condition.getCity() != null && !condition.getCity().isBlank()) {
            query.setParameter("city", "%" + condition.getCity() + "%");
        }

        // 페이징 처리
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Hub> hubs = query.getResultList();

        // 전체 개수 조회
        long total = countSearch(condition);

        return new PageImpl<>(hubs, pageable, total);
    }

    private long countSearch(HubSearchCondition condition) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(DISTINCT h) FROM Hub h LEFT JOIN h.hubAddress a WHERE h.deletedAt IS NULL");
        List<String> conditions = new ArrayList<>();

        if (condition.getName() != null && !condition.getName().isBlank()) {
            conditions.add("h.name LIKE :name");
        }
        if (condition.getRegion() != null && !condition.getRegion().isBlank()) {
            conditions.add("a.region LIKE :region");
        }
        if (condition.getCity() != null && !condition.getCity().isBlank()) {
            conditions.add("a.city LIKE :city");
        }

        if (!conditions.isEmpty()) {
            jpql.append(" AND ").append(String.join(" AND ", conditions));
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);

        if (condition.getName() != null && !condition.getName().isBlank()) {
            query.setParameter("name", "%" + condition.getName() + "%");
        }
        if (condition.getRegion() != null && !condition.getRegion().isBlank()) {
            query.setParameter("region", "%" + condition.getRegion() + "%");
        }
        if (condition.getCity() != null && !condition.getCity().isBlank()) {
            query.setParameter("city", "%" + condition.getCity() + "%");
        }

        return query.getSingleResult();
    }

    @Override
    public Page<Hub> findAllWithAddress(Pageable pageable) {
        return jpaHubRepository.findAllWithAddress(pageable);
    }
}
