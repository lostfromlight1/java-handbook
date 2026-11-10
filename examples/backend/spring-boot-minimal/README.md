# Spring Boot minimal demo

Smallest **Spring Boot 3.4** web app used to verify handbook Module 37 claims (starters, embedded Tomcat, fat JAR, `@WebMvcTest`).

## Prerequisites

- **JDK 21** (verified: Temurin 21.0.12.1)
- No Maven on `PATH` required — use the **Maven Wrapper** in this directory

## Commands

```bash
./mvnw test
./mvnw -DskipTests package
java -jar target/spring-boot-minimal-1.0.0-SNAPSHOT.jar
```

Windows: `mvnw.cmd` instead of `./mvnw`.

## Verified output (Temurin 21.0.12.1, 2026-11-10)

- **`mvnw test`:** `Tests run: 1, Failures: 0` (`HelloControllerTest`)
- **Running the fat JAR:** Tomcat on port **18080**; `GET http://127.0.0.1:18080/hello` → `{"message":"bootMinimal=ok"}`

## Concepts demonstrated

- `@SpringBootApplication` bootstrap
- `spring-boot-starter-web` (DispatcherServlet + Jackson + embedded Tomcat)
- `spring-boot-maven-plugin` repackaged executable JAR
- `@WebMvcTest` + `MockMvc` slice test

Compare HTTP semantics without Spring: [rest-httpserver-demo](../rest-httpserver-demo/).
