# Diagnostics and Agents

> **Status:** Completed  
> **Module:** 26 — JVM Internals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Use **`java.lang.management`** beans for threads and memory.
- Know **`jcmd`**, **JFR**, and agents exist; avoid fabricating their output.
- Capture **real** MXBean readings in small programs first.

## 2. Prerequisites

- Ch.01–04; Module **24** thread counts.

## 3. Conceptual Explanation

**JMX** exposes JVM metrics (`ThreadMXBean`, `MemoryMXBean`, `GarbageCollectorMXBean`). **JDK tools:**

- **`jcmd <pid> help`** — diagnostic commands (HotSpot).
- **JFR** — flight recordings (enable and analyze with JDK Mission Control; not simulated here).
- **Java agents** — `premain`/`agentmain` transform classes (JVMS instrumentation).

This handbook records **small MXBean demos**; full JFR dumps are left to hands-on labs with your JDK.

## 4. Step-by-Step Examples

Demo: [diagnostics-demo](../../examples/jvm/diagnostics-demo/).

**Verified output:**

```text
threadCount=7
peakThreadCount=7
threadsListed=7
```

Counts include JVM housekeeping threads; numbers vary slightly by JDK build.

## 5. Best Practices

- Thread dumps (`jcmd Thread.print`) when investigating deadlocks — correlate with Module 24 locks.
- Label whether a metric is **MXBean API** vs **HotSpot-only** command.

## 6. Chapter Summary

Start diagnostics with MXBeans and `jcmd`; escalate to JFR/profilers with recorded real artifacts, not invented logs.

## 7. Next Steps

Module **27** Garbage Collection.

## Review Checklist

- [x] Verified demo
- [x] No fabricated JFR/GC log excerpts
