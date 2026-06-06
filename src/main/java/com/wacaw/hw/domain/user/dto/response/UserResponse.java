package com.wacaw.hw.domain.user.dto.response;

import com.wacaw.hw.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "사용자 프로필 응답")
public class UserResponse {

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "이메일", example = "user@example.com")
  private String email;

  @Schema(description = "이름", example = "홍길동")
  private String name;

  @Schema(description = "프로필 이미지 URL", example = "https://...")
  private String profileImage;

  @Schema(description = "레벨", example = "초보")
  private String level;

  @Schema(description = "레벨 점수", example = "120")
  private int levelScore;

  @Schema(description = "가입일시")
  private LocalDateTime createdAt;

  // Entity → Response 변환 메서드
  public static UserResponse from(Users user) {
    return UserResponse.builder()
        .userId(user.getUserId())
        .email(user.getEmail())
        .name(user.getName())
        .profileImage(user.getProfileImage())
        .level(user.getLevel())
        .levelScore(user.getLevelScore())
        .createdAt(user.getCreatedAt())
        .build();
  }
}
