package com.wacaw.hw.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {

    // JWT Bearer 인증 스킴 이름
    String jwtSchemeName = "Bearer Authentication";

    // API 전역에 JWT 인증 적용
    SecurityRequirement securityRequirement =
        new SecurityRequirement().addList(jwtSchemeName);

    // JWT Bearer 스킴 정의
    SecurityScheme securityScheme = new SecurityScheme()
        .name(jwtSchemeName)
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");

    return new OpenAPI()
        .info(new Info()
            .title("API 명세서(9주차 과제)")
            .version("v1.0.0"))
        .addSecurityItem(securityRequirement)
        .components(new Components()
            .addSecuritySchemes(jwtSchemeName, securityScheme));
  }
}
