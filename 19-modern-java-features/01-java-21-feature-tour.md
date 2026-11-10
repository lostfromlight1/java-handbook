# Java 21 Feature Tour

> **Status:** Completed  
> **Module:** 19 — Modern Java Features  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Name major **Java 21 LTS** language and library features the handbook builds on.
- Run code using **virtual threads**, **sequenced collections**, **record patterns**, and **switch expressions**.
- Relate Java 21 features to earlier modules (records, pattern matching, concurrency preview paths).
- Decide when a feature is **production-default** on Java 21 versus still **preview/incubator** (Ch.03).

## 2. Prerequisites

- Modules **03**, **15** — pattern matching, records, sealed types.
- Module **16+** — lambdas and streams (for comparing thread models).
- [appendix/java-version-history.md](../appendix/java-version-history.md) — LTS policy.

## 3. Why This Concept Exists

Java 21 is the handbook’s **primary LTS**. Teams standardizing on 21 need a concise map of what changed since Java 17/11—not every JEP, but the features you will see in new codebases: lighter concurrency, richer pattern matching, and collection API cleanup.

## 4. Conceptual Explanation

### 4.1 Virtual threads (JEP 444 — final in 21)

**Platform threads** map to OS threads; creating many is expensive. **Virtual threads** are JVM-managed, cheap to spawn; blocking I/O parks the virtual thread instead of holding an OS thread. Use `Executors.newVirtualThreadPerTaskExecutor()` for request-per-task server styles. Do not confuse with “automatic parallelism”—CPU-bound work still needs platform threads or pools sized to cores.

### 4.2 Sequenced collections (JEP 431)

`SequencedCollection`, `SequencedSet`, and `SequencedMap` unify “first/last” and “reversed view” operations. `List` now exposes `getFirst()`, `getLast()`, `addFirst()`, `addLast()` with consistent semantics.

### 4.3 Record patterns (JEP 440)

After **record types** (Module 15), **record patterns** deconstruct components in `instanceof` and `switch`. Example: `if (o instanceof Point(int x, int y))`.

### 4.4 Pattern matching for `switch` (JEP 441)

`switch` supports type patterns and guards; **switch expressions** (arrow form) produce values. Still avoid fall-through bugs in classic colon form.

### 4.5 Other stable highlights on the 21 path

- **Text blocks** (since 15) for multi-line strings.
- **Foreign Function & Memory API** — third preview in 21 (incubator/preview path—Ch.03).
- Generational **ZGC** improvements—runtime tuning, not syntax.

## 5. Terminology

| Term | Meaning |
|------|---------|
| LTS | Long-term support release (21 is handbook primary) |
| Virtual thread | Lightweight thread scheduled by the JVM |
| Record pattern | Pattern matching that extracts record components |
| Sequenced collection | Collection with defined encounter order and first/last API |

## 6. Syntax and Language Rules

- Virtual thread executors: `try (var ex = Executors.newVirtualThreadPerTaskExecutor()) { ... }`
- Record patterns require a **record** type (or nested patterns in later releases).
- Switch expressions use `->` and must be **exhaustive** when used as expressions (compiler checks).

## 7. Step-by-Step Examples

Demo: [java21-features-demo](../../examples/core-java/java21-features-demo/).

**Verified output (no args):**

```text
virtualThread=vt-ready
sequenced=head/tail
recordPattern=3+4=7
switchExpr=default
textBlockLines=2
```

`virtualThread=` shows a task running on a virtual thread. `sequenced=` uses `addFirst`/`addLast`. `recordPattern=` deconstructs `Point`. `switchExpr=` uses arity-based switch expression. `textBlockLines=` counts lines from a text block.

## 8. Real-World Use Cases

- HTTP servers and microservices: one virtual thread per request with blocking JDBC/HTTP clients.
- Data pipelines: sequenced deque/list operations without manual index math.
- Domain modeling: record patterns in command handlers and message dispatch.

## 9. Internal Behavior

Virtual threads mount on a **carrier** platform thread; blocking calls unmount. This is **HotSpot** behavior; other JVMs may differ in scheduler details. Record patterns are compile-time sugar desugaring to accessors.

## 10. Edge Cases and Limitations

- **Pinning**: synchronized blocks or native code on virtual threads can pin carriers—monitor in production.
- Record patterns on **non-record** types use different pattern forms (Module 03/15).
- Features marked preview in 21 require `--enable-preview` (Ch.03)—do not mix casually in production.

## 11. Common Mistakes

### Incorrect

Treating virtual threads as free CPU parallelism for heavy compute.

### Corrected

Use virtual threads for **many blocking tasks**; size platform thread pools to CPU cores for compute.

## 12. Best Practices

- Standardize on **Java 21 language level** in build tools (`--release 21`).
- Prefer **sequenced** APIs over `list.get(0)` when intent is first element.
- Track **JEP status** when adopting features newer than 21 (Ch.02).

## 13. Design Trade-offs

Virtual threads simplify thread-per-task code but require **review of thread-local and locking** assumptions. Pattern matching improves readability but needs team familiarity.

## 14. Performance Considerations

Millions of virtual threads are feasible; millions of platform threads are not. Measure carrier pool and pinning before rollout.

## 15. Security Considerations

New APIs do not bypass sandbox or module access rules; preview features may change semantics—avoid in security-sensitive code until final.

## 16. Java Version and Compatibility Notes

| Feature | Introduced | Status in 21 |
|---------|------------|--------------|
| Virtual threads | 21 | Final |
| Sequenced collections | 21 | Final |
| Record patterns | 21 | Final |
| Pattern matching for switch | 21 | Final |
| Structured concurrency | 21 | Preview |

## 17. Related Language Comparisons

Go goroutines and Kotlin coroutines solve similar “cheap tasks” problems with different runtimes; Java virtual threads keep **blocking** style code viable.

## 18. Revision and Mastery

- Explain carrier pinning.
- When is `getFirst()` clearer than `get(0)`?
- Difference between record **type** and record **pattern**?

## 19. Exercises

### Beginner

List three Java 21 features you already used in earlier modules.

### Intermediate

Extend `Java21FeaturesDemo` with a sealed `Shape` and exhaustive `switch`.

### Advanced

Compare virtual-thread executor vs fixed platform pool for 10,000 simulated blocking tasks.

## 20. Interview Questions

What problem do virtual threads solve? What is a sequenced collection?

## 21. Solutions

See module assessment solutions; exercise code in local copies of demos.

## 22. Chapter Summary

Java 21 LTS delivers **virtual threads**, **sequenced collections**, and mature **pattern matching** (records + switch). The handbook targets this release; later versions extend the story in Ch.02–03.

## 23. Key Terminology

Virtual thread, sequenced collection, record pattern, switch expression, LTS.

## 24. References and Further Reading

- [Oracle Java 21 release notes](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
- JEP 444, 431, 440, 441

## 25. Next Steps

[02-features-after-java-21.md](02-features-after-java-21.md) — cadence after LTS 21.  
[03-preview-and-incubator-features.md](03-preview-and-incubator-features.md) — preview flags and lifecycle.

## Review Checklist

- [x] Learning objectives covered  
- [x] Depth gate passed  
- [x] Outputs verified Temurin 21.0.12.1  
- [x] Preview features labeled where relevant
