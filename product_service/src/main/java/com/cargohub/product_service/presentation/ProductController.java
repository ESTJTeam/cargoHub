package com.cargohub.product_service.presentation;

import com.cargohub.product_service.application.ProductService;
import com.cargohub.product_service.application.command.*;
import com.cargohub.product_service.application.dto.CreateProductResultV1;
import com.cargohub.product_service.application.dto.ReadProductDetailResultV1;
import com.cargohub.product_service.application.dto.ReadProductSummaryResultV1;
import com.cargohub.product_service.application.service.UserInfoResponse;
import com.cargohub.product_service.common.JwtUtil;
import com.cargohub.product_service.presentation.dto.request.*;
import com.cargohub.product_service.presentation.dto.response.BulkProductQueryResponseV1;
import com.cargohub.product_service.presentation.dto.response.CreateProductResponseV1;
import com.cargohub.product_service.presentation.dto.response.ReadProductDetailResponseV1;
import com.cargohub.product_service.presentation.dto.response.ReadProductSummaryResponseV1;
import com.cargohub.product_service.common.success.BaseResponse;
import com.cargohub.product_service.common.success.BaseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    @ModelAttribute("userInfo")
    public UserInfoResponse getUser(@RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return UserInfoResponse.anonymous();
        }
        return jwtUtil.parseJwt(accessToken);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CreateProductResponseV1> createProduct(@RequestBody @Valid CreateProductRequestV1 request, @ModelAttribute("userInfo") UserInfoResponse userInfoResponse){

        CreateProductCommandV1 commandV1 = new CreateProductCommandV1(
                request.name(),
                request.firmId(),
                request.hubId(),
                request.stockQuantity(),
                request.price(),
                request.sellable(),
                new UserInfo(userInfoResponse.userId(), userInfoResponse.role())
        );

        CreateProductResultV1 result = productService.createProduct(commandV1);

        return BaseResponse.ok(
                CreateProductResponseV1.from(result),
                BaseStatus.CREATED
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Page<ReadProductSummaryResponseV1>> readProductPage(@ModelAttribute SearchProductRequestV1 search, @PageableDefault(size = 10) Pageable pageable, @ModelAttribute("userInfo") UserInfoResponse userInfoResponse) {

        SearchProductCommandV1 searchCommand = new SearchProductCommandV1(
                search.name(),
                search.firmId(),
                search.hubId(),
                search.sellable(),
                new UserInfo(userInfoResponse.userId(), userInfoResponse.role())
        );

        Page<ReadProductSummaryResultV1> pageResult = productService.readProductPage(searchCommand, pageable);

        return BaseResponse.ok(pageResult.map(ReadProductSummaryResponseV1::from), BaseStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReadProductDetailResponseV1> readProduct(@PathVariable("id") UUID id) {

        ReadProductDetailResultV1 productDetailResult = productService.readProduct(id);

        return BaseResponse.ok(ReadProductDetailResponseV1.from(productDetailResult), BaseStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateProduct(@PathVariable("id") UUID id, @RequestBody @Valid UpdateProductRequestV1 request, @ModelAttribute("userInfo") UserInfoResponse userInfoResponse) {

        UpdateProductCommandV1 commandV1 = new UpdateProductCommandV1(
                id,
                request.name(),
                request.stockQuantity(),
                request.price(),
                request.sellable(),
                new UserInfo(userInfoResponse.userId(), userInfoResponse.role())
        );

        productService.updateProduct(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> deleteProduct(@PathVariable("id") UUID id, @ModelAttribute("userInfo") UserInfoResponse userInfoResponse) {

        DeleteProductCommandV1 commandV1 = new DeleteProductCommandV1(
                id,
                new UserInfo(userInfoResponse.userId(), userInfoResponse.role())
        );

        productService.deleteProduct(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/check-stock")
    public BaseResponse<Void> checkStock(@RequestBody CheckProductStockRequestV1 request) {

        CheckProductStockCommandV1 commandV1 = CheckProductStockCommandV1.from(request);
        productService.checkStock(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/decrease")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> decreaseStock(@RequestBody @Valid UpdateProductStockRequestV1 request) {

        UpdateProductStockCommandV1 commandV1 = UpdateProductStockCommandV1.from(request);
        productService.decreaseStock(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/increase")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> increaseStock(@RequestBody @Valid UpdateProductStockRequestV1 request) {

        UpdateProductStockCommandV1 commandV1 = UpdateProductStockCommandV1.from(request);
        productService.increaseStock(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/bulk-query")
    public ResponseEntity<BulkProductQueryResponseV1> bulkProduct(@RequestBody BulkProductQueryRequestV1 request) {

        BulkProductQueryCommandV1 commandV1 = new BulkProductQueryCommandV1(request.ids());

        Map<UUID, ReadProductSummaryResponseV1> productMap = productService.bulkProduct(commandV1).stream()
                .map(ReadProductSummaryResponseV1::from)
                .collect(Collectors.toMap(ReadProductSummaryResponseV1::id, Function.identity()));

        return ResponseEntity.ok(new BulkProductQueryResponseV1(productMap));
    }

}
