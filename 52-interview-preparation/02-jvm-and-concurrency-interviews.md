# JVM and Concurrency Interviews

> **Status:** Completed  
> **Module:** 52 — Interview Preparation  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3

## 1. Learning Objectives

- Explain **JVM structure**, **class loading**, and **GC** at interview depth.
- Answer **JMM** questions: happens-before, `volatile`, `synchronized`.
- Compare **thread pools**, **virtual threads** (Java 21), and **concurrency utilities**.
- Diagnose **deadlock**, **livelock**, and **visibility** bugs verbally.

## 2. Prerequisites

- **Modules 24–28** — concurrency, JMM, GC, JIT.
- **Module 26** — JVM internals overview.

## 3. Why This Concept Exists

Backend interviews probe **production failures**: memory leaks, GC pauses, races, pool exhaustion—not just syntax.

## 4. Conceptual Explanation

### JVM topics

| Topic | Answer skeleton |
|-------|-----------------|
| Heap vs stack | Objects heap; frames hold local refs (M26) |
| Class loading | load, link, initialize; parent delegation |
| GC | generational collection; G1/ZGC names (M27) |
| OOM types | heap, metaspace, direct buffer |

**Q: What triggers full GC?**  
Collector-specific; avoid universal folklore without naming collector.

### JMM essentials

- **happens-before** establishes visibility ordering (JLS).
- **volatile** — visibility/order; not atomic `i++`.
- **synchronized** — mutual exclusion + release/acquire on monitor.

### Utilities

| Type | Use |
|------|-----|
| `ExecutorService` | Bounded task submission |
| `CountDownLatch` | One-shot wait |
| `Semaphore` | Limit concurrency |
| `ConcurrentHashMap` | Concurrent map |

### Java 21 virtual threads

Lightweight; cheap blocking on I/O. **Pinning** when carrier blocked on `synchronized` or native—know headline; verify current JDK release notes for your interview date.

## 5. Mechanism and Execution Flow

### happens-before example (interview)

```mermaid
flowchart LR
  W[write x=1 in thread A] --> U[unlock monitor]
  U --> L[lock same monitor in B]
  L --> R[read x in B sees 1]
```

Without such an edge, **no visibility guarantee**.

### Virtual thread mount (conceptual)

1. Virtual thread starts on carrier platform thread.
2. Blocking I/O parks virtual thread; carrier may run other virtual threads.
3. Pinning prevents unmount during `synchronized` block on some JDK builds.

## 6. Scenario Questions

**Q: Two threads increment static `int`?**  
Race unless `AtomicInteger` or synchronization.

**Q: Detect deadlock?**  
Thread dumps (`jcmd`/`jstack`), JFR; prevention via lock ordering.

## 7. Links to demos

- [happens-before-demo](../../examples/jvm/happens-before-demo/)
- [executors-futures-demo](../../examples/core-java/executors-futures-demo/)
- [virtual-threads-demo](../../examples/core-java/virtual-threads-demo/)

Run on Temurin 21 when practicing.

## 8. Common Mistakes

- “All GC stops the world” without low-pause collector nuance.
- `Thread.sleep` as synchronization design.

## 9. Revision and Mastery

- happens-before for broken double-checked locking without `volatile`.
- Three `ThreadPoolExecutor` rejection policies.

## 10. Exercises

1. Explain Module 25 thesis in 90 seconds.
2. Predict happens-before demo output before running.

## 11. Chapter Summary

JVM and concurrency interviews need JMM precision, collector vocabulary, and pool/virtual-thread awareness from Modules 24–28.

## Next Steps

[03-spring-and-system-design-interviews.md](03-spring-and-system-design-interviews.md)

## Review Checklist

- [x] Technical L2–3; happens-before mechanism; JVM + JMM preserved
