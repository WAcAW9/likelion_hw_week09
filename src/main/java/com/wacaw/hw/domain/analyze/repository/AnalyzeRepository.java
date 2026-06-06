package com.wacaw.hw.domain.analyze.repository;

import com.wacaw.hw.domain.analyze.entity.ContractsAnalyze;
import com.wacaw.hw.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnalyzeRepository extends JpaRepository<ContractsAnalyze, Long> {

  // 유저의 분석 이력 전체 조회 (최신순)
  List<ContractsAnalyze> findByUserOrderByCreatedAtDesc(Users user);

  // 분석 ID + 유저로 단건 조회 (권한 확인용)
  Optional<ContractsAnalyze> findByContractAnalyzeIdAndUser(
      Long contractAnalyzeId, Users user);
}
