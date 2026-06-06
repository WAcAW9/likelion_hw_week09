package com.wacaw.hw.domain.chat.dto.response;

import com.wacaw.hw.domain.chat.entity.AiChatMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "채팅 메시지 전송 응답")
public class ChatMessageResponse {

  @Schema(description = "사용자 메시지")
  private MessageDto userMessage;

  @Schema(description = "AI 응답 메시지")
  private MessageDto aiMessage;

  // 메시지 단건 DTO
  @Getter
  @Builder
  @Schema(description = "메시지 단건")
  public static class MessageDto {

    @Schema(description = "메시지 ID", example = "1")
    private Long messageId;

    @Schema(description = "발신자 (USER / AI)", example = "USER")
    private String sender;

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String content;

    @Schema(description = "전송 일시")
    private LocalDateTime createdAt;

    public static MessageDto from(AiChatMessages message) {
      return MessageDto.builder()
          .messageId(message.getMessageId())
          .sender(message.getSender())
          .content(message.getContent())
          .createdAt(message.getCreatedAt())
          .build();
    }
  }

  // 메시지 이력 조회용 DTO
  @Getter
  @Builder
  @Schema(description = "메시지 이력 응답")
  public static class ChatHistoryResponse {

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatId;

    @Schema(description = "메시지 목록")
    private List<MessageDto> messages;

    public static ChatHistoryResponse of(Long chatId, List<MessageDto> messages) {
      return ChatHistoryResponse.builder()
          .chatId(chatId)
          .messages(messages)
          .build();
    }
  }
}
