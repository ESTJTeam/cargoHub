package com.cargohub.firm_service.common.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        ErrorCode code = e.getErrorCode();
        String path = request.getMethod() + " " + request.getRequestURI();

        // 🔥 로그 추가
        log.warn("[BusinessException] {} - code={}, message={}", path, code.getCode(), e.getMessage(), e);

        return ResponseEntity.status(code.getStatus()).body(
                ErrorResponse.builder()
                        .status(code.getStatus().value())
                        .code(code.getCode())
                        .message(code.getMessage())
                        .path(path)
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();

        // 🔥 로그 추가
        log.warn("[IllegalArgumentException] {} - {}", path, e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .code("INVALID_ARGUMENT")
                        .message(e.getMessage())
                        .path(path)
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        String message = e.getMessage() != null ? e.getMessage() : "요청한 리소스를 찾을 수 없습니다.";

        // 🔥 로그 추가
        log.warn("[EntityNotFoundException] {} - {}", path, message, e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .code("ENTITY_NOT_FOUND")
                        .message(message)
                        .path(path)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("유효성 검증에 실패했습니다.");

        // 🔥 로그 추가
        log.warn("[MethodArgumentNotValidException] {} - {}", path, message, e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .code("VALIDATION_FAILED")
                        .message(message)
                        .path(path)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        String message = "잘못된 형식의 데이터입니다: " + e.getName();

        // 🔥 로그 추가
        log.warn("[MethodArgumentTypeMismatchException] {} - {}", path, message, e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .code("TYPE_MISMATCH")
                        .message(message)
                        .path(path)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();

        // 🔥 최종 방어선은 무조건 에러 로그 + 스택트레이스
        log.error("[UnhandledException] {} - {}", path, e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .code("INTERNAL_SERVER_ERROR")
                        .message("서버 내부 오류가 발생했습니다.")
                        .path(path)
                        .build()
        );
    }
}
