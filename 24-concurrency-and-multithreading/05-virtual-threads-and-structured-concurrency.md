# Virtual Threads and Structured Concurrency

> **Status:** Completed  
> **Module:** 24 — Concurrency and Multithreading  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Create **virtual threads** (Java **21 final**, JEP 444).
- Use **`Executors.newVirtualThreadPerTaskExecutor()`** for per-task carriers.
- Describe **structured concurrency** as a **preview** API (not required on Java 21 without `--enable-preview`).

## 2. Prerequisites

- Ch.01–03; Module **19** Java 21 tour.

## 3. Why This Concept Exists

Platform threads are scarce; blocking I/O on a large platform thread pool wastes memory. **Virtual threads** are JVM-managed, cheap to create; blocking parks the virtual thread instead of holding an OS thread (HotSpot implementation on JDK 21 — not a JVMS guarantee for all JVMs).

## 4. Conceptual Explanation

- **`Thread.startVirtualThread(Runnable)`** or **`Thread.ofVirtual().start(...)`** — starts a virtual thread.
- **`Thread.currentThread().isVirtual()`** — distinguishes carrier task type.
- **CPU-bound work** still needs sizing to cores; virtual threads do not multiply CPU throughput.

**Structured Concurrency (Preview):** `StructuredTaskScope` groups subtasks with a single failure/cancellation boundary. In Java 21 it is **preview** — enable with `--enable-preview --release 21` and re-check JEP status on upgrades. Module **19** demo runs a minimal preview sample; this chapter does not treat preview APIs as production defaults.

## 5. Step-by-Step Examples

Demo: [virtual-threads-demo](../../examples/core-java/virtual-threads-demo/).

**Verified output:**

```text
isVirtual=true
vtName=
executorVirtual=true
platformIsVirtual=false
```

Empty `vtName` is normal for default-named virtual threads on this JDK.

## 6. Real-World Use Cases

- HTTP servers: one virtual thread per request with blocking JDBC/HTTP clients.
- Fan-out I/O where tasks spend time waiting.

## 7. Internal Behavior

- **Java SE API (final):** Virtual threads are standard threads from the language model’s perspective for `Thread` APIs.
- **HotSpot:** Mount virtual threads on a **carrier** platform thread pool; **pinning** can occur with some `synchronized` blocks or native code — monitor in production (JEP / release notes, not folklore).

## 8. Edge Cases and Limitations

- Thread-local heavy code may need audit at scale.
- Do not use huge platform thread pools and virtual threads interchangeably without measurement.

## 9. Common Mistakes

### Incorrect

Spinning CPU-heavy work on millions of virtual threads expecting linear speedup.

### Corrected

Bound CPU work to cores (`ExecutorService` with fixed platform pool or parallel streams sized deliberately).

## 10. Best Practices

- Use virtual threads for **many blocking tasks**; keep existing pool for legacy libraries if pinning appears.
- Record JDK vendor/version when comparing pinning behavior.

## 11. Chapter Summary

Java 21 ships **virtual threads** as final API; structured concurrency remains **preview** — label and enable explicitly.

## 12. Next Steps

Module **25** — Java Memory Model (happens-before).

## Review Checklist

- [x] Verified demo
- [x] Preview vs final labeled
