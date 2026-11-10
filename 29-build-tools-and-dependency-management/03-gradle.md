# Gradle

> **Status:** Completed  
> **Module:** 29 — Build Tools and Dependency Management  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Conceptual alignment with Maven demos; Gradle not installed on handbook validation host

## 1. Learning Objectives

- Compare Gradle’s **build.gradle(.kts)** model to Maven’s POM.
- Identify **tasks**, **configurations** (`implementation`, `testImplementation`), and the **Gradle Wrapper**.
- Know when teams choose Gradle vs Maven.

## 2. Prerequisites

- Ch.02 — Maven coordinates and lifecycle.

## 3. Why This Concept Exists

Some projects need **faster incremental builds**, custom task graphs, and Kotlin DSL ergonomics. **Gradle** composes **tasks** with dependencies (not only a fixed lifecycle) while still resolving artifacts from Maven-compatible repositories.

## 4. Conceptual Explanation

| Gradle concept | Maven analogue |
|----------------|----------------|
| `plugins { java }` | Packaging + Java plugin conventions |
| `implementation` configuration | `compile` scope (api/implementation split in modern Java plugin) |
| `testImplementation` | `test` scope |
| `tasks.test` | `mvn test` / Surefire |
| `gradlew` wrapper | Reproducible Gradle version in repo |

**Kotlin DSL example** (equivalent to maven-awareness-demo — not executed on validation host):

```kotlin
plugins {
    java
    application
}
application {
    mainClass = "com.handbook.maven.MavenAwarenessDemo"
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
repositories {
    mavenCentral()
}
```

Run: `./gradlew run` (Unix) or `gradlew.bat run` (Windows).

Gradle resolves the same **Maven Central** coordinates; `group:artifact:version` strings match Maven dependency notation.

## 5. Internal Behavior (build tool, not JVM)

Gradle uses a **daemon** JVM for faster subsequent builds and tracks **task inputs/outputs** for incremental execution. This is Gradle product behavior, not JLS.

## 6. Real-World Use Cases

- Android and Kotlin-first repos (Gradle default).
- Large polyglot monorepos with custom tasks (codegen, Docker).
- Migrating from Maven via `gradle init` and gradual port.

## 7. Edge Cases and Limitations

- **Build script classpath** differs from application classpath — plugin versions matter.
- Caching can mask stale outputs until `clean`.
- Corporate mirrors must be configured in `settings.gradle` / `repositories {}`.

## 8. Common Mistakes

### Incorrect

Uses `compile` configuration (removed) instead of `implementation` / `api`.

### Corrected

Follow the Java Library plugin scope guidelines for Gradle 7+.

## 9. Best Practices

- Commit **Gradle Wrapper** (`gradlew`, `gradle/wrapper/gradle-wrapper.properties`).
- Use **toolchains** for JDK 21 alignment with CI.
- Mirror Maven’s reproducibility: lock versions, avoid floating `+` versions in production.

## 10. Design Trade-offs

Gradle flexibility vs Maven simplicity. Teams standardized on Maven often stay for BOM and plugin maturity; greenfield Android/Kotlin stacks gravitate to Gradle.

## 11. Validation note

This handbook’s runnable proof for Java 21 + dependencies uses **Maven** demos under `examples/backend/` and `examples/build-tools/maven-awareness-demo/`. Install Gradle locally to execute equivalent `build.gradle.kts` projects; concepts map one-to-one.

## 12. Chapter Summary

Gradle is a task-graph build system with Maven-compatible dependency resolution; understand Maven first, then map phases to Gradle tasks.

## 13. Next Steps

[04-java-platform-module-system.md](04-java-platform-module-system.md)

## Review Checklist

- [x] Gradle concepts documented; execution deferred (no Gradle on validation host)
