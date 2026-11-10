# GraalVM and Native Image Concepts

> **Status:** Completed  
> **Module:** 49 — Cloud and Containerization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Contrast **HotSpot JIT** with **ahead-of-time (AOT)** native images.
- List **reachability**, **reflection**, and **classpath** challenges for Spring/native.
- Know when **native image** helps (cold start, memory) vs when **JVM mode** remains better (peak throughput, dynamic features).
- Connect native constraints to **Module 50** reflection and agents.

## 2. Prerequisites

- **Module 26** — JIT compilation overview.
- **Module 36–37** — Spring DI, proxies, reflection-heavy frameworks.
- **Module 50 Ch.01** — reflection and annotations (for native metadata).

## 3. Why This Concept Exists

Serverless and dense Kubernetes charge for **memory** and **startup time**. A conventional JVM warms up JIT and uses more RSS. **GraalVM Native Image** compiles a closed-world subset of the app to a **native binary** with faster startup and smaller footprint—at the cost of build complexity and dynamic-language restrictions.

## 4. Conceptual Explanation

### Execution models

| Mode | Startup | Peak throughput | Dynamic reflection |
|------|---------|-----------------|---------------------|
| **HotSpot** (Temurin 21) | Slower warm | Excellent after warmup | Full |
| **Native Image** | Fast cold | Good; differs by workload | Must be declared at build time |

### Closed-world assumption

Native Image **analyzes** reachability at build time. Code paths not reachable may be stripped. **Reflection**, **JNI**, **classpath resources**, **dynamic proxies**, and **serialization** need **metadata** (`reflect-config.json`, `resource-config.json`, Spring AOT processing in Boot 3).

### Spring Boot 3 + native (conceptual)

```bash
./mvnw -Pnative native:compile
```

Build uses **Spring AOT** to generate bean definitions without full classpath scan at runtime. Still requires testing every integration (JPA, WebClient, etc.).

### When to choose native

| Good fit | Poor fit |
|----------|----------|
| CLI tools, small APIs, functions | Heavy JIT workloads, lots of dynamic codegen |
| Strict memory limits | Extensive reflection without config |
| Fast scale-from-zero | Java agents, deep JVMTI (Module 50) |

### GraalVM vs Temurin

**Temurin** — reference OpenJDK HotSpot for handbook primary path. **GraalVM** distribution provides `native-image` tool and optional same JDK bits. Production teams often use **GraalVM CE** or vendor builds for native; runtime JVM services may stay on Temurin.

## 5. Internal Behavior (labeled)

Native Image includes **Substrate VM** (not HotSpot). No traditional JIT; some JIT-like optimizations at build time. **GC** (e.g. G1-like serializers) differs—profile memory under load.

## 6. Edge Cases

- **Class.forName** in libraries without reachability config → `ClassNotFoundException` at runtime.
- **CGLIB** proxies vs JDK proxies — Spring native prefers different code paths.
- **Preview features** (FFM on 21) — native support lags; verify release notes.

## 7. Common Mistakes

### Incorrect — native build once, never integration-test

Passes smoke test; fails on first reflective Hibernate path.

### Corrected — full regression suite on native binary in CI.

### Incorrect — expecting identical JVM tuning flags

Many `-XX:*` HotSpot flags do not apply.

### Corrected — native-specific docs and monitoring.

## 8. Design Trade-offs

| JVM container | Native binary |
|---------------|---------------|
| Familiar ops | Smaller image, faster start |
| Agents/FFM easier | Build minutes, metadata maintenance |

## 9. Revision and Mastery

- Why does reflection need configuration in native image?
- Name two workloads better on HotSpot.
- How does Spring AOT relate to closed-world assumption?

## 10. Exercises

1. List handbook demos that would need reflection config (proxy demo, JPA).
2. Compare startup goals for API vs batch job.

## 11. Chapter Summary

GraalVM Native Image trades dynamic flexibility for startup and size; Java 21 services usually stay on HotSpot unless requirements drive native. Plan reflection metadata (Module 50) and Spring AOT when adopting.

## Review Checklist

- [x] HotSpot vs AOT; closed world; Spring; when to use
