package com.cargohub.firm_service.domain.repository;

import com.cargohub.firm_service.domain.entity.Firm;

import java.util.Optional;
import java.util.UUID;

public interface FirmRepository {

    Firm save(Firm firm);

    Optional<Firm> findById(UUID id);
}
