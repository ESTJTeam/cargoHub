package user_server.user_server.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final user_server.user_server.global.exception.domain.ErrorCode errorCode;

    public BusinessException(user_server.user_server.global.exception.domain.ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}