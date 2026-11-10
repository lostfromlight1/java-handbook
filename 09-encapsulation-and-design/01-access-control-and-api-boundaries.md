# Access Control and API Boundaries

> **Status:** Completed  
> **Module:** 09 — Encapsulation and Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain how Java **access modifiers** define who may **see** and **use** types and members.
- Distinguish the **public API** of a type from its **internal implementation**.
- Design **package boundaries** with package-private types and members.
- Use **`protected`** deliberately as an **extension API** for subclasses without leaking internals to all callers.
- Recognize when **getters** expose **too much** and how to return safe views instead.
- Apply **information hiding** so invariants stay enforceable (building on Module 05).

## 2. Prerequisites

- **Module 05 Ch.02** — encapsulation intro, `private` fields, guard methods.
- **Module 06** — constructors, immutability, defensive copying preview.
- **Module 07 Ch.01** — `protected` in inheritance hierarchies.
- **Module 08 Ch.04** — depend on narrow **interfaces** at seams.

Brief refresher: **encapsulation** hides representation and forces mutations through methods you control. **Access control** is the language mechanism that enforces which code is allowed to touch which members at **compile time**.

## 3. Why This Concept Exists

Applications split into layers (UI, service, persistence). When every class can read and write every field, **one careless line** breaks invariants: negative balance, duplicate ids, half-initialized objects.

**Access boundaries** answer: “What is this type **for** others to use, and what is **none of their business**?”

Without boundaries:

```java
ledger.balance = -1;      // bypasses validation
ledger.entries = 99;      // corrupts audit trail
```

With boundaries, only **`credit`**, **`balance`**, and **`entryCount`** are callable from outside; **`apply`** is private and updates both fields together.

That is an **API boundary**: callers depend on stable operations, not on field layout.

## 4. Conceptual Explanation

### 4.1 Access modifiers (full decision matrix)

| Modifier | Same class | Same package | Subclass (other package) | Everywhere |
|----------|------------|--------------|---------------------------|------------|
| `private` | Yes | No | No | No |
| *(none)* — package-private | Yes | Yes | No | No |
| `protected` | Yes | Yes | Yes | No |
| `public` | Yes | Yes | Yes | Yes |

**Classes and interfaces** at top level can be `public` (visible everywhere) or package-private (visible in package only). You cannot declare a top-level `private` class in a normal `.java` file (nested types are different; Module 06).

**Members** — fields, methods, nested types — use all four levels.

**Guideline for production types:**

- Fields almost always **`private`**.
- **`public`** only what external code must call.
- **`protected`** only when subclasses in other packages **must** see a hook (inheritance API).
- **Package-private** for collaborators in the same package that are not part of the exported product API.

### 4.2 Public API vs implementation

The **public API** is everything another compilation unit may legally reference: `public` types and `public`/`protected` members (if callers/subclasses need them).

**Implementation** is everything that supports that API but should not be a dependency: private fields, private helpers, package-private utilities.

**Stable API** principle: you should be able to change private representation (e.g. `int` cents → `long` cents) without touching call sites—**if** the public methods stay compatible.

The demo [`AccessControlDemo`](../../examples/core-java/access-control-demo/):

| Member | Visibility | Role |
|--------|------------|------|
| `credit`, `balance`, `entryCount` | `public` | Caller API |
| `balance`, `entries` fields | `private` | State |
| `apply` | `private` | Internal mutation helper |

### 4.3 Package as a boundary

A **package** is both a **namespace** and a **trust zone**. Types in `com.app.billing` may use package-private `LedgerSupport` while `com.app.web` sees only `public Ledger`.

**JPMS (Java 9+)** adds explicit **module** boundaries (`module-info.java` exports); this chapter focuses on classic package + modifier rules that every Java project uses regardless of modules.

### 4.4 Protected: extension surface, not a shortcut

**`protected`** lets subclasses access members even from another package. It is **not** “slightly less private”—it is an intentional **subclass contract**.

Use `protected` for:

- **Hook methods** in template-method designs (Module 07).
- Fields subclasses must read in optimized paths (use sparingly).

Avoid `protected` mutable fields that arbitrary subclasses can corrupt—prefer `protected` **methods** with invariant checks.

### 4.5 Leaky getters and exposed mutability

Returning a live mutable collection from a getter:

```java
public List<String> getLines() { return lines; } // caller can lines.clear()
```

breaks encapsulation. Prefer **unmodifiable views**, **copies**, or **domain methods** (`addLine(String)`).

### 4.6 API boundary + interfaces (Ch.02–03 bridge)

At module boundaries, **`public` classes** often expose **`public`** methods whose parameters use **interface types** from Module 08. Access control says *who can call*; interfaces say *what role* the collaborator plays.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Access modifier** | Keyword (`private`, `protected`, `public`) or default package access controlling visibility |
| **Public API** | Types and members intended for use outside the owning library or package |
| **Implementation detail** | Internal state and helpers not meant as dependencies |
| **Information hiding** | Concealing representation so callers cannot bypass rules |
| **Package boundary** | Visibility scoped to types in the same `package` declaration |
| **Extension API** | `protected` members exposed for subclass customization |

## 6. Syntax and Language Rules

```java
public class ExportedService {
    private final Repository repo;           // field private

    public ExportedService(Repository repo) { // ctor often public for wiring
        this.repo = repo;
    }

    public Result run(Command cmd) { ... }  // public API

    void logInternal(String msg) { ... }    // package-private helper
}

class PackageHelper { }                      // package-private class
```

- **`private`** top-level types only as **nested** classes.
- Subclasses **do not** inherit access to **`private`** members of the superclass (they use `super` public/protected API).
- **`public`** interface methods are implicitly `public abstract` (unless `default`).

## 7. Step-by-Step Examples

### 7.1 Minimal — private state, public operations

```java
class Counter {
    private int n;
    public void inc() { n++; }
    public int value() { return n; }
}
```

Only `inc` and `value` affect `n`; no `n++` from outside.

### 7.2 Realistic — Ledger demo

Full source: [`AccessControlDemo.java`](../../examples/core-java/access-control-demo/AccessControlDemo.java).

```java
ledger.credit(100);
ledger.credit(50);
System.out.println("balance=" + ledger.balance());
System.out.println("entries=" + ledger.entryCount());
```

**Verified output (Temurin 21.0.12.1):**

```text
balance=150
entries=2
```

**Important lines:**

- `credit` validates `amount > 0` before calling `apply` — invariant at the **public** entry.
- `apply` is **`private`**: both `balance` and `entries` update in one place; future `debit` can call the same helper.
- From `main`, `ledger.balance` as a field does **not** compile — the compiler enforces the boundary.

### 7.3 Advanced — layered API in one package

```java
package billing;

public final class InvoiceService {
    private final TaxTable tax; // package-private or private type

    public InvoiceService(TaxTable tax) { this.tax = tax; }
    public Money total(LineItem item) { return tax.apply(item); }
}

final class TaxTable { // package-private — not exported to other packages
    Money apply(LineItem item) { ... }
}
```

Callers outside `billing` see `InvoiceService` only; `TaxTable` can change freely.

## 8. Real-World Use Cases

- **Libraries (JDK, Spring, Guava):** vast `public` surface; `sun.*` / internal packages are unsupported—depend only on documented public API.
- **REST DTOs:** `public` getters for JSON mappers; keep validation in service layer, not in setters alone.
- **Domain models:** `Account` exposes `withdraw(amount)` not `setBalance` for ATM and ledger rules.
- **Multi-module Gradle/Maven:** each module’s `api` vs `implementation` classpath mirrors what should be **exported** vs hidden.

## 9. Internal Behavior

Access checks happen at **compile time** (`javac`). The JVM does **not** re-check `private` at runtime for ordinary field access from bytecode in the same class—**illegal access from other classes is rejected by the compiler** (reflection can bypass with `setAccessible`; Module 41+ covers reflection and security).

Nested classes: the compiler may synthesize **bridge** or accessor methods so outer classes can touch `private` members of nested types (still **source-level** privacy).

## 10. Edge Cases and Limitations

- **Same compilation unit tricks:** nested classes can access outer `private` members; still encapsulated from **outside** the outer class.
- **Reflection and serialization** can read private fields—do not treat `private` as a security boundary against malicious code.
- **JUnit / tests in same package** sometimes use package-private `void testHook()`—convenient but blurs boundary; prefer testing via public API when possible.
- **`protected static`** shared mutable state is a common foot-gun across subclasses.

## 11. Common Mistakes

### Incorrect — public fields

```java
public class Account {
    public int balance; // any caller can corrupt
}
```

### Corrected

```java
public class Account {
    private int balance;
    public int balance() { return balance; }
    public void deposit(int cents) { if (cents > 0) balance += cents; }
}
```

### Incorrect — protected field “for convenience”

```java
protected List<Event> events = new ArrayList<>(); // subclasses and same-package code mutate freely
```

### Corrected

```java
private final List<Event> events = new ArrayList<>();
protected void onEvent(Event e) { ... } // controlled hook
public List<Event> snapshot() { return List.copyOf(events); }
```

### Incorrect — widening visibility on override

```java
@Override
protected void save() { } // was public in super — compile error if narrower
```

Override visibility must be **same or wider** (JLS).

## 12. Best Practices

- **Minimize `public` surface** — every public method is a promise you maintain across releases.
- **Prefer `final` classes** when extension is not designed (Module 07 sealed types for closed hierarchies).
- **Document invariants** on public methods (Javadoc): preconditions, postconditions, null rules.
- **Favor composition + package-private helpers** over `protected` field inheritance.
- Align **package structure** with team boundaries (`api`, `internal`, `impl` packages).

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Everything `public` | Fast to wire | No invariants; fragile refactors |
| Strict `private` + small API | Safe evolution | More boilerplate methods |
| Package-private collaborators | Clean module innards | Tests may need same package or public API tests |
| `protected` hooks | Framework extensibility | Subclass misuse breaks invariants |

## 14. Performance Considerations

Access modifiers do not change runtime dispatch cost in normal compiled code. **Defensive copies** on getters have a cost—trade copying vs unmodifiable wrappers vs immutable collections (Module 06, Module 18).

## 15. Security Considerations

`private` prevents **accidental** misuse by teammates, not **hostile** code in the same JVM. Use **SecurityManager** (deprecated for removal), module **exports**, and **input validation** at trust boundaries for untrusted callers. Never expose secrets through `public` getters without need.

## 16. Java Version and Compatibility Notes

Access rules are core Java since 1.0. **JPMS** (Java 9) adds module `exports` / `opens`. **Records** (16+) generate accessor methods instead of JavaBeans getters by default—still a deliberate public API.

## 17. Related Language Comparisons

C# uses `internal` (assembly-wide) similar to package-private; Java has no assembly keyword—use modules or package discipline. Kotlin `internal` is module-wide within Gradle metadata.

## 18. Revision and Mastery

- **Gaps:** thinking `protected` is “friendly private”; exposing mutable collections; public setters on every field.
- **You should answer:** Who can call `package-private void helper()`? Why keep `apply` private when `credit` is public?
- **Advanced:** `exports` vs `opens` in JPMS; API compatibility with semantic versioning.

## 19. Exercises

### Beginner

1. Add `debit(int amount)` to `Ledger` that refuses amounts larger than balance; predict balance after credit 100, debit 30.
2. Why does `main` fail to compile if it assigns `ledger.entries = 0`?

### Intermediate

3. Sketch a `Temperature` with private Celsius field and public `celsius()` / `fahrenheit()` only—no public setters.
4. Split a class with `public` helpers into package-private `Parser` + `public` `ReportService`.

### Advanced

5. Write one paragraph: when is `protected abstract void onCommit()` better than `private void commit()` calling a `CommitListener` interface?

## 20. Interview Questions

1. Four access levels in Java?
2. Difference between package-private and `protected` for a subclass in another package?
3. Can you make a top-level class `private` in a normal source file?
4. What is a leaky abstraction in getters?
5. How do access modifiers relate to API stability?

## 21. Solutions

1. After credits 100+50, debit 30 → balance 120, entries 2 (debit does not add entry in minimal sketch unless you extend `apply`).
2. `entries` is `private`; only `Ledger` body may access it.
3. `fahrenheit()` computes from stored Celsius; no direct field exposure.
4. `ReportService` public; `Parser` package-private in `com.app.reports`.
5. Template method in hierarchy needs subclass hook; listener favors composition over inheritance (Ch.03).

## 22. Chapter Summary

**Access control** defines compile-time **visibility**. A strong **public API** hides **implementation details** behind `private` state and narrow methods. The **Ledger** demo shows **invariant enforcement** at public entry points and **private** helpers that keep fields consistent. **Package** and **`protected`** boundaries extend this to teams and subclasses. Next: **coupling** and **cohesion** across classes.

## 23. Key Terminology

Access modifier · public API · implementation detail · information hiding · package boundary · extension API

## 24. References and Further Reading

- [JLS §6.6 — Access Control](https://docs.oracle.com/javase/specs/jls/se21/html/jls-6.html#jls-6.6)
- [JLS §7.6 — Top Level Type Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-7.html#jls-7.6)
- Effective Java — Item on minimize accessibility

## 25. Next Steps

- [02-coupling-and-cohesion.md](02-coupling-and-cohesion.md)
- [03-composition-versus-inheritance.md](03-composition-versus-inheritance.md)
- Module 10 — type system and safe casting at boundaries

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; modifier matrix and API vs impl
- [x] AccessControlDemo verified on Temurin 21.0.12.1
- [x] Exercises, Revision and Mastery, references
- [x] First-appearance concepts explained in chapter
