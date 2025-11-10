package slack_service.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 500
    SLACK_CHANNEL_OPEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 채널 생성 실패");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
