# Module 33 — Databases and JDBC

**Status:** Completed  
**Depth pass:** 2026-11-11 — Technical Level 3 expansion (mechanism sections, Temurin 21.0.12.1 verification)

| # | Chapter | Status | Verified examples |
|---|---------|--------|-------------------|
| 01 | [Relational and SQL Basics](01-relational-and-sql-basics.md) | Completed | SQL via [h2-jdbc-demo](../examples/backend/h2-jdbc-demo/) |
| 02 | [JDBC Fundamentals](02-jdbc-fundamentals.md) | Completed | `name=Ada`, `rowCount=1`, `jdbcH2=ok` |
| 03 | [Transactions and Pooling](03-transactions-and-pooling.md) | Completed | JPA RESOURCE_LOCAL in [jpa-h2-demo](../examples/backend/jpa-h2-demo/) |

**Prerequisites:** Modules 11–12, 14; see [LEARNING-ROADMAP.md](../LEARNING-ROADMAP.md).

**Run JDBC demo (no Maven):**

```powershell
cd examples/backend/h2-jdbc-demo
javac -cp lib\h2-2.3.232.jar -d out src\main\java\com\handbook\jdbc\JdbcH2Demo.java
java -cp "out;lib\h2-2.3.232.jar" com.handbook.jdbc.JdbcH2Demo
```

**Assessment:** [33-databases-jdbc-assessment.md](../assessments/module-assessments/33-databases-jdbc-assessment.md)
