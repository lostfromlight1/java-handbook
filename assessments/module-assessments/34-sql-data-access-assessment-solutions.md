# Solutions: Module 34

1. `V<n>__description.sql` (e.g. `V2__add_user_email.sql`).
2. Destructive/uncontrolled schema; no reviewed migration history.
3. One query for parents then one per child row instead of join/batch fetch.
4. Aggregate per group (COUNT, SUM) with grouped columns.
5. Indexes speed reads but add maintenance cost on INSERT/UPDATE/DELETE.
6. Inspect query execution plan before tuning.
7. Memory exhaustion, latency; use LIMIT/OFFSET or cursors.
8. `WHERE` filters rows before grouping; `HAVING` filters groups after aggregation.
9. Add nullable column → backfill → enforce NOT NULL.
10. `LIMIT`/`OFFSET` (PostgreSQL/H2) vs Oracle row limiting syntax differences.

11. `rowCount=1`
