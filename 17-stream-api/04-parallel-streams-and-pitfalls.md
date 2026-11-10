# Parallel Streams and Pitfalls

> **Status:** Completed  
> **Module:** 17 — Stream API  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain how **parallel streams** use the **ForkJoin common pool**.
- Decide when parallelization may help vs hurt.
- Identify **thread-safety** requirements on sources and accumulators.
- Avoid **side effects** and shared mutation in parallel pipelines.
- Use **`parallel()`** / **`parallelStream()`** with ordering awareness.

## 2. Prerequisites

- **Ch.01–03** — sequential pipelines, collectors.
- **Module 34+** (future) — deeper concurrency; here: basic happens-before intuition.

## 3. Why This Concept Exists

Multi-core CPUs waste time if all collection work stays single-threaded. **Parallel streams** attempt automatic splitting via **Spliterator** and **ForkJoinPool**—convenient but easy to misuse.

## 4. Conceptual Explanation

### 4.1 Fork/join model

`parallelStream()` marks the pipeline for parallel execution. The framework splits the source, processes chunks on **common pool** workers, **combines** results (collectors' combiners, reduction associativity).

### 4.2 When parallel might help

- Large in-memory data.
- **CPU-bound** pure `map/filter` per element.
- **Associative** reductions with cheap per-element work.

### 4.3 When parallel hurts

- Small collections (split overhead dominates).
- **IO-bound** work (blocks common pool—bad for whole JVM).
- **Order-sensitive side effects**.
- Non-thread-safe shared structures.

### 4.4 Ordering

`forEach` on parallel streams: **order not guaranteed**. Use `forEachOrdered` if you pay for ordering.

### 4.5 Demo: pure map vs shared list

`ParallelStreamsDemo` shows identical mapped lists sequential vs parallel for tiny data. The **anti-pattern** block mutates shared `ArrayList`—on larger inputs you risk **lost elements**, **ArrayIndexOutOfBoundsException**, or corrupt size; demo may still print `badSize=5` by luck on five elements.

**Verified output:**

```text
sequential=[10, 20, 30, 40, 50]
parallel=[10, 20, 30, 40, 50]
sideEffect=5
badSize=5
```

Do not interpret `badSize=5` as proof the pattern is safe.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Common pool | Shared `ForkJoinPool` for parallel streams |
| Combiner | Merges partial results in parallel collect |
| Side effect | Mutation of non-local state during pipeline |

## 6. Syntax and Language Rules

- `stream().parallel()` toggles parallel flag on pipeline.
- Custom `ForkJoinPool` wrapper requires submitting task to alternate pool—advanced pattern outside default API.

## 7. Step-by-Step Examples

Demo: [parallel-streams-demo](../../examples/core-java/parallel-streams-demo/).

Safe pattern:

```java
List<Integer> mapped = data.parallelStream().map(n -> n * 10).toList();
```

## 8. Real-World Use Cases

- Large batch price recalculations in memory.
- Scientific simulations over arrays (after profiling).

Often **not** used in request-thread servlet code—use dedicated executor instead.

## 9. Internal Behavior

Default parallelism is `Runtime.getRuntime().availableProcessors() - 1` (HotSpot common pool sizing—implementation detail). **Spliterator** must report `ORDERED`, `SIZED`, `SUBSIZED` for good splits.

## 10. Edge Cases and Limitations

- **Thread-local random** streams OK; **ThreadLocal** mutation inside pipeline still risky.
- Nested parallel streams can **starve** common pool.
- **Stateful** lambdas (`map` with counter) race.

## 11. Common Mistakes

### Incorrect — parallel + synchronized list add without need

```java
list.parallelStream().forEach(results::add);
```

### Correct

```java
List<T> results = list.parallelStream().map(...).toList();
```

### Incorrect — parallel stream inside request handler doing blocking HTTP

Blocks common pool—use **`ExecutorService`**.

## 12. Best Practices

- Measure with JMH or production metrics—default is sequential.
- Keep pipelines **pure** per element.
- For IO concurrency, use structured executors (Java 21+ elsewhere in curriculum).

## 13. Design Trade-offs

Manual `ExecutorService` vs parallel stream: executors give naming, rejection policy, isolation; streams give succinct splits for homogeneous CPU work.

## 14. Performance Considerations

Amdahl’s law: serial portions limit speedup. Split costs dominate under ~10k elements for light ops (rule of thumb—profile your case).

## 15. Security Considerations

Parallel leaks timing side channels in crypto—use constant-time libs, not parallel maps.

## 16. Java Version Notes

Parallel streams since Java 8; no Java 21 change to defaults.

## 17. Related Comparisons

Java 21 **virtual threads** address IO concurrency differently—not a drop-in parallel stream replacement.

## 18. Revision and Mastery

- Why is shared `ArrayList` unsafe?
- When is `forEachOrdered` needed?

## 19. Exercises

1. Predict demo four lines.
2. Rewrite anti-pattern using `collect(toList())`.

## 20. Interview Questions

1. Which pool do parallel streams use?
2. Is `ArrayList` thread-safe for parallel forEach add?
3. Does parallel `map` always speed up?

## 21. Solutions

1. §4.5.
2. `data.parallelStream().map(...).collect(Collectors.toList())`.

## 22. Chapter Summary

**Parallel streams** speed **pure**, **associative**, **large** CPU work using the **common pool**. **Shared mutation** and **blocking IO** are pitfall magnets. Demo contrasts safe `map` with documented unsafe `forEach` accumulation.

## 23. Key Terminology

Parallel stream · ForkJoinPool · combiner · side effect

## 24. References

- [Java Tutorial — Parallelism](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html)

## 25. Next Steps

- **Module 18** — Optional and null handling

## Review Checklist

- [x] Pitfalls emphasized; demo verified
