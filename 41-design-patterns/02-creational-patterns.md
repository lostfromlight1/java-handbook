# Creational Patterns

> **Status:** Completed  
> **Module:** 41 — Design Patterns  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

> **Examples verified with:** Temurin JDK 21.0.12.1 — [creational-demo](../../examples/patterns/creational-demo/)

## 1. Learning Objectives

- Apply **factory method**, **abstract factory**, **builder**, and **singleton** (cautiously) in Java.
- Prefer **constructors/factories** over excessive pattern ceremony.
- Relate factories to Spring `@Bean` methods (composition root creates objects).

## 2. Prerequisites

- Ch.01 — SOLID, especially D and O.
- Module 06 — constructors, static factories.

## 3. Why This Concept Exists

Object creation sprawls (`new` everywhere) hides **which implementation** runs and complicates testing. **Creational patterns** centralize creation while keeping callers on stable types.

## 4. Patterns

### Factory method

Subclass or factory class decides implementation:

```java
Exporter ex = ExporterFactory.create(ExportKind.CSV);
```

### Abstract factory (conceptual)

Family of related products (`WidgetFactory` → `Button` + `Checkbox`). Use when you must swap whole UI kits; otherwise YAGNI.

### Builder

Step-by-step construction for many optional fields:

```java
Report report = Report.builder().title("T").rowCount(2).build();
```

Java **records** and compact constructors cover small cases; builders help when validation chains grow.

### Singleton

One instance per JVM (`enum` idiom). **Danger:** global state, testing pain. Prefer **scoped singleton** in DI container over hand-rolled `getInstance()`.

## 5. Runnable demo

[creational-demo](../../examples/patterns/creational-demo/)

**Verified output:**

```text
factory=csv:rows
builder=Report{title=T,rows=2}
creational=ok
```

## 6. Spring connection (conceptual)

`@Bean` methods are **factory methods** in the composition root—container manages singleton scope.

## 7. Real-World Use Cases

- `ExporterFactory` by file format.
- `HttpClient` builder (JDK) for timeouts/TLS.
- Test fixtures building complex domain aggregates.

## 8. Common Mistakes

### Incorrect — singleton for every service

Use DI lifecycle instead.

### Incorrect — builder for two-field immutable type

Use record.

## 9. Revision and Mastery

- Factory method vs abstract factory?
- Why is enum singleton preferred over double-checked locking?

## 10. Exercises

1. Add `JSON` exporter to demo factory without changing `Checkout`.
2. When would you use `Optional` factory vs exception?
3. Map Spring `@Bean` to pattern name.

## 11. Chapter Summary

Creational patterns concentrate object birth decisions; the demo shows factory and builder plainly. Spring `@Bean` registration is enterprise-scale factory wiring at the root.

## 12. Next Steps

[03-structural-and-behavioral-patterns.md](03-structural-and-behavioral-patterns.md)

## 5. Mechanism and Execution Flow *(mandatory)*

**Factory method flow:** caller requests `ExportKind.CSV` → factory selects `CsvExporter` → returns `Exporter` interface → caller writes rows without `new` at call site.

**Builder flow:** validate optional fields stepwise → `Report.builder()` accumulates → `build()` enforces invariants.

**Verified run (Temurin 21.0.12.1, 2026-11-11):** `factory=csv:rows`, `builder=Report{title=T,rows=2}`, `creational=ok`.

```bash
cd examples/patterns/creational-demo
javac -d out -source 21 -target 21 *.java && java -cp out CreationalDemo
```

## 21. FAQ — Creational patterns *(mandatory depth)*

**Q: Spring `@Bean` pattern name?** Factory method at composition root.

**Q: Singleton enum?** Prefer over double-checked locking for true JVM singletons.

**Q: Builder vs telescoping constructor?** Builder when many optional fields.

**Q: Abstract factory cost?** Only when product families swap together.

**Q: Test factories?** Object mothers in test sources—keep out of production domain.


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

## Registry and service-loader variants *(conditional)*

**ServiceLoader** (`META-INF/services`) discovers `Exporter` implementations on the classpath—JDK mechanism predating Spring. Prefer explicit factory registration when discovery magic obscures startup graphs.

### Thread-safety of builders

Document whether `Report.Builder` is **not thread-safe** (typical)—one builder per constructing thread.

### Test doubles via factories

```java
Exporter csv = ExporterFactory.create(ExportKind.CSV);
// tests may inject ExporterFactory with lambda: kind -> fake
```

Factory indirection keeps production `new` statements out of domain code.

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Verified creational-demo; singleton warning
