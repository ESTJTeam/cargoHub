package com.cargohub.delivery_service.infrastructure.repository;


import com.cargohub.delivery_service.domain.entity.HubDelivery;
import com.cargohub.delivery_service.domain.repository.HubDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubDeliveryRepositoryAdapter implements HubDeliveryRepository {

    private final HubDeliveryJpaRepository jpa;

    @Override
    public HubDelivery save(HubDelivery delivery) {
        return jpa.save(delivery);
    }

    @Override
    public Optional<HubDelivery> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public List<HubDelivery> findByOrderId(UUID orderId) {
        return jpa.findByOrderId(orderId);
    }

    @Override
    public List<HubDelivery> findByStartHubId(UUID startHubId) {
        return jpa.findByStartHubId(startHubId);
    }

    @Override
    public List<HubDelivery> findByEndHubId(UUID endHubId) {
        return jpa.findByEndHubId(endHubId);
    }
}
