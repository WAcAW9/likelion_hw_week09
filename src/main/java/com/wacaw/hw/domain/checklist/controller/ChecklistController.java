package com.wacaw.hw.domain.checklist.controller;

import com.wacaw.hw.domain.checklist.dto.request.ChecklistUpdateRequest;
import com.wacaw.hw.domain.checklist.dto.response.ChecklistResponse;
import com.wacaw.hw.domain.checklist.service.ChecklistService;
import com.wacaw.hw.global.commnon.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Checklist", description = "체크리스트 API")
@RestController
@RequestMapping("/api/checklists")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ChecklistController {

  private final ChecklistService checklistService;

  @Operation(
      summary = "단계별 체크리스트 조회",
      description = """
                계약 단계별 체크리스트 항목과 진행률을 조회합니다.
                
                **stage 값**
                - `BEFORE` : 계약 전
                - `DURING` : 계약 중
                - `AFTER`  : 계약 후
                """
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "조회 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "등록된 계약 없음")
  })
  @GetMapping
  public BaseResponse<ChecklistResponse> getChecklists(
      @Parameter(description = "계약 단계",
          example = "BEFORE",
          required = true)
      @RequestParam String stage) {
    return BaseResponse.success(checklistService.getChecklists(stage));
  }

  @Operation(
      summary = "체크 상태 토글",
      description = "체크리스트 항목의 완료 여부를 변경합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "변경 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "403", description = "접근 권한 없음"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "항목 없음")
  })
  @PutMapping("/{instanceId}")
  public BaseResponse<Void> toggleChecklist(
      @Parameter(description = "체크리스트 인스턴스 ID", example = "1")
      @PathVariable Long instanceId,
      @Valid @RequestBody ChecklistUpdateRequest request) {
    checklistService.toggleChecklist(instanceId, request);
    return BaseResponse.success("체크 상태가 변경되었습니다.");
  }
}
