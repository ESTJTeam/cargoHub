package com.cargohub.product_service.common.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {

        log.warn("비즈니스 규칙 위반: {}", e.getMessage());

        ErrorCode code = e.getErrorCode();
        String path = request.getMethod() + " " + request.getRequestURI();

        ErrorResponse response = new ErrorResponse(code.getStatus().value(), code.getCode(), code.getMessage(), path);

        return ResponseEntity.status(code.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        log.warn("입력 검증 실패: {}", e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("입력 검증 실패");
        String path = request.getMethod() + " " + request.getRequestURI();

        ErrorResponse response = new ErrorResponse(status.value(), status.name(), message, path);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {

        log.warn("제약 위반: {}", e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("제약 위반이 발생했습니다");

        String path = request.getMethod() + " " + request.getRequestURI();

        ErrorResponse response = new ErrorResponse(status.value(), status.name(), message, path);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest request) {

        log.warn("바인딩 예외: {}", e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("바인딩 예외");
        String path = request.getMethod() + " " + request.getRequestURI();

        ErrorResponse response = new ErrorResponse(status.value(), status.name(), message, path);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e, HttpServletRequest request) {

        log.error("서버 내부 오류 발생", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String path = request.getMethod() + " " + request.getRequestURI();

        ErrorResponse response = new ErrorResponse(status.value(), status.name(), e.getMessage(), path);

        return ResponseEntity.status(status).body(response);
    }

}
