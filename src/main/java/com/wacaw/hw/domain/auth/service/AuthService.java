package com.wacaw.hw.domain.auth.service;

import com.wacaw.hw.domain.auth.dto.request.LoginRequest;
import com.wacaw.hw.domain.auth.dto.request.SignupRequest;
import com.wacaw.hw.domain.auth.dto.request.TokenRefreshRequest;
import com.wacaw.hw.domain.auth.dto.response.TokenResponse;
import com.wacaw.hw.domain.user.entity.Users;
import com.wacaw.hw.domain.user.repository.UserRepository;
import com.wacaw.hw.global.exception.CustomException;
import com.wacaw.hw.global.exception.model.BaseErrorCode;
import com.wacaw.hw.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  // 회원가입
  @Transactional
  public void signup(SignupRequest request) {

    // 이메일 중복 확인
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(BaseErrorCode.DUPLICATE_EMAIL);
    }

    // 비밀번호 암호화 후 저장
    Users user = Users.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .build();

    userRepository.save(user);
  }

  // 로그인
  @Transactional(readOnly = true)
  public TokenResponse login(LoginRequest request) {

    // 이메일로 사용자 조회
    Users user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));

    // 비밀번호 확인
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new CustomException(BaseErrorCode.INVALID_PASSWORD);
    }

    // JWT 토큰 발급
    String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
    String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .tokenType("Bearer")
        .userId(user.getUserId())
        .name(user.getName())
        .level(user.getLevel())
        .build();
  }

  // 토큰 갱신
  public TokenResponse refresh(TokenRefreshRequest request) {

    String refreshToken = request.getRefreshToken();

    // Refresh Token 유효성 검증
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new CustomException(BaseErrorCode.EXPIRED_REFRESH_TOKEN);
    }

    // 토큰에서 이메일 추출 후 사용자 조회
    String email = jwtTokenProvider.getEmailFromToken(refreshToken);
    userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));

    // 새 Access Token 발급
    String newAccessToken = jwtTokenProvider.generateAccessToken(email);

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .tokenType("Bearer")
        .build();
  }
}
