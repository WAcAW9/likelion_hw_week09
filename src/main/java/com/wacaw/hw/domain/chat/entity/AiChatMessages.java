package com.wacaw.hw.domain.chat.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_chat_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AiChatMessages {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long messageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatId", nullable = false)
  private AiChatRooms chatRoom;

  // USER 또는 AI
  @Column(nullable = false, length = 10)
  private String sender;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
