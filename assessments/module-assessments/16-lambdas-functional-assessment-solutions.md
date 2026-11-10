# Solutions: Module 16 — Lambdas and Functional Programming

> **Assessment:** [16-lambdas-functional-assessment.md](16-lambdas-functional-assessment.md)

## Part A

1. Exactly **one abstract instance method** (SAM); defaults/static/`Object` methods excluded from count.
2. Compiler infers lambda type from **context** (assignment, argument, cast).
3. Captured locals must not change — JVM implements stable closure semantics.
4. `Predicate` → `boolean`; `Function` → transformed value type `R`.
5. Static, bound instance, unbound instance, constructor.
6. **`f` then `g`**: `g.apply(f.apply(x))`.
7. **Yes** — only abstract count must stay one.
8. Avoids **boxing** allocation on each call.
9. Lambda **shorter**; anonymous class **name in stack traces** / extra `.class` (trade-offs vary).
10. **No** — `this` is enclosing class instance.

## Part B

11. `15`  
12. `fig` (shortest length 3)  
13. `[10, 20, 30]`

## Part C

14. `2`  
15. `[ADA, BOB]`

## Part D

16. `14`  
17. `ADA` (after trim/upper, sorted natural)

## Part E

18. **Separation of concerns** — library sorts; caller supplies policy without copying sort algorithm.
19. Usually **yes** in callbacks unless the SAM is intentionally `Function<T, Optional<R>>`.
20. **Captures non-serializable / environment-specific state** — fragile across versions and nodes.

## Remediation

- B wrong → rerun [functional-fundamentals-demo](../../examples/core-java/functional-fundamentals-demo/)
- C wrong → Ch.02 predicates on null/blank
- D wrong → Ch.03 compose order
