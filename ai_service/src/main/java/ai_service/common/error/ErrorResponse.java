package ai_service.common.error;

import lombok.Builder;

@Builder
public record ErrorResponse(int status, String code, String message, String path) {

}
