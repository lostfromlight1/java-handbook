# Collectors: G1, ZGC, Shenandoah

> **Status:** Completed  
> **Module:** 27 — Garbage Collection  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Compare **G1**, **ZGC**, and **Shenandoah** at a **conceptual** level (OpenJDK collectors).
- Verify which collector is active on **your** JDK with flags, not assumptions.
- Choose collectors based on **latency vs throughput** goals and measured data.

## 2. Prerequisites

- Ch.01 GC fundamentals.

## 3. Conceptual Explanation

| Collector | JDK 21 status (OpenJDK family) | Typical positioning |
|-----------|-------------------------------|---------------------|
| **G1** | Default on many HotSpot builds | Balanced; regional heap; predictable pauses with tuning |
| **ZGC** | Production (`-XX:+UseZGC`) | Low pause targets on large heaps (measure) |
| **Shenandoah** | Available on some builds (`-XX:+UseShenandoahGC`) | Concurrent compaction focus; vendor/build dependent |

**JVMS** does not mandate a collector. **HotSpot** on Temurin 21 defaults to **G1** (`UseG1GC=true` ergonomic on this machine).

## 4. Step-by-Step Examples

Demo: [collectors-overview-demo](../../examples/jvm/collectors-overview-demo/).

**Verified output:**

```text
vmName=OpenJDK 64-Bit Server VM
vmVendor=Eclipse Adoptium
note=See chapter for verifying default collector flags on your JDK
```

**Flag check (verified Temurin 21.0.12.1):**

```text
bool UseG1GC = true {product} {ergonomic}
```

No fabricated pause tables — compare collectors with your workload and GC logging.

## 5. Real-World Use Cases

- G1: general JVM services on Java 21 LTS.
- ZGC/Shenandoah: latency-sensitive heaps when profiling shows benefit.

## 6. Chapter Summary

Know defaults via flags; treat collector marketing numbers as hypotheses until you measure.

## 7. Next Steps

[03-tuning-and-memory-leaks.md](03-tuning-and-memory-leaks.md)

## Review Checklist

- [x] Real `UseG1GC` flag line
- [x] No invented pause times
