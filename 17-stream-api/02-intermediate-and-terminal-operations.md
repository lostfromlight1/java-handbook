# Intermediate and Terminal Operations

> **Status:** Completed  
> **Module:** 17 — Stream API  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Use common **intermediates**: `filter`, `map`, `flatMap`, `sorted`, `distinct`, `limit`, `skip`.
- Use common **terminals**: `forEach`, `reduce`, `collect`, `min`/`max`, `count`, `findFirst`, `anyMatch`/`allMatch`/`noneMatch`.
- Map elements with **`map`** vs flatten with **`flatMap`**.
- Interpret **`Optional`** results from find/max/min.
- Avoid mutable accumulation inside `forEach`.

## 2. Prerequisites

- **Ch.01** — pipeline model.
- **Module 18 Ch.02** — `Optional` preview (terminals return Optional often).

## 3. Why This Concept Exists

Each operation encodes a reusable iteration pattern. **`flatMap`** models one-to-many transforms (lines to words). **`reduce`** expresses folds without external index variables.

## 4. Conceptual Explanation

### 4.1 Intermediate catalog (frequent)

| Op | Role |
|----|------|
| `filter` | Keep matching elements |
| `map` | 1:1 transform |
| `flatMap` | 1:many flatten |
| `sorted` | Stable order (natural or Comparator) |
| `distinct` | Dedupe via `equals` |
| `peek` | Debug side effect (still lazy) |

### 4.2 Terminal catalog (frequent)

| Op | Role |
|----|------|
| `toList()` | Unmodifiable list (Java 16+) |
| `collect` | Mutable reduction via `Collector` (Ch.03) |
| `reduce` | Associative fold |
| `forEach` | Side effect per element |
| `count` | Count after intermediates |
| `max`/`min` | Optional extremum |

### 4.3 `reduce` identity vs no identity

```java
int sum = nums.stream().reduce(0, Integer::sum);
Optional<Integer> max = nums.stream().reduce(Integer::max);
```

Identity form always returns `T`; no-identity returns empty Optional on empty stream.

## 5. Terminology

| Term | Meaning |
|------|---------|
| `flatMap` | map then flatten `Stream` per element |
| Associative reduction | `(a op b) op c` equals `a op (b op c)` for reduce |

## 6. Syntax and Language Rules

- `sorted()` requires `Comparable` or `Comparator`.
- `forEach` order on **parallel** streams is intentionally undefined for general consumers.
- Primitive streams (`mapToInt`) avoid boxing.

## 7. Step-by-Step Examples

Demo: [stream-operations-demo](../../examples/core-java/stream-operations-demo/).

Pipeline on `[3, 10, 7, 4, 8]`:
- Evens: 10, 4, 8 → double → 20, 8, 16 → sorted → **8, 16, 20**

**Verified output:**

```text
evensDouble=[8, 16, 20]
sum=32
max=10
anyBig=true
```

## 8. Real-World Use Cases

- Invoice line totals with `mapToInt` + `sum`.
- Deduplicate API IDs with `distinct`.
- Pagination via `skip` + `limit`.

## 9. Internal Behavior

`distinct` uses `LinkedHashSet` or `ConcurrentHashMap` (Sized) internally for sequential/parallel—implementation may change; memory cost for large streams.

## 10. Edge Cases and Limitations

- Empty stream: `reduce(Integer::max)` → empty Optional; identity reduce returns identity.
- `findFirst` on parallel may differ from `findAny` under race.
- **`Comparator` nulls** — specify `nullsFirst`.

## 11. Common Mistakes

### Incorrect — external mutable list in forEach

```java
List<Integer> bad = new ArrayList<>();
nums.parallelStream().forEach(bad::add);
```

### Correct

```java
List<Integer> good = nums.stream().filter(...).toList();
```

### Incorrect — get() without check

```java
stream.max(Comparator.naturalOrder()).get(); // NoSuchElementException if empty
```

### Correct

```java
int m = stream.max(Comparator.naturalOrder()).orElse(-1);
```

## 12. Best Practices

- Use `mapToInt` when primitives dominate.
- Prefer declarative `filter`/`map` over indexed loops when readability wins.
- Replace `forEach` + external mutation with `collect`/`reduce`.

## 13. Design Trade-offs

`flatMap` vs nested loop: flatMap clearer for nested lists; loop fine for two-level only.

## 14. Performance Considerations

`sorted()` is O(n log n) and buffers full stream—cannot short-circuit sorting.

## 15. Security Considerations

Filtering PII with `filter` still materializes values in memory—protect downstream logs.

## 16. Java Version Notes

`toList()` Java 16+. `Stream.toList()` is unmodifiable.

## 17. Related Comparisons

Reactive streams (RxJava) async-push; java.util.stream is pull-based in-process.

## 18. Revision and Mastery

- Explain `evensDouble` ordering.
- `flatMap` vs `map` for `List<List<T>>`.

## 19. Exercises

1. Predict demo output lines.
2. From words list, flatMap to chars count unique.

## 20. Interview Questions

1. `map` vs `flatMap`?
2. Terminal that short-circuits?
3. Why not `forEach` to build list?

## 21. Solutions

1. §7.
2. `words.stream().flatMap(w -> w.chars().mapToObj(c -> (char)c)).distinct().count()`.

## 22. Chapter Summary

Intermediates transform lazily; terminals finish the pipeline. Demo filters evens, doubles, sorts, sums, and queries max/anyMatch.

## 23. Key Terminology

filter · map · flatMap · reduce · Optional terminal

## 24. References

- [Stream interface](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Stream.html)

## 25. Next Steps

- [03-collectors-and-grouping.md](03-collectors-and-grouping.md)

## Review Checklist

- [x] Demo verified
