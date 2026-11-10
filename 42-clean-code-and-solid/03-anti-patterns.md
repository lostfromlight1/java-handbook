# Anti-Patterns

> **Status:** Completed  
> **Module:** 42 — Clean Code and SOLID  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Recognize **anti-patterns** that rot Java backends: God object, spaghetti, anemic domain, golden hammer.
- Map symptoms to **remediation** without big-bang rewrites.
- Connect anti-patterns to **observability** and **incident** costs (Module 47).

## 2. Prerequisites

- Chapters 01–02 in this module; Module 40 architecture.
- Module 33–35 — persistence patterns.

## 3. Why This Concept Exists

Anti-patterns name **recurring bad solutions** that look productive short-term. Labeling them speeds retrospectives and hiring interviews beyond vague “messy code.”

## 4. Conceptual Explanation

An **anti-pattern** is a common response to a problem that **creates more problems** than it solves. Unlike a one-off bug, it is a **habit** or **structure**.

## 5. Catalog — backend and Java

| Anti-pattern | Symptoms | Remediation |
|--------------|----------|-------------|
| **God object / God service** | `ApplicationService` 3k lines, 80 dependencies | Extract use cases; SRP; module boundaries |
| **Spaghetti code** | No layers; JDBC in controllers | Layered/hexagonal; ArchUnit rules |
| **Anemic domain model** | Entities = getters/setters only | Rich behavior on aggregates where rules live |
| **Golden hammer** | Every problem → new microservice | Modular monolith first (Module 40) |
| **Boat anchor** | Dead code “for later” | Delete with VCS history |
| **Copy-paste programming** | Divergent validation rules | Single validator; DRY with care |
| **Primitive obsession** | `String userId` everywhere | Value types (`UserId`, `Money`) |
| **Singleton abuse** | Global mutable `INSTANCE` | DI scoped beans; test doubles |
| **Exception as flow control** | Expected path uses exceptions | Result types / Optional at boundaries |
| **Logging as println debugging** | Unstructured noise in prod | Structured logs + correlation ids (Module 47) |

## 6. God Service — deep dive

```java
// Smell: one class knows HTTP, SQL, Kafka, and PDF
public class OrderApplicationService {
    public ResponseEntity<?> placeOrder(...) { /* jdbc + email + metrics */ }
}
```

**Why it fails:** merge conflicts, untestable combos, unclear transaction boundaries.

**Fix path:** extract `PlaceOrderUseCase`, `OrderRepository` port, `OrderPlacedNotifier` adapter; keep transactions on use case façade.

## 7. Anemic domain — deep dive

```java
public class Order {
    private String status;
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
// OrderService.cancel sets status = "CANCELLED" without invariants
```

**Better (when rules exist):**

```java
public void cancel(String reason) {
    if (!isCancellable()) throw new IllegalStateException("state=" + status);
    this.status = Status.CANCELLED;
    this.cancelReason = reason;
}
```

Not every CRUD app needs DDD; add richness when **invariants** and **state machines** appear.

## 8. Golden hammer microservices

Symptoms: network chattiness, distributed monolith, sagas for data that lived in one transaction (Module 44).

**Alternative:** modular monolith with clear packages; extract service when **scaling or team boundary** justifies cost.

## 9. Real-World Use Cases

- **E-commerce checkout:** God service caused double-charge bugs; split payment port.
- **Reporting:** Copy-paste SQL → shared repository + tested queries.
- **Startup scale-up:** Singleton config holder blocked parallel tests → Spring `@ConfigurationProperties`.

## 10. Edge Cases

- **Acceptable duplication** across bounded contexts (Module 40 DDD)—not all duplication is DRY violation.
- **Framework-generated glue** (Spring Data) is not automatically anemic if domain rules sit in services **on purpose**.

## 11. Common Mistakes in remediation

### Incorrect — rewrite entire system

Big bang fails; use **strangler fig** on one use case.

### Correct — characterize + slice

Wrap legacy behind port; new path uses clean slice; migrate traffic gradually.

## 12. Performance and observability

God objects hide **N+1 queries** and **missing metrics**. Splitting responsibilities makes SLIs per use case possible (Module 47).

## 13. Revision and Mastery

- Difference between anti-pattern and simple mistake?
- Name anti-pattern in “static utility does everything.”
- How does primitive obsession hurt security validation?

## 14. Exercises

1. Annotate a pseudo codebase diagram with three anti-patterns and fixes.
2. Convert anemic `Order` to cancellation with invariants.
3. Propose module boundary to split God service.

## 15. Chapter Summary

Anti-patterns are organizational habits as much as code shapes—recognize God services, anemic models, and distribution for fashion; remediate with boundaries, ports, and incremental strangler moves.

## 16. References

- Brown et al., *AntiPatterns* — catalog mindset.
- Module 41 patterns for **positive** replacements (Strategy, Adapter).

## 5. Mechanism and Execution Flow *(mandatory)*

Anti-patterns accumulate via **shortcut loops**: deadline → god service → copy-paste validation → silent incidents → longer deadlines. Naming the pattern breaks the loop in retros.

Remediation always starts with **characterization tests** then extract use cases/ports (Modules 30, 40).

## 21. FAQ — Anti-patterns *(mandatory depth)*

**Q: Golden hammer microservices symptom?** Network chatter, shared DB, no team autonomy.

**Q: Anemic domain when ok?** Simple CRUD admin with no rules.

**Q: Primitive obsession fix cost?** Introduce value types at boundaries first.

**Q: Exception flow control?** Use domain results for expected cases.

**Q: Logging anti-pattern fix?** Structured JSON logs + correlation id.


## Depth appendix — progressive teaching (Anti-patterns)

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

### Incident-driven catalog entry

When sev-1 root cause is God service, file ADR: split use cases, add ArchUnit rule, schedule refactor budget.

### Metrics that reveal smells

- Cyclomatic complexity trending up on one class.
- PRs always touching same file.
- Test coverage low on `OrderApplicationService`.

### Gradual remediation

Strangler inside monolith: new feature in `com.app.orders.v2` package; old path frozen except bugfix.

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

## Refactoring sprint template *(conditional)*

1. Pick one anti-pattern from retro.
2. Add characterization test.
3. Extract port or use case.
4. Measure complexity before/after.

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Table + deep dives; remediation paths; no exploit content
