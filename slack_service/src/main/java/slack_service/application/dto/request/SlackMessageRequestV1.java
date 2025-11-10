package slack_service.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SlackMessageRequestV1 {

    // DM 받을 수신자 Slack 아이디
    @NotBlank
    private String channel;

    // 전송할 메시지 본문
    @NotBlank
    private String text;

    @Builder
    private SlackMessageRequestV1(String channel, String text) {

        this.channel = channel;
        this.text = text;
    }
}
