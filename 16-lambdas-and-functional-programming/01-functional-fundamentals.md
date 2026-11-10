# Functional Fundamentals

> **Status:** Completed  
> **Module:** 16 — Lambdas and Functional Programming  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain **functional programming** ideas as they appear in Java (not as a separate language paradigm).
- Treat **behavior as data**: pass sorting rules, mappers, and validators as arguments.
- Distinguish **higher-order functions** (methods that accept or return behavior) from ordinary methods.
- Describe **lambda expressions** as concise implementations of a **functional interface**’s single abstract method (SAM).
- Explain **closures** and the **effectively final** rule for captured local variables.
- Compare anonymous classes, lambdas, and named classes for one-off behavior.

## 2. Prerequisites

- **Module 08 Ch.03** — functional interfaces and SAM (brief refresher below).
- **Module 04** — methods, pass-by-value, references.
- **Module 12** — `List`, `Comparator`, collections.

**Refresher:** A **functional interface** has exactly one abstract instance method. A **lambda** `x -> x + 1` is an expression whose type is inferred from context (the **target type**), usually that interface.

## 3. Why This Concept Exists

Enterprise code repeats patterns: “sort by this key,” “validate each row,” “map DTO to entity.” Copy-pasting loops with tiny variations produces bugs and resists change.

**Functional style in Java** (since Java 8) means APIs accept **behavior** as parameters—`Comparator`, `Function`, stream operations—so one loop skeleton serves many policies. You still write classes and mutable state where appropriate; you gain **composition** and **testability** where behavior varies.

Before lambdas, behavior was passed via **anonymous classes**:

```java
copy.sort(new Comparator<String>() {
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
});
```

Lambdas remove boilerplate when the interface is a SAM.

## 4. Conceptual Explanation

### 4.1 Imperative vs declarative (in Java terms)

**Imperative:** you describe *how* to loop step by step. **Declarative (local):** you describe *what* transformation you want (`sorted`, `map`) and the library iterates. Streams (Module 17) deepen this; this chapter uses small custom helpers and `Comparator`.

### 4.2 First-class behavior

Java does not make functions first-class values like some languages. Instead, **functional interface instances** (lambdas, method references, anonymous classes) stand in for “a function object.”

### 4.3 Higher-order functions

A **higher-order** method takes or returns behavior:

```java
static <T, R> List<R> map(List<T> items, Function<T, R> fn) {
    List<R> out = new ArrayList<>();
    for (T item : items) {
        out.add(fn.apply(item));
    }
    return out;
}
```

Callers supply `fn`; the method supplies the loop structure.

### 4.4 Closures and effectively final

A lambda may **read** local variables from the enclosing scope. Those variables must be **effectively final**—never reassigned after initialization:

```java
int base = 10;
IntUnaryOperator addBase = x -> x + base; // OK
// base = 11; // would break compilation of addBase
```

The lambda captures the *value* at use time for primitives (conceptually); for references, the reference must stay pointing at the same object if you rely on stability.

### 4.5 Purity and side effects

A **pure** function has no side effects and depends only on inputs. Java lambdas often close over services, loggers, or mutable collections—**impure** but practical. Parallel streams (Module 17 Ch.04) punish shared mutation harshly.

### 4.6 Relationship to OOP

Lambdas do not replace objects. They complement **strategy** objects: instead of a named `SortingStrategy` class for one line, a lambda suffices; for complex policy, a class remains clearer.

## 5. Terminology

| Term | Meaning |
|------|---------|
| SAM | Single abstract method on a functional interface |
| Target type | Expected functional interface type at a lambda’s use site |
| Higher-order | Method/function taking or returning other functions/behavior |
| Effectively final | Local variable never reassigned; may be captured by lambdas |
| Closure | Lambda body that uses variables from an enclosing scope |

## 6. Syntax and Language Rules

- Lambda form: `(params) -> expr` or `(params) -> { statements; }`.
- Parameter types optional when the compiler can infer from the target type.
- Lambdas cannot declare checked exceptions not allowed by the SAM unless handled—same rules as anonymous classes.
- **`this` inside a lambda** refers to the enclosing class instance, not the lambda (lambdas have no own instance type name).

## 7. Step-by-Step Examples

Runnable demo: [examples/core-java/functional-fundamentals-demo/](../../examples/core-java/functional-fundamentals-demo/).

### 7.1 Closure and apply

```java
int base = 10;
IntUnaryOperator addBase = x -> x + base;
addBase.applyAsInt(5); // 15
```

`base` is effectively final; the operator adds the captured value.

### 7.2 Behavior as data — sorting

```java
copy.sort(Comparator.comparingInt(String::length));
```

`Comparator.comparingInt` is a higher-order factory: you pass **length extraction** (`String::length`), the library builds a full comparator.

### 7.3 Custom map

See `FunctionalFundamentalsDemo.map` — same structural loop, caller supplies `Function`.

**Verified output (Temurin 21.0.12.1):**

```text
apply=15
sorted=[fig, kiwi, apple]
transformed=[10, 20, 30]
```

`sorted` orders by length: `fig`(3), `kiwi`(4), `apple`(5).

## 8. Real-World Use Cases

- **REST validation:** `Predicate<Request>` chains for field rules.
- **Batch ETL:** `Function<Row, Entity>` per source format without duplicating iteration.
- **Pricing rules:** `UnaryOperator<Money>` composition for discount stacks.
- **Testing:** inject lambdas as doubles instead of heavyweight mocks for simple behavior.

## 9. Internal Behavior

Lambdas are implemented via **invokedynamic** and `LambdaMetafactory` (Java 8+). The compiler generates a synthetic **bootstrap** that links the lambda body to the SAM at first execution. You may see synthetic methods in bytecode (`lambda$main$0`); behavior is **not** recompiled on every call after linkage (HotSpot implementation detail).

Anonymous classes remain separate `.class` files per site; lambdas often share lighter linkage—**not** a performance guarantee for all workloads.

## 10. Edge Cases and Limitations

- Lambdas cannot replace **abstract classes** directly—only functional interfaces.
- **Serialization** of lambdas is fragile (captures, synthetic names)—prefer named classes for long-lived serialized behavior.
- Null behavior follows the SAM—`Comparator` null-handling uses `nullsFirst`/`nullsLast` explicitly.

## 11. Common Mistakes

### Incorrect — mutating captured non-final variable

```java
int count = 0;
list.forEach(x -> count++); // compile error
```

### Correct — use accumulator object or stream reduction

```java
int total = list.stream().mapToInt(String::length).sum();
```

### Incorrect — assuming lambda is its own `this`

```java
Runnable r = () -> System.out.println(this); // enclosing instance's this
```

## 12. Best Practices

- Prefer **method references** when a lambda only delegates (`String::length`).
- Extract complex lambdas to **private methods** or **named classes**.
- Keep captured state **minimal**; document thread-safety if behavior is shared.
- Name functional parameters in APIs (`Predicate<String> nonBlank`) for call-site readability.

## 13. Design Trade-offs

| Approach | When | Risk |
|----------|------|------|
| Lambda | One-liner behavior | Hard to debug in large stacks |
| Anonymous class | Need extra fields named on the object | Verbose |
| Named class | Reused / stateful policy | More files |

## 14. Performance Considerations

Tiny lambdas are usually inlined by the JIT like equivalent code. Capturing many values allocates a **capture object**. Hot paths with millions of allocations may profile better with explicit loops—measure first.

## 15. Security Considerations

Do not pass user-controlled code without sandboxing—Java lambdas are not arbitrary script injection, but **behavior from untrusted plugins** still runs with your privileges.

## 16. Java Version and Compatibility Notes

Lambdas and method references since **Java 8**. **Java 21** adds no lambda syntax change; **var** in lambdas remains limited (parameters cannot use `var` until context allows—prefer explicit types in public APIs).

## 17. Related Language Comparisons

Kotlin has **function types** (`(Int) -> Int`) as first-class syntax. Java uses **functional interfaces** as the carrier type. C# **delegates** resemble SAM interfaces plus `Func<>`/`Action<>`.

## 18. Revision and Mastery

- **Gaps:** confusing “functional programming” with “no mutation anywhere.”
- **You should answer:** Why must `base` be effectively final in the demo?
- **Advanced:** `LambdaMetafactory` vs anonymous class allocation in your JDK’s `-XX:+PrintInlining`.

## 19. Exercises

### Beginner

1. Predict all three lines of `FunctionalFundamentalsDemo` without running.
2. Write a lambda `IntPredicate` that tests even numbers.

### Intermediate

3. Implement `filter(List<T>, Predicate<T>)` like `map` in the demo.
4. When is an anonymous class still preferable to a lambda?

### Advanced

5. Design a retry helper taking `Supplier<T>`—where do checked exceptions from `get` go?

## 20. Interview Questions

1. What is a functional interface?
2. What does “effectively final” mean?
3. Can a lambda implement two interfaces at once?

## 21. Solutions

1. See §7.3 verified output.
2. `n -> n % 2 == 0`.
3. Loop adding when `predicate.test(item)`.
4. When you need a **named type**, extra state fields, or **multiple methods** beyond SAM + defaults.
5. Propagate per `Supplier.get()` signature or wrap in `RuntimeException` with clear policy.

## 22. Chapter Summary

Java’s functional features center on **SAM interfaces** and **lambdas** that implement them, enabling **higher-order** APIs without abandoning the class model. Closures obey **effectively final** capture rules. **`FunctionalFundamentalsDemo`** shows closure arithmetic, `Comparator` behavior, and a custom `map`.

## 23. Key Terminology

Functional interface · SAM · lambda · closure · effectively final · higher-order

## 24. References and Further Reading

- [JLS 9.8 — Functional Interfaces](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html#jls-9.8)
- [JLS 15.27 — Lambda Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.27)
- [Module 08 Ch.03](../08-interfaces-and-abstraction/03-functional-and-marker-interfaces.md)

## 25. Next Steps

- [02-lambdas-and-functional-interfaces.md](02-lambdas-and-functional-interfaces.md) — `java.util.function` toolkit
- [03-method-references-and-composition.md](03-method-references-and-composition.md) — `::` and composing functions

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; demo verified Temurin 21.0.12.1
- [x] Explain-before-use for SAM/target type
- [x] Revision and Mastery, exercises, references
