# Null and NullPointerException

> **Status:** Completed  
> **Module:** 18 — Optional and Null Handling  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain **null** as absence of object reference in Java’s type system.
- Read **`NullPointerException` (NPE)** stack traces and locate dereference sites.
- Apply **defensive checks**, **guard clauses**, and **null-safe defaults**.
- Contrast null with **empty** strings, **empty collections**, and **Optional** (Ch.02).
- Describe **null-hostile** vs **nullable** API design at boundaries.

## 2. Prerequisites

- **Module 02** — references vs primitives.
- **Module 14** — exceptions.

## 3. Why This Concept Exists

**Tony Hoare’s “billion-dollar mistake”**: null references make “absent value” implicit. Callers forget checks; failures happen far from the bug. Java keeps null for compatibility; modern APIs layer **Optional** and **annotations** (`@Nullable`) to document intent—null itself remains.

## 4. Conceptual Explanation

### 4.1 What null is

A **reference variable** holds either an address of an object or **null** (no object). Primitives cannot be null. Dereferencing null (`missing.length()`) throws **NPE**.

### 4.2 NPE is unchecked

`NullPointerException` extends `RuntimeException`. The JVM throws it at the **dereference** site, not at assignment of null.

### 4.3 Layers of defense

1. **Validate at boundaries** (HTTP params, DB rows).
2. **Fail fast** with clear messages on illegal null.
3. **Return null** only when contract documents it—or prefer Optional for new internal APIs (Ch.02).
4. **Use empty collections** instead of null lists.

### 4.4 null vs empty

| | null | empty `""` | empty list |
|---|------|------------|------------|
| Meaning | unknown/absent object | known empty text | known zero elements |
| `length`/`size` | NPE if null | 0 | 0 |

## 5. Terminology

| Term | Meaning |
|------|---------|
| Dereference | Using `.` or method on reference |
| NPE | Runtime error when reference is null |
| Guard clause | Early return/throw on invalid input |

## 6. Syntax and Language Rules

- `== null` and `!= null` are reference tests.
- **`Objects.requireNonNull`** throws NPE with message if null.
- **Pattern matching** `if (x instanceof String s)` does not apply to null `x` in the same way as non-null patterns.

## 7. Step-by-Step Examples

Demo: [null-npe-demo](../../examples/core-java/null-npe-demo/).

**Verified output:**

```text
safe=unknown
npe=NullPointerException
user=Ada
display=<anonymous>
```

`orDefault` avoids NPE; raw `missing.length()` catches NPE; `displayName(null)` maps to sentinel string.

## 8. Real-World Use Cases

- Map lookups returning null for missing keys (`Map.get`).
- JDBC `ResultSet.getString` may return null for SQL NULL.
- JSON null fields deserialized to Java null.

## 9. Internal Behavior

NPE is thrown by JVM **quick** bytecodes (`invokevirtual` on null receiver) with stack trace cost. JDK null checks on newer APIs sometimes use **`Objects.requireNonNull`** in constructors.

## 10. Edge Cases and Limitations

- **Auto-unboxing** null `Integer` → NPE on unbox.
- **Chained calls** `a.getB().getC()` — NPE does not say which hop was null (use helper methods or Optional chain).
- **`equals` on null** — call on constant: `"x".equals(maybeNull)`.

## 11. Common Mistakes

### Incorrect

```java
if (s.equals("admin")) { } // NPE if s null
```

### Correct

```java
if ("admin".equals(s)) { }
// or
if (s != null && s.equals("admin")) { }
```

## 12. Best Practices

- Document nullable returns in Javadoc.
- Prefer **empty collections** over null returns for “no items.”
- Validate constructor parameters with `requireNonNull`.

## 13. Design Trade-offs

Sentinel null vs Optional vs exception:
- Null: lightweight, easy to forget.
- Optional: explicit absence in return types.
- Exception: invalid state, not mere absence.

## 14. Performance Considerations

Null checks are cheap. Optional adds allocation—use at API boundaries, not every field access.

## 15. Security Considerations

Null deref in security gate may skip check if code path assumes non-null after weak validation.

## 16. Java Version Notes

**JEP 358** (Java 14+) improved NPE messages (“Helpful NullPointerExceptions”)—shows which variable was null when JVM can determine it (HotSpot).

## 17. Related Comparisons

Kotlin nullable types `String?` compile-time enforced. Java uses annotations + discipline.

## 18. Revision and Mastery

- Why `display=<anonymous>`?
- Difference null list vs empty list for caller loops.

## 19. Exercises

1. Predict `NullNpeDemo` lines.
2. Fix chained getter with intermediate locals.

## 20. Interview Questions

1. Can primitives be null?
2. Why NPE unchecked?
3. Yoda equals pattern?

## 21. Solutions

1. §7.
2. `var b = a.getB(); if (b == null) return; var c = b.getC();`

## 22. Chapter Summary

**Null** means no object; **NPE** strikes at dereference. Guard at boundaries, prefer empty over null collections, use safe equals. Demo shows defaulting, caught NPE, and display sentinel.

## 23. Key Terminology

null · NPE · guard clause · dereference

## 24. References

- [JLS 11.2 — Exception Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html)

## 25. Next Steps

- [02-optional-api.md](02-optional-api.md)

## Review Checklist

- [x] Demo verified
