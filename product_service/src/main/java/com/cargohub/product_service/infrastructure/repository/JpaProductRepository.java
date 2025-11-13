package com.cargohub.product_service.infrastructure.repository;

import com.cargohub.product_service.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID>, CustomProductRepository {

    Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);

    List<Product> findAllByIdIn(Collection<UUID> ids);
}
