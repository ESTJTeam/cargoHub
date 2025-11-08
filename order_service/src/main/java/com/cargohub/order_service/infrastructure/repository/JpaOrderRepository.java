package com.cargohub.order_service.infrastructure.repository;

import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.vo.FirmDeliveryId;
import com.cargohub.order_service.domain.vo.HubDeliveryId;
import com.cargohub.order_service.domain.vo.ReceiverId;
import com.cargohub.order_service.domain.vo.SupplierId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
}
