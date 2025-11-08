package com.cargohub.product_service.application;

import com.cargohub.product_service.application.command.CreateProductCommandV1;
import com.cargohub.product_service.application.command.DeleteProductCommandV1;
import com.cargohub.product_service.application.command.UpdateProductCommandV1;
import com.cargohub.product_service.application.command.UserInfo;
import com.cargohub.product_service.application.dto.CreateProductResultV1;
import com.cargohub.product_service.application.dto.ReadProductDetailResultV1;
import com.cargohub.product_service.application.dto.ReadProductSummaryResultV1;
import com.cargohub.product_service.common.error.BusinessException;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.domain.repository.ProductRepository;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import com.cargohub.product_service.domain.vo.UserRole;
import com.cargohub.product_service.presentation.error.ErrorCode;
import com.cargohub.product_service.presentation.error.ProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

//    private final FirmClient firmClient;
//    private final HubClient hubClient;

    @Transactional
    public CreateProductResultV1 createProduct(CreateProductCommandV1 createProductCommandV1) {
        // 권한 체크
        checkPermission(createProductCommandV1.user().role());

        // todo: 업체 존재 확인 - getId

        FirmId firmId = FirmId.of(createProductCommandV1.firmId());
       // todo: 허브 존재 확인 - getId, 담당허브인지 확인

        HubId hubId = HubId.of(createProductCommandV1.hubId());
        Product product = Product.ofNewProduct(
                createProductCommandV1.name(),
                firmId,
                hubId,
                createProductCommandV1.stockQuantity(),
                createProductCommandV1.price(),
                createProductCommandV1.sellable() == null || createProductCommandV1.sellable(),
                createProductCommandV1.user().id()
        );

        Product newProduct = productRepository.save(product);

        return CreateProductResultV1.from(newProduct, "업체 명", "허브 명");
    }

    private void checkPermission(UserRole role) {

        if(role != UserRole.MASTER
                && role != UserRole.HUB_MANAGER) {
            throw new ProductException(ErrorCode.PRODUCT_ACCESS_DENIED);
        }

    }
}
