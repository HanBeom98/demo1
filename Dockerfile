# Dockerfile
# Ubuntu 24.04 기반 OpenJDK 17 이미지
FROM ubuntu:24.04

# Java 설치
RUN apt-get update && apt-get install -y openjdk-17-jdk

# 작업 디렉토리 생성
WORKDIR /app

# JAR 파일 복사
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 8080 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
