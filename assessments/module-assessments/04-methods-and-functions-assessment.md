# Assessment: Module 04 — Methods and Functions

> **Type:** Module  
> **Maps to:** Module 04 chapters 01–04  
> **Time guide:** 60–90 minutes  
> **Solutions:** [04-methods-and-functions-assessment-solutions.md](04-methods-and-functions-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Prefer writing small programs and predicting output before running.

## Part A — Conceptual

1. What problem do methods solve that a single long `main` does not?
2. Distinguish **parameter** from **argument**.
3. Why can two methods share a name if their parameter lists differ, but not if only the return type differs?
4. What does `String... parts` mean, and where must a varargs parameter appear in the list?
5. Is Java pass-by-reference? Explain what is copied for `int` vs for an `int[]`.
6. When is mutating an object through a method parameter visible to the caller? When is reassigning the parameter not visible?
7. What is a **stack frame**, and how does deep recursion relate to `StackOverflowError`?
8. Contrast a **static** method with an **instance** method (who owns it, is there `this`?).
9. What is a **method reference**, and how does it relate to a lambda?

## Part B — Predict the output

10. After `int x = 5; bump(x);` where `static void bump(int n) { n = n + 1; }`, what is `x`?
11. After `int[] a = {1}; touch(a);` where `static void touch(int[] arr) { arr[0] = 7; }`, what is `a[0]`?
12. After `int[] a = {1}; replace(a);` where `static void replace(int[] arr) { arr = new int[]{9}; }`, what is `a[0]`?
13. `max(2, 8)` with overloads `max(int,int)` and `max(double,double)` — which overload runs?
14. `join()` with `static String join(String... parts)` returning `""` when length is 0 — what prints for empty call?
15. `factorial(4)` recursive with base `n <= 1 → 1` — result?

## Part C — Debugging

16. Non-`void` method has a path with no `return`. What happens?
17. `static int parse(String s)` and `static double parse(String s)` in the same class — why won’t it compile?
18. Calling an instance method as `Counter.describe()` without an instance — what goes wrong?
19. Unbounded `static int bomb() { return bomb(); }` — what runtime failure is likely?

## Part D — Practical coding

20. Write `static boolean isEven(int n)` and call it from `main` for `n = 4` and `n = 7`.
21. Write overloaded `area(int side)` (square) and `area(int w, int h)` (rectangle); print both.
22. Write `static int sum(int... values)` that returns 0 for no args; demonstrate `sum()`, `sum(1,2,3)`.
23. Write iterative `static long power(int base, int exp)` for non-negative `exp` (no recursion required).

## Part E — Advanced

24. Explain why `String s = "hi"; append(s);` with `static void append(String t) { t = t + "!"; }` leaves `s` as `"hi"`.
25. Given a `Counter` with static `created` and instance `ticks`, explain what `createdCount()` vs `describe()` each report after two bumps on one instance and one bump on another.
26. Rewrite `names.stream().map(s -> s.toUpperCase())` using a method reference.

## Scoring guide

| Band | Next |
|------|------|
| Strong | Module 05 OOP |
| Partial | Re-read weak chapters; rerun Module 04 demos |
| Weak | Redo all four demos; rework Part B predictions on paper first |

## Chapter mapping

| Q | Ch |
|---|-----|
| 1–2, 16, 20 | 01 |
| 3–4, 13–15, 17, 21–23 | 02 |
| 5–7, 10–12, 19, 24 | 03 |
| 8–9, 18, 25–26 | 04 |
