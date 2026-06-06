package com.wacaw.hw.domain.chat.service;

import com.wacaw.hw.domain.chat.dto.request.ChatMessageRequest;
import com.wacaw.hw.domain.chat.dto.response.ChatMessageResponse;
import com.wacaw.hw.domain.chat.dto.response.ChatRoomResponse;
import com.wacaw.hw.domain.chat.entity.AiChatMessages;
import com.wacaw.hw.domain.chat.entity.AiChatRooms;
import com.wacaw.hw.domain.chat.repository.AiChatMessageRepository;
import com.wacaw.hw.domain.chat.repository.AiChatRoomRepository;
import com.wacaw.hw.domain.user.entity.Users;
import com.wacaw.hw.domain.user.repository.UserRepository;
import com.wacaw.hw.global.commnon.SecurityUtil;
import com.wacaw.hw.global.exception.CustomException;
import com.wacaw.hw.global.exception.model.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final AiChatRoomRepository chatRoomRepository;
  private final AiChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;

  // 현재 유저 조회 공통 메서드
  private Users getCurrentUser() {
    String email = SecurityUtil.getCurrentUserEmail();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));
  }

  // 채팅방 생성
  @Transactional
  public ChatRoomResponse createChatRoom() {
    Users user = getCurrentUser();

    AiChatRooms room = AiChatRooms.builder()
        .user(user)
        .build();

    chatRoomRepository.save(room);

    return ChatRoomResponse.from(room, "새 대화를 시작해보세요!");
  }

  // 채팅방 목록 조회
  @Transactional(readOnly = true)
  public List<ChatRoomResponse> getChatRooms() {
    Users user = getCurrentUser();

    return chatRoomRepository.findByUserOrderByCreatedAtDesc(user)
        .stream()
        .map(room -> {
          // 마지막 메시지 조회
          AiChatMessages lastMsg =
              chatMessageRepository
                  .findTopByChatRoomOrderByCreatedAtDesc(room);
          String lastMessageContent = lastMsg != null
              ? lastMsg.getContent()
              : "대화를 시작해보세요!";
          return ChatRoomResponse.from(room, lastMessageContent);
        })
        .toList();
  }

  // 메시지 전송
  @Transactional
  public ChatMessageResponse sendMessage(Long chatId, ChatMessageRequest request) {
    Users user = getCurrentUser();

    // 채팅방 조회 (권한 확인)
    AiChatRooms room = chatRoomRepository.findByChatIdAndUser(chatId, user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CHAT_ROOM_NOT_FOUND));

    // 유저 메시지 저장
    AiChatMessages userMessage = AiChatMessages.builder()
        .chatRoom(room)
        .sender("USER")
        .content(request.getMessage())
        .build();
    chatMessageRepository.save(userMessage);

    // 가짜 AI 응답 생성
    String aiContent = generateFakeAiResponse(request.getMessage());

    // AI 메시지 저장
    AiChatMessages aiMessage = AiChatMessages.builder()
        .chatRoom(room)
        .sender("AI")
        .content(aiContent)
        .build();
    chatMessageRepository.save(aiMessage);

    return ChatMessageResponse.builder()
        .userMessage(ChatMessageResponse.MessageDto.from(userMessage))
        .aiMessage(ChatMessageResponse.MessageDto.from(aiMessage))
        .build();
  }

  // 메시지 이력 조회
  @Transactional(readOnly = true)
  public ChatMessageResponse.ChatHistoryResponse getChatHistory(Long chatId) {
    Users user = getCurrentUser();

    AiChatRooms room = chatRoomRepository.findByChatIdAndUser(chatId, user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CHAT_ROOM_NOT_FOUND));

    List<ChatMessageResponse.MessageDto> messages =
        chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(room)
            .stream()
            .map(ChatMessageResponse.MessageDto::from)
            .toList();

    return ChatMessageResponse.ChatHistoryResponse.of(chatId, messages);
  }

  // 채팅방 삭제
  @Transactional
  public void deleteChatRoom(Long chatId) {
    Users user = getCurrentUser();

    AiChatRooms room = chatRoomRepository.findByChatIdAndUser(chatId, user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CHAT_ROOM_NOT_FOUND));

    chatRoomRepository.delete(room);
  }

  // 가짜 AI 응답 생성
  private String generateFakeAiResponse(String message) {

    return "안녕하세요! 부동산 AI봇 집집이에요 🏠\n\n" +
        "저는 부동산 계약과 관련된 궁금한 점을 도와드려요.\n\n" +
        "이런 것들을 물어보실 수 있어요!\n" +
        "- 전세사기 예방 방법\n" +
        "- 등기부등본 보는 법\n" +
        "- 확정일자, 전입신고 방법\n" +
        "- 계약서 특약 주의사항\n" +
        "- 보증금 반환 관련\n\n" +
        "무엇이든 편하게 물어보세요!";

  }
}
