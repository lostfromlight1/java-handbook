# Assessment: Module 11 — Arrays and Strings

> **Type:** Module  
> **Maps to:** Module 11 chapters 01–04  
> **Time guide:** 45–75 minutes  
> **Solutions:** [11-arrays-and-strings-assessment-solutions.md](11-arrays-and-strings-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.

## Part A — Conceptual

1. What property of Java arrays is fixed at creation time?
2. Default value of `new int[3]` elements? Of `new String[3]`?
3. Why does `int[] a = {1}; int[] b = a; b[0]=2` change `a[0]`?
4. What exception is thrown for index `arr.length`?
5. Define **immutability** for `String`.
6. When is `a == b` true for two `String` variables with same text?
7. Why should you use `equals` for user input comparison?
8. What is the **string pool**?
9. Difference between `StringBuilder` and `StringBuffer` (thread safety)?
10. What delimiter starts a **text block**?
11. Why avoid `+` in a tight loop building one large string?
12. What is a UTF-16 **surrogate pair**?
13. Why might `String.length()` differ from UTF-8 byte length?
14. Why call `getBytes(StandardCharsets.UTF_8)` instead of `getBytes()`?
15. What does `Arrays.binarySearch` require about the array?

## Part B — Predict (ArraysDemo)

16. `len=`?
17. `max=`?
18. `sorted=`?
19. `grid01=`?
20. If you sort `scores` in place without `copyOf`, does original order change?

## Part C — Predict (StringsDemo)

21. `a==b=`?
22. `a==c=`?
23. `a.equals(c)=`?
24. `upper=`?
25. `sub=`?

## Part D — Predict (StringBuilderTextBlockDemo)

26. First line of output?
27. `jsonLines=`?
28. Does the JSON block include a line with `"name"`?
29. Is `StringBuilder` immutable?
30. Must you call `toString()` before passing builder to `System.out.println` expecting text?

## Part E — Predict (EncodingPerfDemo)

31. `chars=` for `café`?
32. `utf8Bytes=`?
33. After round trip, is content still `café`?
34. `built=`?
35. Does `text.length()` equal `text.getBytes(UTF_8).length` for all strings?

## Part F — Debugging / Design

36. `String s = read(); if (s == "OK")` — typical bug?
37. `charAt(0)` on empty string?
38. `new String("x") == "x"` — usually?
39. Ragged `int[][]` — allowed?
40. Storing passwords in `String` — concern?

## Part G — Coding

41. Method `max(int[] a)` returning largest element or throw if empty.
42. Build CSV `"1,2,3"` with `StringBuilder` loop.
43. Text block for two-line HTML `<p>` without leading spaces in output.
44. Encode `"Hi"` to UTF-8 and decode back.
45. `firstCodePoint(String s)` returning first Unicode code point as `int`.

## Part H — Advanced

46. Array covariance: `Object[] o = new String[1];` — what fails at runtime?
47. When is `String.intern()` dangerous?
48. `codePointCount` vs `length` for `"😀"`?
49. JEP 378 — one sentence purpose.
50. Why compressed strings (JEP 254) do not change `String` API guarantees.

## Scoring guide (self-check)

| Part | Items | Focus |
|------|-------|-------|
| A | 1–15 | Vocabulary and rules |
| B–E | 16–35 | Demo predictions |
| F | 36–40 | Pitfalls |
| G | 41–45 | Small programs |
| H | 46–50 | Internals and policy |

Review wrong items in the mapped chapters before Module 12.
