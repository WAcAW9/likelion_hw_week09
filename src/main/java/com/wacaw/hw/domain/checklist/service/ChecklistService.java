package com.wacaw.hw.domain.checklist.service;

import com.wacaw.hw.domain.checklist.dto.request.ChecklistUpdateRequest;
import com.wacaw.hw.domain.checklist.dto.response.ChecklistResponse;
import com.wacaw.hw.domain.checklist.entity.ChecklistInstances;
import com.wacaw.hw.domain.checklist.repository.ChecklistInstanceRepository;
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
public class ChecklistService {

  private final ChecklistInstanceRepository checklistInstanceRepository;
  private final ContractRepository contractRepository;
  private final UserRepository userRepository;

  // 현재 유저 조회 공통 메서드
  private Users getCurrentUser() {
    String email = SecurityUtil.getCurrentUserEmail();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));
  }

  // 단계별 체크리스트 조회
  @Transactional(readOnly = true)
  public ChecklistResponse getChecklists(String stage) {
    Users user = getCurrentUser();

    // 유저의 계약 조회
    Contracts contract = contractRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(BaseErrorCode.CONTRACT_NOT_FOUND));

    // 계약 + 단계로 체크리스트 인스턴스 조회
    List<ChecklistInstances> instances =
        checklistInstanceRepository.findByContractAndStage(contract, stage);

    // 진행률 계산
    int totalCount = instances.size();
    int checkedCount = (int) instances.stream()
        .filter(ChecklistInstances::isChecked)
        .count();
    int progressRate = totalCount > 0
        ? (checkedCount * 100 / totalCount)
        : 0;

    // 항목 변환
    List<ChecklistResponse.ChecklistItemResponse> items = instances.stream()
        .map(ChecklistResponse.ChecklistItemResponse::from)
        .toList();

    return ChecklistResponse.builder()
        .stage(stage)
        .totalCount(totalCount)
        .checkedCount(checkedCount)
        .progressRate(progressRate)
        .items(items)
        .build();
  }

  // 체크 상태 토글
  @Transactional
  public void toggleChecklist(Long instanceId, ChecklistUpdateRequest request) {
    Users user = getCurrentUser();

    // instanceId + userId로 조회 (다른 유저 항목 접근 방지)
    ChecklistInstances instance = checklistInstanceRepository
        .findByInstanceIdAndUserUserId(instanceId, user.getUserId())
        .orElseThrow(() -> new CustomException(BaseErrorCode.CHECKLIST_NOT_FOUND));

    instance.toggleChecked(request.getIsChecked());
  }
}
