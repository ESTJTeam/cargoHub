package com.cargohub.product_service.infrastructure.repository;


import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdaptor implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }
}
