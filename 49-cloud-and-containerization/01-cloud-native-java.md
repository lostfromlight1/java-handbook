# Cloud-Native Java

> **Status:** Completed  
> **Module:** 49 — Cloud and Containerization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Define **cloud-native** properties: elasticity, resilience, observability, automation.
- Map handbook skills (HTTP, JDBC, Spring, testing) to **12-factor** style services.
- Choose **managed services** (DB, cache, queue) vs self-hosted for JVM workloads.
- Configure **externalized config** and **stateless processes** for horizontal scale.

## 2. Prerequisites

- **Modules 37–40** — Spring Boot, REST, architecture patterns.
- **Module 48** — containers, CI/CD, deployment strategies.

## 3. Why This Concept Exists

Running Java in the cloud is not “lift-and-shift VM + one giant WebLogic.” **Cloud-native** design uses **small deployable units**, **declared infrastructure**, and **platform services** so teams scale individual services, recover from zone failures, and ship frequently.

## 4. Conceptual Explanation

### Cloud-native pillars (for Java backends)

| Pillar | Java manifestation |
|--------|---------------------|
| **Microservices / modular deployables** | Spring Boot JARs, Module 40 boundaries |
| **Containers & orchestration** | Module 48–49 Kubernetes |
| **DevOps automation** | CI/CD pipelines, GitOps |
| **Observability** | Logs, metrics, traces (Module 47) |
| **Resilience** | Timeouts, retries, circuit breakers (Module 32) |

### 12-factor highlights

1. **Codebase** — one repo per service (usually).
2. **Dependencies** — Maven/Gradle lock versions; no hidden JARs on server.
3. **Config** — env vars, ConfigMaps (not baked into WAR).
4. **Backing services** — PostgreSQL as attachable resource URL.
5. **Build/release/run** — immutable release artifact (container digest).
6. **Processes** — **stateless** JVM; session in Redis if needed.
7. **Port binding** — embedded Tomcat/Netty (Spring Boot default).
8. **Concurrency** — scale out processes, not one giant JVM (Module 24 thread pools per instance).
9. **Disposability** — fast startup; graceful SIGTERM (Module 48).
10. **Dev/prod parity** — same JDK 21 image class path.
11. **Logs** — stdout aggregated by platform (not log4j files on disk only).
12. **Admin processes** — one-off migration jobs as separate K8s Job.

### Managed vs self-managed

| Managed (RDS, Cloud SQL) | Self-managed DB in cluster |
|--------------------------|----------------------------|
| Ops offload, backups | Control, cost at scale |

Java apps connect via JDBC URL from env (Module 33–35); rotate credentials via secrets manager.

### Configuration patterns

```java
// Spring Boot reads SPRING_DATASOURCE_URL from env (Module 37)
// @Value and @ConfigurationProperties bind typed config
```

**Never** commit secrets; use platform secret mounts.

## 5. Real-World Use Cases

- E-commerce checkout service scales on CPU during sales; cart service uses Redis.
- Event-driven order fulfillment publishes to SQS/Kafka; consumer is another Spring Boot app.

## 6. Edge Cases

- **Cold start** of JVM on serverless (Lambda, Cloud Run) — GraalVM native (Ch.03) reduces latency; plain JVM pays warmup tax.
- **Clock skew** — use UTC; NTP assumed on nodes.
- **Multi-region** — JDBC read replicas, sticky writes, Module 34 transaction boundaries.

## 7. Common Mistakes

### Incorrect — local absolute paths in config

`/var/app/data` breaks in cloud.

### Corrected — mount volumes or use object storage SDK.

### Incorrect — treating cloud as infinite CPU

No rate limits, connection pool exhaustion, GC pressure under load.

### Corrected — load test; pool sizing; horizontal scale.

## 8. Design Trade-offs

| Modular monolith (Module 40) | Microservices |
|------------------------------|---------------|
| Simpler deploy | Independent scale |

| Always microservice split | Split when boundaries clear |
|-----------------------------|----------------------------|

## 9. Revision and Mastery

- Name three 12-factor items especially relevant to JDBC/Spring.
- Why should JVM processes be stateless at the HTTP layer?
- What is an immutable release in CD terms?

## 10. Exercises

1. List env vars for a Boot app (DB URL, profile, JVM options).
2. Compare lift-and-shift vs cloud-native for a single WAR.

## 11. Chapter Summary

Cloud-native Java emphasizes stateless Spring Boot (or similar) services in containers, externalized config, managed backing services, and automation from Module 48— not monolithic VMs with manual releases.

## Next Steps

[02-kubernetes-basics-for-java.md](02-kubernetes-basics-for-java.md)

## Review Checklist

- [x] Cloud-native + 12-factor; managed services; mistakes
