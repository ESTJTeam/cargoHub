package com.cargohub.firm_service.common.success;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "data"})
public class BaseResponse<T> {

    private final BaseStatus status;
    private final T data;

    public BaseResponse(BaseStatus status) {
        this(status, null);
    }

    public BaseResponse(BaseStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> BaseResponse<T> ok(BaseStatus status, T data) {
        return new BaseResponse<>(status, data);
    }

    public static BaseResponse<Void> ok(BaseStatus status) {
        return new BaseResponse<>(status);
    }

    public static BaseResponse<Void> error(BaseStatus status) {
        return new BaseResponse<>(status);
    }
}
