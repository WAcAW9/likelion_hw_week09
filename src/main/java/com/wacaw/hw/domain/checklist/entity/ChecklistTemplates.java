package com.wacaw.hw.domain.checklist.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checklist_templates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ChecklistTemplates {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long templateId;

  // 체크리스트 항목명 (예: 등기부등본 확인)
  @Column(nullable = false, length = 255)
  private String title;

  // 확인 방법 (예: 대법원 인터넷등기소)
  @Column(length = 255)
  private String confirm;

  // 유의사항
  @Column(length = 255)
  private String notification;

  // 상세 설명
  @Column(length = 255)
  private String description;

  // 연관관계
  @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
  @Builder.Default
  private List<ChecklistInstances> instances = new ArrayList<>();
}
