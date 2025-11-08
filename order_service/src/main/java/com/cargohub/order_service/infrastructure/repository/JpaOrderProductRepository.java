package com.cargohub.order_service.infrastructure.repository;

import com.cargohub.order_service.domain.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderProductRepository extends JpaRepository<OrderProduct, UUID> {
}
