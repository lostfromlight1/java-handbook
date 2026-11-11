# JVM Reference

> **Status:** Completed  
> **Profile:** Technical / Deep-dive awareness reference (Levels 2–3)  
> **Purpose:** Compact map of JVM runtime concepts, HotSpot tooling, and collector choices—not a replacement for Modules 25–28.  
> **Last updated:** 2026-11-11  
> **Assumes:** Java 21 mental model; HotSpot details labeled explicitly

## How to read this page

The **Java Virtual Machine Specification (JVMS)** defines what any conforming JVM must provide. **HotSpot** (the JVM in Oracle JDK and most OpenJDK builds) implements those rules plus many practical extras (JIT tiers, specific collectors, `jcmd` diagnostics).

| Label in this appendix | Meaning |
|------------------------|---------|
| **JVMS** | Spec requirement or terminology—portable across conforming JVMs |
| **HotSpot** | Implementation detail of HotSpot; other JVMs may differ |
| **Engineering advice** | Common practice; not a language or JVM guarantee |

**Depth chapters:** [Module 25 — JMM](../25-java-memory-model/README.md), [Module 26 — JVM Internals](../26-jvm-internals/README.md), [Module 27 — GC](../27-garbage-collection/README.md), [Module 28 — JIT](../28-jit-and-performance/README.md), [Module 31 — Debugging](../31-debugging-and-troubleshooting/README.md).

## Big picture

```mermaid
flowchart LR
  src[Source .java] --> javac[javac]
  javac --> class[.class bytecode]
  class --> load[Class loading]
  load --> link[Linking]
  link --> init[Initialization]
  init --> exec[Execution interpreter / JIT]
  exec --> heap[Heap + GC]
```

**Mechanism (short):** `javac` produces bytecode. At runtime the JVM **loads** classes, **links** them (verify, prepare, resolve), **initializes** static state, then **executes** methods. HotSpot often starts interpreting and later compiles hot methods (JIT). The **heap** holds objects; a **garbage collector** reclaims unreachable ones.

## Runtime data areas (JVMS orientation)

The JVMS describes several runtime data areas. Exact layout and sizing are implementation-dependent.

| Area | JVMS role | HotSpot notes (awareness) |
|------|-----------|---------------------------|
| **Program counter (PC) register** | Per-thread address of current instruction | One logical PC per Java thread |
| **JVM stacks** | Per-thread frames for locals, operand stack, frame data | Stack size tunable (`-Xss`); deep recursion → `StackOverflowError` |
| **Heap** | Shared object storage | Sized with `-Xms` / `-Xmx`; collected by GC |
| **Method area** | Per-class metadata (runtime representation of classes) | HotSpot historically used Metaspace for class metadata |
| **Runtime constant pool** | Per-class symbolic references and constants | Resolved during linking / first use |
| **Native method stacks** | Support for `native` code | Distinct from Java stacks |

**Frames:** Each method invocation pushes a frame (locals + operand stack). Returning pops it. This is why unbounded recursion exhausts the stack rather than the heap.

**Depth:** [26-jvm-internals/04-runtime-data-areas.md](../26-jvm-internals/04-runtime-data-areas.md), [25-java-memory-model/01-stack-heap-and-references.md](../25-java-memory-model/01-stack-heap-and-references.md)

## Class loading overview (JVMS)

Loading is not “find a `.class` file” alone—it is a pipeline.

1. **Loading** — find binary representation, create `Class` structure  
2. **Linking** — verification, preparation (static field storage), optionally resolution of symbolic refs  
3. **Initialization** — execute class initialization (`<clinit>`), under JVMS locking rules  

**Delegation idea (classic class loaders):** A class loader usually asks its parent before defining a class itself. The bootstrap loader loads core JDK classes; application loaders load your code. Modules add readability constraints on top of this model.

| Failure | Typical meaning |
|---------|-----------------|
| `ClassNotFoundException` | Loader API could not find the class (checked) |
| `NoClassDefFoundError` | Class was present at compile time but failed/missing at runtime use |
| `ExceptionInInitializerError` | Static initializer threw |

**Depth:** [26-jvm-internals/02-class-loading-and-linking.md](../26-jvm-internals/02-class-loading-and-linking.md)

## Execution: interpreter and JIT (HotSpot)

**JVMS** requires that bytecode semantics are respected. **HotSpot** commonly:

- Interprets cold methods
- Compiles hot methods with C1/C2 (tiered compilation)
- May deoptimize if assumptions fail (for example uncommon traps)

Do not treat a particular JIT optimization as a portability guarantee. Measure with proper methodology (Module 28 / JMH) rather than micro-folklore.

**Depth:** [26-jvm-internals/03-bytecode-and-execution.md](../26-jvm-internals/03-bytecode-and-execution.md), [28-jit-and-performance/01-hotspot-and-tiered-compilation.md](../28-jit-and-performance/01-hotspot-and-tiered-compilation.md)

## Garbage collectors summary (HotSpot awareness)

**JVMS** requires that unreachable objects may be reclaimed; it does not mandate a specific collector algorithm. The table below is **HotSpot-oriented**.

| Collector | Character (simplified) | When people consider it |
|-----------|------------------------|-------------------------|
| **G1** (Garbage-First) | Default on recent HotSpot server-class configs; region-based; balances throughput and pause goals | General-purpose server default |
| **ZGC** | Low-pause oriented; generational ZGC available in modern JDK lines | Large heaps / tight latency goals (validate on your workload) |
| **Shenandoah** | Low-pause oriented; generational variant evolving on newer JDKs | Similar niche to ZGC; availability depends on build/vendor |
| Serial / Parallel | Simpler / throughput-oriented older options | Small heaps, batch jobs, containers with care |

**Teachable mental model:** GC finds **unreachable** objects from **GC roots** (stacks, statics, JNI, etc.). Generational collectors bet that most objects die young. Pause behavior is a trade-off against throughput and footprint—never invent pause numbers; measure.

**Depth:** [27-garbage-collection/](../27-garbage-collection/README.md) especially [02-collectors-g1-zgc-shenandoah.md](../27-garbage-collection/02-collectors-g1-zgc-shenandoah.md)

## Common HotSpot flags (cheat sheet)

Flags change across releases; treat this as a **starting map**, not a tuning bible. Prefer documenting the exact JDK version when you record results.

| Flag | Role |
|------|------|
| `-Xms`, `-Xmx` | Initial / max heap size |
| `-Xss` | Java thread stack size |
| `-XX:+UseG1GC` | Select G1 (often already default) |
| `-XX:+UseZGC` | Select ZGC (HotSpot) |
| `-XX:+UseShenandoahGC` | Select Shenandoah when available |
| `-XX:MaxGCPauseMillis=` | Pause *goal* hint for some collectors—not a hard SLA |
| `-Xlog:gc*` | Unified logging for GC (modern syntax) |
| `-XX:+HeapDumpOnOutOfMemoryError` | Write heap dump on OOME |
| `-XX:HeapDumpPath=` | Dump location |
| `-XX:+FlightRecorder` | Enable JFR (availability/licensing historically varied by distribution—check your JDK) |

**Ergonomics:** HotSpot chooses many defaults from machine class and flags. Blindly copying production flags from a blog without measuring is a common failure mode.

**Depth:** [27-garbage-collection/03-tuning-and-memory-leaks.md](../27-garbage-collection/03-tuning-and-memory-leaks.md)

## Diagnostics awareness (`jcmd`, JFR)

These tools are **HotSpot/OpenJDK serviceability** features. Always confirm they exist on your distribution.

### `jcmd` (orientation)

`jcmd <pid> help` lists commands for a live process. Common awareness targets:

| Command family | Use |
|----------------|-----|
| `VM.version` / `VM.flags` | Confirm runtime and effective flags |
| `GC.run` / GC heap info commands | Trigger or inspect GC-related state (use carefully) |
| `Thread.print` | Thread dump style output |
| `JFR.start` / `JFR.dump` / `JFR.stop` | Flight Recorder control when enabled |

Exact subcommand names can vary slightly by JDK version—use `help` on the machine you are debugging.

### Java Flight Recorder (JFR)

JFR records timed events (GC, allocations, method samples, I/O, …) with lower overhead than many naive profilers when configured sanely. Pair with JDK Mission Control or other viewers.

**Depth:** [26-jvm-internals/05-diagnostics-and-agents.md](../26-jvm-internals/05-diagnostics-and-agents.md), [31-debugging-and-troubleshooting/02-dumps-and-flight-recorder.md](../31-debugging-and-troubleshooting/02-dumps-and-flight-recorder.md)

## Memory model vs “JVM memory”

Do not confuse:

| Phrase | Usually means |
|--------|---------------|
| JVM runtime data areas | Stacks, heap, method area, … (this page / Module 26) |
| Java Memory Model (JMM) | Spec rules for visibility, ordering, `synchronized`, `volatile`, happens-before (Module 25) |

A correctly synchronized program has defined visibility under the JMM regardless of which HotSpot GC you picked. GC choice does not replace locking or immutable publication patterns.

## Related appendix pages

- [java-version-history.md](java-version-history.md)  
- [java-quick-reference.md](java-quick-reference.md)  
- [common-errors.md](common-errors.md) — OOME, stack overflow, classloading errors  

## References

1. [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/) (runtime data areas, linking, initialization)
2. [Java SE HotSpot Virtual Machine documentation](https://docs.oracle.com/en/java/javase/21/vm/) (implementation guides)
3. OpenJDK GC and serviceability docs for G1 / ZGC / Shenandoah / JFR on your JDK line
4. `java` tool spec: [JDK 21 `java` man page](https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html)
