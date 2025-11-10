package com.cargohub.product_service.infrastructure.repository;

import com.cargohub.product_service.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID>, CustomProductRepository {
}
