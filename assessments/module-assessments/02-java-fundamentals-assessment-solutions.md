# Solutions: Module 02 — Java Fundamentals Assessment

## Part A

1. `byte short int long float double char boolean`. Beginners: `int`, `double`.
2. No—reference type; can be `null`; has methods; `==` is identity.
3. Cannot reassign that variable.
4. Both operands `int` → integer division truncates toward zero.
5. Skip right side when result known: `x != null && x.length() > 0` skips `length` if null (for references later)—or `false && (1/0==0)` won’t divide.
6. Names visible only inside their declaring `{ ... }` (and nested blocks).
7. Narrowing needs a cast; widening usually does not.
8. No—`var` is compile-time inference; still static typing.

## Part B

9. `14`
10. `n=23` vs `n=5`
11. Prints `5`, then `x` is `6`
12. `3`

## Part C

13. Use `3000000000L`.
14. `double avg = (double) sum / count;`
15. Compile error; declare outside if needed or use a different variable for the result.

## Part D

16. Example: `double f = c * 9.0 / 5.0 + 32;` (ensure floating operands).
17. Declare `int inner` inside `{ }`; commented use outside shows the lesson.

## Part E

18. Binary floating rounding; prefer integer cents or `BigDecimal`.
19. When the RHS type is unclear and the left-side type documents the API.
