# Load Testing and Capacity

> **Status:** Completed  
> **Module:** 46 — Caching and Performance Engineering  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Plan **load**, **stress**, and **soak** tests with clear goals.
- Interpret **latency percentiles**, throughput, and error rates.
- Connect results to **JVM**, **thread pools**, **DB pool**, and **GC** (Modules 26–28).
- Avoid **mistaking load generator limits** for system limits.

## 2. Prerequisites

- Module 30 testing mindset.
- Module 33 connection pooling.
- Chapters 01–02 caching.

## 3. Why This Concept Exists

Production traffic exposes bottlenecks that unit tests never see. Load testing **before** launch validates SLOs and capacity plans (Module 47).

## 4. Test types

| Type | Goal | Duration |
|------|------|----------|
| **Load** | Expected peak traffic | Minutes |
| **Stress** | Find breaking point | Ramp until failure |
| **Soak** | Leaks, drift | Hours–days |
| **Spike** | Sudden burst | Short ramp |

## 5. Metrics that matter

- **Throughput:** requests/sec successful.
- **Latency:** p50, p95, p99—not average alone.
- **Error rate:** 5xx, timeouts.
- **Saturation:** CPU, pool wait time, GC pause (Module 26).

```text
SLO example: 99% of checkout POST < 800ms at 500 RPS with <0.1% errors
```

## 6. Tooling (awareness)

- **JMeter**, **Gatling**, **k6** — script scenarios; distributed workers for high RPS.
- **JMH** (Module 28 awareness) — microbenchmarks, not replacement for system load tests.

Do not fabricate benchmark numbers in docs; record environment when you run tests.

## 7. Test environment fidelity

| Aspect | Risk if wrong |
|--------|----------------|
| Smaller DB than prod | False confidence |
| No replica latency | Misleading read paths |
| Mocked payment | Ignores real timeouts |

Use **scaled-down but proportional** stacks or cost-approved prod-like staging.

## 8. Java service knobs under load

- **Tomcat/virtual threads:** max connections vs DB pool size (pool must not exceed DB capacity).
- **HikariCP** `maximumPoolSize`—each waiting thread is latency.
- **GC:** G1/ZGC pause targets; heap too small → allocation stalls.

## 9. Caching under load

Measure **hit rate** during test. Cold cache at start mimics deploy—run warm-up phase before recording.

## 10. Real-World Use Cases

- Black Friday rehearsal with 3× traffic.
- Soak test finds `ThreadLocal` leak in custom pool.
- Stress test sets autoscaling policy max instances.

## 11. Edge Cases

- **Coordinated omission:** client drops slow responses—use tools reporting corrected latency.
- **Thundering herd** after deploy cold cache—pre-warm or gradual traffic shift.

## 12. Common Mistakes

| Mistake | Fix |
|---------|-----|
| Test laptop as generator only | Separate load driver fleet |
| Ignore think time | Model realistic pacing |
| Single metric focus | CPU low but pool exhausted |

## 13. Revision and Mastery

- Load vs soak?
- Why p99 for checkout SLO?
- Pool size vs threads relationship?

## 14. Exercises

1. Write load test scenario for `GET /products/{id}` with cache.
2. Capacity worksheet: RPS, p95, instances, pool size.
3. List warmup steps before measurement window.

## 15. Chapter Summary

Load testing validates Java services against percentile latency and error SLOs with realistic drivers and environment parity—interpreting pools, GC, and cache behavior together.

## Review Checklist

- [x] Test types; percentiles; no fabricated benchmark results
