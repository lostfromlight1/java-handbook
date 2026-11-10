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

Test mutually exclusive ranges from most specific/highest first:

```java
if (score >= 90) { ... }
else if (score >= 80) { ... }
else { ... }
```

Order matters: if you check `>= 60` before `>= 90`, the A branch never runs.

### 4.3 Nested `if`

Allowed, but deep nesting hurts readability. Prefer early `return` / guard clauses (Module 04 methods) when logic grows.

### 4.4 Ternary operator (related)

```java
String status = score >= 60 ? "passing" : "failing";
```

Use for simple value selection—not for multi-statement logic.

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

Bytecode uses conditional jumps (`ifeq`, `if_icmpge`, …). Mentally: evaluate boolean, jump to the matching block.

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

- De Morgan’s laws for boolean simplification  
- Exhaustive handling vs `else` catch-alls  
- Prefer polymorphism over type-code if/else chains  

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
