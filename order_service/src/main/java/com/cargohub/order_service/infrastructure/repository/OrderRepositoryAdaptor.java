package com.cargohub.order_service.infrastructure.repository;

import com.cargohub.order_service.application.command.SearchOrderCommandV1;
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

import java.util.Collection;
import java.util.List;
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
    public Page<Order> findOrderPage(SearchOrderCommandV1 search, Pageable pageable) {
        return jpaOrderRepository.findOrderPage(search, pageable);
    }

    @Override
    public Page<Order> findOrderPageBySupplierId(SupplierId supplierId, SearchOrderCommandV1 search, Pageable pageable) {
        return jpaOrderRepository.findOrderPageBySupplierId(supplierId, search, pageable);
    }

    @Override
    public Page<Order> findOrderPageByFirmIdIn(Collection<UUID> firmIds, SearchOrderCommandV1 search, Pageable pageable) {
        return jpaOrderRepository.findOrderPageByFirmIdIn(firmIds, search, pageable);
    }

    @Override
    public Page<Order> findOrderPageByReceiverId(ReceiverId receiverId, SearchOrderCommandV1 search, Pageable pageable) {
        return jpaOrderRepository.findOrderPageByReceiverId(receiverId, search, pageable);
    }

    @Override
    public Page<Order> findOrderPageByFirmId(UUID firmId, SearchOrderCommandV1 search, Pageable pageable) {
        return jpaOrderRepository.findOrderPageByFirmId(firmId, search, pageable);
    }

    @Override
    public Page<Order> findOrderPageByHubDeliveryId(HubDeliveryId hubDeliveryId, SearchOrderCommandV1 search, Pageable pageable) {
        return jpaOrderRepository.findOrderPageByHubDeliveryId(hubDeliveryId, search, pageable);
    }

    @Override
    public Page<Order> findOrderPageByFirmDeliveryId(FirmDeliveryId firmDeliveryId, SearchOrderCommandV1 search, Pageable pageable) {
        return jpaOrderRepository.findOrderPageByFirmDeliveryId(firmDeliveryId, search, pageable);
    }
}
