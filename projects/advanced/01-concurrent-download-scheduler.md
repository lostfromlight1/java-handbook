# Project: Concurrent Download Scheduler

> **Level:** Advanced  
> **Status:** Completed  
> **Prerequisites:** Modules 24, 25, 32 (HTTP client), 14  
> **Last reviewed:** 2026-11-10

## Goals

Schedule N URL fetches with bounded parallelism using `ExecutorService`, report latencies, and enforce timeouts. Reinforces concurrency + `HttpClient` (Module 32).

## Learning objectives

- `ExecutorService` with fixed pool; `Future` or `CompletableFuture`.
- Timeout per task with `orTimeout` / `cancel`.
- Aggregate results thread-safely (`ConcurrentLinkedQueue` or records collected at end).
- Optional: Java 21 **virtual threads** executor compare mode flag.

## Architecture

`SchedulerMain` → `DownloadService` → `HttpClient` (shared instance).

## Milestones

1. Sequential download list from file.
2. Parallel pool size 4; summary success/fail counts.
3. Per-URL timeout 5s; failures categorized.
4. JMM-safe publication of results (no unsynchronized `ArrayList` from workers).

## Acceptance criteria

- [ ] Pool size configurable via `-Dpool=4`.
- [ ] Shutdown `executor` in `finally`; no leaked threads.
- [ ] README documents happens-before between task completion and summary print.
- [ ] Tests mock slow URLs without network (inject `Clock` or fake client interface).

## Starter hints

- [http-client-demo](../../examples/networking/http-client-demo/) for client builder.
- [executors-futures-demo](../../examples/core-java/executors-futures-demo/).

## Verification steps

```bash
java -cp out com.handbook.scheduler.SchedulerMain urls.txt
# expect: completed<=N, failures logged, wall time < N * sequential
```

Compare `-Dvirtual=true` vs platform threads on I/O-heavy list (document observations, not fabricated timings).

## Extension

Circuit breaker from [circuit-breaker-demo](../../examples/resilience/circuit-breaker-demo/).
