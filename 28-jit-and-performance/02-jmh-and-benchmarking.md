# JMH and Benchmarking

> **Status:** Completed  
> **Module:** 28 — JIT and Performance  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- State why ad-hoc `System.nanoTime` loops mislead (JIT, GC, layout).
- Outline **JMH** structure: `@Benchmark`, forks, warmup, blackholes.
- Run a **warm-up micro demo** without claiming benchmark scores.

## 2. Prerequisites

- Ch.01 tiered compilation.

## 3. Conceptual Explanation

**JMH** (Java Microbenchmark Harness) controls class loading isolation, forked JVMs, warmup iterations, and dead-code elimination (`Blackhole`).

Problems with hand-rolled loops:

- JIT optimizes away unused results.
- GC pauses dominate short timings.
- One JVM run ≠ distribution.

This module **does not require** running full JMH in CI — awareness + honest micro demo suffice.

## 4. Step-by-Step Examples

Demo: [jmh-awareness-demo](../../examples/jvm/jmh-awareness-demo/).

**Verified output:**

```text
warmedSample=1806
jmhRequired=for publication-grade benchmarks
```

`warmedSample=42*42+42=1806` — arithmetic check, not a performance score.

**Illustrative JMH skeleton (not executed in handbook build):**

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public void myMethod(MyState state) { /* ... */ }
```

Label any numbers from external tutorials as **unverified** unless you run JMH locally.

## 5. Chapter Summary

Use JMH for publishable microbenchmarks; our demo only illustrates warmup need.

## 6. Next Steps

[03-profiling.md](03-profiling.md)

## Review Checklist

- [x] Verified demo
- [x] No fake JMH numbers
