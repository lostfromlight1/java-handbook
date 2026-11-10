# Assessment: Module 03 — Control Flow

> **Type:** Module  
> **Maps to:** Module 03 chapters 01–04  
> **Time guide:** 60–90 minutes  
> **Solutions:** [03-control-flow-assessment-solutions.md](03-control-flow-assessment-solutions.md)

## Part A — Conceptual

1. Why must `if` conditions be boolean in Java?
2. What is fall-through in a classic switch? How do arrow cases differ?
3. When do you prefer `while` over a counted `for`?
4. What do `break` and `continue` do?
5. What problem does `instanceof String s` solve vs classic instanceof+cast?
6. Why pair sealed types with pattern switches?

## Part B — Predict

7. Grade chain: score 80 with thresholds A≥90, B≥80 → ?
8. Switch expression `case "SAT","SUN" -> "weekend"` for `"SAT"` → ?
9. LoopsDemo-style: for i=1..6, continue on even, break if i>4, print odds → ?
10. `(int) values` where value is Integer 42 via `instanceof Integer i` — what prints for `i`?

## Part C — Debugging

11. `if (ready); { doWork(); }` always runs `doWork`. Why?
12. Classic switch missing `break` prints two messages. Fix two ways.
13. Enhanced-for removal from a List throws—what’s the safer approach? (conceptual)

## Part D — Coding

14. Write `FizzBuzz` for 1..15 (print rules: ÷15 FizzBuzz, ÷3 Fizz, ÷5 Buzz, else number).
15. Write a switch expression mapping HTTP-like codes 200/404/500 to labels; default `"other"`.
16. Write `describe(Object o)` using pattern instanceof for String/Integer/null/other.

## Part E — Advanced

17. Add a sealed interface with two records and an exhaustive area/perimeter switch.
18. Explain one dominance/order pitfall in pattern switches.

## Scoring guide

| Band | Next |
|------|------|
| Strong | Module 04 |
| Partial | Re-read weak chapters + rerun demos |
| Weak | Redo all Module 03 examples |

## Chapter mapping

| Q | Ch |
|---|-----|
| 1,7,11 | 01 |
| 2,8,12,15 | 02 |
| 3,4,9,13–14 | 03 |
| 5,6,10,16–18 | 04 |
