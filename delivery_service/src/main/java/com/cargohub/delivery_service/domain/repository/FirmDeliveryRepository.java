package com.cargohub.delivery_service.domain.repository;

import com.cargohub.delivery_service.domain.entity.DeliveryStatus;
import com.cargohub.delivery_service.domain.entity.FirmDelivery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FirmDeliveryRepository {

    FirmDelivery save(FirmDelivery delivery);

    Optional<FirmDelivery> findById(UUID id);

    List<FirmDelivery> findByOrderId(UUID orderId);

    List<FirmDelivery> findByDeliveryStatus(DeliveryStatus status);
}
