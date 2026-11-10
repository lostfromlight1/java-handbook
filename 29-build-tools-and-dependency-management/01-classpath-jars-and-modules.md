# Classpath, JARs, and Modules

> **Status:** Completed  
> **Module:** 29 — Build Tools and Dependency Management  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Explain **classpath** vs **module path** at a practitioner level.
- Build a **JAR** with `jar` and run with `-cp`.
- Read a **MANIFEST.MF** `Main-Class` entry and understand fat vs thin JAR trade-offs.

## 2. Prerequisites

- Module **01** Ch.05 — packages and classpath.
- Module **26** — class loading (where the JVM finds bytecode).

## 3. Why This Concept Exists

Real applications span dozens of packages and third-party libraries. Without a repeatable way to name **where bytecode lives**, `java` and `javac` cannot find types. **JAR files** (Java ARchive) bundle `.class` files and resources; the **classpath** is the ordered search list the launcher and compiler use on the **classpath** (non-modular) model.

## 4. Conceptual Explanation

| Mechanism | Role |
|-----------|------|
| Classpath (`-cp` / `CLASSPATH`) | Directories and JARs searched for packages and classes (legacy default model). |
| JAR | Zip archive with `.class` files, optionally `META-INF/MANIFEST.MF`. |
| Module path (`--module-path`) | JPMS modules (Ch.04); stronger encapsulation than flat classpath. |
| Fat/uber JAR | One JAR containing your app **and** dependency classes (convenient deploy; merge conflicts possible). |
| Thin JAR | Your code only; dependencies resolved separately (Maven/Gradle coordinate this). |

**JLS/JVMS:** The launcher must load the initial class named on the command line; user classes are found via classpath or module path per launch mode.

**HotSpot:** Uses application class loaders (classpath) or module layers (JPMS) — implementation detail, but explains “class not found” vs `NoClassDefFoundError`.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Entry point | Class with `public static void main` specified to run. |
| Manifest | `META-INF/MANIFEST.MF` metadata (`Main-Class`, `Class-Path`, module info in modular JARs). |
| Coordinates | Maven-style `groupId:artifactId:version` (Ch.02) — not used by raw `jar` alone. |

## 6. Step-by-Step Examples

### 6.1 Multi-package compile + JAR

Demo: [classpath-jars-demo](../../examples/build-tools/classpath-jars-demo/).

**Verified output (Temurin 21.0.12.1):**

```text
greet=Hello, Handbook!
fromJar=true
```

`fromJar=true` — `ProtectionDomain.getCodeSource()` is non-null when loaded from the JAR.

Commands (see demo README): `javac -d out ...`, `jar --create --file=lib/greeter-app.jar -C out .`, `java -cp lib/greeter-app.jar app.App`.

### 6.2 Runnable JAR with manifest (conceptual command)

```text
jar --create --file=app.jar --main-class=app.App -C out .
java -jar app.jar
```

`--main-class` writes `Main-Class:` into the manifest so `java -jar` knows the entry point.

## 7. Real-World Use Cases

- Shipping a CLI tool as a single `-jar` artifact.
- Classpath conflicts when two JARs contain the same package (mitigated by build tools and JPMS).
- Docker images that copy one fat JAR vs layered thin JARs.

## 8. Internal Behavior

Classpath loading: **application class loader** delegates to the platform loader for JDK classes, then searches your JARs. Wrong order on `Class-Path` manifest entries causes subtle `NoClassDefFoundError` at first use of a type, not at JVM start.

## 9. Edge Cases and Limitations

- Duplicate classes on the classpath: **first match wins** (undefined which JAR “should” win — build tools enforce one version).
- `java -jar` **ignores** `-cp` unless you use `-cp` without `-jar` or customize the launcher.
- Resources in JARs require **classpath-relative** paths (`getResourceAsStream`).

## 10. Common Mistakes

### Incorrect

Compiles with `javac app/App.java` but forgets `util/Greeter.java` — compiles against stale classes.

### Corrected

Compile all sources into one output tree (`-d out`) before packaging.

## 11. Best Practices

- Prefer **Maven/Gradle** for dependency graphs; use raw `javac`/`jar` to understand what build tools automate.
- One **output directory per compile**; never commit `out/` or `.class` files.
- Name JARs with **version** in the filename for operators.

## 12. Design Trade-offs

Fat JAR: simple deploy, large artifact, shading may be needed. Thin JAR + resolved `lib/`: smaller app JAR, requires correct classpath at runtime.

## 13. Revision and Mastery

- Difference between `ClassNotFoundException` and `NoClassDefFoundError`?
- When would you choose module path over classpath?

## 14. Chapter Summary

Classpath and JARs are the non-modular packaging model every Java developer meets; build tools layer coordinates and transitive resolution on top.

## 15. Next Steps

[02-maven.md](02-maven.md)

## Review Checklist

- [x] Verified classpath-jars-demo
- [x] JLS launcher vs HotSpot class loading labeled
