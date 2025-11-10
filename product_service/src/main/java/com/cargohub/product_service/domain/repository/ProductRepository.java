package com.cargohub.product_service.domain.repository;

import com.cargohub.product_service.domain.entity.Product;

public interface ProductRepository {

    Product save(Product product);
}
