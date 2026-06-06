package com.wacaw.hw.domain.checklist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "체크리스트 상태 변경 요청")
public class ChecklistUpdateRequest {

  @NotNull(message = "체크 여부는 필수 항목입니다.")
  @Schema(description = "체크 여부", example = "true")
  private Boolean isChecked;
}
