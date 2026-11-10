# Happens-Before and Visibility

> **Status:** Completed  
> **Module:** 25 — Java Memory Model  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- State the **happens-before** relation from **JLS §17.4** (not informal folklore).
- Use **`volatile`**, **`synchronized`**, and **`Thread.join`** as established HB edges.
- Avoid claiming “usually works” for unsynchronized shared fields.

## 2. Prerequisites

- Module **24** Ch.02; this chapter cites **JLS** rules, not blogs.

## 3. Why This Concept Exists

Without ordering rules, CPUs and JIT may reorder or cache writes so another thread never sees your updates. **Happens-before** defines when a read is **consistent** with a prior write across threads.

## 4. Conceptual Explanation (JLS)

If action **a** happens-before **b**, then **a**’s effects are visible to **b** (JLS memory model). Key sources (non-exhaustive):

- Monitor unlock on **m** happens-before subsequent lock on **m**.
- **Volatile** write happens-before volatile read of that variable.
- **`Thread.start()`** happens-before any action in the started thread.
- **`Thread.join()`** completion happens-before `join` return in the joining thread.
- **`ExecutorService`** task submission happens-before task execution (per `java.util.concurrent` spec).

**Not happens-before:** two unsynchronized writes/reads on ordinary fields — data race; no well-defined behavior for those accesses (JLS).

Do **not** cite “cache flush to main memory” as the spec — that is an implementation intuition; the contract is happens-before.

## 5. Step-by-Step Examples

Demo: [happens-before-demo](../../examples/jvm/happens-before-demo/).

**Verified output:**

```text
payload=42
publishDone=true
```

Writer sets `payload` then `ready = true` on **`volatile`**. Reader spins until `ready`; HB from volatile write to read ensures `payload=42`.

Removing `volatile` from `ready` can yield `payload=0` — data race; demo is intentionally correct.

## 6. Real-World Use Cases

- One-shot initialization flags, publish/subscribe handoff between threads.
- Pairs with `ConcurrentHashMap`/`Atomics` where API documents ordering.

## 7. Edge Cases and Limitations

- **Double-checked locking** requires `volatile` on the shared reference (JLS pattern).
- **Incorrect** “every field is flushed on every synchronized” intuition — spec is HB edges only.

## 8. Common Mistakes

### Incorrect

“`boolean` reads are always safe without sync.”

### Corrected

Plain `boolean` fields racy with other unsynchronized writes have undefined behavior for those races.

## 9. Chapter Summary

Correctness rests on **documented happens-before** edges; `volatile` and locks are your portable tools.

## 10. Next Steps

[03-safe-publication.md](03-safe-publication.md)

## Review Checklist

- [x] Verified demo
- [x] JLS happens-before; no folk theorems
