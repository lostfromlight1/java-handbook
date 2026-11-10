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

### Class loading interview sequence

```mermaid
flowchart LR
  L[Loading] --> K[Linking verify prepare]
  K --> I[Initializing clinit]
  I --> U[First active use]
```

**Parent delegation:** custom classloader asks parent first so JDK classes load consistently. **Wrong:** "class loaders load bytecode from network only"—they also define packages and security.

### GC talking points (collector-neutral)

| Question | Safe answer shape |
|----------|-------------------|
| Young vs old gen | Most objects die young; minor collections cheaper |
| GC pause vs throughput | G1/ZGC tune for pause targets; measure don't guess |
| `System.gc()` | Hint only; production avoid explicit calls |

### `java.util.concurrent` depth

| API | Interview angle |
|-----|-----------------|
| `CompletableFuture` | Async chain; `orTimeout` (Java 9+); exceptional completion |
| `Phaser` | Rare; mention only if asked |
| `BlockingQueue` | Producer-consumer backpressure |

## 6. Scenario Questions

**Q: Two threads increment static `int`?**  
Race unless `AtomicInteger` or synchronization.

**Q: Detect deadlock?**  
Thread dumps (`jcmd`/`jstack`), JFR; prevention via lock ordering.

**Q: Is `ConcurrentHashMap` lock-free?**  
Segment/bin synchronization evolved by JDK version—say **fine-grained locking / CAS**, not "fully lock-free map" without version.

**Q: Virtual thread pin when using JDBC?**  
Blocking JDBC parks carrier; ensure pool sized for carriers × platform threads, not "millions of platform threads."

### Whiteboard: fixed thread pool saturation

Given `core=2`, `max=2`, `queue=10`, `CallerRunsPolicy`: eleventh submit when queue full runs on **caller thread**—backpressure propagates to servlet thread (interview tests policy names).

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

### Memory leak interview narrative

| Leak pattern | Signal | Tool |
|--------------|--------|------|
| Static map grows | Old gen climbs, full GC ineffective | Heap histogram `jcmd GC.class_histogram` |
| Thread leak | Thread count rises | Thread dump |
| Classloader leak | Metaspace OOM | Duplicate loaded classes in dump |

**Answer shape:** "I'd capture heap dump during growth, compare dominator tree, tie to redeploy cycle or cache without bounds."

### Double-checked locking (classic)

```java
// Incorrect without volatile
if (instance == null) {
    synchronized (Lock.class) {
        if (instance == null) instance = new Singleton();
    }
}
```

Without `volatile` on `instance`, another thread may see partially constructed object (JMM). Fix: `volatile` field or initialize-on-demand holder idiom (M24).

## 11. Chapter Summary

JVM and concurrency interviews need JMM precision, collector vocabulary, pool/virtual-thread awareness, and leak-debugging vocabulary from Modules 24–28.

## Next Steps

[03-spring-and-system-design-interviews.md](03-spring-and-system-design-interviews.md)

## Review Checklist

- [x] Technical L2–3; happens-before mechanism; JVM + JMM preserved; deepen pass 2026-11-11
