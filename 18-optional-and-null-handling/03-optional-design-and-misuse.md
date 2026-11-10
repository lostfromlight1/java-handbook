# Optional Design and Misuse

> **Status:** Completed  
> **Module:** 18 — Optional and Null Handling  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Apply Brian Goetz / JDK guidance: Optional is for **returns**, not fields/parameters.
- Model **repository lookups** and **parsers** with Optional cleanly.
- Spot anti-patterns: `Optional.get()` without check, `Optional` in DTO JSON, boxed everywhere.
- Combine Optional with **streams** and **exception** strategies.
- Choose between Optional, exceptions, and sentinel null at system boundaries.

## 2. Prerequisites

- **Ch.01–02** — null, Optional API.

## 3. Why This Concept Exists

Misused Optional adds **allocation** and **confusion** without clarity—`Optional<User> nickname` parameters force callers to wrap values. Clear rules keep Optional a **semantic tool** for “result may be missing,” not a second null.

## 4. Conceptual Explanation

### 4.1 Intended uses

- **Return type** when a value may legitimately be absent (`findByEmail`).
- **Chaining** transformations in service layer.
- **Optional stream** flat-mapping in pipelines.

### 4.2 Discouraged uses

| Anti-pattern | Why |
|--------------|-----|
| Field `Optional<String> middleName` | Serializes poorly; use null + document |
| Parameter `void save(Optional<String> nick)` | Caller ceremony; use overloads or nullable |
| `List<Optional<T>>` | Smell—use flat list or separate queries |
| `optional.get()` blindly | Throws like null |

### 4.3 Repository pattern (demo)

```java
Optional<String> findUserName(int id) {
    return Optional.ofNullable(users.get(id));
}
```

Callers: `orElse("unknown")` for UI default.

### 4.4 JSON / REST

Jackson may serialize Optional if not configured—usually map absent as **omit field** or **null**, not `{ "present": true }` wrapper.

### 4.5 Exceptions vs Optional

| Signal | Mechanism |
|--------|-----------|
| Expected absence | Optional empty |
| Unexpected failure | Exception |
| Validation error | Exception or result type (Module 07 `Result`) |

## 5. Terminology

| Term | Meaning |
|------|---------|
| Absent value | Modeled by empty Optional or documented null |
| Sentinel | Magic value meaning missing (legacy) |

## 6. Syntax and Language Rules

- `Optional` is not `null`—methods should return `Optional.empty()`, not null Optional reference.
- Generic `Optional<T>` cannot hold primitive without boxing wrappers.

## 7. Step-by-Step Examples

Demo: [optional-design-demo](../../examples/core-java/optional-design-demo/).

**Verified output:**

```text
good=Ada
missing=unknown
hasUser1=true
```

## 8. Real-World Use Cases

- Spring Data `findById` → `Optional<Entity>`.
- Configuration service optional keys.
- Parsing optional XML elements.

## 9. Internal Behavior

Same as Ch.02; design focus here.

## 10. Edge Cases and Limitations

- **Recursive** data with Optional wrappers increases GC pressure.
- **equals** on Optional compares value; two empty equal.

## 11. Common Mistakes

### Incorrect

```java
public void update(Optional<String> name) { ... }
```

### Correct

```java
public void update(String name) { ... } // nullable documented
public void updateIfPresent(Consumer<String> c) { ... }
```

## 12. Best Practices

- Use Optional **only where absence is part of the happy path**.
- At boundaries, convert once: `Optional` inside, plain values or DTOs outside.
- Test **empty** and **present** branches explicitly.

## 13. Design Trade-offs

| Boundary | Nullable | Optional |
|----------|----------|----------|
| Public HTTP DTO | common | rare |
| Internal service return | either | Optional OK |
| Database row column | null SQL | map at DAO |

## 14. Performance Considerations

High-frequency internals: nullable faster; Optional at DAO boundary acceptable.

## 15. Security Considerations

Do not use Optional to hide authorization failures—use 403/404 policies explicitly.

## 16. Java Version Notes

No change to guidance in Java 21.

## 17. Related Comparisons

Rust `Option`, Haskell `Maybe`—language-level; Java Optional is library-level only.

## 18. Revision and Mastery

- Why not Optional fields?
- When exception beats empty Optional?

## 19. Exercises

1. Predict `OptionalDesignDemo`.
2. Refactor `get()` usage to `orElseThrow` with supplier message.

## 20. Interview Questions

1. Optional as method parameter—opinion?
2. `Optional.of` vs `ofNullable` for DB column?
3. Serialize Optional to JSON?

## 21. Solutions

1. §7.
2. `return find().orElseThrow(() -> new NotFoundException(id));`

## 22. Chapter Summary

Use **Optional** to announce **optional results**, not to replace **every** null. Avoid fields/parameters/lists of Optionals. Demo shows repository lookup defaults.

## 23. Key Terminology

Anti-pattern · repository lookup · boundary conversion

## 24. References

- [Optional JavaDoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html) (usage notes)

## 25. Next Steps

- **Module 19** — [Modern Java features](../19-modern-java-features/) (per roadmap)

## Review Checklist

- [x] Misuse patterns covered; demo verified
