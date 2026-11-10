# Logging, Metrics, and Tracing

> **Status:** Completed  
> **Module:** 47 — Observability and Monitoring  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Apply the **three pillars**: logs, metrics, traces.
- Run [StructuredLoggingDemo](../../examples/observability/structured-logging-demo/).
- Use **correlation ids** across HTTP and messaging (Modules 32, 45).
- Choose log levels and **avoid secret leakage** (Module 43).

## 2. Prerequisites

- Module 31 — diagnostics mindset.
- Module 44 — distributed calls.

## 3. Why This Concept Exists

Production failures require answering: **what happened**, **how often**, **which request path**. Unstructured println output does not scale across pods and time zones.

## 4. Three pillars

| Pillar | Question | Examples |
|--------|----------|----------|
| **Logs** | What event occurred? | Error with stack, audit trail |
| **Metrics** | How much / how fast? | RPS, p99 latency, pool usage |
| **Traces** | Which services in one request? | Span tree across calls |

Use all three: metrics alert; logs explain; traces localize latency.

## 5. Structured logging

**Structured logging** emits machine-parseable fields (JSON or key=value):

Demo output includes `event=order.placed`, `orderId=ord-42`, `level=INFO`.

Benefits: filter in ELK/Loki; correlate with `traceId`.

### Incorrect

```java
System.out.println("Order placed " + orderId);
```

### Correct

```java
log.info("order.placed orderId={} userId={}", orderId, userId);
```

Or map-based demo in `StructuredLoggingDemo`.

**JDK:** `java.util.logging`, **SLF4J** façade + Logback/Log4j2 in Spring apps.

## 6. Correlation and context

- **`traceId`:** one user request across services (W3C `traceparent` header).
- **`spanId`:** sub-operation within service.
- **`correlationId`:** business-friendly id (order id) in addition.

Propagate on `HttpClient` and message headers (Module 45).

## 7. Metrics types

| Type | Use |
|------|-----|
| **Counter** | Monotonic (errors total) |
| **Gauge** | Point-in-time (heap used) |
| **Histogram / Summary** | Latency distribution |

**Micrometer** (Spring Boot Actuator) abstracts Prometheus, Datadog, etc.

Expose `/actuator/prometheus` when using Prometheus stack (Ch.02).

## 8. Distributed tracing (conceptual)

Each inbound HTTP call creates a **span**; outbound calls become child spans. **OpenTelemetry** (Ch.02) standardizes API and export.

Without a collector installed, design for propagation headers and test with logging of ids locally.

## 9. Log levels

| Level | Use |
|-------|-----|
| **ERROR** | Action needed; user impact |
| **WARN** | Degraded, retry succeeded |
| **INFO** | Business milestones, startup |
| **DEBUG** | Troubleshooting; off in prod default |

Never **DEBUG** full payloads with PII in production.

## 10. Real-World Use Cases

- Checkout incident: metric spike 503 → trace shows payment client timeout.
- Audit: structured `security.login.failure` with `principal` hash only.
- Capacity: gauge of Hikari pending threads before pool exhaustion.

## 11. Edge Cases

- **High cardinality labels:** `userId` on every metric explodes cost—use logs/traces for ids.
- **Async logging:** buffer loss on crash—balance durability vs latency.

## 12. Common Mistakes

| Mistake | Fix |
|---------|-----|
| Logging secrets | Redact filters |
| Only average latency | Histogram percentiles |
| Missing trace propagation | Filter/interceptor inject headers |

## 13. Revision and Mastery

- When metric vs log?
- Fields in demo line for order placed?
- cardinality example to avoid?

## 14. Exercises

1. Add `traceId` field to structured logger helper.
2. List three Micrometer metrics for REST service.
3. Define log policy for payment errors.

## 15. Chapter Summary

Observability combines structured logs, metrics with percentiles, and distributed traces linked by correlation ids—demo proves key=value logging without external stacks.

## 16. Next Steps

[02-opentelemetry-and-prometheus.md](02-opentelemetry-and-prometheus.md)

## Review Checklist

- [x] Three pillars; structured demo verified Temurin 21.0.12.1
