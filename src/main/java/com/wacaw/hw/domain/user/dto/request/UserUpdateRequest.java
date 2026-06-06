package com.wacaw.hw.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "프로필 수정 요청")
public class UserUpdateRequest {

  @NotBlank(message = "이름은 필수 항목입니다.")
  @Schema(description = "변경할 이름", example = "김철수")
  private String name;

  @Schema(description = "변경할 프로필 이미지 URL", example = "https://...")
  private String profileImage;
}
