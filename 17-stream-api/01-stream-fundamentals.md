# Stream Fundamentals

> **Status:** Completed  
> **Module:** 17 — Stream API  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain what a **Stream** is (and is not) in Java.
- Create streams from collections, arrays, ranges, and generators.
- Distinguish **intermediate** vs **terminal** operations and **lazy** evaluation.
- Describe **one-shot** consumption: streams cannot be reused after terminal operation.
- Choose between **sequential** and **parallel** streams at a high level (details Ch.04).

## 2. Prerequisites

- **Module 16** — lambdas, functional interfaces.
- **Module 12** — `Collection`, immutability of source.

**Refresher:** A **Stream** is a sequence of elements supporting aggregate operations. It does **not** store elements; it reads from a **source** (collection, array, IO) through a **pipeline**.

## 3. Why This Concept Exists

Manual index loops scatter policy across `for` bodies. **Streams** centralize iteration patterns—filter/map/reduce—while leaving **what** to compute in lambdas. They enable **lazy** fusion: skip work until a terminal operation demands results, and **short-circuit** when possible (`findFirst`, `anyMatch`).

## 4. Conceptual Explanation

### 4.1 Stream is not a Collection

You do not `add` to a stream. Operations return **new streams** (intermediates) or **values** (terminals). The **source** may be mutated only with care—stream may not see changes per specification if concurrent modification occurs.

### 4.2 Pipeline structure

```
source → intermediate* → terminal
```

**Intermediate** (`filter`, `map`, `sorted`) return `Stream` and are **lazy**. **Terminal** (`count`, `collect`, `forEach`) trigger evaluation.

### 4.3 Stream sources (Java 21)

| Source | Example |
|--------|---------|
| Collection | `list.stream()` |
| Arrays | `Arrays.stream(arr)` |
| Primitives | `IntStream.range(1, 5)` |
| Factory | `Stream.of("a", "b")` |
| Builder | `Stream.builder().add(x).build()` |

### 4.4 Laziness illustration

In `StreamFundamentalsDemo`, `filter` prints **only when terminal runs**—and stops after `findFirst` once a match is found:

```text
beforeTerminal
filter:Ada
filter:Lin
firstL=Lin
```

`Ada` fails `startsWith("L")`; `Lin` matches; `Zoe` never filtered.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Intermediate operation | Lazy stage returning Stream |
| Terminal operation | Consumes stream, produces result or side effect |
| Short-circuit | Terminal may stop early (`findFirst`) |
| Spliterator | Internal splitter feeding stream from source |

## 6. Syntax and Language Rules

- Generic streams: `Stream<T>`; primitives: `IntStream`, `LongStream`, `DoubleStream`.
- **`stream()`** on collections is sequential by default; **`parallelStream()`** forks (Ch.04).
- Calling two terminals on same stream instance: **illegal** (`IllegalStateException`).

## 7. Step-by-Step Examples

Demo: [stream-fundamentals-demo](../../examples/core-java/stream-fundamentals-demo/).

**Verified output:**

```text
listCount=3
rangeSum=15
beforeTerminal
filter:Ada
filter:Lin
firstL=Lin
```

`rangeSum` is 1+2+3+4+5 = 15.

## 8. Real-World Use Cases

- Aggregating search hits before paging.
- Parsing log lines in memory batches.
- Joining in-memory report rows without SQL when data already loaded.

## 9. Internal Behavior

Streams use **Spliterator** (`trySplit` for parallel). Laziness implemented by **iterator chaining** of op wrappers (implementation detail in `java.util.stream`). Label as **JDK implementation**, not JLS guarantee of evaluation order for side effects—**do not rely on side effect order** in parallel.

## 10. Edge Cases and Limitations

- Infinite streams need short-circuit or limit (`Stream.iterate`, `generate`).
- `null` elements allowed unless stream forbids; many ops throw NPE if null where forbidden.
- Modifying backing list during sequential stream: `ConcurrentModificationException` likely.

## 11. Common Mistakes

### Incorrect — reusing stream

```java
Stream<String> s = list.stream();
s.count();
s.count(); // IllegalStateException
```

### Correct — new stream per pipeline

```java
long n = list.stream().count();
```

### Incorrect — assuming eager filter

```java
System.out.println("setup");
stream.filter(...); // no output until terminal
```

## 12. Best Practices

- Prefer **clear terminal** (`toList()` Java 16+) over manual collect when unmodifiable list OK.
- Keep side effects out of intermediates; use `peek` only for debugging.
- Do not use streams when simple indexed loop is clearer.

## 13. Design Trade-offs

| Loop | Stream |
|------|--------|
| Break/continue labels easy | Short-circuit terminals |
| Mutable accumulation local | Collectors (Ch.03) |
| Lowest overhead tiny lists | Clarity at scale |

## 14. Performance Considerations

Streams add object overhead; for 3-element lists a `for` loop may win. Streams shine on larger data and composable logic—profile.

## 15. Security Considerations

Do not expose unbounded streams on attacker-sized inputs without `limit`.

## 16. Java Version and Compatibility Notes

Streams since Java 8. **`toList()`** unmodifiable since Java 16. Java 21 adds no stream syntax change.

## 17. Related Language Comparisons

C# LINQ is similar but integrated with language syntax. Java streams stay library-based.

## 18. Revision and Mastery

- **Why** only two `filter:` lines printed?
- **Gap:** treating stream as collection copy.

## 19. Exercises

1. Predict `StreamFundamentalsDemo` lazy lines.
2. Create `IntStream` of squares 1–5, sum with terminal.

## 20. Interview Questions

1. Difference intermediate vs terminal?
2. Can you reuse a stream?
3. What triggers evaluation?

## 21. Solutions

1. See §7.
2. `IntStream.rangeClosed(1,5).map(x->x*x).sum()` → 55.

## 22. Chapter Summary

**Streams** are lazy pipelines over sources. **Intermediates** defer work until **terminals** run; short-circuit ops stop early. Demo shows lazy `filter` with `findFirst`.

## 23. Key Terminology

Stream · lazy · terminal · short-circuit · Spliterator

## 24. References

- [Package java.util.stream](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html)

## 25. Next Steps

- [02-intermediate-and-terminal-operations.md](02-intermediate-and-terminal-operations.md)

## Review Checklist

- [x] Depth gate; demo verified
