# JDK, JRE, JVM, and Tooling

> **Status:** Completed  
> **Module:** 01 — Introduction to Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9 (`javac 21.0.9`)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain JDK, JRE, and JVM precisely and relate them to day-to-day commands.
- Identify core JDK tools: `java`, `javac`, `jar`, `javap`, `jshell`, `jcmd` (awareness).
- Read runtime properties that reveal which JVM is running your code.
- Distinguish the Java language, the SE API, and a specific JDK distribution.
- Avoid common tooling misconceptions that break beginners’ setups.

## 2. Prerequisites

- Module 00, especially [What Is Programming and What Is Java?](../00-programming-foundations/01-what-is-programming-and-what-is-java.md) and [How Computers Execute Programs](../00-programming-foundations/02-how-computers-execute-programs.md)

## 3. Why This Concept Exists

“Install Java” is ambiguous. Teams fail builds because one person has a JRE-only mental model, another has JDK 17 on `PATH`, and CI uses 21. This chapter makes the platform vocabulary and tools sharp so Modules 02+ can assume a working JDK 21.

## 4. Conceptual Explanation

### 4.1 Three layers (refined)

| Layer | What it is | What you use it for |
|-------|------------|---------------------|
| **Language** | Syntax and rules (JLS) | Writing `.java` source |
| **JVM** | Runtime that executes bytecode | Running programs, memory management, threads |
| **JDK** | Development kit: compiler + tools + libraries + runtime | Daily development |

**JRE** historically meant “runtime only” (run apps, limited/no compiler). Modern Oracle/OpenJDK downloads emphasize the **JDK**. For this handbook: **install a JDK 21**.

```mermaid
flowchart TB
  lang[Java language JLS]
  api[Java SE API libraries]
  jdk[JDK distribution]
  javac[javac and tools]
  jvm[JVM e.g. HotSpot]
  lang --> jdk
  api --> jdk
  jdk --> javac
  jdk --> jvm
```

### 4.2 Distributions vs specifications

- **Specifications** define required behavior (JLS, JVMS, standard APIs).
- **Distributions** are concrete installs: Oracle JDK, Eclipse Temurin, Amazon Corretto, etc.

This handbook validates examples on **Oracle JDK 21.0.9**. Other JDK 21 builds should run the same language/API examples; vendor extras and defaults can differ—label vendor-specific advice.

### 4.3 Essential command-line tools

| Tool | Role |
|------|------|
| `javac` | Compile `.java` → `.class` |
| `java` | Launch the JVM and run a main class |
| `jar` | Create/inspect JAR archives |
| `javap` | Print human-readable views of compiled `.class` files (see §4.6) |
| `jshell` | Interactive REPL for experiments |
| `jcmd` / `jps` | Runtime diagnostics (later modules) |

You do not need every tool on day one. You do need `javac` and `java` fluently.

### 4.4 What “running Java” starts

When you type `java MainClass`, you are not “interpreting” your `.java` file directly. You start the **`java` launcher**, which boots a **JVM process** (HotSpot on Oracle JDK 21 used here). At a beginner level, the useful mental model is:

1. **Locate the class** — The launcher asks the JVM to load a class whose *binary name* matches what you passed (for example `HelloWorld`, or `app.App` when using packages). It searches **classpath** entries and platform libraries (JDK classes like `java.lang.String`).
2. **Load and verify** — The JVM’s **class loader** reads the matching `.class` file, checks it is well-formed, and prepares it for execution. You do not call the class loader yourself in day-one code; it is part of the runtime.
3. **Find the entry point** — For classic programs, the JVM looks for `public static void main(String[] args)` on that class.
4. **Invoke `main`** — The launcher passes your command-line tokens as the `String[] args` array and starts running bytecode from `main`.
5. **Run until done** — Your code allocates objects on the **heap** (JVM-managed memory). **Garbage collection (GC)** is the JVM’s automatic reclaiming of objects that are no longer reachable, so you rarely call `free` manually. GC policies and tuning are advanced topics (Modules 27–28); here, remember: the JVM owns object lifetime.
6. **Exit** — When all non-daemon threads finish (or you call `System.exit`), the JVM process ends and returns an exit code to the OS.

The JVM in Oracle JDK 21 here is **HotSpot**—a concrete *implementation* of the JVM specification, not the Java language itself. Another JDK build might use a different VM; language and SE API behavior still come from the specifications.

### 4.5 Classpath (first introduction)

The **classpath** is the list of places the JVM and compiler look for **your** classes (not the built-in JDK libraries, which are always available). Each entry might be:

- A directory whose subfolders mirror package names (for example `out/` containing `out/app/App.class`), or
- A **JAR** file that bundles many `.class` files.

**Why it exists:** Java separates *compile time* (where `.java` files live) from *run time* (where the JVM finds `.class` files). If compiled output sits in `out/` but you run `java` from the project root without telling the JVM about `out/`, the launcher cannot find your main class.

**Typical commands:**

```bash
javac -d out src/app/App.java    # write .class files under out/
java -cp out app.App             # search out/ when loading app.App
```

On many simple single-folder demos, the **current directory** `.` is on the classpath by default, so `java HelloWorld` works right beside `HelloWorld.class`. As soon as you use packages or a separate output folder, you set `-cp` (or `-classpath`) explicitly. Build tools (Module 29) compute classpath for you; the underlying idea is the same.

### 4.6 What `javap` shows (conceptually)

**`javap`** is a JDK tool that inspects **already compiled** `.class` files. It does not run your program. Think of it as a readable summary of what the compiler produced:

- **Class name**, supertypes, and modifiers (`public`, `final`, …).
- **Fields** and **methods** with descriptors (types in JVM shorthand).
- With **`-c`**, a listing of **bytecode instructions** per method—the low-level steps the JVM executes (loads, calls, returns).

You use `javap` to connect source-level Java to “what is actually in the class file,” for learning or debugging version/skew issues. Output is tied to your JDK’s format; treat it as a snapshot, not something you must memorize.

## 5. Terminology

| Term | Meaning |
|------|---------|
| JDK | Java Development Kit |
| JRE | Java Runtime Environment (runtime-focused packaging) |
| JVM | Java Virtual Machine |
| Distribution | A vendor’s JDK/JRE build |
| Classpath | List of directories/JARs where `javac`/`java` find user-defined classes; see §4.5 |
| REPL | Read-Eval-Print Loop (`jshell`) |

## 6. Syntax and Language Rules

No new language syntax. Tool invocation patterns:

```bash
javac HelloWorld.java
java HelloWorld
javap -c HelloWorld
jshell
```

Class names for `java` omit `.class`. Package-qualified mains use dots: `java app.App`.

## 7. Step-by-Step Examples

### 7.1 Minimal — verify tools

Verified on this machine:

```text
java version "21.0.9" 2025-10-21 LTS
javac 21.0.9
JAVA_HOME=C:\Program Files\Java\jdk-21
```

### 7.2 Realistic — ask the running JVM

Runnable: [examples/beginner/runtime-info/](../examples/beginner/runtime-info/)

```java
public class RuntimeInfo {
    public static void main(String[] args) {
        System.out.println("java.version = " + System.getProperty("java.version"));
        System.out.println("java.home = " + System.getProperty("java.home"));
        System.out.println("os.name = " + System.getProperty("os.name"));
        System.out.println("user.dir = " + System.getProperty("user.dir"));
    }
}
```

**Verified output (excerpt):**

```text
java.version = 21.0.9
java.home = C:\Program Files\Java\jdk-21
os.name = Windows 11
```

`user.dir` is the working directory when the process started—not necessarily where the `.java` file lives forever after you move files.

### 7.3 Advanced awareness — `javap`

```bash
javac RuntimeInfo.java
javap -public RuntimeInfo
javap -c RuntimeInfo
```

`-public` lists public fields and methods (you should see `main`). `-c` adds bytecode instructions for each method—useful to see that `main` is a real method body, not magic. Treat listings as snapshots for your JDK/compiler; names and instruction details can differ slightly across versions.

## 8. Real-World Use Cases

- Onboarding: “Which JDK does this repo require?”
- CI matrices: pin Temurin 21 or Oracle 21
- Production: runtime image may be a slim JRE-like custom runtime (`jlink`)—advanced Module 48/49

## 9. Internal Behavior

`System.getProperty("java.home")` points at the JRE-equivalent root inside the JDK layout for modern JDKs. Exact directory layout is distribution-specific. HotSpot is the default VM in Oracle JDK builds used here.

## 10. Edge Cases and Limitations

- Multiple `java.exe` on `PATH` → surprising versions
- IDE bundled JDK ≠ terminal JDK
- `java -version` writes to stderr on many installs (still normal)

## 11. Common Mistakes

### Incorrect

Installing a browser-era “Java plugin” and expecting `javac`.

### Corrected

Install a **JDK 21** from an official vendor page.

### Incorrect

Assuming `java.version` always matches the `javac` you just used if `PATH` is messy.

### Corrected

Check both; align `JAVA_HOME` and `PATH` (next chapter).

## 12. Best Practices

- Pin a major version per project (21 for this handbook).
- Record `java -version` in bug reports.
- Prefer one clear JDK install for learning before juggling many.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Full JDK | Compile + tools | Larger download |
| Custom runtime (`jlink`) | Smaller deploy | Build complexity |
| Vendor A vs B | Support/licensing differences | Must read vendor docs |

## 14. Performance Considerations

Tool choice rarely dominates app performance. JVM flags and GC matter later (Modules 27–28)—not while learning `javac`.

## 15. Security Considerations

Download JDKs only from trusted vendors. Verify checksums in corporate environments. Don’t run untrusted JARs with your user privileges.

## 16. Java Version and Compatibility Notes

- Handbook primary: **Java 21 LTS**
- Latest LTS at research time: 25 (documented in appendix; not required here)
- Tools like `jshell` have existed since Java 9

## 17. Related Language Comparisons

Go ships a single `go` toolchain binary set; Java’s `javac`/`java` split mirrors compile-vs-run more explicitly. Not required to proceed.

## 18. Revision and Mastery

### Should already know

Difference between language level (`--release 21`) and the running JVM; multi-release JARs (awareness).

### Gaps

- IDE and CLI on different JDKs
- Confusing JVMS requirements with HotSpot flags
- Ignoring classpath vs module path until Module 29

### Questions

1. What does a JDK add over a pure runtime story?
2. Name five JDK tools and one sentence each.
3. Why might two terminals show different `java -version`?

## 19. Exercises

### Beginner

1. Run `java -version` and `javac -version`; paste results into notes.
2. Run [RuntimeInfo](../examples/beginner/runtime-info/).

### Intermediate

3. Run `javap -public java.lang.Object` and skim method names.
4. List every `java`/`javac` on your PATH (Windows: `where java`).

### Advanced

5. Research `--release` vs `-source/-target` in `javac` docs; explain when `--release` is safer.
6. Explain what `jlink` is for without installing it yet.

## 20. Interview Questions

1. **JDK vs JRE vs JVM?** — JVM executes; JDK develops; JRE was runtime packaging.  
2. **What is HotSpot?** — A widely used JVM implementation.  
3. **What does javac produce?** — `.class` bytecode files (and related artifacts).  
4. **Why record java.version in bugs?** — Behavior and bytecode compatibility depend on it.  

## 21. Solutions

1–2. Match your machine; handbook reference machine used 21.0.9 / jdk-21 home.  
3. You should see Object methods like `equals`, `hashCode`, `toString`.  
4. This machine listed Oracle `javapath` and `jdk-21\bin`.  
5. `--release` sets source/target/API against a platform version more consistently.  
6. `jlink` builds a custom runtime image with selected modules.

## 22. Chapter Summary

Develop against a JDK. The JVM runs bytecode; HotSpot is one implementation. Master `javac`/`java` first; grow into `javap`, `jshell`, and diagnostics as needed.

## 23. Key Terminology

JDK · JRE · JVM · HotSpot · distribution · classpath · javac · java · jshell

## 24. References and Further Reading

- [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/)
- [JDK 21 Tool Specifications](https://docs.oracle.com/en/java/javase/21/docs/specs/man/)
- [JDK 21 API](https://docs.oracle.com/en/java/javase/21/docs/api/)

## 25. Next Steps

[02-installing-java-and-environment.md](02-installing-java-and-environment.md)

## Review Checklist

- [x] Depth gate passed; RuntimeInfo verified on JDK 21.0.9
- [x] Spec vs HotSpot labeled
- [x] Revision and Mastery included
