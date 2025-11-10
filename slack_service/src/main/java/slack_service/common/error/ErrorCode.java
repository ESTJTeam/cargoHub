package slack_service.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 500
    SLACK_CHANNEL_OPEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 채널 생성 실패"),
    SLACK_MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 메시지 전송 실패"),

    // 400
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "수신자의 슬랙 아이디가 올바르지 않습니다."),

    // 404
    SLACK_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Slack 로그를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
