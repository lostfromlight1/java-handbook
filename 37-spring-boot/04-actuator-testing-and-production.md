# Actuator, Testing, and Production

> **Status:** Completed  
> **Module:** 37 — Spring Boot  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Enable **Spring Boot Actuator** for health and metrics endpoints.
- Structure **`@SpringBootTest`** and slice tests (`@WebMvcTest`).
- Apply production practices: **graceful shutdown**, logging, container config.
- Connect to Module 30 testing fundamentals—Boot adds the Spring context.

## 2. Prerequisites

- Module 30 — JUnit, test doubles.
- Module 31 — diagnostics mindset.

## 3. Why This Concept Exists

Production services need **liveness/readiness** probes, **observable metrics**, and **automated regression** of the wiring you configured in Ch.01–03. Actuator and Boot test support operationalize those needs without custom servlet code for every project.

## 4. Actuator (conceptual)

Dependency: `spring-boot-starter-actuator`.

| Endpoint | Purpose |
|----------|---------|
| `/actuator/health` | UP/DOWN with components |
| `/actuator/metrics` | Micrometer metrics |
| `/actuator/info` | Build/git info (customizable) |

**Security:** lock down non-health endpoints in production (Spring Security + `management.endpoints.web.exposure.include`).

Custom health:

```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return checkPool() ? Health.up().build() : Health.down().withDetail("db", "pool exhausted").build();
    }
}
```

Integrates with Kubernetes **liveness** vs **readiness** when you split checks.

## 5. Testing layers

| Annotation | Loads | Use |
|------------|-------|-----|
| `@WebMvcTest` | MVC slice + mocks | Controller unit with `MockMvc` |
| `@DataJpaTest` | JPA + in-memory DB | Repository tests |
| `@SpringBootTest` | Full context | Integration tests |

Example `MockMvc` snippet (conceptual):

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired MockMvc mvc;
    @MockBean UserRepository users;

    @Test void getReturns404WhenMissing() throws Exception {
        when(users.findById(99L)).thenReturn(Optional.empty());
        mvc.perform(get("/api/v1/users/99"))
           .andExpect(status().isNotFound());
    }
}
```

`@MockBean` replaces a context bean with a Mockito mock—**Spring test** integration, not a language feature.

## 6. Production checklist

- **JAVA_OPTS** / container memory aligned with JVM ergonomics (Module 27).
- **Structured logging** (JSON) with correlation IDs from filters.
- **Graceful shutdown:** `server.shutdown=graceful` + adequate `spring.lifecycle.timeout-per-shutdown-phase`.
- **Externalized config** only; secrets from env/vault.
- **Disable** devtools and verbose SQL in prod.

## 7. Edge Cases

- **Full `@SpringBootTest`** is slow—prefer slices for fast CI.
- **Testcontainers** for real DB integration without mocking SQL (optional enhancement).
- **Random context failure** — read condition evaluation report: `--debug` or `FailureAnalyzer` output.

## 8. Common Mistakes

### Incorrect — exposing all actuator endpoints publicly

Attackers probe `/actuator/env`.

### Incorrect — no tests for security rules

Use `@SpringBootTest` + `MockMvc` or `@AutoConfigureMockMvc` with `@WithMockUser`.

## 9. Revision and Mastery

- Difference between liveness and readiness?
- When is `@MockBean` preferable to pure unit tests without context?

## 10. Exercises

1. List three metrics you would alert on for a payment API.
2. Write a test plan: slice vs full integration counts for a new service.
3. Document shutdown sequence for in-flight HTTP requests.

## 11. Chapter Summary

Actuator exposes operational endpoints; Boot test annotations load controlled slices of the application context for fast verification. Production readiness still depends on JVM, security, and configuration discipline outside Boot itself.

## 12. Next Steps

Module **38** — REST API design and contract testing.

## Review Checklist

- [x] Actuator security; test pyramid; production notes
