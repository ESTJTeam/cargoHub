package com.cargohub.product_service.domain.exception;

import com.cargohub.product_service.common.error.BusinessException;

public class ProductException extends BusinessException {
    public ProductException(ProductErrorCode productErrorCode) {
        super(productErrorCode);
    }
}
