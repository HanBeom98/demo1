# Demo 프로젝트 (백엔드 과제)

## 소개
이 프로젝트는 Spring Boot 기반으로 개발된 웹 애플리케이션으로, JWT 인증 및 EC2 배포와 CI/CD 파이프라인을 포함합니다.

## 주요 기능
- **JWT 기반 사용자 인증 및 인가**
- **Spring Security 설정**
- **User API 제공 (회원가입, 로그인, 사용자 정보 조회)**
- **JUnit 기반 테스트 코드**
- **EC2 및 GitHub Actions 기반 CI/CD 적용**

## 기술 스택
- **Back-end**: Java 17, Spring Boot 3.4.2, Spring Security, JWT
- **Database**: MySQL
- **Build Tool**: Gradle
- **CI/CD**: GitHub Actions
- **Deployment**: AWS EC2 (Ubuntu 24.04)

---

## API 개요 및 Postman 호출 방법

### 1. **회원가입 (`POST /api/auth/signup`)**
- **요청 예시 (Postman):**
```json
{
  "username": "testuser",
  "password": "password123"
}
```

- **응답 예시 (Postman):**
```json
{
  "message": "회원가입 성공"
}
```
### 2. **로그인 (POST /api/auth/login))**
- **요청 예시 (Postman):**
```json
{
  "username": "testuser",
  "password": "password123"
}
```
- **응답 예시 (Postman):**
```json
{
  "token": "eyJhbGci..."
}
```
💡 Postman Authorization에 JWT 토큰을 Bearer Token으로 설정하여 이후 요청을 호출합니다.

### 3. 사용자 정보 조회 (GET /api/user/me)**
- **요청 헤더 (Postman):**
  ```http
  Authorization: Bearer <JWT_TOKEN>
```
- **응답 예시 (Postman):**
```json
{
  "id": 1,
  "username": "testuser",
  "createdAt": "2025-02-16"
}
```

### CI/CD 파이프라인**
- ****GitHub Actions (.github/workflows/deploy.yml)**을 통해 EC2에 자동 배포됩니다.**
- main 브랜치에 Push 시 자동으로 Build, 테스트 후 배포가 진행됩니다.
