# JVM Reference

> **Status:** Completed  
> **Profile:** Technical / Deep-dive awareness reference (Levels 2–3)  
> **Purpose:** Compact map of JVM runtime concepts, HotSpot tooling, and collector choices—not a replacement for Modules 25–28.  
> **Last updated:** 2026-11-11  
> **Assumes:** Java 21 mental model; HotSpot details labeled explicitly  
**Companion depth:** Modules 25–28 and 31; interview vocabulary in [interview-glossary.md](interview-glossary.md)

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

**Cross-check:** “Out of memory” in ops speech might mean Java heap, Metaspace, thread stacks, or native buffers—name which area before changing flags.

Prefer evidence (`jcmd`, GC logs, RSS) over a single `-Xmx` bump.

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

| Phase (**HotSpot**) | Role (simplified) |
|---------------------|-------------------|
| Interpreter | Correct, flexible execution while gathering profiles |
| C1 (client compiler) | Faster compile, lighter optimizations—good for startup / shallow methods |
| C2 (server compiler) | Heavier optimizations for hot methods—peak throughput focus |
| Deoptimization | Bail back when speculative assumptions fail |

Do not treat a particular JIT optimization as a portability guarantee. Measure with proper methodology (Module 28 / JMH) rather than micro-folklore.

**Depth:** [26-jvm-internals/03-bytecode-and-execution.md](../26-jvm-internals/03-bytecode-and-execution.md), [28-jit-and-performance/01-hotspot-and-tiered-compilation.md](../28-jit-and-performance/01-hotspot-and-tiered-compilation.md)

## Garbage collectors summary (HotSpot awareness)

**JVMS** requires that unreachable objects may be reclaimed; it does not mandate a specific collector algorithm. The table below is **HotSpot-oriented**.

| Collector | Heap model (simplified) | Pause / throughput character | Typical consideration | Caveats (**HotSpot**) |
|-----------|-------------------------|------------------------------|------------------------|------------------------|
| **G1** (Garbage-First) | Region-based; generational | Balances throughput and pause *goals* | Default on many recent server-class HotSpot configs | `MaxGCPauseMillis` is a goal, not an SLA |
| **ZGC** | Colored pointers / load barriers; generational ZGC on modern JDK lines | Low-pause oriented | Large heaps or tight latency budgets | Validate on *your* workload and JDK build |
| **Shenandoah** | Concurrent compaction family; generational variants evolving | Low-pause oriented | Similar niche to ZGC | Availability depends on vendor/build |
| **Parallel** | Generational; throughput-oriented stop-the-world collections | High throughput, longer pauses | Batch / offline jobs | Poor fit for latency-sensitive APIs |
| **Serial** | Single-threaded collections | Simple; pauses scale with heap | Tiny heaps, constrained containers, some embedded | Do not use blindly on multi-GB heaps |
| **Epsilon** | No-op collector (experimental / special-purpose) | No GC reclamation | Short-lived jobs / measurement harnesses | Process dies when heap fills—**not** for servers |

**Teachable mental model:** GC finds **unreachable** objects from **GC roots** (stacks, statics, JNI, etc.). Generational collectors bet that most objects die young. Pause behavior is a trade-off against throughput and footprint—never invent pause numbers; measure.

**Engineering advice:** Change one GC-related variable at a time; keep heap sizing, logging, and application allocation behavior in the same experiment notes.

**Depth:** [27-garbage-collection/](../27-garbage-collection/README.md) especially [02-collectors-g1-zgc-shenandoah.md](../27-garbage-collection/02-collectors-g1-zgc-shenandoah.md)

## Common HotSpot flags (cheat sheet)

Flags change across releases; treat this as a **starting map**, not a tuning bible. Prefer documenting the exact JDK version when you record results. **Warning:** many `-XX` options are diagnostic or historically unstable—prefer supported flags and measure.

| Flag | Role | Warning |
|------|------|---------|
| `-Xms`, `-Xmx` | Initial / max heap size | Setting them far apart can cause resize churn; containers need care with ergonomics |
| `-Xss` | Java thread stack size | Too large × many threads wastes native memory; too small → `StackOverflowError` |
| `-XX:+UseG1GC` | Select G1 (often already default) | Redundant on defaults; still useful to be explicit in docs |
| `-XX:+UseZGC` | Select ZGC (**HotSpot**) | Confirm vendor support; re-benchmark after major JDK upgrades |
| `-XX:+UseShenandoahGC` | Select Shenandoah when available | Not on every build—verify before production flags |
| `-XX:MaxGCPauseMillis=` | Pause *goal* hint for some collectors | **Not** a hard SLA; lying goals cause thrashing folklore |
| `-XX:NewRatio=` / young sizing knobs | Influence generational sizing | Easy to overfit; prefer observing occupancy first |
| `-Xlog:gc*` | Unified logging for GC (modern syntax) | Verbose logs cost I/O; scope tags (`gc`, `gc+heap`) intentionally |
| `-XX:+HeapDumpOnOutOfMemoryError` | Write heap dump on OOME | Dumps are large; secure the path; disk full → secondary failure |
| `-XX:HeapDumpPath=` | Dump location | Directory must be writable by the process user |
| `-XX:+FlightRecorder` | Enable JFR | Confirm enabled on your distribution; control overhead via settings |
| `-XX:StartFlightRecording=` | Start recording at launch | Keep duration/size bounded in production recipes |
| `-XX:+UnlockDiagnosticVMOptions` | Unlocks diagnostic `-XX` | **Do not** casually unlock experimental knobs in prod without review |
| `-XX:+PrintFlagsFinal` | Dump effective flags at startup | Noisy; prefer `jcmd VM.flags` on a running process when possible |

**Ergonomics:** HotSpot chooses many defaults from machine class and flags. Blindly copying production flags from a blog without measuring is a common failure mode.

**Depth:** [27-garbage-collection/03-tuning-and-memory-leaks.md](../27-garbage-collection/03-tuning-and-memory-leaks.md)

## Diagnostics awareness (`jcmd`, JFR)

These tools are **HotSpot/OpenJDK serviceability** features. Always confirm they exist on your distribution.

### `jcmd` (orientation)

`jcmd <pid> help` lists commands for a live process. Exact subcommand names can vary slightly by JDK version—use `help` on the machine you are debugging.

### `jcmd` recipe list (HotSpot)

Run against a live PID you are authorized to diagnose. Prefer read-only inspection before anything that triggers GC or dumps.

| Recipe | Example shape | When |
|--------|---------------|------|
| List commands | `jcmd <pid> help` | First contact with an unfamiliar JDK build |
| Runtime identity | `jcmd <pid> VM.version` | Confirm vendor/version before citing flags |
| Effective flags | `jcmd <pid> VM.flags` | Diff assumed vs actual ergonomics |
| System properties | `jcmd <pid> VM.system_properties` | Config / classpath clues |
| Thread dump | `jcmd <pid> Thread.print` | Deadlock / stuck request suspicion |
| Class histogram | `jcmd <pid> GC.class_histogram` | Quick retained-type sketch (can be costly) |
| Heap dump | `jcmd <pid> GC.heap_dump /path/heap.hprof` | Leak analysis offline (**large files**; secure path) |
| Trigger GC | `jcmd <pid> GC.run` | Rare; can distort latency—avoid as a “fix” |
| JFR start | `jcmd <pid> JFR.start name=app settings=profile` | Timed production sampling when enabled |
| JFR dump | `jcmd <pid> JFR.dump name=app filename=/path/app.jfr` | Pull a recording without stopping the JVM |
| JFR stop | `jcmd <pid> JFR.stop name=app` | End a named recording cleanly |

**Engineering advice:** Script the exact JDK version beside every dump/recording you keep; flag names and defaults drift across releases.

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

## Metaspace and native memory (HotSpot awareness)

**JVMS** talks about a method area for class metadata. **HotSpot** stores much of that in **Metaspace** (off-heap native memory), distinct from the Java heap sized by `-Xmx`.

| Symptom | Often investigate |
|---------|-------------------|
| `OutOfMemoryError: Java heap space` | Heap retained objects / leaks / undersized `-Xmx` |
| `OutOfMemoryError: Metaspace` | Classloader leaks, excessive dynamic class generation |
| Native OOM / process RSS growth | Direct buffers, JNI, thread stacks, metaspace, allocator fragmentation |

**Engineering advice:** Pair heap dumps with process RSS and metaspace metrics; fixing only `-Xmx` when Metaspace is the limit wastes time.

## Container and ergonomics notes (HotSpot)

HotSpot reads cgroup/memory limits on modern JDKs so heap ergonomics respect container budgets better than ancient releases. Still verify:

- Explicit `-Xmx` vs percentage-based options on your JDK line
- CPU count visibility for thread-pool defaults
- Whether sidecars share the same cgroup

Treat container sizing as an experiment: record JDK version, limit, observed heap, and GC logs together.

## What this page deliberately omits

Full collector algorithms, every `-XX` diagnostic switch, and vendor-specific forks. Those belong in Modules 26–28 and vendor docs. This appendix is a **map**: labels, comparison tables, safe first commands, and pointers—not a production runbook.

Also omitted: non-HotSpot JVMs (except as a reminder that they exist), commercial APM products, and OS-level tracing beyond what `jcmd`/JFR typically surface.

## Quick operational checklist

1. Confirm **JVMS vs HotSpot** before quoting a flag or collector as “Java law.”
2. Capture `VM.version` + `VM.flags` before changing production knobs.
3. Enable bounded GC logging (`-Xlog:gc*`) when investigating pauses.
4. Prefer JFR/`jcmd` over guesswork; keep dump paths secure and disk-aware.
5. Re-validate after major JDK upgrades—defaults and collectors move.
6. Separate **heap** OOME from **Metaspace** / native pressure before resizing blindly.
7. Treat pause *goals* (`MaxGCPauseMillis`) as hints—validate with recordings, not blog numbers.

### Minimal incident breadcrumb (HotSpot)

When a service misbehaves, a useful first bundle is often: `VM.version`, `VM.flags`, one `Thread.print`, GC log slice, and a short JFR dump (if enabled). That set answers “which JVM / which flags / stuck threads / GC shape” without immediately taking a multi-gigabyte heap dump.

Take a full heap dump only when retention analysis is required and disk/security allow it—dumps are sensitive (secrets, PII) and expensive.

## Related appendix pages

- [java-version-history.md](java-version-history.md)  
- [java-quick-reference.md](java-quick-reference.md)  
- [common-errors.md](common-errors.md) — OOME, stack overflow, classloading errors  
- [interview-glossary.md](interview-glossary.md) — HB, GC roots, JIT vocabulary  

## References

1. [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/) (runtime data areas, linking, initialization)
2. [Java SE HotSpot Virtual Machine documentation](https://docs.oracle.com/en/java/javase/21/vm/) (implementation guides)
3. OpenJDK GC and serviceability docs for G1 / ZGC / Shenandoah / JFR on your JDK line
4. `java` tool spec: [JDK 21 `java` man page](https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html)
5. JDK Flight Recorder and `jcmd` help output on your installed JDK 21 build
