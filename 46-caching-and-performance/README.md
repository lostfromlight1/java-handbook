# Module 46 — Caching and Performance Engineering

**Status:** Completed — fourth EXPAND-PRESERVE deepen pass 2026-11-11 (Technical Levels 2–3).  
**Last reviewed:** 2026-11-11  

| # | Chapter | Status |
|---|---------|--------|
| 01 | [Caching Strategies](01-caching-strategies.md) | Completed |
| 02 | [Redis and Invalidation](02-redis-and-invalidation.md) | Completed |
| 03 | [Load Testing and Capacity](03-load-testing-and-capacity.md) | Completed |

**Prerequisites:** Modules 33–35 (data access), Module 14 (concurrency), Modules 44–45 helpful for resilience and events.

**Assessment:** [46-caching-performance-assessment.md](../assessments/module-assessments/46-caching-performance-assessment.md)

**Examples:** [in-memory-cache-demo](../examples/caching/in-memory-cache-demo/) — Verified on Oracle JDK 21.0.9 (2026-11-11; matches prior Temurin 21.0.12.1 baseline): `user:2=miss`; `in-memory-cache=ok`.

**Depth note (fourth deepen 2026-11-11):** Chapters expanded to uniform textbook depth (mechanism flows, progressive examples, trade-offs, security/performance where warranted). Redis remains conceptual without fabricated cluster output; load-test numbers stay illustrative worksheets only.
