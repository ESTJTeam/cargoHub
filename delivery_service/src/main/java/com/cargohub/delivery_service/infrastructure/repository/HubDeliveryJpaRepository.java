package com.cargohub.delivery_service.infrastructure.repository;

import com.cargohub.delivery_service.domain.entity.HubDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HubDeliveryJpaRepository extends JpaRepository<HubDelivery, UUID> {

    List<HubDelivery> findByOrderId(UUID orderId);

    List<HubDelivery> findByStartHubId(UUID startHubId);

    List<HubDelivery> findByEndHubId(UUID endHubId);
}
