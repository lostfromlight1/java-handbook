# Method References and Composition

> **Status:** Completed  
> **Module:** 16 — Lambdas and Functional Programming  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Classify **method reference** forms: static, bound instance, unbound instance, constructor.
- Choose method references vs lambdas for readability.
- Compose **`Function`** pipelines with **`andThen`** and **`compose`**.
- Build **`Comparator`** chains and **`Predicate`** combinations.
- Recognize signature mismatches that block a reference.

## 2. Prerequisites

- **Ch.02** — `Function`, `UnaryOperator`.
- **Module 04 Ch.04** — method reference preview.

## 3. Why This Concept Exists

Lambdas that only call an existing method add noise: `s -> s.trim()` vs `String::trim`. **Method references** document intent and often improve stack traces slightly. **Composition** avoids nested temporary variables when building request/response pipelines.

## 4. Conceptual Explanation

### 4.1 Reference kinds

| Form | Example | Equivalent lambda (conceptually) |
|------|---------|--------------------------------|
| Static | `Integer::sum` | `(a,b) -> Integer.sum(a,b)` |
| Bound instance | `name::length` | `() -> name.length()` (zero-arg SAM) |
| Unbound instance | `String::trim` | `s -> s.trim()` |
| Constructor | `ArrayList::new` | `() -> new ArrayList<>()` |

The compiler must find a method whose **erased signature** matches the SAM after adaptation (including boxing).

### 4.2 `andThen` vs `compose`

For `f.andThen(g)`: apply **`f` then `g`** — `(x) -> g.apply(f.apply(x))`.

For `f.compose(g)`: apply **`g` then `f`** — `(x) -> f.apply(g.apply(x))`.

```java
Function<Integer, Integer> pipeline = times3.andThen(plus2);
pipeline.apply(4); // (4*3)+2 = 14
plus2.compose(times3).apply(4); // same order if compose wraps outer
```

Demo prints `pipeline=14` and `compose=14` for the given definitions.

### 4.3 Comparator composition

```java
Comparator<User> byDeptThenName =
    Comparator.comparing(User::dept).thenComparing(User::name);
```

### 4.4 Predicate composition

```java
Predicate<String> ok = nonBlank.and(s -> s.length() <= 100);
```

## 5. Terminology

| Term | Meaning |
|------|---------|
| Method reference | `Type::member` shorthand for delegating lambda |
| `andThen` | Run second function after first |
| `compose` | Run first function before second (outer view) |

## 6. Syntax and Language Rules

- **Array constructor references** use `Type[]::new` for appropriate SAM.
- **Super** references: `super::method` inside instance methods (rare).
- Reference to **overloaded** method requires target type to pick overload.

## 7. Step-by-Step Examples

Demo: [method-references-demo](../../examples/core-java/method-references-demo/).

```java
Function<String, String> normalize = String::trim.andThen(String::toUpperCase);
```

**Verified output:**

```text
normalized=[ADA, LIN]
pipeline=14
compose=14
```

Normalization trims and uppercases; sort is lexicographic on results.

## 8. Real-World Use Cases

- HTTP client: `andThen` chain for auth header → request → parse.
- **Map** lookups: `Function.identity()` as default mapper.
- DTO mapping pipelines in batch jobs.
- **Comparator** for stable UI table sorting.

## 9. Internal Behavior

Method references generate lambdas via same **LambdaMetafactory** path; callee is invoked directly when linkage resolves.

## 10. Edge Cases and Limitations

- `String::new` is constructor reference; `String::valueOf` is static.
- Instance reference on **null** bound receiver fails at invocation, not creation.
- Varargs methods as references follow SAM adaptation rules—can surprise arity.

## 11. Common Mistakes

### Incorrect — wrong compose order

```java
// Want trim then upper; wrote upper.andThen(trim) — reverses intent
```

### Correct

```java
Function<String, String> normalize = String::trim.andThen(String::toUpperCase);
```

### Incorrect — reference where lambda needs extra logic

```java
// String::trim does not log; add lambda when logging required
```

## 12. Best Practices

- Prefer references for single delegation; use lambdas when **guards** or logging needed.
- Name composed functions: `private static final UnaryOperator<String> NORMALIZE = ...`.
- Document order in `andThen` chains left-to-right as data flow.

## 13. Design Trade-offs

Long `andThen` chains vs private methods:
- Chain: concise in streams.
- Methods: easier debugging, unit test per step.

## 14. Performance Considerations

Composition creates wrapper objects; JIT usually inlines small chains. Profiling wins are rare vs clarity.

## 15. Security Considerations

Composed validators still run on attacker-controlled input—order validators so cheap rejects run first.

## 16. Java Version and Compatibility Notes

Method references since Java 8. `Predicate::and`, `Function::andThen` standard library.

## 17. Related Language Comparisons

Kotlin **function composition** operators (`andThen` as extension). Java uses instance methods on functional interfaces.

## 18. Revision and Mastery

- **Gaps:** confusing `compose` vs `andThen`.
- **You should answer:** Why `normalized` sorts `ADA` before `LIN`?
- **Advanced:** method reference to generic constructor `MyBox::new` with type witness.

## 19. Exercises

1. Predict `MethodReferencesDemo` three lines.
2. Build `Comparator` reversing name after dept.
3. Rewrite `x -> Integer.toString(x)` with a reference.

## 20. Interview Questions

1. Four forms of method references?
2. `f.andThen(g)` evaluation order?
3. When not use a reference?

## 21. Solutions

1. See §7.
2. `thenComparing(Comparator.comparing(User::name).reversed())`.
3. `Integer::toString` or `String::valueOf` per target type.

## 22. Chapter Summary

**Method references** shorten delegating lambdas. **`andThen`/`compose`** wire **Function** pipelines declaratively. Demo normalizes strings and composes integer transforms.

## 23. Key Terminology

Method reference · andThen · compose · Comparator chaining

## 24. References and Further Reading

- [Java Tutorial — Method References](https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html)
- [Module 17](../17-stream-api/01-stream-fundamentals.md) — streams consume this style heavily

## 25. Next Steps

- **Module 17** — Stream API

## Review Checklist

- [x] All reference forms and composition covered
- [x] Demo verified Temurin 21.0.12.1
