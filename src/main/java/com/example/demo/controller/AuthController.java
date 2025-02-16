package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
    userService.registerUser(userDto);
    return ResponseEntity.ok("회원가입 성공!");
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody UserDto userDto) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
      );
      String token = jwtUtil.createToken(userDto.getUsername());
      return ResponseEntity.ok("로그인 성공! JWT: " + token);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패"); // ✅ 명확한 실패 메시지
    }
  }
}
