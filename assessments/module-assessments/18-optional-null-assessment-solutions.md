# Solutions: Module 18 — Optional and Null Handling

> **Assessment:** [18-optional-null-assessment.md](18-optional-null-assessment.md)

## Part A

1. **No object** referenced; absent / uninitialized reference.
2. **Unchecked** (`RuntimeException`).
3. **Immediate NPE** from `of`.
4. **`orElseGet`** — supplier runs only when empty.
5. **No** — skipped.
6. **Returns** (JDK guidance); avoid fields/parameters.
7. `flatMap` when mapper returns **Optional**; avoids nesting.
8. **`"constant".equals(variable)`**.
9. Prefer **empty list** — foreach without null guard.
10. **JEP 358** Helpful NullPointerExceptions.

## Part B

11. `unknown`  
12. `NullPointerException`  
13. `<anonymous>`

## Part C

14. `ADA`  
15. `fallback`  
16. `2`

## Part D

17. `unknown`  
18. `true`

## Part E

19. **Yes** — flatten to `List<User>` or filter present; Optionals inside collections confuse callers.
20. Return **`Optional.empty()`** for documented optional-result API; **never null Optional**.

## Remediation

- Optional chaining → optional-api-demo `findLength`
- Design → Ch.03 anti-pattern table
