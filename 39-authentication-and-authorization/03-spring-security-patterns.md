# Spring Security Patterns

> **Status:** Completed  
> **Module:** 39 — Authentication and Authorization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

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

## Review Checklist

- [x] Filter chain, CSRF/CORS, method security, tests
