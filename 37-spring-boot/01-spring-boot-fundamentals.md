# Spring Boot Fundamentals

> **Status:** Completed  
> **Module:** 37 — Spring Boot  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Note:** Spring Boot snippets are **conceptual** in this handbook (Maven CLI unavailable in validation environment). Mechanics tie to Module 36 pure-Java demos.

## 1. Learning Objectives

- Contrast **Spring Framework** manual configuration with **Spring Boot** autoconfiguration and starters.
- Explain `@SpringBootApplication` composition and **component scanning**.
- Describe **fat JAR** deployment as classpath composition (Module 29).
- Recognize what Boot does **not** replace: JVM, JPA semantics, HTTP, security fundamentals.

## 2. Prerequisites

- Module 36 — IoC, beans, MVC overview.
- Module 29 — Maven coordinates, fat JAR concept.

## 3. Why This Concept Exists

Configuring a servlet container, Jackson, DataSource, and transaction manager by hand is repetitive. **Spring Boot** applies **convention over configuration**: add `spring-boot-starter-web`, write a `@SpringBootApplication` class, and the classpath triggers **autoconfiguration** classes that register beans when sensible defaults apply.

Boot is **not a different language**—it is Spring Framework plus auto-config, embedded servers, and production-ready starters.

## 4. Conceptual Explanation

`@SpringBootApplication` combines:

| Annotation | Role |
|------------|------|
| `@Configuration` | Java config beans |
| `@EnableAutoConfiguration` | Import autoconfig from `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` |
| `@ComponentScan` | Find `@Component` in the application package and below |

Minimal application (conceptual):

```java
@SpringBootApplication
public class HandbookApplication {
    public static void main(String[] args) {
        SpringApplication.run(HandbookApplication.class, args);
    }
}
```

`SpringApplication.run` starts an **ApplicationContext**, applies autoconfig, then starts **embedded Tomcat** (for `spring-boot-starter-web`) if on classpath.

### Starters

| Starter | Brings (simplified) |
|---------|---------------------|
| `spring-boot-starter-web` | Spring MVC, Jackson, Tomcat |
| `spring-boot-starter-data-jpa` | Hibernate, DataSource autoconfig |
| `spring-boot-starter-security` | Security filter chain defaults |

**Dependency management** BOM pins compatible versions—still Maven/Gradle resolution (Module 29).

## 5. Autoconfiguration mechanics (framework)

Autoconfig classes are `@Configuration` guarded by **`@Conditional*`**:

- `@ConditionalOnClass` — only if type on classpath (e.g. `DataSource`).
- `@ConditionalOnMissingBean` — back off if you defined your own bean.

You override by defining your own `@Bean` of the same type—Boot steps aside.

## 6. Comparison to plain Java

| Task | Plain Java (this repo) | Spring Boot |
|------|------------------------|-------------|
| HTTP server | [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/) | Embedded Tomcat + `@RestController` |
| Wiring | [manual-di-container](../../examples/backend/manual-di-container/) | `ApplicationContext` + component scan |
| Packaging | `javac` + manifest | `spring-boot-maven-plugin` repackage fat JAR |

Run Boot locally when Maven is installed:

```xml
<!-- examples/backend/spring-boot-minimal/pom.xml — optional in your fork -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.1</version>
</parent>
```

(Boot 3.x requires **Jakarta EE** namespace and **Java 17+**; Java 21 is fully supported.)

## 7. Real-World Use Cases

- Microservice with single executable JAR.
- Rapid internal admin APIs.
- Integration tests with `@SpringBootTest` (Ch.04).

## 8. Common Mistakes

### Incorrect — treating Boot as bypass for learning Spring core

Annotations hide wiring; debugging fails without IoC/proxy knowledge.

### Incorrect — giant `@SpringBootApplication` class with business logic

Keep `main` bootstrap thin; place controllers/services in separate types.

## 9. Design Trade-offs

| Pros | Cons |
|------|------|
| Fast onboarding | “Magic” until you read conditions |
| Curated stack versions | Upgrade coupling to BOM |
| Embedded server simplicity | Memory footprint vs plain Netty/custom |

## 10. Revision and Mastery

- What does `@ConditionalOnMissingBean` protect?
- How is a fat JAR different from a thin JAR + lib folder?

## 11. Exercises

1. List beans you expect from `spring-boot-starter-data-jpa` before writing config.
2. Sketch package layout: `com.app`, `com.app.web`, `com.app.domain`.
3. Explain why removing Tomcat from classpath might switch to Jetty (optional starter).

## 12. Chapter Summary

Spring Boot automates Spring Framework setup via starters, conditions, and embedded servers while remaining standard Java and Spring beans underneath. Learn Module 36 mechanics first; use Boot for productive defaults, not as a substitute for core knowledge.

## 13. Next Steps

[02-configuration-and-profiles.md](02-configuration-and-profiles.md)

## Review Checklist

- [x] Autoconfig and starters explained; plain-Java parallels cited
- [x] Boot 3 / Jakarta noted
