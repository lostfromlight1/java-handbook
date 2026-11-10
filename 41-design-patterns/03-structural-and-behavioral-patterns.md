# Structural and Behavioral Patterns

> **Status:** Completed  
> **Module:** 41 — Design Patterns  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

> **Examples verified with:** Temurin JDK 21.0.12.1 — [structural-behavioral-demo](../../examples/patterns/structural-behavioral-demo/)

## 1. Learning Objectives

- Use **Adapter**, **Decorator**, **Facade**, **Proxy** for structure.
- Apply **Strategy**, **Observer**, **Template Method**, **Command** for behavior.
- Relate **Proxy** to Spring AOP and **Strategy** to `PricingStrategy` in demo.
- Avoid pattern obsession—name patterns you already use.

## 2. Prerequisites

- Ch.01–02 — SOLID, creational patterns.
- Module 36 Ch.02 — JDK proxies.

## 3. Why This Concept Exists

Integrations expose legacy APIs; behavior varies by policy; events fan out to listeners. **Structural** patterns compose objects; **behavioral** patterns encapsulate algorithms and notifications—often as **interfaces + delegation** in Java.

## 4. Structural highlights

| Pattern | Role |
|---------|------|
| **Adapter** | Make `LegacyApi` implement `ModernClient` |
| **Decorator** | Wrap object adding behavior (I/O streams, `Collections.checkedList`) |
| **Facade** | Simplify subsystem (`OrderFacade` coordinates services) |
| **Proxy** | Control access; lazy load; AOP (Module 36) |

### Adapter (demo)

```java
ModernClient client = new LegacyAdapter(new LegacyApi());
```

## 5. Behavioral highlights

| Pattern | Role |
|---------|------|
| **Strategy** | Pluggable algorithm (`PricingStrategy`) |
| **Observer** | Publish/subscribe (`EventBus`) |
| **Template Method** | Algorithm skeleton in superclass (Module 07) |
| **Command** | Encapsulate request as object (`Runnable`, job queues) |

### Strategy + Observer (demo)

**Verified output:**

```text
adapter=v2:data
strategy=discount10
observer=events:2
patterns=ok
```

## 6. JDK and framework examples

- **Proxy:** `java.lang.reflect.Proxy`, Spring AOP.
- **Observer:** `PropertyChangeListener`, reactive streams.
- **Iterator:** `Iterable`/`Iterator` (behavioral).
- **Template method:** `AbstractList`, servlet `doGet`/`doPost` hooks.

## 7. Real-World Use Cases

- Adapter for third-party payment SDK behind `PaymentPort`.
- Strategy for tax calculation by region.
- Observer for domain events → metrics/listeners.

## 8. Common Mistakes

### Incorrect — pattern name without need

Start with interface + delegation; name pattern in review.

### Incorrect — decorator chain without clear ordering

Document wrap order for metrics vs retry.

## 9. Design Trade-offs

| Inheritance template method | Strategy composition |
|-----------------------------|----------------------|
| Fixed skeleton | Runtime algorithm swap |

Prefer composition (Module 05).

## 10. Revision and Mastery

- Adapter vs Facade?
- How is Spring MVC interceptor related to decorator/chain?

## 11. Exercises

1. Wrap demo `Greeter` with timing decorator without AOP.
2. Implement Command for undoable text edit (sketch).
3. Map `HttpClient` redirect policy to strategy.

## 12. Chapter Summary

Structural and behavioral patterns formalize composition and variation already idiomatic in Java—adapters, strategies, proxies, and observers underpin Spring and clean architecture.

## 13. Next Steps

Module **42** (per PROGRESS)—advanced topics track.

## 5. Mechanism and Execution Flow *(mandatory)*

**Adapter:** `LegacyAdapter` translates `legacy.fetch()` into `ModernClient.getData()` so use cases depend on modern port.

**Strategy:** `PricingStrategy` injected at runtime—`Checkout` never branches on discount type.

**Observer:** `EventBus` notifies subscribers after domain change—decouples metrics/email.

**Verified (Temurin 21.0.12.1, 2026-11-11):** `adapter=v2:data`, `strategy=discount10`, `observer=events:2`, `patterns=ok`.

## 21. FAQ — Structural/behavioral *(mandatory depth)*

**Q: Adapter vs Facade?** Adapter fixes one interface mismatch; Facade simplifies whole subsystem API.

**Q: Proxy without Spring?** JDK `Proxy.newProxyInstance` for interfaces.

**Q: Observer vs messaging?** In-process bus vs broker for cross JVM (Module 45).

**Q: Command pattern?** `Runnable` queues, job tables.

**Q: Decorator order?** Metrics outside retry or inside—document wrap chain.


## Depth appendix — progressive teaching (Design patterns)

### Incorrect vs corrected patterns

Documenting failure modes accelerates review. Each pair below states **why** the wrong approach breaks client or operator expectations.

### Operational checklist before marking API complete

- OpenAPI matches deployed routes (Ch.03).
- Error envelope consistent (ProblemDetail or documented custom schema).
- AuthN/Z enforced on mutations (Module 39).
- Examples in `examples/` compiled on Temurin 21.0.12.1 with recorded output.
- Versioning/deprecation policy published for partners.

### Revision drills (Track B)

1. Explain one design trade-off you reversed after load testing.
2. Write three MockMvc or plain HTTP negative tests not in the happy path.
3. Identify one boundary where DTO mapping must never leak entities.

### References (official hierarchy)

- RFC 9110 — HTTP semantics (methods, status).
- Jakarta EE / Spring reference docs for Illustrative framework binding only.
- OWASP ASVS — verification levels for security chapters (Module 43).

**Mechanism merge note:** Where earlier sections already diagram execution flow, this appendix reinforces **operational** depth without duplicating definitions.

### Pattern selection heuristic

| Symptom | Consider |
|---------|----------|
| Switch on type grows | Strategy or factory |
| Legacy API mismatch | Adapter |
| Cross-cutting concern | Decorator or AOP (Module 36) |
| One-to-many notification | Observer or events (Module 45) |

### Testing patterns

Fakes implement ports (DIP) without heavyweight pattern names—tests stay readable.

### Anti-pattern: pattern slurry

Three patterns in one 40-line class—simplify first, name pattern in review after clarity exists.

## Technical L3 supplement — teaching depth

### Motivation recap (concrete scenario)

A team ships a Java 21 service without documented HTTP contracts, security rules, or messaging guarantees. Mobile clients cache error shapes; a deploy renames a field and checkout silently fails. Operations cannot trace requests across services. The material in this module prevents that class of failure by making **boundaries explicit**: REST semantics, DTOs, auth filters, architecture ports, patterns, clean code, security controls, resilience, and delivery semantics.

### Mechanism trace (how this chapter shows up at runtime)

1. **Ingress** — HTTP or message enters an adapter (controller, listener).
2. **Policy** — auth, validation, and rate limits run before domain logic (fail closed).
3. **Application** — use case coordinates domain + ports inside a transaction or saga step.
4. **Egress** — DTO mapping, event publish (outbox), or outbound HttpClient with timeouts.
5. **Observation** — structured logs with correlation id; metrics on error rate (Module 47).

This sequence is framework-agnostic; Spring/Boot annotations are **Illustrative** wiring for the same pipeline taught in earlier modules.

### Progressive example ladder

| Stage | Focus | Verification label |
|-------|--------|-------------------|
| Minimal | JDK-only demo or pure unit logic | Verified / Compile-verified |
| Realistic | Layered service with DTO + port | Illustrative Spring config |
| Advanced | Failure modes: timeout, duplicate message, 409 conflict | Test + documented output |

Never label **Verified** without Temurin 21.0.12.1 compile/run recorded in chapter or `PROGRESS.md`.

### Wrong vs right (review board)

| Wrong | Why it hurts | Right |
|-------|----------------|-------|
| Skip server AuthZ | IDOR incidents | Check principal vs resource every mutation |
| Expose JPA graph in JSON | Lazy-load storms, schema coupling | Map to DTO in service |
| Infinite HttpClient wait | Thread pile-up, incident duration | Connect + request timeouts |
| Retry all POSTs | Duplicate charges | Idempotency keys + dedup store |
| Dual write DB + broker | Lost or phantom events | Transactional outbox |

### Design tensions engineers negotiate

- **Consistency vs availability** during partitions (Module 44).
- **Stateless JWT vs revocable sessions** (Module 39).
- **Modular monolith vs microservices** operational cost (Module 40).
- **Kafka vs Rabbit** retention and routing (Module 45).
- **Strict SOLID vs delivery speed** (Modules 41–42).

Document the choice in ADRs; the handbook gives options, not one universal answer.

### Java 21 notes

- **Records** for immutable DTOs and events (`Module 08`, `15`).
- **Virtual threads** improve blocking HTTP and JDBC throughput but do not remove timeouts or auth checks.
- **Pattern matching** and **sealed types** clarify domain result models (`PaymentResult` in SOLID chapters).

### Compatibility and sources

Prefer **RFC 9110** for HTTP, **RFC 7807** for errors, **OWASP ASVS** for verification language, and vendor docs for brokers labeled Illustrative. Distinguish **JLS/API guarantees** from **Spring/HotSpot** behavior as in `DOCUMENTATION-STANDARDS.md`.

### Track B — mastery questions

1. Where is the transaction boundary in your current project, and why?
2. Which dependency would you invert first behind a port for testing?
3. What status code and error shape would clients see for your most common validation failure?
4. How would you prove an idempotent consumer in tests?
5. What metric would fire before users notice a downstream slowdown?

### Track B — field exercise (no solution in chapter)

Implement or extend one `examples/` demo for this module: add a negative-path assertion (4xx, breaker OPEN, or rejected input), compile with `javac -source 21`, run on Temurin 21.0.12.1, paste output into `PROGRESS.md` only after a real run.

### Cross-module links

| Need | Module |
|------|--------|
| HTTP client timeouts | 32 |
| JDBC transactions | 33–35 |
| Spring DI / Security | 36–37 |
| Testing & CI | 30 |
| Observability | 47 |

### Glossary touchpoints

Add unfamiliar terms encountered here to `GLOSSARY.md` when stabilizing definitions across the handbook (repository workflow in java-documentation skill).

### Explicit omissions (this supplement)

- Vendor-specific console screenshots and cloud provisioning scripts.
- Exploit reproduction steps (defensive security only in Module 43).
- Exhaustive broker tuning guides (operational manuals remain external).

## Combining patterns in one flow

```text
HTTP adapter → Facade → Strategy pricing → Observer metrics → Repository adapter
```

Each pattern name marks a **role**, not a required class hierarchy. Start with interfaces and delegation; refactor to named patterns when duplication appears.

### Collections framework as behavioral patterns

`Iterator`, `Comparator`, `Comparator.comparing` — study JDK before reinventing in domain code.

### AOP as proxy stack (Module 36)

Spring wraps beans with **transaction** + **security** proxies—conceptually a **decorator chain**. Order matters: security before transactional boundary or vice versa—document team convention.

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Verified structural-behavioral-demo; JDK/Spring ties
