# Solutions: Module 44 — Microservices and Distributed Systems

1. Choose availability or strong consistency per operation; document product choice (e.g. reject writes vs stale reads).
2. Timeouts, retries with caps, circuit breakers; never infinite `HttpClient` wait.
3. Sync: simpler call chain, coupled failure; async: decoupled, delivery semantics complexity.
4. Prevents hidden coupling and shared schema changes breaking multiple services.
5. Fail fast without calling unhealthy dependency (`CircuitOpenException` in demo).
6. Idempotent reads/retries; writes with idempotency keys or known-safe operations.
7. Isolate resource pools so one slow dependency cannot exhaust all threads.
8. Saga: multiple local commits + compensations; no single cross-service transaction.
9. `refundPayment` after failed inventory reservation.
10. DB unique constraint on processed event/idempotency key table.

11. `circuit-breaker=ok`
12. Probe whether dependency recovered before fully closing breaker.
