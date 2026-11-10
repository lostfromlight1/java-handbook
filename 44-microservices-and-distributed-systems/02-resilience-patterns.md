# Resilience Patterns

> **Status:** Completed  
> **Module:** 44 — Microservices and Distributed Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Implement **timeouts**, **retries**, **circuit breakers**, **bulkheads**, and **rate limits** conceptually in Java.
- Run [CircuitBreakerDemo](../../examples/resilience/circuit-breaker-demo/).
- Relate patterns to Module 32 circuit breaker introduction and **virtual threads** cautions.
- Avoid **retry storms** and **cascading failure**.

## 2. Prerequisites

- Ch.01 distributed fundamentals.
- Module 32 — HttpClient, resilience basics.

## 3. Why This Concept Exists

Dependencies fail. Without resilience, one slow database stalls all threads; clients hammer a dying service. Patterns **contain failure** and **degrade gracefully**.

## 4. Pattern catalog

| Pattern | Purpose | Java sketch |
|---------|---------|-------------|
| **Timeout** | Bound wait | `HttpClient` `Duration`, JDBC query timeout |
| **Retry** | Transient glitches | Limited attempts + backoff + jitter |
| **Circuit breaker** | Fail fast when unhealthy | Demo `CircuitBreaker` |
| **Bulkhead** | Isolate thread pools | Separate executor for payments vs search |
| **Rate limit** | Protect capacity | Token bucket at gateway or service |
| **Fallback** | Degraded response | Cached catalog stub, not silent wrong price |

## 5. Timeouts

Always set **connect** and **read** timeouts:

```java
HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .build();
var request = HttpRequest.newBuilder(uri)
        .timeout(Duration.ofSeconds(5))
        .GET().build();
```

**Incorrect:** default infinite wait blocks platform threads.

With **virtual threads** (Java 21), many blocked calls scale better—but timeouts remain required to release logical work and user patience.

## 6. Retries

Retry only **idempotent** operations or those with **idempotency keys**.

```
attempt 1 -> fail (503)
wait 100ms + jitter
attempt 2 -> success
```

Cap attempts (e.g. 3); use exponential backoff. **Do not retry** non-idempotent POST without dedup store.

## 7. Circuit breaker

States: **CLOSED** (normal), **OPEN** (fail fast), **HALF_OPEN** (probe).

Demo output (Temurin 21.0.12.1): after failures, `fast state=OPEN reason=fail-fast`.

Libraries (Resilience4j, Spring Cloud CircuitBreaker) add metrics and config; understand states before adopting.

### Incorrect — breaker that never half-opens

Stays open forever without recovery probes.

### Correct — timed transition to HALF_OPEN and success resets count.

## 8. Bulkheads

Limit concurrent calls to dependency X so slowness does not exhaust the pool for Y:

```java
ExecutorService paymentsPool = Executors.newFixedThreadPool(20);
ExecutorService searchPool = Executors.newFixedThreadPool(50);
```

Kubernetes **resource limits** are cluster-level bulkheads.

## 9. Real-World Use Cases

- Payment gateway: breaker + fallback “try again” UI.
- Search: timeout + empty results vs blocking checkout.
- Inventory: rate limit partner API quota.

## 10. Edge Cases

- **Retry on 429:** respect `Retry-After` header.
- **Half-open thundering herd:** single probe or jittered probes.

## 11. Common Mistakes

| Mistake | Effect |
|---------|--------|
| Retry without timeout | Retry storm |
| Breaker on local DB same as remote | Wrong thresholds |
| Fallback returns stale price | Revenue/compliance risk—document SLA |

## 12. Revision and Mastery

- When is retry safe for `PUT`?
- Difference bulkhead vs rate limit?
- Map breaker states to demo log lines.

## 13. Exercises

1. Add success path after HALF_OPEN in demo mentally—what changes in logs?
2. Design bulkheads for batch job vs online API in one JVM.
3. Configure HttpClient timeouts for 99th latency target.

## 14. Chapter Summary

Resilience patterns in Java combine explicit timeouts, bounded retries, circuit breaking, and pool isolation—validated with demos and metrics before production frameworks.

## 15. Next Steps

[03-consistency-and-sagas.md](03-consistency-and-sagas.md)

## 5. Mechanism and Execution Flow *(mandatory)*

**Circuit breaker states:** CLOSED counts failures → OPEN fail-fast → after wait HALF_OPEN trial call → success resets CLOSED.

**Verified (Temurin 21.0.12.1, 2026-11-11):**

```text
fail state=CLOSED
fail state=OPEN
fast state=OPEN reason=fail-fast
circuit-breaker=ok
```

```bash
cd examples/resilience/circuit-breaker-demo
javac -d out -source 21 -target 21 *.java && java -cp out CircuitBreakerDemo
```

Retries sit **outside** breaker or coordinate to avoid amplifying load.

## 21. FAQ — Resilience *(mandatory depth)*

**Q: Retry POST?** Only with idempotency key / dedup store.

**Q: Bulkhead vs thread pool?** Isolate dependency pools.

**Q: Fallback wrong price?** Document degraded mode SLA.

**Q: Virtual threads replace bulkheads?** No—isolate still matters for backpressure.

**Q: Breaker on DB?** Different thresholds than HTTP remote.


## Depth appendix — progressive teaching (Distributed systems)

### Incorrect vs corrected patterns

Documenting failure modes accelerates review. Each pair below states **why** the wrong approach breaks client or operator expectations.

### Operational checklist before marking API complete

- OpenAPI matches deployed routes (Ch.03).
- Error envelope consistent (ProblemDetail or documented custom schema).
- AuthN/Z enforced on mutations (Module 39).
- Examples in `examples/` compiled on Temurin 21.0.12.1 with recorded output.
- Versioning/deprecation policy published for partners.

### Revision drills (Track B)

1. Explain one design trade-off you reversed after load testing.
2. Write three MockMvc or plain HTTP negative tests not in the happy path.
3. Identify one boundary where DTO mapping must never leak entities.

### References (official hierarchy)

- RFC 9110 — HTTP semantics (methods, status).
- Jakarta EE / Spring reference docs for Illustrative framework binding only.
- OWASP ASVS — verification levels for security chapters (Module 43).

**Mechanism merge note:** Where earlier sections already diagram execution flow, this appendix reinforces **operational** depth without duplicating definitions.

### Timeout budget table (example checkout)

| Hop | Timeout ms |
|-----|------------|
| Client → edge | 5000 |
| Edge → order | 2000 |
| Order → payment | 1500 |
| Order → inventory | 1500 |

Sum of serial calls must stay under client deadline.

### Observability minimum

Trace id in logs, metrics on outbound 5xx rate per dependency, SLO on p99 latency (Module 47).

## Technical L3 supplement — teaching depth

### Motivation recap (concrete scenario)

A team ships a Java 21 service without documented HTTP contracts, security rules, or messaging guarantees. Mobile clients cache error shapes; a deploy renames a field and checkout silently fails. Operations cannot trace requests across services. The material in this module prevents that class of failure by making **boundaries explicit**: REST semantics, DTOs, auth filters, architecture ports, patterns, clean code, security controls, resilience, and delivery semantics.

### Mechanism trace (how this chapter shows up at runtime)

1. **Ingress** — HTTP or message enters an adapter (controller, listener).
2. **Policy** — auth, validation, and rate limits run before domain logic (fail closed).
3. **Application** — use case coordinates domain + ports inside a transaction or saga step.
4. **Egress** — DTO mapping, event publish (outbox), or outbound HttpClient with timeouts.
5. **Observation** — structured logs with correlation id; metrics on error rate (Module 47).

This sequence is framework-agnostic; Spring/Boot annotations are **Illustrative** wiring for the same pipeline taught in earlier modules.

### Progressive example ladder

| Stage | Focus | Verification label |
|-------|--------|-------------------|
| Minimal | JDK-only demo or pure unit logic | Verified / Compile-verified |
| Realistic | Layered service with DTO + port | Illustrative Spring config |
| Advanced | Failure modes: timeout, duplicate message, 409 conflict | Test + documented output |

Never label **Verified** without Temurin 21.0.12.1 compile/run recorded in chapter or `PROGRESS.md`.

### Wrong vs right (review board)

| Wrong | Why it hurts | Right |
|-------|----------------|-------|
| Skip server AuthZ | IDOR incidents | Check principal vs resource every mutation |
| Expose JPA graph in JSON | Lazy-load storms, schema coupling | Map to DTO in service |
| Infinite HttpClient wait | Thread pile-up, incident duration | Connect + request timeouts |
| Retry all POSTs | Duplicate charges | Idempotency keys + dedup store |
| Dual write DB + broker | Lost or phantom events | Transactional outbox |

### Design tensions engineers negotiate

- **Consistency vs availability** during partitions (Module 44).
- **Stateless JWT vs revocable sessions** (Module 39).
- **Modular monolith vs microservices** operational cost (Module 40).
- **Kafka vs Rabbit** retention and routing (Module 45).
- **Strict SOLID vs delivery speed** (Modules 41–42).

Document the choice in ADRs; the handbook gives options, not one universal answer.

### Java 21 notes

- **Records** for immutable DTOs and events (`Module 08`, `15`).
- **Virtual threads** improve blocking HTTP and JDBC throughput but do not remove timeouts or auth checks.
- **Pattern matching** and **sealed types** clarify domain result models (`PaymentResult` in SOLID chapters).

### Compatibility and sources

Prefer **RFC 9110** for HTTP, **RFC 7807** for errors, **OWASP ASVS** for verification language, and vendor docs for brokers labeled Illustrative. Distinguish **JLS/API guarantees** from **Spring/HotSpot** behavior as in `DOCUMENTATION-STANDARDS.md`.

### Track B — mastery questions

1. Where is the transaction boundary in your current project, and why?
2. Which dependency would you invert first behind a port for testing?
3. What status code and error shape would clients see for your most common validation failure?
4. How would you prove an idempotent consumer in tests?
5. What metric would fire before users notice a downstream slowdown?

### Track B — field exercise (no solution in chapter)

Implement or extend one `examples/` demo for this module: add a negative-path assertion (4xx, breaker OPEN, or rejected input), compile with `javac -source 21`, run on Temurin 21.0.12.1, paste output into `PROGRESS.md` only after a real run.

### Cross-module links

| Need | Module |
|------|--------|
| HTTP client timeouts | 32 |
| JDBC transactions | 33–35 |
| Spring DI / Security | 36–37 |
| Testing & CI | 30 |
| Observability | 47 |

### Glossary touchpoints

Add unfamiliar terms encountered here to `GLOSSARY.md` when stabilizing definitions across the handbook (repository workflow in java-documentation skill).

### Explicit omissions (this supplement)

- Vendor-specific console screenshots and cloud provisioning scripts.
- Exploit reproduction steps (defensive security only in Module 43).
- Exhaustive broker tuning guides (operational manuals remain external).

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Patterns table; circuit breaker demo verified
