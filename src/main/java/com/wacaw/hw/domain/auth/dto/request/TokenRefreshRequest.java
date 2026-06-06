package com.wacaw.hw.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "토큰 갱신 요청")
public class TokenRefreshRequest {

  @NotBlank(message = "Refresh Token은 필수 항목입니다.")
  @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9...")
  private String refreshToken;
}
