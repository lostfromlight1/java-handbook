# Assessment: Module 16 — Lambdas and Functional Programming

> **Type:** Module  
> **Maps to:** Module 16 chapters 01–03  
> **Time guide:** 35–50 minutes  
> **Solutions:** [16-lambdas-functional-assessment-solutions.md](16-lambdas-functional-assessment-solutions.md)

## Purpose

Learning diagnostic before Module 17 streams.

## Instructions

- Attempt without solutions.
- Predict demo output on paper, then verify with Temurin JDK 21 if needed.
- Revisit Module 08 Ch.03 for SAM rules if Part A is weak.

## Part A — Conceptual

1. What makes an interface **functional**?
2. What is **target typing** for a lambda?
3. **Effectively final** — why required for captured locals?
4. `Predicate` vs `Function` — return types?
5. Four kinds of **method reference**?
6. `f.andThen(g)` — which runs first on input `x`?
7. Can a functional interface have many `default` methods?
8. Why prefer `IntUnaryOperator` over `UnaryOperator<Integer>` in hot loops?
9. Lambda vs anonymous class for `Runnable` — one trade-off?
10. Does a lambda introduce a new `this`?

## Part B — Predict (FunctionalFundamentalsDemo)

11. `apply=`?
12. First element of `sorted=`?
13. `transformed=` line?

## Part C — Predict (LambdasInterfacesDemo)

14. `nonBlankCount=`?
15. `mapped=`?

## Part D — Predict (MethodReferencesDemo)

16. `pipeline=`?
17. First element of `normalized=`?

## Part E — Design

18. API accepts `Comparator<Row>` — why not only `List<Row>` sorted in caller?
19. Returning `Optional` from a lambda callback — smell?
20. Serialized long-lived lambda capturing `Socket` — concern?

## Scoring guide

| Score | Focus |
|-------|--------|
| 85–100% | Proceed to Module 17 |
| 60–84% | Re-read Ch.02 `java.util.function` |
| Below 60% | Ch.01 + Module 08 Ch.03 |
