# SOLID and Principles

> **Status:** Completed  
> **Module:** 41 — Design Patterns  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Apply **SOLID** principles with Java examples.
- Relate **composition over inheritance** and **program to interfaces** to Module 36 DI.
- Recognize **DRY**, **KISS**, **YAGNI** as balance, not dogma.
- Connect **LSP** to Module 07 dynamic dispatch.

## 2. Prerequisites

- Modules 05–07 — OOP, inheritance, interfaces.
- Module 36 — dependency injection.

## 3. Why This Concept Exists

Codebases rot when classes **do too much**, **depend on concrete details**, or **subtypes break callers**. SOLID and related principles give **review vocabulary** and design heuristics for maintainable Java services.

## 4. SOLID

| Letter | Principle | Java sketch |
|--------|-----------|-------------|
| **S** | Single Responsibility | `InvoicePdfRenderer` separate from `InvoiceCalculator` |
| **O** | Open/Closed | Add `PricingStrategy` impl without editing `Checkout` (see patterns demo) |
| **L** | Liskov Substitution | Subtypes honor superclass contracts; no surprising exceptions |
| **I** | Interface Segregation | Split fat `Repository` into `UserQueries` + `UserCommands` if needed |
| **D** | Dependency Inversion | High-level `OrderService` depends on `PaymentPort`, not `StripeClient` |

### Dependency Inversion ↔ Spring

Module 36: bind `PaymentPort` → `StripeAdapter` in container. **D** is the principle; **DI** is the technique.

### LSP example

If `List` subclass throws on `add` where callers expect mutability, substitutability breaks—prefer composition or document constraints.

## 5. Other principles

- **DRY** — one authoritative rule; duplicated validation invites drift.
- **KISS** — simplest design meeting requirements.
- **YAGNI** — do not build abstract factories for one implementation today (patterns in Ch.02–03 when earned).

## 6. Real-World Use Cases

- Extract class when service exceeds ~300 lines or mixed concerns (HTTP + SQL + email).
- Ports/adapters (Module 40) embody **D** and **O**.

## 7. Common Mistakes

### Incorrect — interface per class “for SOLID”

Unnecessary indirection; introduce ports at stability boundaries.

### Incorrect — inheritance for code reuse only

Favors fragile base classes; use composition.

## 8. Design Trade-offs

| Strict SRP | Pragmatic merge |
|------------|-----------------|
| Many small types | Fewer files for CRUD |

Tune to team and domain complexity.

## 9. Revision and Mastery

- Give LSP violation example from JDK or your code.
- How does OCP show up in `ExporterFactory` demo?

## 10. Exercises

1. Refactor pseudo `GodService` into three responsibilities.
2. Identify which SOLID principle a `@MockBean` test double supports.
3. Explain D vs S in one sentence each.

## 11. Chapter Summary

SOLID guides cohesive, replaceable Java designs; DI and hexagonal architecture are structural expressions of Dependency Inversion and Open/Closed discipline.

## 12. Next Steps

[02-creational-patterns.md](02-creational-patterns.md)

## Review Checklist

- [x] All five SOLID; links to DI/LSP
