package com.wacaw.hw.domain.auth.controller;

import com.wacaw.hw.domain.auth.dto.request.LoginRequest;
import com.wacaw.hw.domain.auth.dto.request.SignupRequest;
import com.wacaw.hw.domain.auth.dto.request.TokenRefreshRequest;
import com.wacaw.hw.domain.auth.dto.response.TokenResponse;
import com.wacaw.hw.domain.auth.service.AuthService;
import com.wacaw.hw.global.commnon.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "회원가입", description = "이메일, 비밀번호, 이름으로 회원가입합니다.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이메일 중복")
  })
  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<Void> signup(@Valid @RequestBody SignupRequest request) {
    authService.signup(request);
    return BaseResponse.success("회원가입이 완료되었습니다.");
  }

  @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인 후 JWT 토큰을 발급받습니다.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "비밀번호 불일치"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "이메일 없음")
  })
  @PostMapping("/login")
  public BaseResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
    TokenResponse response = authService.login(request);
    return BaseResponse.success("로그인에 성공했습니다.", response);
  }

  @Operation(summary = "토큰 갱신", description = "Refresh Token으로 새로운 Access Token을 발급받습니다.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh Token 만료")
  })
  @PostMapping("/refresh")
  public BaseResponse<TokenResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
    TokenResponse response = authService.refresh(request);
    return BaseResponse.success("토큰이 갱신되었습니다.", response);
  }
}
