package com.cargohub.product_service.domain.repository;

import com.cargohub.product_service.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

}
