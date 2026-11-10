# Distributed Fundamentals

> **Status:** Completed  
> **Module:** 44 — Microservices and Distributed Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Explain **CAP**, **network partitions**, and **latency** in distributed Java systems.
- Compare **monolith**, **modular monolith**, and **microservices** (Module 40).
- Model **synchronous** vs **asynchronous** integration trade-offs.
- Identify **failure modes**: timeouts, partial failures, cascading errors.

## 2. Prerequisites

- Modules 32, 40 — HTTP, architecture.
- Module 14 — concurrency basics.

## 3. Why This Concept Exists

Splitting a system across processes and networks **removes single-machine assumptions**. Transactions, exceptions, and stack traces no longer span one JVM. Fundamentals prevent designing **distributed monoliths** that are harder than one well-structured deployable.

## 4. Conceptual Explanation

**Distributed system:** independent nodes communicate via messages (HTTP, gRPC, queues) without shared memory.

| Property | Single JVM | Distributed |
|----------|------------|-------------|
| **Consistency** | Strong within one DB transaction (often) | Requires explicit protocol |
| **Failure detection** | Exception | Timeout, ambiguous result |
| **Latency** | Microseconds–ms | Milliseconds–seconds + tail latency |

## 5. CAP (practical framing)

In a **partition**, choose between **availability** and **strong consistency** for a given operation—not a license to ignore both.

Most product services pick:

- **AP** for reads with eventual consistency (catalog cache).
- **CP** for financial ledger writes (reject or queue during partition).

Java services implement this via **database choice**, **read models**, and **sagas** (Ch.03)—not magic in Spring.

## 6. Fallacies of distributed computing

1. The network is reliable.
2. Latency is zero.
3. Bandwidth is infinite.
4. The topology does not change.
5. There is one administrator.
6. Transport cost is zero.
7. The network is homogeneous.

Every HTTP client needs **timeouts**, **retries with caps**, and **idempotency** keys for writes.

## 7. Integration styles

| Style | Pros | Cons |
|-------|------|------|
| **Sync REST/gRPC** | Simple mental model | Couples availability; tail latency stacks |
| **Async messaging** | Decouples peaks | Delivery semantics complexity (Module 45) |
| **Shared database** | Easy queries | Tight coupling—anti-pattern across services |

## 8. Service boundaries

Align with **bounded contexts** (Module 40). Bad boundary: “UserService” split by technical layer. Better: **Billing** vs **Shipping** with owned data stores.

**Database per service** is ideal; **schema per service** on shared cluster is a stepping stone.

## 9. Observability requirement

Distributed systems need **correlation ids** across calls (Module 47). Without them, incidents are unreadable.

## 10. Real-World Use Cases

- E-commerce: order service owns orders; inventory service owns stock counts.
- Read replicas for product search; write master for checkout.
- Feature team autonomy with independent release cadence.

## 11. Edge Cases

- **Dual writes** without saga: inconsistent inventory and payment.
- **Clock skew:** do not rely on `Instant.now()` across nodes for ordering—use DB or logical clocks.

## 12. Common Mistakes

- Microservices before **modular monolith** discipline exists.
- No **timeout** on `HttpClient.send`.
- Distributed **2PC** without operational maturity.

## 13. Revision and Mastery

- Why can’t a thrown exception prove remote rollback?
- CAP during network partition—example product choice?
- Sync vs async for order confirmation email?

## 14. Exercises

1. Diagram three services with owned databases and one anti-pattern shared DB.
2. List timeouts for a checkout fan-out of four HTTP calls.
3. Map Module 40 modular monolith packages to potential service cuts.

## 15. Chapter Summary

Distributed Java backends trade local simplicity for explicit consistency, failure, and latency handling—bounded contexts and messaging choices matter more than deployment count.

## 16. Next Steps

[02-resilience-patterns.md](02-resilience-patterns.md)

## Review Checklist

- [x] CAP, fallacies, integration table; links Module 40
