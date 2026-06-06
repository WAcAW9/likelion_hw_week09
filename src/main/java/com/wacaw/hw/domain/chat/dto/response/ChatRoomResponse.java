package com.wacaw.hw.domain.chat.dto.response;

import com.wacaw.hw.domain.chat.entity.AiChatRooms;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "채팅방 응답")
public class ChatRoomResponse {

  @Schema(description = "채팅방 ID", example = "1")
  private Long chatId;

  @Schema(
      description = "마지막 메시지 내용",
      example = "계약서에 수리비 전액 임대인 면책이라는 문구가 있는데..."
  )
  private String lastMessage;

  @Schema(description = "채팅방 생성일시")
  private LocalDateTime createdAt;

  public static ChatRoomResponse from(AiChatRooms room, String lastMessage) {
    return ChatRoomResponse.builder()
        .chatId(room.getChatId())
        .lastMessage(lastMessage)
        .createdAt(room.getCreatedAt())
        .build();
  }
}
