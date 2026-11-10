# Anti-Patterns

> **Status:** Completed  
> **Module:** 42 — Clean Code and SOLID  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

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

## Review Checklist

- [x] Table + deep dives; remediation paths; no exploit content
