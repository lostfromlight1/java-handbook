# JDBC Fundamentals

> **Status:** Completed  
> **Module:** 33 — Databases and JDBC  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1; H2 2.3.232 (Maven and manual `-cp` run)

## 1. Learning Objectives

- Open connections with **`DriverManager`** or **DataSource**.
- Use **`PreparedStatement`** and **`ResultSet`** safely.
- Apply **try-with-resources** for JDBC auto-close.

## 2. Prerequisites

- Ch.01 — SQL basics.
- Module **14** — SQLException, try-with-resources.

## 3. Why This Concept Exists

**JDBC** (Java Database Connectivity) is the standard API (`java.sql`) vendors implement via **drivers**. Your code speaks JDBC; the driver translates to PostgreSQL wire protocol, H2 in-memory engine, etc. Frameworks (Spring, JPA) still sit on JDBC drivers.

## 4. Conceptual Explanation

| Type | Role |
|------|------|
| `Connection` | Session to database |
| `Statement` / `PreparedStatement` | SQL execution (`?` parameters) |
| `ResultSet` | Cursor over query rows |
| `SQLException` | Checked exception for DB errors |

**URL pattern:** `jdbc:h2:mem:name;DB_CLOSE_DELAY=-1` — embedded H2 in RAM for tests.

**Driver loading:** JDBC 4+ **SPI** — drivers on classpath auto-register; no `Class.forName` required for modern drivers.

## 5. Step-by-Step Examples

Demo: [h2-jdbc-demo](../../examples/backend/h2-jdbc-demo/).

**Verified output (Maven `exec:java` and manual `java -cp out;h2.jar`):**

```text
name=Ada
rowCount=1
jdbcH2=ok
```

Important lines: `PreparedStatement` for `MERGE` and `SELECT`; nested try-with-resources closes statements and connection.

Manual JDK path (no Maven on PATH) — download H2 JAR to `lib/` per README; `javac`/`java -cp` as documented when Maven unavailable.

## 6. Real-World Use Cases

- Repository implementations before JPA adoption.
- Batch `addBatch` / `executeBatch` for ETL.
- Calling stored procedures via `CallableStatement`.

## 7. Internal Behavior

Driver manages **server-side prepared statement** cache when supported. H2 in-memory DB dies when last connection closes unless `DB_CLOSE_DELAY` set.

## 8. Edge Cases and Limitations

- **`ResultSet` type** — forward-only default; scrolling costs memory.
- **Time types** — use `java.time` with JDBC 4.2+ (`setObject` with `OffsetDateTime`).
- SQLite/MySQL dialect differences — test on target DB.

## 9. Common Mistakes

### Incorrect

Holds `Connection` open entire JVM lifetime without pool — exhausts DB max connections.

### Corrected

Pool connections (Ch.03); short borrow per request.

## 10. Best Practices

- Always **parameterize** SQL.
- Map `SQLException` to domain errors at service boundary.
- Integration-test against **Testcontainers** or H2 with dialect caveats documented.

## 11. Chapter Summary

JDBC is the thin Java layer to relational engines; the H2 demo records real query results for Ada.

## 12. Next Steps

[03-transactions-and-pooling.md](03-transactions-and-pooling.md)

## Review Checklist

- [x] Verified JDBC output on Temurin 21.0.12.1
