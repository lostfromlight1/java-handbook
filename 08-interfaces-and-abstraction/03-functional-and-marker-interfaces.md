# Functional and Marker Interfaces

> **Status:** Completed  
> **Module:** 08 — Interfaces and Abstraction  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Define **functional interface** and **single abstract method (SAM)**.
- Apply **`@FunctionalInterface`** and interpret compile-time checks.
- Pass **lambda expressions** and **method references** where a functional interface is expected.
- Explain **marker interfaces** and when tagging types without methods is justified.
- Implement **`reduce`-style** algorithms parameterized by a functional interface.

## 2. Prerequisites

- **Ch.01–02** — interfaces, defaults (defaults do **not** count as extra abstract methods if only one abstract remains).
- **Module 04 Ch.04** — lambda preview (syntax refresher below).

Brief refresher on **lambda syntax**: `(a, b) -> a + b` is an expression implementing a **functional interface**’s abstract method without a named class. The compiler checks the lambda against the **target type** (the expected interface).

## 3. Why This Concept Exists

Before Java 8, passing “a bit of behavior” meant **anonymous classes**:

```java
new Runnable() { public void run() { work(); } };
```

Verbose for one method. **Functional interfaces** + **lambdas** let APIs accept behavior as data: sorting keys, stream operators, retry policies.

**Marker interfaces** (no methods) historically tagged types for frameworks (`Serializable`). Modern code often prefers **annotations**, but markers still appear in domain modeling (“this type is an **Entity**”).

## 4. Conceptual Explanation

### 4.1 Functional interface (SAM)

A **functional interface** has **exactly one abstract instance method** (SAM). Additional **`default`**, **`static`**, or methods from `Object` (`equals`, `hashCode`, `toString`) do not increase the abstract count.

```java
@FunctionalInterface
interface IntOp {
    int apply(int a, int b);
}
```

**`@FunctionalInterface`** is optional but recommended: the compiler errors if the interface is not functional (more than one abstract method).

### 4.2 Lambdas as implementations

```java
IntOp add = (a, b) -> a + b;
IntOp mul = (a, b) -> a * b;
```

Each lambda must match **`apply`’s** signature: two `int` parameters, `int` return. Types may be inferred.

### 4.3 Method references

When a lambda only forwards to an existing method:

```java
IntOp add = Integer::sum; // if signature matches
```

Use when it improves readability.

### 4.4 Defaults and functional status

```java
@FunctionalInterface
interface Worker {
    void work();
    default void warmUp() { } // still functional — one abstract work()
}
```

If a second abstract method is added without `default`, the interface stops being functional and existing lambdas break.

### 4.5 Marker interfaces

A **marker interface** declares **no methods** (or only constants). It marks membership in a category:

```java
interface Entity { }
record Product(String id, int price) implements Entity {}
```

Frameworks or business rules use `instanceof Entity` or generics bounds. **`Serializable`** in the JDK is the classic marker—behavior is handled by JVM reflection, not interface methods.

**Trade-off:** markers carry no compile-time operations; **`sealed`** interfaces + types (Module 07) or **annotations** often express intent more richly.

### 4.6 `reduce` pattern

Higher-order methods accept functional interfaces:

```java
static int reduce(int[] values, int identity, IntOp op) {
    int acc = identity;
    for (int v : values) {
        acc = op.apply(acc, v);
    }
    return acc;
}
```

Here `add` combines accumulator with each element: `0 + 1 + 2 + 3 + 4 = 10` in the demo.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Functional interface** | Interface with one abstract instance method |
| **SAM** | Single abstract method that lambdas implement |
| **`@FunctionalInterface`** | Compile-time assertion of functional status |
| **Target typing** | Lambda type inferred from assignment/parameter context |
| **Marker interface** | Interface with no methods—tags a type |
| **Higher-order function** | Method taking behavior (functional interface) as argument |

## 6. Syntax and Language Rules

```java
@FunctionalInterface
interface IntPredicate {
    boolean test(int value);
}

IntPredicate positive = v -> v > 0;
```

- Lambdas may capture **effectively final** locals.
- **Varargs** and generics on SAM follow normal override rules.
- **`java.util.function`** package supplies standard functional types (`Predicate`, `Function`, etc.)—Module 16 deepens streams.

## 7. Step-by-Step Examples

### 7.1 Module demo

Full code: [functional-marker-demo](../examples/core-java/functional-marker-demo/).

**Verified output:**

```text
add=5
mul=6
sum=10
entity=true
```

- `add.apply(2, 3)` → 5; `mul.apply(2, 3)` → 6.
- `reduce(..., 0, add)` folds with addition: `(((0+1)+2)+3)+4` → 10.
- `Product` implements marker `Entity`; `instanceof Entity` is true.

### 7.2 Realistic: passing behavior to services

```java
interface RetryPolicy {
    int maxAttempts();
    default long backoffMillis(int attempt) { return 100L * attempt; }
}
```

Still functional if `maxAttempts` is the only abstract method—defaults supply backoff.

### 7.3 Advanced: custom `@FunctionalInterface` vs `java.util.function`

| Custom `IntOp` | `BinaryOperator<Integer>` |
|--------------|---------------------------|
| Domain-named `apply` | Generic `Integer` boxing |
| Clear in finance/math code | Interops with `Stream.reduce` |

Choose domain names in business code; use `java.util.function` in stream-heavy pipelines.

## 8. Real-World Use Cases

- **Executors** — `Runnable`, `Callable`.
- **Streams** — `map`, `filter` accept functional interfaces.
- **Comparators** — `Comparator.comparing`.
- **Spring / CDI** — callback interfaces (often annotated as functional).
- **Testing** — replace `IntOp` with stub returning constant.

## 9. Internal Behavior

Lambdas are implemented via **invokedynamic** and **lambda metafactory** (JVM)—often `private static` synthetic methods on the enclosing class rather than one `.class` file per anonymous inner class. Behavior is spec-defined; do not rely on specific synthetic names.

## 10. Edge Cases and Limitations

- Lambdas **cannot** declare abstract methods on interfaces—still need a named class for multiple methods.
- **Serializable lambdas** require extra care (not serializable by default).
- **Checked exceptions** in lambdas must be handled or declared on SAM.
- Marker interfaces give **weak** guarantees compared to sealed types.

## 11. Common Mistakes

### Incorrect: two abstract methods with `@FunctionalInterface`

```java
@FunctionalInterface
interface Bad {
    void a();
    void b(); // compile error
}
```

### Incorrect: wrong lambda arity

```java
IntOp o = (a) -> a; // compile error — needs two ints
```

### Incorrect: overusing markers

Tagging everything with empty interfaces instead of types or annotations obscures design.

## 12. Best Practices

- Annotate intentional SAM types with **`@FunctionalInterface`**.
- Name SAM for domain (`IntOp`, `MoneyConverter`) not only `doWork`.
- Prefer **`java.util.function`** when it matches and avoids boxing.
- Replace legacy markers with **`sealed`** hierarchies or annotations when modeling closed sets.

## 13. Design Trade-offs

| Marker interface | Annotation | Sealed interface |
|------------------|--------------|------------------|
| Runtime `instanceof` | Retention policy controls visibility | Compiler-enforced set |
| No behavior | Metadata-rich | Algebraic modeling |

## 14. Performance Considerations

Lambdas avoid anonymous class allocation in many cases; hot loops still profile better with simple method references or plain methods when optimization matters.

## 15. Security Considerations

Passing lambdas across trust boundaries is rare; marker `Serializable` on unintended types can expose serialization gadgets—understand serialization before implementing marker JDK interfaces.

## 16. Java Version and Compatibility Notes

- Lambdas and functional interfaces — Java 8.
- **`@FunctionalInterface`** — Java 8.
- Local variable type inference (`var`) for functional types — Java 10+.

## 17. Related Language Comparisons

JavaScript functions as first-class values; Java keeps **single inheritance** but functional interfaces emulate function types with SAM + lambda sugar.

## 18. Revision and Mastery

- **Gaps:** counting abstract methods when defaults exist; boxing in `Function<Integer,Integer>`.
- **Advanced:** lambda capture of `this`; specialized vs primitive functional interfaces (`IntConsumer`).
- **You should answer:** Why `Runnable` is functional? Is `Entity` functional?

## 19. Exercises

### Beginner

1. Add `IntOp sub = (a, b) -> a - b` and print `sub.apply(5, 2)`.
2. Why is `Entity` not a functional interface?

### Intermediate

3. Write `IntOp max` returning larger of two ints via lambda.
4. Change `reduce` to use `mul` as operator starting identity `1` on `{2,3}`—predict result before running.

### Advanced

5. Sketch `sealed interface Expr` with lambdas avoided in favor of records—when lambdas win?

## 20. Interview Questions

1. What makes an interface functional?
2. Do `default` methods affect functional status?
3. Difference between lambda and anonymous class?
4. Purpose of `@FunctionalInterface`?
5. Example marker interface in JDK?

## 21. Solutions

1. Prints `3`.
2. Zero abstract methods—not a SAM.
3. `(a, b) -> Math.max(a, b)`.
4. `((1*2)*3)=6`.
5. Lambdas win for one-off behavior; sealed + records win for exhaustive evaluation trees.

## 22. Chapter Summary

**Functional interfaces** declare one abstract method so **lambdas** and **method references** supply implementations cheaply. **`@FunctionalInterface`** guards design. **Marker interfaces** tag types without operations—use sparingly. The demo combines **IntOp** lambdas, **reduce**, and **`Entity`** tagging. Next: **API design** with interfaces.

## 23. Key Terminology

Functional interface · SAM · lambda · `@FunctionalInterface` · marker interface · target typing

## 24. References and Further Reading

- [JLS §9.8 — Functional Interfaces](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html#jls-9.8)
- [java.util.function package](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/package-summary.html)
- JEP 335 (lambda)

## 25. Next Steps

- [04-api-design-with-interfaces.md](04-api-design-with-interfaces.md)
- Module 16 — streams and richer `java.util.function` usage

## Review Checklist

- [x] SAM, lambdas, markers, reduce demo verified
- [x] Depth gate met
- [x] FunctionalMarkerDemo Temurin 21.0.12.1
