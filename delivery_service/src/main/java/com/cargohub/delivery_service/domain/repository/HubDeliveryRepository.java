package com.cargohub.delivery_service.domain.repository;

import com.cargohub.delivery_service.domain.entity.HubDelivery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubDeliveryRepository {

    HubDelivery save(HubDelivery delivery);

    Optional<HubDelivery> findById(UUID id);

    List<HubDelivery> findByOrderId(UUID orderId);

    List<HubDelivery> findByStartHubId(UUID startHubId);

    List<HubDelivery> findByEndHubId(UUID endHubId);
}
