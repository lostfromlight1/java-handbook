# GC Fundamentals

> **Status:** Completed  
> **Module:** 27 — Garbage Collection  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Define **reachability** vs manual `free` (JVMS automatic GC).
- Read **heap** totals via `Runtime` and **GC beans** via JMX.
- Avoid treating **`System.gc()`** as a production control knob.

## 2. Prerequisites

- Module **25** heap; Module **26** runtime areas.

## 3. Conceptual Explanation

**Garbage collection** reclaims objects not **reachable** from GC roots (JLS/JVMS model). Roots include stack references, static fields, JNI handles, etc. (HotSpot specifics in JVM docs).

Phases (collector-dependent, conceptual):

- **Mark** reachable objects.
- **Sweep/compact** or **copy** unreachable space.
- **Pause** some work during stop-the-world phases (duration is **measured**, not guessed here).

## 4. Step-by-Step Examples

Demo: [gc-fundamentals-demo](../../examples/jvm/gc-fundamentals-demo/).

**Verified output:**

```text
heapTotalBytes=268435456
heapFreeBytes=265876736
collector=G1 Young Generation
collector=G1 Concurrent GC
collector=G1 Old Generation
gcBeanCount=3
```

Heap byte counts reflect this JVM’s current sizing at run start — your numbers may differ with `-Xmx`. Collector names come from **real** `GarbageCollectorMXBean` on Temurin 21 G1.

## 5. Internal Behavior

- **JVMS:** GC is implementation-defined but memory safety required.
- **HotSpot G1:** Default on Java 21 Temurin (`UseG1GC=true` ergonomic — verified with `-XX:+PrintFlagsFinal`).

## 6. Common Mistakes

### Incorrect

Citing specific pause times (e.g. “always &lt; 10 ms”) without measurement on your workload.

### Corrected

Use GC logs (`-Xlog:gc*`), JFR GC events, or Micrometer for **your** app.

## 7. Chapter Summary

GC traces reachability; JMX exposes collector names; never invent pause benchmarks.

## 8. Next Steps

[02-collectors-g1-zgc-shenandoah.md](02-collectors-g1-zgc-shenandoah.md)

## Review Checklist

- [x] Verified demo
