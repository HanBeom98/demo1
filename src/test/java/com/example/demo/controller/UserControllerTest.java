package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserDetails userDetails;

  @InjectMocks
  private UserController userController;

  @Test
  @DisplayName("내 프로필 조회 성공 - 200 OK 반환")
  void getMyProfileSuccess() {
    // Given
    MockitoAnnotations.openMocks(this);
    when(userDetails.getUsername()).thenReturn("testuser");
    when(userRepository.findByUsername("testuser"))
        .thenReturn(Optional.of(new User("testuser", "password123")));

    // When
    ResponseEntity<?> response = userController.getMyProfile(userDetails);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("현재 로그인된 사용자: testuser", response.getBody());
  }

  @Test
  @DisplayName("내 프로필 조회 실패 - 사용자 정보 없음으로 400 반환")
  void getMyProfileFailure() {
    // Given
    MockitoAnnotations.openMocks(this);
    when(userDetails.getUsername()).thenReturn("unknownuser");
    when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

    // When & Then
    try {
      userController.getMyProfile(userDetails);
    } catch (IllegalArgumentException e) {
      assertEquals("사용자 정보를 찾을 수 없습니다.", e.getMessage());
    }
  }
}
