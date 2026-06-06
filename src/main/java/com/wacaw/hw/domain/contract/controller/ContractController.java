package com.wacaw.hw.domain.contract.controller;

import com.wacaw.hw.domain.contract.dto.request.ContractRequest;
import com.wacaw.hw.domain.contract.dto.response.ContractResponse;
import com.wacaw.hw.domain.contract.service.ContractService;
import com.wacaw.hw.global.commnon.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Contract", description = "계약 API")
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ContractController {

  private final ContractService contractService;

  @Operation(
      summary = "내 계약 조회",
      description = "현재 로그인한 사용자의 계약 정보를 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "조회 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "등록된 계약 없음")
  })
  @GetMapping
  public BaseResponse<ContractResponse> getMyContract() {
    return BaseResponse.success(contractService.getMyContract());
  }

  @Operation(
      summary = "계약 등록",
      description = """
                새로운 계약을 등록합니다.
                회원 1명당 계약은 1개만 등록 가능합니다.
                계약 등록 시 계약전/중/후 체크리스트가 자동으로 생성됩니다.
                """
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "201", description = "등록 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "409", description = "이미 계약 존재")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<ContractResponse> createContract(
      @Valid @RequestBody ContractRequest request) {
    return BaseResponse.created("계약이 등록되었습니다.",
        contractService.createContract(request));
  }

  @Operation(
      summary = "계약 수정",
      description = "등록된 계약 정보를 수정합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "수정 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "계약 없음")
  })
  @PutMapping
  public BaseResponse<ContractResponse> updateContract(
      @Valid @RequestBody ContractRequest request) {
    return BaseResponse.success("계약 정보가 수정되었습니다.",
        contractService.updateContract(request));
  }

  @Operation(
      summary = "계약 삭제",
      description = "등록된 계약을 삭제합니다. 관련 체크리스트도 함께 삭제됩니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "삭제 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "계약 없음")
  })
  @DeleteMapping
  public BaseResponse<Void> deleteContract() {
    contractService.deleteContract();
    return BaseResponse.success("계약이 삭제되었습니다.");
  }
}
