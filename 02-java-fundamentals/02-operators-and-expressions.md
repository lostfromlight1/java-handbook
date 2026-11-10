# Operators and Expressions

> **Status:** Completed  
> **Module:** 02 — Java Fundamentals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Build expressions from operators and operands.
- Use arithmetic, relational, logical, assignment, and increment operators correctly.
- Explain integer division and remainder (`%`).
- Apply operator precedence and use parentheses deliberately.
- Contrast postfix vs prefix `++` / `--`.

## 2. Prerequisites

- [01-variables-and-types.md](01-variables-and-types.md)

## 3. Why This Concept Exists

Control flow and algorithms are built from expressions that compute values and truth. Silent mistakes—integer division, precedence, `=` vs `==`—cause logic bugs that compile cleanly.

## 4. Conceptual Explanation

### 4.1 Expressions and statements

An **expression** produces a value (`a + b`). A **statement** performs an action (`System.out.println(a + b);`). Many statements contain expressions.

### 4.2 Arithmetic operators

| Op | Meaning | Notes |
|----|---------|-------|
| `+ - * /` | Add subtract multiply divide | `/` on ints truncates toward zero |
| `%` | Remainder | `10 % 3 == 1` |

```java
int a = 10, b = 3;
a / b; // 3, not 3.333...
```

### 4.3 Relational and equality

| Op | Meaning |
|----|---------|
| `== !=` | Equal / not equal |
| `< > <= >=` | Ordering |

For primitives, `==` compares values. For objects/references, `==` compares identity—use `equals` later (Module 06).

### 4.4 Logical operators

| Op | Meaning |
|----|---------|
| `&&` | Logical and (short-circuit) |
| `\|\|` | Logical or (short-circuit) |
| `!` | Not |

**Short-circuit:** `false && anything` does not evaluate `anything`. Important when the right side would crash or is expensive.

### 4.5 Assignment and compounds

```java
x = 5;
x += 2; // x = x + 2
```

### 4.6 Increment / decrement

```java
x++; // postfix: yield old value, then add 1
++x; // prefix: add 1, then yield new value
```

### 4.7 Precedence

`*` binds tighter than `+`:

```java
2 + 3 * 4   // 14
(2 + 3) * 4 // 20
```

When unsure, parenthesize. Clarity beats memorizing the full table.

### 4.8 String `+`

If either side of `+` is a `String`, Java performs concatenation (with conversion of the other operand to text). That is why `"sum=" + (a + b)` needs parentheses for numeric add-first.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Operand | Value an operator works on |
| Precedence | Which operator binds first |
| Short-circuit | Skipping right-hand evaluation |
| Side effect | Change caused by evaluating (`++`) |

## 6. Syntax and Language Rules

Boolean operators require `boolean` operands—no C-style `if (1)`.  
Assignment is an expression (`a = b = 0`) but prefer simple statements while learning.

## 7. Step-by-Step Examples

### 7.1–7.2 Operators demo

[examples/core-java/operators-demo/](../examples/core-java/operators-demo/)

**Verified output:**

```text
a + b = 13
a / b = 3
a % b = 1
a > b = true
(a > 5) && (b < 5) = true
2 + 3 * 4 = 14
(2 + 3) * 4 = 20
x++ yields 5, then x=6
++x yields 6, then x=6
```

### 7.3 Classic bug

```java
System.out.println("sum=" + a + b); // concatenates as strings after "sum="
System.out.println("sum=" + (a + b)); // adds first
```

## 8. Real-World Use Cases

Bounds checks, feature flag combinations, pagination math (`offset % pageSize`), counters.

## 9. Internal Behavior

Integer ops wrap on overflow. Floating ops follow IEEE rules including `NaN`. Bitwise operators (`& | ^ << >> >>>`) exist—useful later; don’t confuse `&` with `&&`.

## 10. Edge Cases and Limitations

- Division by zero: ints throw `ArithmeticException`; doubles yield Infinity/NaN.
- `&&` vs `&` for booleans: `&` always evaluates both sides.
- Comparing doubles with `==` is often fragile—use tolerances for measured values.

## 11. Common Mistakes

### Incorrect

`if (a = 5)` when you meant `==` — often a compile error for `int` (good!), but dangerous with booleans.

### Corrected

Always use `==` for comparison.

### Incorrect

Expecting `5 / 2 == 2.5`.

### Corrected

Use `5 / 2.0` or cast.

## 12. Best Practices

- Parenthesize mixed precedence.
- Prefer `&&` / `||` over non-short-circuit forms unless you need both sides.
- Avoid clever `++` inside dense expressions.

## 13. Design Trade-offs

| Style | Benefit | Cost |
|-------|---------|------|
| Dense operators | Short | Hard to debug |
| Named temps | Clear | Slightly longer |

## 14. Performance Considerations

Short-circuit can skip work—correctness first, then rely on it intentionally.

## 15. Security Considerations

Don’t use floating comparisons for authorization boundaries; prefer exact ints/enums.

## 16. Java Version and Compatibility Notes

Core operators are stable across modern Java. Validated on **21.0.9**.

## 17. Related Language Comparisons

Java forbids many implicit truthy conversions that JavaScript allows—booleans are explicit.

## 18. Revision and Mastery

Bitwise ops, `>>>`, operator overloading absence, `Objects.equals` vs `==`.

## 19. Exercises

### Beginner

1. Predict `17 % 5`, then verify in JShell or a tiny class.
2. Run OperatorsDemo.

### Intermediate

3. Print both `"x=" + a + b` and `"x=" + (a + b)` for `a=2,b=3`.
4. Write an expression true only if `n` is between 1 and 10 inclusive.

### Advanced

5. Demonstrate short-circuit preventing a divide-by-zero on the right side.
6. Explain when `&` on booleans differs from `&&`.

## 20. Interview Questions

1. **Integer division of 5/2?** — 2.  
2. **What is short-circuit?** — Skip right side when result known.  
3. **`==` on objects?** — Identity, not necessarily equality.  

## 21. Solutions

1. `2`.  
3. `x=23` vs `x=5`.  
4. `n >= 1 && n <= 10`.  
5. `b != 0 && (a / b > 1)`.  

## 22. Chapter Summary

Operators build expressions. Watch integer division, precedence, short-circuit logic, and `++` placement.

## 23. Key Terminology

Expression · precedence · short-circuit · remainder · postfix · prefix

## 24. References and Further Reading

- JLS operator precedence table
- Next: [03-scope-naming-and-comments.md](03-scope-naming-and-comments.md)

## 25. Next Steps

[03-scope-naming-and-comments.md](03-scope-naming-and-comments.md)

## Review Checklist

- [x] OperatorsDemo verified
- [x] Depth gate passed
