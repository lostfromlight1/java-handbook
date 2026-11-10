# Executors and Futures

> **Status:** Completed  
> **Module:** 24 — Concurrency and Multithreading  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Submit tasks with **`ExecutorService`**.
- Retrieve results via **`Future`** and **`Callable`**.
- Shut down pools with **try-with-resources** (`ExecutorService` extends `AutoCloseable` since Java 19).

## 2. Prerequisites

- Ch.01–02; Module **14** try-with-resources.

## 3. Why This Concept Exists

Creating a thread per task does not scale. **Thread pools** reuse platform threads, cap parallelism, and queue work. **`Future`** decouples submission from waiting for a result.

## 4. Conceptual Explanation

- **`Executors.newFixedThreadPool(n)`** — bounded pool of platform threads.
- **`submit(Callable)`** — returns `Future<T>`; exceptions wrap in `ExecutionException` on `get()`.
- **`get(timeout, unit)`** — bounded wait.
- **`ExecutorService` as try-with-resources** — invokes `shutdown()` and awaits termination (API).

## 5. Step-by-Step Examples

Demo: [executors-futures-demo](../../examples/core-java/executors-futures-demo/).

**Verified output:**

```text
futureResult=42
done=true
poolShutdown=ok
```

## 6. Real-World Use Cases

- Parallel fan-out to downstream HTTP/DB calls with a bounded pool.
- Offloading CPU work from a UI thread (on JVM clients).

## 7. Internal Behavior

- **API/JLS:** No happens-before between arbitrary code and task body unless publication rules met (Module 25).
- **HotSpot:** Pool threads are platform threads unless you use virtual-thread executors (Ch.05).

## 8. Edge Cases and Limitations

- Unbounded queues + fixed pool can grow unbounded task backlog.
- `shutdown()` stops new tasks; `shutdownNow()` interrupts workers — handle interruption in tasks.

## 9. Common Mistakes

### Incorrect

Never shutting down a pool in a long-lived CLI — JVM hang on non-daemon workers.

### Corrected

`try (ExecutorService ex = ...) { ... }` or explicit `shutdown()`/`awaitTermination`.

## 10. Best Practices

- Size CPU pools near core count; size blocking-I/O pools larger or use virtual threads (Ch.05).
- Use `CompletableFuture` for composition (stdlib; patterns in backend modules).

## 11. Chapter Summary

Executors manage thread lifecycle; `Callable` + `Future` return values from background tasks.

## 12. Next Steps

[04-concurrent-collections.md](04-concurrent-collections.md)

## Review Checklist

- [x] Verified demo
