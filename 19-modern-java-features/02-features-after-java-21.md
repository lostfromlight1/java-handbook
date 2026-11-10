# Features After Java 21

> **Status:** Completed  
> **Module:** 19 — Modern Java Features  
> **Target Java version:** 21 (primary); awareness for 22–25+  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Read **`Runtime.version()`** and JDK **feature** numbers for upgrade planning.
- Distinguish **non-LTS feature releases** (22–24, 26…) from **LTS 25**.
- Adopt a checklist before using syntax from a newer release on an older JDK.

## 2. Prerequisites

- Ch.01 Java 21 tour; [appendix/java-version-history.md](../appendix/java-version-history.md).

## 3. Why This Concept Exists

Production moves slower than OpenJDK’s six-month cadence. Developers read blogs about Java 23 or 25 syntax but deploy on **21**. This chapter teaches **awareness without treating every feature release as the curriculum default**.

## 4. Conceptual Explanation

### 4.1 Release cadence

- **Feature release** every ~6 months (22, 23, 24, …).
- **LTS** releases (8, 11, 17, 21, **25**, …) get longer vendor support—typical enterprise anchor.

### 4.2 Handbook policy

Primary teaching path: **Java 21**. Document **25 LTS** differences when migrating. Use **22–24 / 26–27** as short “introduced in / status” notes, not main-path requirements.

### 4.3 Representative themes after 21 (awareness)

Always verify on [OpenJDK JEPs](https://openjdk.org/jeps/0) before adoption. Examples commonly discussed:

| Area | Examples (check current JEP status) |
|------|-------------------------------------|
| Language tidy-ups | Unnamed variables (`_`), statements before `super(...)` |
| Libraries | Continued Collections / Concurrency API evolutions |
| Runtime | Generational GC defaults shifting in newer LTS builds |
| Removed / retired | Preview features dropped or promoted—never assume blog posts are current |

This table is **awareness**, not an exam list—status changes each release.

### 4.4 Upgrade discipline

1. Read **`java --version`** on every environment (CI, laptop, prod).
2. Align **Maven/Gradle `--release`** with the lowest deployment JDK.
3. Run **tests** with the target JDK before merging syntax from newer releases.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Feature number | `Runtime.version().feature()` — major release (21, 22, …) |
| Non-LTS | Shorter-supported feature release |
| LTS 25 | Next LTS after 21 at time of handbook writing (Nov 2026 awareness) |

## 6. Step-by-Step Examples

Demo: [features-after-21-demo](../../examples/core-java/features-after-21-demo/).

**Verified output:**

```text
feature=21
interim=0
update=12
handbookPrimary=21-LTS
awareness=Document 22-24 non-LTS and 25-LTS in Module 19 Ch.02
upgradeCheck=java --version and Runtime.version() before adopting new syntax
```

On Temurin 21.0.12.1, `feature=21` confirms the runtime used for handbook validation. When you run the same demo on JDK 25, `feature=25` without recompiling—useful for smoke tests.

## 7. Real-World Use Cases

- CI matrix: build on 21, optional job on 25 for forward compatibility.
- Library authors: multi-release JARs (`META-INF/versions/`) for APIs that need newer bytecode.
- Code review: reject syntax that does not match project `release` level.

## 8. Internal Behavior

`Runtime.version()` parses the **running** JVM’s version string per `Runtime.Version` API (Java 9+). It does not detect source-level features at compile time—that is the compiler’s `--release` flag.

## 9. Edge Cases and Limitations

- Vendor strings (Temurin, Corretto) differ; **feature** number is what matters for language level.
- Early-access builds may show `interim`/`update` non-zero.
- Container images often lag LTS adoption—verify base image tags.

## 10. Common Mistakes

### Incorrect

Copying Java 24 snippet into a Java 21 project without checking JEP status.

### Corrected

Add JEP reference + `introduced in` + `status` comment; gate with CI on correct `--release`.

## 11. Best Practices

- Pin JDK in **`.sdkmanrc` / Docker / CI** explicitly.
- Maintain internal “**platform version**” doc listing allowed language features.
- Prefer **21-final** APIs in shared libraries; experimental features behind flags.

## 12. Design Trade-offs

Chasing every feature release maximizes new syntax but increases **support and training** cost. Staying on LTS trades freshness for stability.

## 13. Performance Considerations

Newer releases may change GC defaults—benchmark after JDK upgrades, not only after code changes.

## 14. Security Considerations

Stay on vendor-supported JDK builds; non-LTS releases have shorter security support windows.

## 15. Java Version and Compatibility Notes

See appendix version table. **Java 21** remains handbook primary; **25** documented for migration awareness.

## 16. Revision and Mastery

- Difference between `feature` release and LTS?
- Why non-LTS releases are not the main curriculum path?

## 17. Exercises

### Beginner

Run `java -version` and record feature vs vendor.

### Intermediate

List three JEPs promoted between 21 and 25 and their final/preview status.

### Advanced

Sketch a CI policy: compile 21, test 21+25.

## 18. Chapter Summary

After Java 21, treat newer releases as **versioned upgrades** with explicit checks. Use `Runtime.version()` and build `release` flags—do not assume blog-ready syntax runs on LTS 21.

## 19. Next Steps

[03-preview-and-incubator-features.md](03-preview-and-incubator-features.md)

## Review Checklist

- [x] Awareness vs primary path distinguished  
- [x] Verified demo output recorded
