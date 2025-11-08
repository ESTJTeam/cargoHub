package com.cargohub.product_service.application;

import com.cargohub.product_service.application.command.CreateProductCommandV1;
import com.cargohub.product_service.application.dto.CreateProductResultV1;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.domain.repository.ProductRepository;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

//    private final UserClient userClient;
//    private final FirmClient firmClient;
//    private final HubClient hubClient;

    @Transactional
    public CreateProductResultV1 createProduct(CreateProductCommandV1 createProductCommandV1) {
        // todo: 권한 체크

        // todo: 업체 존재 확인

        FirmId firmId = FirmId.of(createProductCommandV1.firmId());
       // todo: 허브 존재 확인, 담당허브인지 확인

        HubId hubId = HubId.of(createProductCommandV1.hubId());
        Product product = Product.ofNewProduct(
                createProductCommandV1.name(),
                firmId,
                hubId,
                createProductCommandV1.stockQuantity(),
                createProductCommandV1.price(),
                createProductCommandV1.sellable(),
                createProductCommandV1.createdBy()
        );

        Product newProduct = productRepository.save(product);

        return CreateProductResultV1.from(newProduct);
    }
}
