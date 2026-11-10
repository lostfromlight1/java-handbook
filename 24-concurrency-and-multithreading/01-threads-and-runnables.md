# Threads and Runnables

> **Status:** Completed  
> **Module:** 24 — Concurrency and Multithreading  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Start work on another **thread** using `Thread` and **`Runnable`**.
- Use **`start`**, **`join`**, and name threads for observability.
- Distinguish **platform threads** (OS-backed) from work queued on the current thread.

## 2. Prerequisites

- Module **00** Ch.02 (process vs thread, call stack).
- Module **16** lambdas (runnable tasks as lambdas).

## 3. Why This Concept Exists

Servers handle many requests, UIs stay responsive while loading, and pipelines overlap I/O with computation. One thread executing `main` cannot do all of that without blocking everything else. **Concurrency** runs multiple threads of control in one JVM process; you must then reason about **shared memory** (Module 25–24 Ch.02).

## 4. Conceptual Explanation

A **thread** is a sequence of bytecode execution with its own **JVM stack** (JVMS: each thread has a thread stack). The **heap** is shared across threads.

`Runnable` is a functional interface (`void run()`). `Thread` implements `Runnable` but you normally pass a **task** (`new Thread(() -> { ... })`) rather than subclassing `Thread`.

- **`start()`** — asks the JVM to begin `run()` asynchronously on a new thread (JLS/API contract: must not call `run()` directly if you want concurrency).
- **`join()`** — current thread waits until the target finishes.
- **Daemon threads** — `setDaemon(true)` before `start()`; JVM may exit when only daemons remain (API behavior).

## 5. Terminology

| Term | Meaning |
|------|---------|
| Platform thread | Thread backed by an OS thread (traditional `new Thread(...)`). |
| Task | `Runnable` (or `Callable`) body to execute. |
| Lifecycle | New → runnable → running → terminated (simplified model). |

## 6. Step-by-Step Examples

Demo: [threads-runnable-demo](../../examples/core-java/threads-runnable-demo/).

**Verified output (Temurin 21.0.12.1):**

```text
mainThread=main
daemonDefault=false
workerName=handbook-worker
workerAlive=true
afterJoinAlive=false
```

`handbook-worker` shows naming; `join` waits until `afterJoinAlive=false`.

## 7. Real-World Use Cases

- Background refresh on a desktop or mobile JVM client.
- Parallel stages in batch jobs (with proper coordination).
- Request handling (often via executors — Ch.03).

## 8. Internal Behavior

- **JLS/JVMS:** Threads share heap; per-thread stacks hold frames and locals.
- **HotSpot:** Maps platform threads to OS threads; scheduling is OS + JVM cooperative points (not something you rely on for correctness).

## 9. Edge Cases and Limitations

- Uncaught exceptions on non-main threads do not automatically stop the JVM.
- Calling `run()` on the current thread — no new parallelism.
- Creating unbounded platform threads can exhaust memory or OS limits.

## 10. Common Mistakes

### Incorrect

Subclasses `Thread` and overrides `run()` but never calls `start()`.

### Corrected

Construct `Thread` with a `Runnable`, call `start()`, then `join()` when you need happens-before completion (Ch.25).

## 11. Best Practices

- Prefer **executor services** (Ch.03) over raw `new Thread` in application code.
- Name threads (`setName` or constructor) for logs and thread dumps.
- Treat shared fields as unsafe until synchronized or concurrent structures are used.

## 12. Design Trade-offs

Raw `Thread` is simple for learning and tiny tools; executors add pooling, lifecycle, and policy.

## 13. Chapter Summary

Threads plus `Runnable`/`start`/`join` are the foundation of Java concurrency; shared heap requires synchronization in later chapters.

## 14. Next Steps

[02-synchronization-and-atomics.md](02-synchronization-and-atomics.md)

## Review Checklist

- [x] Verified demo
- [x] JLS/JVMS vs HotSpot labeled
