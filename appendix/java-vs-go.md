# Java vs Go (for Java learners)

> **Status:** Completed  
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

### Illustrative concurrency mental models

**Illustrative** — shapes only; not Verified runs.

```java
// Java 21 — many blocking tasks, virtual threads
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        exec.submit(() -> callRemoteApi()); // blocking OK on VT
    }
}
```

```go
// Go — many tasks, goroutines + WaitGroup (awareness)
var wg sync.WaitGroup
for i := 0; i < 10_000; i++ {
    wg.Add(1)
    go func() {
        defer wg.Done()
        callRemoteAPI()
    }()
}
wg.Wait()
```

Go often coordinates with channels; Java often uses executors, futures, or structured concurrency APIs. Do not force channel-shaped designs into Java unless the problem truly needs that topology.

### JMM still matters

Go’s memory model and Java’s **JMM** ([Module 25](../25-java-memory-model/README.md)) both exist; learning Java means learning happens-before, `volatile`, and safe publication—even if you came from Go channels that “feel” ordered.

---

## Errors vs exceptions

Go encourages returning `(result, error)` and handling failures in-band. Panic/recover is for truly exceptional paths.

Java uses a **Throwable hierarchy**: checked exceptions, unchecked exceptions, and errors ([Module 14](../14-exceptions-and-error-handling/README.md)). Stack unwinding, `try`/`catch`/`finally`, and try-with-resources are core.

### Illustrative side-by-side (error handling)

**Illustrative** — idioms, not a claim that APIs are equivalent.

```go
// Go — errors as values
data, err := os.ReadFile(path)
if err != nil {
    return fmt.Errorf("read config: %w", err)
}
```

```java
// Java — exceptions + try-with-resources
try (var in = Files.newInputStream(path)) {
    return in.readAllBytes();
} catch (IOException e) {
    throw new UncheckedIOException("read config", e);
}
```

**Transfer tips:**

- Treat Go’s explicit `error` as similar in spirit to **checked** “you must confront this” design—Java just uses the type system differently.
- Prefer meaningful exception types and clear boundaries; do not catch `Exception` everywhere as a fake `if err != nil`.
- For APIs shared with non-Java clients, map failures to status codes/problem details ([Module 38](../38-rest-api-development/02-dtos-errors-and-versioning.md)), not raw stack traces.

---

## Packaging and tooling

| Concern | Java | Go |
|---------|------|-----|
| Dependency graph | Maven Central + Maven/Gradle | Module proxy + `go.mod` |
| Lock / integrity | Gradle lockfiles / Maven versions plugin (opt-in culture) | `go.sum` is central and expected |
| Artifact | JAR/WAR, sometimes native image | Usually a single binary |
| Build reproducibility | Strong when you pin plugins + deps; easy to get wrong | `go build` + `go.sum` is the default story |
| Modules | JPMS optional for apps; strong for JDK ([Module 29](../29-build-tools-and-dependency-management/04-java-platform-module-system.md)) | Language-level modules from day one |
| Test runner | JUnit (via build tool) | `go test` in the toolchain |
| Format / lint | Spotless, Checkstyle, Error Prone (ecosystem) | `gofmt` / `go vet` built into culture |
| Cross-compile | Possible (esp. native image / containers) | First-class `GOOS`/`GOARCH` |
| IDE expectation | IntelliJ / Eclipse deep Java support | GoLand / VS Code + gopls |

Java’s strength is a huge ecosystem and mature build/test tooling. Go’s strength is simple deployable binaries and a small standard toolchain. Learning Java means becoming comfortable with **classpath vs module path**, dependency scopes, and IDE-driven builds—not pretending JARs are “just binaries.”

**Practical Java build checklist for Go migrants:** pick Maven *or* Gradle; pin the JDK in CI; understand `compile` vs `runtime` vs `test` scopes; know how your fat JAR is built; run tests the same way locally and in CI.

---

## Common migration mistakes (Go → Java)

1. **Catching everything like `if err != nil`.** Empty or overly broad `catch (Exception e)` hides failures; use typed exceptions and boundaries.
2. **Assuming interfaces are implicit.** Java needs `implements` (or adapters); structural typing will not save you at compile time.
3. **Spawning unbounded platform threads.** Go’s “just `go func`” habit on Java platform threads exhausts the OS; prefer virtual threads or bounded executors.
4. **Expecting a single static binary by default.** Fat JARs and containers are the usual deploy story; GraalVM native image is opt-in ([Module 49](../49-cloud-and-containerization/03-graalvm-and-native-image-concepts.md)).
5. **Ignoring the JMM because channels “felt ordered.”** Shared mutable Java objects need HB (locks, `volatile`, concurrent collections).
6. **Treating `nil`-interface intuition as `null` safety.** Java `null` and Go’s nil interface/pointer traps differ; learn Java’s NPE patterns separately ([Module 18](../18-optional-and-null-handling/README.md)).

---

## Null, zero values, and APIs

Go’s zero values and `nil` interfaces create failure modes that do not map 1:1 onto Java `null`. A non-nil interface value can still hold a typed nil pointer—an infamous Go footgun. Java’s hazard is usually a plain `null` reference and `NullPointerException`, plus Optional misuse at API edges ([Module 18](../18-optional-and-null-handling/README.md)).

**Transfer tip:** design Java APIs with explicit absence (`Optional` as a *return* type, clear domain types) rather than inventing Go-shaped sentinel zeros on reference types.

## Performance mental model

- **Java / HotSpot:** Warm-up, tiered JIT, escape analysis, GC pauses/throughput trade-offs ([Modules 27–28](../27-garbage-collection/README.md)). Peak throughput after warm-up can be excellent; startup and footprint need attention (containers, GraalVM native image awareness in [Module 49](../49-cloud-and-containerization/03-graalvm-and-native-image-concepts.md)).
- **Go:** Fast startup, simpler performance story for many CLIs and network services; GC exists but the operational knobs differ.

| Question | Prefer asking |
|----------|----------------|
| Cold start / CLI | Go often wins on simplicity; Java needs CDS/AppCDS, smaller heaps, or native image awareness |
| Steady-state service | Measure both; HotSpot after warm-up is competitive on many backends |
| Huge blocking concurrency | Java 21 virtual threads vs goroutines—pick by team and ecosystem, not slogans |
| Memory ceiling | Both GC; tune and measure; do not assume Go is always smaller |

Do not claim one is universally faster. Measure the workload you care about.

## Ecosystem and HTTP services (awareness)

Go’s standard library `net/http` is often enough for small services. Java backends typically lean on **Spring Boot**, Jakarta Servlet containers, or similar—more moving parts, deeper ops/integration story ([Module 37](../37-spring-boot/README.md)). That is not a moral judgment; it changes what “hello production” means (config, metrics, DI, transactions).

If you know Go middleware chains, map them mentally to servlet filters / Spring interceptors—but learn the Java lifecycle and proxy rules separately.

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

**Study order for Go → Java transfers:** (1) types + erasure, (2) exceptions + try-with-resources, (3) platform vs virtual threads, (4) JMM / happens-before, (5) Maven or Gradle + classpath. Skip none of those five even if Go felt “simple.”

Keep a private cheat sheet of *false friends* (goroutine ≠ platform thread, `error` ≠ checked exception syntax, `go.mod` ≠ JPMS) and revise it after each module.

---

## See also

- Virtual threads: [24/05](../24-concurrency-and-multithreading/05-virtual-threads-and-structured-concurrency.md)
- Exceptions: [14/01](../14-exceptions-and-error-handling/01-throwable-hierarchy.md)
- Build tooling: [29/README](../29-build-tools-and-dependency-management/README.md)
- Interview concurrency: [52/02](../52-interview-preparation/02-jvm-and-concurrency-interviews.md)
