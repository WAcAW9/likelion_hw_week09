package com.wacaw.hw.domain.analyze.dto.response;

import com.wacaw.hw.domain.analyze.entity.ContractsAnalyze;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "계약서 분석 응답")
public class AnalyzeResponse {

  @Schema(description = "분석 ID", example = "1")
  private Long contractAnalyzeId;

  @Schema(
      description = "입력된 계약서 원문",
      example = "임차인이 2개월 이상 차임을 연체할 경우..."
  )
  private String clauseText;

  @Schema(
      description = "계약서 내용 요약",
      example = "월세를 2달 이상 못 내면 집주인이 바로 계약을 끊을 수 있다는 뜻이에요."
  )
  private String description;

  @Schema(
      description = "AI 해석",
      example = "법적으로도 2개월 이상 연체 시 해지 가능해서 이것은 표준 조항이에요."
  )
  private String aiDescription;

  @Schema(
      description = "유의사항",
      example = "월세 납부일과 방법을 특약에 명시해두면 분쟁 예방 가능해요."
  )
  private String notification;

  @Schema(description = "분석 일시")
  private LocalDateTime createdAt;

  // Entity → Response 변환
  public static AnalyzeResponse from(ContractsAnalyze analyze) {
    return AnalyzeResponse.builder()
        .contractAnalyzeId(analyze.getContractAnalyzeId())
        .clauseText(analyze.getClauseText())
        .description(analyze.getDescription())
        .aiDescription(analyze.getAiDescription())
        .notification(analyze.getNotification())
        .createdAt(analyze.getCreatedAt())
        .build();
  }

  // 목록용 간략 응답 (상세 내용 제외)
  @Getter
  @Builder
  @Schema(description = "계약서 분석 목록 응답 (간략)")
  public static class AnalyzeSummaryResponse {

    @Schema(description = "분석 ID", example = "1")
    private Long contractAnalyzeId;

    @Schema(
        description = "계약서 원문 앞 50자",
        example = "임차인이 2개월 이상 차임을 연체할 경우..."
    )
    private String clauseTextPreview;

    @Schema(description = "분석 일시")
    private LocalDateTime createdAt;

    public static AnalyzeSummaryResponse from(ContractsAnalyze analyze) {
      // 원문이 50자 넘으면 잘라서 "..." 붙이기
      String preview = analyze.getClauseText().length() > 50
          ? analyze.getClauseText().substring(0, 50) + "..."
          : analyze.getClauseText();

      return AnalyzeSummaryResponse.builder()
          .contractAnalyzeId(analyze.getContractAnalyzeId())
          .clauseTextPreview(preview)
          .createdAt(analyze.getCreatedAt())
          .build();
    }
  }
}
