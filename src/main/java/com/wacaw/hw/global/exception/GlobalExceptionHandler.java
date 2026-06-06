package com.wacaw.hw.global.exception;

import com.wacaw.hw.global.commnon.BaseResponse;
import com.wacaw.hw.global.exception.model.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 우리가 직접 던지는 커스텀 예외 처리
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<BaseResponse<Void>> handleCustomException(CustomException e) {
    log.error("CustomException: {}", e.getMessage());
    BaseErrorCode errorCode = e.getErrorCode();
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(BaseResponse.fail(errorCode.getStatus(), errorCode.getMessage()));
  }

  // @Valid 검증 실패 처리 (필드 누락, 형식 오류 등)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Void>> handleValidationException(
      MethodArgumentNotValidException e) {
    // 첫 번째 에러 메시지만 반환
    String message = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(FieldError::getDefaultMessage)
        .orElse("입력값이 올바르지 않습니다.");

    log.error("ValidationException: {}", message);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(BaseResponse.fail(400, message));
  }

  // 예상치 못한 서버 에러 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
    log.error("UnexpectedException: {}", e.getMessage(), e);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(BaseResponse.fail(500, "서버 내부 오류가 발생했습니다."));
  }
}
