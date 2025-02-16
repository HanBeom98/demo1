package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("회원가입 성공 테스트")
  void registerUserSuccess() {
    // Given
    UserDto userDto = new UserDto("testuser", "1234");
    when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(new User("testuser", "encodedPassword"));

    // When
    assertDoesNotThrow(() -> userService.registerUser(userDto));

    // Then
    verify(passwordEncoder, times(1)).encode(userDto.getPassword());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName("회원가입 시 비밀번호 암호화 검증")
  void passwordEncodingTest() {
    // Given
    UserDto userDto = new UserDto("testuser", "1234");
    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

    // When
    userService.registerUser(userDto);

    // Then
    verify(passwordEncoder, times(1)).encode("1234");
  }
}
