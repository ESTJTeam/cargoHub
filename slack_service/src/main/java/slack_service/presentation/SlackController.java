package slack_service.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slack_service.application.SlackService;
import slack_service.application.dto.request.SlackMessageRequestV1;
import slack_service.common.success.BaseResponse;
import slack_service.common.success.BaseStatus;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/slack")
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/post-messages")
    public BaseResponse<Void> postMessage(@RequestBody SlackMessageRequestV1 request) {

        slackService.sendDmToUser(request.getChannel(), request.getText());

        return BaseResponse.ok(BaseStatus.CREATED);
    }
}
