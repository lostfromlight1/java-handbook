# Module 24 — Concurrency and Multithreading

**Status:** Completed — third EXPAND-PRESERVE deepen pass 2026-11-11 (residual short chapters brought to textbook depth). Demos re-verified on Oracle JDK 21.0.9 (Temurin 21.0.12.1 not installed on this host; outputs match prior Temurin recordings). `StructuredTaskScope` probe verified with `--enable-preview`.

| # | Chapter | Status | Notes |
|---|---------|--------|-------|
| 01 | [Threads and Runnables](01-threads-and-runnables.md) | Completed | Deep-dive; start/join HB; stack vs heap |
| 02 | [Synchronization and Atomics](02-synchronization-and-atomics.md) | Completed | Deep-dive L4 JMM; left intact this pass (~300+) |
| 03 | [Executors and Futures](03-executors-and-futures.md) | Completed | Technical; pool lifecycle, Future, overload risks |
| 04 | [Concurrent Collections](04-concurrent-collections.md) | Completed | Technical; per-method atomicity vs compound races |
| 05 | [Virtual Threads and Structured Concurrency](05-virtual-threads-and-structured-concurrency.md) | Completed | Deep-dive; VT final (JEP 444); STS **preview** (JEP 453) |

**Prerequisites:** Modules 00–23; especially collections, lambdas, streams, Module 19 virtual-thread tour.

**Assessment:** [24-concurrency-multithreading-assessment.md](../assessments/module-assessments/24-concurrency-multithreading-assessment.md)
