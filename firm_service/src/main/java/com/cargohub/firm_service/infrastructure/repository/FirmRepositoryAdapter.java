package com.cargohub.firm_service.infrastructure.repository;

import com.cargohub.firm_service.domain.entity.Firm;
import com.cargohub.firm_service.domain.repository.FirmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FirmRepositoryAdapter implements FirmRepository {

    private final FirmJpaRepository jpa;

    @Override
    public Firm save(Firm firm) {
        return jpa.save(firm);
    }

    @Override
    public Optional<Firm> findById(UUID id) {
        return jpa.findById(id);
    }
}
