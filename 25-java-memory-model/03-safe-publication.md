# Safe Publication

> **Status:** Completed  
> **Module:** 25 — Java Memory Model  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- **Publish** shared objects so other threads see fully constructed state (JLS/HB).
- Use **final fields**, **volatile references**, or **confined** construction patterns.
- Spot **unsafe publication** of mutable partially initialized objects.

## 2. Prerequisites

- Ch.02 happens-before; Module **06** initialization.

## 3. Why This Concept Exists

If thread B sees reference **r** to a new `Holder` without a happens-before edge from construction, B might observe **default field values** even after A finished the constructor (JMM allowed behavior for racy publication).

## 4. Conceptual Explanation

**Safe publication** means: when another thread reads a reference, **all** construction writes are visible per the memory model.

Mechanisms:

1. **Final fields** — JLS final-field safe publication after construction completes (when reference escapes only after ctor ends normally).
2. **Volatile** write of the reference — HB to subsequent volatile read.
3. **Thread confinement** — object never shared until fully built and published under lock.
4. **Static holder idiom** with class initialization HB.

## 5. Step-by-Step Examples

Demo: [safe-publication-demo](../../examples/jvm/safe-publication-demo/).

**Verified output:**

```text
finalFieldValue=99
published=true
```

`holder` assigned after `Holder` construction; reader sees `final` field `99`. For stronger cross-thread guarantees on the reference itself, use `volatile` for `holder` in production patterns.

## 6. Real-World Use Cases

- Singleton service registries in JVM apps.
- Publishing immutable config snapshots after load.

## 7. Edge Cases and Limitations

- Allowing `this` to escape from a constructor before completion risks publishing incompletely initialized instances.
- Immutable objects still need safe **reference** publication.

## 8. Common Mistakes

### Incorrect

Global `static Holder holder;` written in one thread, read in another with no HB edge.

### Corrected

`volatile` reference, `synchronized` publish, or confine to `join`/`Executor` submission HB.

## 9. Chapter Summary

Sharing references requires happens-before between construction and other threads’ reads; **final**, **volatile**, and confinement are standard tools.

## 10. Next Steps

Module **26** JVM internals.

## Review Checklist

- [x] Verified demo
