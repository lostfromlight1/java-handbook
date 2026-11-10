# Assessment: Module 15 — Enums, Records, and Sealed Types

> **Type:** Module  
> **Maps to:** Module 15 chapters 01–03  
> **Time guide:** 40–60 minutes  
> **Solutions:** [15-enums-records-sealed-assessment-solutions.md](15-enums-records-sealed-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.
- Revisit M07 Ch.04 if sealed exhaustiveness items are weak.

## Part A — Conceptual

1. Can an enum type be subclassed?
2. What does `Level.valueOf("high")` do (case as declared in `EnumsDemo`)?
3. Why is `==` idiomatic for comparing enum constants?
4. Name two methods the compiler generates for a record besides the canonical constructor.
5. What is a **compact constructor** for?
6. Can a record declare extra instance fields not listed in the header?
7. What does `sealed` enforce on direct subtypes?
8. When is a `default` branch unnecessary in a `switch` on a sealed type?
9. How does Module 15's `Expr` ADT relate to Module 07's `Result` type?
10. Enum vs sealed + records — when is enum enough?
11. What accessor name does `record Point(int x, int y)` use for `x`?
12. What risk does using `ordinal()` for business ordering create?

## Part B — Predict (EnumsDemo)

13. Line 1 (`name=`)?
14. Line 2 (`ordinal=`)?
15. `parse=`?
16. `isHigh=`?

## Part C — Predict (RecordsDemo)

17. First line (`Point[...]`)?
18. `equals=` and `hashEqual=`?
19. `contains3=` and `contains9=`?

## Part D — Predict (SealedPatternsDemo)

20. `eval=` for `Add(Const(2), Const(3))`?
21. `desc=` for `Const(7)`?
22. `descAdd=` for the same `Add` as in the demo?

## Part E — Design / Debugging

23. `record R(List<String> xs) {}` without copying — immutability problem?
24. Open `interface Expr` with exhaustive switch claim — valid?
25. `case Object o` before `case String s` — compile result?
26. Adding `Mul` to `sealed interface Expr permits ...` — what must update?
27. Record as JPA entity — typical recommendation?
28. `EnumSet` vs `HashSet<MyEnum>` — one advantage of `EnumSet`?
29. `non-sealed` permitted subtype — effect on inheritance?
30. Link pattern: which module introduced **type patterns in switch** first at control-flow depth?

## Scoring guide

| Score | Focus |
|-------|--------|
| 85–100% | Ready for Module 16; skim record patterns if fuzzy |
| 60–84% | Re-read Ch.03 exhaustiveness; redo Part D |
| Below 60% | Ch.01 enums + Ch.02 records, then M07 sealed refresher |

Review wrong items in the mapped chapters before Module 16.
