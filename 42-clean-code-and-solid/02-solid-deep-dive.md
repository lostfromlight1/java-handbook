# SOLID Deep Dive

> **Status:** Completed  
> **Module:** 42 — Clean Code and SOLID  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Apply each **SOLID** principle with Java 21 patterns (records, sealed types, DI).
- Detect violations in layered and hexagonal code (Modules 40, 36).
- Balance SOLID with **YAGNI** on small codebases.
- Relate **LSP** to generics and collections (Modules 07, 11).

## 2. Prerequisites

- Module 41 Ch.01 — SOLID overview.
- Module 36 — dependency injection and ports.
- Module 07 — inheritance and substitution.

## 3. Why This Concept Exists

SOLID names **recurring failure modes**: blobs of responsibility, fragile inheritance, untestable concrete coupling. Without shared vocabulary, reviews argue taste instead of **coupling and change cost**.

## 4. Single Responsibility (SRP)

**Definition:** A type should have **one reason to change**—one stakeholder or axis of evolution.

Not “one method only.” A `PdfInvoiceRenderer` changes when PDF layout rules change; `InvoiceCalculator` changes when tax law changes.

```java
public final class InvoiceCalculator {
    public Money totalWithTax(Invoice invoice, TaxTable tax) { /* ... */ }
}

public final class PdfInvoiceRenderer {
    public byte[] render(Invoice invoice) { /* ... */ }
}
```

**Smell:** class imports both `java.sql` and `jakarta.mail`.

## 5. Open/Closed (OCP)

**Definition:** Extend behavior **without editing** stable core code—usually via **new types** implementing an interface.

Module 41 `ExporterFactory`: new exporter = new class + registry entry, not `switch` edits everywhere.

```java
public interface PricingStrategy {
    Money price(LineItem item);
}

public final class Checkout {
    private final PricingStrategy pricing;
    public Checkout(PricingStrategy pricing) { this.pricing = pricing; }
    public Money quote(LineItem item) { return pricing.price(item); }
}
```

**Trade-off:** indirection vs switch explosion.

## 6. Liskov Substitution (LSP)

**Definition:** Subtypes must honor the **contract** callers expect of the supertype—preconditions, postconditions, exceptions.

**Java example:** overriding `List` to throw on `add` breaks callers using `List` as mutable.

**Sealed hierarchies** (Java 17+) document allowed subtypes:

```java
public sealed interface PaymentResult permits Approved, Declined { }
```

### Incorrect — strengthened precondition

```java
class AdminUser extends User {
    @Override
    public void setEmail(String email) {
        if (!email.endsWith("@corp.com")) throw new IllegalArgumentException();
        super.setEmail(email);
    }
}
```

If `User.setEmail` accepted any valid email, callers passing a `AdminUser` where `User` is expected may fail unexpectedly—document or redesign (composition).

## 7. Interface Segregation (ISP)

**Definition:** Clients should not depend on methods they do not use.

Split fat ports:

```java
public interface OrderQueries { Optional<Order> findById(UUID id); }
public interface OrderCommands { void place(Order order); }
```

Spring Data sometimes merges these; in domain cores, segregation keeps fakes small in tests.

## 8. Dependency Inversion (DIP)

**Definition:** High-level policy depends on **abstractions**; details implement them.

```java
public final class PlaceOrderUseCase {
    private final OrderRepository orders;
    private final PaymentPort payments;
    public PlaceOrderUseCase(OrderRepository orders, PaymentPort payments) {
        this.orders = orders;
        this.payments = payments;
    }
}
```

`JpaOrderRepository` and `InMemoryOrderRepository` are adapters (Module 40).

**DIP ≠ every class needs an interface:** invert at **instability boundaries** (DB, HTTP, clock).

## 9. Progressive example — order placement

1. **Minimal:** `new StripeClient()` inside use case (fast, untestable).
2. **Realistic:** `PaymentPort` + constructor injection (Module 36).
3. **Advanced:** fake clock + fake port in JUnit (Module 30).

## 10. Real-World Use Cases

- **Feature flags:** OCP via strategy for rollout providers.
- **Multi-region tax:** SRP separates `TaxTable` loader from calculator.
- **Read replicas:** ISP splits read vs write repositories.

## 11. Edge Cases

- **Spring `@Service` concrete classes:** DIP satisfied by **injecting interfaces** at edges; core need not mirror every bean.
- **Records as DTOs:** SRP for **immutable data**; behavior stays in services or domain.

## 12. Common Mistakes

| Mistake | Fix |
|---------|-----|
| Interface per class by default | Introduce port when second implementation or test fake appears |
| Inheritance for reuse only | Favor composition (`WrapsLegacyApi`) |
| God interface `Repository` with 30 methods | Split by client needs |

## 13. Best Practices

- **ArchUnit** (optional, Module 40): forbid `domain` → `infrastructure` imports.
- Code review asks: **which SOLID pressure** does this change address?

## 14. Design Trade-offs

Strict SOLID in a 500-line service is overhead. In a 50-person billing domain, **OCP and DIP** pay rent quickly.

## 15. Security Considerations

DIP helps **swap** crypto providers and token validators without touching business rules (Module 43).

## 16. Revision and Mastery

- Explain LSP with a JDK collection example.
- Where does Spring `@Transactional` sit in SRP?
- DIP vs “inject concrete HikariDataSource”—when is concrete OK?

## 17. Exercises

1. Draw dependencies for hexagonal order service labeling S/O/L/I/D.
2. Find LSP risk in a class overriding `equals` without `hashCode`.
3. Split one “Repository” interface into query/command ports.

## 18. Chapter Summary

SOLID in Java means cohesive types, extension via new implementations, honest subtypes, lean interfaces, and ports at technology boundaries—applied with judgment, not ceremony.

## 19. Next Steps

[03-anti-patterns.md](03-anti-patterns.md)

## 5. Mechanism and Execution Flow *(mandatory)*

Each SOLID letter addresses a **failure mode** discovered during change: SRP splits reasons to change; OCP adds types instead of editing switches; LSP keeps substitutability; ISP shrinks interfaces; DIP aims adapters at ports.

Place-order example progression (Ch.09): inline `new StripeClient` → constructor-injected `PaymentPort` → JUnit with fake port.

## 21. FAQ — SOLID deep dive *(mandatory depth)*

**Q: DIP without interface on use case?** Invert dependencies that touch DB/HTTP, not every helper.

**Q: LSP and exceptions?** Subtypes should not widen thrown checked exceptions unexpectedly.

**Q: ISP in Spring Data?** Query interfaces may split for test fakes.

**Q: SRP for DTO records?** Data only—behavior elsewhere.

**Q: ArchUnit + SOLID?** Automate dependency direction rules.


## Depth appendix — progressive teaching (SOLID)

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

### Code review SOLID prompts

- **S:** List two reasons this class might change—should it split?
- **O:** Where would you add a new variant without editing existing switch?
- **L:** Can every subtype pass through the superclass type in tests?
- **I:** Does this interface have unused methods for some clients?
- **D:** What concrete dependency should become a port?

### Spring `@Transactional` placement

Keep transactional boundaries on **application services**, not controllers or repositories, to preserve SRP and clear rollback rules.

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

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] All five principles with Java examples; links Modules 36–41
- [x] Incorrect/correct patterns; revision section
