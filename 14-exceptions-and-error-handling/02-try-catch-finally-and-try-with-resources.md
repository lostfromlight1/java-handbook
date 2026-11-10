# try/catch/finally and try-with-resources

> **Status:** Completed  
> **Module:** 14 — Exceptions and Error Handling  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (`try`/`catch`/`finally`, TWR, suppressed exceptions)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. Do **not** manufacture content to fill headings. For omitted sections, record the reason in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow *(or merge note)* | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs *(strongly encouraged for Technical / Deep-dive)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Structure **`try`**, **`catch`**, and **`finally`** for success, failure, and guaranteed cleanup.
- Order **`catch`** clauses correctly by **most specific type first**.
- Use **multi-catch** and **try-with-resources** for concise, safe cleanup.
- Explain when **`finally`** runs and how it interacts with **`return`** and new exceptions.
- Describe **suppressed exceptions** when both body and `close()` fail.

## 2. Prerequisites

- **Module 14 Ch.01** — `throw` / `catch`, checked vs unchecked types.
- **Module 08** — interfaces; **`AutoCloseable`** is an interface with one method.
- **Module 11** — I/O previews; many JDK resources implement **`Closeable`**.

Brief refresher: a **`try`** block contains code that may **throw**. One or more **`catch`** blocks handle matching types. **`finally`** runs on **all** normal and exceptional exits from the `try`/`catch` structure (with rare JVM halt exceptions).

## 3. Why This Concept Exists

Failures must not **leak resources** (files, sockets, database connections) or leave invariants half-updated. **`finally`** (and later, try-with-resources) gives **one place** for cleanup that runs whether the `try` completes or throws.

Before Java 7, manual `try/finally` with nested closes was error-prone—**`close()` skipped** if an earlier line threw. **Try-with-resources** (JEP 286 semantics in Java SE 7+) calls **`close()` automatically** in defined order and links secondary failures as **suppressed** exceptions on the primary failure.

## 4. Conceptual Explanation

### 4.1 Basic `try` / `catch`

```java
try {
    risky(false);           // may throw
    System.out.println("ok-path");
} catch (IllegalStateException ex) {
    System.out.println("catch=" + ex.getMessage());
}
```

If `risky` throws, statements after the throw **inside the same `try`** do not run; matching **`catch`** runs instead.

### 4.2 `finally` — always (almost always) runs

```java
try {
    risky(flag);
} catch (IllegalStateException ex) {
    System.out.println("handled");
} finally {
    System.out.println("finally-ran");
}
```

**`finally`** executes after:

- normal completion of `try`,
- a caught exception,
- an uncaught exception propagating outward,
- **`return`** from `try` or `catch` (before the value is actually returned—subtle; see edge cases).

It does **not** run if the JVM exits (`System.exit`), the process is killed, or a **non-catchable** `Error` stops the thread in some catastrophic scenarios.

### 4.3 Multi-catch

```java
} catch (IOException | DateTimeException ex) {
    log.warn("recoverable", ex);
}
```

One block for **sibling** exception types; **`ex`** is **effectively final**. Cannot combine unrelated types with overlap (e.g. `IOException` and `FileNotFoundException` in one multi-catch—use the superclass only).

### 4.4 Try-with-resources

```java
try (DemoResource r = new DemoResource("R1")) {
    System.out.println(r.read());
}
```

- Resource variables must implement **`AutoCloseable`** (or **`Closeable`**).
- **`close()`** is invoked **automatically** when the block exits—normal or exceptional.
- Resources are closed **in reverse order** of declaration when multiple are declared.
- If `try` throws **and** `close()` throws, the **`close()` exception is suppressed** on the primary (try) exception (`getSuppressed()`).

Since **Java 9**, you may use **effectively final** existing variables:

```java
DemoResource r = new DemoResource("R1");
try (r) {
    // use r
}
```

## 5. Mechanism and Execution Flow

### 5.1 `try` / `catch` / `finally` completion order

For `try { ... } catch (E e) { ... } finally { ... }`:

1. The **`try`** block runs until it **completes normally** or **throws**.
2. On throw, matching **`catch`** clauses are consulted **in order**; the first assignable type runs its block.
3. **`finally`** runs **after** the `try`/`catch` path finishes—whether that path ended normally, after a handled exception, or with an exception about to propagate.
4. If **`finally`** throws, it **replaces** any pending exception from `try`/`catch` (the earlier failure may be lost unless suppressed manually)—a critical edge case for bare `finally`.

### 5.2 Try-with-resources (`AutoCloseable`)

For `try (DemoResource r = new DemoResource("R1")) { ... }` in [`TryWithResourcesDemo`](../../examples/core-java/try-with-resources-demo/):

1. Resource expressions are **evaluated** and references stored (JLS §14.20.3).
2. The **`try`** body runs.
3. On exit (normal or exceptional), **`close()`** is invoked on each resource in **reverse declaration order**.
4. If the body **threw** and **`close()`** also throws, the **close** failure is recorded as a **suppressed exception** on the **primary** (body) throwable via `addSuppressed`; callers inspect with **`getSuppressed()`**.

That ordering is why the demo still prints **`finally-ran`** around manual patterns while TWR guarantees **`close=R1`** even when `catch2=boom`.

### 5.3 Interaction with `return`

A **`return`** inside `try` schedules the return value, but **`finally`** still runs **before** the method actually returns to the caller—unless `finally` also **`return`s** or throws, which can override the pending value/exception. Prefer **no `return` in `finally`**.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **try block** | Protected region where exceptions may originate |
| **catch clause** | Handler bound to a specific throwable type |
| **finally block** | Cleanup that runs after try/catch completion |
| **Try-with-resources (TWR)** | `try` declaring `AutoCloseable` resources with automatic `close()` |
| **Suppressed exception** | Secondary failure attached to primary when TWR closes after a throw |
| **AutoCloseable** | Interface with `void close() throws Exception` |

## 7. Syntax and Language Rules

```java
// try / catch / finally
try {
    // ...
} catch (SpecificException ex) {
    // ...
} finally {
    // cleanup
}

// try-with-resources
try (InputStream in = Files.newInputStream(path)) {
    // ...
}

// Multiple resources — semicolon-separated
try (Reader r = ...; Writer w = ...) { }
```

Rules:

- **`catch`** requires a **`try`** (or try-with-resources).
- **`finally`** may appear with **`catch`**, or with **`try`** alone (try-finally without catch).
- Resource declarations in TWR are **implicitly final**.
- **`close()`** on `AutoCloseable` may throw **checked** `Exception`; TWR translates that into try block semantics (must handle or declare on enclosing method).

## 8. Step-by-Step Examples

### 7.1 Minimal — `TryWithResourcesDemo`

Path: [examples/core-java/try-with-resources-demo/](../examples/core-java/try-with-resources-demo/)

The demo runs two **`try/catch/finally`** sequences, then one **TWR** block.

**Verified output (Temurin 21.0.12.1):**

```text
ok-path
finally-ran
catch2=boom
finally2-ran
open=R1
data-R1
close=R1
```

Line-by-line:

1. **`ok-path`** — `risky(false)` returns without throwing; post-call println runs.
2. **`finally-ran`** — `finally` after successful `try`.
3. **`catch2=boom`** — second `try` calls `risky(true)`; message from `IllegalStateException`.
4. **`finally2-ran`** — `finally` still runs after catch.
5. **`open=R1`** — constructor prints when resource is created, before `read()`.
6. **`data-R1`** — `read()` result printed inside TWR `try`.
7. **`close=R1`** — `close()` runs automatically when leaving the TWR block (success path).

No `twr-error=` line appears because **`read()`** did not throw.

### 7.2 Realistic — JDBC-style manual vs TWR

Manual (error-prone):

```java
Connection conn = dataSource.getConnection();
try {
    runQuery(conn);
} finally {
    conn.close();
}
```

Preferred:

```java
try (Connection conn = dataSource.getConnection()) {
    runQuery(conn);
}
```

If `runQuery` throws, **`close()`** still runs. Compare with Ch.01: connection acquisition may throw **checked** `SQLException`—callers still declare or handle it on the outer method.

### 7.3 Advanced — suppressed exception inspection

```java
try (FailingResource r = new FailingResource()) {
    throw new IllegalStateException("primary");
} catch (IllegalStateException ex) {
    for (Throwable sup : ex.getSuppressed()) {
        System.out.println("suppressed=" + sup.getMessage());
    }
    throw ex;
}
```

When **`close()`** also fails, you often **log suppressions** before rethrowing the primary business failure.

## 9. Real-World Use Cases

- **File and network I/O** — `InputStream`, `Socket`, `Channel` in TWR blocks in request handlers.
- **Transactions** — `finally` to roll back unless committed (often structured as try/catch around JDBC with careful ordering).
- **Locks** — prefer **`try/finally`** with `lock()` / `unlock()` (Module 40 deepens); TWR pattern appears in some lock wrappers.
- **Temp files** — create in `try`, delete in `finally` or use **`deleteOnExit()`** sparingly.

## 10. Internal Behavior

The compiler lowers try-with-resources to **try/finally** with helper logic (see JLS). At runtime, **`close()`** is invoked in **`finally`**-like paths; suppression uses **`Throwable.addSuppressed`**.

If **`finally`** itself throws, that exception may **mask** an exception leaving `try`/`catch` (“exception during exception handling”). TWR defines suppression rules to reduce masking for **close** vs **body**.

## 11. Edge Cases and Limitations

- **`return` in `try` plus `return` in `finally`** — `finally` wins (avoid `return` in `finally`).
- **`finally` that throws** — can replace the original exception from `try`.
- **Null resource in TWR** — `AutoCloseable` variable must be initialized before block entry; `null` skip-close behavior: no close call.
- **Async / threads** — `finally` on one thread does not run cleanup for work leaked to another thread.
- **`close()` must be idempotent** on JDK types; custom resources should document whether double-close is safe.

## 12. Common Mistakes

### Incorrect — swallowing in empty `catch`

```java
try {
    save();
} catch (IOException ex) {
}
```

### Corrected

Log and rethrow, wrap, or recover with an alternate path—never silent swallow at persistence boundaries.

### Incorrect — catching `Exception` before `IOException`

```java
try { read(); }
catch (Exception ex) { }
catch (IOException ex) { }  // COMPILE ERROR — unreachable
```

### Corrected

**Most specific first:**

```java
catch (FileNotFoundException ex) { }
catch (IOException ex) { }
```

### Incorrect — manual close only on happy path

```java
InputStream in = open();
process(in);
in.close();
```

### Corrected

Use **TWR** or **try/finally** so **`close()`** runs when `process` throws.

## 13. Best Practices

- Prefer **try-with-resources** for any type with a **`close()`** lifecycle.
- Keep **`try` blocks small** so the failure origin stays obvious.
- Put **recovery** in `catch`, **non-conditional cleanup** in `finally` or TWR—not business rules in `finally`.
- Use **multi-catch** when handling is identical; avoid duplicating logic.

## 14. Design Trade-offs

| Pattern | When | Caveat |
|---------|------|--------|
| try/catch/finally | Non-`AutoCloseable` cleanup (locks, flags) | `finally` exceptions mask earlier ones |
| try-with-resources | `AutoCloseable` assets | `close()` throwing checked `Exception` |
| try without catch | Propagate after cleanup | Outer method must declare throws |

## 15. Performance Considerations

TWR has **minor** overhead versus hand-written `finally`; negligible versus I/O cost. **`finally`** on hot paths that never throw still runs every invocation—avoid heavy work in `finally` for tight loops.

## 16. Security Considerations

Ensure **`close()`** on sensitive streams runs even on parse errors so handles and temp files do not leak. Do not log **full** stack traces from user-triggered parse failures to clients.

## 17. Java Version and Compatibility Notes

**try-with-resources** since **Java 7**. **Effectively final resource** in TWR since **Java 9**. Semantics stable in **Java 21**.

## 18. Related Language Comparisons

C# **`using`** statement and Python **`with`** mirror TWR. C++ **RAII** destructors run on scope exit without a language `finally`—conceptually similar cleanup discipline.

## 19. Revision and Mastery

- **Gaps:** assuming **`catch` prevents `finally`**; forgetting **reverse close order** with multiple resources.
- **You should answer:** Why does `TryWithResourcesDemo` print `finally-ran` even when no exception occurs?
- **Advanced:** When primary and `close()` both fail, which appears in the top-level thrown exception from TWR?

## 20. Exercises

### Beginner

1. Predict all seven lines of `TryWithResourcesDemo`.
2. Add a third TWR block where `read()` throws; predict `open`, error line, and `close` order.

### Intermediate

3. Rewrite a two-line `open`/`close` file read using TWR.
4. Why is `catch (IOException | Exception e)` illegal?

### Advanced

5. Sketch try/finally without catch that still propagates `IOException` to the caller.

## 21. Interview Questions

1. Does `finally` run when `return` is in `try`?
2. What is a suppressed exception in TWR?
3. When would you use try-finally without catch?

## 22. Solutions

1. See §8.1 verified output.
2. `open`, then catch prints `twr-error=...`, then **`close` still runs** on TWR exit.
3. `try (var in = Files.newInputStream(path)) { return new String(in.readAllBytes()); }`
4. **`Exception` is a supertype of `IOException`** — duplicate alternative in one multi-catch.
5. `try { work(); } finally { cleanup(); }` — exception from `work()` propagates after `finally` completes unless `finally` throws.

## 23. Chapter Summary

**`try` / `catch`** route failures to handlers; **`finally`** and **try-with-resources** guarantee **cleanup**. **`TryWithResourcesDemo`** shows **`finally` on both success and failure** and **automatic `close()`** after a resource `try`. Multi-catch reduces duplication; **suppressed** exceptions preserve **close** failures alongside **body** failures. Next: **designing exception types** for your domain.

## 24. Key Terminology

try · catch · finally · try-with-resources · AutoCloseable · suppressed exception · multi-catch

## 25. References and Further Reading

- [JLS 14.20 — try statements](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20)
- [`AutoCloseable` (Java SE 21)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AutoCloseable.html)
- [Module 14 Ch.01](01-throwable-hierarchy.md)

## 26. Next Steps

- [03-custom-exceptions-and-design.md](03-custom-exceptions-and-design.md) — checked domain types, wrapping, API style
- [Module 15](../15-enums-records-and-sealed-types/README.md) — sealed exception families (preview link)

## Review Checklist

**Mandatory Depth Review:** Technical profile; try/catch/finally and TWR at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (TWR close order §5.2)
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11)

**Omissions (section → reason):**

- **Custom `AutoCloseable` patterns beyond demo** — try-with-resources on domain types only; framework lifecycle in later modules.

**Verification note:** `TryWithResourcesDemo` output unchanged on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
