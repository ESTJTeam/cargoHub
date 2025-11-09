package com.cargohub.product_service.presentation;

import com.cargohub.product_service.application.ProductService;
import com.cargohub.product_service.application.command.*;
import com.cargohub.product_service.application.dto.CreateProductResultV1;
import com.cargohub.product_service.presentation.dto.request.CreateProductRequestV1;
import com.cargohub.product_service.presentation.dto.request.UpdateProductRequestV1;
import com.cargohub.product_service.presentation.dto.request.UpdateProductStockRequestV1;
import com.cargohub.product_service.presentation.dto.response.CreateProductResponseV1;
import com.cargohub.product_service.presentation.dto.response.ReadProductDetailResponseV1;
import com.cargohub.product_service.presentation.dto.response.ReadProductSummaryResponseV1;
import com.cargohub.product_service.common.success.BaseResponse;
import com.cargohub.product_service.common.success.BaseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CreateProductResponseV1> createProduct(@RequestBody @Valid CreateProductRequestV1 request){

        CreateProductCommandV1 commandV1 = new CreateProductCommandV1(
                request.name(),
                request.firmId(),
                request.hubId(),
                request.stockQuantity(),
                request.price(),
                request.sellable(),
                UUID.randomUUID() // todo: 생성자 ID로 수정
        );

        CreateProductResultV1 result = productService.createProduct(commandV1);

        return BaseResponse.ok(
                CreateProductResponseV1.from(result),
                BaseStatus.CREATED
        );
    }

    @GetMapping
    public BaseResponse<Page<ReadProductSummaryResponseV1>> readProductPage(@PageableDefault(size = 10) Pageable pageable) {
        // todo: 애플리케이션 서비스 호출 - 사용자 정보 필요(id, role)
//        productService.readProductPage(pageable, user);

        ReadProductSummaryResponseV1 responseV1 = new ReadProductSummaryResponseV1(
                UUID.randomUUID(),
                "상품 명",
                UUID.randomUUID(),
                UUID.randomUUID(),
                100000,
                BigDecimal.valueOf(10000),
                true,
                LocalDateTime.now()
        );

        return BaseResponse.ok(new PageImpl<>(List.of(responseV1)), BaseStatus.OK);
    }

    @GetMapping("/{id}")
    public BaseResponse<ReadProductDetailResponseV1> readProduct(@PathVariable("id") UUID id) {
        // todo: 애플리케이션 서비스 호출 - 사용자 정보 필요(id, role)
//        ReadProductDetailResultV1 productDetailResultV1 = productService.readProduct(id, user);

        ReadProductDetailResponseV1 responseV1 = new ReadProductDetailResponseV1(
                UUID.randomUUID(),
                "상품 명",
                UUID.randomUUID(),
                UUID.randomUUID(),
                100000,
                BigDecimal.valueOf(10000),
                true,
                LocalDateTime.now(),
                UUID.randomUUID(),
                null,
                null,
                null,
                null
        );

        return BaseResponse.ok(responseV1, BaseStatus.OK);
    }

    @PatchMapping("/{id}")
    public BaseResponse<Void> updateProduct(@PathVariable("id") UUID id, @RequestBody @Valid UpdateProductRequestV1 request) {

        UpdateProductCommandV1 commandV1 = new UpdateProductCommandV1(
                id,
                request.name(),
                request.stockQuantity(),
                request.price(),
                request.sellable(),
                UUID.randomUUID() // todo: 수정 - 수정자 ID
        );

        // todo: 사용자 정보 필요(id, role)
        productService.updateProduct(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteProduct(@PathVariable("id") UUID id) {

        DeleteProductCommandV1 commandV1 = new DeleteProductCommandV1(
                id,
                UUID.randomUUID() // todo: 수정 - 삭제자 ID
        );

        // todo: 애플리케이션 서비스 호출 - 사용자 정보 필요(id, role)
//        productService.deleteProduct(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/decrease")
    public BaseResponse<Void> decreaseStock(@RequestBody @Valid UpdateProductStockRequestV1 request) {

        UpdateProductStockCommandV1 commandV1 = UpdateProductStockCommandV1.from(request);
        // todo: 애플리케이션 서비스 호출
//        productService.decreaseStock(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/increase")
    public BaseResponse<Void> increaseStock(@RequestBody @Valid UpdateProductStockRequestV1 request) {

        UpdateProductStockCommandV1 commandV1 = UpdateProductStockCommandV1.from(request);
        // todo: 애플리케이션 서비스 호출
//        productService.increaseStock(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

}
