package com.wacaw.hw.global.commnon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "BaseResponse DTO", description = "공통 API 응답 형식")
public class BaseResponse<T> {

  @Schema(description = "상태 코드", example = "200")
  private final int status;
  @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
  private final String message;
  @Schema(description = "응답 데이터")
  private final T data;

  // 성공 (데이터 있음)
  public static <T> BaseResponse<T> success(T data) {
    return new BaseResponse<>(200, "요청이 성공했습니다.", data);
  }

  // 성공 (커스텀 메시지)
  public static <T> BaseResponse<T> success(String message, T data) {
    return new BaseResponse<>(200, message, data);
  }

  // 성공 (201 Created)
  public static <T> BaseResponse<T> created(String message, T data) {
    return new BaseResponse<>(201, message, data);
  }

  // 성공 (데이터 없음, 삭제 등)
  public static <T> BaseResponse<T> success(String message) {
    return new BaseResponse<>(200, message, null);
  }

  // 실패
  public static <T> BaseResponse<T> fail(int status, String message) {
    return new BaseResponse<>(status, message, null);
  }
}
