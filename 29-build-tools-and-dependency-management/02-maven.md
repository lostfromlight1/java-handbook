# Maven

> **Status:** Completed  
> **Module:** 29 — Build Tools and Dependency Management  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1; Maven 3.9.6 (portable install for handbook runs)

## 1. Learning Objectives

- Read a **`pom.xml`**: coordinates, dependencies, plugins, lifecycle phases.
- Run **`mvn compile`**, **`test`**, **`package`**, and **`exec:java`**.
- Understand **repositories**, **scopes**, and **transitive** dependencies.

## 2. Prerequisites

- Ch.01 — JARs and classpath (Maven places artifacts on the compile/test classpath).

## 3. Why This Concept Exists

Hand-maintaining dozens of JAR versions and compile commands does not scale. **Apache Maven** uses convention-over-configuration: standard directory layout, a **Project Object Model (POM)**, and a **lifecycle** (`compile`, `test`, `package`, …) so CI and teammates reproduce the same build.

## 4. Conceptual Explanation

| POM section | Purpose |
|-------------|---------|
| `groupId`, `artifactId`, `version` | **Coordinates** identifying the project artifact. |
| `<dependencies>` | Libraries Maven downloads and adds to classpaths. |
| `<scope>` | `compile` (default), `test`, `provided`, `runtime` — when a dependency is visible. |
| `<build><plugins>` | Compiler release, Surefire (tests), JAR packaging, exec, etc. |
| **Local repository** | `~/.m2/repository` cache of downloaded artifacts. |
| **Central** | Default remote repository (`repo1.maven.org`). |

**Lifecycle (default):** `validate` → `compile` → `test` → `package` → `verify` → `install` → `deploy`. Running `mvn test` runs prior phases through `test`.

Maven is **not** part of the JDK — install separately or use a CI image with Maven preinstalled.

## 5. Step-by-Step Examples

Demo: [maven-awareness-demo](../../examples/build-tools/maven-awareness-demo/).

**Verified output:**

```text
mavenCompiled=ok
javaVersion=21.0.12.1
```

```bash
mvn -q compile exec:java
```

`-q` quiets plugin logging; `exec:java` runs the configured `mainClass`.

### JDBC + test example (cross-links Module 30/33)

[h2-jdbc-demo](../../examples/backend/h2-jdbc-demo/) — shows dependencies on H2 and JUnit 5. Verified exec output:

```text
name=Ada
rowCount=1
jdbcH2=ok
```

`mvn -q test` completes successfully (JUnit `SumPositiveTest`).

## 6. Real-World Use Cases

- Multi-module backends (`<modules>`) with shared parent POM.
- CI pipelines: `mvn -B verify` on every pull request.
- Publishing to corporate Nexus/Artifactory.

## 7. Edge Cases and Limitations

- **Dependency convergence** — two libraries pulling different versions of the same artifact; Maven’s nearest-wins rule may hide conflicts until runtime.
- **SNAPSHOT** versions change over time — lock releases in production.
- Offline builds need a populated local repository.

## 8. Common Mistakes

### Incorrect

Pins ancient `maven-compiler-plugin` without `<release>21</release>` — bytecode or API mismatches.

### Corrected

Use `<maven.compiler.release>21</maven.compiler.release>` (or compiler plugin `release`).

## 9. Best Practices

- Commit **`pom.xml`**, not `target/` or downloaded JARs.
- Use **BOM** imports (`dependencyManagement`) in large orgs.
- Prefer **`mvnw`** wrapper in open source so contributors share Maven version.

## 10. Design Trade-offs

Maven: XML, rigid lifecycle, excellent ecosystem. **Gradle** (Ch.03) trades XML for Groovy/Kotlin DSL and finer incremental builds.

## 11. Revision and Mastery

- What does `test` scope mean for transitive compile dependencies?
- Which phase creates the JAR?

## 12. Chapter Summary

Maven models projects as coordinates plus dependencies; it automates classpath assembly and repeatable phases that raw `javac` would script by hand.

## 13. Next Steps

[03-gradle.md](03-gradle.md)

## Review Checklist

- [x] Verified maven-awareness-demo and h2-jdbc-demo via Maven 3.9.6
