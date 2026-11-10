# Solutions: Module 46 — Caching and Performance

1. Read cache; on miss load DB, put cache, return.
2. Single-flight lock, jittered TTL, request coalescing.
3. `LinkedHashMap` access-order evicts eldest when size > max (2 entries).
4. Redis shared across JVMs; Caffeine local fast but per-instance.
5. TTL simple staleness window; invalidation accurate on updates.
6. Write-behind async flush—data loss risk if cache dies before DB write.
7. Highly mutable secured data without invalidation discipline.
8. Load: expected peak; soak: hours to find leaks/drift.
9. Tail latency drives user pain; average hides outliers.
10. Pending threads waiting for connections; rising wait time.

11. `in-memory-cache=ok`
12. `miss` (evicted when `user:3` inserted after touching `user:1`).
