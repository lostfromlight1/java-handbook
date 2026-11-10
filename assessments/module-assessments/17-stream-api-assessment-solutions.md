# Solutions: Module 17 — Stream API

> **Assessment:** [17-stream-api-assessment.md](17-stream-api-assessment.md)

## Part A

1. Collection **stores** elements; stream **computes** over a source once per pipeline.
2. A **terminal** operation.
3. **No** — `IllegalStateException`.
4. `map` 1:1; `flatMap` 1:many flatten nested streams.
5. Returns **`0`** (identity).
6. `Map<K, List<T>>`.
7. **`ForkJoinPool.commonPool()`** (implementation default).
8. **Not guaranteed** — use `forEachOrdered` if order required.
9. `Stream.toList()` → **unmodifiable** list (Java 16+); `collect(Collectors.toList())` → mutable `ArrayList` traditionally.
10. `findFirst`, `anyMatch`, `limit` with terminal, etc.

## Part B

11. `15`  
12. **Two** (`Ada`, `Lin`)  
13. `Lin`

## Part C

14. `[8, 16, 20]`  
15. `32`  
16. `true`

## Part D

17. **2** names  
18. `2`

## Part E

19. **Non-thread-safe** shared collection; data races / corruption.
20. **Blocks common pool**, harms entire JVM throughput — use dedicated executor.

## Remediation

- Laziness → Ch.01 + fundamentals demo
- Collectors → grouping demo source code
- Parallel → Ch.04 anti-pattern section
