# Assessment: Module 10 — Java Type System

> **Type:** Module  
> **Maps to:** Module 10 chapters 01–03  
> **Time guide:** 45–75 minutes  
> **Solutions:** [10-java-type-system-assessment-solutions.md](10-java-type-system-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.

## Part A — Conceptual

1. Define **static typing** and **strong typing** in Java in one sentence each.
2. What is the difference between a variable’s **declared type** and an object’s **runtime class**?
3. Does `var` make Java dynamically typed? Why or why not?
4. What is a **type test**? How does `null` behave with `instanceof`?
5. What problem do **type patterns** in `switch` solve compared to cast chains?
6. When can you omit `default` in a `switch` on a reference type?
7. Why is `List<Integer>` **not** a subtype of `List<Number>`?
8. State **PECS** in full and name producer vs consumer wildcards.
9. What can you typically do with `List<? extends Number>` — read, write, or both? Restrictions?
10. What can you typically do with `List<? super Integer>` — read, write, or both?
11. What is **type erasure** at a high level?
12. Why does `ints.getClass().getSimpleName()` print `ArrayList` not `List<Integer>`?
13. How do **covariant arrays** differ from generic lists (preview)?
14. What is a **raw type** and why avoid it?
15. `instanceof` vs `getClass() ==` — when prefer each?

## Part B — Predict (TypingDemo)

16. Line 1 output?
17. What does `runtime=` print?
18. If `Number n = Integer.valueOf(42)`, why does `n.intValue()` compile?
19. Could `String s = n` compile with a cast? What risk?
20. `len=` output for `"hello"`?

## Part C — Predict (InstanceofPatternsDemo)

21. `describe("Ada")`?
22. `describe(21)`?
23. `describe(null)`?
24. `describe(3.5)`?
25. Fifth line when `box = "JAVA"` and guarded `instanceof` runs?
26. What if `box` were `Integer.valueOf(5)` — is fifth line printed?
27. `describe(Boolean.TRUE)` with current `default`?
28. Must `case null` appear before `case String s`?

## Part D — Predict (VarianceErasurePreviewDemo)

29. `sum=` after adding 1 and 2?
30. `first=` through `nums` wildcard?
31. `objs=` after `addNumber(objs, 7)`?
32. `listClass=` line?
33. Does `sum` compile when passed `List<Double>` of `1.5` and `2.5`? Result type?
34. Can `addNumber(ints, 3)` compile when `ints` is `List<Integer>`? Why or why not?

## Part E — Debugging / Design

35. `void f(List<Number> xs)` — can you pass `new ArrayList<Integer>()`?
36. Sketch a method signature to **copy** numbers from any `List<? extends Number>` into `List<? super Number>`.
37. Why is `nums.add(1)` illegal when `nums` is `List<? extends Number>`?
38. Explain unchecked warning when casting `Object` to `List<String>`.

## Part F — Coding

39. Write `static Number firstNumber(List<? extends Number> list)` returning first element or throwing if empty.
40. Write `describeKind(Object o)` using switch: `null`, `String`, `Integer`, `default` with prefixes like the demo.
41. Add `void addAllNumbers(List<? super Integer> dest, List<? extends Integer> src)`.

## Part G — Advanced

42. What is **bridge method** relevance to erasure? (one sentence; detail in M13)
43. `Comparator<? super T>` in `List.sort` — PECS justification?
44. When would declaration-site variance (Kotlin `out`/`in`) help vs Java wildcards?

## Scoring guide

| Score | Suggestion |
|-------|------------|
| &lt; 60% | Re-read Ch.01–02; rerun demos |
| 60–84% | Ch.03 PECS + erasure; preview M13 index |
| 85%+ | Module 11 arrays/strings; sketch generic repository API |

## Honesty check

Did you predict outputs before running? Note any surprises in your own words.
