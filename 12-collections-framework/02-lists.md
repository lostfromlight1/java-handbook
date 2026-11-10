# Lists

> **Status:** Completed  
> **Module:** 12 — Collections Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Use **`List`** operations: **`get`**, **`set`**, **`add`**, **`add(index, e)`**, **`remove`**, **`indexOf`**.
- Describe **`ArrayList`** internal **growable array** layout and **amortized** append cost.
- Describe **`LinkedList`** as **doubly-linked nodes** and when head/tail operations shine.
- Contrast **random access** vs **middle insert/remove** complexity between the two structures.
- Choose **`ArrayList`** as the default list and **`LinkedList`** when deque behavior dominates.

## 2. Prerequisites

- **Module 11 Ch.01** — fixed arrays, `length`, O(1) index read.
- **Module 12 Ch.01** — `Collection` / `List` hierarchy.
- **Module 06 Ch.04** — `equals` for `contains` and `indexOf`.

Brief refresher: an **array** is a fixed-length contiguous block. **`ArrayList`** hides resizing by allocating a larger backing array and copying when capacity is exceeded—conceptually “array with growth policy.”

## 3. Why This Concept Exists

Many algorithms need **positional** sequences: play queue order, spreadsheet rows, undo stacks, protocol frames in order. **`List`** guarantees **iteration order** matches insertion order (unless you sort) and supports **index-based** access.

Two classic implementations trade memory layout for time complexity:

- **`ArrayList`** — cache-friendly contiguous storage; excellent **tail growth** and **`get(i)`**.
- **`LinkedList`** — nodes linked by references; excellent **head/tail** deque ops without shifting elements.

## 4. Conceptual Explanation

### 4.1 List contract

A **`List<E>`** is an **ordered** collection (not necessarily “sorted”—sorted means comparator order, Ch.03 `TreeSet`). Duplicates are allowed. Core indexed API:

```java
E get(int index);
E set(int index, E element);
void add(int index, E element);
E remove(int index);
```

Indices are **0-based** like arrays; out-of-range indices throw **`IndexOutOfBoundsException`**.

### 4.2 ArrayList — structure

OpenJDK **`ArrayList`** stores elements in an **`Object[] elementData`** (for reference types) plus **`size`** (logical length ≤ capacity).

```
elementData:  [ A | B | C | _ | _ ]   capacity 5, size 3
index:          0   1   2
```

- **`add(e)`** at tail — if `size < capacity`, write at `size++`; else grow (typically ~1.5× capacity), copy, then append — **amortized O(1)**.
- **`get(i)` / `set(i)`** — direct array index — **O(1)**.
- **`add(i, e)`** in the **middle** — shift elements `i..size-1` right one slot — **O(n)**.
- **`remove(i)`** in the middle — shift left — **O(n)**.

**Why default choice:** Real services mostly append, scan, or random-read; they rarely insert at index 1 in a million-element list.

### 4.3 LinkedList — structure

OpenJDK **`LinkedList`** implements **`List`** and **`Deque`**. Each element sits in a **node**:

```
Node:  [ prev | item | next ]
```

The list holds references to **first** and **last** nodes (and **`size`**).

- **`addFirst` / `addLast` / `poll`** — rewire pointers — **O(1)**.
- **`get(i)`** — walk from nearer end — **O(n)** (no skip list).
- **`add(i, e)`** — must locate node at index — **O(n)** plus pointer updates.

**Memory cost:** Each element pays for two reference fields plus node object header—higher overhead than a plain array slot.

### 4.4 When LinkedList still makes sense

Use **`LinkedList`** (or often better, **`ArrayDeque`**) when the workload is **queue/deque** shaped: BFS fringe, undo buffers, work stealing from both ends. Implementing **`Deque`** with **`ArrayList`** is awkward (`remove(0)` shifts the whole array).

### 4.5 List views and sublists

**`subList(from, to)`** returns a **view** backed by the original list—structural changes through either reference affect the other. **`List.of`** returns immutable lists with no sublist mutation surprises.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Random access** | Fetch or update by index in constant time (`ArrayList`) |
| **Sequential access** | Cost grows with distance from ends (`LinkedList` `get`) |
| **Capacity** | Backing array length in `ArrayList` (≥ `size`) |
| **Amortized cost** | Average cost over many operations after occasional expensive resize |
| **Deque** | Double-ended queue: add/remove first and last (`Deque` interface) |
| **Fail-fast** | Iterator detects concurrent structural modification (Ch.05) |

## 6. Syntax and Language Rules

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

List<String> names = new ArrayList<>();
names.add("Ada");
names.add(1, "Grace");   // insert at index 1

LinkedList<String> deck = new LinkedList<>();
deck.addFirst("head");
deck.addLast("tail");
String x = deck.poll();      // remove head, null if empty
```

- Prefer **`ArrayList`** constructor with **expected size** when you know count: `new ArrayList<>(10_000)` avoids repeated growth.
- **`List.copyOf(collection)`** — immutable snapshot (Java 10+).
- Do not use **`Vector`** / **`Stack`** in new code except when maintaining legacy APIs.

## 7. Step-by-Step Examples

### 7.1 Minimal — index insert on ArrayList

```java
List<String> a = new ArrayList<>();
a.add("one");
a.add("two");
a.add(1, "1.5");
System.out.println(a);     // [one, 1.5, two]
System.out.println(a.get(1)); // 1.5
```

Insert at index **1** shifts `"two"` right—cheap for three elements, expensive at scale.

### 7.2 Realistic — ListsDemo

Full source: [`ListsDemo.java`](../../examples/core-java/lists-demo/ListsDemo.java).

```bash
javac ListsDemo.java && java ListsDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
arrayList=[one, 1.5, two]
get1=1.5
linked=[head, tail]
poll=head
linkedAfter=[tail]
```

**Important lines:**

- **`arrayList.add(1, "1.5")`** — middle insert; backed by array shift.
- **`get(1)`** — O(1) random access.
- **`linked.addFirst` / `addLast`** — pointer rewiring, not array shift.
- **`poll()`** — **`Deque`** removal from head; list becomes `[tail]`.

### 7.3 Advanced — algorithmic contrast table

| Operation | `ArrayList` | `LinkedList` |
|-----------|-------------|--------------|
| `get(i)` / `set(i)` | O(1) | O(n) |
| `add` at end | O(1) amortized | O(1) |
| `add` at index i | O(n) | O(n) locate + O(1) link |
| `remove(0)` | O(n) shift | O(1) |
| Memory per element | low (array slot) | higher (node overhead) |

For **frequent middle inserts**, neither structure is ideal—tree or gap buffers are specialized; most Java business code still uses **`ArrayList`** plus occasional **`ArrayList` rebuild** or **`LinkedList`** only when profiling proves need.

## 8. Real-World Use Cases

- **REST pagination** — `List<RowDto>` returned from repository; random access to page slice via index math on in-memory window.
- **Event sourcing replay** — ordered `List<Event>` append-only like tail `add`.
- **Task scheduler deque** — `LinkedList` or `ArrayDeque` for poll from head while producers add at tail.
- **Sorting pipeline** — `Collections.sort(list)` requires random access; **`ArrayList`** preferred.

## 9. Internal Behavior

**`ArrayList.grow`** allocates new array, **`Arrays.copyOf`**, assigns to `elementData`. Capacity trim is not automatic on remove—`trimToSize()` exists.

**`LinkedList`** does not expose nodes; all pointer surgery is encapsulated. Iterator **fail-fast** tracks **`modCount`** on both implementations.

HotSpot: bounds checks on array access; linked traversal prevents CPU cache locality compared to dense arrays.

## 10. Edge Cases and Limitations

- **Huge lists** — `ArrayList` max size bounded by array limits (`Integer.MAX_VALUE - 8` soft cap in OpenJDK).
- **`null` elements** — allowed once in `ArrayList`/`LinkedList`; `indexOf(null)` works.
- **Concurrent use** — neither is thread-safe; use `Collections.synchronizedList` or `CopyOnWriteArrayList` (concurrency modules).
- **Casting sublist** — sublist is not a full copy; clearing parent affects child.

## 11. Common Mistakes

### Incorrect — LinkedList for heavy `get(i)` in a loop

```java
LinkedList<Item> items = new LinkedList<>();
for (int i = 0; i < items.size(); i++) {
    process(items.get(i));   // O(n²) total
}
```

### Corrected

```java
ArrayList<Item> items = new ArrayList<>();
// same loop: O(n) total get cost
// or for (Item x : items) when index not needed
```

### Incorrect — `remove(0)` on large ArrayList in a loop

```java
while (!list.isEmpty()) {
    list.remove(0);   // shifts every time — O(n²)
}
```

### Corrected

```java
while (!list.isEmpty()) {
    list.remove(list.size() - 1);   // O(1) amortized
}
// or use Deque / Iterator.remove
```

## 12. Best Practices

- Default **`List`** implementation: **`ArrayList`**.
- Pre-size when building from known count: `new ArrayList<>(n)`.
- Use **enhanced for** or **stream** when index is unnecessary.
- For stacks/queues, prefer **`ArrayDeque`** over **`LinkedList`** unless you need **`List`** methods at both ends with linked semantics.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| `ArrayList` | Fast `get`, compact | Middle insert/remove shifts |
| `LinkedList` | Fast ends, implements `Deque` | Slow indexed access, higher per-element overhead |
| Immutable `List.of` | Safe shared constants | No mutation |
| Sort in place | No extra list | Comparator must see all elements |

## 14. Performance Considerations

Micro-benchmark folklore (“linked lists are always slower”) is misleading—measure **your** access pattern. JVM **JIT** optimizes tight `ArrayList` loops well because of sequential memory. Linked structures scatter nodes on the heap.

## 15. Security Considerations

Untrusted input that controls **index** must be bounds-checked before `get`. Do not return internal `ArrayList` from a singleton service without copying if clients can mutate.

## 16. Java Version and Compatibility Notes

**`ArrayList`** since 1.2; current growth strategy in OpenJDK 21 uses `grow` helper. **SequencedCollection** (Java 21) adds `getFirst`/`getLast` on lists—`ArrayList` participates.

## 17. Related Language Comparisons

Python `list` is a dynamic array like `ArrayList`. C++ `std::vector` similar; `std::list` doubly-linked like `LinkedList`. Rust `Vec` default resizable.

## 18. Revision and Mastery

- **Gaps:** assuming `List` implies `ArrayList`; using `LinkedList` without deque need.
- **You should answer:** Why is `get(500_000)` on `LinkedList` expensive?
- **Advanced:** `ArrayList` iterator `remove` vs `list.remove(index)` during traversal.

## 19. Exercises

### Beginner

1. Build `[1,2,3]`, insert `99` at index 1, print list and `get(1)`.
2. Predict `linkedAfter=` in `ListsDemo`.

### Intermediate

3. Time complexity of `contains` on unsorted `ArrayList`.
4. When would `new ArrayList<>(linkedList)` be useful?

### Advanced

5. Explain why sorting a `LinkedList` may internally copy to array (OpenJDK `List.sort`).

## 20. Interview Questions

1. `ArrayList` vs `LinkedList` for 1M random reads?
2. What happens to indices after `remove(2)`?
3. Is `LinkedList` thread-safe?

## 21. Solutions

1. `[1, 99, 2, 3]`, `99`.
2. `[tail]`.
3. O(n) linear scan with `equals`.
4. Snapshot into random-access list for algorithms needing indexes.
5. `List.sort` may use array copy for linked implementations to meet access needs of TimSort.

## 22. Chapter Summary

**`List`** gives **indexed, ordered** sequences. **`ArrayList`** backs a **growable array**—best default for append and random access. **`LinkedList`** is a **doubly-linked** structure—best when **deque** operations at ends dominate, not random `get(i)`. **`ListsDemo`** contrasts middle insert on array storage with head poll on a linked deque.

## 23. Key Terminology

`ArrayList` · `LinkedList` · random access · capacity · amortized · `Deque` · `poll`

## 24. References and Further Reading

- [Java SE `List`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html)
- [Java SE `ArrayList`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html)
- [Java SE `LinkedList`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/LinkedList.html)

## 25. Next Steps

- [03-sets-and-queues.md](03-sets-and-queues.md) — uniqueness and FIFO processing
- Module 13 — `List<String>` wildcards in APIs

## Review Checklist

- [x] Learning objectives covered
- [x] ArrayList vs LinkedList structure/trade-offs taught
- [x] `ListsDemo` verified Temurin 21.0.12.1
- [x] Revision and Mastery, exercises, references
