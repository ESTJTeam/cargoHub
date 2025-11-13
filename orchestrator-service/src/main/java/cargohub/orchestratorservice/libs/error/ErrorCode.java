package cargohub.orchestratorservice.libs.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 사용 예시이므로 추가로 작성 해주시면 찡긋

    // USER
    INVALID_HUB_ID(HttpStatus.BAD_REQUEST,  "유효하지 않은 허브 ID입니다."),
    INVALID_SLACK_ID(HttpStatus.BAD_REQUEST,  "유효하지 않은 슬랙 ID입니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST,  "유효하지 않은 사용자 역할입니다."),
    TOO_MANY_DELIVERY_MANAGERS(HttpStatus.CONFLICT, "허용된 배송담당자 수를 초과했습니다."),

    // JWT) 관련
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),

    // 403
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),

    //409
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "재로그인이 필요합니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
    DUPLICATE_SLACK_ID(HttpStatus.CONFLICT, "이미 존재하는 slackID"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 username"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 nickname"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 email"),


    // Order 관련
    // 401
    INVALID_ORDER_SUPPLIER(HttpStatus.BAD_REQUEST, "같은 공급 업체 상품만 주문할 수 있습니다."),
    ORDER_PRODUCT_EMPTY(HttpStatus.BAD_REQUEST, "주문 항목은 최소 1개 이상이어야 합니다."),
    ORDER_PRODUCT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "해당 상품은 주문할 수 없습니다."),
    ORDER_CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "현재 상태에서는 주문을 취소할 수 없습니다."),

    // 403
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 주문에 접근할 권한이 없습니다."),

    // 404
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}

