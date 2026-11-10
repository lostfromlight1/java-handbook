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

An **expression** evaluates to a **single value** (or to `void` for a few special cases). You can nest expressions inside larger expressions:

```java
int a = 10, b = 3;
a + b              // expression; value 13
a > b              // expression; value true
(a > 5) && (b < 5) // expression; value true
```

A **statement** performs an action and ends with `;` (or is a control-flow statement with a body). It may contain expressions but is not “just a value” by itself:

```java
int x = a + b;           // declaration statement; initializer contains expression
System.out.println(a);   // expression statement; println(...) is void
if (a > b) { x = 1; }    // if is a statement; condition is expression
```

`x = 5` is both an expression (it evaluates to `5`) and commonly used as a statement. Prefer using assignment as its own statement while learning—dense `a = b = c` chains hide bugs.

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

For **primitives**, `==` compares values. For **references**, `==` asks whether two references point at the **same object** (identity), not whether two strings contain the same characters.

```java
String x = "hi";
String y = "hi";
String z = new String("hi");

x == y;          // often true (literal pooling); not a general rule for all strings
x.equals(y);     // true — same character sequence
x == z;          // false — different objects
x.equals(z);     // true — value comparison
```

**Rule of thumb:** never use `==` to compare `String` content; use `equals` (and `equalsIgnoreCase` when appropriate). Module 06 covers `equals`, hashing, and contracts in depth.

### 4.4 Logical operators

| Op | Meaning |
|----|---------|
| `&&` | Logical and (short-circuit) |
| `\|\|` | Logical or (short-circuit) |
| `!` | Not |

**Short-circuit:** `false && anything` does not evaluate `anything`. Important when the right side would crash or is expensive.

Walk-through with a side effect on the right-hand side:

```java
int calls = 0;
boolean skipped = false && ((calls++) > 0); // short-circuit: calls stays 0
boolean ran     = true  && ((calls++) > 0); // evaluates RHS; calls becomes 1
```

For `||`, if the left operand is `true`, the right operand is skipped.

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

Operators bind in a fixed order (full table in JLS). High-level picture:

| Precedence (high → low) | Operators | Notes |
|-------------------------|-----------|--------|
| Postfix | `expr++` `expr--` | Value before increment |
| Unary | `++expr` `--expr` `!` unary `+` `-` | |
| Multiplicative | `*` `/` `%` | Before add/subtract |
| Additive | `+` `-` | String `+` is still `+` |
| Relational | `<` `>` `<=` `>=` | |
| Equality | `==` `!=` | After ordering |
| Logical AND | `&&` | Short-circuit |
| Logical OR | `\|\|` | Short-circuit |
| Assignment | `=` `+=` … | Lowest common pitfall |

`*` binds tighter than `+`:

```java
2 + 3 * 4   // 14, not 20
(2 + 3) * 4 // 20
boolean mid = a > 5 && b < 5; // && looser than > ; clearer with parens when mixing
```

When unsure, parenthesize. Clarity beats memorizing every row of the JLS table.

### 4.8 `&` vs `&&` on booleans

Both combine boolean values, but `&` is **not** short-circuit: it always evaluates both sides. `&&` stops when the result is fixed.

```java
int sideEffect = 0;
boolean b = false && ((sideEffect++) > 0); // RHS skipped
boolean c = false &  ((sideEffect++) > 0); // RHS runs; sideEffect becomes 1
```

Use `&&` and `||` for conditions. Reserve `&` for integer **bitwise** AND unless you truly need both sides evaluated.

### 4.9 String `+`

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

### 7.1 Arithmetic and precedence

```java
int a = 10, b = 3;
a + b;   // 13
a / b;   // 3 — integer division truncates toward zero
a % b;   // 1 — remainder of division
2 + 3 * 4;    // 14
(2 + 3) * 4;  // 20
```

Full tour: [examples/core-java/operators-demo/](../examples/core-java/operators-demo/)

**Verified output (arithmetic + precedence + increment section):**

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

### 7.2 Logic, comparison, and short-circuit

Relational results are `boolean`; combine them with `&&` / `||`:

```java
a > b;                 // true
(a > 5) && (b < 5);    // true
b != 0 && (a / b > 1); // safe: divide only if divisor non-zero
```

If `b` were `0`, `b != 0 && …` avoids evaluating `a / b` (short-circuit).

### 7.3 Strings, concatenation, and increment pitfalls

String `+` converts non-`String` operands to text:

```java
System.out.println("sum=" + a + b);      // "sum=103" — left-to-right concatenation
System.out.println("sum=" + (a + b));    // "sum=13" — add ints first
```

Postfix vs prefix increment (see verified lines in output above):

```java
int x = 5;
int old = x++; // old is 5, then x becomes 6
int newer = ++x; // x becomes 7, newer is 7
```

## 8. Real-World Use Cases

Bounds checks, feature flag combinations, pagination math (`offset % pageSize`), counters.

## 9. Internal Behavior

Integer ops wrap on overflow. Floating ops follow IEEE rules including `NaN`. Bitwise operators (`& | ^ << >> >>>`) exist—useful later; don’t confuse `&` with `&&`.

## 10. Edge Cases and Limitations

- Division by zero: ints throw `ArithmeticException`; doubles yield Infinity/NaN.
- `&&` vs `&` for booleans: `&` always evaluates both sides (§4.8).
- `==` on `String` compares references, not text—use `equals` (§4.3).
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
