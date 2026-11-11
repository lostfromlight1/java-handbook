# Java vs Go (for Java learners)

> **Status:** Drafted  
> **Purpose:** Contrast Go with Java so Java concepts click faster—**not** a Go tutorial  
> **Target:** Java 21; HotSpot notes labeled  
> **Last updated:** 2026-11-11  
> **Related:** [24-concurrency](../24-concurrency-and-multithreading/README.md) · [14-exceptions](../14-exceptions-and-error-handling/README.md) · [29-build-tools](../29-build-tools-and-dependency-management/README.md)

You do **not** need Go knowledge to learn Java. Use this appendix only when a Go comparison helps, or when you already know Go and are transferring skills.

---

## Snapshot

| Dimension | Java (21) | Go (typical) |
|-----------|-----------|--------------|
| Typing | Static, nominal, generics with erasure | Static, structural interfaces, generics (since 1.18) |
| Runtime | JVM (+ HotSpot JIT common) | Native compilation, smaller runtime |
| Concurrency unit | Platform threads + **virtual threads** | Goroutines + channels |
| Errors | Exceptions (checked + unchecked) | Explicit `error` values (panic rare) |
| Packaging | JARs, modules (JPMS), Maven/Gradle | Modules (`go.mod`), single static-ish binaries |
| Null | `null` references; `Optional` as API spice | `nil` interfaces/pointers with different traps |
| Inheritance | Class inheritance + interfaces | No class inheritance; composition + interfaces |

---

## Typing and abstraction

### What Java learners should notice

Java centers on **nominal types**: a type is what you name and declare (`class`, `interface`, `record`). Implements relationships are explicit. Generics are powerful at compile time but subject to **type erasure** at runtime ([Module 13](../13-generics/03-type-erasure-and-restrictions.md)).

Go interfaces are **satisfied implicitly** if methods match. That reduces ceremony and changes API design culture (small interfaces). Neither model is “more OOP”; they optimize different trade-offs.

### Mental model for transferring

- Prefer Java **interfaces** for contracts and test doubles—same idea as Go’s small interfaces, with explicit `implements`.
- Do not expect Go-style implicit satisfaction; Java needs a declared subtype relationship (or adapters).
- Records and sealed types ([Module 15](../15-enums-records-and-sealed-types/README.md)) cover many “data + variants” designs Go handles with structs and interfaces.

---

## Concurrency: goroutines vs threads / virtual threads

### Go’s picture (awareness only)

Goroutines are cheap concurrently scheduled functions. Channels and `select` coordinate. The Go runtime multiplexes goroutines onto OS threads.

### Java’s picture (what you must learn)

Historically, Java concurrency meant **platform threads** (expensive at huge counts) plus executors, locks, and `java.util.concurrent` ([Module 24](../24-concurrency-and-multithreading/README.md)).

**Java 21 virtual threads** close much of the “cheap concurrency” gap for **blocking I/O-bound** workloads: you can write straightforward blocking code at high concurrency without inventing reactive style for every service. They are not identical to goroutines:

| Concern | Virtual threads (Java 21) | Goroutines (Go) |
|---------|---------------------------|-----------------|
| Scheduling | JVM schedules virtual threads onto carrier platform threads | Go runtime schedules goroutines |
| Idiom | Often plain blocking + structured concurrency APIs | Channels / `select` are central |
| CPU-bound work | Still limited by cores; VTs do not invent parallelism | Same core limit |
| Pinning / blocking | Long `synchronized` / native calls can pin carriers (**HotSpot/implementation detail**) | Different runtime constraints |

### JMM still matters

Go’s memory model and Java’s **JMM** ([Module 25](../25-java-memory-model/README.md)) both exist; learning Java means learning happens-before, `volatile`, and safe publication—even if you came from Go channels that “feel” ordered.

---

## Errors vs exceptions

Go encourages returning `(result, error)` and handling failures in-band. Panic/recover is for truly exceptional paths.

Java uses a **Throwable hierarchy**: checked exceptions, unchecked exceptions, and errors ([Module 14](../14-exceptions-and-error-handling/README.md)). Stack unwinding, `try`/`catch`/`finally`, and try-with-resources are core.

**Transfer tips:**

- Treat Go’s explicit `error` as similar in spirit to **checked** “you must confront this” design—Java just uses the type system differently.
- Prefer meaningful exception types and clear boundaries; do not catch `Exception` everywhere as a fake `if err != nil`.
- For APIs shared with non-Java clients, map failures to status codes/problem details ([Module 38](../38-rest-api-development/02-dtos-errors-and-versioning.md)), not raw stack traces.

---

## Packaging and tooling

| Concern | Java | Go |
|---------|------|-----|
| Dependency graph | Maven Central + Maven/Gradle | Module proxy + `go.mod` |
| Artifact | JAR/WAR, sometimes native image | Usually a single binary |
| Build reproducibility | Lockfiles/tooling vary; Gradle/Maven configs matter | `go.sum` is central |
| Modules | JPMS optional for apps; strong for JDK ([Module 29](../29-build-tools-and-dependency-management/04-java-platform-module-system.md)) | Language-level modules from day one |

Java’s strength is a huge ecosystem and mature build/test tooling. Go’s strength is simple deployable binaries and a small standard toolchain. Learning Java means becoming comfortable with **classpath vs module path**, dependency scopes, and IDE-driven builds—not pretending JARs are “just binaries.”

---

## Performance mental model

- **Java / HotSpot:** Warm-up, tiered JIT, escape analysis, GC pauses/throughput trade-offs ([Modules 27–28](../27-garbage-collection/README.md)). Peak throughput after warm-up can be excellent; startup and footprint need attention (containers, GraalVM native image awareness in [Module 49](../49-cloud-and-containerization/03-graalvm-and-native-image-concepts.md)).
- **Go:** Fast startup, simpler performance story for many CLIs and network services; GC exists but the operational knobs differ.

Do not claim one is universally faster. Measure the workload you care about.

---

## When each shines

### Prefer Java when

- Large enterprise domains, rich libraries (Spring, Hibernate ecosystem), and long-lived services with heavy tooling needs
- Strong static modeling (sealed types, rich generics APIs) and large team refactoring support
- Existing JVM investment, polyglot JVM languages, or application-server ecosystems
- High-concurrency **blocking** I/O services on Java 21+ virtual threads

### Prefer Go when (awareness)

- Small network services/CLIs where a single static binary and tiny ops surface matter most
- Teams standardized on Go’s concurrency style and standard library net/http stack
- Greenfield infra tooling where Go’s culture and deployment model dominate

### Learning advice

Master Java’s type system, concurrency utilities, JMM, and packaging on their own terms. Use this comparison only as a bridge—never as a prerequisite. If a handbook chapter mentions Go, it should still teach the Java mechanism fully without requiring Go.

---

## See also

- Virtual threads: [24/05](../24-concurrency-and-multithreading/05-virtual-threads-and-structured-concurrency.md)
- Exceptions: [14/01](../14-exceptions-and-error-handling/01-throwable-hierarchy.md)
- Build tooling: [29/README](../29-build-tools-and-dependency-management/README.md)
- Interview concurrency: [52/02](../52-interview-preparation/02-jvm-and-concurrency-interviews.md)
