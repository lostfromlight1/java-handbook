# Synchronization and Atomics

> **Status:** Completed  
> **Module:** 24 — Concurrency and Multithreading  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Explain **data races** on shared mutable state.
- Use **`synchronized`** and **`java.util.concurrent.atomic`** for mutual exclusion and atomic updates.
- Choose between **intrinsic locks** and atomics for simple counters.

## 2. Prerequisites

- Ch.01 threads; Module **25** (happens-before) deepens visibility rules.

## 3. Why This Concept Exists

`counter++` is read-modify-write. Two threads can interleave updates and lose increments. Without coordination, behavior is a **data race** (JLS) and visibility is undefined for racy fields.

## 4. Conceptual Explanation

- **`synchronized` block/method** — mutual exclusion on a monitor (intrinsic lock). Unlock **happens-before** the next lock (JMM — Module 25).
- **`AtomicInteger` / `AtomicReference`** — lock-free CAS loops for single-variable updates; `incrementAndGet()` is atomic.

Atomics do not compose multi-field invariants; use locks or higher-level constructs when multiple fields must move together.

## 5. Step-by-Step Examples

Demo: [synchronization-atomics-demo](../../examples/core-java/synchronization-atomics-demo/).

**Verified output:**

```text
unsafeCounter=12188
atomicCounter=40000
syncCounter=40000
```

Four threads × 10_000 increments should be 40_000. The **unsafe** path shows a lost-update race (exact partial count varies by scheduling — this run: `12188`). **Atomic** and **synchronized** paths reached `40000` on Temurin 21.0.12.1.

## 6. Real-World Use Cases

- Metrics counters, sequence generators, single-slot caches.
- Guarding invariants on small critical sections.

## 7. Internal Behavior

- **JLS/JMM:** `volatile`, `synchronized`, and atomics establish happens-before; racy plain fields have no guarantee.
- **HotSpot:** Locks inflate, bias, and use CAS; details vary by release and are not portable guarantees.

## 8. Edge Cases and Limitations

- `synchronized` on different objects does not protect the same data — pick one lock per guarded structure.
- Deadlock: threads wait on each other’s locks — consistent lock ordering mitigates.
- Atomics on one field do not prevent other fields from racing.

## 9. Common Mistakes

### Incorrect

`if (map.get(k) == null) map.put(k, v)` without locking — two threads can both put.

### Corrected

`ConcurrentHashMap.computeIfAbsent` (Ch.04) or `synchronized` on the structure.

## 10. Best Practices

- Prefer `java.util.concurrent` over hand-rolled `wait`/`notify` in new code.
- Keep critical sections short; measure before micro-optimizing locks vs atomics.

## 11. Chapter Summary

Races break counters and invariants; `synchronized` and atomics provide the mechanisms; visibility rules are formalized in Module 25.

## 12. Next Steps

[03-executors-and-futures.md](03-executors-and-futures.md)

## Review Checklist

- [x] Verified demo (race output recorded from real run)
