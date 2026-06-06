package com.wacaw.hw.domain.contract.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "계약 등록/수정 요청")
public class ContractRequest {

  @NotBlank(message = "주소는 필수 항목입니다.")
  @Schema(description = "계약 주소", example = "서울특별시 성북구 서경로 124")
  private String location;

  @NotBlank(message = "계약 상태는 필수 항목입니다.")
  @Schema(description = "계약 상태 (BEFORE: 계약전 / DURING: 계약중 / AFTER: 계약후)",
      example = "BEFORE",
      allowableValues = {"BEFORE", "DURING", "AFTER"})
  private String status;

}
