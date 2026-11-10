# Clean Code Practices

> **Status:** Completed  
> **Module:** 42 — Clean Code and SOLID  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Apply **naming**, **functions**, **comments**, and **formatting** rules that survive code review.
- Refactor toward **small, intention-revealing** types without premature abstraction.
- Connect clean code to **testability** (Module 30) and **hexagonal boundaries** (Module 40).
- Run the [readability demo](../../examples/clean-code/readability-demo/).

## 2. Prerequisites

- Modules 02–07 — types, methods, OOP.
- Module 41 — SOLID vocabulary (this module deepens application).
- Module 30 — tests as safety net for refactors.

## 3. Why This Concept Exists

Readable code **reduces defects** and **onboarding time**. “Works on my machine” code that only the author understands becomes a liability when requirements change. Clean code is not aesthetic preference—it is **risk management**: names and structure document intent when original authors are unavailable.

## 4. Conceptual Explanation

**Clean code** means code another professional can **read, change, and test** without guessing. In Java this shows up as:

| Practice | What it means | Why it matters |
|----------|---------------|----------------|
| **Intention-revealing names** | `calculateInvoiceTotal` not `calc` | Searchability and reviews |
| **Small functions** | One level of abstraction per method | Easier tests and reuse |
| **Few arguments** | Prefer objects over long parameter lists | Fewer permutations of bugs |
| **No surprising side effects** | `findUser` should not send email | Callers reason locally |
| **Consistent formatting** | Project formatter (IDE) | Diffs show logic, not whitespace |
| **Meaningful errors** | Domain exceptions + context | Faster incident response |

Clean code complements **SOLID** (Ch.02): SRP keeps classes cohesive; clean naming makes violations visible in review.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Code smell** | Surface sign that design may need change (not always wrong immediately) |
| **Refactoring** | Behavior-preserving structural improvement (tests green before/after) |
| **Technical debt** | Shortcuts that speed today but slow tomorrow |
| **Boy Scout rule** | Leave code slightly cleaner than you found it |

## 6. Naming in Java

### Types and packages

- **Classes:** nouns (`InvoiceService`, `Money`).
- **Methods:** verbs (`cancelOrder`, `toDto`).
- **Booleans:** `isActive`, `hasStock`—not `flag`.
- **Packages:** lowercase, no underscores (`com.acme.billing`).

### Constants

Use `static final` with screaming snake case only for true constants (`MAX_RETRY_COUNT`).

### Incorrect — cryptic domain leakage

```java
void proc(Map<String, Object> m) { /* ... */ }
```

### Correct — domain language

```java
void applyDiscount(Quote quote, DiscountPolicy policy) { /* ... */ }
```

See [ReadabilityDemo](../../examples/clean-code/readability-demo/ReadabilityDemo.java): `Money` and `InvoiceLine` encode units and rounding in one place.

## 7. Functions and methods

Rules of thumb for services and domain code:

1. **≤ 20–30 lines** visible logic before extracting (guideline, not law).
2. **Extract** when a block needs a comment explaining *what* it does.
3. **Prefer pure functions** in domain math (deterministic, no I/O).
4. **`Optional` return** documents absence; avoid sentinel `null` for “not found.”

### Incorrect — boolean flag parameters

```java
void exportReport(boolean asCsv, boolean asPdf, boolean sendEmail) { }
```

### Correct — strategy or command object

```java
void exportReport(ExportFormat format, DeliveryTarget target) { }
```

## 8. Comments and documentation

- **Good comments:** legal headers, `//` explaining *why* a non-obvious constraint exists.
- **Bad comments:** restating the code (`// increment i`).
- **Javadoc:** public API surfaces and framework extension points—not every private helper.

Use Module 38 **OpenAPI** for HTTP contracts; do not duplicate endpoint shapes only in comments.

## 9. Error handling and flow

- **Fail fast** on invalid inputs at boundaries (see Module 43 input validation).
- **Do not swallow** exceptions without logging context (Module 47).
- **Guard clauses** reduce nesting:

```java
public Invoice finalize(Quote quote) {
    if (quote == null) throw new IllegalArgumentException("quote");
    if (!quote.isApproved()) throw new IllegalStateException("not approved");
  // happy path at low indentation
}
```

## 10. Real-World Use Cases

- **Payment service:** isolate `Money` type; never use `double` for currency.
- **REST layer:** thin controllers; no SQL in `@RestController`.
- **Legacy lift:** rename fields when module boundaries are clear; pair with characterization tests.

## 11. Internal Behavior

The JVM does not care about names—**developers and tooling** do. IDEs rename-refactor across modules; meaningful names make **static analysis** (Module 30) and **security scanners** reports actionable.

## 12. Edge Cases and Limitations

- **Over-abstraction:** three similar lines do not always need a framework.
- **Premature micro-types:** `EmailAddressString` value object is useful in email-heavy domains; noise elsewhere.
- **Generated code:** Lombok, MapStruct—review generated shape in PRs.

## 13. Common Mistakes

### Incorrect — magic numbers

```java
if (status == 3) { /* shipped */ }
```

### Correct

```java
if (status == OrderStatus.SHIPPED) { }
```

### Incorrect — large God class

`OrderService` with 40 methods mixing PDF, SMTP, and JDBC—split by responsibility (SRP).

## 14. Best Practices

- Enforce **Checkstyle/SpotBugs** in CI (Module 30).
- **Code review checklist:** naming, tests, boundaries, logging on errors.
- Align with **module structure** in modular monoliths (Module 40).

## 15. Design Trade-offs

| Strict clean code | Pragmatic legacy |
|-------------------|------------------|
| More types and files | Faster hotfix in one class |
| Easier long-term change | Short-term copy-paste |

Balance with team size and release pressure; never skip tests on refactors.

## 16. Java Version Notes

Java 21 **records** (`record Money(BigDecimal amount)`) reduce boilerplate for value objects—use when immutability fits (Module 08).

## 17. Revision and Mastery

- When is a comment justified vs deleting it?
- How does `Money` improve clean code vs `BigDecimal` everywhere?
- Name three smells in a recent PR you reviewed.

## 18. Exercises

1. Refactor a 80-line method into three named methods (preserve tests).
2. Replace two `boolean` parameters with an enum or small type.
3. Run `ReadabilityDemo` and extend with tax calculation on `Money`.

## 19. Interview Questions

- Difference between clean code and SOLID?
- How do tests enable refactoring?
- When would you *not* extract a method?

## 20. Chapter Summary

Clean code makes Java systems maintainable through intentional naming, small cohesive units, honest error handling, and reviews backed by tests—not through dogmatic rules.

## 21. Key Terminology

Code smell, refactoring, technical debt, intention-revealing name, guard clause.

## 22. References

- Joshua Bloch, *Effective Java* (3rd ed.) — Items on naming and APIs.
- Robert C. Martin, *Clean Code* — habits (adapt to Java idioms).

## 23. Next Steps

[02-solid-deep-dive.md](02-solid-deep-dive.md)

## Review Checklist

- [x] Depth gate; demo linked and verified Temurin 21.0.12.1
- [x] Revision and Mastery; exercises without inline solutions
