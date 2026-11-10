# Method Handles, VarHandles, and FFM

> **Status:** Completed  
> **Module:** 50 — Advanced Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [method-handles-varhandles-demo](../../examples/advanced/method-handles-varhandles-demo/); [ffm-arena-demo](../../examples/advanced/ffm-arena-demo/)

## 1. Learning Objectives

- Invoke code via **MethodHandle** with types checked at lookup time.
- Update fields with **VarHandle** and specified memory ordering.
- Allocate off-heap memory with **Foreign Function & Memory (FFM)** APIs on JDK 21.
- Compare reflection, method handles, and JNI for performance and safety.

## 2. Prerequisites

- **Module 50 Ch.01** — reflection basics.
- **Module 25** — happens-before, `volatile` (VarHandle ordering).

## 3. Why This Concept Exists

`Method.invoke` is flexible but can be slow and loosely typed. **Method handles** are JVM-supported callable references with **signature polymorphism** (invokedynamic, lambda metafactory). **VarHandles** unify `volatile`, CAS, and array element access for low-level concurrent code. **FFM** replaces fragile JNI for many native interop cases with explicit **arena** lifetimes.

## 4. Conceptual Explanation

### MethodHandles.Lookup

| Lookup kind | Typical use |
|-------------|-------------|
| `findStatic` / `findVirtual` | Methods |
| `findConstructor` | Constructors |
| `findVarHandle` | Fields |

Lookup is **capability-based**: caller class limits which members are accessible (JPMS-aware).

### Demo: method handles + VarHandle

[method-handles-varhandles-demo](../../examples/advanced/method-handles-varhandles-demo/):

**Verified:** `fromHandle=Hello, Ada`; `counter=1`; `methodHandlesDemo=ok`

`setVolatile` / `getVolatile` provide volatile field semantics without `synchronized`.

### Foreign Function & Memory (JDK 21 status)

In **Java 21**, `java.lang.foreign` is a **preview** API (finalized in a later Java release). Handbook demos compile with:

```bash
javac --enable-preview --release 21 FfmArenaDemo.java
java --enable-preview FfmArenaDemo
```

[ffm-arena-demo](../../examples/advanced/ffm-arena-demo/) — **Verified:** `sum=63`; `ffmDemo=ok`

| Concept | Role |
|---------|------|
| **Arena** | Owns memory lifetime; `ofConfined` tied to thread |
| **MemorySegment** | Off-heap region with bounds checks |
| **ValueLayout** | Size/alignment of primitives in memory |

Calling **native functions** via `Linker.defaultLinker()` may require `--enable-native-access=...` on newer JDKs when restricted; JDK 21 off-heap-only sample runs with preview alone.

### Arenas and safety

**Confined** arena — single thread; freed on `close()`. **Shared** arena — coordinated access across threads with explicit synchronization. Use try-with-resources to avoid leaks.

## 5. Internal Behavior

**invokedynamic** bootstrap methods use method handles for lambdas (Module 16). VarHandles may compile to atomic CPU instructions.

FFM bounds checks on segments prevent many JNI buffer overruns; misuse of **shared** arena still risks use-after-free if lifetimes overlap.

## 6. Real-World Use Cases

- **Libraries** (Project Panama) wrapping OpenSSL, zlib via FFM instead of handwritten JNI.
- **Concurrent algorithms** using VarHandle on counters (alternative to `AtomicInteger` internals).
- **Bytecode generators** and compilers emitting invokedynamic.

## 7. Edge Cases

- **Preview warnings** — plan migration when upgrading JDK past 21.
- **Misaligned layouts** — `ValueLayout` alignment rules matter for native structs.
- **Final fields** — VarHandle rules differ; illegal writes throw.

## 8. Common Mistakes

### Incorrect — using FFM segment after arena closed

Use-after-free; may crash JVM.

### Corrected — keep arena open for entire native call scope.

### Incorrect — ignoring preview in CI

Build fails without `--enable-preview`.

### Corrected — document flags in README and pipeline.

## 9. Design Trade-offs

| Reflection | MethodHandle |
|------------|--------------|
| Easier discovery | Faster, stricter types |

| JNI | FFM |
|-----|-----|
| Mature ecosystem | Safer memory model, learning curve |

## 10. Revision and Mastery

- Why preview flags for FFM on JDK 21?
- VarHandle vs `synchronized` on a counter field?
- What does `Arena.ofConfined()` guarantee on close?

## 11. Exercises

1. Extend demo with `compareAndSet` on counter via VarHandle.
2. Allocate 1 KiB segment, write bytes, read back sum.
3. Read JEP summary for FFM standardization version.

## 12. Chapter Summary

Method handles and VarHandles are the low-level invocation and concurrency primitives beneath lambdas and atomics. FFM on JDK 21 provides scoped off-heap access with preview flags; native calls need extra access flags on newer releases.

## Next Steps

[03-bytecode-and-agents.md](03-bytecode-and-agents.md)

## Review Checklist

- [x] MH, VarHandle, FFM preview; verified outputs
