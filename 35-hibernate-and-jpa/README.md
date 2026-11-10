# Module 35 — Hibernate and JPA

**Status:** Completed  
**Depth pass:** 2026-11-11 — Technical Level 3 expansion (lifecycle, fetching, flush/cache)

| # | Chapter | Status | Verified examples |
|---|---------|--------|-------------------|
| 01 | [ORM and Entities](01-orm-and-entities.md) | Completed | [jpa-h2-demo](../examples/backend/jpa-h2-demo/) |
| 02 | [Relationships and Fetching](02-relationships-and-fetching.md) | Completed | Mapping rules; extend demo locally for SQL logs |
| 03 | [Transactions, Caching, and Performance](03-transactions-caching-and-performance.md) | Completed | `name=Lin`, `idNotNull=true`; HHH000026 2LC log |

**Prerequisites:** Modules 33–34; see [LEARNING-ROADMAP.md](../LEARNING-ROADMAP.md).

**Run JPA demo:**

```powershell
cd examples/backend/jpa-h2-demo
..\..\spring-boot-minimal\mvnw.cmd -q compile "exec:java" "-Dexec.mainClass=com.handbook.jpa.JpaH2Demo"
```

**Assessment:** [35-hibernate-jpa-assessment.md](../assessments/module-assessments/35-hibernate-jpa-assessment.md)
