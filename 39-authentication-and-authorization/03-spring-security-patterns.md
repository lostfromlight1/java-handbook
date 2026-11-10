# Spring Security Patterns

> **Status:** Completed  
> **Module:** 39 — Authentication and Authorization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Configure **`SecurityFilterChain`** with explicit matchers and rule order.
- Apply **`@PreAuthorize`** / **`@PostAuthorize`** method security.
- Integrate **JWT resource server**, **form login**, and **CORS** safely.
- Test security with **`@WithMockUser`** and `MockMvc`.

## 2. Prerequisites

- Ch.01–02 — sessions, JWT, OAuth.
- Module 37 — Boot web layer.

## 3. Why This Concept Exists

Ad-hoc security filters per controller diverge and miss CSRF, headers, and exception translation. **Spring Security** provides a composable **filter chain**, authentication managers, and method-level hooks consistent across the app.

## 4. SecurityFilterChain (conceptual)

```java
@Bean
SecurityFilterChain app(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // only if stateless JWT API
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/api/v1/public/**").permitAll()
            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((req, res, e) -> res.sendError(401))
            .accessDeniedHandler((req, res, e) -> res.sendError(403)))
        .build();
}
```

**Order matters:** first matching rule wins for `authorizeHttpRequests`.

### CSRF

- **Session cookie + browser form:** enable CSRF tokens.
- **Bearer JWT APIs:** often disable CSRF for that matcher (no cookie auth).

### CORS

```java
@Bean
CorsConfigurationSource cors() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("https://app.example.com"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", config);
    return source;
}
```

Misconfigured CORS exposes APIs to malicious origins.

## 5. Method security

Enable with `@EnableMethodSecurity`.

```java
@Service
public class OrderService {
    @PreAuthorize("hasAuthority('orders:read')")
    public OrderDto get(long id, Authentication auth) {
        Order order = repo.findById(id).orElseThrow();
        assertOwnerOrAdmin(order, auth);
        return OrderDto.from(order);
    }
}
```

SpEL in `@PreAuthorize` references `authentication`, `hasRole`, beans.

**Defense in depth:** URL rules + method checks + data-level ownership.

## 6. Password encoding (form login)

```java
@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Never compare plaintext passwords.

## 7. Testing

```java
@Test
@WithMockUser(roles = "ADMIN")
void adminEndpointAllowed() throws Exception {
    mvc.perform(get("/api/v1/admin/stats")).andExpect(status().isOk());
}

@Test
void anonymousForbidden() throws Exception {
    mvc.perform(get("/api/v1/admin/stats")).andExpect(status().isUnauthorized());
}
```

## 8. Common Mistakes

### Incorrect — `permitAll()` on `/api/**` during development left in prod

Public data leak.

### Incorrect — trusting `roles` claim without validating issuer

Forge tokens if signature keys are wrong.

## 9. Real-World Patterns

- **BFF** (Backend for Frontend) holds session; calls APIs with service token.
- **Gateway** validates JWT once; internal mTLS.
- **Multi-tenant:** tenant claim enforced in repository queries.

## 10. Revision and Mastery

- When is CSRF disabled acceptable?
- Filter chain vs `@PreAuthorize`—which runs first?

## 11. Exercises

1. Write matcher rules for actuator vs business API.
2. Add ownership check pseudo-code for `GET /orders/{id}`.
3. Plan security tests for 401, 403, 200 matrix.

## 12. Chapter Summary

Spring Security expresses AuthN/AuthZ as ordered filter rules and optional method annotations, integrating JWT, sessions, and CORS. Policies must still follow fail-closed design and resource-level checks.

## 13. Next Steps

Module **40** — backend architecture.

## 5. Mechanism and Execution Flow *(mandatory)*

```mermaid
flowchart LR
  REQ[HttpServletRequest] --> SEC[SecurityFilterChain]
  SEC --> AUTHN[Bearer / Form login]
  AUTHN --> AUTHZ[authorizeHttpRequests]
  AUTHZ --> MVC[DispatcherServlet]
  MVC --> MS[@PreAuthorize optional]
```

Filter chain runs **before** `DispatcherServlet`. Method security uses AOP around service beans—defense in depth with URL rules.

All Spring configuration blocks in this chapter are **Illustrative** (require `spring-boot-starter-security` on classpath).

## 21. FAQ — Spring Security *(mandatory depth)*

**Q: CSRF off for REST JWT?** Acceptable when no session cookie auth; risky for cookie sessions.

**Q: `permitAll` actuator?** Lock down `/actuator/**` in production.

**Q: CORS preflight?** `OPTIONS` must be permitted before browser sends real request.

**Q: Test matrix?** Table: anonymous vs user vs admin × endpoints.

**Q: Multi-tenant filter?** `OncePerRequestFilter` extracts tenant claim early.


## Depth appendix — progressive teaching (Authentication / authorization)

### Incorrect vs corrected patterns

Documenting failure modes accelerates review. Each pair below states **why** the wrong approach breaks client or operator expectations.

### Operational checklist before marking API complete

- OpenAPI matches deployed routes (Ch.03).
- Error envelope consistent (ProblemDetail or documented custom schema).
- AuthN/Z enforced on mutations (Module 39).
- Examples in `examples/` compiled on Temurin 21.0.12.1 with recorded output.
- Versioning/deprecation policy published for partners.

### Revision drills (Track B)

1. Explain one design trade-off you reversed after load testing.
2. Write three MockMvc or plain HTTP negative tests not in the happy path.
3. Identify one boundary where DTO mapping must never leak entities.

### References (official hierarchy)

- RFC 9110 — HTTP semantics (methods, status).
- Jakarta EE / Spring reference docs for Illustrative framework binding only.
- OWASP ASVS — verification levels for security chapters (Module 43).

**Mechanism merge note:** Where earlier sections already diagram execution flow, this appendix reinforces **operational** depth without duplicating definitions.

### Threat modeling prompts

- Credential stuffing → rate limit + MFA policy.
- Session fixation → rotate session id after login.
- Token theft → short TTL, refresh rotation, detect reuse.

### Audit fields

Store `createdBy` / `modifiedBy` from principal on mutations; support incident investigation (Module 47).

### Multi-tenant isolation

Every repository query includes `tenant_id` predicate derived from token—not from request body parameters.

## Technical L3 supplement — teaching depth

### Motivation recap (concrete scenario)

A team ships a Java 21 service without documented HTTP contracts, security rules, or messaging guarantees. Mobile clients cache error shapes; a deploy renames a field and checkout silently fails. Operations cannot trace requests across services. The material in this module prevents that class of failure by making **boundaries explicit**: REST semantics, DTOs, auth filters, architecture ports, patterns, clean code, security controls, resilience, and delivery semantics.

### Mechanism trace (how this chapter shows up at runtime)

1. **Ingress** — HTTP or message enters an adapter (controller, listener).
2. **Policy** — auth, validation, and rate limits run before domain logic (fail closed).
3. **Application** — use case coordinates domain + ports inside a transaction or saga step.
4. **Egress** — DTO mapping, event publish (outbox), or outbound HttpClient with timeouts.
5. **Observation** — structured logs with correlation id; metrics on error rate (Module 47).

This sequence is framework-agnostic; Spring/Boot annotations are **Illustrative** wiring for the same pipeline taught in earlier modules.

### Progressive example ladder

| Stage | Focus | Verification label |
|-------|--------|-------------------|
| Minimal | JDK-only demo or pure unit logic | Verified / Compile-verified |
| Realistic | Layered service with DTO + port | Illustrative Spring config |
| Advanced | Failure modes: timeout, duplicate message, 409 conflict | Test + documented output |

Never label **Verified** without Temurin 21.0.12.1 compile/run recorded in chapter or `PROGRESS.md`.

### Wrong vs right (review board)

| Wrong | Why it hurts | Right |
|-------|----------------|-------|
| Skip server AuthZ | IDOR incidents | Check principal vs resource every mutation |
| Expose JPA graph in JSON | Lazy-load storms, schema coupling | Map to DTO in service |
| Infinite HttpClient wait | Thread pile-up, incident duration | Connect + request timeouts |
| Retry all POSTs | Duplicate charges | Idempotency keys + dedup store |
| Dual write DB + broker | Lost or phantom events | Transactional outbox |

### Design tensions engineers negotiate

- **Consistency vs availability** during partitions (Module 44).
- **Stateless JWT vs revocable sessions** (Module 39).
- **Modular monolith vs microservices** operational cost (Module 40).
- **Kafka vs Rabbit** retention and routing (Module 45).
- **Strict SOLID vs delivery speed** (Modules 41–42).

Document the choice in ADRs; the handbook gives options, not one universal answer.

### Java 21 notes

- **Records** for immutable DTOs and events (`Module 08`, `15`).
- **Virtual threads** improve blocking HTTP and JDBC throughput but do not remove timeouts or auth checks.
- **Pattern matching** and **sealed types** clarify domain result models (`PaymentResult` in SOLID chapters).

### Compatibility and sources

Prefer **RFC 9110** for HTTP, **RFC 7807** for errors, **OWASP ASVS** for verification language, and vendor docs for brokers labeled Illustrative. Distinguish **JLS/API guarantees** from **Spring/HotSpot** behavior as in `DOCUMENTATION-STANDARDS.md`.

### Track B — mastery questions

1. Where is the transaction boundary in your current project, and why?
2. Which dependency would you invert first behind a port for testing?
3. What status code and error shape would clients see for your most common validation failure?
4. How would you prove an idempotent consumer in tests?
5. What metric would fire before users notice a downstream slowdown?

### Track B — field exercise (no solution in chapter)

Implement or extend one `examples/` demo for this module: add a negative-path assertion (4xx, breaker OPEN, or rejected input), compile with `javac -source 21`, run on Temurin 21.0.12.1, paste output into `PROGRESS.md` only after a real run.

### Cross-module links

| Need | Module |
|------|--------|
| HTTP client timeouts | 32 |
| JDBC transactions | 33–35 |
| Spring DI / Security | 36–37 |
| Testing & CI | 30 |
| Observability | 47 |

### Glossary touchpoints

Add unfamiliar terms encountered here to `GLOSSARY.md` when stabilizing definitions across the handbook (repository workflow in java-documentation skill).

### Explicit omissions (this supplement)

- Vendor-specific console screenshots and cloud provisioning scripts.
- Exploit reproduction steps (defensive security only in Module 43).
- Exhaustive broker tuning guides (operational manuals remain external).

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Filter chain, CSRF/CORS, method security, tests
