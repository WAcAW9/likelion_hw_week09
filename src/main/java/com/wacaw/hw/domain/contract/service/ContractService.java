package com.wacaw.hw.domain.contract.service;

import com.wacaw.hw.domain.checklist.entity.ChecklistInstances;
import com.wacaw.hw.domain.checklist.entity.ChecklistTemplates;
import com.wacaw.hw.domain.checklist.repository.ChecklistInstanceRepository;
import com.wacaw.hw.domain.checklist.repository.ChecklistTemplateRepository;
import com.wacaw.hw.domain.contract.dto.request.ContractRequest;
import com.wacaw.hw.domain.contract.dto.response.ContractResponse;
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
public class ContractService {

  private final ContractRepository contractRepository;
  private final UserRepository userRepository;
  private final ChecklistTemplateRepository checklistTemplateRepository;
  private final ChecklistInstanceRepository checklistInstanceRepository;

  // 현재 유저 조회 공통 메서드
  private Users getCurrentUser() {
    String email = SecurityUtil.getCurrentUserEmail();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));
  }

  // 내 계약 조회
  @Transactional(readOnly = true)
  public ContractResponse getMyContract() {
    Users user = getCurrentUser();

    Contracts contract = contractRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CONTRACT_NOT_FOUND));

    return ContractResponse.from(contract);
  }

  // 계약 등록
  @Transactional
  public ContractResponse createContract(ContractRequest request) {
    Users user = getCurrentUser();

    // 이미 계약이 있으면 중복 등록 불가
    if (contractRepository.existsByUser(user)) {
      throw new CustomException(BaseErrorCode.CONTRACT_ALREADY_EXISTS);
    }

    Contracts contract = Contracts.builder()
        .user(user)
        .location(request.getLocation())
        .status(request.getStatus())
        .build();

    contractRepository.save(contract);

    // 계약 등록 시 체크리스트 자동 초기화
    initChecklist(contract, user);

    return ContractResponse.from(contract);
  }

  // 계약 수정
  @Transactional
  public ContractResponse updateContract(ContractRequest request) {
    Users user = getCurrentUser();

    Contracts contract = contractRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CONTRACT_NOT_FOUND));

    contract.update(
        request.getLocation(),
        request.getStatus()
    );

    return ContractResponse.from(contract);
  }

  // 계약 삭제
  @Transactional
  public void deleteContract() {
    Users user = getCurrentUser();

    Contracts contract = contractRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CONTRACT_NOT_FOUND));

    contractRepository.delete(contract);
  }

  // 체크리스트 자동 초기화 (내부 메서드)
  // 모든 템플릿을 BEFORE/DURING/AFTER 3단계로 나눠서 인스턴스 생성
  private void initChecklist(Contracts contract, Users user) {
    List<ChecklistTemplates> templates =
        checklistTemplateRepository.findAll();

    // 템플릿 index 기준으로 단계 분류
    // 1~5번: BEFORE, 6~9번: DURING, 10~12번: AFTER
    for (int i = 0; i < templates.size(); i++) {
      ChecklistTemplates template = templates.get(i);

      String stage;
      if (i < 5) {
        stage = "BEFORE";
      } else if (i < 9) {
        stage = "DURING";
      } else {
        stage = "AFTER";
      }

      ChecklistInstances instance = ChecklistInstances.builder()
          .contract(contract)
          .template(template)
          .user(user)
          .stage(stage)
          .build();

      checklistInstanceRepository.save(instance);
    }
  }
}
