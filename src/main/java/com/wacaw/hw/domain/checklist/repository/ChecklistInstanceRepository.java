package com.wacaw.hw.domain.checklist.repository;

import com.wacaw.hw.domain.checklist.entity.ChecklistInstances;
import com.wacaw.hw.domain.contract.entity.Contracts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistInstanceRepository
    extends JpaRepository<ChecklistInstances, Long> {

  // 계약 + 단계로 체크리스트 목록 조회
  List<ChecklistInstances> findByContractAndStage(Contracts contract, String stage);

  // instanceId + userId로 단건 조회 (권한 확인용)
  Optional<ChecklistInstances> findByInstanceIdAndUserUserId(
      Long instanceId, Long userId);
}
