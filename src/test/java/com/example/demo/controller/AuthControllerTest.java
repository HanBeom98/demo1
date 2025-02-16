package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserService userService;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private AuthController authController;

  @Test
  @DisplayName("회원가입 성공 - 200 OK 반환")
  void signupSuccess() {
    // Given
    MockitoAnnotations.openMocks(this);
    UserDto userDto = new UserDto("testuser", "password123");

    // When
    ResponseEntity<String> response = authController.signup(userDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("회원가입 성공!", response.getBody());
    verify(userService, times(1)).registerUser(userDto);
  }

  @Test
  @DisplayName("로그인 성공 - JWT 반환 및 200 OK")
  void loginSuccess() {
    // Given
    MockitoAnnotations.openMocks(this);
    UserDto userDto = new UserDto("testuser", "password123");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(null);
    when(jwtUtil.createToken("testuser")).thenReturn("mockedJwtToken");

    // When
    ResponseEntity<String> response = authController.login(userDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("로그인 성공! JWT: mockedJwtToken", response.getBody());
  }

  @Test
  @DisplayName("로그인 실패 - 잘못된 인증 정보로 401 반환")
  void loginFailure() {
    // Given
    MockitoAnnotations.openMocks(this);
    UserDto userDto = new UserDto("wronguser", "wrongpassword");

    doThrow(new BadCredentialsException("Invalid credentials"))
        .when(authenticationManager)
        .authenticate(any(UsernamePasswordAuthenticationToken.class));

    // When
    ResponseEntity<String> response = authController.login(userDto);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("로그인 실패", response.getBody());
  }
}
