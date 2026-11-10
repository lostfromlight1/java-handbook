# Enums

> **Status:** Completed  
> **Module:** 15 — Enums, Records, and Sealed Types  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare **enum types** with named constants, fields, constructors, and methods.
- Explain why enums replace **int/string constant** hacks and how the JVM represents them.
- Use **`name()`**, **`ordinal()`**, **`values()`**, and **`valueOf`** safely.
- Branch with **`switch`** on enums and rely on **singleton** identity semantics.
- Choose enums vs **sealed types** for closed domains (preview Ch.03).
- Avoid common pitfalls: **`ordinal()` for business order**, mutable enum state, and serialization surprises.

## 2. Prerequisites

- **Module 05** — classes, fields, constructors, encapsulation.
- **Module 03** — `switch` expressions and statements.
- **Module 06 Ch.04** — `equals`, `hashCode`, identity vs value.
- **Module 14** — `IllegalArgumentException` for invalid parsing input.

Brief refresher: every **enum constant** is a **public static final** field of the enum type. You cannot `new` an enum; the JVM creates one instance per constant at class initialization time.

## 3. Why This Concept Exists

Before Java 5, teams modeled fixed sets of choices with **`public static final int`** or **`String`** constants:

```java
public static final int STATUS_ACTIVE = 1;
public static final int STATUS_CLOSED = 2;
```

Callers could pass any `int`, arrays could not be type-safe, `switch` had no exhaustiveness, and refactoring did not update scattered magic numbers.

An **`enum`** is a **proper reference type** whose legal values are **enumerated at compile time**. The compiler knows the finite set; `switch` can cover all constants; APIs document allowed states in the type system itself.

## 4. Conceptual Explanation

### 4.1 Declaration and constants

```java
enum Day {
    MONDAY, TUESDAY, WEDNESDAY;
}
```

Each constant is an **enum constant declaration**—implicitly `public static final Day MONDAY = ...`. Enum types **implicitly extend `java.lang.Enum<E>`** and **cannot extend any other class** (single inheritance is already used by `Enum`).

### 4.2 Constructors, fields, and methods

Enum constructors are **private** (or package-private in the enum body). You attach **per-constant data** and **behavior**:

```java
enum Level {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String label;

    Level(String label) {
        this.label = label;
    }

    String label() {
        return label;
    }
}
```

Constants may list constructor arguments: `HIGH("High")` calls `Level(String)` for that constant only.

### 4.3 Standard `Enum` API

| Method | Role | Caveat |
|--------|------|--------|
| `name()` | Exact constant identifier as declared | Stable for logging; not for UI labels |
| `ordinal()` | Zero-based declaration order | **Do not** use for domain ordering if constants may be reordered |
| `values()` | Clone array of all constants | Allocates each call; cache if hot path |
| `valueOf(String)` | Parse by constant **name** | Throws `IllegalArgumentException` if unknown |

Prefer **`Enum.valueOf(Level.class, "HIGH")`** or your own parser when input is messy (trim, case) — see demo `Level.from`.

### 4.4 Identity and equality

Enum constants are **singletons**. **`==` is idiomatic and safe** for enums because each constant exists once per class loader:

```java
if (level == Level.HIGH) { }
```

`equals` agrees for distinct references to the same constant; `compareTo` uses **ordinal** order.

### 4.5 `switch` on enums

Exhaustive `switch` over all constants needs **no `default`** when you list every constant (and none are expected to be added without a compile-time update elsewhere). Adding a constant forces you to update switches — a feature for closed domains.

### 4.6 Implements interfaces

Enums may **`implement`** interfaces — useful for strategy-style behavior per constant:

```java
enum Operation implements IntConsumer {
    LOG { public void accept(int x) { System.out.println(x); } },
    NOP { public void accept(int x) { } };
}
```

Each constant is a **singleton implementing** the interface; no separate class file per variant unless you prefer sealed + records (Ch.03).

### 4.7 Specialized collections

- **`EnumSet`** — compact bit-set representation of a subset of one enum type.
- **`EnumMap`** — array-backed `Map` keyed by enum; efficient when keys are enums.

Use when profiling or API clarity warrants; ordinary `HashSet`/`HashMap` with enum keys still works correctly.

### 4.8 Enum vs `int` / `String` / sealed ADT

| Approach | When it fits |
|----------|----------------|
| **Enum** | Fixed named constants; optional uniform fields; little per-variant data |
| **Sealed + records** | Each variant carries **different** fields (expression trees, `Ok`/`Err`) |
| **`String` codes** | External wire format; validate and map to enum at boundaries |

Module 07 and Ch.03 compare sealing with enums for closed worlds.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Enum type** | Class-like type whose instances are a fixed set of named constants |
| **Enum constant** | One named value of an enum (`Level.HIGH`) |
| **Ordinal** | Integer position in the enum declaration |
| **Type-safe enum** | Enum as a reference type instead of raw codes |

## 6. Syntax and Language Rules

- Enum constants must appear **first** in the body (before fields/methods) unless separated by semicolon after last constant with trailing members.
- Constructors cannot be `public` or `protected`.
- Enum types are implicitly **`final`** — cannot be subclassed.
- **`values()`** and **`valueOf`** are compiler-generated; do not call them on `null` receivers.
- An enum may not be explicitly instantiated with `new`.

```java
enum Planet {
    EARTH(5.97e24),
    MARS(6.39e23);

    private final double massKg;

    Planet(double massKg) {
        this.massKg = massKg;
    }
}
```

## 7. Step-by-Step Examples

### 7.1 Minimal — `EnumsDemo`

[examples/core-java/enums-demo/](../examples/core-java/enums-demo/)

```java
Level level = Level.HIGH;
System.out.println("name=" + level.name());
System.out.println("ordinal=" + level.ordinal());
System.out.println("label=" + level.label());
System.out.println("parse=" + Level.from("low"));
System.out.println("isHigh=" + Level.HIGH.isAtLeast(Level.MEDIUM));
```

**Verified output (Temurin 21.0.12.1):**

```text
name=HIGH
ordinal=2
label=High
parse=LOW
isHigh=true
```

Important lines:

- `ordinal=2` — third constant in declaration (`LOW`, `MEDIUM`, `HIGH`).
- `from("low")` — trims and uppercases before `valueOf`.
- `isHigh=true` — `HIGH.ordinal() >= MEDIUM.ordinal()`.

### 7.2 Realistic — HTTP-style status grouping

```java
enum HttpFamily {
    INFORMATIONAL(100, 199),
    SUCCESS(200, 299),
    CLIENT_ERROR(400, 499);

    private final int min, max;

    HttpFamily(int min, int max) {
        this.min = min;
        this.max = max;
    }

    static HttpFamily of(int code) {
        for (HttpFamily f : values()) {
            if (code >= f.min && code <= f.max) return f;
        }
        throw new IllegalArgumentException("code=" + code);
    }
}
```

Lookup by range is clearer than ordinal comparisons.

### 7.3 Advanced — enum implementing interface + `EnumSet`

```java
enum Feature implements Predicate<Config> {
    TLS(cfg -> cfg.hasTls()),
    AUDIT(cfg -> cfg.hasAudit());

    private final Predicate<Config> delegate;
    Feature(Predicate<Config> delegate) { this.delegate = delegate; }
    public boolean test(Config c) { return delegate.test(c); }
}

EnumSet<Feature> enabled = EnumSet.of(Feature.TLS, Feature.AUDIT);
```

Each constant holds behavior; `EnumSet` tracks enabled flags compactly.

## 8. Real-World Use Cases

- **Domain state machines** — order status, payment state, job lifecycle.
- **API error codes** — stable enum mapped to HTTP status in controllers.
- **Configuration toggles** — environment names (`DEV`, `STAGING`, `PROD`).
- **JUnit / test lifecycle** — framework enums for annotations and listeners.

## 9. Internal Behavior

Enums are **classes**. `javac` emits a `class Level extends Enum<Level>` with static fields for each constant, static initializer block constructing them, and synthetic `values()` / `valueOf`. At class initialization time, constants are created **once** in declaration order (ordinal).

Serialization (when used) has special handling so **constant identity** survives deserialization by name, not by ordinary object graph rules alone — still avoid storing mutable state on enums.

## 10. Edge Cases and Limitations

- Reordering constants changes **ordinal** and can break persisted ordinal-based data.
- `values()` returns a **new array** each call (JDK implementation detail — treat as such).
- **`valueOf("high")`** fails — names are case-sensitive; normalize external input first.
- Enum constants in **switch** must use unqualified constant names (`case HIGH` not `case Level.HIGH` in older style; in switch rules use `case HIGH`).

## 11. Common Mistakes

### Incorrect — business logic on `ordinal()`

```java
boolean isUrgent(Priority p) {
    return p.ordinal() >= Priority.HIGH.ordinal();
}
```

Reordering enum declarations silently changes meaning.

### Corrected

```java
enum Priority {
    LOW, MEDIUM, HIGH;
    boolean isAtLeast(Priority other) {
        return this.compareTo(other) >= 0; // still declaration order — document or use explicit rank field
    }
}
```

Better: store an explicit **`int rank`** in the constructor.

### Incorrect — mutable enum field

```java
enum Counter { A;
    int count; // any holder can mutate shared enum state
}
```

### Corrected

Keep enum state **immutable**; accumulate in instances of ordinary classes.

## 12. Best Practices

- Treat enums as **immutable** value objects.
- Parse external strings at **boundaries**; fail with `IllegalArgumentException` or `Optional`.
- Use **`name()`** for logs and persistence keys; use **`label()`** (or similar) for UI.
- Prefer **`EnumSet`/`EnumMap`** in performance-sensitive enum-keyed structures.
- Document whether new constants require **database migration** or API version bumps.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Enum | Simple, idiomatic, JVM-optimized `switch` | All constants share one field layout |
| Sealed + records | Distinct shapes per variant | More types and files |
| String in DB | Flexible schema | Validation burden at read time |

## 14. Performance Considerations

Enum `switch` often compiles to **tableswitch** or **lookupswitch** — O(1) on constant count. `EnumSet` uses bit vectors for up to 64 constants (implementation-dependent sizing). `values()` allocation matters only in tight loops — use `EnumSet.allOf` or cached arrays.

## 15. Security Considerations

Do not trust unvalidated user strings passed to `valueOf` — treat as injection into application logic. Map wire values through allow-lists. Avoid exposing raw `ordinal()` across trust boundaries.

## 16. Java Version and Compatibility Notes

Enums since **Java 5**. Switch rules and exhaustiveness interact with modern pattern matching (Module 03 / Ch.03) but enum switches use **constant labels**, not type patterns. No Java 21-specific enum syntax change.

## 17. Related Language Comparisons

Rust and Kotlin use **algebraic enums** with per-variant data. Java splits that use case between **classic enums** (uniform shape) and **sealed + records** (variant data). C# enums are closer to Java but allow `[Flags]` bit masks without `EnumSet`.

## 18. Revision and Mastery

- **Gaps:** assuming `ordinal()` is stable business order; forgetting enums cannot extend classes.
- **You should answer:** Why does `EnumsDemo` print `parse=LOW` from `"low"`?
- **Advanced:** enum-specific `Serialization` and `readResolve` for singleton enforcement when customizing serialization.

## 19. Exercises

### Beginner

1. Predict all five lines of `EnumsDemo` without running.
2. Add `CRITICAL` above `HIGH` with label `"Critical"`. What happens to `HIGH.ordinal()`?

### Intermediate

3. Implement `Level fromLabel(String label)` using `values()` and `label()` equality.
4. When is `==` preferred over `equals` for enums?

### Advanced

5. Design: fixed set of error codes — enum only, or sealed `ErrorDetail`? Justify.

## 20. Interview Questions

1. Can you extend an enum?
2. Difference between `name()` and `toString()` on enums?
3. Why might `EnumSet` beat `HashSet<Level>`?

## 21. Solutions

1. See §7.1 verified output.
2. `HIGH.ordinal()` becomes **3** (was 2).
3. Loop `values()`, match `label()` case-insensitively or exact per spec.
4. Same constant is singleton; `==` is null-safe and idiomatic; `equals` works but adds no value among constants.
5. Uniform codes → enum; each code with different fields → sealed + records.

## 22. Chapter Summary

**Enums** model **closed sets** of named constants as real types. Constructors attach data; methods encode behavior. Use **`name()`/`valueOf`** for identity strings, not ordinals, for domain rules. **`EnumsDemo`** shows labels, parsing, and comparison. Next: **records** for immutable data carriers without enum’s fixed constant set.

## 23. Key Terminology

Enum · enum constant · ordinal · `valueOf` · `EnumSet` · exhaustiveness

## 24. References and Further Reading

- [JLS 8.9 — Enum Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.9)
- [Java Tutorial — Enum Types](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html)
- [Module 03 — switch](../03-control-flow/02-switch-statements.md)

## 25. Next Steps

- [02-records.md](02-records.md) — compact immutable data classes
- [03-sealed-types-and-patterns.md](03-sealed-types-and-patterns.md) — sealed hierarchies with pattern matching

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; `EnumsDemo` verified Temurin 21.0.12.1
- [x] Enum vs sealed trade-off introduced
- [x] Revision and Mastery, exercises, references
