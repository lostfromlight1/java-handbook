# Solutions: Module 51

1. Incrementally replace legacy subsystem behind facade without full stop.
2. **Feature** colocates related code; **layer-only** spreads one feature across packages → coupling.
3. Automated architecture rules (e.g. forbid illegal package dependencies).
4. Finds reliance on unsupported JDK internal APIs before migration.
5. **Expand** — add nullable column / backward-compatible schema first.
6. e.g. reliability, performance, security, operability, data, compliance (any four).
7. **SLO** pages on user-visible burn; raw ERROR logs cause fatigue unless correlated.
8. Total JDBC connections must not exceed database `max_connections`.
9. Long freeze, delivery stall, high failure probability.
10. Forces review from owning team on sensitive paths.

11. Migration adding column (nullable) **before** or with app that tolerates null; NOT NULL only after backfill—never require column before it exists.
12. Overview/dependencies; dashboards; common failures (DB, pool, GC); rollback; contacts.
