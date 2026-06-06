package com.wacaw.hw.domain.analyze.entity;


import com.wacaw.hw.domain.contract.entity.Contracts;
import com.wacaw.hw.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contracts_analyze")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ContractsAnalyze {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long contractAnalyzeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private Users user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contractId", nullable = false)
  private Contracts contract;

  // 계약서 원문 텍스트
  @Column(nullable = false, columnDefinition = "TEXT")
  private String clauseText;

  // 계약서 내용 요약
  @Column(columnDefinition = "TEXT")
  private String description;

  // AI 해석
  @Column(columnDefinition = "TEXT")
  private String aiDescription;

  // 유의사항
  @Column(columnDefinition = "TEXT")
  private String notification;

  @Column(nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
