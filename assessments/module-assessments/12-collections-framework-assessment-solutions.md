# Solutions: Module 12 — Collections Framework

> **Assessment:** [12-collections-framework-assessment.md](12-collections-framework-assessment.md)

## Part A

1. No — `Map` models key–value pairs; `Collection` models single elements.
2. `false`.
3. `ArrayList`.
4. Growable `Object[]` (reference arrays) plus `size`.
5. O(n) — walk from nearer end.
6. None guaranteed; can change on rehash.
7. Insertion order (first time each element was added).
8. `Comparable` natural order or a `Comparator` passed to constructor.
9. `ArrayDeque` implementing `Deque`.
10. `poll` returns `null`; `remove` throws `NoSuchElementException`.
11. Find bucket, then `equals` compare keys in bucket chain/tree.
12. `a.hashCode() == b.hashCode()`.
13. Replaces the previous value; still one entry for that key.
14. Iterator throws `ConcurrentModificationException` if collection structurally modified without iterator’s `remove`.
15. `it.remove()` after `it.next()`.

## Part B

16. `2`  
17. `2`  
18. `2`  
19. `true`

## Part C

20. `[one, 1.5, two]`  
21. `1.5`  
22. `head`  
23. `[tail]`

## Part D

24. `2`  
25. `[b, a]`  
26. `[a, b]`  
27. `1`  
28. `1`  
29. `[2]`

## Part E

30. `37`  
31. `2`  
32. `true`  
33. `1`  
34. `two`

## Part F

35. `3`  
36. `5`  
37. `[1, 3]`

## Part G

38. `ConcurrentModificationException` (fail-fast).
39. Entry may become unreachable; `get` fails despite “same” object reference.
40. O(n²) total for n elements.
41. `NullPointerException` at runtime — `List.of` forbids null elements.
42. Unchecked warnings; heap pollution risk; no compile-time type safety on `add`.
43. Callers cannot mutate internal list; publication safety.
44. Sorted iteration, range views (`subMap`), or `NavigableMap` operations needed.
45. No — `NullPointerException` on `offer(null)`.

## Part H

46. Example:

```java
static List<String> dedupe(List<String> in) {
    return new ArrayList<>(new LinkedHashSet<>(in));
}
```

47. Loop words; `counts.merge(word, 1, Integer::sum)`.

48. `offer` a,b,c; `poll` prints a, then b, then c.

49. `final class` with `final String code`; `equals`/`hashCode` on `code` only.

50. `list.removeIf(Objects::isNull);`

## Part I

51. Tracks structural modifications for fail-fast iterators.
52. `0.75f` default load factor on `HashMap`.
53. `HashMap` allows one null key; set delegates to map keys.
54. `EnumSet` is compact bit vector, faster and more memory-efficient for enums.
55. Explicit first/last element operations on ordered collections (`getFirst`, `reversed`, etc.).
