# Reflection, Annotations, and Process APIs

> **Status:** Completed  
> **Module:** 20 — Java Standard Library  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Read **runtime annotations** and invoke methods via **reflection** (`java.lang.reflect`).
- Declare simple **annotations** with retention and targets.
- Start external processes with **`ProcessBuilder`** safely.

## 2. Prerequisites

- Modules **05–07** — classes, methods; Module **06** nested types optional.

## 3. Why This Concept Exists

Frameworks (Spring, JPA, JUnit) use **annotations + reflection** for wiring. Build tools and ops scripts spawn processes. Understanding these JDK facilities clarifies framework magic and enables small utilities.

## 4. Conceptual Explanation

### 4.1 Annotations

Metadata on declarations. Define with `@interface`; control visibility with `@Retention` (`SOURCE`, `CLASS`, `RUNTIME`). Frameworks read `RUNTIME` annotations via reflection.

### 4.2 Reflection

`Class<?>`, `Method`, `Field`, `Constructor` let code inspect types at runtime. **Trade-off**: breaks encapsulation, bypasses compile-time checks, affected by JPMS opens.

### 4.3 Process API

`ProcessBuilder` configures command, environment, redirects; `start()` returns `Process`. Read streams to avoid deadlocks; prefer `redirectErrorStream(true)` for simple cases.

## 5. Step-by-Step Examples

Demo: [reflection-annotations-process-demo](../../examples/core-java/reflection-annotations-process-demo/).

**Verified output:**

```text
annotation=sample-bean
reflectInvoke=hello Ada
processRan=true
processArgs=[java, -version]
```

`annotation=` reads `@DemoTag` on `SampleBean`. `reflectInvoke=` constructs instance and calls `greet`. `processRan=` confirms `java -version` produced output containing “version”.

## 6. Real-World Use Cases

- Test runners discovering `@Test` methods.
- Serialization/config frameworks mapping annotations to behavior.
- Gradle/Maven plugins spawning compiler or formatters.

## 7. Edge Cases and Limitations

- **JPMS**: deep reflection on non-exported packages requires `--add-opens`.
- **Performance**: reflection invocation slower than direct calls—cache `Method` handles.
- **Security**: untrusted code must not control `ProcessBuilder` commands (injection).

## 8. Common Mistakes

### Incorrect

Calling `clazz.newInstance()` (deprecated)—use `getConstructor().newInstance()`.

### Corrected

Handle `InvocationTargetException` cause chain for real failures.

## 9. Security Considerations

Never pass user input into shell commands unvalidated; avoid `Runtime.exec(String)` string concatenation.

## 10. Best Practices

- Prefer **compile-time** code generation (annotation processors) over heavy runtime reflection when possible.
- Set **`redirectErrorStream`** and consume streams promptly.
- Document reflection usage for native-image compatibility (GraalVM).

## 11. Revision and Mastery

- Retention policies difference?
- Why `processArgs` shows list, not a single string?

## 12. Chapter Summary

Annotations describe metadata; reflection reads it at runtime; `ProcessBuilder` runs external programs with explicit argument lists.

## 13. Next Steps

Module 21 — file I/O depth.

## Review Checklist

- [x] Verified output  
- [x] Security note on process invocation
