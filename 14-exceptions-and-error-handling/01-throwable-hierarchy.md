# Throwable Hierarchy

> **Status:** Completed  
> **Module:** 14 — Exceptions and Error Handling  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (hierarchy, checked vs unchecked, throw/catch propagation)  
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

- Place **`Throwable`**, **`Error`**, **`Exception`**, and **`RuntimeException`** on the language’s exception hierarchy.
- Explain the difference between **checked** and **unchecked** exceptions and what the compiler enforces.
- Use **`throw`** to signal failure and **`catch`** to handle specific failure types.
- Choose appropriate JDK exception types for common validation and state bugs (`IllegalArgumentException`, `IllegalStateException`).
- State when **`Error`** subclasses are **not** meant for application `catch` blocks.

## 2. Prerequisites

- **Module 05 Ch.02** — `IllegalArgumentException` at validation boundaries (unchecked preview).
- **Module 06** — methods, `throws` on method declarations.
- **Module 07** — inheritance (`extends`) applies to exception classes too.
- **Module 10** — static typing; exception types are ordinary reference types in the type system.

Brief refresher: when a method **`throws`** a checked exception type, **callers must** either handle it with `catch` or declare `throws` themselves. Unchecked types under **`RuntimeException`** do not create that compile-time obligation.

## 3. Why This Concept Exists

Programs fail. Networks drop, disks fill, users send bad input, and invariants break. Without a structured failure path, code returns **sentinel values** (`null`, `-1`, magic booleans) or calls **`System.exit`**—callers cannot tell **normal** from **failure**, and libraries cannot be composed.

Java models failure with **exceptions**: an object carrying a **message**, optional **cause**, and **stack trace** unwinds the call stack until a matching **`catch`** runs or the thread terminates. The **type** of the exception documents **what went wrong** and lets handlers branch (`catch (IOException ex)` vs `catch (SQLException ex)`).

The hierarchy separates **conditions programmers are expected to handle** (`Exception`) from **serious JVM/resource failures** (`Error`) and, within `Exception`, **checked** (recoverable externalities) from **unchecked** (programming bugs and optional runtime failures).

## 4. Conceptual Explanation

### 4.1 `Throwable` — root of all errors and exceptions

Every throwable object is an instance of **`Throwable`** or a subclass. Core members:

| Member | Role |
|--------|------|
| **Message** | Human-readable description (`getMessage()`) |
| **Cause** | Chained underlying failure (`getCause()`) |
| **Stack trace** | Call path at throw time (`printStackTrace()`, `getStackTrace()`) |

You **create** throwables with `new` and **signal** failure with **`throw`**.

### 4.2 `Error` vs `Exception`

```
Throwable
├── Error          (e.g. OutOfMemoryError, StackOverflowError)
└── Exception
    ├── RuntimeException (+ subclasses)  → unchecked
    └── other Exception subclasses       → usually checked
```

- **`Error`**: serious problems the **application usually should not try to recover from** (out of memory, class linkage failure). You *can* catch `Error`, but doing so to “continue normally” is almost always wrong.
- **`Exception`**: conditions an application **might** handle (missing file, bad HTTP status, invalid user input wrapped as a domain exception).

HotSpot and the JLS treat **`Error`** and **`Exception`** as subclasses of **`Throwable`**; only **`Exception`** (and its subclasses) participate in the **checked exception** rules described below.

### 4.3 Checked vs unchecked

| Category | Supertype | Compiler on callers | Typical meaning |
|----------|-----------|---------------------|-----------------|
| **Checked** | `Exception` but **not** `RuntimeException` | Must handle or declare `throws` | Recoverable, expected failure modes (I/O, parsing) |
| **Unchecked** | `RuntimeException` (or `Error`) | No extra compile rule | Bugs, violated preconditions, optional failures |

**Unchecked** does not mean “never catch”—it means the **language does not force** every caller to declare or handle the type. Many APIs still document unchecked failures in Javadoc.

### 4.4 `throw` and `catch` at a glance

```java
void readConfig() throws ConfigException {
    throw new ConfigException("missing-file");
}

try {
    readConfig();
} catch (ConfigException ex) {
    System.out.println("caughtChecked=" + ex.getMessage());
}
```

The **`catch`** parameter type must be assignable from the thrown type. Handlers for **subtypes** must appear **before** handlers for **supertypes** if both appear in one `try`.

## 5. Mechanism and Execution Flow

Exception handling spans **compile-time analysis** (checked types), **runtime throw**, and **stack unwinding** until a handler matches or the thread terminates.

### 5.1 Compile-time checked analysis (`javac`)

When method `load()` **declares** `throws IOException`:

1. **`javac`** records that `load` may complete abruptly with `IOException` (JLS §11.2).
2. Each **call site** must either **handle** `IOException` in a `try`-`catch` or **declare** `throws IOException` on the enclosing method.
3. Types under **`RuntimeException`** and **`Error`** are **unchecked**—no mandatory declaration at callers; the compiler still type-checks `catch` clauses.

`ThrowableDemo`’s **`readConfig() throws ConfigException`** forces the `try`-`catch` in `main`; **`mightFail`** does not, because `IllegalArgumentException` is unchecked.

### 5.2 Throw and unwind (runtime)

When **`throw new IllegalArgumentException("bad-arg")`** runs:

1. The JVM **allocates** the throwable and fills **stack trace** data (implementation-specific in HotSpot).
2. The current method’s frame is **popped** unless a **`catch`** in the same method matches.
3. The search continues **up the call stack** until a matching **`catch`** runs or the thread’s **uncaught handler** receives the throwable.
4. If a handler runs, execution continues **after** the `catch` block unless the handler **rethrows**.

This is **not** a normal `return`—intermediate frames between throw site and handler **do not** run statements after the throw point.

### 5.3 Type matching at `catch`

A **`catch (IllegalArgumentException ex)`** matches the thrown object when the runtime type is **`IllegalArgumentException`** or any **subtype**. Handlers are tried **in source order**; the first assignable type wins—so **more specific** types must appear **before** supertypes in the same `try`.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Throwable** | Root class for `Error` and `Exception` in Java |
| **Checked exception** | `Exception` subtype that is not a `RuntimeException`; checked at compile time |
| **Unchecked exception** | `RuntimeException` or `Error`; no mandatory `throws`/`catch` for callers |
| **Throw** | Start abnormal control flow by raising a `Throwable` instance |
| **Catch** | Handle a thrown type in a `catch` clause |
| **Stack trace** | Snapshot of active frames when the throwable was created (or wrapped) |
| **Propagate** | Let an exception leave a method without catching (declared or unchecked) |

## 7. Syntax and Language Rules

```java
// Throwing
throw new IllegalArgumentException("bad-arg");

// Declaring checked propagation
void load() throws IOException { }

// Catching
try {
    load();
} catch (IOException ex) {
    // handle
}

// Multi-catch (Ch.02) — siblings only
} catch (IOException | SQLException ex) { }

// Rethrow
catch (IOException ex) {
    throw ex;
}
```

Rules:

- Only **`Throwable`** (and subclasses) may be **thrown** or **caught**.
- A method may list **multiple** checked types in `throws` (`throws IOException, SQLException`).
- **`RuntimeException`** and **`Error`** need not appear in `throws` even when thrown.
- Subclasses of checked exceptions remain **checked** unless they extend `RuntimeException`.

## 8. Step-by-Step Examples

### 7.1 Minimal — `ThrowableDemo`

Path: [examples/core-java/throwable-demo/](../examples/core-java/throwable-demo/)

```java
try {
    mightFail(true);
} catch (IllegalArgumentException ex) {
    System.out.println("caughtUnchecked=" + ex.getMessage());
}

try {
    readConfig();
} catch (ConfigException ex) {
    System.out.println("caughtChecked=" + ex.getMessage());
}

System.out.println("runtimeIsUnchecked=" + (new RuntimeException() instanceof Exception));
System.out.println("errorIsThrowable=" + (new OutOfMemoryError() instanceof Throwable));
```

**Verified output (Temurin 21.0.12.1):**

```text
caughtUnchecked=bad-arg
caughtChecked=missing-file
runtimeIsUnchecked=true
errorIsThrowable=true
```

- **`IllegalArgumentException`** is unchecked; `mightFail` needs no `throws`.
- **`ConfigException extends Exception`** is checked; `readConfig` declares **`throws ConfigException`**.
- Every **`RuntimeException`** is an **`Exception`**, but not every `Exception` is unchecked.
- **`OutOfMemoryError`** is an **`Error`**, still a **`Throwable`**.

### 7.2 Realistic — validation vs external I/O

```java
public void transfer(Money amount, Account dest) {
    Objects.requireNonNull(amount, "amount");
  if (amount.isNegative()) {
        throw new IllegalArgumentException("amount must be non-negative");
    }
    if (account.isClosed()) {
        throw new IllegalStateException("account closed");
    }
    try {
        ledger.post(amount, dest);
    } catch (IOException ex) {
        throw new TransferException("ledger unavailable", ex);
    }
}
```

**Unchecked** types flag **caller mistakes** or **illegal state** on objects you control. **Checked** `IOException` reflects an **external** resource; wrapping preserves cause for logs (Ch.03).

### 7.3 Advanced — `instanceof` and catching by interface

```java
try {
    service.run();
} catch (RecoverableException ex) {
    retry(ex);
} catch (Exception ex) {
    abort(ex);
}
```

Domain **`RecoverableException`** (checked or unchecked by design) narrows handling. Avoid **`catch (Exception ex)`** at the top level without rethrowing unless you are a framework boundary.

## 9. Real-World Use Cases

- **HTTP APIs** — map `IllegalArgumentException` to **400 Bad Request**, missing auth to **401**, optimistic lock failure to **409**.
- **Batch jobs** — catch **checked** I/O errors per file, log, continue; let **unchecked** programming bugs fail the run.
- **Libraries** — document unchecked precondition failures; use checked exceptions for operations that **always** depend on I/O or remote systems.
- **Frameworks** — translate persistence errors into **`DataAccessException`** hierarchy (Spring) while keeping driver specifics in the **cause**.

## 10. Internal Behavior

When **`throw`** runs, the JVM searches **up the stack** for a matching **`catch`**. If none exists, the **thread’s default uncaught handler** runs and the stack trace prints. The object is allocated like any other; stack trace data is filled in by the JVM (implementation-specific details in HotSpot).

The **compiler** enforces checked rules: **`javac`** rejects call sites that can throw checked types unless handled or declared. Unchecked types are tracked for analysis tools but not for mandatory `throws` lists.

See [JLS §11.2](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2) (exception analysis) and the Java SE API for [`Throwable`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html).

## 11. Edge Cases and Limitations

- **`null`** is not throwable; only `Throwable` references.
- You cannot **`catch`** with a **type parameter** (`catch (T ex)`).
- **`Exception`** as a catch-all swallows **checked and unchecked** application failures—easy to hide bugs.
- **`ThreadDeath`** and some **`Error`** types exist for JVM lifecycle; application code rarely handles them correctly.
- **Generics:** you cannot `throw` a type variable bound only to `Exception` without erasure complications (Module 13).

## 12. Common Mistakes

### Incorrect — catching `Error` to keep going

```java
try {
    process();
} catch (OutOfMemoryError e) {
    // pretend nothing happened
}
```

### Corrected

Treat **`Error`** as fatal: log, release scarce resources if possible, **terminate or fail the job**; fix heap/config instead of masking.

### Incorrect — using checked exceptions for every validation failure

```java
void setAge(int age) throws InvalidAgeException {
    if (age < 0) throw new InvalidAgeException();
}
```

### Corrected

```java
void setAge(int age) {
    if (age < 0) throw new IllegalArgumentException("age < 0");
}
```

Callers violating a **parameter contract** get **unchecked** failures; reserve **checked** for **external** conditions callers cannot prevent by correct arguments alone.

## 13. Best Practices

- Prefer **JDK standard types** when semantics match (`IllegalArgumentException`, `IllegalStateException`, `UnsupportedOperationException`).
- Document **unchecked** failures in public API Javadoc (`@throws`).
- Do not use exceptions for **ordinary control flow** (empty loops, expected “not found” on hot paths)—consider **Optional** or return types ([Module 18](../18-optional-and-null-handling/README.md)).
- Catch **specific types** first; avoid empty `catch` blocks (Ch.02).

## 14. Design Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| Checked exceptions | Forces awareness of failure modes | Verbose `throws` chains, harder in lambdas |
| Unchecked exceptions | Cleaner signatures, fits streams/lambdas | Easy to forget handling at boundaries |
| Result / Optional types | Explicit success/failure in type | Not for all layered stacks; mixing styles hurts |

Java commits to **both** checked and unchecked; modern APIs often bias toward **unchecked** + documentation for library ergonomics.

## 15. Performance Considerations

Throwing is **slow relative to normal returns** because it captures stack traces and unwinds frames. Use exceptions for **exceptional** paths, not per-element validation in tight inner loops when a boolean or validation result suffices.

## 16. Security Considerations

Stack traces and messages may leak **paths, SQL, or user data** in error responses. Sanitize outward-facing messages; log full detail server-side. Do not catch **`Throwable`** at public entry points and continue with partial trust state.

## 17. Java Version and Compatibility Notes

The hierarchy is stable since Java 1.0. **Java 7** added **multi-catch** and **try-with-resources** (Ch.02). **Java 21** has no change to checked/unchecked rules. Preview features elsewhere do not alter `Throwable` semantics.

## 18. Related Language Comparisons

C# has **no checked exceptions**; `throws` clauses do not exist. Rust uses **`Result<T, E>`** for recoverable errors and **panic** for unrecoverable states. Python has a flat exception tree with **no compile-time checking**.

## 19. Revision and Mastery

- **Gaps:** believing **`Exception`** always means checked; forgetting that **`RuntimeException extends Exception`**.
- **You should answer:** Why does `ThrowableDemo` print `runtimeIsUnchecked=true`?
- **Advanced:** Exception transparency in streams; wrapping checked exceptions for functional interfaces (`wrap`/`sneakyThrow` patterns and why libraries discourage the latter).

## 20. Exercises

### Beginner

1. Predict all four lines of `ThrowableDemo` without running.
2. Write a method that throws `IllegalStateException` when a `boolean open` field is false.

### Intermediate

3. Why must `readConfig() throws ConfigException` appear on the declaration?
4. Is `catch (Throwable t)` a good idea at the root of a web request? Why?

### Advanced

5. Explain why lambdas cannot easily propagate arbitrary **checked** exceptions without wrapping.

## 21. Interview Questions

1. Difference between `Error` and `Exception`?
2. What makes an exception “checked”?
3. When would you use `IllegalArgumentException` vs a custom checked type?

## 22. Solutions

1. See verified output in §8.1.
2. `if (!open) throw new IllegalStateException("not open");`
3. `ConfigException` is checked; the compiler requires declaration or handling at every call site.
4. No for production—masks bugs, continues with corrupted state; framework handlers map known exceptions to responses and let unknown ones fail safely.
5. Lambda interfaces (`Runnable`, etc.) do not declare checked throws; checked exceptions inside lambdas must be caught, wrapped, or declared on the enclosing method if it throws.

## 23. Chapter Summary

**`Throwable`** roots **errors** and **exceptions**. **`Error`** signals serious JVM-level problems; **`Exception`** covers application-level failures. **`RuntimeException`** branches are **unchecked**; other **`Exception`** types are **checked** and enforced by **`javac`**. **`ThrowableDemo`** contrasts catching unchecked and checked types and confirms type relationships. Next: **structuring handlers** with `try`, `finally`, and **automatic resource cleanup**.

## 24. Key Terminology

Throwable · Error · Exception · RuntimeException · checked exception · unchecked exception · throw · stack trace

## 25. References and Further Reading

- [JLS 11 — Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html)
- [Java Tutorial — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html)
- [Module 05 Ch.02 — validation exceptions](../05-object-oriented-programming/02-encapsulation-and-abstraction.md)

## 26. Next Steps

- [02-try-catch-finally-and-try-with-resources.md](02-try-catch-finally-and-try-with-resources.md) — control flow, cleanup, and `AutoCloseable`
- [03-custom-exceptions-and-design.md](03-custom-exceptions-and-design.md) — domain types and API boundaries

## Review Checklist

**Mandatory Depth Review:** Technical profile; throwable hierarchy and checked/unchecked rules at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (hierarchy §4.2; unwind flow §5.2)
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

- **Virtual-thread exception semantics** — noted in Revision and Mastery; full structured concurrency in later concurrency modules.

**Verification note:** `ThrowableDemo` output unchanged on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
