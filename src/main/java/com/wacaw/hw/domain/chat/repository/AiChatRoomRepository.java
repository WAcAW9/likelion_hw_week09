package com.wacaw.hw.domain.chat.repository;

import com.wacaw.hw.domain.chat.entity.AiChatRooms;
import com.wacaw.hw.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AiChatRoomRepository extends JpaRepository<AiChatRooms, Long> {

  // 유저의 채팅방 목록 최신순 조회
  List<AiChatRooms> findByUserOrderByCreatedAtDesc(Users user);

  // 채팅방 ID + 유저로 단건 조회 (권한 확인용)
  Optional<AiChatRooms> findByChatIdAndUser(Long chatId, Users user);
}
