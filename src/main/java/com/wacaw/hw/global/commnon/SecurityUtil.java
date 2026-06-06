package com.wacaw.hw.global.commnon;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

  private SecurityUtil() {}

  // SecurityContext에서 현재 로그인한 유저의 이메일 추출
  public static String getCurrentUserEmail() {
    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("인증 정보가 없습니다.");
    }

    return authentication.getName();
  }
}
