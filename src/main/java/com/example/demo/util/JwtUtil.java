package com.example.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  private Key key;
  private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
  private static final String SECRET = "MySecretKeyMySecretKeyMySecretKey"; // 꼭 32바이트 이상!

  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(SECRET.getBytes());
  }

  // JWT 생성
  public String createToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // JWT 검증 및 파싱
  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  // JWT 유효성 검증 (ExpiredJwtException을 그대로 던짐)
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      System.out.println("만료된 JWT입니다.");
      throw e;  // ✅ 예외를 그대로 던져서 테스트에서 감지할 수 있게 변경
    } catch (Exception e) {
      System.out.println("유효하지 않은 JWT입니다.");
      return false;
    }
  }
}
