package com.wacaw.hw.domain.user.controller;

import com.wacaw.hw.domain.user.dto.request.UserUpdateRequest;
import com.wacaw.hw.domain.user.dto.response.UserResponse;
import com.wacaw.hw.domain.user.service.UserService;
import com.wacaw.hw.global.commnon.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

  private final UserService userService;

  @Operation(
      summary = "내 프로필 조회",
      description = "현재 로그인한 사용자의 프로필 정보를 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "조회 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "401", description = "인증 필요")
  })
  @GetMapping("/me")
  public BaseResponse<UserResponse> getMyProfile() {
    return BaseResponse.success(userService.getMyProfile());
  }

  @Operation(
      summary = "프로필 수정",
      description = "현재 로그인한 사용자의 이름과 프로필 이미지를 수정합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "수정 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "401", description = "인증 필요")
  })
  @PutMapping("/me")
  public BaseResponse<UserResponse> updateMyProfile(
      @Valid @RequestBody UserUpdateRequest request) {
    return BaseResponse.success("프로필이 수정되었습니다.",
        userService.updateMyProfile(request));
  }
}
