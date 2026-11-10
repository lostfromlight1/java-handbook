# Lambdas and Functional Interfaces

> **Status:** Completed  
> **Module:** 16 — Lambdas and Functional Programming  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Write lambdas for common **SAM** shapes: no arg, one arg, two args, void return.
- Use **`@FunctionalInterface`** and standard types in **`java.util.function`**.
- Apply **target typing** and **type inference** for lambda parameters.
- Convert between lambdas, anonymous classes, and method references when signatures match.
- Avoid pitfalls: **boxing**, wrong arity, non-functional interfaces, exception handling.

## 2. Prerequisites

- **Ch.01** — closures, higher-order methods.
- **Module 08 Ch.03** — functional interfaces and defaults.

## 3. Why This Concept Exists

Without a shared vocabulary (`Predicate`, `Function`, `Consumer`), every library invents its own callback interface. **`java.util.function`** supplies interoperable SAM types so streams, `Optional`, and `CompletableFuture` compose consistently.

## 4. Conceptual Explanation

### 4.1 Lambda syntax variants

```java
() -> "Hello"
s -> s.length()
(a, b) -> a + b
(x, y) -> { int z = x * y; return z; }
```

Expression lambdas must be compatible with the SAM’s return type; block lambdas need explicit `return` for non-void SAMs.

### 4.2 Target typing

The compiler infers the lambda’s type from **context**:

```java
Predicate<String> p = s -> !s.isBlank(); // target: Predicate.test
```

Assignment, method argument, cast, or conditional branch can provide the target.

### 4.3 Core `java.util.function` types

| Interface | SAM | Typical use |
|-----------|-----|-------------|
| `Predicate<T>` | `test(T) -> boolean` | filter, validation |
| `Function<T,R>` | `apply(T) -> R` | map, transform |
| `Consumer<T>` | `accept(T) -> void` | forEach side effect |
| `Supplier<T>` | `get() -> T` | lazy factories, orElseGet |
| `UnaryOperator<T>` | `apply(T) -> T` | same-type pipeline |
| `BiFunction<T,U,R>` | `apply(T,U) -> R` | combine two inputs |

Primitive specializations (`IntPredicate`, `ToIntFunction`, …) reduce boxing in numeric hot paths.

### 4.4 Defaults and `@FunctionalInterface`

If an interface has **two** abstract instance methods, it is **not** functional—even with many defaults. `@FunctionalInterface` triggers a compile error when the count is wrong.

### 4.5 Boxing in predicates

`Predicate<Integer>` on a stream of `int` may autobox; prefer `IntPredicate` or `IntStream` when profiling shows cost.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Arity | Number of lambda parameters |
| Target type | Functional interface expected at the use site |
| `java.util.function` | Standard SAM library (Java 8+) |

## 6. Syntax and Language Rules

- Lambda parameter types: optional if target is concrete; required in some ambiguous overloads.
- **`var` in lambda parameters** (Java 11+): both or neither parameters must use `var` in a list.
- Checked exceptions: SAM’s `throws` clause must accommodate lambda body or wrap/catch.

## 7. Step-by-Step Examples

Demo: [lambdas-interfaces-demo](../../examples/core-java/lambdas-interfaces-demo/).

```java
Predicate<String> nonBlank = s -> s != null && !s.isBlank();
Function<String, Integer> length = String::length;
Supplier<String> greeting = () -> "Hello";
```

**Verified output:**

```text
greeting=Hello
lenAda=3
nonBlankCount=2
mapped=[ADA, BOB]
```

Only `"Ada"` and `"Bob"` pass `nonBlank` (empty and whitespace-only rejected).

### 7.2 Custom `@FunctionalInterface`

```java
@FunctionalInterface
interface CsvFormatter {
    String format(List<String> rows);
    default String header() { return "name"; }
}
```

Still functional—one abstract `format`.

## 8. Real-World Use Cases

- **Spring `BeanValidator`:** predicates on configuration properties.
- **Resilience4j:** `Supplier` for guarded calls.
- **Event buses:** `Consumer<Event>` subscriptions.
- **Repository layer:** `Function<Id, Entity>` for cache-aside loaders.

## 9. Internal Behavior

Generic functional interfaces erase type parameters at runtime like any generic type. **`Predicate<String>`** and **`Predicate<Object>`** are different **erased** `test(Object)` bridges at bytecode level with bridge methods when overriding—same erasure story as Module 13.

## 10. Edge Cases and Limitations

- **`Object` methods** on interfaces do not count as SAM methods.
- Lambdas assigned to `Serializable` functional interfaces may need cast tricks—avoid unless required.
- Overload resolution with multiple applicable functional interfaces may need explicit cast.

## 11. Common Mistakes

### Incorrect — non-blank check without null guard

```java
Predicate<String> bad = String::isBlank; // NPE if element is null in stream
```

### Correct

```java
Predicate<String> nonBlank = s -> s != null && !s.isBlank();
```

### Incorrect — extra abstract method silently breaks lambda

```java
interface Broken {
    void a();
    void b();
}
// Broken b = () -> {}; // compile error
```

## 12. Best Practices

- Import standard functional types instead of one-off duplicates.
- Document nullability on **custom** functional interfaces.
- Prefer **`method reference`** when delegating exactly (`String::toUpperCase`).
- Keep lambdas short; extract methods for multi-branch logic.

## 13. Design Trade-offs

Custom SAM vs `Function`:
- **Named SAM** (`CsvFormatter`) documents intent at API boundary.
- **`Function`** reduces type surface but erases domain meaning.

## 14. Performance Considerations

Avoid `Function<Integer, Integer>` in tight numeric loops if boxing dominates; use `IntUnaryOperator` or primitives stream.

## 15. Security Considerations

Validators implemented as lambdas still execute untrusted **data**—sanitize before `test`, not after.

## 16. Java Version and Compatibility Notes

`java.util.function` since Java 8. Java 9+ added `Predicate.not`, optional helpers; Java 21 stable.

## 17. Related Language Comparisons

Java has no `func` keyword—interfaces carry types. Scala and Kotlin unify syntax; Java optimizes for **JVM** and **existing APIs**.

## 18. Revision and Mastery

- **Gaps:** using `get()` on wrong functional type.
- **You should answer:** Why `nonBlankCount=2` for four strings?
- **Advanced:** dual-interface lambda (rare) via intersection types `(Runnable & Serializable) () -> {}`.

## 19. Exercises

1. Predict `LambdasInterfacesDemo` output.
2. Write `Predicate<Integer>` for primes under 100 (slow OK).
3. Chain `and` / `or` on predicates for composite validation.

## 20. Interview Questions

1. Difference between `Consumer` and `Function`?
2. What does `@FunctionalInterface` do?
3. When use `Supplier` vs passing a value directly?

## 21. Solutions

1. See §7.1.
2. Test divisibility up to `sqrt(n)`.
3. `nonBlank.and(s -> s.length() < 100)` etc.

## 22. Chapter Summary

**Lambdas** implement **SAM** types with concise syntax. **`java.util.function`** standardizes predicates, functions, consumers, and suppliers for streams and modern APIs. **`LambdasInterfacesDemo`** filters blank strings and maps survivors to upper case.

## 23. Key Terminology

Predicate · Function · Consumer · Supplier · target typing

## 24. References and Further Reading

- [java.util.function package](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/package-summary.html)
- [Ch.01](01-functional-fundamentals.md)

## 25. Next Steps

- [03-method-references-and-composition.md](03-method-references-and-composition.md)

## Review Checklist

- [x] Depth gate; demo verified
- [x] Standard functional types covered
- [x] Common mistakes and exercises
