# Transactions and Pooling

> **Status:** Completed  
> **Module:** 33 — Databases and JDBC  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Transaction pattern in [jpa-h2-demo](../../examples/backend/jpa-h2-demo/); pooling conceptual + HikariCP notes

## 1. Learning Objectives

- Use JDBC **`setAutoCommit`**, **`commit`**, **`rollback`**.
- Explain **isolation levels** and **lost update** risks.
- Describe **connection pools** (HikariCP) and why not to open per-request raw connections in production.

## 2. Prerequisites

- Ch.02 — `Connection` lifecycle.
- Module **25** — visibility (transaction isolation relates to concurrent writes).

## 3. Why This Concept Exists

Business rules often require **all-or-nothing** updates (debit + credit). **Transactions** group SQL statements. **Pools** reuse expensive TCP+auth connections instead of creating one per statement.

## 4. Conceptual Explanation

**JDBC transaction:**

```java
conn.setAutoCommit(false);
try {
    // statements
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
    throw e;
} finally {
    conn.setAutoCommit(true);
}
```

**Isolation levels** (`Connection.TRANSACTION_*`):

| Level | Dirty read | Non-repeatable read | Phantom |
|-------|------------|---------------------|---------|
| READ UNCOMMITTED | possible | possible | possible |
| READ COMMITTED | no | possible | possible |
| REPEATABLE READ | no | no | possible* |
| SERIALIZABLE | no | no | no |

\*DB-specific phantom behavior — PostgreSQL RR is stricter than MySQL in places.

**Connection pool (HikariCP):** `DataSource.getConnection()` borrows from pool; `close()` returns to pool (not necessarily closing TCP). Spring Boot default: HikariCP.

## 5. Step-by-Step Examples

JPA demo wraps insert in **`em.getTransaction().begin()` / `commit()`** — resource-local JPA transaction maps to JDBC transaction under Hibernate:

**Verified jpa-h2-demo output after persist:**

```text
name=Lin
idNotNull=true
jpaH2=ok
```

JDBC-only transaction snippet (same semantics as Ch.02 connection):

```java
conn.setAutoCommit(false);
try (PreparedStatement ps = conn.prepareStatement(
        "UPDATE accounts SET balance = balance - ? WHERE id = ?")) {
    ps.setInt(1, 100);
    ps.setInt(2, 1);
    ps.executeUpdate();
    // second statement for credit...
    conn.commit();
} catch (SQLException ex) {
    conn.rollback();
    throw ex;
}
```

Not executed as standalone demo — pattern documented; use H2 integration test in your repo to verify.

## 6. Real-World Use Cases

- Transfer money across two account rows — single transaction.
- **Read-only** transactions for reporting replicas.
- Pool sizing from **latency × concurrent requests** (not “number of threads” alone).

## 7. Edge Cases

- **Long transactions** hold locks — keep short.
- **Connection leak** — missing `close()` exhausts pool (use try-with-resources).
- **Distributed transactions** (2PC) rare in microservices — saga patterns instead.

## 8. Common Mistakes

### Incorrect

`@Transactional` on private method expecting Spring proxy — does not apply (framework detail preview for Module **36**).

### Corrected

Public service methods; understand Spring AOP proxies use JDBC/JPA session binding.

## 9. Best Practices

- Default **READ COMMITTED** on many OLTP DBs; elevate only with evidence.
- Monitor pool **waiting threads** metric.
- Idempotent consumers for outbox patterns (advanced).

## 10. Chapter Summary

Transactions coordinate JDBC work; pools amortize connection cost; JPA demo shows explicit transaction boundaries on H2.

## 11. Next Steps

Module **34** — SQL and Data Access.

## Review Checklist

- [x] JPA transaction verified via jpa-h2-demo output
