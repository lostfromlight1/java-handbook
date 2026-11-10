# Spring Boot Fundamentals

> **Status:** Completed  
> **Module:** 37 — Spring Boot  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Deep-dive  
> **Depth target:** Level 3 overall; **Level 4** on autoconfiguration conditions and classpath-triggered bean registration  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [spring-boot-minimal](../../examples/backend/spring-boot-minimal/) (`mvnw test`, fat JAR `/hello`); [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/); [manual-di-container](../../examples/backend/manual-di-container/)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Contrast **manual Spring Framework** setup (explicit `@Configuration`, XML, servlet deployment) with **Spring Boot** conventions (starters, autoconfiguration, embedded server).
- Decompose **`@SpringBootApplication`** into configuration, component scanning, and auto-config import.
- Explain **starters** and the **dependency management BOM** as curated classpath bundles, not separate frameworks.
- Describe how **`@ConditionalOnClass`**, **`@ConditionalOnMissingBean`**, and related conditions decide which `@Configuration` classes run.
- Explain **fat JAR** repackaging and how it relates to Module 29 classpath composition.
- List what Boot **does not replace**: JVM semantics, JPA rules, HTTP, security fundamentals, your domain logic.

## 2. Prerequisites

- **Module 36** — IoC, beans, MVC overview, proxies.
- **Module 29** — Maven coordinates, transitive dependencies, JAR manifests.
- **Servlet model (Module 32):** HTTP is still servlet-based under embedded Tomcat.

## 3. Why This Concept Exists

A classic Spring application required you to:

- Choose and wire a **DataSource**, **JPA `EntityManagerFactory`**, **transaction manager**, **Jackson**, and **`DispatcherServlet`**.
- Deploy a **WAR** to an external Tomcat and align library versions manually.

That work is repetitive across services. **Spring Boot** applies **convention over configuration**: add the right **starter** to the classpath, provide a `main` method, and Boot’s **autoconfiguration** registers sensible defaults **only when** matching classes and properties are present.

Boot is **not a different stack**—it is **Spring Framework + opinionated defaults + embedded servers + production starters** (actuator, metrics hooks, etc.).

## 4. Conceptual Explanation

### 4.1 Framework vs Boot

| Aspect | Spring Framework alone | Spring Boot |
|--------|------------------------|-------------|
| Object graph | You define most `@Bean` methods | Autoconfig + your overrides |
| Web server | External container or manual embed | Embedded Tomcat/Jetty/Undertow via starter |
| Versions | You align JAR versions | BOM from `spring-boot-starter-parent` |
| Entry point | `AnnotationConfigApplicationContext` in your `main` | `SpringApplication.run` + auto-config import |

Boot **accelerates** onboarding; it does **not** remove the need to understand beans, proxies, and MVC from Module 36.

### 4.2 `@SpringBootApplication` decomposed

```java
@SpringBootApplication
public class HandbookApplication {
    public static void main(String[] args) {
        SpringApplication.run(HandbookApplication.class, args);
    }
}
```

`@SpringBootApplication` is a composed annotation:

| Member | Role |
|--------|------|
| `@Configuration` | Marks the class as a source of `@Bean` definitions (same as Chapter 01). |
| `@EnableAutoConfiguration` | Imports Boot’s autoconfiguration registry from the classpath. |
| `@ComponentScan` (default) | Scans the application class package and **sub-packages** for `@Component`, `@Service`, `@RestController`, etc. |

**Package layout matters:** if `HandbookApplication` lives in `com.handbook.boot`, components in `com.handbook.web` are scanned, but `com.other.LegacyController` is not unless you add `@ComponentScan("com.other")`.

#### Component scan boundaries (what gets registered—and what does not)

`@ComponentScan` on `@SpringBootApplication` defaults to **`scanBasePackages` = the package of the annotated class** (not the classpath root). Spring registers stereotype types discovered under that package tree:

| Stereotype | Typical use |
|------------|-------------|
| `@Component` | Generic Spring-managed type |
| `@Service` | Domain/application service layer |
| `@Repository` | Persistence layer (exception translation) |
| `@Controller` / `@RestController` | Web endpoints |

**Included:** `com.handbook.boot.Main`, `com.handbook.boot.web.OrderController`, `com.handbook.boot.domain.Order` (if annotated).

**Excluded unless you configure otherwise:**

- Sibling packages above the application package (`com.handbook` when the app is in `com.handbook.boot`).
- Entirely separate roots (`com.legacy.api`) unless listed in `@ComponentScan(basePackages = …)` or imported via `@Import`.
- Types without a stereotype, even if they sit under a scanned package—plain `public class Helper {}` is invisible to component scanning.
- Third-party library classes (always).

**`@SpringBootApplication(scanBasePackages = "com.handbook")`** lifts the root so `com.handbook.legacy` is included—use when modularizing a monolith without moving the `main` class.

**Interaction with autoconfig:** scan registers **your** components; autoconfig registers **framework** beans. A `@RestController` in an unscanned package never becomes a bean, while `DispatcherServletAutoConfiguration` may still run if `spring-boot-starter-web` is present—requests simply have no matching handler.

**Edge case — multiple `@SpringBootApplication` classes in one module:** only the class passed to `SpringApplication.run` defines the default scan root unless every entry point repeats the same `scanBasePackages`.

`SpringApplication.run`:

1. Creates an **`ApplicationContext`** (typically `AnnotationConfigServletWebServerApplicationContext` for web apps).
2. Registers your application class and scanned components.
3. Processes **autoconfiguration** classes (conditional `@Configuration`).
4. Starts the **embedded web server** when `spring-boot-starter-web` is present.

**Verified ([spring-boot-minimal](../../examples/backend/spring-boot-minimal/)):** Boot 3.4.1 starts embedded Tomcat on port `18080`; `GET /hello` → `{"message":"bootMinimal=ok"}`.

### 4.3 Starters and the BOM

A **starter** is a Maven dependency that pulls a **curated set** of libraries:

| Starter | Brings (simplified) |
|---------|---------------------|
| `spring-boot-starter-web` | Spring MVC, Jackson, embedded Tomcat, `spring-web` |
| `spring-boot-starter-data-jpa` | Hibernate, JDBC, transaction manager, Spring Data JPA |
| `spring-boot-starter-security` | Spring Security filter chain defaults |
| `spring-boot-starter-test` | JUnit Jupiter, AssertJ, Mockito, Spring Test, MockMvc support |

The parent POM **`spring-boot-starter-parent`** imports a **BOM** (`spring-boot-dependencies`) that pins compatible versions for hundreds of artifacts. You still use normal Maven resolution (Module 29)—Boot does not replace the build tool.

**Why starters reduce risk:** without a BOM, teams pin `spring-web`, `hibernate-core`, and `jackson-databind` independently. A minor Jackson bump can break Spring’s `ObjectMapper` customization. Starters pull **integration-tested sets**; the parent BOM is the single dial for upgrading the whole stack in a deliberate release.

**Transitive classpath = autoconfig triggers:** adding `spring-boot-starter-data-jpa` places Hibernate and JDBC types on the classpath, which flips `@ConditionalOnClass` inside JPA autoconfig. Removing a starter often removes beans you assumed were “built into Spring”—they were conditional on those classes.

**Optional vs provided:** some starters mark container-provided APIs as `provided` when targeting external deployment; embedded-server starters bring `tomcat-embed-*` as compile/runtime dependencies. Changing packaging from JAR to WAR shifts which server integration autoconfig runs—do not assume identical beans across packaging modes without checking conditions.

### 4.4 What Boot does **not** replace

Boot does **not**:

- Change **Java language rules**, memory model, or threading.
- Fix incorrect **JPA** mapping or transaction boundaries (Module 35).
- Automatically design secure **authorization** rules (Module 36 overview; Module 39 depth).
- Remove the need to understand **HTTP status codes** and DTO boundaries (Module 32, 38).

Treat Boot as **automation around Spring**, not a substitute for core knowledge.

## 5. Mechanism and Execution Flow — Autoconfiguration (Level 4)

### 5.1 Where autoconfig classes come from

`@EnableAutoConfiguration` loads candidate classes listed in:

`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

(on older Boot versions, `spring.factories` served a similar role). Each entry is a `@Configuration` class, often annotated with multiple **`@Conditional*`** annotations.

Autoconfig runs **after** your own configuration is registered but respects **`@ConditionalOnMissingBean`** when you define overrides.

#### 5.1.1 Walkthrough: from `AutoConfiguration.imports` to live beans (Level 4)

Treat startup as a **pipeline** you can trace in logs when `--debug` is enabled:

```text
1. SpringApplication.run(HandbookApplication.class)
2. Create ApplicationContext (web → AnnotationConfigServletWebServerApplicationContext)
3. Register HandbookApplication as a @Configuration source
4. @ComponentScan runs → candidate components become BeanDefinitions
5. @EnableAutoConfiguration imports AutoConfigurationImportSelector
6. Selector reads META-INF/spring/...AutoConfiguration.imports from every JAR
7. De-duplicate and sort candidates (respect @AutoConfigureBefore / After)
8. For each auto-config class: evaluate @Conditional* (classpath, properties, existing beans)
9. Matching configs processed like user @Configuration → @Bean methods executed
10. Context refresh completes → embedded server starts (if web starter present)
```

**Step 6–8 in prose:** each line in `AutoConfiguration.imports` (for example `org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration`) is a **candidate**, not a guarantee. Spring does not instantiate every class blindly. It builds a **condition evaluation report** listing, per configuration class, which `@ConditionalOnClass`, `@ConditionalOnMissingBean`, `@ConditionalOnProperty`, and custom conditions matched or failed.

Example mental trace for `spring-boot-starter-web` on the classpath:

1. `DispatcherServletAutoConfiguration` — `@ConditionalOnClass(DispatcherServlet.class)` **match** (servlet API + spring-webmvc present).
2. `JacksonAutoConfiguration` — **match** when Jackson is on the classpath (pulled by the web starter).
3. `DataSourceAutoConfiguration` — `@ConditionalOnClass(DataSource.class)` may **match**, but the `@Bean` method is guarded by `@ConditionalOnMissingBean(DataSource.class)`; if you defined `DataSource`, the autoconfig **backs off**.

**Bean registration:** when a configuration class passes all conditions, Spring treats its `@Bean` methods like any user `@Configuration`: factory methods run during context refresh (or produce lazy proxies depending on setup). The resulting beans are indistinguishable from manually declared ones in the graph—only their **origin metadata** differs (useful in `/actuator/beans` when exposed).

**Ordering matters:** `@AutoConfigureAfter(DataSourceAutoConfiguration.class)` on `HibernateJpaAutoConfiguration` ensures a `DataSource` exists before JPA tries to build an `EntityManagerFactory`. Breaking ordering (custom autoconfig without `@AutoConfigureAfter`) produces obscure `BeanCurrentlyInCreationException` or missing dependency errors.

**De-duplication:** multiple JARs must not list conflicting autoconfig for the same concern; Boot’s BOM aligns versions so one winning `JacksonAutoConfiguration` applies.

### 5.2 Conditional annotations (critical ones)

| Annotation | Effect |
|------------|--------|
| `@ConditionalOnClass` | Register this config only if named classes are on the classpath (e.g. `DataSource`, `DispatcherServlet`). |
| `@ConditionalOnMissingBean` | Back off if the context already has a bean of that type—**your explicit `@Bean` wins**. |
| `@ConditionalOnProperty` | Enable when `application.properties` matches (`havingValue`, `matchIfMissing`). |
| `@ConditionalOnWebApplication` | Servlet vs reactive vs none. |
| `@AutoConfigureAfter` / `@Before` | Ordering when one autoconfig depends on another (e.g. DataSource before JPA). |

Illustrative pattern (simplified from Boot’s style):

```java
@Configuration
@ConditionalOnClass(DataSource.class)
@ConditionalOnMissingBean(DataSource.class)
public class DataSourceAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
```

**Mechanism:**

1. Classpath contains JDBC + Hikari → `@ConditionalOnClass` passes.
2. You did not define your own `DataSource` → bean is created from properties.
3. You **do** define `@Bean DataSource custom()` → autoconfig **skips** its bean.

### 5.3 Debugging “magic”

When a bean is missing or duplicated:

- Run with **`--debug`** or set `logging.level.org.springframework.boot.autoconfigure=DEBUG` to print **condition evaluation report** (which autoconfig matched or failed).
- Read **failure analyzers** in startup logs for actionable hints.

This is labeled **Spring Boot framework behavior** (not JLS).

#### Condition evaluation report (how to read it)

With `--debug`, near the end of startup Boot prints sections such as **Positive matches** and **Negative matches**:

```text
Positive matches:
-----------------
   DispatcherServletAutoConfiguration matched:
      - @ConditionalOnClass found required class 'jakarta.servlet.Servlet'

Negative matches:
-----------------
   DataSourceAutoConfiguration:
      Did not match:
         - @ConditionalOnMissingBean (types: DataSource) found bean 'customDataSource'
```

**How to use it:**

1. Identify the **missing capability** (no `DataSource`, duplicate `ObjectMapper`, wrong embedded server).
2. Search the report for the autoconfig class name (grep logs for `*AutoConfiguration`).
3. Read the **first failed condition**—that is usually the root cause, not the final stack trace.
4. If your bean should win but autoconfig still registers one, check **bean name and type** (`@ConditionalOnMissingBean` can be type-only or name-specific).

**Common false leads:** a negative match on unrelated autoconfig is normal; focus on the configuration tied to the bean you care about.

#### Failure analyzers (mental model)

When the context **fails to start**, Boot invokes **`FailureAnalyzer`** implementations before printing a raw stack trace. Each analyzer knows a narrow failure pattern and returns a **`FailureAnalysis`** with **description**, **action**, and sometimes **cause**.

| Symptom class | Typical analyzer hint |
|---------------|------------------------|
| Port already in use | BindException → “Identify and stop the process listening on port 8080” |
| Missing driver | `Cannot load driver class` → add JDBC driver dependency |
| Cycle in beans | circular reference → which beans participate |
| Misconfigured JPA | Hibernate / schema errors → datasource or `ddl-auto` |

**Workflow for developers:**

1. Scroll to the **blockquoted** Boot error section (starts with `APPLICATION FAILED TO START`).
2. Read **Description** and **Action** before expanding nested stack traces.
3. If no analyzer matched, fall back to the root `Caused by:` in the stack—often a `BeanCreationException` one level down.
4. Re-run with `--debug` to correlate with condition report if the failure is “bean not found” rather than “bind failed”.

**Limitation:** custom `@Configuration` mistakes (wrong `@Qualifier`, package not scanned) are **not** always covered by built-in analyzers—you still need Module 36 IoC literacy.

#### Duplicate or “wrong” bean scenarios

| Situation | What happened | Fix direction |
|-----------|---------------|---------------|
| Two `DataSource` beans | Autoconfig + your `@Bean` without `@Primary` | Mark one `@Primary` or exclude autoconfig |
| No `RestTemplate` / `WebClient` | Starter not on classpath; autoconfig not loaded | Add starter or define `@Bean` |
| Embedded Tomcat starts but no MVC | `spring.main.web-application-type=none` or missing web starter | Check properties and dependencies |

### 5.4 Replacing or excluding autoconfig

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
```

Or in properties:

```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

Use when integrating a custom stack or writing tests without a database.

## 6. Fat JAR Packaging

### 6.1 Thin JAR vs executable fat JAR

A **thin** application JAR contains only your `.class` files; dependencies sit in `lib/`.

Boot’s **`spring-boot-maven-plugin`** **repackages** the build into an executable **fat JAR**:

- Your classes in `BOOT-INF/classes/`
- Dependencies in `BOOT-INF/lib/`
- **`JarLauncher`** in the manifest `Main-Class` loads the nested classpath and starts `HandbookApplication.main`

**Verified:** `java -jar target/spring-boot-minimal-1.0.0-SNAPSHOT.jar` starts Tomcat and serves `/hello` (see demo README).

**Launcher mechanics (labeled framework behavior):** the fat JAR manifest’s `Main-Class` is `org.springframework.boot.loader.launch.JarLauncher` (Boot 3.2+ loader package; older artifacts used `JarLauncher` under `org.springframework.boot.loader`). The launcher:

1. Opens the outer JAR as an archive.
2. Constructs a **nested URL classpath** from `BOOT-INF/lib/*.jar` and `BOOT-INF/classes/`.
3. Delegates to your application `main` in `BOOT-INF/classes/` with that class loader.

This is why **you cannot** treat the repackaged artifact like a thin JAR on a flat `lib/` classpath without the launcher—dependencies are **inside** the archive. Tools that scan JARs for security (CVE scanners) must unzip nested libs.

**`PropertiesLauncher`** (advanced): alternative manifest entry for external config dirs or additional classpath roots—used in some ops patterns, not required for typical microservices.

### 6.2 Trade-offs

| Fat JAR | External container WAR |
|---------|-------------------------|
| Simple deploy (`java -jar`) | Shared Tomcat across apps |
| Larger artifact | Container manages patching |
| One process per service | Traditional ops model |

## 7. Comparison to Plain Java Demos

| Task | Plain Java (this repo) | Spring Boot |
|------|------------------------|-------------|
| HTTP server | [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/) | Embedded Tomcat + `@RestController` |
| Wiring | [manual-di-container](../../examples/backend/manual-di-container/) | `ApplicationContext` + scan + autoconfig |
| Build / run | `javac` + `java` | `mvnw package` + `java -jar` |

**rest-httpserver-demo verified output:**

```text
GET /users/1 status=200 body={"id":1,"name":"Lin"}
POST /users status=201
restDemo=ok
```

Same status discipline; Boot adds binding, validation hooks, and autowired dependencies.

### 7.1 Boot application lifecycle hooks (brief)

Beyond `main`, production services often register:

| Hook | Use |
|------|-----|
| `ApplicationRunner` / `CommandLineRunner` | One-shot startup tasks after context is ready |
| `@EventListener ApplicationReadyEvent` | Start consuming queues when health should report UP |
| `@PreDestroy` on beans | Release non-Spring resources |

Runners execute **after** autoconfig finishes—safe to autowire `DataSource` and `MeterRegistry`. Avoid heavy work in `@PostConstruct` that blocks readiness if Kubernetes probes are strict.

## 8. Version Notes (Boot 3.x)

- **Jakarta EE** namespace (`jakarta.*` servlets, JPA, validation)—not `javax.*` for EE APIs.
- **Java 17+** baseline; **Java 21** fully supported for Boot 3.2+.
- This handbook targets **Boot 3.4.1** in [spring-boot-minimal](../../examples/backend/spring-boot-minimal/).

## 9. Real-World Use Cases

- **Microservice** with one executable JAR per container image.
- **Internal admin API** spun up quickly with `starter-web` + `starter-data-jpa`.
- **Integration tests** loading a slice or full context (`@SpringBootTest` — Chapter 04).

## 10. Common Mistakes

### Incorrect — treating Boot as a bypass for learning Spring core

When autoconfig fails, you need IoC, conditional beans, and MVC flow knowledge to fix it.

### Incorrect — business logic in the `main` application class

Keep bootstrap thin; put controllers and services in dedicated types.

### Incorrect — assuming `@ConditionalOnMissingBean` always sees your bean

Bean definition order and `@Import` timing matter; define custom beans in clear `@Configuration` classes.

## 11. Design Trade-offs

| Pros | Cons |
|------|------|
| Fast onboarding | “Magic” until you read condition reports |
| Curated compatible versions | BOM upgrades affect the whole stack |
| Embedded server simplicity | Higher baseline memory than a minimal HttpServer |

## 12. Revision and Mastery

1. What three annotations does `@SpringBootApplication` combine?
2. When does `DataSourceAutoConfiguration` refuse to create a `DataSource`?
3. How is a Boot fat JAR different from a thin JAR + `lib/` folder?
4. What file lists autoconfig classes on Boot 3?
5. Why does component scan depend on package placement?

## 13. Exercises

1. List beans you expect from `spring-boot-starter-data-jpa` before writing any `@Configuration`.
2. Sketch package layout: `com.app`, `com.app.web`, `com.app.domain` — which package holds `SpringBootApplication`?
3. Explain how removing Tomcat from the classpath while adding Jetty starter could change the embedded server (property `spring.main.web-application-type` included in narrative).
4. Run `mvnw test` on [spring-boot-minimal](../../examples/backend/spring-boot-minimal/) and correlate log lines with Tomcat + `DispatcherServlet` initialization.

## 14. References

- [Spring Boot Reference — Auto-configuration](https://docs.spring.io/spring-boot/reference/using/auto-configuration.html)
- [Spring Boot Maven Plugin — Repackage](https://docs.spring.io/spring-boot/maven-plugin/packaging.html)

## 15. Chapter Summary

Spring Boot wraps Spring Framework with **starters**, **conditional autoconfiguration**, and **embedded servers**. Your code remains standard Java beans; Boot decides which default beans appear based on **classpath and properties**, stepping aside when you provide your own. Learn Module 36 mechanics first; use Boot for productive defaults, not ignorance of what it wired.

## 16. Next Steps

[02-configuration-and-profiles.md](02-configuration-and-profiles.md)

## Review Checklist

- [x] Mandatory Depth Review; autoconfig at Level 4
- [x] Verified spring-boot-minimal demo cited with real output
- [x] Boot 3 / Jakarta noted; plain-Java parallels linked
