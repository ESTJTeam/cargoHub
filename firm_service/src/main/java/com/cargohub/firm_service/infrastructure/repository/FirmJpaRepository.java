package com.cargohub.firm_service.infrastructure.repository;

import com.cargohub.firm_service.domain.entity.Firm;
import com.cargohub.firm_service.domain.vo.HubId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface FirmJpaRepository extends JpaRepository<Firm, UUID> {

    Page<Firm> findByHubId_HubId(UUID hubId, Pageable pageable);
}
