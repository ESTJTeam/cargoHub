package com.cargohub.firm_service.domain.repository;

import com.cargohub.firm_service.domain.entity.Firm;
import com.cargohub.firm_service.domain.vo.HubId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface FirmRepository {

    Firm save(Firm firm);

    Optional<Firm> findById(UUID id);

    Page<Firm> findByHubId(HubId hubId, Pageable pageable);

}
