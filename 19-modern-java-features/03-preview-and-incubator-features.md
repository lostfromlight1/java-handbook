# Preview and Incubator Features

> **Status:** Completed  
> **Module:** 19 — Modern Java Features  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (`--enable-preview --release 21`)

## 1. Learning Objectives

- Define **preview**, **incubator**, and **final** feature stages.
- Compile and run preview code with **`--enable-preview`** and matching **`--release`**.
- Label preview APIs in documentation and avoid production defaults on non-final features.

## 2. Prerequisites

- Ch.01–02; JDK install (Module 01).

## 3. Why This Concept Exists

OpenJDK ships experimental APIs for community feedback. Preview features may **change or be removed**; incubating modules may move packages. Without explicit labeling, teams build production code on unstable surfaces.

## 4. Conceptual Explanation

### 4.1 Lifecycle (simplified)

1. **Incubator** — often `jdk.incubator.*` modules; API unstable.
2. **Preview** — language/vm features requiring `--enable-preview`; may warn in compiler.
3. **Second preview** — refinement round.
4. **Final** — standard semantics; no preview flag.

### 4.2 Compiler and runtime flags

```text
javac --enable-preview --release 21 MyPreview.java
java --enable-preview MyPreview
```

**Release must match** the JDK feature used for preview. Mismatch causes “unrecognized option” or class version errors.

### 4.3 Structured Concurrency (example on Java 21)

`StructuredTaskScope` (JEP 453 preview in 21) groups forked tasks with **structured failure** propagation—if one fork fails, scope shuts down siblings. Status in 21: **preview**—verify current JEP before adoption.

### 4.4 Documentation obligation

In chapters and APIs, tag:

- **Introduced in:** version  
- **Status:** preview | incubator | final | deprecated | removed  

## 5. Terminology

| Term | Meaning |
|------|---------|
| Preview feature | Requires `--enable-preview`; not final semantics |
| Incubator module | Delivered in incubator module path; may graduate or change |
| `--release` | Cap language/API to a specific Java SE version |

## 6. Step-by-Step Examples

Demo: [preview-incubator-demo](../../examples/core-java/preview-incubator-demo/).

Compile and run:

```bash
javac --enable-preview --release 21 PreviewIncubatorDemo.java
java --enable-preview PreviewIncubatorDemo
```

**Verified output:**

```text
structuredConcurrency=scoped-result
compileFlags=--enable-preview --release 21
status=Structured Concurrency is preview in Java 21; check JEP for current phase
```

`structuredConcurrency=` shows a successful fork/join under preview APIs. Lines `compileFlags=` and `status=` document required flags and non-final status.

## 7. Real-World Use Cases

- Spikes and benchmarks on preview APIs—**isolated branches**, not `main`.
- Framework early adopters gate features behind JDK version + flag checks.

## 8. Internal Behavior

Preview features are implemented in HotSpot/javac but guarded by metadata; removing preview in a later JDK can **break source** that depended on preview syntax.

## 9. Edge Cases and Limitations

- **IDE support** may lag preview syntax.
- Mixing preview and non-preview libraries in one module can confuse JPMS.
- Some previews (e.g. deprecated previews) are removed entirely—no migration path.

## 10. Common Mistakes

### Incorrect

`java --enable-preview` without `javac --release` alignment, or passing `--release` to `java` (invalid).

### Corrected

Pair `javac --enable-preview --release 21` with `java --enable-preview` on the same major version.

## 11. Best Practices

- No preview features in **production LTS** baselines without explicit risk acceptance.
- Track JEP **delivery** section each release.
- Remove preview flags promptly when features go final.

## 12. Design Trade-offs

Preview access lets teams influence design but imposes **rework cost** if APIs change.

## 13. Security Considerations

Preview/incubator APIs receive less scrutiny; avoid handling untrusted input through experimental native/FFM previews until final.

## 14. Java Version and Compatibility Notes

On Java 21, Structured Concurrency is **preview**. Always re-check [JEP index](https://openjdk.org/jeps/0) when upgrading JDK.

## 15. Revision and Mastery

- Preview vs incubator difference?
- Why `--release` matters for preview compile?

## 16. Exercises

### Beginner

Compile the demo with and without preview; capture the compiler error message.

### Intermediate

Find one preview feature in current JDK release notes and record its JEP status.

## 17. Chapter Summary

Label non-final features clearly; use **`--enable-preview`** and **`--release`** together. Treat preview code as **experimental**, not handbook default path.

## 18. Next Steps

Module 20 — standard library survey.

## Review Checklist

- [x] Preview labeled for Structured Concurrency  
- [x] Verified preview compile/run output
