# Custom Exceptions and Design

> **Status:** Completed  
> **Module:** 14 — Exceptions and Error Handling  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (domain types, checked/unchecked choice, wrapping)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. Do **not** manufacture content to fill headings. For omitted sections, record the reason in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow *(or merge note)* | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs *(strongly encouraged for Technical / Deep-dive)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare **custom exception classes** extending **`Exception`** or **`RuntimeException`** with meaningful messages and fields.
- Choose **checked vs unchecked** for domain failures at API boundaries.
- **Wrap** lower-level exceptions with **`initCause`** or constructor overloads preserving the chain.
- Apply **naming and packaging** conventions (`*Exception`, avoid deep hierarchies without need).
- Recognize **anti-patterns**: exception-driven control flow, leaky stack traces, catching `Throwable`.

## 2. Prerequisites

- **Modules 14 Ch.01–02** — hierarchy, `try`/`catch`, propagation.
- **Module 06 Ch.04** — immutability; exception **state** should be well-defined after construction.
- **Module 13** — generics do not change exception rules except erasure limits on `catch (T e)`.

Brief refresher: a **custom exception** is a normal class that **extends** an existing exception base so callers can **`catch`** your type and read **domain-specific fields** beyond the message string.

## 3. Why This Concept Exists

JDK types (`IOException`, `SQLException`) describe **infrastructure** failures. Business rules—“insufficient funds,” “seat already booked,” “coupon expired”—need **stable domain types** so:

- Callers **without** parsing message strings can branch on **typed catches** or **structured fields**.
- Logs and metrics tag **error codes** consistently.
- API documentation lists **recoverable** vs **fatal** outcomes.

Poor design—500-line `catch (Exception e)` blocks, **checked** exceptions for every typo, or **messages as JSON**—forces fragile clients. This chapter focuses on **small, purposeful** exception types.

## 4. Conceptual Explanation

### 4.1 Minimal custom checked exception

```java
class InsufficientFundsException extends Exception {
    private final int shortfall;

    InsufficientFundsException(int shortfall) {
        super("need " + shortfall + " more");
        this.shortfall = shortfall;
    }

    int shortfall() { return shortfall; }
}
```

- **`extends Exception`** → **checked**; `withdraw` must **`throws InsufficientFundsException`**.
- **Fields** capture facts **`getMessage()` alone** might bury.
- **Constructor** sets message for logs and humans.

### 4.2 Custom unchecked exception

```java
public final class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("order not found: " + orderId);
    }
}
```

Use when failure is a **caller mistake** or **not expected** to be recovered inline at every layer—REST layer maps to 404.

### 4.3 Chaining (wrapping)

```java
public RepositoryException(String message, Throwable cause) {
    super(message, cause);
}
```

Preserves **driver `SQLException`** inside **domain `RepositoryException`** for **`getCause()`** in logs without exposing driver types to business code.

### 4.4 Where to catch

| Layer | Typical role |
|-------|----------------|
| **Domain/service** | Throw domain exceptions; rarely catch infrastructure |
| **Infrastructure** | Translate I/O/driver errors to domain or wrap |
| **Entry (HTTP, CLI)** | Catch, map to status/exit code, log once |

## 5. Mechanism and Execution Flow

### 5.1 Throw with domain state

In [`CustomExceptionsDemo`](../../examples/core-java/custom-exceptions-demo/):

1. **`Account.withdraw`** validates **balance** before mutating state.
2. When funds are insufficient, it **`throw new InsufficientFundsException(shortfall)`**—allocation + message + **immutable field** `shortfall`.
3. Because the type **`extends Exception`**, the compiler forces **`withdraw` → throws InsufficientFundsException`** on the declaration; callers must handle or declare.

No partial debit occurs—the method throws **before** changing balance, preserving **exception safety** for the account aggregate.

### 5.2 Wrapping and the cause chain

When infrastructure catches **`IOException`** and throws **`RepositoryException(message, cause)`**:

1. The **`super(message, cause)`** constructor links **`cause`** via `Throwable.initCause` semantics (constructor overload).
2. Logs can print **`getCause()`** stack traces while public API exposes only **`RepositoryException`**.
3. **`catch` at the HTTP boundary** maps **`RepositoryException`** to 503 without leaking driver class names in responses.

### 5.3 Translation at layer boundaries

Typical **downward** flow: driver throws → repository **wraps** once → service **rethrows** domain type → controller **catches once**, logs, maps to HTTP status. **Avoid** catching, logging, and rethrowing the **same** message at every layer—that duplicates noise without adding type information.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Domain exception** | Type modeling a business rule failure |
| **Chaining / wrapping** | New exception with **cause** pointing to the original failure |
| **Exception translation** | Converting low-level type to API-level type at a boundary |
| **Exception safety** | Object remains valid after failed operation (often **no partial mutation**) |

## 7. Syntax and Language Rules

```java
// Checked — declare throws
void withdraw(int amount) throws InsufficientFundsException { }

// Unchecked — no throws required
void findOrder(String id) { }

// Serialization — optional serialVersionUID on public exceptions
public class ApiException extends Exception {
    private static final long serialVersionUID = 1L;
}
```

Rules:

- Provide **at least** a constructor taking **`String message`** and often **`String message, Throwable cause`** for public API types.
- **`final`** on simple leaf exception classes prevents accidental drift.
- **Do not** override **`getMessage()`** to compute side effects; keep messages stable.

## 8. Step-by-Step Examples

### 7.1 Minimal — `CustomExceptionsDemo`

Path: [examples/core-java/custom-exceptions-demo/](../examples/core-java/custom-exceptions-demo/)

```java
AccountService svc = new AccountService(100);
try {
    svc.withdraw(40);
    System.out.println("balance=" + svc.balance());
    svc.withdraw(100);
} catch (InsufficientFundsException ex) {
    System.out.println("error=" + ex.getMessage());
    System.out.println("shortfall=" + ex.shortfall());
}
```

**Verified output (Temurin 21.0.12.1):**

```text
balance=60
error=need 40 more
shortfall=40
```

- First **withdraw(40)** succeeds; balance **100 → 60**.
- Second **withdraw(100)** fails; **no deduction**; catch runs.
- **`shortfall=40`** = `100 - 60` encoded in the exception (demo uses `amount - balance` at throw site).

### 7.2 Realistic — translation at repository boundary

```java
public User loadUser(String id) {
    try {
        return jdbc.findById(id);
    } catch (SQLException ex) {
        throw new DataAccessException("load user " + id, ex);
    }
}
```

Service layer catches **`DataAccessException`** once; controllers map to **503** or **500** with sanitized body.

### 7.3 Advanced — fail before mutating state

```java
public void withdraw(int amount) throws InsufficientFundsException {
    if (amount > balance) {
        throw new InsufficientFundsException(amount - balance);
    }
    balance -= amount;
}
```

**Validate-then-act** keeps **exception safety**: balance unchanged when throw happens (contrast with deduct-then-check bugs).

## 9. Real-World Use Cases

- **Banking / payments** — insufficient funds, limit exceeded, duplicate idempotency key.
- **Validation** — aggregate `ConstraintViolationException` or use Bean Validation (framework module).
- **Workflow engines** — typed “transition not allowed” for illegal state machines.
- **Batch import** — row-level exception collected; job continues with error report.

## 10. Internal Behavior

Custom exceptions are **ordinary classes** on the heap; **`throw`** captures stack trace like any `Throwable`. **Serialization** (RMI, some caches) may marshal exception classes—provide **`serialVersionUID`** on **public** checked exceptions intended to cross class loader boundaries.

## 11. Edge Cases and Limitations

- **Over-specific hierarchies** (`FinanceException` → `DebitException` → `WeekendDebitException`) confuse more than they help—start flat.
- **Checked** exceptions in **stream lambdas** force wrapping—design APIs with that cost in mind.
- **Internationalization:** `getMessage()` is often English technical text; UIs should map **error codes**, not display raw server messages.
- **Equals** on exceptions is rarely meaningful—compare type + code fields if needed.

## 12. Common Mistakes

### Incorrect — using exceptions for expected empty results

```java
User getUser(String id) throws UserNotFoundException {
    User u = cache.get(id);
    if (u == null) throw new UserNotFoundException(id);
    return u;
}
```

### Corrected (when “not found” is common)

Return **`Optional<User>`** or a **result type** for lookup; reserve exceptions for **violated assumptions** or **true fault** paths ([Module 18](../18-optional-and-null-handling/README.md)).

### Incorrect — losing the cause

```java
catch (SQLException ex) {
    throw new RepositoryException("db error");
}
```

### Corrected

```java
throw new RepositoryException("db error", ex);
```

### Incorrect — checked exception on every setter typo

Forces `throws` pollution through the entire stack for **preventable** argument errors—use **unchecked** `IllegalArgumentException` (Ch.01).

## 13. Best Practices

- Name with suffix **`Exception`**; be specific (`InsufficientFundsException`, not `MyException`).
- Document **checked** types on public methods; document **unchecked** with `@throws` in Javadoc.
- Expose **immutable** details (`shortfall()`, `orderId()`) for programmatic handling.
- **Log once** at the boundary; avoid logging the same stack trace at every rethrow layer.

## 14. Design Trade-offs

| Style | Fit | Risk |
|-------|-----|------|
| Checked domain | Caller must plan recovery (I/O-heavy workflows) | Verbose signatures |
| Unchecked domain | REST/streams/lambdas | Forgotten handling at edges |
| Error codes in enum | Stable client contracts | Mapping maintenance |
| Optional / Result | Expected absence | Not universal in Java stacks |

**Effective Java** and modern framework style lean **unchecked for programming errors**, **checked or wrapped** for true externalities—your module boundaries should be **consistent**, not mixed arbitrarily.

## 15. Performance Considerations

Rich exception objects (large attachments, deep stacks) cost allocation. For **high-volume validation**, prefer **batch error lists** over throwing per invalid field in inner loops.

## 16. Security Considerations

Custom messages must not echo **passwords, tokens, or PII** upward to clients. **Chained causes** in public JSON leak implementation—strip for outward responses, keep in server logs.

## 17. Java Version and Compatibility Notes

Custom exceptions unchanged in **Java 21**. **Records** can hold error details on **non-throwable** result types (Module 15); exception classes remain **classes** for hierarchy and `instanceof` catches.

## 18. Related Language Comparisons

Kotlin has **no checked exceptions**; Java interop uses **`@Throws`** when needed. Scala encourages **`Try`**/`Either` for recoverable errors alongside exceptions.

## 19. Revision and Mastery

- **Gaps:** treating **`getMessage()`** as API contract; catching **`Exception`** and rethrowing without cause.
- **You should answer:** Why is `shortfall=40` after the failed second withdraw?
- **Advanced:** Idempotent retries—when **must not** retry after a domain exception?

## 20. Exercises

### Beginner

1. Predict three lines of `CustomExceptionsDemo`.
2. Add `balance()` after failed withdraw inside `catch`—what value and why?

### Intermediate

3. Convert `InsufficientFundsException` to **unchecked**—what changes for `withdraw` callers?
4. Write `RepositoryException` with message + cause constructors.

### Advanced

5. Design exception strategy for a REST API: map domain, validation, and infrastructure failures to status codes without `catch (Throwable)`.

## 21. Interview Questions

1. Checked vs unchecked custom exceptions—criteria?
2. Why preserve **cause** when wrapping?
3. Why should `finally` not commit business transactions without care?

## 22. Solutions

1. See §8.1.
2. Still **60**—failed withdraw did not change balance.
3. Remove `throws` from `withdraw`; callers no longer forced to catch—handle at boundary or become uncaught.
4. Two constructors delegating to `super(message)` and `super(message, cause)`.
5. e.g. validation → 400, domain rule → 409/422, auth → 401, not found → 404, infrastructure → 503 with generic body; log causes server-side.

## 23. Chapter Summary

**Custom exceptions** encode **domain failures** with **types and fields**, not ad hoc strings. **Checked** types fit recoverable workflows callers must acknowledge; **unchecked** types fit bugs and many service APIs. **`CustomExceptionsDemo`** shows **checked** withdrawal failure with **`shortfall`**. **Wrap** infrastructure failures, **validate before mutate**, and **handle once** at system edges. Module 14 complete—continue to **algebraic data shapes** in Module 15.

## 24. Key Terminology

Domain exception · wrapping · cause chain · exception translation · exception safety

## 25. References and Further Reading

- [Java SE `Exception`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Exception.html)
- [Effective Java — Item on exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html) (unchecked policy)
- [Module 05 Ch.02](../05-object-oriented-programming/02-encapsulation-and-abstraction.md) · [Module 13 Ch.04](../13-generics/04-generic-api-design.md)

## 26. Next Steps

- [Module 15 — Enums, Records, and Sealed Types](../15-enums-records-and-sealed-types/README.md)
- [Module 18 — Optional and Null Handling](../18-optional-and-null-handling/README.md)

## Review Checklist

**Mandatory Depth Review:** Technical profile; custom domain exceptions at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (layer translation §5.3)
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11)

**Omissions (section → reason):**

- **Bean Validation / global exception handlers** — cited for later Spring/Web modules; not compile-time exception syntax.

**Verification note:** `CustomExceptionsDemo` output unchanged on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
