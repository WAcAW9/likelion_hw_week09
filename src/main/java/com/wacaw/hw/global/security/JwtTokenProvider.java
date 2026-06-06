package com.wacaw.hw.global.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long accessTokenExpiration;
  private final long refreshTokenExpiration;
  private final CustomUserDetailsService userDetailsService;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
      @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
      CustomUserDetailsService userDetailsService) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.accessTokenExpiration = accessTokenExpiration;
    this.refreshTokenExpiration = refreshTokenExpiration;
    this.userDetailsService = userDetailsService;
  }

  // Access Token 생성
  public String generateAccessToken(String email) {
    return generateToken(email, accessTokenExpiration);
  }

  // Refresh Token 생성
  public String generateRefreshToken(String email) {
    return generateToken(email, refreshTokenExpiration);
  }

  // 토큰 생성 공통 메서드
  private String generateToken(String email, long expiration) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .subject(email)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();
  }

  // 토큰에서 이메일 추출
  public String getEmailFromToken(String token) {
    return parseClaims(token).getSubject();
  }

  // 토큰 유효성 검증
  public boolean validateToken(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.error("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 JWT 토큰입니다.");
    } catch (MalformedJwtException e) {
      log.error("잘못된 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.error("JWT 토큰이 비어있습니다.");
    }
    return false;
  }

  // 토큰으로 Authentication 객체 생성
  public Authentication getAuthentication(String token) {
    String email = getEmailFromToken(token);
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    return new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities()
    );
  }

  // Claims 파싱
  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
