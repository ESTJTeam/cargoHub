package com.cargohub.firm_service.infrastructure.repository;

import com.cargohub.firm_service.domain.entity.Firm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FirmJpaRepository extends JpaRepository<Firm, UUID> {
}
