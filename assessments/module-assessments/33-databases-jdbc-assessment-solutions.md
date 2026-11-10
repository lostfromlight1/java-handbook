# Solutions: Module 33

1. Uniquely identifies row; enforces entity integrity.
2. Parameterized queries (`?` binds), never concatenate untrusted input.
3. Precompiled SQL, safe parameters, often better performance.
4. Locates driver and database instance (`jdbc:h2:mem:...`).
5. Can only consume rows in order once unless scrollable type requested.
6. Transaction statements all commit or all roll back.
7. `true` — each statement commits individually unless disabled.
8. Returns connection to pool (pooling DataSource), may not close TCP.
9. Embedded, no external DB install; reproducible for learners/CI.
10. Checked exception (must handle or declare).

11. `name=Ada`
12. `jdbcH2=ok`
