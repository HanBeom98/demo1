package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("유저 정보 로드 성공 - UserDetails 반환")
  void loadUserByUsernameSuccess() {
    // Given
    User user = new User("testuser", "encodedPassword");
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

    // When
    UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

    // Then
    assertNotNull(userDetails);
    assertEquals("testuser", userDetails.getUsername(), "유저네임이 일치해야 합니다.");
    verify(userRepository, times(1)).findByUsername("testuser");
  }

  @Test
  @DisplayName("유저 정보 로드 실패 - UsernameNotFoundException 발생")
  void loadUserByUsernameFailure() {
    // Given
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    // When & Then
    UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> customUserDetailsService.loadUserByUsername("unknownuser"),
        "사용자를 찾지 못하면 예외가 발생해야 합니다."
    );

    assertEquals("사용자를 찾을 수 없습니다: unknownuser", exception.getMessage());
    verify(userRepository, times(1)).findByUsername("unknownuser");
  }
}
