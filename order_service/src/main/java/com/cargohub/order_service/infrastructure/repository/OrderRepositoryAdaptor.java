package com.cargohub.order_service.infrastructure.repository;

import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.repository.OrderRepository;
import com.cargohub.order_service.domain.vo.FirmDeliveryId;
import com.cargohub.order_service.domain.vo.HubDeliveryId;
import com.cargohub.order_service.domain.vo.ReceiverId;
import com.cargohub.order_service.domain.vo.SupplierId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdaptor implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaOrderRepository.findById(id);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return jpaOrderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> findAllBySupplierId(SupplierId supplierId, Pageable pageable) {
        return jpaOrderRepository.findAllBySupplierId(supplierId, pageable);
    }

    @Override
    public Page<Order> findAllByReceiverId(ReceiverId receiverId, Pageable pageable) {
        return jpaOrderRepository.findAllByReceiverId(receiverId, pageable);
    }

    @Override
    public Page<Order> findAllByHubDeliveryId(HubDeliveryId hubDeliveryId, Pageable pageable) {
        return jpaOrderRepository.findAllByHubDeliveryId(hubDeliveryId, pageable);
    }

    @Override
    public Page<Order> findAllByFirmDeliveryId(FirmDeliveryId firmDeliveryId, Pageable pageable) {
        return jpaOrderRepository.findAllByFirmDeliveryId(firmDeliveryId, pageable);
    }
}
