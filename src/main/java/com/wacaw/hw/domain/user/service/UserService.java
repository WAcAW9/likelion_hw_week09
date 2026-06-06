package com.wacaw.hw.domain.user.service;

import com.wacaw.hw.domain.user.dto.request.UserUpdateRequest;
import com.wacaw.hw.domain.user.dto.response.UserResponse;
import com.wacaw.hw.domain.user.entity.Users;
import com.wacaw.hw.domain.user.repository.UserRepository;
import com.wacaw.hw.global.commnon.SecurityUtil;
import com.wacaw.hw.global.exception.CustomException;
import com.wacaw.hw.global.exception.model.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  // 내 프로필 조회
  @Transactional(readOnly = true)
  public UserResponse getMyProfile() {
    String email = SecurityUtil.getCurrentUserEmail();

    Users user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));

    return UserResponse.from(user);
  }

  // 프로필 수정
  @Transactional
  public UserResponse updateMyProfile(UserUpdateRequest request) {
    String email = SecurityUtil.getCurrentUserEmail();

    Users user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(BaseErrorCode.USER_NOT_FOUND));

    user.updateProfile(request.getName(), request.getProfileImage());

    return UserResponse.from(user);
  }
}
