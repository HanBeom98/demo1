package com.example.demo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

  private JwtUtil jwtUtil;
  private String token;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil();
    jwtUtil.init(); // @PostConstruct 메서드 수동 호출
    token = jwtUtil.createToken("testuser");
  }

  @Test
  @DisplayName("JWT 생성 및 파싱 - 유저네임 검증")
  void createAndParseToken() {
    // When
    String username = jwtUtil.getUsernameFromToken(token);

    // Then
    assertEquals("testuser", username, "토큰에서 파싱한 유저네임이 일치해야 합니다.");
  }

  @Test
  @DisplayName("JWT 유효성 검사 - 올바른 토큰")
  void validateValidToken() {
    // When
    boolean isValid = jwtUtil.validateToken(token);

    // Then
    assertTrue(isValid, "유효한 토큰이어야 합니다.");
  }

  @Test
  @DisplayName("JWT 유효성 검사 - 잘못된 토큰")
  void validateInvalidToken() {
    // Given
    String invalidToken = token.substring(0, token.length() - 2) + "xx";

    // When
    boolean isValid = jwtUtil.validateToken(invalidToken);

    // Then
    assertFalse(isValid, "잘못된 토큰은 유효하지 않아야 합니다.");
  }

  @Test
  @DisplayName("JWT 유효성 검사 - 만료된 토큰")
  void validateExpiredToken() throws InterruptedException {
    // Given
    jwtUtil = new JwtUtil();
    jwtUtil.init();
    jwtUtil = new JwtUtil() {
      @Override
      public String createToken(String username) {
        return io.jsonwebtoken.Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new java.util.Date(System.currentTimeMillis() - 3600000)) // 1시간 전 발급
            .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000)) // 이미 만료됨
            .signWith(jwtUtil.key, io.jsonwebtoken.SignatureAlgorithm.HS256)
            .compact();
      }
    };
    String expiredToken = jwtUtil.createToken("testuser");

    // When
    boolean isValid = jwtUtil.validateToken(expiredToken);

    // Then
    assertFalse(isValid, "만료된 토큰은 유효하지 않아야 합니다.");
  }
}
