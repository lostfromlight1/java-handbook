# SQL for Java Developers

> **Status:** Completed  
> **Module:** 34 — SQL and Data Access  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** H2 via h2-jdbc-demo queries; advanced SQL conceptual

## 1. Learning Objectives

- Write **parameterized** queries and map rows to Java types.
- Use **aggregations**, **subqueries**, and **EXISTS** for reporting.
- Avoid **SELECT *** in production APIs when stability matters.

## 2. Prerequisites

- Module **33** — JDBC and SQL basics.

## 3. Why This Concept Exists

ORMs generate SQL, but **production debugging** requires reading execution plans and hand-written SQL for reports, migrations, and performance fixes. Java developers bridge **domain objects** and **set-based** relational logic.

## 4. Conceptual Explanation

**Mapping rows to Java:**

| SQL type | Java type (JDBC 4.2+) |
|----------|----------------------|
| `INTEGER` | `int` / `Integer` |
| `VARCHAR` | `String` |
| `TIMESTAMP WITH TIME ZONE` | `OffsetDateTime` |
| `DECIMAL` | `BigDecimal` |

**Aggregation:**

```sql
SELECT dept, COUNT(*) AS headcount
FROM employees
GROUP BY dept
HAVING COUNT(*) >= 5;
```

**Correlated subquery / EXISTS** — often clearer than large joins for existence checks.

## 5. Step-by-Step Examples

From verified H2 JDBC demo pattern — **count query**:

```sql
SELECT COUNT(*) FROM users;
```

**Verified result:** `rowCount=1` printed by `JdbcH2Demo`.

**Pagination** (dialect-specific; PostgreSQL style):

```sql
SELECT id, name FROM users ORDER BY id LIMIT 20 OFFSET 40;
```

H2 supports `LIMIT`/`OFFSET`; Oracle uses different syntax — migrations handle dialect (Ch.02).

## 6. Real-World Use Cases

- Dashboard queries with `GROUP BY` and window functions (`ROW_NUMBER()`).
- Bulk update with `WHERE` predicates tied to business dates (`java.time` in bind params).
- Read replicas for heavy SELECT traffic.

## 7. Edge Cases

- **Cartesian products** from missing JOIN conditions.
- **Implicit conversion** in DB comparing string to number.
- Unicode collation affecting `ORDER BY` on names.

## 8. Common Mistakes

### Incorrect

Loads millions of rows into `List` in Java — OOME.

### Corrected

Stream with fetch size, cursor, or server-side pagination.

## 9. Best Practices

- Explain queries with **`EXPLAIN`** on target DB.
- Version SQL in migration files, not ad hoc prod runs.
- Keep reporting SQL in named repository methods or views.

## 10. Chapter Summary

Java services own SQL quality through JDBC binding and set-based thinking; handbook counts verified on H2 users table.

## 11. Next Steps

[02-query-design-and-migrations.md](02-query-design-and-migrations.md)

## Review Checklist

- [x] COUNT output tied to real demo
