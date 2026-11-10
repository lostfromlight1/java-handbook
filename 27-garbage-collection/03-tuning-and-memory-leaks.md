# Tuning and Memory Leaks

> **Status:** Completed  
> **Module:** 27 — Garbage Collection  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Describe **memory leak** as unintended **reachability** (objects kept alive).
- Set **`-Xmx` / `-Xms`** intentionally; tune only with evidence.
- Recognize **static collections** and **thread locals** as leak sources.

## 2. Prerequisites

- Ch.01–02; Module **25** references.

## 3. Conceptual Explanation

Java cannot leak memory like `malloc` without `free` if objects become unreachable — but you can **leak references** (e.g. static `List` growing forever). GC cannot collect objects still reachable.

Tuning knobs (HotSpot examples, not exhaustive):

- **Heap size** `-Xmx`, `-Xms`
- **GC logs** `-Xlog:gc*`
- **Region sizing** for G1 (`-XX:MaxGCPauseMillis` is a goal, not a guarantee)

## 4. Step-by-Step Examples

Demo: [memory-leak-pattern-demo](../../examples/jvm/memory-leak-pattern-demo/).

**Verified output:**

```text
leakListBefore=0
leakListAfter=1
reachable=true
```

Static `LEAK` keeps each `byte[]` reachable — pattern for “accidental retention,” not an OOME demo.

## 5. Best Practices

- Use heap dumps (`jcmd GC.heap_dump`) when leaks suspected — analyze with JDK tools.
- Fix retention (clear maps, weak references where appropriate) before exotic GC flags.

## 6. Chapter Summary

Leaks are reachable objects; tuning follows measurement, not folklore pause claims.

## 7. Next Steps

Module **28** JIT and performance.

## Review Checklist

- [x] Verified demo
