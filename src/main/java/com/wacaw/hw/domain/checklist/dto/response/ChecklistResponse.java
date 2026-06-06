package com.wacaw.hw.domain.checklist.dto.response;

import com.wacaw.hw.domain.checklist.entity.ChecklistInstances;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "체크리스트 응답")
public class ChecklistResponse {

  @Schema(description = "단계", example = "BEFORE",
      allowableValues = {"BEFORE", "DURING", "AFTER"})
  private String stage;

  @Schema(description = "전체 항목 수", example = "5")
  private int totalCount;

  @Schema(description = "완료 항목 수", example = "2")
  private int checkedCount;

  @Schema(description = "진행률 (%)", example = "40")
  private int progressRate;

  @Schema(description = "체크리스트 항목 목록")
  private List<ChecklistItemResponse> items;

  // 단건 항목 응답
  @Getter
  @Builder
  @Schema(description = "체크리스트 단건 항목")
  public static class ChecklistItemResponse {

    @Schema(description = "인스턴스 ID", example = "1")
    private Long instanceId;

    @Schema(description = "템플릿 ID", example = "3")
    private Long templateId;

    @Schema(description = "항목명", example = "등기부등본 확인")
    private String title;

    @Schema(description = "확인 방법", example = "대법원 인터넷등기소(iros.go.kr)")
    private String confirm;

    @Schema(description = "유의사항",
        example = "소유자가 계약하는 집주인과 동일인지 확인")
    private String notification;

    @Schema(description = "상세 설명",
        example = "근저당권 설정 금액 확인")
    private String description;

    @Schema(description = "체크 여부", example = "true")
    private boolean isChecked;

    // Entity → Response 변환
    public static ChecklistItemResponse from(ChecklistInstances instance) {
      return ChecklistItemResponse.builder()
          .instanceId(instance.getInstanceId())
          .templateId(instance.getTemplate().getTemplateId())
          .title(instance.getTemplate().getTitle())
          .confirm(instance.getTemplate().getConfirm())
          .notification(instance.getTemplate().getNotification())
          .description(instance.getTemplate().getDescription())
          .isChecked(instance.isChecked())
          .build();
    }
  }
}
