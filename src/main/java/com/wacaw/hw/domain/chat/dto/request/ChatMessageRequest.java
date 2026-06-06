package com.wacaw.hw.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "채팅 메시지 전송 요청")
public class ChatMessageRequest {

  @NotBlank(message = "메시지를 입력해주세요.")
  @Schema(
      description = "사용자 메시지",
      example = "계약서에 수리비 전액 임대인 면책이라는 문구가 있는데 괜찮은 건가요?"
  )
  private String message;
}
