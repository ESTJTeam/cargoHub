package com.cargohub.product_service.application;

import com.cargohub.product_service.application.command.CreateProductCommandV1;
import com.cargohub.product_service.application.command.DeleteProductCommandV1;
import com.cargohub.product_service.application.command.UpdateProductCommandV1;
import com.cargohub.product_service.application.command.UpdateProductStockCommandV1;
import com.cargohub.product_service.application.dto.CreateProductResultV1;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.application.exception.ProductErrorCode;
import com.cargohub.product_service.application.exception.ProductException;
import com.cargohub.product_service.domain.repository.ProductRepository;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Transactional
    public void updateProduct(UpdateProductCommandV1 updateProductCommandV1) {

        // 권한 체크
//        checkPermission(updateProductCommandV1.user().role());

        Product product = findProduct(updateProductCommandV1.id());

        //todo: 허브 담당자일 경우 상품이 담당 허브에 소속되어 있는지 체크

        product.update(
                updateProductCommandV1.name(),
                updateProductCommandV1.stockQuantity(),
                updateProductCommandV1.price(),
                updateProductCommandV1.sellable(),
                updateProductCommandV1.updatedBy()
        );
    }

    @Transactional
    public void deleteProduct(DeleteProductCommandV1 deleteProductCommandV1) {
        // 권한 체크
//        checkPermission(deleteProductCommandV1.user().role());

        Product product = findProduct(deleteProductCommandV1.id());

        //todo: 허브 담당자일 경우 상품이 담당 허브에 소속되어 있는지 체크

        product.delete(deleteProductCommandV1.deletedBy());
    }

    @Transactional
    public void decreaseStock(UpdateProductStockCommandV1 command) {
        updateStock(command, false);
    }

    @Transactional
    public void increaseStock(UpdateProductStockCommandV1 command) {
        updateStock(command, true);
    }

    private void updateStock(UpdateProductStockCommandV1 command, boolean increase) {

        List<Product> products = findProductList(command.items().keySet());

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

    private List<Product> findProductList(Set<UUID> productIds) {

        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        return products;
    }
}
