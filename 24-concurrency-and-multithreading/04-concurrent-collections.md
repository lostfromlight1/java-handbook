# Concurrent Collections

> **Status:** Completed  
> **Module:** 24 — Concurrency and Multithreading  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Use **`ConcurrentHashMap`** for shared maps under concurrency.
- Avoid synchronizing entire **`HashMap`** on every read.
- Know **compound actions** still need explicit coordination.

## 2. Prerequisites

- Module **12** collections; Ch.02 synchronization.

## 3. Why This Concept Exists

`Collections.synchronizedMap` locks the whole map per operation. **`ConcurrentHashMap`** (Java API) offers fine-grained concurrency for many workloads; **`BlockingQueue`** variants support producer–consumer patterns.

## 4. Conceptual Explanation

`ConcurrentHashMap` provides thread-safe `put`, `get`, and atomic operations like **`merge`**, **`compute`**, **`putIfAbsent`**. Iterators are weakly consistent — they reflect the map at some point but need not throw `ConcurrentModificationException`.

**Not automatic:** check-then-act across keys still races unless you use a single atomic map method or external lock.

## 5. Step-by-Step Examples

Demo: [concurrent-collections-demo](../../examples/core-java/concurrent-collections-demo/).

**Verified output:**

```text
Ada=2
Lin=2
size=2
```

Two threads merge counts; `Ada` incremented from 1 to 2.

## 6. Real-World Use Cases

- Per-tenant rate limit counters in memory.
- Shared registries in JVM services.

## 7. Internal Behavior

- **API contract:** Documented atomicity per method; no global snapshot without `size()` caveats.
- **HotSpot:** Segmented/table design is implementation detail.

## 8. Edge Cases and Limitations

- `size()` and `isEmpty()` are approximate under heavy mutation.
- Null keys/values not permitted (unlike `HashMap`).

## 9. Common Mistakes

### Incorrect

`if (!map.containsKey(k)) map.put(k, v)` on `ConcurrentHashMap` — still racy for composed logic.

### Corrected

`map.putIfAbsent(k, v)` or `map.computeIfAbsent`.

## 10. Best Practices

- Pick the collection for the access pattern (`CopyOnWriteArrayList` for rare writes, many readers, etc.).
- For ordering between producers/consumers, use `BlockingQueue`.

## 11. Chapter Summary

`java.util.concurrent` collections encapsulate safe sharing patterns; understand each operation’s atomicity scope.

## 12. Next Steps

[05-virtual-threads-and-structured-concurrency.md](05-virtual-threads-and-structured-concurrency.md)

## Review Checklist

- [x] Verified demo
