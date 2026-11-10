# Solutions: Module 24 — Concurrency and Multithreading

1. `start()` schedules new thread; `run()` executes on current thread.
2. Join completion happens-before return from `join` (JLS).
3. Data race on plain `int` increment — lost updates.
4. Both can reach correct counter; atomics lighter for single field.
5. `shutdown()` and await termination (AutoCloseable).
6. `ExecutionException` wrapping cause.
7. No.
8. Java 21 (final).
9. Worker and executor tasks (`executorVirtual=true`).
10. Preview (`StructuredTaskScope`); needs `--enable-preview`.

11. `false`
12. `42`
13. `2`

14. Fixed platform pool sized to cores (CPU-bound).
15. Use `putIfAbsent`/`computeIfAbsent` for atomic check-act.
