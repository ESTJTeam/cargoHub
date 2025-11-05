package user_server.user_server.presentation.error.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(int status, String code, String message, String path) {

}
