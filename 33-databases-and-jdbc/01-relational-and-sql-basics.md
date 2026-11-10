# Relational and SQL Basics

> **Status:** Completed  
> **Module:** 33 — Databases and JDBC  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Concepts; SQL exercised in Ch.02 JDBC demo (H2)

## 1. Learning Objectives

- Explain **relational** tables, rows, **primary keys**, and **foreign keys**.
- Write **`SELECT`**, **`INSERT`**, **`UPDATE`**, **`DELETE`**, and basic **`JOIN`**.
- Understand **ACID** at a transaction overview level (deep dive Ch.03).

## 2. Prerequisites

- Module **12** — maps as in-memory key/value mental model.
- Module **11** — strings for SQL literals (injection preview).

## 3. Why This Concept Exists

Most enterprise Java persists data in **relational databases** (PostgreSQL, MySQL, Oracle, H2). **SQL** is the lingua franca; JDBC (Ch.02) sends SQL from Java. Understanding tables and joins prevents ORM confusion later (Module **35**).

## 4. Conceptual Explanation

| Term | Meaning |
|------|---------|
| Table | Named relation with typed columns |
| Row | One record (tuple) |
| Primary key | Unique row identifier |
| Foreign key | Column referencing another table’s key |
| Index | Structure speeding lookups (trade-off: write cost) |
| Normalization | Reduce redundancy; denormalize for read perf when measured |

**ACID (overview):**

- **Atomicity** — all statements in transaction commit or none.
- **Consistency** — constraints hold after commit.
- **Isolation** — concurrent transactions’ visibility rules (levels in Ch.03).
- **Durability** — committed data survives crash (WAL on disk).

## 5. Step-by-Step Examples

SQL used in [h2-jdbc-demo](../../examples/backend/h2-jdbc-demo/) (executed by JDBC, real H2 output in Ch.02):

```sql
CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(64));
MERGE INTO users KEY(id) VALUES (1, 'Ada');
SELECT name FROM users WHERE id = 1;
```

**Verified JDBC result:** `name=Ada`, `rowCount=1`.

**JOIN example** (conceptual schema for orders):

```sql
SELECT u.name, o.total_cents
FROM users u
JOIN orders o ON o.user_id = u.id
WHERE u.id = 1;
```

Run against your schema — handbook does not invent join result rows without a demo.

## 6. Real-World Use Cases

- Normalized schema for OLTP APIs.
- Reporting queries with aggregations (`GROUP BY`, `HAVING`).
- Migration scripts in Module **34**.

## 7. Edge Cases and Limitations

- **`NULL`** semantics — `NULL = NULL` is unknown, not true.
- Case sensitivity of identifiers depends on DB (H2 defaults; PostgreSQL lowercases unquoted).
- N+1 query problem at ORM layer (Module **35**).

## 8. Common Mistakes

### Incorrect

String concatenation in SQL: `"SELECT * FROM users WHERE id = " + id` — **SQL injection**.

### Corrected

**PreparedStatement** placeholders `?` (Ch.02).

## 9. Best Practices

- Primary keys on every table.
- Explicit column lists in `INSERT`/`SELECT` for stable APIs.
- Use migrations (Module **34**) not manual prod edits.

## 10. Chapter Summary

Relational SQL models data Java services persist via JDBC; demo SQL produces verified `Ada` row through H2.

## 11. Next Steps

[02-jdbc-fundamentals.md](02-jdbc-fundamentals.md)

## Review Checklist

- [x] SQL tied to verified JDBC output
