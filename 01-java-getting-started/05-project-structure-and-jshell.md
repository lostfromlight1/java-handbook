# Project Structure and JShell

> **Status:** Completed  
> **Module:** 01 — Introduction to Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain packages and how they map to directories.
- Compile and run a small multi-file program with `-d` and `-cp`.
- Describe a simple project layout before Maven/Gradle.
- Use **JShell** for tiny experiments without a full class file.
- Know what build tools will later standardize (preview of Module 29).

## 2. Prerequisites

- [04-first-java-program-deep-dive.md](04-first-java-program-deep-dive.md)
- Comfortable with `javac` / `java`

## 3. Why This Concept Exists

Real programs are not one file forever. Packages prevent name collisions and organize code. Classpath rules decide what the JVM can load. JShell shortens the feedback loop for trying language ideas. Master these before Spring Boot project wizards hide them.

## 4. Conceptual Explanation

### 4.1 Packages

A **package** is a namespace for types. Declaration:

```java
package app;
```

Convention: reverse-domain style later (`com.example.app`). For learning demos, short names like `app` and `util` are fine.

**Directory rule:** source file `app/App.java` declares `package app;` — folders mirror package names.

### 4.2 Compiling to an output tree

```bash
javac -d out app/App.java util/Greeter.java
```

`-d out` writes class files into `out/app/App.class` and `out/util/Greeter.class`, preserving package structure.

### 4.3 Classpath

`-cp` (or `-classpath`) tells the JVM where to find classes:

```bash
java -cp out app.App Handbook
```

Note the **dotted** main class name `app.App`, not a path with slashes.

```mermaid
flowchart LR
  src[Source packages]
  outdir[out directory tree]
  jvm[java -cp out]
  src -->|javac -d out| outdir --> jvm
```

Default classpath often includes the current directory (`.`), which is why single-file demos work without `-cp`. Multi-package layouts need explicit discipline.

### 4.4 Simple project layout (pre-Maven)

```text
classpath-demo/
  app/App.java
  util/Greeter.java
  out/                 (generated; do not commit)
```

Later, Maven/Gradle use `src/main/java` and `target`/`build` (Module 29). Same package idea; more conventions.

### 4.5 JShell

**JShell** is the JDK’s REPL: type snippets, see results, no `main` required.

```text
jshell
jshell> 1 + 1
$1 ==> 2
```

Use it to try expressions, small loops, and API calls. Still learn real classes/projects—the REPL is a lab bench, not a substitute for program structure.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Package | Namespace for classes/interfaces |
| Classpath | Search path for user classes |
| `-d` | javac output directory root |
| REPL | Interactive evaluation loop |
| Artifact / JAR | Packaged classes (later) |

## 6. Syntax and Language Rules

```java
package util;

public class Greeter {
    public static String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

```java
package app;

import util.Greeter;

public class App {
    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : "world";
        System.out.println(Greeter.greet(name));
    }
}
```

`import` lets you write `Greeter` instead of `util.Greeter`. It does **not** load code by itself—classpath still must contain `util.Greeter`.

## 7. Step-by-Step Examples

### 7.1 Minimal JShell

Verified: JShell is available with JDK 21 on the handbook machine. Interactive evaluation of `1 + 1` yields `2`.

### 7.2 Realistic — multi-file classpath demo

Sources: [examples/beginner/classpath-demo/](../examples/beginner/classpath-demo/)

```bash
javac -d out app/App.java util/Greeter.java
java -cp out app.App Handbook
```

**Verified output:**

```text
Hello, Handbook!
```

### 7.3 Incorrect classpath (predict)

Running `java app.App` from the wrong directory without `-cp out` fails with `Could not find or load main class`. Fix: `-cp out` from the demo root (or `cd out` and `java app.App` when classes are rooted there).

## 8. Real-World Use Cases

- Every Maven module’s `src/main/java`
- Splitting API vs implementation packages
- Quick math/API checks in JShell during debugging

## 9. Internal Behavior

The JVM loads classes by binary name (`util.Greeter`) using class loaders that consult the classpath/module path (Module 26). Package directories on disk match binary names for ordinary directory-based classpaths.

## 10. Edge Cases and Limitations

- Default package (no `package` line) is OK for tiny demos; avoid it in real projects
- JAR packaging changes how classpath entries look (`-cp app.jar`)
- Module path (`JPMS`) adds another dimension—Module 29

## 11. Common Mistakes

### Incorrect

`package app;` but file lives in the wrong folder.

### Corrected

Match directory and package.

### Incorrect

`java out/app/App` 

### Corrected

`java -cp out app.App`

### Incorrect

Assuming `import` copies source into your file.

### Corrected

`import` is a compile-time name shortcut; classes load via classpath.

## 12. Best Practices

- One public top-level class per file; file name matches class.
- Generate classes into `out/` / `target/`; don’t commit `.class` files.
- Use JShell for experiments; commit real programs as `.java` files.

## 13. Design Trade-offs

| Layout | Benefit | Cost |
|--------|---------|------|
| Flat default package | Fast demos | Collisions; poor scaling |
| Packages + `-d out` | Clear | More commands |
| Maven/Gradle | Standard | Learning curve (Module 29) |

## 14. Performance Considerations

Classpath size can affect startup in huge apps; irrelevant for these demos.

## 15. Security Considerations

Never put untrusted directories first on the classpath (class shadowing). Production uses build-managed dependencies (Module 29, 43).

## 16. Java Version and Compatibility Notes

Packages and classpath are foundational. JShell since Java 9. Validated on **21.0.9**.

## 17. Related Language Comparisons

Go’s package directories differ (all files in a directory share a package). Don’t assume Go rules in Java.

## 18. Revision and Mastery

### Questions

1. What does `import` not do?
2. Why `-d out` plus `-cp out`?
3. When is JShell the wrong tool?

## 19. Exercises

### Beginner

1. Run the classpath-demo commands; change the argument name.
2. Start `jshell`, evaluate `"hi".length()`, exit with `/exit`.

### Intermediate

3. Add `util/LoudGreeter.java` and call it from `App`.
4. Create a JAR with `jar` (optional stretch) and run with `-cp app.jar`.

### Advanced

5. Draw how `app.App` references `util.Greeter` from class files on disk.
6. Read JDK docs for `--class-path` vs `--module-path` and write three bullet differences.

## 20. Interview Questions

1. **What is a package?** — Namespace mirroring directories.  
2. **What is the classpath?** — Where the JVM looks for user classes.  
3. **What is JShell?** — Official Java REPL.  

## 21. Solutions

1. Output becomes `Hello, <name>!`.  
2. Should print `2` for `"hi".length()`.  
3. New class + import/call.  
4. `jar cf app.jar -C out .` then `java -cp app.jar app.App`.  
5. `out/app/App.class` and `out/util/Greeter.class`.  
6. Classpath vs modules: JARs vs `module-info`, explicit reads/exports, etc.

## 22. Chapter Summary

Packages organize types into directories. Compile with `-d`, run with `-cp` and dotted main names. JShell accelerates tiny experiments; build tools will later standardize layout.

## 23. Key Terminology

Package · import · classpath · `-d` · JShell · REPL

## 24. References and Further Reading

- JDK tools: `javac`, `java`, `jshell`, `jar` manuals (Java 21)
- Next module: [02-java-fundamentals](../02-java-fundamentals/README.md)
- Later: Module 29 build tools / JPMS

## 25. Next Steps

Module 01 assessment, then Module 02 fundamentals (variables, types, operators).

## Review Checklist

- [x] classpath-demo verified (`Hello, Handbook!`)
- [x] JShell availability verified
- [x] Depth gate passed
- [x] Revision and Mastery included
