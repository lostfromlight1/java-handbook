# Immutability and Builders

> **Status:** Completed  
> **Module:** 06 — Classes and Object Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Define an **immutable object** and list practical rules for immutability in Java.
- Explain why immutability simplifies **reasoning**, **caching**, **concurrency** (preview), and **value equality**.
- Implement small **immutable value types** (`Money`) with `final` fields and operations that return **new** instances.
- Apply the **builder pattern** for types with many optional fields (`HttpRequest`).
- Recognize when **builders** beat telescoping constructors and when **records** (Java 16+) are enough.

## 2. Prerequisites

- **Module 05 Ch.02** — encapsulation, invariants, validation.
- **Module 06 Ch.01** — constructors, `final` fields.
- **Module 06 Ch.02** — **static nested** class for `Builder`.
- **Module 02 Ch.01** — reference vs primitive; `String` already immutable in the JDK.

Brief refresher: a **mutable** object’s state can change after construction (`setBalance`). An **immutable** object’s observable state does not change after construction; “changes” are modeled by creating and using a **new** object.

## 3. Why This Concept Exists

Shared mutable state is a primary source of bugs: one thread or method changes data another relied on; caches return aliases clients mutate; `HashMap` keys change hash buckets if someone mutates a key object (Ch.04).

**Immutability** makes values **stable**: pass a `Money` to ten methods—each sees the same cents and currency. Thread-safety for **reads** becomes trivial for the value itself because there is nothing to synchronize on the object’s fields (Module 23 adds nuance for publication of references).

Many API types resist a single compact constructor: HTTP requests need method, URL, headers, body, timeouts. **Telescoping constructors** (`new HttpRequest("GET")`, `new HttpRequest("GET", url)`, …) explode. A **builder** collects optional steps fluently and validates once in `build()`.

## 4. Conceptual Explanation

### 4.1 What immutability means in Java

Java has no `const` for objects. Immutability is a **convention enforced by design**:

| Technique | Role |
|-----------|------|
| `final` class (optional) | Prevent subclasses that could add mutability |
| `private final` fields for all state | No field reassignment after constructor |
| No setters | No approved mutation API |
| Defensive copies | If a mutable value must be stored (`List`, `Date`), copy in constructor and in getters |
| Operations return new instances | `plus`, `withHeader`, etc. |

**Shallow immutability:** all fields are `final` but a field points to a mutable list—object is **not** fully immutable unless the list is unmodifiable or copied.

**String** and **wrapper types** (`Integer`, etc.) are immutable in the JDK; `java.time` types (`Instant`, `LocalDate`) are designed as immutable value types.

### 4.2 Value objects

A **value object** represents a measurable concept (money, email, coordinates) defined by its **field values**, not object identity. Immutability pairs naturally with value semantics and correct `equals`/`hashCode` (Ch.04).

### 4.3 Builder pattern

A **builder** is usually a **static nested class** with:

- Mutable workspace fields (defaults like `method = "GET"`).
- **Fluent** methods returning `this` for chaining.
- **`build()`** calling a **private** product constructor and validating required fields.

The product constructor is often **`private`** or package-private so callers must use `Product.builder()`.

### 4.4 Immutability + builder together

The **built object** is immutable; the **builder** is mutable and often discarded after `build()`. Do not reuse builders across threads without synchronization—or create a fresh builder per request.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Immutable object** | State not intended to change after construction |
| **Mutable object** | State can change via methods or exposed fields |
| **Defensive copy** | Clone of mutable input so callers cannot mutate internal state |
| **Value object** | Type compared by value; often immutable |
| **Builder pattern** | Separate mutable assembly object; produces immutable product |
| **Fluent interface** | Methods return `this` (or builder) for chaining |

## 6. Syntax and Language Rules

Minimal immutable type:

```java
final class Money {
    private final int cents;
    private final String currency;

    Money(int cents, String currency) {
        this.cents = cents;
        this.currency = currency;
    }

    Money plus(Money other) {
        return new Money(this.cents + other.cents, currency);
    }
}
```

Builder sketch:

```java
static Builder builder() { return new Builder(); }
static final class Builder {
    Builder url(String u) { this.url = u; return this; }
    Product build() { return new Product(this); }
}
```

Java **records** (`record Point(int x, int y) { }`) generate accessor, `equals`, `hashCode`, `toString` for immutable data carriers—use when no custom invariants or builder are needed.

## 7. Step-by-Step Examples

### 7.1 Minimal money value

Immutable fields; `plus` returns new `Money` after currency check (see demo).

### 7.2 Module demo

**Source:** [`examples/core-java/immutability-builder-demo/ImmutabilityBuilderDemo.java`](../examples/core-java/immutability-builder-demo/ImmutabilityBuilderDemo.java)

```bash
cd examples/core-java/immutability-builder-demo
javac ImmutabilityBuilderDemo.java
java ImmutabilityBuilderDemo
```

**Verified output (Temurin JDK 21.0.12.1):**

```text
1250 USD
GET https://example.com/api Accept=application/json
```

`Money` uses constructor validation:

```java
if (currency == null || currency.isBlank()) {
    throw new IllegalArgumentException("currency required");
}
```

`plus` refuses mixed currencies—invalid operations throw instead of silently converting.

`HttpRequest.builder()` chains:

```java
HttpRequest req = HttpRequest.builder()
        .method("GET")
        .url("https://example.com/api")
        .header("Accept", "application/json")
        .build();
```

`build()` enforces `url` present—fail fast before an invalid request object exists.

Private constructor from builder:

```java
private HttpRequest(Builder b) {
    this.method = b.method;
    this.url = b.url;
    this.accept = b.accept;
}
```

### 7.3 Advanced: defensive copies

```java
final class Tags {
    private final List<String> tags;

    Tags(List<String> tags) {
        this.tags = List.copyOf(tags); // Java 10+ unmodifiable copy
    }

    List<String> tags() {
        return tags; // already unmodifiable
    }
}
```

Without `List.copyOf`, a caller could pass a mutable list and mutate it after construction.

## 8. Real-World Use Cases

| Context | Immutable / builder usage |
|---------|---------------------------|
| **Money / currency** | Never use raw `double`; integer cents + currency code |
| **HTTP clients** | OkHttp `Request.Builder`, JDK `HttpRequest` builder |
| **Configuration** | Classpath options built once, then frozen |
| **DTOs in concurrent pipelines** | Immutable messages between stages |
| **Domain IDs** | `UUID`, SKU strings wrapped in validated types |

REST APIs map JSON to mutable beans for frameworks, but domain cores often convert to immutable types before business rules run.

## 9. Internal Behavior

Immutable objects with only `final` fields are **safe to publish** without synchronization **after** construction completes (assuming no escapes from constructor). The JMM guarantees visibility of `final` fields after constructor (JLS/JMM—Module 35).

Builders are ordinary mutable objects on the heap until `build()`; short-lived builders in one thread are the common case.

## 10. Edge Cases and Limitations

- **Subclassing** breaks immutability unless base class is `final` or tightly controlled.
- **Arrays** as state: `final byte[] data` is immutable reference only—clone or wrap.
- **Lazy initialization** inside “immutable” objects (memoized hash) needs careful synchronization or `volatile` (advanced).
- **Builder not copying defaults:** if `build()` is called twice on same builder, decide whether to allow multiple products or throw.
- **Records with mutable components** defeat the purpose—keep components immutable types.

## 11. Common Mistakes

### Incorrect: “immutable” with public mutable field

```java
class Bad {
    public final List<String> items = new ArrayList<>();
}
```

### Corrected

```java
final class Good {
    private final List<String> items;
    Good(List<String> items) { this.items = List.copyOf(items); }
}
```

### Incorrect: builder returns mutable product

```java
class Request {
    public String url; // exposed mutation after build()
}
```

### Corrected: private fields, no setters on product; only builder mutates workspace.

### Incorrect: reusing one builder across requests without reset

Race or stale fields if builder kept in a field—create `builder()` per request.

## 12. Best Practices

- Mark immutable classes **`final`** when subclassing adds no value.
- Centralize validation in **constructor** or **`build()`**.
- Use **`Objects.requireNonNull`** for required references.
- Prefer **`List.copyOf` / `Map.copyOf`** for defensive snapshots (Java 10+).
- For 2–3 required fields, constructor may suffice; for optional headers/timeouts, use builder.
- Implement **`equals` / `hashCode` / `toString`** for value types (Ch.04).

## 13. Design Trade-offs

| Choice | When |
|--------|------|
| Constructor only | Few required fields, no optional knobs |
| Builder | Many optional fields; readable call sites; validation batching |
| Record | Simple immutable data carrier; canonical equals/hash |
| Mutable bean | Framework binding (JPA, some JSON mappers)—convert to domain immutable type inward |

Builders add classes and boilerplate; codegen (Lombok `@Builder`) trades source noise for tooling dependency.

## 14. Performance Considerations

Creating new objects for each `plus` on money may allocate more than in-place mutation; for high-frequency trading kernels, specialized mutable buffers exist—but **domain services** usually favor clarity. Object allocation on modern GC is cheap compared to I/O.

Reuse builders only **after** `build()` if API provides `clear()` or new builder per operation.

## 15. Security Considerations

Immutable request objects cannot be altered after security checks approve them—important when passing credentials or signed URLs through pipelines.

Validate URLs and headers in `build()` to block SSRF or header injection at construction time, not scattered in callers.

## 16. Java Version and Compatibility Notes

- **`List.copyOf`**, **`Map.copyOf`** — Java 10.
- **Records** — Java 16 (immutable carriers; compact constructors for validation).
- Preview features for future value types may evolve—Java 21 path uses classes, records, and builders as shown.

## 17. Related Language Comparisons

Kotlin `data class` with `copy()`; C# records; Rust ownership enforces move semantics. Java relies on discipline plus `final` and factories.

## 18. Revision and Mastery

- **Gaps:** assuming `final` keyword alone makes whole object immutable; ignoring contained mutable collections.
- **Mastery check:** implement `Email` immutable with normalization; builder optional.
- **Framework note:** Jackson can deserialize into builders with annotations—pattern common in APIs.

## 19. Exercises

### Beginner

1. Add `Money.toString()` behavior prediction before running demo.
2. What happens if `HttpRequest.builder().build()` without `url`?

### Intermediate

3. Add `HttpRequest.Builder.header` support for `Authorization` stored in a new `final` field.
4. Implement `Money` `minus` returning new instance; reject negative result if business rule forbids.

### Advanced

5. Explain double-checked locking vs immutable holder for lazy singleton (single paragraph).

## 20. Interview Questions

1. How do you make a class immutable in Java?
2. Builder vs telescoping constructors?
3. Why defensive copy?
4. Can immutable objects contain mutable objects?
5. Records vs custom immutable class?

## 21. Solutions

1. `1250 USD` format from demo.
2. `IllegalStateException: url required`.
3. Extend builder workspace; pass through private constructor.
4. `return new Money(cents - other.cents, currency)` with guard.
5. Immutable holder: initialize-on-demand with holder idiom; DCL needs `volatile` for classic singleton—prefer enum singleton or holder.

## 22. Chapter Summary

**Immutability** locks object state after construction using `final` fields, no setters, and operations that return **new** instances. **Builders** tame configurations with many optional parts while keeping products immutable and validated. Together they underpin value modeling, safer sharing, and correct use in collections keyed by value (Ch.04).

## 23. Key Terminology

Immutability · mutable · value object · builder pattern · fluent interface · defensive copy · `List.copyOf`

## 24. References and Further Reading

- Effective Java (Bloch) — items on immutability, builders, defensive copying
- [JLS §17.5 — `final` Field Semantics](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html#jls-17.5)
- [Java SE — `List.copyOf`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection))
- [JEP 395 — Records](https://openjdk.org/jeps/395)

## 25. Next Steps

- [04-equals-hashcode-and-tostring.md](04-equals-hashcode-and-tostring.md) — value equality for immutable types.
- Module 14 — `java.time` immutable API.
- Module 23 — sharing immutable objects across threads.

## Review Checklist

- [x] Learning objectives covered
- [x] Immutability rules and builder pattern at depth
- [x] ImmutabilityBuilderDemo verified Temurin 21.0.12.1
- [x] Mistakes, trade-offs, exercises, Revision and Mastery
- [x] Explain-before-use for builder fluent chaining
