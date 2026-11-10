# Optional API

> **Status:** Completed  
> **Module:** 18 — Optional and Null Handling  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Create **`Optional`** with `of`, `ofNullable`, `empty`.
- Transform with **`map`**, **`flatMap`**, **`filter`** without nested null checks.
- Provide defaults with **`orElse`**, **`orElseGet`**, **`or`**, **`orElseThrow`**.
- Avoid **`get()`** without presence check.
- Integrate Optional with **streams** (`optional.stream()` Java 9+).

## 2. Prerequisites

- **Ch.01** — null and NPE.
- **Module 16** — `Supplier`, method references.

## 3. Why This Concept Exists

Nested `if (x != null) { y = x.getY(); ... }` obscures business logic. **`Optional<T>`** models an **optional single value** with a small fluent API—presence explicit in the **type** of return values, not in every caller variable.

## 4. Conceptual Explanation

### 4.1 Creation rules

| Factory | Use |
|---------|-----|
| `Optional.of(v)` | Non-null `v`; throws NPE if null |
| `Optional.ofNullable(v)` | Null → empty |
| `Optional.empty()` | Shared empty instance |

### 4.2 Query

- `isPresent()` / `isEmpty()` (Java 11+)
- Prefer functional style over raw `if (opt.isPresent()) opt.get()`.

### 4.3 map vs flatMap

```java
optional.map(String::trim)           // Optional<String>
optional.flatMap(this::wrapPrefix)    // wrapPrefix returns Optional
```

`flatMap` avoids `Optional<Optional<T>>`.

### 4.4 Defaults

- `orElse(x)` — always evaluates `x` even if present (avoid expensive `x`).
- `orElseGet(Supplier)` — lazy when empty.
- `or(() -> otherOptional)` — Java 9+ optional fallback without unwrapping.

### 4.5 Chaining example from demo

`findLength` trims, filters empty, maps length—returns `"0"` if any step empty.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Optional | Container for zero or one non-null reference |
| empty | Absent value (singleton) |

## 6. Syntax and Language Rules

- `Optional` is **final**; not for primitives (use `OptionalInt`, etc.).
- Do not use as field type or method parameter (Ch.03).

## 7. Step-by-Step Examples

Demo: [optional-api-demo](../../examples/core-java/optional-api-demo/).

**Verified output:**

```text
present=Ada
emptyIsEmpty=true
nullableEmpty=true
upper=ADA
fromEmpty=fallback
len=2
```

`fromEmpty` uses `or` because `flatMap` on empty yields empty, then `or` supplies `fallback`.

## 8. Real-World Use Cases

- Repository `findById` returning `Optional<User>`.
- Config property lookup.
- Stream terminal `max` already returns Optional.

## 9. Internal Behavior

`Optional` holds a private `value` reference; `empty` is singleton. **Serialization** supported but rarely appropriate for domain model.

## 10. Edge Cases and Limitations

- **`of(null)`** throws immediately.
- Optional does not replace **null on wire** (JSON) without mapping layer.
- **`map` on empty** skips function—short-circuit.

## 11. Common Mistakes

### Incorrect

```java
Optional<String> o = optional.get(); // NoSuchElementException
```

### Correct

```java
String s = optional.orElse("default");
```

### Incorrect — expensive orElse

```java
opt.orElse(expensive()); // expensive runs even if present
```

### Correct

```java
opt.orElseGet(this::expensive);
```

## 12. Best Practices

- Return Optional from methods where absence is normal.
- Never return null Optional—use `empty()`.
- Chain `map`/`filter` instead of nested ifs.

## 13. Design Trade-offs

Optional vs nullable return:
- Optional: self-documenting, composable, small allocation.
- Nullable: less ceremony for hot internal calls.

## 14. Performance Considerations

Optional allocates; avoid in tight loops on primitives—use sentinels or `OptionalInt`.

## 15. Security Considerations

`orElseThrow` messages must not leak internals to clients.

## 16. Java Version Notes

`isEmpty`, `or`, `stream` since Java 9/11. Stable in Java 21.

## 17. Related Comparisons

Guava `Optional` pre-dates JDK; prefer `java.util.Optional` in new Java code.

## 18. Revision and Mastery

- Why `len=2` for `"  hi  "`?
- `orElse` vs `orElseGet`?

## 19. Exercises

1. Predict demo lines.
2. Rewrite three-level null check with flatMap chain.

## 20. Interview Questions

1. `of` vs `ofNullable`?
2. `map` vs `flatMap` on Optional?
3. Is Optional serializable?

## 21. Solutions

1. §7.
2. Chain `flatMap` steps returning Optional.

## 22. Chapter Summary

**Optional** makes absence explicit and chains transforms safely. Use lazy defaults, avoid `get()`, prefer `flatMap` for nested Optional. Demo covers creation, map, `or`, and multi-step string pipeline.

## 23. Key Terminology

Optional · ofNullable · flatMap · orElseGet

## 24. References

- [Optional class](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html)

## 25. Next Steps

- [03-optional-design-and-misuse.md](03-optional-design-and-misuse.md)

## Review Checklist

- [x] Demo verified
