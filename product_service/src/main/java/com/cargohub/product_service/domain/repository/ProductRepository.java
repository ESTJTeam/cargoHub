package com.cargohub.product_service.domain.repository;

import com.cargohub.product_service.domain.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAllById(Set<UUID> id);

}
