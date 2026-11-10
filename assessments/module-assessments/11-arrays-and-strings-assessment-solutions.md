# Solutions: Module 11 — Arrays and Strings

> **Assessment:** [11-arrays-and-strings-assessment.md](11-arrays-and-strings-assessment.md)

## Part A — Conceptual

1. **Length** (element count).
2. `0` for `int`; `null` for `String` references.
3. **Reference assignment** — `b` aliases same array object as `a`.
4. **`ArrayIndexOutOfBoundsException`**.
5. Character sequence cannot change after construction; “modifying” methods return new strings.
6. When both references point to the **same interned/pooled** object (or same object generally)—not guaranteed for arbitrary content.
7. User input creates **new heap strings**; `==` compares identity, not content.
8. Pool of canonical string literals / `intern()` storage (implementation-managed).
9. `StringBuffer` methods are **synchronized**; `StringBuilder` is not.
10. **`"""`** (three double quotes) with newline rules per JLS.
11. Repeated copying causes **quadratic** time/allocation; builder amortizes growth.
12. Two `char` code units representing one supplementary code point.
13. `length()` counts UTF-16 code units; UTF-8 encoding is **variable-width** per character.
14. Platform **default charset** is environment-dependent; UTF-8 is portable.
15. Array must be **sorted** (ascending for natural ordering used by overload).

## Part B

16. `len=3`
17. `max=95`
18. `sorted=[85, 90, 95]`
19. `grid01=b`
20. **Yes** — in-place sort mutates `scores`.

## Part C

21. `a==b=true`
22. `a==c=false`
23. `a.equals(c)=true`
24. `upper=JAVA`
25. `sub=hand`

## Part D

26. `Hello Java!`
27. `jsonLines=4`
28. **Yes**
29. **No** — mutable buffer.
30. `println` calls `String.valueOf` which invokes **`toString()`** on builder—works, but APIs expecting `String` still need explicit `toString()`.

## Part E

31. `chars=4`
32. `utf8Bytes=5`
33. **Yes** (logical content `café`)
34. `built=012`
35. **No** — e.g. emoji and BMP accents differ.

## Part F

36. **`==` compares references** — use `equals`.
37. **`StringIndexOutOfBoundsException`**.
38. Usually **false** (`new` creates distinct object).
39. **Yes** — rows may differ in length.
40. **Immutability** — value may linger in heap until GC; prefer `char[]` wipe patterns for secrets (advanced).

## Part G

41. Loop or stream max; throw `IllegalArgumentException` if `length==0`.
42. Loop `append(i)` with comma between `0` and `n-1`.
43. Align closing `"""` and use strip rules; verify with `println`.
44. `byte[] b = "Hi".getBytes(StandardCharsets.UTF_8); new String(b, UTF_8);`
45. `s.codePointAt(0)` if non-empty.

## Part H

46. `o[0] = new Object();` → **`ArrayStoreException`**.
47. Untrusted **`intern()`** can exhaust perm/gen pool memory.
48. `length()` **2**; `codePointCount` **1**.
49. Multiline string literals with readable indentation stripping.
50. Still immutable `String` API; internal representation is JDK optimization only.
