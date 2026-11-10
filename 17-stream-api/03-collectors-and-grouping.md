# Collectors and Grouping

> **Status:** Completed  
> **Module:** 17 — Stream API  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Use **`Collectors.toList`**, **`toSet`**, **`toMap`**, **`joining`** with awareness of mutability and duplicate keys.
- Apply **`groupingBy`** with downstream collectors (`mapping`, `counting`, `toList`).
- Build **multi-level** grouping and partitioning with `partitioningBy`.
- Summarize with **`summingInt`**, **`averagingDouble`**, **`summarizingInt`**.
- Choose immutable vs mutable collection results.

## 2. Prerequisites

- **Ch.02** — terminal `collect`.
- **Module 12** — `Map`, `List`.
- **Module 15** — records as row types.

## 3. Why This Concept Exists

`reduce` alone is awkward for **group-by** reports. **Collectors** are reusable reduction strategies the stream library optimizes and documents—especially **downstream** collectors for nested structures.

## 4. Conceptual Explanation

### 4.1 `Collector` shape (conceptual)

A collector has **supplier** (create result), **accumulator** (add element), **combiner** (merge parallel), **finisher** (optional transform). You usually use static factories in `Collectors`.

### 4.2 `groupingBy`

```java
Collectors.groupingBy(Person::dept)
```

Produces `Map<K, List<T>>` by default—values are lists of items per key.

### 4.3 Downstream collectors

```java
Collectors.groupingBy(
    Person::dept,
    Collectors.mapping(Person::name, Collectors.toList())
)
```

Values become **lists of names** per department, not lists of `Person`.

### 4.4 `joining`

Delimiter-joined string reduction—order follows encounter order in sequential stream.

### 4.5 `toMap` hazards

Duplicate keys throw **`IllegalStateException`** unless merge function supplied.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Downstream collector | Inner collector in grouping |
| Finisher | Post-process mutable acc to immutable view |

## 6. Syntax and Language Rules

- `groupingBy` concurrent variants need concurrent downstream accumulators.
- `toUnmodifiableList` (Java 10+) for immutable lists from collect.

## 7. Step-by-Step Examples

Demo: [collectors-grouping-demo](../../examples/core-java/collectors-grouping-demo/).

**Verified output:**

```text
byDept={ops=[Zoe], eng=[Ada, Lin]}
csv=Ada, Lin, Zoe
counts={ops=1, eng=2}
```

Map iteration order is undefined for `HashMap`—lines may vary in order but content matches.

## 8. Real-World Use Cases

- Group transactions by account for daily totals.
- Build lookup `Map<String, User>` with merge on duplicate ID policy.
- CSV export via `joining`.

## 9. Internal Behavior

`groupingBy` uses `map.merge` style accumulation; parallel combiners merge sub-maps—keys must combine consistently.

## 10. Edge Cases and Limitations

- Null keys: `HashMap` allows one null key historically—avoid null group keys.
- **Mutable lists** inside map values—defensive copy if exposing outside.

## 11. Common Mistakes

### Incorrect — `toMap` without merge on duplicates

```java
stream.collect(Collectors.toMap(Person::name, p -> p)); // throws on duplicate names
```

### Correct

```java
stream.collect(Collectors.toMap(Person::name, p -> p, (a, b) -> a));
```

## 12. Best Practices

- Use **`groupingBy` + `counting`** for dashboards.
- Prefer **`toList()` terminal** when no custom collector needed.
- Document stable ordering requirements (`groupingBy` with `TreeMap` supplier).

## 13. Design Trade-offs

Imperative `Map` loop vs collectors: collectors shorter; complex reporting may read clearer in SQL or loops.

## 14. Performance Considerations

`groupingBy` allocates per key lists—fine for in-memory batch sizes; huge data needs database aggregation.

## 15. Security Considerations

Joined strings for HTML need escaping—`joining` is not escape-aware.

## 16. Java Version Notes

`Collectors` since Java 8; `teeing` collector Java 12 for dual summaries.

## 17. Related Comparisons

SQL `GROUP BY` is declarative over tables; stream grouping is in-memory over collections.

## 18. Revision and Mastery

- Why `byDept` values are `List<String>` not `List<Person>`?
- Predict `counts` map.

## 19. Exercises

1. Predict three demo lines.
2. Average salary per dept with `averagingInt`.

## 20. Interview Questions

1. Default downstream of `groupingBy`?
2. `toMap` duplicate key behavior?
3. `joining` vs manual `StringBuilder`?

## 21. Solutions

1. §7.
2. `Collectors.averagingInt(Person::salary)`.

## 22. Chapter Summary

**Collectors** encode mutable reductions; **groupingBy** with downstream **`mapping`** shapes nested reports. Demo groups names by department, joins CSV, counts per dept.

## 23. Key Terminology

Collector · groupingBy · downstream · joining

## 24. References

- [Collectors](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Collectors.html)

## 25. Next Steps

- [04-parallel-streams-and-pitfalls.md](04-parallel-streams-and-pitfalls.md)

## Review Checklist

- [x] Demo verified
