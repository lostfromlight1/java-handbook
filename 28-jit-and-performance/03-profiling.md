# Profiling

> **Status:** Completed  
> **Module:** 28 — JIT and Performance  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Distinguish **CPU**, **allocation**, and **wall-clock** profiling.
- Read **`ThreadMXBean`** CPU time support as a minimal probe.
- Map findings to **hot methods** and **GC** (Modules 27–26) without invented profiles.

## 2. Prerequisites

- Ch.01–02; Module **26** diagnostics.

## 3. Conceptual Explanation

**Profiling** samples or instruments execution to show where time/allocation goes. Tools:

- **JFR** / JDK Mission Control (events, not fabricated here).
- **`async-profiler`**, **VisualVM** (HotSpot ecosystem).
- **JMX** thread CPU time (coarse).

Optimize after measurement; beware **observer effect** and **coordinated omission** in benchmarks.

## 4. Step-by-Step Examples

Demo: [profiling-awareness-demo](../../examples/jvm/profiling-awareness-demo/).

**Verified output:**

```text
cpuTimeSupported=true
cpuTimeNanos=62500000
cpuTimeAfterSleep=62500000
```

Nanos values depend on JVM/thread accounting; sleep may not increase CPU time — illustrates CPU vs wall time distinction.

## 5. Best Practices

- Profile production-like load in staging before micro-optimizing.
- Combine allocation profiling with GC logs when heap pressure is suspected.

## 6. Chapter Summary

Start with JMX probes and JDK profilers; record real output; avoid made-up flame graphs.

## 7. Next Steps

Continue roadmap **Module 29** per `LEARNING-ROADMAP.md`.

## Review Checklist

- [x] Verified demo
