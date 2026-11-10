# Assessment: Module 14 — Exceptions and Error Handling

> **Type:** Module  
> **Maps to:** Module 14 chapters 01–03  
> **Time guide:** 40–60 minutes  
> **Solutions:** [14-exceptions-assessment-solutions.md](14-exceptions-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.
- Revisit Ch.01–03 for any wrong hierarchy or TWR ordering items.

## Part A — Conceptual

1. What is the supertype of both `Error` and `Exception`?
2. Name the branch of `Exception` that makes a type **unchecked**.
3. Must a method declare `throws IllegalArgumentException`? Why or why not?
4. When is catching `OutOfMemoryError` usually inappropriate?
5. What compile-time rule applies to **checked** exceptions at call sites?
6. Difference between **`throw`** and **`throws`**?
7. Can `catch (Exception ex)` catch `IllegalStateException`? Why?
8. Should validation failures in a setter typically be checked or unchecked?
9. What does **`getCause()`** return on a wrapped exception?
10. Why preserve **cause** when translating `SQLException` to a domain type?
11. What interface does try-with-resources require on declared resources?
12. What happens to a **secondary** exception thrown from `close()` when the try body already threw?

## Part B — Predict (ThrowableDemo)

13. Line 1 (`caughtUnchecked=`)?
14. Line 2 (`caughtChecked=`)?
15. `runtimeIsUnchecked=`?
16. `errorIsThrowable=`?

## Part C — Predict (TryWithResourcesDemo)

17. After successful first `try`, first two lines?
18. Lines 3–4 on second `try` (failure path)?
19. Three lines from the TWR block (`open`, data, `close`)?

## Part D — Predict (CustomExceptionsDemo)

20. `balance=` after first withdraw?
21. `error=` message?
22. `shortfall=`?

## Part E — Debugging / Design

23. `catch (IOException ex)` placed after `catch (Exception ex)` — compile result?
24. Empty `catch (IOException ex) { }` at a service boundary — main risk?
25. `withdraw` checks balance **after** deducting — exception-safety problem?
26. When is a custom **checked** exception justified vs `IllegalArgumentException`?
27. Why avoid using exceptions for “user not found” on every lookup in a hot API?
28. Does `finally` run if `catch` handles the exception?
29. Resource declaration order `try (A a = ...; B b = ...)` — close order?
30. Name one JDK unchecked type for “object used incorrectly after close or wrong lifecycle.”

## Scoring guide

| Score | Focus |
|-------|--------|
| 85–100% | Ready for Module 15; skim TWR suppression if fuzzy |
| 60–84% | Re-read Ch.02 `finally`/TWR; redo Part C predictions |
| Below 60% | Ch.01 hierarchy with `ThrowableDemo`, then Ch.02–03 |

Review wrong items in the mapped chapters before Module 15.
