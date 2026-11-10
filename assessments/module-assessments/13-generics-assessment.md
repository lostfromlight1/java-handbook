# Assessment: Module 13 — Generics

> **Type:** Module  
> **Maps to:** Module 13 chapters 01–04  
> **Time guide:** 45–70 minutes  
> **Solutions:** [13-generics-assessment-solutions.md](13-generics-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.
- Revisit [Module 10 Ch.03](../../10-java-type-system/03-variance-wildcards-and-erasure-preview.md) for any wrong PECS/erasure items.

## Part A — Conceptual

1. Difference between **type parameter** and **type argument**?
2. Why is `List<Integer>` not assignable to `List<Number>`?
3. State **PECS** in one sentence and name producer vs consumer wildcards.
4. Can you `add(3)` to `List<? extends Number>`? Why or why not?
5. What does erasure replace unbounded `T` with at bytecode level?
6. Why might `List<String>` and `List<Integer>` share the same `getClass()`?
7. What is a **bridge method** for?
8. Name two illegal uses of a type parameter `T`.
9. What does `@SafeVarargs` communicate about a generic varargs method?
10. Why return `Optional<T>` from `find` instead of nullable `T`?
11. When should a method use `<T extends Comparable<T>>` vs `List<? extends Comparable<?>>`?
12. What is **wildcard capture**?

## Part B — Predict (GenericsBasicsDemo)

13. `value=`?
14. `first=`?
15. `pair=`?

## Part C — Predict (GenericsBoundsDemo)

16. `sum=`?
17. `nums=` after copy?
18. `producer0=`?

## Part D — Predict (GenericsErasureDemo)

19. `sameRawClass=`?
20. `s=`?
21. `i=`?
22. `built=`?

## Part E — Predict (GenericsApiDemo)

23. `found=`?
24. `missing=` (id `"9"`)?
25. `count=`?

## Part F — Debugging / Design

26. `List raw = new ArrayList(); raw.add(1);` assigned to `List<String> s = raw;` — what risk appears at `s.get(0)`?
27. Why is `if (x instanceof List<String>)` a compile error?
28. Sketch method signature: copy from `List<Integer>` to `List<Object>` using PECS.
29. Should a public getter return internal `ArrayList<T>`? Preferred alternative?
30. `Comparator<Object>` passed to `List<String>.sort` — which wildcard bound makes that legal?

## Scoring guide

| Score | Focus |
|-------|--------|
| 85–100% | Ready for Module 14; skim erasure if bridge methods fuzzy |
| 60–84% | Re-read Ch.02 PECS + Ch.03 erasure; redo Part C/D predictions |
| Below 60% | Ch.01–02 with demos; M10 Ch.03 preview, then return |

Review wrong items in the mapped chapters before Module 14.
