# Solutions: Module 13 — Generics

> **Assessment:** [13-generics-assessment.md](13-generics-assessment.md)

## Part A

1. **Type parameter** is the formal name on a declaration (`T` in `class Box<T>`); **type argument** is the concrete substitution at use site (`String` in `Box<String>`).
2. **Invariance** — `List<Integer>` must not accept arbitrary `Number` elements via a `List<Number>` reference; would break type safety.
3. **PECS:** Producer **extends**, Consumer **super** — read with `? extends T`, write with `? super T`.
4. No (except `null` in some cases) — compiler cannot know concrete list type; `add` might break `List<Integer>`.
5. **`Object`** (with casts at boundaries).
6. **Type erasure** — runtime class is raw `ArrayList`; type arguments are not reified on `Class`.
7. Preserves **override/polymorphism** when erased superclass signature differs from subclass generic signature.
8. Examples: `new T()`, `T[]` array creation, `instanceof T`, `catch (T e)`.
9. Method will not cause **heap pollution** from the varargs array exposure pattern; compiler suppresses related warnings.
10. Callers avoid **null** ambiguity; composable `map`/`filter` without NPE on absent values (not for fields).
11. **`<T extends Comparable<T>>`** when one `T` flows through method body and you call `compareTo` on `T`; **`? extends Comparable<?>`** when you only read heterogeneous comparables from a list.
12. Compiler introduces a fresh type variable so bodies can type-check operations on wildcard parameters (e.g. `dest.add(item)` in `copy`).

## Part B

13. `hello`  
14. `1`  
15. `a:1`

## Part C

16. `6.0`  
17. `[1, 2, 3]`  
18. `1`

## Part D

19. `true`  
20. `hi`  
21. `7`  
22. `[a, b]`

## Part E

23. `Ada`  
24. `false`  
25. `1`

## Part F

26. **Heap pollution** — element may not be `String`; `ClassCastException` when treated as `String`.
27. **Parameterized type** with concrete argument is not reifiable at runtime; use `instanceof List<?>` or validate elements.
28. `static <T> void copy(List<? extends T> src, List<? super T> dest)` — for `Integer`/`Object`, `T` binds to `Integer`.
29. No — expose **`List.copyOf`**, **unmodifiable view**, or immutable snapshot; keeps encapsulated `T`.
30. **`Comparator<? super String>`** — contravariant consumer of comparators (`Object` compares wider).

## Remediation map

| Question buckets | Chapter |
|------------------|---------|
| 1–2, 13–15 | Ch.01 |
| 3–4, 11–12, 16–18, 28–30 | Ch.02 |
| 5–9, 19–22, 26–27 | Ch.03 |
| 10, 23–25, 29 | Ch.04 |
