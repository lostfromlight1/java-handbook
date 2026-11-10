# Transactions, Caching, and Performance

> **Status:** Completed  
> **Module:** 35 — Hibernate and JPA  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Resource-local transactions in jpa-h2-demo; caching conceptual (2nd level disabled in demo logs)

## 1. Learning Objectives

- Align **JPA transactions** with JDBC (`RESOURCE_LOCAL` vs JTA).
- Explain **persistence context** (first-level cache) and **second-level cache** overview.
- List performance tactics: batch inserts, query plans, stateless sessions.

## 2. Prerequisites

- Module **33** Ch.03 — JDBC transactions.
- Ch.02 — fetch planning.

## 3. Why This Concept Exists

ORM convenience fails when every `flush` hits the database blindly or sessions span unbounded work. **Transaction boundaries** keep units of work consistent; **caching** reduces read load when data is safely cacheable.

## 4. Conceptual Explanation

**Transaction types:**

| `transaction-type` | Use |
|--------------------|-----|
| `RESOURCE_LOCAL` | `EntityTransaction` on single JDBC connection (demo) |
| `JTA` | Container-managed XA (Java EE / some app servers) |

**First-level cache:** single persistence context — repeated `find` by id within transaction may not re-query DB.

**Second-level cache:** shared across sessions (regions, eviction policies) — enable only with measured benefit; demo log shows `Second-level cache disabled`.

**Flush modes:** `AUTO` (before query) vs `COMMIT` — understand when INSERT appears.

**Performance levers:**

- `hibernate.jdbc.batch_size` for bulk inserts.
- StatelessSession for ETL-style writes without full context.
- `@Version` optimistic locking for concurrent updates.
- Query cache (distinct from 2nd level) — rarely default-on.

## 5. Step-by-Step Examples

[jpa-h2-demo](../../examples/backend/jpa-h2-demo/) transaction block:

```java
em.getTransaction().begin();
User user = new User("Lin");
em.persist(user);
em.getTransaction().commit();
User loaded = em.find(User.class, user.getId());
```

**Verified:** `name=Lin`, `idNotNull=true` after commit and find in same `EntityManager` before close.

Hibernate startup log (excerpt from real run):

```text
HHH000026: Second-level cache disabled
```

## 6. Real-World Use Cases

- `@Transactional` service methods (Spring) wrapping JPA calls — Module **36**.
- Read-only transactions for queries (`@Transactional(readOnly = true)` hint).
- Cache-aside for reference data with TTL outside Hibernate 2LC.

## 7. Edge Cases

- **Long session** — memory growth holding managed entities.
- **Dirty checking** at flush — unintended UPDATEs if fields mutated.
- **Open Session In View** anti-pattern — keeps session open for view rendering; avoid in REST APIs.

## 8. Common Mistakes

### Incorrect

Commits per row in loop — thousands of transactions.

### Corrected

Batch in one transaction with periodic flush/clear.

## 9. Best Practices

- Size connection pool and match transaction scope to request.
- Profile SQL with `hibernate.format_sql` in dev only.
- Use **pessimistic lock** (`PESSIMISTIC_WRITE`) sparingly for true contention.

## 10. Design Trade-offs

Full ORM vs jOOQ/JDBC for report queries — hybrid architectures common.

## 11. Chapter Summary

JPA performance is transaction and fetch discipline first, caching second; demo shows explicit RESOURCE_LOCAL commit on H2 with 2LC off.

## 12. Next Steps

Module **36** — Spring Framework (dependency injection and `@Transactional` integration).

## Review Checklist

- [x] Real Hibernate log line for 2LC disabled
- [x] Verified transactional persist output
