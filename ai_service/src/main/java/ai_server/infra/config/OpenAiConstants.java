package ai_server.infra.config;

public class OpenAiConstants {

    private OpenAiConstants() {}

    public static final String PROVIDER_OPENAI = "OpenAI";
    public static final String MODEL_GPT_4O_MINI = "gpt-4o-mini";

    /**
     * 배송 발송 시한 계산 + Slack 메시지 생성용 프롬프트
     */
    public static final String DEADLINE_SLACK_PROMPT =
        """
        다음 주문 맥락을 모두 고려해 고객이 원하는 시간에 도착하려면 마지막 발송 시점을 계산하고,
        한국어 한 문단으로 Slack에 바로 보낼 메시지를 작성하세요.
        마지막 줄에는 반드시 '최종 발송 시한은 yyyy-MM-dd HH:mm 입니다.' 형식으로 명시하세요.

        - 주문 번호: %d
        - 주문자: %s / %s
        - 주문 시각: %s
        - 상품: %s / 수량 %d개
        - 요청 사항: %s
        - 발송지: %s
        - 경유지: %s
        - 도착지: %s
        - 배송 담당자: %s / %s
        """;
}

// TODO - Few-shot Prompting 으로 수정 필요