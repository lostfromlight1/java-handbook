# Query Design and Migrations

> **Status:** Completed  
> **Module:** 34 — SQL and Data Access  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Migration patterns documented; Hibernate `hbm2ddl` in jpa-h2-demo (dev only)

## 1. Learning Objectives

- Apply **indexing** and **query plan** thinking for OLTP vs OLAP.
- Manage schema with **versioned migrations** (Flyway/Liquibase concepts).
- Distinguish **hbm2ddl** / `create-drop` from production migration discipline.

## 2. Prerequisites

- Ch.01 — SQL for developers.
- Module **33** — transactions.

## 3. Why This Concept Exists

Schema evolves with application releases. **Ad hoc** `ALTER` in production causes drift and downtime. **Migration tools** apply ordered, reviewed scripts. **Query design** prevents full table scans that JDBC cannot fix by itself.

## 4. Conceptual Explanation

| Tool | Model |
|------|-------|
| **Flyway** | Versioned SQL files `V1__init.sql`, checksum tracking |
| **Liquibase** | XML/YAML/SQL changelog with changesets |
| **Hibernate hbm2ddl** | Auto schema from entities — `create`, `update`, `validate` |

[jpa-h2-demo](../../examples/backend/jpa-h2-demo/) uses:

```xml
<property name="hibernate.hbm2ddl.auto" value="create-drop"/>
```

**Dev/test only** — drops schema on shutdown. Production uses **validate** or migrations, not `create-drop`.

**Indexing trade-off:** B-tree index on `users(email)` speeds login lookup; slows inserts and consumes disk.

**N+1:** ORM loads parent list then one query per child — fix with join fetch or batch size (Module **35**).

## 5. Step-by-Step Examples

**Example Flyway script** (not executed in handbook repo):

```sql
-- V2__add_user_email.sql
ALTER TABLE app_user ADD COLUMN email VARCHAR(255);
CREATE UNIQUE INDEX ux_app_user_email ON app_user(email);
```

Run via `mvn flyway:migrate` in your service — requires configured datasource.

**Query design check:**

```sql
EXPLAIN SELECT * FROM app_user WHERE id = ?;
```

Expect index seek on primary key — H2/PostgreSQL output format differs; run on your DB.

## 6. Real-World Use Cases

- Blue/green deploy with backward-compatible migrations (expand → migrate data → contract).
- Partial indexes for soft-deleted rows (`WHERE deleted_at IS NULL`).
- Read models in CQRS fed by migration-defined materialized views.

## 7. Edge Cases

- **Long-running migration** locks table — use online schema change tools on MySQL/PostgreSQL at scale.
- Rollback scripts often absent — design forward-only with feature flags.
- Multi-tenant schemas: per-tenant migration state.

## 8. Common Mistakes

### Incorrect

Relies on Hibernate `update` to add non-nullable column without default — fails on existing rows.

### Corrected

Two-step migration: add nullable → backfill → set NOT NULL.

## 9. Best Practices

- Peer-review migration SQL like application code.
- Load-test critical queries after index changes.
- Keep demo `hbm2ddl.auto=create-drop` out of production configs.

## 10. Chapter Summary

Sustainable data access pairs JDBC/ORM with versioned migrations and deliberate indexing; JPA demo illustrates auto-DDL only for local learning.

## 11. Next Steps

Module **35** — Hibernate and JPA.

## Review Checklist

- [x] hbm2ddl setting documented from real persistence.xml
