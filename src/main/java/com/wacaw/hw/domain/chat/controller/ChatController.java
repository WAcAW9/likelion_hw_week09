package com.wacaw.hw.domain.chat.controller;

import com.wacaw.hw.domain.chat.dto.request.ChatMessageRequest;
import com.wacaw.hw.domain.chat.dto.response.ChatMessageResponse;
import com.wacaw.hw.domain.chat.dto.response.ChatRoomResponse;
import com.wacaw.hw.domain.chat.service.ChatService;
import com.wacaw.hw.global.commnon.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "AI 채팅 API")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ChatController {

  private final ChatService chatService;

  @Operation(
      summary = "채팅방 생성",
      description = "새로운 AI 채팅방을 생성합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "201", description = "생성 성공")
  })
  @PostMapping("/rooms")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<ChatRoomResponse> createChatRoom() {
    return BaseResponse.created("채팅방이 생성되었습니다.",
        chatService.createChatRoom());
  }

  @Operation(
      summary = "채팅방 목록 조회",
      description = "내 채팅방 목록을 최신순으로 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "조회 성공")
  })
  @GetMapping("/rooms")
  public BaseResponse<List<ChatRoomResponse>> getChatRooms() {
    return BaseResponse.success(chatService.getChatRooms());
  }

  @Operation(
      summary = "메시지 전송",
      description = """
                메시지를 전송하면 AI가 즉시 응답합니다(가짜 응답 데이터 반환)
                """
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "201", description = "전송 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "403", description = "접근 권한 없음"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "채팅방 없음")
  })
  @PostMapping("/rooms/{chatId}/messages")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<ChatMessageResponse> sendMessage(
      @Parameter(description = "채팅방 ID", example = "1")
      @PathVariable Long chatId,
      @Valid @RequestBody ChatMessageRequest request) {
    return BaseResponse.created("메시지가 전송되었습니다.",
        chatService.sendMessage(chatId, request));
  }

  @Operation(
      summary = "메시지 이력 조회",
      description = "채팅방의 전체 대화 이력을 시간순으로 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "조회 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "채팅방 없음")
  })
  @GetMapping("/rooms/{chatId}/messages")
  public BaseResponse<ChatMessageResponse.ChatHistoryResponse> getChatHistory(
      @Parameter(description = "채팅방 ID", example = "1")
      @PathVariable Long chatId) {
    return BaseResponse.success(chatService.getChatHistory(chatId));
  }

  @Operation(
      summary = "채팅방 삭제",
      description = "채팅방과 모든 대화 이력을 삭제합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "삭제 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "채팅방 없음")
  })
  @DeleteMapping("/rooms/{chatId}")
  public BaseResponse<Void> deleteChatRoom(
      @Parameter(description = "채팅방 ID", example = "1")
      @PathVariable Long chatId) {
    chatService.deleteChatRoom(chatId);
    return BaseResponse.success("채팅방이 삭제되었습니다.");
  }
}
