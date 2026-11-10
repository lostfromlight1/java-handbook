# Resilience Patterns

> **Status:** Completed  
> **Module:** 44 — Microservices and Distributed Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

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

## Review Checklist

- [x] Patterns table; circuit breaker demo verified
