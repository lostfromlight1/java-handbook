# DDD Basics

> **Status:** Completed  
> **Module:** 40 — Backend Architecture  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Define **bounded context**, **ubiquitous language**, **entity**, **value object**, and **aggregate**.
- Choose **aggregate roots** and enforce invariants inside them.
- Distinguish **domain events** from integration events.
- Avoid mapping every table to an anemic entity without boundaries.

## 2. Prerequisites

- Ch.01 — layered/hexagonal architecture.
- Module 15 — records for value objects.

## 3. Why This Concept Exists

Large domains (billing, shipping, catalog) overload a single “User” model with conflicting meanings. **Domain-Driven Design (DDD)** splits the model into **bounded contexts** with explicit language and **aggregates** that maintain consistency per transaction.

## 4. Strategic design

| Concept | Meaning |
|---------|---------|
| **Bounded context** | Model boundary with its own terms (Sales vs Support “Customer”) |
| **Ubiquitous language** | Terms devs and experts share in code (`Shipment`, `Fulfillment`) |
| **Context mapping** | How contexts relate (ACL, shared kernel, events) |

Not every app needs multiple contexts day one—start with clear **modules** and split when friction appears.

## 5. Tactical building blocks

### Value object (immutable)

```java
public record Money(long amountCents, Currency currency) {
    public Money {
        if (amountCents < 0) throw new IllegalArgumentException("negative");
    }
    public Money add(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("currency");
        return new Money(amountCents + other.amountCents, currency);
    }
}
```

Equality by **value**, not id—use records carefully with validation.

### Entity

Object defined by **identity** (`OrderId`) over time, not just attribute values.

### Aggregate and root

Cluster treated as **one consistency boundary**. External references only to **aggregate root** by id.

```java
public class Order { // root
    private final OrderId id;
    private final List<OrderLine> lines = new ArrayList<>();

    public void addLine(ProductRef product, int qty, Money unitPrice) {
        if (qty <= 0) throw new IllegalArgumentException("qty");
        lines.add(new OrderLine(product, qty, unitPrice));
    }

    public void cancel(String reason) {
        if (status == Status.SHIPPED) throw new IllegalStateException("shipped");
        this.status = Status.CANCELLED;
        registerEvent(new OrderCancelled(id, reason));
    }
}
```

`OrderLine` not modified from outside the aggregate.

### Domain events

`OrderCancelled` — something happened domain cares about; may publish to message bus **after** commit (outbox pattern in advanced systems).

## 6. Repositories

One per aggregate root: `OrderRepository.save(order)`, `findById(OrderId)`.

Do not expose generic `save(OrderLine)` bypassing root rules.

## 7. Real-World Use Cases

- E-commerce: `Cart` aggregate vs `Inventory` context via events.
- Banking: `Account` transfer invariant inside aggregate.

## 8. Common Mistakes

### Incorrect — giant aggregate spanning all tables

Large locks, contention—size aggregates to true invariants.

### Incorrect — DDD everywhere on CRUD admin screens

Match pattern to complexity; CRUD can stay thin.

## 9. Relation to JPA

JPA `@Entity` can implement domain entity **if** you resist exposing setters that break invariants—many teams map **domain model** ↔ **JPA entity** in infrastructure (two types).

## 10. Revision and Mastery

- Aggregate root vs entity?
- Why only reference other aggregates by id?

## 11. Exercises

1. Model `Shipment` aggregate with status transitions.
2. Find bounded contexts in a food delivery app.
3. List domain events for order lifecycle.

## 12. Chapter Summary

DDD organizes complex business into contexts, value objects, and aggregates with enforced invariants—implemented in plain Java; persistence and HTTP are adapters.

## 13. Next Steps

[03-modular-monolith-versus-microservices.md](03-modular-monolith-versus-microservices.md)

## 5. Mechanism and Execution Flow *(mandatory)*

### Aggregate consistency boundary

```text
PlaceOrder command → Order aggregate root
  → validates lines, totals
  → OrderPlaced domain event registered
  → repository.save(order)
  → events published after commit (outbox Module 45)
```

External code holds references only to **root id**, not mutable child entities.

### Bounded context integration

Context A publishes `CustomerCreated` integration event; Context B maintains **anti-corruption layer** translating into its own `Buyer` model.

## 21. FAQ — DDD *(mandatory depth)*

**Q: Aggregate size?** Small enough for one transaction; large enough to enforce rules.

**Q: Records as value objects?** Yes with compact constructor validation.

**Q: Every project needs DDD?** No—use when domain complexity warrants.

**Q: Domain events vs integration events?** Domain = inside context; integration = cross service.

**Q: UUID vs long ids?** UUID helps distributed id generation; opaque to clients still needs AuthZ.


## Depth appendix — progressive teaching (Architecture)

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

### Package dependency sketch

```text
com.app.domain        → (no spring, no jakarta.persistence)
com.app.application   → domain + ports
com.app.infrastructure → jpa, kafka, http clients
com.app.web           → controllers, dtos
```

### Transaction boundary rule

One incoming use case = one transaction unless documented saga (Module 44). Do not span transactions across REST calls inside monolith without reason.

### Extraction checklist

Before splitting a microservice: stable API, independent data, team ownership, observability SLO, failure isolation tested in staging.

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

## Production handoff — domain model *(conditional)*

- Ubiquitous language glossary linked from README.
- Aggregate boundaries reviewed with DB transaction sizes.
- Integration events versioned separately from domain events.

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Strategic/tactical terms; aggregate example; JPA note
