package com.wacaw.hw.domain.analyze.controller;

import com.wacaw.hw.domain.analyze.dto.request.AnalyzeRequest;
import com.wacaw.hw.domain.analyze.dto.response.AnalyzeResponse;
import com.wacaw.hw.domain.analyze.service.AnalyzeService;
import com.wacaw.hw.global.commnon.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Analyze", description = "계약서 분석 API")
@RestController
@RequestMapping("/api/contracts/analyze")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AnalyzeController {

  private final AnalyzeService analyzeService;

  @Operation(
      summary = "계약서 분석 요청",
      description = """
                계약서 이미지를 입력하면 AI가 분석한 결과를 반환합니다.(아무 텍스트나 입력하면, AI 결과 반환)
                """
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "201", description = "분석 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "등록된 계약 없음")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<AnalyzeResponse> analyze(
      @Valid @RequestBody AnalyzeRequest request) {
    return BaseResponse.created("계약서 분석이 완료되었습니다.",
        analyzeService.analyze(request));
  }

  @Operation(
      summary = "계약서 분석 이력 목록 조회",
      description = "분석 요청 이력을 최신순으로 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "조회 성공")
  })
  @GetMapping
  public BaseResponse<List<AnalyzeResponse.AnalyzeSummaryResponse>> getAnalyzeList() {
    return BaseResponse.success(analyzeService.getAnalyzeList());
  }

  @Operation(
      summary = "계약서 분석 상세 조회",
      description = "특정 분석 결과의 상세 내용을 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "조회 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "403", description = "접근 권한 없음"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404", description = "분석 결과 없음")
  })
  @GetMapping("/{contractAnalyzeId}")
  public BaseResponse<AnalyzeResponse> getAnalyzeDetail(
      @Parameter(description = "분석 ID", example = "1")
      @PathVariable Long contractAnalyzeId) {
    return BaseResponse.success(
        analyzeService.getAnalyzeDetail(contractAnalyzeId));
  }
}
