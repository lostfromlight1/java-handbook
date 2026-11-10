# Configuration and Profiles

> **Status:** Completed  
> **Module:** 37 — Spring Boot  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Depth target:** Level 3 (property resolution, profiles, `@ConfigurationProperties`, operational pitfalls)  
> **Examples verified with:** Temurin JDK 21.0.12.1 — configuration concepts align with [spring-boot-minimal](../../examples/backend/spring-boot-minimal/) (`server.port=18080` in `application.properties`)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Externalize settings with **`application.properties` / `application.yml`** and **relaxed binding** to Java fields.
- Explain **property source order** and predict which value wins when files, env vars, and CLI flags disagree.
- Activate **profiles** (`dev`, `test`, `prod`) and register **profile-specific beans** with `@Profile`.
- Bind structured configuration with **`@ConfigurationProperties`**, validate with Bean Validation, and prefer it over scattered `@Value`.
- Avoid common production mistakes: secret leakage, duplicate keys, and untyped config trees.

## 2. Prerequisites

- **Ch.01** — `@Configuration`, beans, autoconfiguration conditions (`@ConditionalOnProperty`).
- **Module 15** — records for immutable configuration DTOs.
- **Module 02** — types and validation mindset.

## 3. Why This Concept Exists

Hard-coding JDBC URLs, mail hosts, feature flags, and thread pool sizes forces **recompilation** for every environment. **Twelve-factor** style apps ship **one artifact** and vary **configuration** per deploy.

Spring Boot centralizes configuration in an **`Environment`** abstraction backed by ordered **property sources**. The same `OrderService` bean graph runs in development and production; only property values and active **profiles** change.

## 4. Conceptual Explanation

### 4.1 The `Environment` model

At runtime, Boot merges many sources into one **property resolver**:

- Default `application.properties` / `application.yaml` on the classpath
- Profile-specific files (`application-prod.yaml`)
- OS **environment variables**
- Java system properties
- **Command-line arguments** (`--server.port=9090`)
- Optional: `spring.config.import` for external files, Kubernetes `ConfigMap` mounts, etc.

Your code injects values via `@Value`, `@ConfigurationProperties`, or directly through `Environment#getProperty`.

### 4.2 Property files and relaxed binding

`src/main/resources/application.yml` (conceptual):

```yaml
app:
  mail:
    from: noreply@example.com
    retry-attempts: 3
spring:
  profiles:
    active: dev
```

**Relaxed binding** maps multiple external forms to one Java property:

| External form | Same property |
|---------------|---------------|
| `app.mail.from` (properties file) | `app.mail.from` |
| `APP_MAIL_FROM` (environment) | `app.mail.from` |
| `app.mail-from` (alternate) | `app.mail.from` |

Environment variable binding uses relaxed rules: `APP_MAIL_RETRYATTEMPTS` → `app.mail.retry-attempts`.

### 4.3 Profiles

A **profile** is a named label (`dev`, `prod`, `local`) that activates:

1. **Additional property files** — `application-prod.yml` merges when `prod` is active.
2. **Conditional beans** — `@Profile("prod")` on a `@Configuration` or `@Bean` method.

```java
@Configuration
@Profile("prod")
public class ProductionLoggingConfig {

    @Bean
    public Filter requestIdFilter() {
        return new RequestIdFilter();
    }
}
```

Activate profiles via:

- `spring.profiles.active=prod` in a file
- Environment variable `SPRING_PROFILES_ACTIVE=prod`
- CLI: `--spring.profiles.active=prod`

**Multiple profiles:** comma-separated list; later sources can override keys from earlier files (see precedence).

**Default vs active profiles:** `spring.profiles.default` registers fallbacks when nothing is active; `spring.profiles.active` is what you set in deploy. CI should assert the active set in smoke tests to catch “prod beans missing because profile typo.”

**`@Profile` on `@Bean` methods vs classes:** class-level profile gates the entire `@Configuration`; method-level gates individual beans—use method-level when one config class serves both `dev` (H2 console bean) and shared beans.

**Negated profiles:** `@Profile("!prod")` registers a bean in every profile except `prod`—handy for dev-only diagnostics without duplicating configuration classes.

### 4.4 `@ConfigurationProperties`

Bind a prefix to a type-safe object—constructor binding works well with **records**:

```java
@ConfigurationProperties(prefix = "app.mail")
@Validated
public record MailProperties(
    @NotBlank String from,
    @Min(1) int retryAttempts
) {}
```

Enable binding:

```java
@SpringBootApplication
@EnableConfigurationProperties(MailProperties.class)
public class HandbookApplication { ... }
```

Or register via `@ConfigurationPropertiesScan`.

Spring maps properties to constructor parameters **before** the bean is used. **`@Validated`** triggers Bean Validation on bound values at startup—invalid mail config fails fast instead of at runtime.

| `@Value("${app.mail.from}")` on many fields | `@ConfigurationProperties` |
|---------------------------------------------|----------------------------|
| Fine for one-off keys | Better for nested trees |
| No structural validation by default | Records + `@Validated` give clear errors |
| Harder to test | Easy to unit-test binding with `@EnableConfigurationProperties` tests |

## 5. Property Source Precedence

Boot 2.4+ uses a **config data API** with a documented order. Simplified mental model for **overriding** (later / higher precedence wins in the merge):

1. Default **`application.properties`** on classpath (lowest among app configs)
2. **Profile-specific** files (`application-prod.properties`) — profile files override non-profile for the same key when that profile is active
3. **Config data imports** (if used)
4. **Servlet context init parameters** (if applicable)
5. **OS environment variables**
6. **Java system properties** (`-Dserver.port=9090`)
7. **Command-line arguments** (`--server.port=9090`) — typically highest for local overrides

**Operational rule:** document where production sets `spring.datasource.url`. If both a file and `DATABASE_URL` env var exist, know which wins in your deploy platform (usually env over file).

Example narrative:

```text
application.yml     → server.port=8080
application-prod.yml  → server.port=8443  (profile prod active)
Environment           → SERVER_PORT=9090
CLI                   → --server.port=18080
Result: 18080 if CLI is present (highest in typical Boot ordering)
```

Consult the [official “Externalized Configuration”](https://docs.spring.io/spring-boot/reference/features/external-config.html) section when precision matters for your Boot minor version.

### 5.1 Boot 3 property source order (expanded reference)

The list below extends section 5 with the **full override ladder** from lowest to highest precedence for typical servlet applications. Higher entries **win** for the same property key. Exact ordering can shift slightly by Boot minor version—verify in the Externalized Configuration chapter when upgrading.

| Precedence (low → high) | Source |
|-------------------------|--------|
| 1 | `defaultProperties` passed to `SpringApplication` |
| 2 | `@PropertySource` on `@Configuration` classes (unless ignored) |
| 3 | Config data: `application.properties` / `application.yaml` (default profile) |
| 4 | Config data: `application-{profile}.*` for each active profile |
| 5 | Imported config (`spring.config.import=optional:file:./local/`) |
| 6 | Servlet context init parameters (when applicable) |
| 7 | Servlet filter init parameters (when applicable) |
| 8 | JNDI attributes (legacy containers) |
| 9 | Java system properties (`-Dkey=value`) |
| 10 | OS environment variables |
| 11 | `RandomValuePropertySource` (`${random.*}`) — special; not override of named keys |
| 12 | Command-line arguments (`--server.port=8081`) |
| 13 | `SPRING_APPLICATION_JSON` inline JSON |
| 14 | `ServletConfig` init parameters |
| 15 | Test `@TestPropertySource` / `@DynamicPropertySource` (tests only) |

**`spring.config.import` nuance:** an imported file becomes its own property source with defined ordering relative to the importer. Use **`optional:`** prefix so missing cloud paths do not abort startup during local dev.

**Config data location search order:** Boot looks in `classpath:/`, `classpath:/config/`, `file:./`, `file:./config/` unless you customize `spring.config.location`. Two files with the same key in different locations follow Boot’s location precedence rules—**not** “nearest file wins by accident.”

**Kubernetes mapping:** a manifest env var `SERVER_PORT` binds via relaxed rules to `server.port` and typically overrides YAML inside the container image—exactly the behavior you want for platform-managed ports.

### 5.2 Profile groups (Boot 2.4+)

**Profile groups** activate **several named profiles** when one “logical” profile is switched on—useful when `prod` always implies `metrics`, `json-logging`, and `no-scheduler`:

```yaml
spring:
  profiles:
    group:
      prod:
        - metrics
        - json-logging
        - no-scheduler
```

Activating `prod` via `SPRING_PROFILES_ACTIVE=prod` also activates the grouped profiles, loading `application-metrics.yaml`, etc., without operators remembering a comma-separated list.

| Without groups | With groups |
|----------------|-------------|
| `SPRING_PROFILES_ACTIVE=prod,metrics,json-logging` | `SPRING_PROFILES_ACTIVE=prod` |
| Docs drift when someone adds a profile | Change the group definition once |

**Edge case:** conflicting keys across grouped profile files still follow normal profile merge rules—document which profile “owns” shared keys like `logging.level.root`.

### 5.3 `@ConfigurationProperties` validation explained

Validation sits in **two layers**:

1. **Binding** — can the string `retry-attempts=abc` become an `int`? Binding failure → `BindException` at startup.
2. **Bean Validation** — is `retryAttempts=0` allowed? **`@Validated`** on the properties type runs after successful bind → `MethodArgumentNotValidException` / constraint violations wrapped in startup failure.

```java
@ConfigurationProperties(prefix = "app.mail")
@Validated
public record MailProperties(
    @NotBlank String from,
    @Min(1) int retryAttempts
) {}
```

```yaml
app:
  mail:
    from: ""           # fails @NotBlank after bind
    retry-attempts: 0  # fails @Min(1)
```

**Startup narrative:**

1. `MailProperties` bean is created via constructor binding.
2. Hibernate Validator inspects constraint annotations.
3. Violations aggregate; Boot fails fast with a message naming `app.mail.from` and `retry-attempts`.
4. No controller ever runs with invalid mail configuration—**fail closed** at bootstrap.

**Testing binding in isolation:**

```java
@Test
void mailPropertiesRejectsInvalidRetry() {
    assertThatExceptionOfType(BindException.class)
        .isThrownBy(() -> new Binder(
                new MapConfigurationPropertySource(
                    Map.of("app.mail.from", "a@b.com", "app.mail.retry-attempts", "0")))
            .bind("app.mail", Bindable.of(MailProperties.class)));
}
```

Or use `@EnableConfigurationProperties(MailProperties.class)` on a `@SpringBootTest` with `application-test.properties`.

**Custom constraints:** `@DurationUnit`, `@DataSize` from `spring-boot-configuration-processor` documentation validate time and size strings in properties (`10s`, `5MB`)—prefer them over manual parsing in setters.

### 5.4 Secrets handling (operational depth)

Secrets are **configuration values with confidentiality requirements**, not merely “prod profile keys.”

| Anti-pattern | Why it hurts |
|--------------|--------------|
| `application-prod.yml` with API keys in Git | History is forever; forks leak |
| Logging bound `@ConfigurationProperties` at INFO | Password fields appear in logs |
| Treating `/actuator/env` as debugging in prod | Exposes `SPRING_DATASOURCE_PASSWORD` |

**Preferred patterns:**

1. **Environment injection** — platform supplies `SPRING_DATASOURCE_PASSWORD`, `APP_PAYMENT_API_KEY`; no file in the image.
2. **Secret manager sidecars** — mount as env or files; use `spring.config.import=optional:configtree:/etc/secrets/` (Boot 2.4+) so each file becomes a property key.
3. **Short-lived credentials** — rotate at runtime; application reconnects via pooled `DataSource` refresh patterns (framework-specific).

**Relaxed binding and discovery:** env var `SPRING_DATASOURCE_PASSWORD` maps to the same property as `spring.datasource.password` in YAML—operators must know scanners and support tools will display env in crash dumps; restrict host access.

**Local dev:** use `.env` files **gitignored** or Testcontainers/local H2 without real production secrets. Never copy prod secrets to laptops without policy.

**What to log at startup:** active profiles, sanitized JDBC URL host (not password), feature flags. Use **`/actuator/info`** for non-sensitive build metadata.

## 6. Step-by-Step Examples

### 6.1 Multi-profile JDBC

`application.yml`:

```yaml
spring:
  profiles:
    active: local
---
spring:
  config:
    activate:
      on-profile: local
  datasource:
    url: jdbc:h2:mem:devdb
---
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://db.internal:5432/app
```

**test** profile often uses H2 or Testcontainers; **prod** uses real credentials from secrets store, not committed files.

### 6.2 Feature toggle

```properties
app.features.new-checkout=true
```

```java
@Configuration
@ConditionalOnProperty(name = "app.features.new-checkout", havingValue = "true")
public class NewCheckoutConfig { ... }
```

Ties configuration to **autoconfig-style** bean registration (Ch.01).

### 6.3 Random port in tests

```properties
# application-test.properties
server.port=0
```

Port `0` asks the OS for a free port—isolates parallel test runs.

### 6.4 `spring-boot-configuration-processor` (IDE metadata)

Add the processor dependency **optional** at compile time. It generates `spring-configuration-metadata.json` documenting your `@ConfigurationProperties` keys for IDE autocomplete—does not affect runtime.

**Pitfall:** metadata is build-time; renaming a field without updating docs in README still breaks operators who rely on old env var names.

### 6.5 Placeholder resolution

```properties
app.base-url=https://api.example.com
app.webhook-callback=${app.base-url}/hooks/payment
```

`${…}` resolves from `Environment` **after** property sources merge—later sources override earlier segments of the chain. Unresolvable placeholders fail startup unless marked optional with `${key:default}`.

## 7. Real-World Use Cases

- **`prod` profile:** real DataSource; structured JSON logging; strict actuator exposure (Ch.04).
- **`test` profile:** in-memory DB, disabled scheduled jobs.
- **Multi-region:** `application-ap-sg.yml` with CDN and DB endpoints.
- **Kubernetes:** mount `ConfigMap` as env vars; secrets as `SPRING_DATASOURCE_PASSWORD`.

## 8. Edge Cases and Limitations

- **Sensitive values** — never commit API keys; use secret managers; rotate credentials.
- **YAML merge surprises** — nested maps merge; a partial override may not replace an entire subtree. Prefer explicit structure or properties for critical trees.
- **SpEL in `@Value`** — powerful but opaque; prefer typed properties for team readability.
- **Profile in tests:** `@ActiveProfiles("test")` on test classes loads `application-test.yml`.

## 9. Common Mistakes

### Incorrect — duplicate keys across env and file without documenting precedence

Symptom: “works on my machine” with file URL while production env var points elsewhere. **Fix:** log active profiles and sanitize critical bound properties at startup (never log secrets).

### Incorrect — large trees with many `@Value` injections

Hard to validate and refactor. **Fix:** one `@ConfigurationProperties` record per bounded context (`MailProperties`, `PaymentClientProperties`).

### Incorrect — `@Profile` on a bean but profile never activated in CI

Bean silently missing; tests pass with mocks but production wiring incomplete.

### Incorrect — storing secrets in `application-prod.yml` in Git

Use environment or secret store; restrict `/actuator/env` in production (Ch.04).

## 10. Security Considerations

- Do not log full datasource passwords or API tokens.
- Validate outbound config (allowed hosts, TLS requirements) at bind time where possible.
- Separate **config server** credentials from application secrets.

## 11. Design Trade-offs

| Properties files | Environment-only |
|------------------|------------------|
| Easy local dev | No accidental commit if policy enforced |
| Versioned defaults | Requires platform discipline |

| Many small `@ConfigurationProperties` types | One giant `AppProperties` |
|---------------------------------------------|---------------------------|
| Clear boundaries | God-object config |

## 12. Revision and Mastery

1. How do you run with `prod` on the command line?
2. Difference between `@Profile` on a class vs on a `@Bean` method?
3. Why might `APP_MAIL_FROM` bind to `app.mail.from`?
4. When does profile-specific YAML override the default file for the same key?
5. Why validate `MailProperties` at startup?

## 13. Exercises

1. Design three profiles for local, CI, and production JDBC URLs.
2. Describe startup failure when `retry-attempts=0` violates `@Min(1)`.
3. Map cloud provider `DATABASE_URL` to `spring.datasource.url` using relaxed binding rules (narrative).
4. List which property source should win for emergency port override during an incident.

## 14. References

- [Spring Boot — Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html)
- [Spring Boot — Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)
- [Spring Boot — `@ConfigurationProperties`](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties)

## 15. Chapter Summary

Boot configuration unifies files, profiles, environment variables, and CLI into one **`Environment`**. **Profiles** switch files and beans; **`@ConfigurationProperties`** gives typed, validatable config instead of stringly-typed fields. Understanding **precedence** prevents environment drift—the same discipline as Module 36’s explicit wiring, applied to values.

## 16. Next Steps

[03-web-layer-and-validation.md](03-web-layer-and-validation.md)

## Review Checklist

- [x] Level 3 depth: precedence, profiles, properties, mistakes
- [x] No deferral of precedence rules without teaching a workable model
