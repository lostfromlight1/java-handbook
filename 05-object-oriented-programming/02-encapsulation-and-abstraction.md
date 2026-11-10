# Encapsulation and Abstraction

> **Status:** Completed  
> **Module:** 05 — Object-Oriented Programming  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain **encapsulation**: hide internal representation, control access, and **validate invariants**.
- Use Java **access modifiers** (`private`, package-private, `protected`, `public`) at an introductory level.
- Decide when **getters and setters** help vs when they add noise.
- Explain **abstraction**: expose operations meaningful to callers while hiding implementation.
- Distinguish **encapsulation** from **abstraction** (related, not identical).
- Apply **information hiding** and reject invalid state with **`IllegalArgumentException`** (exceptions deepen in Module 14).

## 2. Prerequisites

- [01-oop-fundamentals.md](01-oop-fundamentals.md) — classes, objects, fields, methods, `new`, references, `private` fields preview.
- **Module 04** — methods, `return`, guard-style logic.
- **Module 03** — `if`, boolean conditions, `try` / `catch` at a basic reading level (the demo catches `IllegalArgumentException`).

Brief refresher: an **invariant** is a rule that must always hold for a valid object (balance never negative, temperature above absolute zero, username non-blank). Encapsulation lets the type enforce invariants at the boundary of its API.

## 3. Why This Concept Exists

Without boundaries, every layer of an application can reach into another object’s fields. A controller sets `account.balance = -5`; a mapper overwrites a username with spaces; a UI reads internal arrays you planned to replace. **One change to representation** breaks code in dozens of places.

**Encapsulation** and **abstraction** answer two related problems:

| Problem | Mechanism |
|---------|-----------|
| “Anyone can corrupt my state” | Hide fields; expose controlled methods |
| “Callers should not depend on how I store data” | Expose a small, stable **abstract** API |

Java enforces visibility with **access modifiers**. Good types keep invariants true by validating at constructors and mutators, often signaling programmer or caller errors with **exceptions** like `IllegalArgumentException`.

## 4. Conceptual Explanation

### 4.1 Encapsulation

**Encapsulation** means:

1. **Bundle** data and the code that acts on it in one class.
2. **Hide** internal details (private fields, private helper methods).
3. **Expose** a deliberate surface for the rest of the program.

Callers should not depend on **how** balance is stored (cents vs dollars, `int` vs `BigDecimal`)—only on **what** they can ask the object to do (`withdraw`, `balanceCents()`).

Benefits:

- **Invariant preservation** — only methods you write can set fields; you check every path.
- **Evolvability** — switch from `int` cents to `long` without changing every call site if the public API stays stable.
- **Debugging** — breakpoints in one set of methods catch all mutations.

### 4.2 Access modifiers (intro level)

| Modifier | Class | Package | Subclass | Anywhere |
|----------|-------|---------|----------|----------|
| `private` | Yes | No | No | No |
| *(none)* — package-private | Yes | Yes | No | No |
| `protected` | Yes | Yes | Yes | No |
| `public` | Yes | Yes | Yes | Yes |

**`private`** — only code **inside this class** (nested types have special rules).

**Package-private** (no keyword) — same **package**; common for package-internal collaboration; use deliberately.

**`protected`** — subclass access even in other packages; inheritance-heavy APIs (Module 07).

**`public`** — exported API; use for types and methods meant for all clients.

**Guideline for beginners:** fields **`private`**; expose **`public`** (or package) methods only when callers need them. Subclass and inheritance modifiers matter more once you design class hierarchies.

### 4.3 Getters and setters: useful vs overused

**Getters** (accessors) return field values; **setters** (mutators) update them.

**When they help:**

- Expose **read-only** views (`getUsername()` on immutable id).
- **Validate and normalize** on write (`setDisplayName` trims whitespace).
- Frameworks that map JavaBeans conventions (many libraries expect `getX` / `setX`).

**When they are noise:**

- **Anemic models** — every field has public getter/setter but **no** behavior; logic leaks to services anyway.
- **Exposing mutable internals** — `getItems()` returning the live `List` allows callers to bypass your invariants (return copies or unmodifiable views instead).

Prefer **meaningful methods** (`celsius()`, `fahrenheit()`, `withdraw`) over raw `getBalance` / `setBalance` when the operation is domain language.

### 4.4 Abstraction

**Abstraction** means presenting a **simplified model** of a capability: callers know **what** they can do, not **how** it is done.

The demo’s `Temperature` class stores Celsius internally. Callers use:

```java
Temperature t = Temperature.fromCelsius(25);
t.fahrenheit();
```

They need not know the field name or conversion formula unless they read the source. The **abstraction** is “a temperature you can read in C or F.”

Abstraction appears at many levels:

- **Methods** — `withdraw` abstracts ledger rules.
- **Classes** — `java.io.InputStream` abstracts bytes from disk, network, or memory.
- **Modules and packages** — hide packages behind public API (Module 08+).

### 4.5 Encapsulation vs abstraction

| | Encapsulation | Abstraction |
|---|----------------|-------------|
| **Focus** | **Protection** of internal state | **Simplification** of what callers see |
| **Question** | “Who may touch this field?” | “What operation matters to the client?” |
| **Mechanism** | `private`, validation, narrow API | Meaningful method names, hide algorithms |
| **Overlap** | A good public API is usually **both** | You can abstract without strict hiding (e.g. all-public interface to a black-box library) |

You can **encapsulate without abstracting well** (trivial getters for every field). You can **abstract without full encapsulation** (public constants plus complex hidden helpers). Professional types aim for **both**: small, intention-revealing surface and private representation.

### 4.6 Information hiding and `IllegalArgumentException`

**Information hiding** is the practice of not exposing implementation choices (data structures, caching, locking) that might change.

When a caller violates a **precondition** (negative absolute temperature, blank username), the object should **fail fast** rather than enter invalid state:

```java
throw new IllegalArgumentException("below absolute zero");
```

`IllegalArgumentException` extends `RuntimeException` — callers are **not** forced to declare it (unchecked). It signals “you passed an illegal value.” Module 14 covers exception taxonomy, `try` / `catch`, and when to use checked vs unchecked exceptions. **Here:** use it at validation boundaries in constructors and setters when bad arguments must never produce a half-valid object.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Encapsulation | Bundle state with behavior; restrict direct field access |
| Abstraction | Expose essential operations; hide implementation detail |
| Access modifier | `private`, `protected`, `public`, or package-private visibility |
| Getter / setter | Accessor/mutator methods for fields (often `getX` / `setX`) |
| Invariant | Condition that must always hold for valid objects |
| Information hiding | Conceal design decisions that may change |
| `IllegalArgumentException` | Unchecked exception for invalid method/constructor arguments |

## 6. Syntax and Language Rules

**Private field + public behavior:**

```java
public final class Temperature {
    private final double celsius;

    private Temperature(double celsius) { ... }

    public static Temperature fromCelsius(double c) {
        return new Temperature(c);
    }

    public double celsius() { return celsius; }
}
```

**Rules:**

- Top-level classes are `public` (one per file, name matches file) or package-private.
- `private` constructor + **static factory** `fromCelsius` — hides construction and centralizes validation (valid pattern).
- `final` class cannot be subclassed — useful when you rely on invariants not to be broken by overrides (inheritance in Ch.03).

**JavaBeans naming** (convention, not language rule): boolean getters may be `isEmpty()` instead of `getEmpty()`.

## 7. Step-by-Step Examples

Full program: [examples/core-java/encapsulation-demo/](../examples/core-java/encapsulation-demo/)

```bash
javac EncapsulationDemo.java
java EncapsulationDemo
```

### 7.1 Minimal: validated setter

```java
class Counter {
    private int count;

    int getCount() {
        return count;
    }

    void increment() {
        count++;
    }
}
```

Encapsulation here: no `count++` scattered outside; only `increment` mutates. No setter for arbitrary values—invariant “only goes up by one through API.”

### 7.2 Realistic: `Temperature` and `UserAccount`

**Temperature** — private constructor, factory, conversion hidden:

```java
static Temperature fromCelsius(double c) {
    return new Temperature(c);
}

double fahrenheit() {
    return celsius * 9.0 / 5.0 + 32.0;
}
```

Invalid input fails at construction:

```java
Temperature.fromCelsius(-300);  // IllegalArgumentException
```

**UserAccount** — immutable username, mutable display name with trimming setter:

```java
void setDisplayName(String displayName) {
    if (displayName == null || displayName.isBlank()) {
        throw new IllegalArgumentException("displayName required");
    }
    this.displayName = displayName.trim();
}
```

### 7.3 Advanced: factory vs public constructor

| Approach | When |
|----------|------|
| `public Temperature(double c)` | Simple types, one clear way to construct |
| `private Temperature` + `fromCelsius` / `fromFahrenheit` | Multiple creation paths, shared validation |
| Builder pattern | Many optional fields (later modules) |

The demo uses a **private constructor** so every instance passes through validation in one place.

**Verified output (Temurin JDK 21.0.12.1):**

```text
celsius=25.0
fahrenheit=77.0
rejected: below absolute zero
user=ada display='Ada Lovelace'
```

The `rejected` line comes from catching the exception and printing `ex.getMessage()`.

## 8. Real-World Use Cases

- **JPA entities** — private fields, getters; setters optional; validation in service layer or Bean Validation (`@NotNull`).
- **Value types in APIs** — `Money`, `EmailAddress` classes reject bad construction.
- **Security** — do not expose mutable internal `byte[]` for passwords; minimize surface.
- **Library design** — public interface classes, package-private implementation helpers.

## 9. Internal Behavior

**Language:** access checks are **compile-time** for most uses (JLS §6.6). Reflection can bypass `private` (frameworks do; not for normal application code).

**JVM:** there is no separate “encapsulation” bytecode—visibility is enforced by the compiler. At runtime, `invokevirtual` on public methods is the normal call path.

**Framework note:** Spring and Jackson often need getters or visible fields for serialization—design DTOs explicitly rather than exposing entity internals.

## 10. Edge Cases and Limitations

- **Shallow vs deep encapsulation** — copying a reference to a mutable object inside your object (`Date`, `List`) without defensive copy breaks invariants.
- **Getter returns mutable collection** — caller modifies your internal list; return `List.copyOf(items)` or unmodifiable view.
- **Over-validation in getters** — getters should be cheap; heavy validation belongs on construction/mutation.
- **`protected` leakage** — subclasses in other packages can access `protected` members; design carefully.

## 11. Common Mistakes

### Incorrect — public mutable field

```java
public class Profile {
    public String email;  // anyone can set null or garbage
}
```

### Corrected

```java
public class Profile {
    private String email;

    public void updateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("invalid email");
        }
        this.email = email;
    }
}
```

### Incorrect — getter exposes internal list

```java
public List<Item> getItems() {
    return items;  // live reference
}
```

### Corrected

```java
public List<Item> getItems() {
    return List.copyOf(items);
}
```

### Incorrect — silent clamp hides bugs

```java
public void setAge(int age) {
    this.age = Math.max(0, age);  // caller never learns -5 was wrong
}
```

### Corrected (when invalid input is a bug)

```java
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("age must be non-negative");
    }
    this.age = age;
}
```

Choose clamp vs throw based on whether bad values are **expected** (user typing) vs **programming errors**.

## 12. Best Practices

- **Favor immutability** where possible (`final` fields, no setters).
- **Validate early** — constructors and factories, not “eventually consistent” broken objects.
- **Name methods for domain language** (`celsius()` not only `getCelsius()` when it reads naturally).
- **Minimize public surface** — every public method is a maintenance promise.
- **Document invariants** in class Javadoc for team-facing types.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Private fields + behavior | Strong invariants | More methods to write |
| JavaBeans getters/setters | Framework compatibility | Anemic model risk |
| Unchecked `IllegalArgumentException` | Clean API, fail fast | Caller must read docs/tests |
| Checked exceptions for validation | Forces handling | Verbose; less common in modern Java APIs for pure argument errors |

## 14. Performance Considerations

Getter/setter overhead is negligible. Defensive `List.copyOf` on every getter can be costly—prefer unmodifiable views or immutable collections by design when hot paths matter (profile first).

## 15. Security Considerations

Encapsulation limits exposure of credentials and PII. Logging `toString()` on rich objects can leak hidden fields—override `toString` carefully. Never expose direct references to security-sensitive mutable buffers.

## 16. Java Version and Compatibility Notes

Access modifiers are unchanged in Java 21. `List.copyOf` and `String.isBlank()` are Java 9+ / 11+; validated on **21.0.12.1**. Records (later) combine compact syntax with accessor methods.

## 17. Related Language Comparisons

- **C++** — `friend`, `public`/`private` similar spirit; header files expose more structure.
- **Kotlin** — `val`/`var`, properties generate accessors; default `public` is restricted by idioms.
- **Go** — capitalization exports symbols; no `private` keyword (package-level visibility).

## 18. Revision and Mastery

**You should already understand:**

- Why `private` fields from Ch.01 matter for maintainability.
- Pass-by-value of references when passing encapsulated objects to methods.

**Common gaps:**

- Treating encapsulation and abstraction as synonyms.
- Adding getters/setters for every field without behavior.
- Catching `IllegalArgumentException` and continuing with invalid global state.

**Questions you should answer:**

1. Why is `Temperature`’s constructor `private`?
2. What invariant does `UserAccount` protect on `setDisplayName`?
3. When would you return a copy from a getter?

**Practical check:** Add `fromFahrenheit` to `Temperature` reusing private constructor and shared validation.

## 19. Exercises

### Beginner

1. Run `EncapsulationDemo` and identify which methods are getters vs domain operations.
2. Make `Counter` from §7.1 reject `decrement` below zero via `IllegalArgumentException`.
3. List which members of `Temperature` are `private` vs package-private.

### Intermediate

4. Implement `Temperature.fromFahrenheit`.
5. Add `UserAccount` validation: display name max length 80 characters.
6. Write a one-paragraph design for `Email` value type: what is hidden, what is abstract API?

### Advanced

7. Explain why returning `Collections.unmodifiableList(items)` still leaks if elements are mutable.
8. Compare throwing `IllegalArgumentException` vs using `Optional` for a lookup API—trade-offs in two sentences each.

## 20. Interview Questions

1. **What is encapsulation?**
2. **Difference between encapsulation and abstraction?**
3. **When would you use a private constructor?**
4. **Why avoid public fields in domain models?**
5. **What is `IllegalArgumentException` for?**

## 21. Solutions

1. `celsius()`, `fahrenheit()` are domain accessors; `getUsername()` / `getDisplayName()` are classic getters; `setDisplayName` is a mutator with validation.
2. `if (count == 0) throw new IllegalArgumentException("already zero");` before decrement (or guard message of your choice).
3. `celsius` field and constructor are `private`; methods are package-private in the demo file (same package as `EncapsulationDemo`).
4. `fromFahrenheit`: convert with `(fahrenheit - 32) * 5.0 / 9.0`, then `new Temperature(c)`.
5. Check `displayName.length() > 80` after trim, throw if exceeded.
6. Sample: store normalized lowercase in private field; expose `String value()`; constructor rejects missing `@`.
7. Unmodifiable list prevents add/remove; element objects can still be mutated unless elements are immutable.
8. `IllegalArgumentException` for clear programmer/API contract violation; `Optional` for “may not exist” as normal outcome without exceptions.

## 22. Chapter Summary

**Encapsulation** protects state with `private` fields and validated methods. **Access modifiers** define who can see each member. **Getters and setters** help when they enforce rules or satisfy frameworks—not when they only duplicate public fields. **Abstraction** presents a purposeful API (`Temperature`, `withdraw`) without exposing implementation. **`IllegalArgumentException`** rejects illegal arguments at boundaries. Together, these ideas keep domain models safe and evolvable—on the server and in libraries.

## 23. Key Terminology

Encapsulation · abstraction · information hiding · invariant · access modifier · getter · setter · factory method · `IllegalArgumentException`

## 24. References and Further Reading

- [JLS §6.6 — Access Control](https://docs.oracle.com/javase/specs/jls/se21/html/jls-6.html#jls-6.6)
- [JLS §8.4.2 — Method Body](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.2)
- [Java SE — `IllegalArgumentException`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/IllegalArgumentException.html)
- Effective Java (Bloch) — items on encapsulation, immutability, and minimizing accessibility

## 25. Next Steps

- [03-inheritance-and-polymorphism-intro.md](03-inheritance-and-polymorphism-intro.md) — `protected` and overrides in context.
- Module 06 — constructors, `equals` / `hashCode`, immutability patterns.
- Module 14 — exception design and handling in depth.

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate passed; Temperature and UserAccount progressive examples
- [x] EncapsulationDemo verified on Temurin JDK 21.0.12.1
- [x] Encapsulation vs abstraction clarified
- [x] Revision and Mastery, exercises, solutions, references
