# Assessment: Module 18 — Optional and Null Handling

> **Type:** Module  
> **Maps to:** Module 18 chapters 01–03  
> **Time guide:** 30–45 minutes  
> **Solutions:** [18-optional-null-assessment-solutions.md](18-optional-null-assessment-solutions.md)

## Part A — Conceptual

1. What does `null` mean for a reference type?
2. Checked or unchecked — `NullPointerException`?
3. `Optional.of(null)` result?
4. `orElse` vs `orElseGet` when default is expensive?
5. `map` on empty Optional — is mapper invoked?
6. Recommended use of Optional — fields, parameters, or returns?
7. `flatMap` vs `map` on Optional?
8. Null-safe equals idiom with constant first?
9. Empty list vs null list for API “no results”?
10. Helpful NPE messages — which JEP (Java 14+)?

## Part B — Predict (NullNpeDemo)

11. `safe=`?
12. `npe=` line class name?
13. `display=` for null input?

## Part C — Predict (OptionalApiDemo)

14. `upper=`?
15. `fromEmpty=`?
16. `len=` for `"  hi  "`?

## Part D — Predict (OptionalDesignDemo)

17. `missing=`?
18. `hasUser1=`?

## Part E — Design

19. `List<Optional<User>>` from DAO — smell?
20. Return `null` vs `Optional.empty()` from `findById`?

## Scoring guide

| Score | Focus |
|-------|--------|
| 85–100% | Module 19 (modern Java features) |
| 60–84% | Ch.02 API fluency |
| Below 60% | Ch.01 null guards + Ch.03 misuse |
