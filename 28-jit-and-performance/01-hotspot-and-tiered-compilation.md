# HotSpot and Tiered Compilation

> **Status:** Completed  
> **Module:** 28 — JIT and Performance  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Explain **interpreter → C1 → C2** tiered compilation (HotSpot product behavior).
- Understand **warmup** and why first iterations are slow.
- Run a tiny hot loop and record **wall-clock** honestly.

## 2. Prerequisites

- Module **26** bytecode execution; Module **00** Big-O mindset.

## 3. Conceptual Explanation

**HotSpot** (implementation) starts in the **interpreter**, profiles hot methods, then **JIT-compiles** them. **Tiered compilation** mixes quick (C1) and aggressive (C2) optimizations before deoptimization if assumptions fail.

**JVMS** does not require JIT — only bytecode semantics.

## 4. Step-by-Step Examples

Demo: [tiered-compilation-demo](../../examples/jvm/tiered-compilation-demo/).

**Verified output (this machine, one run):**

```text
result=124750
elapsedMs=6
note=Wall-clock time varies by machine; not a benchmark
```

`elapsedMs` is **not** portable; repeat on your hardware if you compare changes.

Optional diagnostic (not required): `-XX:+PrintCompilation` logs compilations (verbose; HotSpot only).

## 5. Edge Cases

- **Cold start** skews micro-timing.
- **OSR** (on-stack replacement) compiles loops inside running methods — HotSpot detail.

## 6. Chapter Summary

JIT makes hot code fast after warmup; never quote single-run `elapsedMs` as universal truth.

## 7. Next Steps

[02-jmh-and-benchmarking.md](02-jmh-and-benchmarking.md)

## Review Checklist

- [x] Verified demo with variability note
