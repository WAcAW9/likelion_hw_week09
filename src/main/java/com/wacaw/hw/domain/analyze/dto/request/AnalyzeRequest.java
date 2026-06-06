package com.wacaw.hw.domain.analyze.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "계약서 분석 요청")
public class AnalyzeRequest {

  @NotBlank(message = "계약서 내용은 필수 항목입니다.")
  @Schema(
      description = "분석할 계약서 조항 텍스트",
      example = "임차인이 2개월 이상 차임을 연체할 경우 임대인은 즉시 계약을 해지할 수 있다."
  )
  private String clauseText;
}
