# Solutions: Module 10 — Java Type System

> **Assessment:** [10-java-type-system-assessment.md](10-java-type-system-assessment.md)

## Part A — Conceptual

1. Static: types checked at compile time. Strong: no silent mixing of incompatible types; explicit rules/casts.
2. Declared type: compile-time label of reference. Runtime class: actual object’s `Class`.
3. No — `var` infers one static type for the variable; reassign to incompatible type fails.
4. Runtime check of assignability; `null instanceof T` is false for reference T.
5. Test + bind without separate cast; clearer multi-way dispatch.
6. When selector type is sealed and all permitted subtypes covered (exhaustive).
7. Invariance: could break list by adding non-Integer Numbers if assignment were allowed.
8. Producer Extends, Consumer Super; read `? extends`, write `? super`.
9. Read as Number; add generally forbidden (except null nuances).
10. Write Integer; read typically as Object.
11. Generic type parameters erased in bytecode to bounds/Object; compile-time checks only.
12. Runtime class is raw `ArrayList`; type args not reified.
13. `Number[]` may refer to `Integer[]`; store wrong element throws at runtime; `List<>` blocks at compile time.
14. Generic type without type args; weaker checks.
15. `instanceof` for inheritance; exact class match with `getClass()==`.

## Part B

16. `3 items`
17. `runtime=Integer`
18. `intValue()` declared on `Number`.
19. Compiles with cast; `ClassCastException` if not String at runtime.
20. `len=5`

## Part C

21. `string:Ada`
22. `int:21`
23. `null`
24. `double:3.5`
25. `long=java`
26. No — not a String.
27. `other:true`
28. Order among cases matters for dominance; `null` is separate pattern—typically explicit early.

## Part D

29. `sum=3`
30. `first=1`
31. `objs=[7]`
32. `listClass=ArrayList`
33. Yes; sum is 4 (int truncation of doubles in `intValue()`).
34. Yes — `List<Integer>` is acceptable where `List<? super Integer>` is required (consumer `super`); `3` autoboxes to `Integer`.

## Part E

35. No — invariance.
36. `void copyNumbers(List<? super Number> dest, List<? extends Number> src) { for (Number n : src) dest.add(n); }`
37. Could insert wrong Number into unknown concrete list behind `? extends`.
38. Runtime cannot verify `String` elements; heap pollution risk.

## Part F

39. Sample: `if (list.isEmpty()) throw new NoSuchElementException(); return list.get(0);`
40. Mirror `InstanceofPatternsDemo.describe` structure.
41. Loop `src`, `dest.add` each element.

## Part G

42. Compiler generates bridges so erased signatures still override correctly (M13).
43. Comparator consumes `T` for comparison — contravariant `? super T`.
44. Declaration-site on type declaration reduces wildcard noise; Java uses use-site wildcards on methods.
