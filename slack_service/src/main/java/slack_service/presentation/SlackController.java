package slack_service.presentation;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import slack_service.application.SlackService;
import slack_service.application.dto.request.SlackDeadlineRequestV1;
import slack_service.application.dto.request.SlackMessageRequestV1;
import slack_service.common.success.BaseResponse;
import slack_service.common.success.BaseStatus;
import slack_service.presentation.dto.response.SlackLogListResponseV1;
import slack_service.presentation.dto.response.SlackLogResponseV1;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/slack")
public class SlackController {

    private final SlackService slackService;

    /**
     * [Slack 사용자에게 DM 전송 - 모든 로그인한 유저 가능]
     * Slack API 통해 메시지를 전송
     *
     * @param request "channel": "userSlackId", "text": "메시지 내용"
     * @return status CREATED 반환
     */
    @PostMapping("/post-message")
    public BaseResponse<Void> postMessage(@RequestBody SlackMessageRequestV1 request) {

        slackService.sendDmToUser(request.getChannel(), request.getText());

        return BaseResponse.ok(BaseStatus.CREATED);
    }

    /**
     * [AI 발송 시한 안내 메시지 자동 전송]
     * AI로 계산된 최종 발송 시한 정보를 Slack API 통해 발송하는 허브 담당자에개 전송
     *
     * @param request "receiverSlackId": "대상자 Slack ID",
     *                "slackFormattedText": "AI가 생성한 전체 메시지(있을 경우 그대로 전송)",
     *                "orderInfo": "주문 기본 정보(Fallback 용도)",
     *                "finalDeadline": "최종 발송 시한",
     *                "aiLogId": "AI 로그 추적용 UUID"
     * @return status CREATED 반환
     */
    @PostMapping("/send-deadline-notice")
    public BaseResponse<Void> sendDeadlineNotice(@Valid @RequestBody SlackDeadlineRequestV1 request) {

        slackService.sendDeadlineNotice(request);

        return BaseResponse.ok(BaseStatus.CREATED);
    }

    /**
     * [Slack 로그 단건 조회]
     *
     * @param id 조회 대상 Slack 로그의 UUID
     * @return SlackLogResponseV1
     */
    @GetMapping("/logs/{id}")
    public BaseResponse<SlackLogResponseV1> getSlackLog(@PathVariable("id") UUID id) {

        SlackLogResponseV1 data = slackService.getSlackLog(id);

        return BaseResponse.ok(data, BaseStatus.OK);
    }

    /**
     * [Slack 로그 검색 조회 - 페이징]
     * keyword 존재하면 검색 결과를 목록으로 페이징 반환, 없으면 전체 로그 목록 페이징
     *
     * @param receiverSlackId 수신자 Slack 아이디
     * @param keyword 메시지 본문 내 검색할 키워드 (null 또는 빈 값이면 전체 목록 조회)
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param pageSize 페이지당 데이터 개수
     * @return SlackLogListResponseV1 (검색 또는 전체 로그 목록 반환)
     */
    @GetMapping("/logs")
    public BaseResponse<Page<SlackLogListResponseV1>> searchSlackLogs(
        @RequestParam(name = "receiverSlackId", required = false) String receiverSlackId,
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "page", defaultValue = "1") Long page,
        @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {

        Page<SlackLogListResponseV1> data =
            slackService.searchLogListByContent(receiverSlackId, keyword, page, pageSize);

        return BaseResponse.ok(data, BaseStatus.OK);
    }

    /**
     * [Slack 로그 단건 삭제 - Soft Delete 처리]
     *
     * @param id 삭제할 Slack 로그의 UUID
     * @return status DELETED 반환
     */
    @DeleteMapping("/logs/{id}")
    public BaseResponse<Void> deleteSlackLog(@PathVariable("id") UUID id) {

        slackService.deleteSlackLog(id);

        return BaseResponse.ok(BaseStatus.DELETED);
    }
}
