# Stack, Heap, and References

> **Status:** Completed  
> **Module:** 25 — Java Memory Model  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Map **locals** and **references** to thread stacks vs **objects** on the heap (JVMS model).
- Use **`==`** vs **`equals`** for references and content.
- Read **`identityHashCode`** as diagnostic only (HotSpot), not a contract.

## 2. Prerequisites

- Module **02** references; Module **04** stack frames; Module **24** shared heap.

## 3. Why This Concept Exists

The **Java Memory Model** (JLS Chapter 17) defines **which writes are visible** to other threads. Before happens-before rules, you need a picture: each thread has stacks; objects live on the heap and are reachable via references.

## 4. Conceptual Explanation

- **JVMS:** Each thread has a JVM stack of frames; object instances live in the heap; arrays are objects.
- **Primitives** in locals live in stack slots (conceptually); **references** are values pointing at heap objects.
- **`new`** allocates on the heap (JVMS allocation; exact allocator is HotSpot detail).

`==` compares references (same object or both `null`). `equals` compares logical value when properly overridden.

## 5. Step-by-Step Examples

Demo: [stack-heap-references-demo](../../examples/jvm/stack-heap-references-demo/).

**Verified output (identity hash varies by run/JVM):**

```text
primitive=7
identityHash=951007336
sameRef=false
equalContent=true
```

Two `new String(...)` with same text are different objects (`sameRef=false`) but `equals` true.

## 6. Real-World Use Cases

- Reasoning about leaks (static refs keep heap reachable — Module 27).
- Debugging shared caches where references escape across threads.

## 7. Internal Behavior

- **JVMS:** Runtime data areas — stack, heap, method area, PC register per thread.
- **HotSpot:** On-stack replacement, TLAB allocation — implementation, not language guarantees.

## 8. Edge Cases and Limitations

- `identityHashCode` can change for some objects when contended; not a stable id.
- String literals may be interned — do not rely on `==` for string content.

## 9. Chapter Summary

Stacks hold frames and references; heap holds objects; JMM rules govern cross-thread visibility next.

## 10. Next Steps

[02-happens-before-and-visibility.md](02-happens-before-and-visibility.md)

## Review Checklist

- [x] Verified demo
