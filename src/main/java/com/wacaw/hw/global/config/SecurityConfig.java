package com.wacaw.hw.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.wacaw.hw.global.security.JwtAuthenticationFilter;
import com.wacaw.hw.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  // 인증 없이 접근 허용할 경로
  private static final String[] PERMIT_ALL_URLS = {
      "/api/auth/**",         // 회원가입, 로그인
      "/swagger-ui/**",       // Swagger UI
      "/swagger-ui.html",
      "/api-docs/**",
      "/v3/api-docs/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // REST API는 csrf 불필요
        .csrf(AbstractHttpConfigurer::disable)

        // JWT 사용하므로 세션 사용 안함
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 경로별 인증 설정
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(PERMIT_ALL_URLS).permitAll()
            .anyRequest().authenticated()
        )

        // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class
        );

    return http.build();
  }

  // 비밀번호 암호화 Bean
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
