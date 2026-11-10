# Spring Data, MVC, and Security Overview

> **Status:** Completed  
> **Module:** 36 — Spring Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Ch.01–02 demos; REST teaching demo in Module 38

## 1. Learning Objectives

- Position **Spring Data**, **Spring MVC**, and **Spring Security** relative to core Java and JPA (Module 35).
- Read a minimal **REST controller** and **repository** interface as Java interfaces + DI.
- Understand **filter chains** and **method security** at a high level before Module 39.

## 2. Prerequisites

- Module 35 — `EntityManager`, entities.
- Module 32 — HTTP verbs, status codes.
- Ch.01–02 — beans, proxies.

## 3. Why This Concept Exists

Enterprises need **HTTP APIs**, **database access**, and **auth** on almost every service. Spring modules add **opinionated integration** so you configure behavior declaratively while the runtime still uses servlet APIs, JDBC/JPA, and filter chains underneath.

## 4. Conceptual Explanation

```mermaid
flowchart TB
  HTTP[HTTP request] --> Filter[Security filter chain]
  Filter --> Dispatcher[DispatcherServlet]
  Dispatcher --> Controller[@RestController]
  Controller --> Service[@Service]
  Service --> Repo[Spring Data repository]
  Repo --> JPA[JPA / Hibernate]
```

| Module | Role |
|--------|------|
| **Spring MVC / Web MVC** | Maps HTTP to controller methods; content negotiation, validation hooks |
| **Spring Data JPA** | Generates repository implementations from interfaces |
| **Spring Security** | Authentication and authorization filters + configuration |

## 5. Spring Data JPA

Repositories are **interfaces**—Spring Data creates a **proxy implementation** at runtime (same proxy idea as Ch.02).

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

**Query methods** — `findByEmail` is parsed into a query from the method name (convention). For complex SQL use `@Query`.

**Important:** repository methods run in a **transaction** when called from a transactional service method; lazy loading still follows Module 35 rules (no `LazyInitializationException` if OSIV is off and transactions end at the service boundary).

## 6. Spring MVC (@RestController)

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable long id) {
        return users.findById(id)
            .map(u -> ResponseEntity.ok(UserDto.from(u)))
            .orElse(ResponseEntity.notFound().build());
    }
}
```

Under the hood:

- `DispatcherServlet` routes by path and HTTP method.
- Handler method return values are converted to HTTP (JSON via Jackson on the classpath in Boot).
- Status codes from `ResponseEntity` or annotations like `@ResponseStatus`.

Compare to [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/) — same HTTP semantics without Spring.

## 7. Spring Security (overview)

**Authentication** — who you are (login, JWT, OAuth).  
**Authorization** — what you may do (`hasRole`, `hasAuthority`).

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
            .build();
    }
}
```

**Filter chain** runs **before** the dispatcher servlet. Each filter can short-circuit (401/403) or continue. Module 39 deepens sessions, JWT, and OAuth.

**Method security** (optional):

```java
@PreAuthorize("hasAuthority('orders:read')")
public OrderDto getOrder(long id) { ... }
```

Implemented via AOP proxies on the service/controller bean.

## 8. Real-World Use Cases

- CRUD API with JPA entity + DTO mapping (Module 38).
- Admin endpoints protected by role; public health checks.
- Pagination: `Page<User> findByLastName(String name, Pageable pageable)`.

## 9. Common Mistakes

### Incorrect — returning JPA entities directly from REST

Exposes lazy graphs, schema coupling, and serialization leaks.

### Corrected — map to **DTO** or projection; control fields explicitly.

### Incorrect — `Open Session In View` masking lazy-load bugs

Default in older Boot; disable for APIs and load needed data in `@Transactional` service layer (Module 35).

## 10. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Spring Data derived queries | Fast CRUD | Name parsing limits |
| `Entity` in API | Less mapping code | Fragile API contract |
| Security as filters | Central enforcement | Debugging indirection |

## 11. Revision and Mastery

- What Java mechanism implements `UserRepository` without you writing a class?
- Where does authorization run relative to `@GetMapping`?

## 12. Exercises

1. Write a `UserDto` record and a static `from(User)` mapper method.
2. List three `requestMatchers` rules for a fictional billing API.
3. Trace one HTTP GET from socket accept to `findById` SQL (narrative).

## 13. Chapter Summary

Spring Data, MVC, and Security layer declarative configuration over JPA, servlets, and servlet filters—all still Java interfaces, DI, and proxies. Master Module 35 and plain HTTP first; treat Spring as structured composition.

## 14. Next Steps

Module **37** — Spring Boot autoconfiguration and production starters.

## Review Checklist

- [x] Maps to JPA/HTTP foundations; no annotation-only teaching
- [x] DTO and OSIV pitfalls included
