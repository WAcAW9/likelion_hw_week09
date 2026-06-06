package com.wacaw.hw.global.security;

import com.wacaw.hw.domain.user.entity.Users;
import com.wacaw.hw.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Users user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(
            "해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email
        ));

    return User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .roles("USER")
        .build();
  }
}
