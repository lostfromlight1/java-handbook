# Solutions: Module 47 — Observability and Monitoring

1. Logs, metrics, traces.
2. Machine query/filter; consistent fields for correlation.
3. Counter monotonic total; gauge instantaneous value (heap, queue depth).
4. Links spans/logs across services for one request.
5. Standard export protocol from SDK/agent to collector/backends.
6. Prometheus scrapes app `/actuator/prometheus` on interval.
7. SLI is measured indicator; SLO is target level for that indicator.
8. When budget burns, slow risky releases; invest in reliability work.
9. Rollback, scale out, disable feature flag, open circuit breaker.
10. Focus on systemic improvement; punitive blame hides failures.

11. `structured-logging=ok`
12. Too many unique label combinations (e.g. per-user); storage/query cost explodes.
