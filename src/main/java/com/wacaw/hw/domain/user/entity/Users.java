package com.wacaw.hw.domain.user.entity;


import com.wacaw.hw.domain.chat.entity.AiChatRooms;
import com.wacaw.hw.domain.contract.entity.Contracts;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 255)
  private String profileImage;

  @Column(nullable = false, length = 100)
  @Builder.Default
  private String level = "초보";

  @Column(nullable = false)
  @Builder.Default
  private int levelScore = 0;

  @Column(nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime updatedAt = LocalDateTime.now();

  // 연관관계
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Contracts> contracts = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<AiChatRooms> aiChatRooms = new ArrayList<>();

  // 프로필 수정 메서드
  public void updateProfile(String name, String profileImage) {
    this.name = name;
    if (profileImage != null) this.profileImage = profileImage;
    this.updatedAt = LocalDateTime.now();
  }
}
