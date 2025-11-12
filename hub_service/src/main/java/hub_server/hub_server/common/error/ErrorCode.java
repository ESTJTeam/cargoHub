package hub_server.hub_server.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 400
    INVALID_HUB_DATA(HttpStatus.BAD_REQUEST, "유효하지 않은 허브 데이터입니다."),
    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "페이지 크기는 10, 30, 50만 가능합니다."),

    // 401
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "Access Token이 필요합니다. Refresh Token은 사용할 수 없습니다."),

    // 403
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),

    // 409
    HUB_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 허브명입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
