package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserRepository userRepository;

  @GetMapping("/me")
  public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    return ResponseEntity.ok("현재 로그인된 사용자: " + user.getUsername());
  }
}
