package com.cargohub.product_service.infrastructure.repository;

import com.cargohub.product_service.application.command.SearchProductCommandV1;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductRepository {

    Page<Product> findProductPage(SearchProductCommandV1 param, Pageable pageable);

    Page<Product> findProductPageByHubId(HubId hubId, SearchProductCommandV1 param, Pageable pageable);

    Page<Product> findProductPageByFirmId(FirmId firmId, SearchProductCommandV1 param, Pageable pageable);
}
