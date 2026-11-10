# Redis and Invalidation

> **Status:** Completed  
> **Module:** 46 — Caching and Performance Engineering  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Use **Redis** as distributed cache, session store, and rate limiter—**conceptually** when Redis is not installed locally.
- Compare **Redis** to in-process **Caffeine** caches.
- Design **invalidation** across app instances and **cache stampede** controls.
- Apply **security** basics: AUTH, TLS, no sensitive data without encryption (Module 43).

## 2. Prerequisites

- Ch.01 caching strategies.
- Module 39 sessions optional read.

## 3. Why This Concept Exists

In-process caches do not **share** entries across JVMs. **Redis** (remote in-memory data store) centralizes cache, locks, and counters for clustered Java services.

## 4. Redis data structures (usage)

| Structure | Use |
|-----------|-----|
| **String** | Serialized JSON blob, counters |
| **Hash** | Fielded objects |
| **Set / ZSet** | Tags, leaderboards |
| **List** | Queues (with care) |
| **Pub/Sub** | Cache invalidation fanout (not durable log) |

Java clients: **Lettuce**, **Jedis** (not in JDK)—Spring Data Redis wraps templates.

```java
// Conceptual — requires lettuce on classpath
// redis.setex("product:42", 3600, json);
// redis.get("product:42");
```

## 5. Distributed cache-aside

Same logic as Ch.01; cache backend is Redis:

1. `GET product:42`
2. On miss, query DB, `SETEX` with TTL
3. On update, `DEL product:42` or publish invalidation

**Multiple app nodes** see same cache entries.

## 6. Local vs Redis two-level

**L1** Caffeine in JVM (microseconds) + **L2** Redis (milliseconds). Invalidate both on write.

Trade-off: complexity vs DB load.

## 7. Invalidation strategies

| Strategy | Mechanism |
|----------|-----------|
| **TTL only** | Simple; temporary staleness |
| **Delete on write** | Stronger; must hit all keys |
| **Version in key** | `product:42:v7` |
| **Pub/Sub `invalidate`** | All nodes drop local L1 |

Event-driven: `ProductUpdated` consumer deletes keys (Module 45).

## 8. Sessions and rate limiting

- **Session store:** Spring Session Redis for sticky-less scaling.
- **Rate limit:** `INCR` + TTL bucket per client id at API gateway.

Never store raw passwords or PAN in Redis; encrypt sensitive payloads if required.

## 9. Real-World Use Cases

- Flash sale: Redis holds inventory counters with Lua atomic decrement.
- Feature flags cached per tenant with 60s TTL.
- JWT blocklist (logout) with TTL = token exp.

## 10. Edge Cases

- **Redis unavailable:** degrade to DB (circuit breaker) or fail closed for non-critical reads only—product decision.
- **Large values:** >512MB strings are wrong tool—use object store.

## 11. Common Mistakes

| Mistake | Fix |
|---------|-----|
| `KEYS *` in production | `SCAN` |
| No connection pooling | Lettuce shared connection |
| Same TTL for hot and cold data | Tiered TTL |

## 12. Conceptual note

This chapter does not require a running Redis instance. Treat command shapes as **reference**; validate in staging with real cluster topology (replicas, sentinel, cluster mode).

## 13. Revision and Mastery

- When Caffeine alone enough?
- Invalidation vs TTL for prices?
- Session in Redis vs JWT trade-offs?

## 14. Exercises

1. Key naming convention for tenant-scoped cache.
2. Invalidation flow diagram for product update API.
3. Failure mode doc when Redis partition occurs.

## 15. Chapter Summary

Redis centralizes distributed caching and coordination; pair with clear invalidation, TTL discipline, and safe failure modes—not as silent source of truth without DB backing.

## 16. Next Steps

[03-load-testing-and-capacity.md](03-load-testing-and-capacity.md)

## Review Checklist

- [x] Redis conceptual; invalidation; security note
