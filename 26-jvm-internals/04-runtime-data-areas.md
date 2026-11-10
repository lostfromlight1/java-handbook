# Runtime Data Areas

> **Status:** Completed  
> **Module:** 26 — JVM Internals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- List JVMS **runtime data areas**: PC, stacks, heap, method area, native.
- Relate **recursive calls** to stack depth (until `StackOverflowError`).
- Place **objects** on the heap via references from stack frames.

## 2. Prerequisites

- Module **25** Ch.01; Module **04** recursion.

## 3. Conceptual Explanation

Each thread: **program counter**, **JVM stack** (frames), optional native method stack. All threads share **heap** and **method area** (metaspace for classes in HotSpot).

Deep recursion grows the stack; unbounded recursion exhausts it — `StackOverflowError` (not catchable as normal recovery for production logic).

## 4. Step-by-Step Examples

Demo: [runtime-data-areas-demo](../../examples/jvm/runtime-data-areas-demo/).

**Verified output:**

```text
stackDepth=100
heapObjectId=951007336
```

`callDepth(100)` returns depth without overflow on default stack; `identityHash` varies by JVM/run.

## 5. Chapter Summary

Stacks are per-thread and finite; heap holds shared objects — foundation for GC and JMM.

## 6. Next Steps

[05-diagnostics-and-agents.md](05-diagnostics-and-agents.md)

## Review Checklist

- [x] Verified demo
