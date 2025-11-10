package slack_service.application;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import slack_service.application.dto.request.SlackDeadlineRequestV1;
import slack_service.application.dto.request.SlackMessageRequestV1;
import slack_service.common.error.BusinessException;
import slack_service.common.error.ErrorCode;
import slack_service.domain.entity.SlackLog;
import slack_service.domain.repository.SlackLogRepository;
import slack_service.presentation.dto.response.SlackLogListResponseV1;
import slack_service.presentation.dto.response.SlackLogResponseV1;

@Service
@RequiredArgsConstructor
public class SlackService {

    private static final String OPEN_CONVERSATION_URL = "https://slack.com/api/conversations.open";
    private static final String POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DEADLINE_FORMAT = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");

    private static final long DEFAULT_PAGE = 1L;
    private static final long DEFAULT_SIZE = 10L;
    private static final List<Long> PAGE_SIZE_WHITELIST = List.of(10L, 30L, 50L);

    private final RestClient restClient;
    private final SlackLogRepository slackLogRepository;

    /**
     * [Slack DM 채널 생성] 특정 사용자(userSlackId)와의 1:1 DM 채널을 개설하거나 기존 채널을 반환한다.
     *
     * @param userSlackId 메시지 수신자의 Slack 아이디
     * @return 채널 아이디
     */
    private String openConversation(String userSlackId) {

        Map<?, ?> response = restClient.post()
            .uri(OPEN_CONVERSATION_URL)
            .body("""
                { "users": ["%s"] }
                """.formatted(userSlackId))
            .retrieve()
            .body(Map.class);

        if (response == null || Boolean.FALSE.equals(response.get("ok"))) {
            throw new BusinessException(ErrorCode.SLACK_CHANNEL_OPEN_FAILED);
        }

        Map<String, Object> channel = (Map<String, Object>) response.get("channel");

        if (channel == null || channel.get("id") == null) {
            throw new BusinessException(ErrorCode.SLACK_CHANNEL_OPEN_FAILED);
        }

        return (String) channel.get("id"); // 예: DO7XYZ123
    }

    /**
     * [Slack 메시지 전송 - 모든 로그인한 유저 가능] 특정 사용자와의 DM 채널을 열고 입력한 메시지를 전송한다.
     *
     * @param receiverSlackId 메시지 수신자의 Slack 아이디
     * @param message         전송할 메시지 본문
     */
    @Transactional
    public void sendDmToUser(String receiverSlackId, String message) {

        if (!StringUtils.hasText(receiverSlackId) || !StringUtils.hasText(message)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }

        // 1. DM 채널 생성 (또는 기존 DM 채널 가져오기)
        String channelId = openConversation(receiverSlackId);

        SlackMessageRequestV1 request = SlackMessageRequestV1.builder()
            .channel(channelId)
            .text(message)
            .build();

        // 2. DM 전송
        Map<?, ?> response = restClient.post()
            .uri(POST_MESSAGE_URL)
            .body(request)
            .retrieve()
            .body(Map.class);

        if (response == null || Boolean.FALSE.equals(response.get("ok"))) {

            throw new BusinessException(ErrorCode.SLACK_MESSAGE_SEND_FAILED);
        }

        SlackLog slackLog = SlackLog.of(receiverSlackId, message);

        slackLogRepository.save(slackLog);
    }

    // TODO - Order 가져와서 수정 예정

    /**
     * [Ai 연동 - 최종 발송 시한 메시지 자동 전송] AI가 생성한 Slack 메시지를 담당자에게 자동으로 전송한다. slackFormattedText 있으면 그대로
     * 전송 없으면 orderInfo + finalDeadline 으로 폴백 메시지 생성 후 전송
     *
     * @param request AI 메시지 또는 Fallback 데이터
     */
    @Transactional
    public void sendDeadlineNotice(SlackDeadlineRequestV1 request) {

        if (!StringUtils.hasText(request.getReceiverSlackId())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }

        // 1. AI가 생성한 메시지가 존재할 경우 그대로 사용
        String text = request.getSlackFormattedText();

        // 2. 없을 경우 Fallback 메시지 구성
        if (!StringUtils.hasText(text)) {

            // 필수 정보(orderInfo, finalDeadline) 없으면 예외
            if (!StringUtils.hasText(request.getOrderInfo())
                || request.getFinalDeadline() == null) {
                throw new BusinessException(ErrorCode.INVALID_PARAMETER);
            }

            text = buildFallbackDeadlineText(
                request.getOrderInfo(),
                request.getFinalDeadline()
            );
        }

        // 3. 메시지 하단에 aiLogId 메타 정보 추가
        text = appendMetaLine(text, request.getAiLogId());

        sendDmToUser(request.getReceiverSlackId(), text);
    }

    /**
     * [SlackLog 단건 조회] Slack 메시지 전송 이력을 ID로 단건 조회
     *
     * @param slackId 조회 대상 Slack 로그의 UUID
     * @return SlackLogResponseV1 (id, receiverSlackId, message, createdAt)
     */
    @Transactional(readOnly = true)
    public SlackLogResponseV1 getSlackLog(UUID slackId) {

        SlackLog slackLog = getLogOrThrow(slackId);

        return SlackLogResponseV1.from(slackLog);
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
    @Transactional(readOnly = true)
    public Page<SlackLogListResponseV1> searchLogListByContent(String receiverSlackId,
        String keyword, Long page, Long pageSize) {

        long p = normalizePage(page) - 1;
        long s = normalizePageSize(pageSize);

        // createdAt desc, id desc 정렬
        Pageable pageable = PageRequest.of(
            (int) p,
            (int) s,
            Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"))
        );

        // 수신자 slack 아이디 & 키워드 있으면 검색 결과 목록 조회, 없으면 전체 목록 조회
        Page<SlackLog> result;
        if (StringUtils.hasText(receiverSlackId) || StringUtils.hasText(keyword)) {
            result = slackLogRepository.searchByMessageWithPaging(receiverSlackId, keyword, pageable);
        } else {
            result = slackLogRepository.findAllActive(pageable);
        }

        return result.map(SlackLogListResponseV1::from);
    }

    // TODO - User UUID 가져와서 deleterUserId 추가
    /**
     * [Slack 로그 단건 삭제 - Soft Delete 처리]
     *
     * @param slackId 삭제할 Slack 로그의 UUID
     */
    @Transactional
    public void deleteSlackLog(UUID slackId) {

        SlackLog slackLog = getLogOrThrow(slackId);

        if (slackLog.checkDeleted()) {
            throw new BusinessException(ErrorCode.SLACK_LOG_ALREADY_DELETED);
        }

        slackLog.delete();
    }

    // [공통] 로그 조회 메서드
    private SlackLog getLogOrThrow(UUID slackId) {

        return slackLogRepository.findById(slackId)
            .orElseThrow(() -> new BusinessException(ErrorCode.SLACK_LOG_NOT_FOUND));
    }

    // [공통] 페이징 보정 - 페이지 번호: null 또는 1 미만이면 1
    private static long normalizePage(Long page) {

        return (page == null || page < 1) ? DEFAULT_PAGE : page;
    }

    // [공통] 페이징 보정 - 페이지 크기: 화이트리스트 외/ null 이면 10
    private static long normalizePageSize(Long pageSize) {

        return (pageSize == null || !PAGE_SIZE_WHITELIST.contains(pageSize)) ? DEFAULT_SIZE
            : pageSize;
    }

    // Fallback 텍스트 생성
    private String buildFallbackDeadlineText(String orderInfo, LocalDateTime finalDeadline) {

        // 시스템 시간대 → 한국 시간대(KST) 변환 및 포맷팅
        String deadlineKst = finalDeadline.atZone(ZoneId.systemDefault())
            .withZoneSameInstant(KST)
            .toLocalDateTime()
            .format(DEADLINE_FORMAT);

        return """
            *배송 최종 발송 시한 알림*
            • 주문: %s
            • 최종 발송 시한(KST): %s
            """.formatted(orderInfo, deadlineKst);
    }

    // aiLogId 추적 용도
    private String appendMetaLine(String text, UUID aiLogId) {

        if (aiLogId == null) {
            return text;
        }

        // Slack 마크다운의 구분선 대용으로 공백 줄 + 얇은 meta 라인 추가
        return text + "\n\n— _aiLogId: " + aiLogId + "_";
    }
}
