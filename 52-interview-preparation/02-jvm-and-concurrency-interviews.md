# JVM and Concurrency Interviews

> **Status:** Completed  
> **Module:** 52 — Interview Preparation  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Explain **JVM structure**, **class loading**, and **GC** at interview depth.
- Answer **JMM** questions: happens-before, `volatile`, `synchronized`.
- Compare **thread pools**, **virtual threads** (Java 21), and **common concurrency utilities**.
- Diagnose **deadlock**, **livelock**, and **visibility** bugs verbally.

## 2. Prerequisites

- **Modules 24–28** — concurrency, JMM, GC, JIT.
- **Module 26** — JVM internals overview.

## 3. Why This Concept Exists

Backend interviews probe whether candidates can reason about **production failures**: memory leaks, GC pauses, race conditions, and thread pool exhaustion—not just syntax.

## 4. JVM Topics

| Topic | Answer skeleton |
|-------|-----------------|
| Heap vs stack | Objects heap; frames local refs (M26) |
| Class loading | load/link/initialize; parent delegation |
| GC | mark-sweep-compact generational; G1/ZGC names |
| OOM types | heap, metaspace, direct buffer |

**Q: What triggers full GC?**  
Discuss collector-specific behavior; avoid folklore “System.gc always full GC” without caveats.

## 5. Concurrency Topics

### JMM essentials

- **happens-before** establishes visibility ordering.
- **volatile** — visibility + ordered access; not atomic `i++`.
- **synchronized** — mutual exclusion + release/acquire on same monitor.

### Utilities

| Type | Use |
|------|-----|
| `ExecutorService` | Bounded task submission |
| `CountDownLatch` | One-shot wait |
| `Semaphore` | Limit concurrency |
| `ConcurrentHashMap` | Concurrent map |

### Java 21 virtual threads

- Lightweight; block cheaply on I/O.
- Pinning caveat when synchronized/native in carrier thread—know headline, cite current JDK docs for details.

## 6. Scenario Questions

**Q: Two threads increment static `int` — result?**  
Race unless `AtomicInteger` or synchronization; explain lost updates.

**Q: How detect deadlock?**  
Thread dumps (`jstack`), JFR, visualizers; prevention: lock ordering.

## 7. Links to demos

- [happens-before-demo](../../examples/jvm/happens-before-demo/)
- [executors-futures-demo](../../examples/core-java/executors-futures-demo/)
- [virtual-threads-demo](../../examples/core-java/virtual-threads-demo/)

## 8. Common Mistakes

- Calling all GC “stop the world” without noting low-pause collectors.
- Using `Thread.sleep` for synchronization in designs (interview red flag).

## 9. Revision and Mastery

- Draw happens-before edge list for double-checked locking (and why broken without volatile).
- Name three thread pool rejection policies.

## 10. Exercises

1. Explain Module 25 chapter thesis in 90 seconds.
2. Predict output of a happens-before demo without running.

## 11. Chapter Summary

JVM and concurrency interviews need JMM precision, collector vocabulary, and practical pool/virtual-thread awareness grounded in Modules 24–28.

## Next Steps

[03-spring-and-system-design-interviews.md](03-spring-and-system-design-interviews.md)

## Review Checklist

- [x] JVM + JMM + pools + virtual threads
