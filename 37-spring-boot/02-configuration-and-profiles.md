# Configuration and Profiles

> **Status:** Completed  
> **Module:** 37 — Spring Boot  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Externalize settings with `application.properties` / `application.yml` and **relaxed binding**.
- Activate **profiles** (`dev`, `test`, `prod`) and `@Profile` beans.
- Bind type-safe **`@ConfigurationProperties`** and validate with Bean Validation.
- Order **property sources** (env vars, command line, files).

## 2. Prerequisites

- Ch.01 — `@Configuration`, beans.
- Module 02 — types; Module 15 — records for immutable config DTOs.

## 3. Why This Concept Exists

Hard-coding JDBC URLs, API keys, and feature flags requires recompilation per environment. **External configuration** lets the same artifact run in dev, staging, and production with different property sets—12-factor app practice.

## 4. Conceptual Explanation

### Property files

`src/main/resources/application.yml`:

```yaml
app:
  mail:
    from: noreply@example.com
spring:
  profiles:
    active: dev
```

`application-dev.yml` and `application-prod.yml` override fragments when their profile is active.

**Relaxed binding:** `app.mail.from`, `APP_MAIL_FROM` (env), and `app.mail-from` map to the same property.

### Profiles

```java
@Configuration
@Profile("prod")
public class ProductionLoggingConfig {
    @Bean
    public Filter requestIdFilter() { ... }
}
```

Only registered when `spring.profiles.active` includes `prod`.

### @ConfigurationProperties

```java
@ConfigurationProperties(prefix = "app.mail")
@Validated
public record MailProperties(
    @NotBlank String from,
    @Min(1) int retryAttempts
) {}
```

Enable with `@EnableConfigurationProperties(MailProperties.class)` or register on `@SpringBootApplication`. Spring binds properties from the environment into the record—**constructor binding** for immutable config.

## 5. Property source precedence (simplified)

Higher wins (later in list overwrites):

1. Default `application.properties`
2. Profile-specific file
3. OS environment variables
4. Command-line args (`--server.port=9090`)

Use **environment variables** in containers (Kubernetes secrets as env).

## 6. Real-World Use Cases

- `prod` profile: real DataSource; `test` profile: H2 in-memory.
- Feature toggle: `app.features.newCheckout=true`.
- Multi-region: `application-ap-sg.yml`.

## 7. Edge Cases

- **Sensitive values** — never commit secrets; use env/secret stores; rotate keys.
- **YAML merge** — profile files merge keys; know which file wins.
- **Random port** in tests: `server.port=0`.

## 8. Common Mistakes

### Incorrect — duplicate keys across env and file without documenting precedence

Leads to “works on my machine” — log active profile and critical bound properties at startup (sanitized).

### Incorrect — `@Value` for large config trees

Prefer `@ConfigurationProperties` for structure and validation.

## 9. Security Considerations

- Do not log full `DataSource` passwords.
- Restrict actuator env endpoint in production (Ch.04).

## 10. Revision and Mastery

- How do you run with `prod` on the command line?
- Difference between `@Profile` on a `@Bean` method vs on a class?

## 11. Exercises

1. Design three profiles for local, CI, and production JDBC URLs.
2. Add validation failure behavior for invalid `MailProperties`.
3. Map `DATABASE_URL` env var to `spring.datasource.url` in narrative form.

## 12. Chapter Summary

Boot configuration layers files, profiles, and environment into one property resolution model, with typed `@ConfigurationProperties` for safe binding. This is still standard Java objects injected by the container.

## 13. Next Steps

[03-web-layer-and-validation.md](03-web-layer-and-validation.md)

## Review Checklist

- [x] Profiles, binding, precedence, secrets
