# Spring and System Design Interviews

> **Status:** Completed  
> **Module:** 52 — Interview Preparation  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Answer **Spring Framework / Boot** questions on DI, scopes, transactions, and MVC flow.
- Structure **system design** responses for Java backends (REST, JDBC, cache, queue).
- Relate **security**, **scalability**, and **data** choices to handbook modules.
- Use **trade-off narration** instead of buzzword lists.

## 2. Prerequisites

- **Modules 36–40** — Spring, REST, auth, architecture.
- **Module 52 Ch.01–02** — Java/JVM baseline.

## 3. Why This Concept Exists

Many Java roles blend **framework fluency** with **design** questions (“design URL shortener”, “ticket booking”). Interviewers want clear APIs, data model, failure handling, and awareness of Spring’s proxy-based tx/AOP limits.

## 4. Spring Interview Topics

| Topic | Depth |
|-------|-------|
| DI | Constructor vs field; `@Component` scanning |
| Bean scope | singleton vs prototype trap (M36) |
| `@Transactional` | Proxy boundaries; rollback rules |
| MVC flow | DispatcherServlet → controller → service → repo |
| Spring Boot | auto-config idea; profiles; Actuator |
| Security filter chain | order; stateless JWT vs session (M39) |

**Q: Why constructor injection?**  
Immutability, testability, required dependencies explicit (M36).

**Q: Self-invocation breaks `@Transactional` — why?**  
Call bypasses proxy; needs external bean call (M36 Ch.02).

## 5. System Design Framework

Use **CIDR**-style structure (adapt as needed):

1. **Clarify** — scale, SLA, read/write ratio, consistency.
2. **API** — REST resources, idempotency keys (M38).
3. **Data** — schema, indexes, caching, partitioning (M33–34).
4. **Components** — JVM services, DB, cache, queue.
5. **Deep dives** — hot path, failure modes, observability (M47).

Example prompt: **Rate-limited public API**

- API gateway or filter; token bucket (Module 39 auth).
- JDBC write path + read replica for queries.
- Horizontal scale stateless Boot pods (M49).
- Metrics on 429 rate and latency p99.

## 6. Trade-off Tables (interview habit)

| Choice | Pro | Con |
|--------|-----|-----|
| Monolith | Simpler deploy | Scale limits |
| Cache-aside Redis | Fast reads | Invalidation complexity |
| Sync REST | Simple | Coupling latency |

## 7. Common Mistakes

- Describing only happy path without DB or cache failure.
- Saying “use microservices” without boundary justification (M40).
- Ignoring idempotency for payment POST.

## 8. Revision and Mastery

- Draw Spring request lifecycle for one `@GetMapping`.
- 15-minute outline: “notification service” with email + DB queue.

## 9. Exercises

1. Mock interview: 25 min system design + 5 min questions.
2. Map three Spring annotations to underlying Java mechanism (reflection/proxy).

## 10. Chapter Summary

Spring interviews test DI, proxies, transactions, and web flow; system design interviews test REST/data/scale reasoning using Modules 36–40 and cloud-native themes from 48–49.

## Review Checklist

- [x] Spring topics; design framework; trade-offs
