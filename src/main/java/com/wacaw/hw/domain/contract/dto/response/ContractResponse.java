package com.wacaw.hw.domain.contract.dto.response;

import com.wacaw.hw.domain.contract.entity.Contracts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "계약 응답")
public class ContractResponse {

  @Schema(description = "계약 ID", example = "1")
  private Long contractId;

  @Schema(description = "계약 주소", example = "서울특별시 성북구 서경로 124")
  private String location;

  @Schema(description = "계약 상태", example = "BEFORE")
  private String status;

  @Schema(description = "등록일시")
  private LocalDateTime createdAt;

  // Entity → Response 변환
  public static ContractResponse from(Contracts contract) {
    return ContractResponse.builder()
        .contractId(contract.getContractId())
        .location(contract.getLocation())
        .status(contract.getStatus())
        .createdAt(contract.getCreatedAt())
        .build();
  }
}
