package com.cargohub.product_service.presentation.error;

import com.cargohub.product_service.common.error.BusinessException;

public class ProductException extends BusinessException {
    public ProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
