# Solutions: Module 52

1. Equal keys must share hash or buckets break lookup contract (M06/M12).
2. e.g. cannot cast `List<?>` to `List<String>` at runtime; `ClassCastException` at bridge.
3. e.g. `synchronized` unlock→lock; `volatile` write/read; `Thread.start`, executor actions (M25).
4. **No** — only visibility/order; use atomics or synchronization.
5. Required deps explicit, immutable fields, easier testing (M36).
6. Internal call skips proxy; transaction advice not applied (M36).
7. High concurrency **blocking I/O** servers; not CPU-bound parallel compute without care.
8. **PUT** (and GET/DELETE); POST create usually not.
9. Read cache → miss → load DB → populate cache.
10. Monolith: simpler deploy; microservices: independent scale at ops cost (M40).

11. Sample: API pods, PostgreSQL, Redis cache; bottleneck DB or cache stampede without TTL/jitter.
