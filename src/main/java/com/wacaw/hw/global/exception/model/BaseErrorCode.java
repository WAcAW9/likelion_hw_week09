package com.wacaw.hw.global.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseErrorCode {

  // 공통
  INVALID_INPUT(400, "입력값이 올바르지 않습니다."),
  UNAUTHORIZED(401, "인증이 필요합니다."),
  FORBIDDEN(403, "접근 권한이 없습니다."),
  NOT_FOUND(404, "리소스를 찾을 수 없습니다."),
  INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),

  // 인증 (Auth)
  INVALID_PASSWORD(401, "비밀번호가 일치하지 않습니다."),
  INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
  EXPIRED_TOKEN(401, "만료된 토큰입니다."),
  EXPIRED_REFRESH_TOKEN(401, "Refresh Token이 만료되었습니다. 다시 로그인해주세요."),

  // 사용자 (User)
  USER_NOT_FOUND(404, "존재하지 않는 사용자입니다."),
  DUPLICATE_EMAIL(409, "이미 사용 중인 이메일입니다."),

  // 계약 (Contract)
  CONTRACT_NOT_FOUND(404, "등록된 계약이 없습니다."),
  CONTRACT_ALREADY_EXISTS(409, "이미 등록된 계약이 있습니다."),

  // 체크리스트 (Checklist)
  CHECKLIST_NOT_FOUND(404, "체크리스트 항목을 찾을 수 없습니다."),

  // 계약서 분석 (Analyze)
  ANALYZE_NOT_FOUND(404, "분석 결과를 찾을 수 없습니다."),
  INVALID_FILE_FORMAT(400, "jpg, png, pdf 형식만 지원합니다."),
  ANALYZE_FAILED(500, "계약서 분석에 실패했습니다. 다시 시도해주세요."),

  // AI 채팅 (Chat)
  CHAT_ROOM_NOT_FOUND(404, "채팅방을 찾을 수 없습니다."),
  EMPTY_MESSAGE(400, "메시지를 입력해주세요.");

  private final int status;
  private final String message;
}
