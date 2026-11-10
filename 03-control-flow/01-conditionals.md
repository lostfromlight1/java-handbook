# Conditionals

> **Status:** Completed  
> **Module:** 03 — Control Flow  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Write `if`, `else if`, and `else` branches that express clear decisions.
- Use boolean expressions as conditions (no C-style truthy ints).
- Nest carefully and prefer flat structures when readable.
- Avoid dangling-else confusion with braces.
- Apply conditionals to realistic grading/validation examples.

## 2. Prerequisites

- Module 02 (booleans, relational/logical operators, scope)

## 3. Why This Concept Exists

Programs must choose paths: valid vs invalid input, A vs B grade, feature on vs off. Conditionals are the first control-flow tool; loops and switches build on the same idea of “decide, then act.”

## 4. Conceptual Explanation

### 4.1 `if` / `else`

```java
if (score >= 60) {
    System.out.println("passing");
} else {
    System.out.println("failing");
}
```

The condition must be `boolean`. Braces `{}` are strongly recommended even for one-liners.

### 4.2 `else if` chains

Test mutually exclusive ranges from **highest threshold first**. Each branch is “first match wins”: as soon as one condition is true, later `else if` tests are skipped.

**Grade thresholds** used by [ConditionalsDemo](../examples/core-java/conditionals-demo/) (and this chapter):

| Letter | Condition (inclusive) |
|--------|------------------------|
| A | `score >= 90` |
| B | `score >= 80` |
| C | `score >= 70` |
| D | `score >= 60` |
| F | otherwise (`score < 60`) |

```java
static String grade(int score) {
    if (score >= 90) {
        return "A";
    } else if (score >= 80) {
        return "B";
    } else if (score >= 70) {
        return "C";
    } else if (score >= 60) {
        return "D";
    } else {
        return "F";
    }
}
```

For `score = 85`, the chain tests `>= 90` (false), then `>= 80` (true) and returns **B**—that matches the demo output below.

Order matters: if you check `>= 60` before `>= 90`, a score of 95 would match the D branch and never reach A.

### 4.3 Nested `if` vs flat structure

Nesting is legal, but each extra level forces the reader to track “which `if` am I inside?” Compare the same check for discount eligibility:

**Nested (harder to scan):**

```java
if (customer != null) {
    if (customer.isActive()) {
        if (orderTotal >= 100) {
            applyDiscount();
        }
    }
}
```

**Flat with guards (same logic, clearer exit paths):**

```java
if (customer == null) {
    return;
}
if (!customer.isActive()) {
    return;
}
if (orderTotal >= 100) {
    applyDiscount();
}
```

Flat structure is not always possible in `main`, but the idea is the same: handle invalid or uninteresting cases first, then keep the “happy path” at the left margin.

### 4.4 Guard clauses and `return`

A **guard clause** is an early exit when a precondition fails, so the rest of the method does not run.

A **method** is a named block of code; **`return`** immediately finishes that method and optionally sends a value back to the caller. In a `void` method, `return;` alone means “stop here.” In `String grade(int score)`, `return "B";` means “stop and the result of calling `grade` is the string `B`.”

```java
static String greet(String name) {
    if (name == null || name.isBlank()) {
        return "Hello, guest";
    }
    return "Hello, " + name;
}
```

Module 04 goes deeper into methods; here you only need guards as a readability tool when `if` chains get deep.

### 4.5 Ternary operator (`? :`)

```java
String status = score >= 60 ? "passing" : "failing";
```

The ternary is an expression: it picks one of two values. Both branches must be **type-compatible** (for example, both `String`, or both `int`). This does not compile if one branch is `String` and the other is `int`:

```java
// Object ok = condition ? "yes" : 1;  // error: incompatible types
Object ok = condition ? "yes" : Integer.valueOf(1);  // both branches are Object-compatible
```

**When not to use it:** multi-step logic, I/O, loops, or nested ternaries (`a ? b : c ? d : e`). Use a normal `if`/`else` or a small method instead. Reserve the ternary for assigning one of two simple values on one line.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Condition | Boolean expression controlling a branch |
| Branch | Code path taken for a decision outcome |
| Guard clause | Early exit when a precondition fails |

## 6. Syntax and Language Rules

```java
if (condition) { statements }
else if (condition) { statements }
else { statements }
```

`else` binds to the nearest `if`—always brace to avoid mistakes.

## 7. Step-by-Step Examples

[examples/core-java/conditionals-demo/](../examples/core-java/conditionals-demo/)

```bash
javac ConditionalsDemo.java
java ConditionalsDemo 85
```

**Verified:**

```text
score=85
grade=B
passing=true
```

## 8. Real-World Use Cases

Auth checks, input validation, feature flags, pricing tiers, HTTP status mapping (later modules).

## 9. Internal Behavior

**Plain language:** the JVM runs your `if`/`else` by evaluating the condition once. If it is true, it runs the first block; if false, it skips that block and runs the `else` branch (if any). Only one path executes per decision.

**Under the hood (Track B):** the compiler often emits **conditional jump** bytecodes—instructions that skip forward when a comparison fails (names like `ifeq`, `if_icmpge` appear in disassembly). You do not need to memorize opcode names to write correct conditionals; they simply show that branching is implemented as “test, then jump to the right instruction,” not as magic.

## 10. Edge Cases and Limitations

- Floating comparisons: prefer tolerances for measured doubles.
- `score >= 60` vs `score > 59` for ints—be explicit about boundaries.
- Null checks belong with references (Module 18).

## 11. Common Mistakes

### Incorrect

```java
if (score >= 60); { System.out.println("pass"); } // stray semicolon
```

### Corrected

Remove the semicolon after `if (...)`.

### Incorrect

Using `=` instead of `==` for `boolean` flags can assign and compile.

### Corrected

Compare with `==`, or use the boolean directly: `if (ready)`.

## 12. Best Practices

- Always use braces.
- Order `else if` ranges carefully.
- Name booleans positively (`isReady`) when possible.

## 13. Design Trade-offs

| Style | Benefit | Cost |
|-------|---------|------|
| Long else-if | Explicit | Can get noisy |
| Switch / maps | Cleaner for discrete cases | Next chapter / later |
| Polymorphism | Open for extension | Needs OOP (Module 05+) |

## 14. Performance Considerations

Branch prediction matters in hot loops later—not in learning demos.

## 15. Security Considerations

Authorization must use server-side conditions, never trust client-only flags (Module 39/43).

## 16. Java Version and Compatibility Notes

`if`/`else` are foundational. Validated on **21.0.9**.

## 17. Related Language Comparisons

Unlike JavaScript, conditions must be boolean—no implicit truthiness of numbers/strings.

## 18. Revision and Mastery

**De Morgan’s laws** rewrite negated conditions (handy when refactoring):

- `!(a && b)` is equivalent to `!a || !b`
- `!(a || b)` is equivalent to `!a && !b`

Example: `if (!(isAdmin && isActive))` ↔ `if (!isAdmin || !isActive)`.

Also review: exhaustive handling vs `else` catch-alls; prefer polymorphism over type-code `if`/`else` chains when behavior varies by type across many methods.

## 19. Exercises

### Beginner

1. Run ConditionalsDemo with `55`, `60`, `90`.
2. Write `max(a,b)` using `if`.

### Intermediate

3. Classify triangle sides (impossible / equilateral / other)—validate inputs.
4. Replace a nested if with guard returns in a small method.

### Advanced

5. Refactor grade ranges to a data-driven approach (array of thresholds)—discuss trade-offs.
6. Find a dangling-else example online and brace-fix it.

## 20. Interview Questions

1. **Why braces?** — Avoid dangling-else and accidental empty bodies.  
2. **Does Java allow `if (1)`?** — No; need boolean.  
3. **Order of else-if?** — First match wins; order critical for ranges.  

## 21. Solutions

1. F / D / A respectively for those scores with the demo’s thresholds.  
2. `if (a >= b) return a; else return b;`  

## 22. Chapter Summary

Conditionals choose paths using boolean expressions. Prefer braced `if`/`else if`/`else`, ordered carefully, kept flat when possible.

## 23. Key Terminology

Condition · branch · else-if · ternary · guard

## 24. References and Further Reading

- JLS statements  
- Next: [02-switch-and-switch-expressions.md](02-switch-and-switch-expressions.md)

## 25. Next Steps

[02-switch-and-switch-expressions.md](02-switch-and-switch-expressions.md)

## Review Checklist

- [x] ConditionalsDemo verified on JDK 21.0.9
- [x] Depth gate passed
