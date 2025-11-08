package com.cargohub.delivery_service.infrastructure.repository;

import com.cargohub.delivery_service.domain.entity.DeliveryStatus;
import com.cargohub.delivery_service.domain.entity.FirmDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FirmDeliveryJpaRepository extends JpaRepository<FirmDelivery, UUID> {

    List<FirmDelivery> findByOrderId(UUID orderId);

    List<FirmDelivery> findByDeliveryStatus(DeliveryStatus status);
}
