# Sets and Queues

> **Status:** Completed  
> **Module:** 12 — Collections Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain **`Set`** uniqueness in terms of **`equals`** (and **`hashCode`** for hash sets).
- Use **`HashSet`**, **`LinkedHashSet`**, and **`TreeSet`** and predict **iteration order**.
- Apply **`Queue`** operations **`offer`**, **`poll`**, **`peek`** on a **`Deque`** implementation.
- Prefer **`ArrayDeque`** over legacy **`Stack`** and over **`LinkedList`** for most queues.
- Choose a set implementation based on **order** and **complexity** needs.

## 2. Prerequisites

- **Module 12 Ch.01–02** — `Collection`, lists, iteration.
- **Module 06 Ch.04** — `equals`/`hashCode` contracts (sets and hash buckets).
- **Module 03** — loops; FIFO intuition (first in, first out).

Brief refresher: **`equals`** decides logical sameness; **`hashCode`** spreads objects into buckets so hash-based collections need not scan every element for `contains`.

## 3. Why This Concept Exists

### Sets — duplicate-free groups

Tag sets, permission names, visited graph nodes, unique visitor IDs—all need **at most one** representative of each value. A **`List`** allows duplicates; a **`Set`** enforces uniqueness on **`add`**.

### Queues — ordered handoff

Thread pools, print spoolers, message buses, and breadth-first search need **fair processing order**. **`Queue`** abstracts **producer → consumer** handoff without exposing internal list shifting details.

## 4. Conceptual Explanation

### 4.1 Set contract

**`Set<E>`** extends **`Collection`**. Critical behaviors:

- **`add(e)`** returns `false` if an **equal** element already exists.
- **`contains(o)`** uses **equality**, not identity (except `HashSet` handles `null` once).
- **No positional API** — you cannot `get(3)` on a set.

Uniqueness is defined by **`equals`**. If you put mutable objects in a set and mutate a field participating in `equals`, the set’s invariants can break—prefer **immutable** keys/elements.

### 4.2 HashSet — unordered, fast average

**`HashSet`** is backed by **`HashMap`** (key = element, dummy value). Buckets chosen by **`hashCode`**, collisions resolved by linked nodes or trees (Ch.04/05).

- **Iteration order** — undefined, can change when the table resizes.
- **`contains` / `add` / `remove`** — **O(1)** average.

### 4.3 LinkedHashSet — insertion order

**`LinkedHashSet`** extends hash table with a **doubly-linked list** through entries—iteration follows **insertion order** (or access order if constructed with `accessOrder` on linked hash map variant for map keys; set uses insertion).

Use when you need **uniqueness plus stable encounter order** (dedupe while preserving first appearance).

### 4.4 TreeSet — sorted order

**`TreeSet`** stores elements in a **red-black tree** ordered by **`Comparable`** natural order or a provided **`Comparator`**.

- Iteration is **sorted** ascending by that order.
- Operations **O(log n)**.
- Requires **consistent** comparison with `equals` for correct set semantics (document in `Comparator` or element type).

### 4.5 Queue and Deque

**`Queue<E>`** — typically **FIFO**:

| Method | Throws if empty? | Returns null if empty? |
|--------|------------------|-------------------------|
| add / offer | `add` throws | `offer` returns false on capacity issue (bounded queues) |
| remove / poll | `remove` throws | `poll` returns null |
| element / peek | `element` throws | `peek` returns null |

**`Deque`** adds **both ends** (`offerFirst`, `pollLast`, …). **`ArrayDeque`** uses a **circular array**—fast, no synchronization, not thread-safe.

**Avoid `java.util.Stack`** — extends `Vector`, legacy synchronization overhead; use **`ArrayDeque`** as a **`Deque`** instead.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Uniqueness** | At most one element equal to a given value |
| **Insertion order** | Iteration matches order elements were first added |
| **Natural ordering** | `Comparable.compareTo` defines sort for `TreeSet` |
| **FIFO** | First inserted at tail is first removed from head (typical queue) |
| **LIFO** | Stack discipline—use `Deque` push/pop at one end |
| **Bounded queue** | Fixed capacity; `offer` may fail when full |

## 6. Syntax and Language Rules

```java
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

Set<String> tags = new HashSet<>();
tags.add("java");
tags.add("java");   // ignored duplicate

Set<Integer> ordered = new TreeSet<>();
ordered.add(3);
ordered.add(1);     // iteration: 1, 3

Queue<String> jobs = new ArrayDeque<>();
jobs.offer("first");
jobs.offer("second");
```

## 7. Step-by-Step Examples

### 7.1 Minimal — duplicate rejection

```java
Set<String> s = new HashSet<>();
System.out.println(s.add("a"));  // true
System.out.println(s.add("a"));  // false
System.out.println(s.size());    // 1
```

### 7.2 Realistic — SetsQueuesDemo

Full source: [`SetsQueuesDemo.java`](../../examples/core-java/sets-queues-demo/SetsQueuesDemo.java).

```bash
javac SetsQueuesDemo.java && java SetsQueuesDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
hashSize=2
linkedOrder=[b, a]
treeOrder=[a, b]
peek=1
poll=1
queue=[2]
```

**Important lines:**

- Duplicate **`"b"`** on hash set → **`hashSize=2`**.
- **`LinkedHashSet`** prints **`[b, a]`** — insertion order, not sorted.
- **`TreeSet`** prints **`[a, b]`** — lexicographic sort on strings.
- **`peek`** inspects head without removal; **`poll`** removes **`"1"`**, leaving **`queue=[2]`**.

### 7.3 Advanced — TreeSet with custom order

```java
Set<String> byLength = new TreeSet<>(
    Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
```

Document that two unequal strings with same length compare by tie-breaker—avoid violating set contract with unstable comparison.

## 8. Real-World Use Cases

- **Authorization** — `Set<Role>` attached to user; membership checks O(1) with hash.
- **Deduping stream events** — `LinkedHashSet` preserves first-seen timeline for audit.
- **Scheduled tasks** — `PriorityQueue` for next-run time (not in demo; same `Queue` interface family).
- **BFS** — `ArrayDeque` as fringe queue of nodes.

## 9. Internal Behavior

**`HashSet`** delegates to **`HashMap`**; static **`PRESENT`** object as value.

**`TreeSet`** red-black tree balancing keeps operations logarithmic.

**`ArrayDeque`** grows circular buffer; **`null`** elements forbidden (unlike `ArrayList`).

Fail-fast iterators on hash structures can throw if map/set modified during iteration without iterator remove.

## 10. Edge Cases and Limitations

- **`TreeSet`** with elements neither `Comparable` nor given `Comparator` → **`ClassCastException`** at runtime.
- **Mutable elements** in **`HashSet`** — if `hashCode` changes after insert, element may become “lost.”
- **`PriorityQueue`** iterator order ≠ priority order; only `poll` respects heap.
- **Thread safety** — none on standard sets/queues; concurrent alternatives in `java.util.concurrent`.

## 11. Common Mistakes

### Incorrect — expecting HashSet iteration order to match insertion

```java
Set<Integer> s = new HashSet<>();
s.add(3); s.add(1); s.add(2);
// iteration order not guaranteed 3,1,2
```

### Corrected

```java
Set<Integer> s = new LinkedHashSet<>(List.of(3, 1, 2));
// or TreeSet for sorted order
```

### Incorrect — using Stack everywhere

```java
Stack<String> st = new Stack<>();
st.push("x");
```

### Corrected

```java
Deque<String> st = new ArrayDeque<>();
st.push("x");
```

## 12. Best Practices

- Default **unique** bag: **`HashSet`**.
- Need **stable insertion order** after dedupe: **`LinkedHashSet`**.
- Need **sorted** unique collection: **`TreeSet`** with well-defined comparison.
- Default **queue/deque**: **`ArrayDeque`** as `Queue` or `Deque`.

## 13. Design Trade-offs

| Implementation | Order on iterate | contains/add avg | Notes |
|----------------|------------------|------------------|-------|
| `HashSet` | None guaranteed | O(1) | Fastest general set |
| `LinkedHashSet` | Insertion | O(1) | Small link overhead |
| `TreeSet` | Sorted | O(log n) | Needs comparison discipline |
| `ArrayDeque` queue | FIFO at ends | O(1) | Not priority-aware |

## 14. Performance Considerations

`HashSet` load factor (default 0.75) triggers rehash—temporary pause. Pre-size constructor `new HashSet<>(expectedSize)` reduces rehashes when count known.

## 15. Security Considerations

**Hash flooding** (adversarial `hashCode`) was mitigated in Java 8+ with treeified buckets on long collision chains in `HashMap`—still prefer trusted key types in security-sensitive maps/sets.

## 16. Java Version and Compatibility Notes

**`Queue`** since 1.5, **`ArrayDeque`** since 1.6. **`SequencedSet`** (Java 21) adds first/last views on some set implementations—`LinkedHashSet` and `TreeSet` participate.

## 17. Related Language Comparisons

Python `set`/`dict` hash like `HashSet`/`HashMap`. Java separates **ordered tree** (`TreeSet`) from **hash** explicitly.

## 18. Revision and Mastery

- **Gaps:** confusing iteration order across set types; using `remove()` on empty queue without handling exception.
- **You should answer:** Why does `SetsQueuesDemo` show different orders for linked vs tree sets with same adds?
- **Advanced:** `EnumSet` bit-vector for enum constants—compact and fast.

## 19. Exercises

### Beginner

1. Add `"a","b","a"` to `HashSet`; print `size()`.
2. Predict `treeOrder=` in demo.

### Intermediate

3. Implement queue of ints using only `ArrayDeque` `offer`/`poll`.
4. Why must `equals` and `compareTo` agree for `TreeSet` of custom types?

### Advanced

5. When is `CopyOnWriteArraySet` appropriate?

## 20. Interview Questions

1. `HashSet` vs `TreeSet` for 1M membership checks?
2. Difference between `poll` and `remove` on `Queue`?
3. Can `HashSet` contain `null`?

## 21. Solutions

1. `2`.
2. `[a, b]`.
3. Offer in loop, poll until empty.
4. Set contract: unequal elements must not compare as 0.
5. Rare reads, frequent iteration snapshots in concurrent listeners.

## 22. Chapter Summary

**Sets** enforce **unique** elements via **`equals`**. **`HashSet`** is fast and unordered; **`LinkedHashSet`** keeps **insertion order**; **`TreeSet`** keeps **sorted order**. **Queues** process elements in **FIFO** order—implement with **`ArrayDeque`** using **`offer`**, **`peek`**, and **`poll`**. **`SetsQueuesDemo`** contrasts the three set orderings and basic queue behavior.

## 23. Key Terminology

`HashSet` · `LinkedHashSet` · `TreeSet` · `Queue` · `Deque` · `ArrayDeque` · FIFO

## 24. References and Further Reading

- [Java SE `Set`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Set.html)
- [Java SE `Queue`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Queue.html)
- [Java SE `ArrayDeque`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayDeque.html)

## 25. Next Steps

- [04-maps-and-hashing.md](04-maps-and-hashing.md) — keys, values, and hash tables
- Module 22 — `BlockingQueue` for producer–consumer threading

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; `SetsQueuesDemo` verified Temurin 21.0.12.1
- [x] Revision and Mastery, exercises, references
