package com.cargohub.order_service.common.success;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    BaseStatus status;
    T data;

    public BaseResponse(BaseStatus status){this.status = status;}

    public BaseResponse(BaseStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> BaseResponse<T> ok(T data, BaseStatus status) {
        return new BaseResponse<>(status, data);
    }

    public static BaseResponse<Void> ok(BaseStatus status) {
        return new BaseResponse<>(status);
    }

}
