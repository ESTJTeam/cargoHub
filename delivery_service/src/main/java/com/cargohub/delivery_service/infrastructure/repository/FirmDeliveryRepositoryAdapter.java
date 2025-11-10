package com.cargohub.delivery_service.infrastructure.repository;

import com.cargohub.delivery_service.domain.entity.DeliveryStatus;
import com.cargohub.delivery_service.domain.entity.FirmDelivery;
import com.cargohub.delivery_service.domain.repository.FirmDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FirmDeliveryRepositoryAdapter implements FirmDeliveryRepository {

    private final FirmDeliveryJpaRepository jpa;

    @Override
    public FirmDelivery save(FirmDelivery delivery) {
        return jpa.save(delivery);
    }

    @Override
    public Optional<FirmDelivery> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public List<FirmDelivery> findByOrderId(UUID orderId) {
        return jpa.findByOrderId(orderId);
    }

    @Override
    public List<FirmDelivery> findByDeliveryStatus(DeliveryStatus status) {
        return jpa.findByDeliveryStatus(status);
    }
}
