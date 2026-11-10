# Safe Publication

> **Status:** Completed  
> **Module:** 25 — Java Memory Model  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Deep-dive  
> **Depth target:** Levels 3–4 (final-field semantics, escape, publication idioms)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives *(mandatory)*

- Define **safe publication** so other threads see **fully constructed** state.
- Use **final fields**, **volatile references**, **confinement**, and class-init HB idioms.
- Spot **this-escape** from constructors and **unsafe publication** of mutable objects.

## 2. Prerequisites *(mandatory)*

- Ch.02 happens-before; Module **06** initialization.

## 3. Why This Concept Exists *(mandatory)*

Thread B reading reference **r** to a new `Holder` without HB from publication may observe **default field values** despite A’s constructor finishing (JMM allowed for racy publication of mutable objects).

## 4. Conceptual Explanation *(mandatory)*

**Safe publication:** when B reads the shared reference, construction effects are visible per JMM rules.

| Mechanism | Idea |
|-----------|------|
| **Final fields** | JLS final-field safe publication when ref escapes after ctor completes normally |
| **Volatile ref write** | Volatile write hb volatile read of reference |
| **Confinement** | Build on one thread; publish under lock / `join` / executor submit HB |
| **Static holder** | Class initialization HB |

**Immutable objects** still need safe **reference** publication.

## 5. Mechanism and Execution Flow *(mandatory)*

1. Constructor runs on thread A, writes `final` field.
2. Assigns `holder` reference (demo uses safe pattern after construction).
3. Thread B reads `holder` and fields—demo uses join/HB context implied by test harness.

Demo: [safe-publication-demo](../../examples/jvm/safe-publication-demo/).

**Verified output (Temurin JDK 21.0.12.1):**

```text
finalFieldValue=99
published=true
```

Production cross-thread ref often adds **`volatile`** on the holder field.

## 12. Common Mistakes *(mandatory)*

### Incorrect

`static Holder h;` written in one thread, read in another with no HB.

### Corrected

`volatile` ref, synchronized publish, executor submit HB, or confinement until join.

## 23. Chapter Summary *(mandatory)*

Sharing references requires **happens-before** between construction and other threads’ reads; **final**, **volatile**, and confinement are standard patterns.

## 26. Next Steps

Module **26** — JVM internals.

## Review Checklist

- [x] Deep-dive; Verified Temurin 21.0.12.1
- [x] JLS publication vs HotSpot barriers distinguished
