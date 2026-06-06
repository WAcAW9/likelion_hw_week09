package com.wacaw.hw.domain.auth.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "회원가입 요청")
public class SignupRequest {

  @NotBlank(message = "이메일은 필수 항목입니다.")
  @Email(message = "이메일 형식이 올바르지 않습니다.")
  @Schema(description = "이메일", example = "user@example.com")
  private String email;

  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
  @Schema(description = "비밀번호 (8자 이상)", example = "password123!")
  private String password;

  @NotBlank(message = "이름은 필수 항목입니다.")
  @Schema(description = "이름", example = "홍길동")
  private String name;
}
