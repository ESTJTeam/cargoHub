package com.cargohub.product_service.application;

import com.cargohub.product_service.application.command.*;
import com.cargohub.product_service.application.dto.CreateProductResultV1;
import com.cargohub.product_service.application.dto.ReadProductDetailResultV1;
import com.cargohub.product_service.application.dto.ReadProductSummaryResultV1;
import com.cargohub.product_service.application.service.UserInfoResponse;
import com.cargohub.product_service.application.service.firm.FirmClient;
import com.cargohub.product_service.application.service.firm.FirmResponseV1;
import com.cargohub.product_service.application.service.hub.HubClient;
import com.cargohub.product_service.application.service.hub.HubManagerCheckResponseV1;
import com.cargohub.product_service.application.service.hub.HubResponseV1;
import com.cargohub.product_service.common.JwtUtil;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.application.exception.ProductErrorCode;
import com.cargohub.product_service.application.exception.ProductException;
import com.cargohub.product_service.domain.repository.ProductRepository;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import com.cargohub.product_service.domain.vo.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;

    private final FirmClient firmClient;
    private final HubClient hubClient;

    @Transactional
    public CreateProductResultV1 createProduct(CreateProductCommandV1 createProductCommandV1, String accessToken) {

        UserInfoResponse user = jwtUtil.parseJwt(accessToken);

        // 업체 존재 확인
        FirmResponseV1 firmInfo = firmClient.getFirm(createProductCommandV1.firmId());
        FirmId firmId = FirmId.of(firmInfo.id());

        // 허브 존재 확인
        HubResponseV1 hubInfo = hubClient.getHub(createProductCommandV1.hubId());
        HubId hubId = HubId.of(hubInfo.hubId());

        // 권한 체크
        checkPermission(user.role());

        // 허브 담당자일 경우 담당 허브인지 확인
        validateHubManagerAuthority(createProductCommandV1.hubId(), user, accessToken);

        // 상품 생성
        Product product = Product.ofNewProduct(
                createProductCommandV1.name(),
                firmId,
                hubId,
                createProductCommandV1.stockQuantity(),
                createProductCommandV1.price(),
                createProductCommandV1.sellable(),
                user.userId()
        );

        Product newProduct = productRepository.save(product);

        return CreateProductResultV1.from(newProduct);
    }

    @Transactional(readOnly = true)
    public Page<ReadProductSummaryResultV1> readProductPage(SearchProductCommandV1 searchProductCommandV1, Pageable pageable) {

        Page<Product> productPage;

        /**
         * 허브 담당자일 경우 - 허브 id 필요함
         * 업체 담당자일 경우 - 업체 id 필요함
         */

        switch (searchProductCommandV1.user().role()) {
            case MASTER, DELIVERY_MANAGER ->
                    productPage = productRepository.findProductPage(searchProductCommandV1, pageable);

            case HUB_MANAGER -> {
                // todo: userId를 통한 hubId 조회 -> hubClient 단건 조회
                HubId hubId = HubId.of(UUID.randomUUID());
                if(hubId == null) {
                    throw new ProductException(ProductErrorCode.HUB_ID_REQUIRED);
                }
                productPage = productRepository.findProductPageByHubId(hubId, searchProductCommandV1, pageable);
            }
            case SUPPLIER_MANAGER -> {
                // todo: firmId 조회 -> firmClient 단건 조회
                FirmId firmId = FirmId.of(UUID.randomUUID());
                if(firmId == null) {
                    throw new ProductException(ProductErrorCode.FIRM_ID_REQUIRED);
                }
                productPage = productRepository.findProductPageByFirmId(firmId, searchProductCommandV1, pageable);
            }
            default ->
                    throw new ProductException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }
        return productPage.map(ReadProductSummaryResultV1::from);
    }

    @Transactional(readOnly = true)
    public ReadProductDetailResultV1 readProduct(UUID productId) {

        Product product = findProduct(productId);

        return ReadProductDetailResultV1.from(product);
    }

    @Transactional
    public void updateProduct(UpdateProductCommandV1 updateProductCommandV1, String accessToken) {

        Product product = findProduct(updateProductCommandV1.id());
        UserInfoResponse user = jwtUtil.parseJwt(accessToken);

        // 권한 체크
        checkPermission(user.role());

        // 허브 담당자일 경우 상품이 담당 허브에 소속되어 있는지 체크
        validateHubManagerAuthority(product.getHubId().getId(), user, accessToken);

        product.update(
                updateProductCommandV1.name(),
                updateProductCommandV1.stockQuantity(),
                updateProductCommandV1.price(),
                updateProductCommandV1.sellable(),
                user.userId()
        );
    }

    @Transactional
    public void deleteProduct(DeleteProductCommandV1 deleteProductCommandV1, String accessToken) {

        Product product = findProduct(deleteProductCommandV1.id());
        UserInfoResponse user = jwtUtil.parseJwt(accessToken);

        // 권한 체크
        checkPermission(user.role());

        // 허브 담당자일 경우 상품이 담당 허브에 소속되어 있는지 체크
        validateHubManagerAuthority(product.getHubId().getId(), user, accessToken);

        product.delete(user.userId());
    }

    // 재고 확인
    @Transactional(readOnly = true)
    public void checkStock(CheckProductStockCommandV1 checkProductStockCommandV1) {

        List<UUID> productIds = checkProductStockCommandV1.products().stream()
                .map(CheckProductStockCommandV1.ProductStockItem::id)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (var item : checkProductStockCommandV1.products()) {
            Product product = productMap.get(item.id());

            if (product == null) {
                throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
            }

            if(!product.getSellable()) {
                throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOR_SALE);
            }

            if (product.getStockQuantity() < item.quantity()) {
                throw new ProductException(ProductErrorCode.INVALID_DECREASE_QUANTITY);
            }
        }
    }

    // 재고 차감
    @Transactional
    public void decreaseStock(UpdateProductStockCommandV1 command) {
        updateStock(command, false);
    }

    // 재고 증가
    @Transactional
    public void increaseStock(UpdateProductStockCommandV1 command) {
        updateStock(command, true);
    }

    public List<ReadProductSummaryResultV1> bulkProduct(BulkProductQueryCommandV1 bulkProductQueryCommandV1) {

        List<UUID> uniqueIds = bulkProductQueryCommandV1.ids().stream()
                .distinct()
                .toList();

        return findProductList(uniqueIds).stream()
                .map(ReadProductSummaryResultV1::from)
                .toList();
    }

    private void updateStock(UpdateProductStockCommandV1 command, boolean increase) {

        List<Product> products = findProductList(command.items().keySet().stream().toList());

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        command.items().forEach((id, quantity) -> {
            Product product = productMap.get(id);
            if (product == null) {
                throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
            }

            if (increase) {
                product.increaseStock(quantity);
            } else {
                product.decreaseStock(quantity);
            }
        });
    }

    private Product findProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    private List<Product> findProductList(List<UUID> productIds) {

        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        return products;
    }

    private void checkPermission(UserRole role) {

        if(role != UserRole.MASTER
                && role != UserRole.HUB_MANAGER) {
            throw new ProductException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }
    }

    private void validateHubManagerAuthority(UUID hubId, UserInfoResponse user, String accessToken) {

        if(user.role().equals(UserRole.HUB_MANAGER)) {
            HubManagerCheckResponseV1 supplierHub =  hubClient.checkHubManager(hubId, accessToken);
            if(!supplierHub.isManager()){
                throw new ProductException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
            }
        }
    }
}
