# JVM Architecture

> **Status:** Completed  
> **Module:** 26 — JVM Internals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Name JVM subsystems at a high level: **class loader**, **runtime data areas**, **execution engine**, **GC**.
- Distinguish **JVMS specification** from **HotSpot** product behavior.
- Read basic **RuntimeMXBean** / **ProcessHandle** facts.

## 2. Prerequisites

- Module **01** JDK/JRE/JVM; Module **00** execution model.

## 3. Conceptual Explanation

**JVMS** describes a virtual machine that executes **class files**. A product JVM (e.g. **HotSpot** on Temurin) implements that spec plus GC, JIT, diagnostics.

Rough pipeline:

1. **Load** `.class` bytes → define `Class`.
2. **Link** (verify, prepare, resolve).
3. **Initialize** statics.
4. **Execute** bytecode (interpreter + **JIT** — Module 28).
5. **GC** reclaims unreachable heap objects (Module 27).

## 4. Step-by-Step Examples

Demo: [jvm-architecture-demo](../../examples/jvm/jvm-architecture-demo/).

**Verified output:**

```text
jvmName=OpenJDK 64-Bit Server VM
javaVersion=21
pid=5176
inputArgsCount=0
```

`pid` changes every run; other lines stable on Temurin 21.0.12.1.

## 5. Internal Behavior

- **JVMS:** Required data areas and bytecode semantics.
- **HotSpot:** G1 default on this JDK (`UseG1GC=true` ergonomic — verified locally with `-XX:+PrintFlagsFinal`).

## 6. Chapter Summary

Think specification vs implementation; architecture frames later chapters on class loading, bytecode, and diagnostics.

## 7. Next Steps

[02-class-loading-and-linking.md](02-class-loading-and-linking.md)

## Review Checklist

- [x] Verified demo
