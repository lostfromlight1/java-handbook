# Caching Strategies

> **Status:** Completed  
> **Module:** 46 — Caching and Performance Engineering  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Choose **cache-aside**, **read-through**, **write-through**, and **write-behind** patterns.
- Understand **TTL**, **staleness**, and **thundering herd**.
- Run [InMemoryCacheDemo](../../examples/caching/in-memory-cache-demo/).
- Relate caching to JPA **second-level cache** awareness (Module 35).

## 2. Prerequisites

- Modules 33–35 — databases.
- Module 14 — concurrency.

## 3. Why This Concept Exists

Repeated identical reads (product catalog, config, reference data) waste DB CPU and add latency. Caches trade **memory** for **speed**—with explicit consistency cost.

## 4. Pattern catalog

| Pattern | Flow | When |
|---------|------|------|
| **Cache-aside** | App reads cache; on miss loads DB and fills cache | Most common |
| **Read-through** | Cache library loads on miss | Embedded caches (Caffeine, Ehcache) |
| **Write-through** | Write goes to cache + DB synchronously | Stronger consistency |
| **Write-behind** | Write to cache; async flush to DB | High write throughput; risk on crash |

## 5. Cache-aside (Java sketch)

```java
Optional<Product> getProduct(String id) {
    return cache.get(id).or(() -> {
        Product p = repository.findById(id);
        if (p != null) cache.put(id, p);
        return Optional.ofNullable(p);
    });
}
```

Demo `LruCache` evicts LRU entry when capacity exceeded—`user:2=miss` after inserting third key.

## 6. TTL and invalidation

- **TTL:** absolute freshness bound; simple but stale window.
- **Explicit invalidation:** on update/delete remove key—harder but accurate.
- **Versioned keys:** `product:123:v5` after update bump.

## 7. Thundering herd

Many threads miss same hot key simultaneously—all hit DB.

Mitigations:

- **Single-flight** lock per key (only one loader).
- **Probabilistic early expiration** (jitter).
- **Request coalescing** in cache library.

## 8. What to cache

**Good:** read-heavy, tolerate staleness, expensive queries.

**Poor:** user-specific security decisions, rapidly mutating inventory without invalidation discipline.

## 9. Real-World Use Cases

- CDN + app cache for product images metadata.
- Reference data (`country codes`) with 24h TTL.
- Spring `@Cacheable` on idempotent read services.

## 10. Edge Cases

- **Null caching:** cache “not found” briefly to protect DB from scans.
- **Large objects:** memory pressure—compress or store ids only.

## 11. Common Mistakes

| Mistake | Effect |
|---------|--------|
| Cache without eviction | OOM |
| Shared mutable objects in cache | Race corruption |
| No metrics | Silent low hit rate |

## 12. Revision and Mastery

- Cache-aside vs write-through for price updates?
- Explain demo LRU eviction.
- Thundering herd on hot product launch?

## 13. Exercises

1. Add TTL map wrapper around `LruCache`.
2. Design invalidation on `ProductUpdated` event (Module 45).
3. Estimate memory for 100k cached DTOs.

## 14. Chapter Summary

Caching strategies balance hit rate, staleness, and invalidation complexity—start with cache-aside, measure, and guard hot keys from stampedes.

## 15. Next Steps

[02-redis-and-invalidation.md](02-redis-and-invalidation.md)

## Review Checklist

- [x] Patterns; demo verified; herd mitigation
