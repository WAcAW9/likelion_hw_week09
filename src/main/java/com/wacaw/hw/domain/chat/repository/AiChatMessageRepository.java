package com.wacaw.hw.domain.chat.repository;

import com.wacaw.hw.domain.chat.entity.AiChatMessages;
import com.wacaw.hw.domain.chat.entity.AiChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatMessageRepository
    extends JpaRepository<AiChatMessages, Long> {

  // 채팅방의 메시지 목록 시간순 조회
  List<AiChatMessages> findByChatRoomOrderByCreatedAtAsc(AiChatRooms chatRoom);

  // 채팅방의 마지막 메시지 1개 조회
  AiChatMessages findTopByChatRoomOrderByCreatedAtDesc(AiChatRooms chatRoom);
}
