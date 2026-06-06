package com.wacaw.hw.domain.contract.repository;

import com.wacaw.hw.domain.contract.entity.Contracts;
import com.wacaw.hw.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contracts, Long> {

  // 유저로 계약 조회
  Optional<Contracts> findByUser(Users user);

  // 유저의 계약 존재 여부
  boolean existsByUser(Users user);
}
