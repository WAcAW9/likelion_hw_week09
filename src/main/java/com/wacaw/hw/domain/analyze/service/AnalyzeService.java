package com.wacaw.hw.domain.analyze.service;

import com.wacaw.hw.domain.analyze.dto.request.AnalyzeRequest;
import com.wacaw.hw.domain.analyze.dto.response.AnalyzeResponse;
import com.wacaw.hw.domain.analyze.entity.ContractsAnalyze;
import com.wacaw.hw.domain.analyze.repository.AnalyzeRepository;
import com.wacaw.hw.domain.contract.entity.Contracts;
import com.wacaw.hw.domain.contract.repository.ContractRepository;
import com.wacaw.hw.domain.user.entity.Users;
import com.wacaw.hw.domain.user.repository.UserRepository;
import com.wacaw.hw.global.commnon.SecurityUtil;
import com.wacaw.hw.global.exception.CustomException;
import com.wacaw.hw.global.exception.model.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzeService {

  private final AnalyzeRepository analyzeRepository;
  private final ContractRepository contractRepository;
  private final UserRepository userRepository;

  // 현재 유저 조회 공통 메서드
  private Users getCurrentUser() {
    String email = SecurityUtil.getCurrentUserEmail();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));
  }

  // 계약서 분석 요청
  @Transactional
  public AnalyzeResponse analyze(AnalyzeRequest request) {
    Users user = getCurrentUser();

    Contracts contract = contractRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CONTRACT_NOT_FOUND));

    // 가짜 AI 분석 결과 생성
    FakeAnalysisResult fakeResult = generateFakeAnalysis(request.getClauseText());

    ContractsAnalyze analyze = ContractsAnalyze.builder()
        .user(user)
        .contract(contract)
        .clauseText(request.getClauseText())
        .description(fakeResult.description())
        .aiDescription(fakeResult.aiDescription())
        .notification(fakeResult.notification())
        .build();

    analyzeRepository.save(analyze);

    return AnalyzeResponse.from(analyze);
  }

  // 분석 이력 목록 조회
  @Transactional(readOnly = true)
  public List<AnalyzeResponse.AnalyzeSummaryResponse> getAnalyzeList() {
    Users user = getCurrentUser();

    return analyzeRepository.findByUserOrderByCreatedAtDesc(user)
        .stream()
        .map(AnalyzeResponse.AnalyzeSummaryResponse::from)
        .toList();
  }

  // 분석 상세 조회
  @Transactional(readOnly = true)
  public AnalyzeResponse getAnalyzeDetail(Long contractAnalyzeId) {
    Users user = getCurrentUser();

    ContractsAnalyze analyze = analyzeRepository
        .findByContractAnalyzeIdAndUser(contractAnalyzeId, user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.ANALYZE_NOT_FOUND));

    return AnalyzeResponse.from(analyze);
  }

  // 가짜 AI 분석 데이터 생성
  private FakeAnalysisResult generateFakeAnalysis(String clauseText) {

    return new FakeAnalysisResult(
        "임차인이 2개월이상 차임을 연체할 경우, 임대인은 즉시 계약을 해지할 수 있다는 내용이예요.",
        "월세를 2달 이상 못내면, 집주인이 바로 계약을 끊을 수 있다는 뜻이예요. 법적으로도 2개월 이상 연체시 해지 가능해서 이것은 표준 조항이예요",
        "표준 범위 안의 조항이에요, 다만 월세 납부일과 방법(계좌이체 등)을 특약에 명시해두면 나중에 분쟁을 예방할 수 있어요!"
    );
  }

  // 가짜 분석 결과 레코드
  private record FakeAnalysisResult(
      String description,
      String aiDescription,
      String notification
  ) {}
}
