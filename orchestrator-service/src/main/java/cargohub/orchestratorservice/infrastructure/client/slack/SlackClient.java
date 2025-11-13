package cargohub.orchestratorservice.infrastructure.client.slack;

import cargohub.orchestratorservice.infrastructure.client.slack.dto.request.SlackDeadlineRequestV1;
import cargohub.orchestratorservice.presentation.success.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "slack-service", path = "/v1/slack")
public interface SlackClient {

    @PostMapping("/send-deadline-notice")
    BaseResponse<Void> sendDeadlineNotice(SlackDeadlineRequestV1 request);
}
