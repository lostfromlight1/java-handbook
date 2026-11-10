# SOLID Deep Dive

> **Status:** Completed  
> **Module:** 42 — Clean Code and SOLID  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

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

## Review Checklist

- [x] All five principles with Java examples; links Modules 36–41
- [x] Incorrect/correct patterns; revision section
