# Internals and Choosing Structures

> **Status:** Completed  
> **Module:** 12 — Collections Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Deep-dive
> **Depth target:** Levels 3–4 (fail-fast, `modCount`, resize, selection checklist)
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. Do **not** manufacture content to fill headings. For omitted sections, record the reason in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow *(or merge note)* | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs *(strongly encouraged for Technical / Deep-dive)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Select **`List`**, **`Set`**, **`Queue`**, and **`Map`** implementations from **access patterns**, not habit.
- Explain **fail-fast** iterators, **`modCount`**, and **`ConcurrentModificationException`**.
- Remove elements **safely** during traversal with **`Iterator.remove`**.
- Use **`Collections`** utilities (unmodifiable, sync wrappers) judiciously.
- Apply a **decision checklist** for production code and interviews.

## 2. Prerequisites

- **Module 12 Ch.01–04** — hierarchy, lists, sets/queues, maps.
- **Module 11** — arrays vs `ArrayList` for fixed vs growable storage.
- **Module 06 Ch.04** — keys and set elements need coherent `equals`/`hashCode`.

## 3. Why This Concept Exists

Knowing interfaces is not enough—shipping code that uses **`LinkedList`** for random access or **`HashMap`** keys without `hashCode` wastes CPU and creates subtle bugs. This chapter ties **internals sketches** to **decision rules** and **iteration safety** you need before generics-heavy APIs (Module 13) and concurrency collections (later modules).

## 4. Conceptual Explanation

### 4.1 Decision guide (default choices)

| Need | Start with | Consider instead when… |
|------|------------|-------------------------|
| Ordered sequence, index access | `ArrayList` | `LinkedList`/`ArrayDeque` for heavy head/tail dequeue |
| Unique elements, fast contains | `HashSet` | `LinkedHashSet` for stable order; `TreeSet` for sorted |
| Key → value lookup | `HashMap` | `LinkedHashMap` for order; `TreeMap` for range/sort |
| FIFO work queue | `ArrayDeque` as `Queue` | `PriorityQueue` for priority; JUC queues when blocking |
| Immutable constant group | `List.of`, `Set.of`, `Map.of` | `Collections.unmodifiableX` on mutable builder |

**Rule of thumb:** **`ArrayList` + `HashSet` + `HashMap` + `ArrayDeque`** cover most backend services until profiling says otherwise.

### 4.2 Structural modification and fail-fast iterators

Collections like **`ArrayList`** track a **`modCount`** field incremented on add/remove that change size. An iterator remembers the **`expectedModCount`**. If they diverge during iteration (except through **`iterator.remove()`**), the iterator throws **`ConcurrentModificationException`**.

This is **best-effort detection**, not a concurrency guarantee—two threads can still corrupt a non-synchronized collection without immediate exception.

### 4.3 Safe removal patterns

**Incorrect during enhanced for:**

```java
for (Integer x : scores) {
    if (x % 2 == 0) {
        scores.remove(x);   // ConcurrentModificationException
    }
}
```

**Correct — iterator remove:**

```java
Iterator<Integer> it = scores.iterator();
while (it.hasNext()) {
    if (it.next() % 2 == 0) {
        it.remove();
    }
}
```

**Also valid — removeIf (Java 8+):**

```java
scores.removeIf(x -> x % 2 == 0);
```

### 4.4 Hash table resize (sketch)

When **`HashMap`** or **`HashSet`** exceeds **load factor × capacity**, it **rehashes** into a larger bucket array. Rehash recomputes bucket indices—iteration order of hash sets can change across resize. Do not depend on hash iteration order for persistence.

### 4.5 Big-O summary for interviews

| Structure | get/contains (typical) | insert end | insert middle | ordered iterate |
|-----------|------------------------|------------|---------------|-----------------|
| `ArrayList` | O(1) get | O(1)* | O(n) | insertion |
| `LinkedList` | O(n) get | O(1) ends | O(n) | insertion |
| `HashSet` | O(1) contains | O(1) | — | none guaranteed |
| `TreeSet` | O(log n) | O(log n) | — | sorted |
| `HashMap` | O(1) get | O(1) | — | none guaranteed |
| `TreeMap` | O(log n) | O(log n) | — | sorted keys |

\*Amortized for array growth.

### 4.6 When arrays still win

Fixed-size primitive batches, tight memory, and JNI interop favor **`int[]`**. Convert to **`List`** at API boundaries when mutability and utilities matter.

## 5. Mechanism and Execution Flow

This chapter connects **observable behavior** (fail-fast, ordering changes) to **implementation counters and resize**.

### 5.1 Fail-fast iterator (`modCount`)

1. **`ArrayList`** (and many JDK collections) store **`modCount`** — incremented on structural change (add/remove that changes size, etc.).
2. When an **iterator** is created, it records **`expectedModCount`**.
3. Each iterator step checks **`modCount == expectedModCount`**; mismatch → **`ConcurrentModificationException`**.
4. **`iterator.remove()`** updates both structure and **`expectedModCount`** legally—enhanced `for` + `list.remove()` does not.

### 5.2 `CollectionsChoiceDemo` removal path

1. **`removeIf`** (or explicit iterator) applies predicate while coordinating **`modCount`** correctly.
2. Resulting list **`odds=[1, 3]`** shows elements surviving predicate—order preserved for `ArrayList`.

### 5.3 Hash table resize (Level 4 sketch)

When **`size > capacity × loadFactor`**:

1. Allocate **new bucket array** (typically 2×).
2. **Rehash** every entry—bucket indices change; **iteration order** of `HashSet`/`HashMap` can reorder.
3. Do not persist or rely on **hash iteration order** across resize or JVM versions for contracts.

### 5.4 Selection execution flow

1. Classify access: **indexed**, **contains-only**, **sorted range**, **FIFO**, **key lookup**.
2. Map pattern to default (**`ArrayList` / `HashSet` / `HashMap` / `ArrayDeque`**).
3. Profile before swapping—Ch.02–04 explain when defaults fail.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Fail-fast** | Iterator throws on detected concurrent structural modification |
| **modCount** | Change counter backing fail-fast behavior (OpenJDK) |
| **Structural modification** | Add/remove affecting size (implementation-specific for `set`) |
| **Access pattern** | How code reads/writes data (random, sequential, by key, FIFO) |
| **Defensive copy** | New collection so callers cannot mutate internals |

## 7. Syntax and Language Rules

```java
List<Integer> scores = new ArrayList<>(List.of(1, 2, 3, 4));
Map<String, Integer> stock = new HashMap<>();
stock.put("kb", 5);

// safe filter in place
scores.removeIf(n -> n % 2 == 0);

// expose read-only view
List<Integer> frozen = List.copyOf(scores);
```

## 8. Step-by-Step Examples

### 8.1 Minimal — match structure to operation

```java
// random access by index → ArrayList
List<Integer> scores = new ArrayList<>(List.of(1, 2, 3, 4));
int third = scores.get(2);   // 3

// key lookup → HashMap
Map<String, Integer> stock = new HashMap<>();
stock.put("kb", 5);
int kb = stock.getOrDefault("kb", 0);   // 5
```

### 8.2 Realistic — CollectionsChoiceDemo

Full source: [`CollectionsChoiceDemo.java`](../../examples/core-java/collections-choice-demo/CollectionsChoiceDemo.java).

```bash
javac CollectionsChoiceDemo.java && java CollectionsChoiceDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
index2=3
kb=5
odds=[1, 3]
```

**Important lines:**

- **`scores.get(2)`** — index 2 in `[1,2,3,4]` is **`3`** (`index2=3`).
- **`getOrDefault("kb", 0)`** — map lookup without null check dance.
- **`Iterator.remove`** — removes evens, leaving **`odds=[1, 3]`** without fail-fast exception.

### 8.3 Advanced — choosing for a microservice cache

```java
Map<String, CachedUser> byId = new HashMap<>(16_384);
Map<String, CachedUser> insertionOrder = new LinkedHashMap<>(16_384, 0.75f, true);
// accessOrder true → LRU-style when override removeEldestEntry
```

Measure heap and hit rate before switching to off-heap or Caffeine (external library, not core collections).

## 9. Real-World Use Cases

- **Order pipeline** — `ArrayList` DTO list + `HashMap` id index built once per request.
- **Permission cache** — `Set<String>` in `HashSet` for O(1) checks on each call.
- **Job queue** — `ArrayDeque` between producer thread and worker pool (later: blocking queues).
- **Public getters** — return `List.copyOf(internal)` so clients cannot mutate backing store.

## 10. Internal Behavior

**`ArrayList.removeIf`** may use **bulk filter** optimization shifting in one pass—still updates `modCount` once per logical change batch.

**`HashMap`** treeified buckets switch between linked list and red-black tree by size thresholds (Java 8+).

Label: specifics are OpenJDK `java.util` sources; other JDKs aim for compatible behavior.

## 11. Edge Cases and Limitations

- **Sublist structural changes** — parent and child share data; concurrent modification rules apply across both.
- **`Collections.synchronizedList`** — manual synchronization on **every** operation including iteration; iterate inside `synchronized (list)` block.
- **Primitive collections** — not in JDK; use arrays, streams, or libraries (Eclipse Collections, fastutil) when boxing cost matters at scale.
- **Huge maps on heap** — GC pause risk; consider partitioning or external store.

## 12. Common Mistakes

### Incorrect — pick by name not pattern

```java
Map<String, Data> m = new TreeMap<>();  // log n everywhere
// when no sorted iteration or range needed
```

### Corrected

```java
Map<String, Data> m = new HashMap<>();
```

### Incorrect — return internal mutable list

```java
public List<Item> items() { return backing; }
```

### Corrected

```java
public List<Item> items() { return List.copyOf(backing); }
```

## 13. Best Practices

- Document expected **size** and **access pattern** in type names or Javadoc when non-obvious.
- Prefer **`removeIf`** / streams for bulk transforms when clarity beats micro-optimization.
- Run **spot checks** on custom key `equals`/`hashCode` with two distinct `new` instances in unit tests.
- Profile before replacing `ArrayList`—allocation and GC often dominate, not `get` nanoseconds.

## 14. Design Trade-offs

| Tactic | Wins | Risks |
|--------|------|-------|
| Default hash types | Speed, simplicity | Wrong order assumptions |
| Tree variants | Sorted views | Higher cost, comparison contract |
| Immutable snapshots | Safe publication | Copy cost |
| Fail-fast iterators | Early bug detection | Not full concurrent safety |

## 15. Performance Considerations

Rehash pauses on large `HashMap` growth—set initial capacity when loading known counts from DB. `ArrayList` growth copies—pre-size collections built from JDBC `ResultSet` with row count hint when available.

## 16. Security Considerations

Returning live mutable collections from authenticated session objects lets callers escalate or corrupt state. Unmodifiable wrappers still allow **element mutation** if elements are mutable objects—immutability depth matters.

## 17. Java Version and Compatibility Notes

**`removeIf`** since Java 8. **`List.copyOf`** since 10. **Sequenced collections** (21) clarify first/last operations—prefer over manual `get(0)` when type is `SequencedCollection`.

## 18. Related Language Comparisons

C# `List<T>` default plus `Dictionary<K,V>` parallels `ArrayList`/`HashMap`. Choosing collection by complexity class is universal interview topic.

## 19. Revision and Mastery

- **Gaps:** removing during foreach; assuming fail-fast means thread-safe.
- **You should answer:** Why does `CollectionsChoiceDemo` use `Iterator.remove` instead of `list.remove` in a foreach?
- **Advanced:** `Spliterator` characteristics (`SIZED`, `ORDERED`) for stream performance.

## 20. Exercises

### Beginner

1. Predict `index2=` and `odds=` from `CollectionsChoiceDemo`.
2. Name default list/set/map for a new REST service.

### Intermediate

3. Rewrite even-removal using `removeIf`.
4. When is `CopyOnWriteArrayList` justified?

### Advanced

5. Sketch `modCount` change on `ArrayList.add` vs `set`.

## 21. Interview Questions

1. `ArrayList` vs `LinkedList` for 90% index reads?
2. How does `HashSet` ensure uniqueness?
3. What triggers `ConcurrentModificationException`?

## 22. Solutions

1. `3`; `[1, 3]`.
2. `ArrayList`, `HashSet`, `HashMap`.
3. One-liner `removeIf`.
4. Many readers, rare writes, snapshot iteration.
5. `add` increments if size changes; `set` replaces element at index—may not change modCount if size unchanged.

## 23. Chapter Summary

Choose collections by **access pattern**: **`ArrayList`** for indexed sequences, **`HashSet`** for uniqueness, **`HashMap`** for key lookup, **`ArrayDeque`** for queues. Respect **fail-fast** rules—use **`Iterator.remove`** or **`removeIf`** when filtering in place. **`CollectionsChoiceDemo`** ties random access, map lookup, and safe removal together.

## 24. Key Terminology

fail-fast · `modCount` · access pattern · defensive copy · `removeIf`

## 25. References and Further Reading

- [Java SE `Collections`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html)
- OpenJDK `ArrayList` / `HashMap` source (behavior reference)
- Module 12 Ch.02–04 — implementation detail chapters

## 26. Next Steps

- [Module 13 — Generics](../13-generics/README.md) — type-safe collection APIs
- Module 22+ — concurrent collections and blocking queues

## Review Checklist

**Mandatory Depth Review:** Deep-dive profile; iterator invalidation and hash resize at **Level 4** on critical paths.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 where demos exist)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **Concurrent collections internals** — JUC modules.

**Verification note:** `CollectionsChoiceDemo` (`index2=3`; `kb=5`; `odds=[1, 3]`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
