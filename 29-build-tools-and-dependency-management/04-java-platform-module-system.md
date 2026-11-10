# Java Platform Module System (JPMS)

> **Status:** Completed  
> **Module:** 29 — Build Tools and Dependency Management  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Read **`module-info.java`**: `requires`, `exports`, `opens`.
- Launch with **`--module-path`** and **`-m module/mainClass`**.
- Contrast **strong encapsulation** with classpath “everything public on the path.”

## 2. Prerequisites

- Module **09** — access control; Module **26** — class loading.

## 3. Why This Concept Exists

Before Java 9, any public type on the classpath was visible to all other JARs. That made **JDK internals** and large apps fragile. **JPMS** (Jigsaw) adds explicit **modules** with exports and dependencies, enabling a **module path** and (for the JDK) the **compact runtime** story.

## 4. Conceptual Explanation

| Directive | Meaning |
|-----------|---------|
| `module name { }` | Named unit in `module-info.java` |
| `requires other.mod;` | Compile-time and runtime dependency |
| `exports pkg;` | Other modules may read public types in `pkg` |
| `opens pkg;` | Deep reflection allowed (frameworks, serialization) |
| `provides` / `uses` | Service loader integration |

**Classpath vs module path:** Mixing them is allowed but confusing; application modules should prefer **module path** for new modular JARs. Non-modular JARs on module path become **automatic modules** (derived name from JAR).

**JLS §7.7** module declarations; **JVMS** module resolution at launch.

## 5. Step-by-Step Examples

Demo: [jpms-hello-demo](../../examples/build-tools/jpms-hello-demo/).

**Verified output:**

```text
greeting=jpms-ok
module=app
```

Layout: library module `com.handbook.greeting` exports `com.handbook.greeting`; app module `app` **requires** it and runs `com.handbook.app.Main`.

## 6. Real-World Use Cases

- Hiding implementation packages while exporting an API package.
- **jlink** custom runtimes including only required JDK modules.
- Libraries publishing `module-info` for clearer API surfaces.

## 7. Edge Cases and Limitations

- **Split packages** (same package in two JARs) forbidden on module path.
- Reflective frameworks (Spring, Hibernate) may need `opens` until native support — Module **35** discusses ORM on classpath/module path.
- Not all third-party libraries are modular yet.

## 8. Common Mistakes

### Incorrect

Exports internal package with mutable singletons — encapsulation lost at API boundary.

### Corrected

Export narrow API package; keep `internal` packages package-private or in non-exported packages.

## 9. Best Practices

- Add `module-info` when you control the application boundary and dependencies support it.
- Use `requires transitive` sparingly to re-export APIs.
- Test with `java --module-path ... -m ...` in CI.

## 10. Design Trade-offs

Full modularization improves encapsulation but costs migration effort. Many Spring Boot apps remain classpath-first; JPMS knowledge still explains JDK structure and future jlink deployments.

## 11. Chapter Summary

JPMS replaces implicit classpath visibility with explicit module graphs; the demo shows minimal `requires`/`exports` and modular launch.

## 12. Next Steps

Module **30** — Testing and Quality.

## Review Checklist

- [x] Verified jpms-hello-demo
- [x] JLS module rules cited
