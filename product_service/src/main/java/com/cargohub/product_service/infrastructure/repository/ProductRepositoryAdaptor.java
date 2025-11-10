package com.cargohub.product_service.infrastructure.repository;


import com.cargohub.product_service.application.command.SearchProductCommandV1;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.domain.repository.ProductRepository;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdaptor implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id);
    }

    @Override
    public List<Product> findAllById(Set<UUID> id) {
        return jpaProductRepository.findAllById(id);
    }

    @Override
    public Page<Product> findProductPage(SearchProductCommandV1 search, Pageable pageable) {
        return jpaProductRepository.findProductPage(search, pageable);
    }

    @Override
    public Page<Product> findProductPageByFirmId(FirmId firmId, SearchProductCommandV1 search, Pageable pageable) {
        return jpaProductRepository.findProductPageByFirmId(firmId, search, pageable);
    }

    @Override
    public Page<Product> findProductPageByHubId(HubId hubId, SearchProductCommandV1 search, Pageable pageable) {
        return jpaProductRepository.findProductPageByHubId(hubId, search, pageable);
    }
}
