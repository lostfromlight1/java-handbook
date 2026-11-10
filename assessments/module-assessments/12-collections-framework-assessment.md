# Assessment: Module 12 — Collections Framework

> **Type:** Module  
> **Maps to:** Module 12 chapters 01–05  
> **Time guide:** 50–80 minutes  
> **Solutions:** [12-collections-framework-assessment-solutions.md](12-collections-framework-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.

## Part A — Conceptual

1. Does `Map` extend `Collection`? Why or why not?
2. What does `Set.add` return when the element is already present (equal)?
3. Default `List` implementation most teams start with?
4. `ArrayList` backing structure in OpenJDK terms?
5. `get(i)` time on `LinkedList`?
6. `HashSet` iteration order guarantee?
7. `LinkedHashSet` iteration order?
8. `TreeSet` requires what from element type or constructor?
9. Preferred modern type instead of `Stack`?
10. Queue: difference between `poll` and `remove` when empty?
11. Two steps `HashMap` uses after `hashCode` on lookup?
12. If `a.equals(b)`, what must hold for `hashCode`?
13. What happens on second `put` with equal key in `HashMap`?
14. What is fail-fast iteration?
15. Safe way to remove during `while (it.hasNext())` traversal?

## Part B — Predict (CollectionsHierarchyDemo)

16. `collectionSize=`?
17. `listSize=`?
18. `setSize=`?
19. `containsY=`?

## Part C — Predict (ListsDemo)

20. `arrayList=`?
21. `get1=`?
22. `poll=`?
23. `linkedAfter=`?

## Part D — Predict (SetsQueuesDemo)

24. `hashSize=`?
25. `linkedOrder=`?
26. `treeOrder=`?
27. `peek=` after both offers?
28. `poll=`?
29. `queue=` after one poll?

## Part E — Predict (MapsHashingDemo)

30. `Ada=`?
31. `size=` (string keys map)?
32. `containsLin=`?
33. `keyedSize=`?
34. `keyedA=`?

## Part F — Predict (CollectionsChoiceDemo)

35. `index2=`?
36. `kb=`?
37. `odds=` after even removal?

## Part G — Debugging / Design

38. `for (String s : set) { set.remove(s); }` on `HashSet` — typical outcome?
39. Using mutable object as `HashMap` key and mutating field in `hashCode`?
40. `LinkedList` + loop `get(i)` for all i — complexity class?
41. `List.of("a", null)` — compile/runtime?
42. Raw `List list = new ArrayList(); list.add(1);` — generics issue?
43. Why return `List.copyOf(backing)` from a getter?
44. When prefer `TreeMap` over `HashMap`?
45. `ArrayDeque` allows `null` elements?

## Part H — Coding

46. Method `dedupe(List<String> in)` preserving first-seen order.
47. `Map<String, Integer>` word count using `merge`.
48. Queue of tasks: `offer` three names, print after each `poll`.
49. Class `Sku` with `String code` suitable as `HashMap` key.
50. Remove all nulls from `ArrayList<String>` with `removeIf`.

## Part I — Advanced

51. What is `modCount` used for in OpenJDK `ArrayList`?
52. HashMap load factor default?
53. Why `HashSet` can contain at most one `null`?
54. `EnumSet` vs `HashSet<Enum>` for enum constants?
55. Java 21 `SequencedCollection` — one benefit?

## Scoring guide (self-check)

| Part | Items | Focus |
|------|-------|-------|
| A | 1–15 | Hierarchy and contracts |
| B–F | 16–37 | Verified demo output |
| G | 38–45 | Pitfalls and API choice |
| H | 46–50 | Small programs |
| I | 51–55 | Internals and Java 21 |

Review wrong items in the mapped chapters before Module 13.

## Chapter mapping

| Question | Chapter |
|----------|---------|
| 1–4, 16–19 | 01 |
| 5, 20–23 | 02 |
| 6–10, 24–29 | 03 |
| 11–14, 30–34 | 04 |
| 15, 35–37, 38–45, 51–55 | 05 |
