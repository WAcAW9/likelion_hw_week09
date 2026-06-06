package com.wacaw.hw.domain.checklist.entity;


import com.wacaw.hw.domain.contract.entity.Contracts;
import com.wacaw.hw.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_instances")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ChecklistInstances {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long instanceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contractId", nullable = false)
  private Contracts contract;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "templateId", nullable = false)
  private ChecklistTemplates template;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private Users user;

  // BEFORE(계약전), DURING(계약중), AFTER(계약후)
  @Column(nullable = false, length = 50)
  private String stage;

  @Column(nullable = false)
  @Builder.Default
  private boolean isChecked = false;

  @Column(nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  // 체크 상태 토글 메서드
  public void toggleChecked(boolean isChecked) {
    this.isChecked = isChecked;
  }
}
