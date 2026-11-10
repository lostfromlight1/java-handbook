# Type Conversion and Local Variable Type Inference

> **Status:** Completed  
> **Module:** 02 — Java Fundamentals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain widening vs narrowing conversions.
- Use casts deliberately and safely.
- Predict integer vs floating division results.
- Use `var` for local variable type inference without hiding meaning.
- Know when explicit types are clearer than `var`.

## 2. Prerequisites

- Chapters [01](01-variables-and-types.md)–[03](03-scope-naming-and-comments.md)

## 3. Why This Concept Exists

Java won’t silently destroy information without a cast—but casts can still truncate. `var` reduces boilerplate yet can obscure APIs. Master both so Module 03+ control flow and Module 13 generics don’t surprise you.

## 4. Conceptual Explanation

### 4.1 Widening (safe promotion)

Smaller numeric types convert to larger ones without a cast:

```java
int whole = 42;
double widened = whole; // OK
```

Typical widenings: `byte/short/char → int → long → float → double` (see JLS for full rules).

### 4.2 Narrowing (explicit cast)

Going to a smaller type needs a cast and may lose data:

```java
double pi = 3.99;
int narrowed = (int) pi; // 3 — fractional part discarded toward zero
```

### 4.3 Binary numeric promotion

In `a / b`, operands are promoted before the operation. Two `int`s → `int` division. Mixing in a `double` yields floating division:

```java
5 / 2;   // 2
5 / 2.0; // 2.5
```

### 4.4 Casting references (preview)

`(String) obj` is different—runtime check, `ClassCastException` possible (Modules 05, 10). This chapter focuses on primitives.

### 4.5 Local variable type inference (`var`)

Since Java 10, locals can use `var` when the initializer makes the type clear:

```java
var message = "inferred String"; // String
var total = 10 + 2;              // int
```

Rules of thumb:

- **Initializer required** — cannot write `var x;`
- **Locals only** (not fields/method parameters in the classic form)
- Prefer `var` when the right-hand side is obvious (`var list = new ArrayList<String>();` later)
- Prefer explicit types when the RHS is opaque (`var x = factory.make();`)

`var` is still **statically typed**—inferred at compile time, not dynamic typing.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Widening | Conversion that preserves magnitude range (no cast) |
| Narrowing | Conversion that may lose information (cast) |
| Cast | `(type) expression` |
| Type inference | Compiler deduces type (`var`) |

## 6. Syntax and Language Rules

```java
(double) sum / count; // cast binds to sum first — often want ((double) sum) / count
```

Parenthesize casts in mixed expressions.

## 7. Step-by-Step Examples

[examples/core-java/conversion-demo/](../examples/core-java/conversion-demo/)

**Verified output:**

```text
widened=42.0
narrowed=3
5/2 (int)=2
5/2.0 (double)=2.5
message=inferred String
total=12
```

## 8. Real-World Use Cases

Averages (`(double) sum / n`), graphics coordinates, interop with APIs needing `long` IDs, cleaner locals with `var` in methods.

## 9. Internal Behavior

Narrowing floating→int drops the fraction toward zero (JLS). Overflow when narrowing large `long` to `int` keeps low bits—another footgun.

## 10. Edge Cases and Limitations

- Casting `NaN`/Infinity to int has defined but surprising results—read JLS if needed.
- `var` with diamond/`new` is fine; `var` with `null` alone is illegal without a target type.
- `var` is not a keyword in all positions historically—but treat it as reserved for locals in practice.

## 11. Common Mistakes

### Incorrect

```java
double avg = sum / count; // both int → truncates before widening assign
```

### Corrected

```java
double avg = (double) sum / count;
```

### Incorrect

Using `var` for every local including unclear factory returns.

### Corrected

Explicit type when it documents the API result.

## 12. Best Practices

- Cast narrowly and close to the need.
- Name `var` variables especially well—the type isn’t visible on the left.
- Never use casts to “silence” type errors you don’t understand.

## 13. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| Always explicit types | Readable left side | Verbose |
| Liberal `var` | Less duplication | Hidden types |
| Balanced | Best of both | Judgment required |

## 14. Performance Considerations

Casts are cheap; wrong numeric type choices matter more than cast syntax.

## 15. Security Considerations

Truncating casts on sizes/lengths can cause allocation or bounds bugs—validate before casting user-controlled numbers.

## 16. Java Version and Compatibility Notes

- Widening/narrowing: foundational  
- `var`: Java **10+**; fine on primary **21**  
- Validated on **21.0.9**

## 17. Related Language Comparisons

C# `var` is similar in spirit. JavaScript `var` is unrelated historically (dynamic binding)—don’t conflate.

## 18. Revision and Mastery

- Compound assignments and hidden casts  
- Bridge methods / generics erasure (Module 13)  
- Pattern matching reducing casts (Modules 03, 10)

## 19. Exercises

### Beginner

1. Run ConversionDemo.
2. Cast `3.14` to `int` and predict the result.

### Intermediate

3. Compute average of `{1,2,3,4}` as `double` without integer truncation.
4. Replace three obvious locals with `var`; leave one explicit on purpose—justify.

### Advanced

5. Show a narrowing cast from a large `long` to `int` and explain the result.
6. Find a case in docs where `var` is illegal and quote why.

## 20. Interview Questions

1. **Widening vs narrowing?** — Safe promote vs possible loss with cast.  
2. **Is var dynamic typing?** — No; compile-time inference.  
3. **Why is 5/2 == 2?** — Both operands int.  

## 21. Solutions

2. `3`.  
3. `(double) sum / length` or `sum * 1.0 / length`.  
5. Example: `(int) 3_000_000_000L` wraps—demonstrate and explain.  

## 22. Chapter Summary

Widen freely, narrow carefully with casts, watch integer division, and use `var` when it clarifies rather than conceals.

## 23. Key Terminology

Widening · narrowing · cast · promotion · `var` · type inference

## 24. References and Further Reading

- JLS conversions chapter  
- JEP 286 (local-variable type inference)  
- Module assessment next; Module 03 control flow

## 25. Next Steps

[Module 02 assessment](../assessments/module-assessments/02-java-fundamentals-assessment.md) → [Module 03 control flow](../03-control-flow/README.md)

## Review Checklist

- [x] ConversionDemo verified
- [x] `var` version notes accurate (Java 10+)
- [x] Depth gate passed
