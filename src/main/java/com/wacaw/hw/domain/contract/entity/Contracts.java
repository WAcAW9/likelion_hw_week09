package com.wacaw.hw.domain.contract.entity;


import com.wacaw.hw.domain.analyze.entity.ContractsAnalyze;
import com.wacaw.hw.domain.checklist.entity.ChecklistInstances;
import com.wacaw.hw.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contracts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Contracts {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long contractId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private Users user;

  @Column(nullable = false, length = 255)
  private String location;

  // BEFORE(계약전), DURING(계약중), AFTER(계약후)
  @Column(nullable = false, length = 50)
  @Builder.Default
  private String status = "BEFORE";


  @Column(nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  // 연관관계
  @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ChecklistInstances> checklistInstances = new ArrayList<>();

  @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ContractsAnalyze> contractsAnalyzes = new ArrayList<>();

  // 계약 수정 메서드
  public void update(String location, String contractType) {
    this.location = location;
    this.status = status;
  }
}
