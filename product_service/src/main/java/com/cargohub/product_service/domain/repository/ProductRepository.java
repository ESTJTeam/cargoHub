package com.cargohub.product_service.domain.repository;

import com.cargohub.product_service.application.command.SearchProductCommandV1;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAllById(List<UUID> id);

    /**
     * todo: 권한 별 조회 필요
     * 1. 마스터 조회 - 전체 상품 조회
     * 2. 허브 관리자 조회 - 담당 허브 상품 조회
     * 3. 업체 담당자 조회 - 담당 업체 상품 조회
     * 4. 배송 담당자 - 전체 상품 조회
     */

    // 마스터 관리자, 배송 담당자
    Page<Product> findProductPage(SearchProductCommandV1 search, Pageable pageable);

    // 업체 담당자 - 담당 업체 상품 조회
    Page<Product> findProductPageByFirmId(FirmId firmId, SearchProductCommandV1 search, Pageable pageable);

    // 허브 관리자 조회 - 담당 허브 상품 조회
    Page<Product> findProductPageByHubId(HubId hubId, SearchProductCommandV1 search, Pageable pageable);

}
