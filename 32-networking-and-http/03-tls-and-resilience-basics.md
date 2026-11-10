# TLS and Resilience Basics

> **Status:** Completed  
> **Module:** 32 — Networking and HTTP  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** TLS via https://example.com in http-client-demo; resilience patterns conceptual

## 1. Learning Objectives

- Explain **TLS** handshake at a high level (certificates, trust, encryption).
- List **resilience** tactics: timeouts, retries, circuit breakers, bulkheads.
- Connect JVM **trust store** to HTTPS failures.

## 2. Prerequisites

- Ch.02 — HttpClient timeouts.
- Module **14** — exception types for I/O failures.

## 3. Why This Concept Exists

Plain TCP sends readable bytes. **TLS** (often branded SSL) encrypts and authenticates endpoints. Production outages also come from **cascading failures** when callers hammer failing dependencies. Resilience patterns limit blast radius.

## 4. Conceptual Explanation

**TLS (simplified):**

1. Client connects and requests secure channel.
2. Server presents **certificate chain** signed by trusted CA.
3. Client verifies chain against **trust store** (`cacerts` in JDK).
4. Session keys encrypt application data (HTTP inside TLS = HTTPS).

Failures: expired cert, hostname mismatch, corporate MITM proxy without imported CA → `SSLHandshakeException`.

**Resilience patterns:**

| Pattern | Purpose |
|---------|---------|
| Timeout | Stop waiting forever |
| Retry with backoff | Transient network blips (idempotent ops only) |
| Circuit breaker | Fail fast when dependency unhealthy |
| Bulkhead | Limit concurrent calls to one dependency |
| Rate limit | Protect service from overload |

Implementations: Resilience4j, Failsafe libraries; Spring Cloud CircuitBreaker in Module **36+**. Core Java: compose with `HttpClient`, `ScheduledExecutorService`, and state machines.

## 5. Step-by-Step Examples

The [http-client-demo](../../examples/networking/http-client-demo/) uses **HTTPS** successfully — JDK trusts the public CA for `example.com`. That is **real TLS** termination at the server/CDN, not a stub.

**Retry sketch** (not a separate demo — illustrates pattern):

```java
for (int attempt = 1; attempt <= 3; attempt++) {
    try {
        var response = client.send(request, BodyHandlers.ofString());
        if (response.statusCode() < 500) {
            return response;
        }
    } catch (IOException e) {
        if (attempt == 3) throw e;
    }
    Thread.sleep(100L * attempt);
}
```

Only retry **idempotent** GET/HEAD or operations safe to repeat.

## 6. Real-World Use Cases

- mTLS between services in zero-trust networks.
- Exponential backoff for payment provider HTTP 503.
- Shedding load when DB pool saturated (Module **33**).

## 7. Edge Cases

- Retrying **POST** orders may double-charge — use idempotency keys.
- **Half-open** circuit flapping — tune thresholds.
- JVM trust store updates lag new public CAs — patch JDK or import certs.

## 8. Best Practices

- Always **time-bound** remote calls.
- Monitor **error budget** and breaker state.
- Test TLS with **staging certs** before prod expiry.

## 9. Chapter Summary

TLS secures HttpClient traffic via JDK trust; resilience patterns complement timeouts demonstrated in Ch.02.

## 10. Next Steps

Module **33** — Databases and JDBC.

## Review Checklist

- [x] HTTPS demo proves TLS path; retry snippet is educational only
