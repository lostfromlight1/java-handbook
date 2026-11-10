# Assessment: Module 02 — Java Fundamentals

> **Type:** Module  
> **Maps to:** Module 02 chapters 01–04  
> **Time guide:** 45–75 minutes  
> **Solutions:** [02-java-fundamentals-assessment-solutions.md](02-java-fundamentals-assessment-solutions.md)

## Purpose

Check primitives, operators, scope/naming, conversions, and `var`—as a learning diagnostic, not a hiring score.

## Part A — Conceptual

1. List the eight primitive types and one default beginner choice for whole numbers and decimals.
2. Is `String` a primitive? Why does that matter?
3. What does `final` mean on a local variable?
4. Why is `10 / 3` equal to `3` in Java ints?
5. What is short-circuit evaluation? Give an example with `&&`.
6. Define scope using a block example.
7. Widening vs narrowing: which needs a cast?
8. Is `var` dynamically typed? Explain.

## Part B — Predict the output

9. `System.out.println(2 + 3 * 4);`
10. `System.out.println("n=" + 2 + 3);` vs `System.out.println("n=" + (2 + 3));`
11. After `int x = 5;`, what does printing `x++` show, and what is `x` afterward?
12. `(int) 3.99`?

## Part C — Debugging

13. `long big = 3000000000;` fails to compile. Fix it.
14. `double avg = sum / count;` with int `sum` and `count` is wrong for averages. Fix it.
15. Code uses loop variable `i` after the `for` loop. What happens and how do you redesign?

## Part D — Coding

16. Write `TempConvert` that takes a Celsius `double` (hardcode or arg) and prints Fahrenheit using `f = c * 9 / 5 + 32` carefully with floating math.
17. Write a tiny program demonstrating a block-scoped variable that is illegal to use outside the block (comment the illegal line).

## Part E — Advanced (optional)

18. Why is `float` a poor default for money?
19. When would you refuse `var` even though it compiles?

## Scoring guide

| Band | Next step |
|------|-----------|
| Strong | Module 03 |
| Partial | Re-read mapped chapters |
| Weak | Redo all `examples/core-java` demos |

## Chapter mapping

| Q | Ch |
|---|----|
| 1–3 | 01 |
| 4–5, 9–11 | 02 |
| 6, 15, 17 | 03 |
| 7–8, 12–14, 16, 18–19 | 04 |
