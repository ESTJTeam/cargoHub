package cargohub.orchestratorservice.infrastructure.client.ai.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AiDeadlineResponseV1(
    UUID aiLogId,
    String orderInfo,
    LocalDateTime finalDeadline
) {

}