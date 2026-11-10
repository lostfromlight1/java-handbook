# Sealed Classes

> **Status:** Completed  
> **Module:** 07 — Inheritance and Polymorphism  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare **`sealed` classes and interfaces** with a **`permits`** clause listing allowed subtypes.
- Explain why sealing restricts **extension** and how it supports **exhaustive** control flow.
- Combine **sealed interfaces**, **`record`** implementations, and **`switch`** pattern matching.
- Choose among **sealed**, **final**, **non-sealed**, and open inheritance for domain modeling.
- Recognize compiler checks for **exhaustive switches** over sealed hierarchies.

## 2. Prerequisites

- **Ch.01–03** — inheritance, abstract types, dispatch, LSP.
- **Module 03** — `switch` expressions and pattern matching basics.
- **Module 06 Ch.03** — records as compact carriers.

Brief refresher: a **`record`** declares an immutable data carrier with generated accessors, `equals`, `hashCode`, `toString`, and a canonical constructor. **`sealed`** types declare **exactly which types** may extend or implement them— the compiler enforces the list.

## 3. Why This Concept Exists

Open inheritance (`class Anyone extends Foo`) makes **exhaustive handling** impossible: a `switch` on `Foo` needs `default` because a new subclass may appear in another JAR tomorrow. API owners want **known closed families**:

- **AST nodes** — `Expr`, `Stmt` with fixed variants.
- **API results** — `Result` is either `Ok` or `Err`, not “maybe a third kind later unless you use default”.
- **Payment types** — card, wallet, bank transfer permitted by base; no rogue subtype bypassing validation.

**Sealed types** document and enforce that closed world. With **pattern matching `switch`**, the compiler verifies **all permitted variants** are handled—refactor-safe when adding a permitted subtype (compiler errors until you update switches).

## 4. Conceptual Explanation

### 4.1 Syntax

```java
sealed interface Result permits Ok, Err {}
```

Permitted types must be:

- **Direct** subtypes (listed explicitly, or in same file / nested / as permitted in same compilation unit per JLS rules).
- **final**, **sealed**, or **non-sealed**—cannot be plain extensible without `non-sealed` escape hatch.

```java
record Ok(int value) implements Result {}
record Err(String message) implements Result {}
```

### 4.2 `sealed class` vs `sealed interface`

| | `sealed class` | `sealed interface` |
|---|----------------|---------------------|
| Can hold state | Yes (if abstract/concrete class) | No instance state (constants / default methods) |
| Records implement | Rare as parent | Common (`implements Result`) |
| Use when | Closed class family with shared fields | Closed capability / ADT style |

### 4.3 `non-sealed`

A permitted subtype may be **`non-sealed`**, reopening inheritance below that branch—use sparingly when one branch must stay open.

### 4.4 Exhaustive `switch`

When switching on a **sealed** type and cases cover **every permitted subtype**, no `default` is required—the compiler proves exhaustiveness (demo `describe(Result r)`).

When a new permitted type is added, **all switches must be updated** or compilation fails—safer than silent `default`.

### 4.5 Algebraic data types (ADT) style

`Result` + `Ok` + `Err` models a **sum type** in Java:

```java
static String describe(Result r) {
    return switch (r) {
        case Ok o -> "ok:" + o.value();
        case Err e -> "err:" + e.message();
    };
}
```

Pattern variables `o` and `e` bind the record components accessors.

### 4.6 Second hierarchy in demo: `Shape2D`

```java
sealed interface Shape2D permits Circle, Rect {}
```

`area` switch covers `Circle` and `Rect`—exhaustive. `Circle` area uses `Math.PI * r * r` → for radius 2, output **`12.566370614359172`** on JDK 21 (full `double` string as printed).

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Sealed type** | Class or interface with restricted direct subtypes |
| **`permits`** | Clause naming allowed direct subtypes |
| **Permitted class** | Listed subtype |
| **`non-sealed`** | Permitted subtype that allows further unrestricted extension |
| **Exhaustive switch** | All cases covered; compiler-checked for sealed domain |
| **ADT** | Algebraic data type — fixed set of variants |

## 6. Syntax and Language Rules

```java
sealed class Base permits A, B {}
final class A extends Base {}
final class B extends Base {}

sealed interface I permits R {}
record R(int x) implements I {}
```

- **Permitted classes** must be direct subclasses/implementors.
- **Same file** alternative: permitted types in same compilation unit can omit `permits` on parent (JLS nuance)—demo uses explicit `permits` for clarity.
- **`final` on records** — implicit; good leaf nodes.

## 7. Step-by-Step Examples

### 7.1 Minimal sealed + records

See [sealed-classes-demo](../../../examples/core-java/sealed-classes-demo/).

**Verified output:**

```text
ok:42
err:boom
area=12.566370614359172
```

### 7.2 Adding a variant (compile-time safety)

If you add `record Pending() implements Result {}` to `permits`, `describe` and any other switches **fail to compile** until you add `case Pending p -> ...`. That is intentional—forced updates across handlers.

### 7.3 `non-sealed` branch (sketch)

```java
sealed class Token permits Known, UnknownLeaf {}
non-sealed class UnknownLeaf extends Token {}
class CustomUnknown extends UnknownLeaf {}
```

`UnknownLeaf` subtree stays open; `Known` stays closed.

## 8. Real-World Use Cases

- **JDK:** `java.lang.constant.ConstantDesc` hierarchy (permitted implementations).
- **Domain events** with fixed kinds for audit pipelines.
- **Compiler / IDE** internal ASTs (often sealed in modern Java codebases).
- **HTTP result** types in internal APIs (`Success`, `Failure`, `Redirect`).

## 9. Internal Behavior

Sealing is primarily **compile-time** and **class-file metadata** (`PermittedSubclasses` attribute). JVM loading still uses ordinary class links; restriction enforced when **verifying** subclass relationships at compile time and when loading with reflection (subclass must be permitted).

## 10. Edge Cases and Limitations

- **Reflection / bytecode** libraries can still construct illegal proxies in advanced attacks—not a security boundary alone.
- **Split packages / modules:** permitted types across modules need **module exports/opens** rules (Module 32).
- **Cannot seal** unless every permitted subtype is known at compile time in allowed configuration.
- **Migrating** existing open hierarchy to sealed is breaking for external subclasses.

## 11. Common Mistakes

### Incorrect: orphan subclass

```java
record Oops(int x) implements Result {} // error if not in permits
```

### Incorrect: forgetting new case in switch

After adding permitted type, compilation fails—fix by adding case (feature, not bug).

### Incorrect: using `default` when exhaustiveness wanted

```java
switch (r) {
    case Ok o -> ...
    default -> ... // hides missing Err when Result changes
}
```

Prefer no `default` on sealed exhaustively covered switches.

## 12. Best Practices

- Seal **domain roots** where variant set is a business rule (`Result`, `PaymentKind`).
- Pair sealed interfaces with **records** for leaf data.
- Use **`non-sealed`** only when a branch must stay extensible—document why.
- Update **tests** when adding variants—compiler helps, tests confirm behavior.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Sealed + records | Exhaustive handling, clarity | Less third-party extension |
| Open class + `default` switch | Plugin freedom | Silent misses when variants added |
| `enum` | Smaller fixed sets | No per-case fields like records carry |

Use **enum** when constants only; **sealed + records** when each variant has distinct data.

## 14. Performance Considerations

`switch` on sealed types may compile to **tableswitch** / **lookupswitch** or efficient chains—similar to enum switch. No inherent overhead vs polymorphism; can beat virtual dispatch when combined with devirtualization in same compilation unit.

## 15. Security Considerations

Sealing prevents **arbitrary** implementation types in **your** JAR’s trust model for parsing—still validate data inside each record. Do not rely on sealing alone against malicious class loading.

## 16. Java Version and Compatibility Notes

- **Sealed classes** standardized in **Java 17** (JEP 409); refined in later releases.
- **Pattern matching for switch** finalized in **Java 21**—demo style.
- **Preview history:** pattern `switch` was preview before 21—use 21 rules in this handbook.

## 17. Related Language Comparisons

Kotlin `sealed class`; Rust `enum` with data; Scala ADTs. Java combines sealing with existing inheritance and records.

## 18. Revision and Mastery

- **Gaps:** thinking `permits` is transitive (only **direct** subtypes listed).
- **Advanced:** interaction with pattern guards and null in switch (JLS 21).
- **You should answer:** When prefer `enum` over sealed interface + records?

## 19. Exercises

### Beginner

1. Predict demo output for `describe(new Ok(0))`.
2. Add `case` for a hypothetical `Warn(String msg)` after adding to `permits`.

### Intermediate

3. Implement `boolean isOk(Result r)` with switch expression.
4. Why is `area` switch exhaustive without `default`?

### Advanced

5. Compare modeling `Option<T>` as sealed interface `Some|None` vs `Optional<T>` class.

## 20. Interview Questions

1. What does `sealed` enforce?
2. How do records fit with sealed interfaces?
3. What is an exhaustive switch?
4. Difference between `final` class and `sealed` class?
5. What is `non-sealed` for?

## 21. Solutions

1. `ok:0`.
2. Add `case Warn w -> ...` everywhere `Result` is switched.
3. `return switch (r) { case Ok o -> true; case Err e -> false; };` (extend if Warn added).
4. Only `Circle` and `Rect` implement `Shape2D`; cases cover both.
5. Sealed ADT: variants carry data; `Optional` is single class with nullable—sealed avoids null for “missing” variant.

## 22. Chapter Summary

**Sealed types** close the inheritance graph at compile time, enabling **algebraic** modeling with **records** and **exhaustive switches**. The demo shows `Result` descriptions and `Shape2D` areas without `default` branches. Use sealing when the **set of variants is part of the domain contract**.

## 23. Key Terminology

Sealed class · sealed interface · permits · permitted subtype · non-sealed · exhaustive switch · ADT

## 24. References and Further Reading

- [JLS §8.1.1.2.2 — Sealed Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.1.2.2)
- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [Pattern matching for switch (JLS §15.29)](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.29)

## 25. Next Steps

- Module 08 — interfaces, multiple inheritance of type, `default` methods
- Module 15 — advanced type patterns and sealed hierarchies in APIs

## Review Checklist

- [x] Sealed + records + switch exhaustiveness
- [x] SealedClassesDemo verified (area line from real JDK output)
- [x] Depth gate, exercises, Revision and Mastery
- [x] Version notes for Java 17/21
