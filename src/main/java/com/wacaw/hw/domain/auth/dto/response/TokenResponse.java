package com.wacaw.hw.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "토큰 응답")
public class TokenResponse {

  @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiJ9...")
  private String accessToken;

  @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9...")
  private String refreshToken;

  @Schema(description = "토큰 타입", example = "Bearer")
  private String tokenType;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "이름", example = "홍길동")
  private String name;

  @Schema(description = "레벨", example = "초보")
  private String level;
}
