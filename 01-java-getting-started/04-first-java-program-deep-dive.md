# First Java Program Deep Dive

> **Status:** Completed  
> **Module:** 01 — Introduction to Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain each part of a classic `main` method signature.
- Use command-line arguments safely and predictably.
- Distinguish normal completion from nonzero exit codes via `System.exit`.
- Read and write beginner-friendly messages on `stdout` vs `stderr`.
- Avoid common `main`-related mistakes (wrong name, wrong modifiers, wrong file name).

## 2. Prerequisites

- Chapters [01](01-jdk-jre-jvm-and-tooling.md)–[03](03-ide-and-command-line-workflow.md)
- Module 00 HelloWorld

## 3. Why This Concept Exists

HelloWorld proves the toolchain. This chapter makes the **entry point** intentional: arguments, failures, and process exit status—skills you need for CLIs and for understanding how servers/scripts launch Java processes.

## 4. Conceptual Explanation

### 4.1 The entry point contract

When you run `java MainDeepDive`, the JVM looks for a method roughly matching:

```java
public static void main(String[] args)
```

| Piece | Meaning |
|-------|---------|
| `public` | Accessible to the launcher |
| `static` | Callable without creating an object first |
| `void` | Does not return a value to the caller (use exit codes instead) |
| `main` | Conventional name the launcher seeks |
| `String[] args` | Command-line arguments as strings |

Exact language rules for launchable methods have expanded slightly over time (for example single-file source-code programs). This handbook’s primary path teaches the classic form first—the one you will see everywhere in production codebases.

### 4.2 Arguments

Everything after the class name becomes `args` elements:

```bash
java ArgsEcho hello Java 21
```

→ `args[0]="hello"`, `args[1]="Java"`, `args[2]="21"`.

Arguments are **strings**. Converting to numbers comes with Module 02 (`Integer.parseInt`, etc.).

### 4.3 Standard streams

| Stream | Java | Typical use |
|--------|------|-------------|
| stdout | `System.out` | Normal program output |
| stderr | `System.err` | Errors/diagnostics |
| stdin | `System.in` | Interactive input (later) |

Scripts can redirect them separately. Put usage errors on `stderr`.

### 4.4 Exit codes

By default, reaching the end of `main` successfully exits with status **0**. Calling `System.exit(code)` ends the JVM with that status. Nonzero conventionally means failure.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Entry point | Method where execution starts (`main`) |
| Exit code / status | Integer process result for the OS/scripts |
| stdout / stderr | Standard output / error streams |
| Launcher | The `java` tool that starts the JVM |

## 6. Syntax and Language Rules

```java
public class MainDeepDive {
    public static void main(String[] args) {
        // ...
    }
}
```

Rules that bite beginners:

- File name must match the public class name: `MainDeepDive.java`
- `String[] args` may also be written `String args[]` (older style)—prefer `String[] args`
- `main` must be `static` for the classic launch pattern

## 7. Step-by-Step Examples

### 7.1 Minimal — args echo

[ArgsEcho](../examples/beginner/args-echo/) verified:

```text
Argument count: 0
Tip: try java ArgsEcho hello Java 21
```

and with args:

```text
Argument count: 3
args[0] = hello
args[1] = Java
args[2] = 21
```

### 7.2 Realistic — validate args and exit codes

[MainDeepDive](../examples/beginner/main-deep-dive/):

```java
public class MainDeepDive {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java MainDeepDive <name>");
            System.exit(1);
            return;
        }
        String name = args[0].trim();
        if (name.isEmpty()) {
            System.err.println("Name must not be blank");
            System.exit(2);
            return;
        }
        System.out.println("Welcome, " + name + "!");
    }
}
```

**Verified (PowerShell `$LASTEXITCODE`):**

| Command | Output | Exit |
|---------|--------|------|
| `java MainDeepDive` | Usage on stderr | 1 |
| `java MainDeepDive ""` | Name must not be blank | 2 |
| `java MainDeepDive Ada` | `Welcome, Ada!` | 0 |

### 7.3 Advanced note — `return` after `exit`

`System.exit` terminates the JVM; `return` afterward is unreachable in practice but can satisfy readers/compilers about control flow. Prefer clear structure; Module 14 covers exceptions as another failure style for libraries (not always for process exit).

## 8. Real-World Use Cases

- CLI utilities (`git`-style commands)
- Container entrypoints: exit nonzero so orchestrators restart/fail the pod
- Passing config paths as args (prefer env/files for secrets—Module 43)

## 9. Internal Behavior

The launcher loads the class and invokes `main` using reflection-like internal mechanisms. Your program’s threads must finish (or you call `System.exit`) for the JVM to end—non-daemon threads can keep it alive (Module 24).

## 10. Edge Cases and Limitations

- Args with spaces need shell quoting
- Empty string arg `""` is length 1, not 0
- `System.exit` skips ordinary shutdown patterns you may want in larger apps (hooks exist—advanced)
- Single-file `java MainDeepDive.java` launch is convenient but not the multi-file mental model

## 11. Common Mistakes

### Incorrect

`public void main(...)` without `static`.

### Corrected

Add `static` (classic entry point).

### Incorrect

Putting usage help only on `System.out` and exiting 0 on failure.

### Corrected

`System.err` + nonzero exit code.

### Incorrect

Assuming args are already integers.

### Corrected

Parse intentionally later; validate failures.

## 12. Best Practices

- Document usage in the error message.
- Use distinct exit codes for distinct failure classes when scripts care.
- Keep `main` thin: parse args, then call other methods (Module 04).

## 13. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| Exit codes | Script-friendly | Easy to forget |
| Exceptions from `main` | Stack traces | Less clean for CLIs unless handled |
| Flags libraries | Rich CLIs | Dependency weight |

## 14. Performance Considerations

Irrelevant for arg parsing at learning scale.

## 15. Security Considerations

Treat args as untrusted input. Don’t pass them unsanitized into shells, SQL, or file paths (Modules 21, 33, 43).

## 16. Java Version and Compatibility Notes

Classic `main` is stable. Validated on **21.0.9**. Newer conveniences (compact source files in later releases) are documented in Module 19 when relevant—primary path stays classic.

## 17. Related Language Comparisons

Similar to `func main()` in Go or static `Main` in C#—every language has an entry ritual.

## 18. Revision and Mastery

### Questions

1. Why is `main` static?
2. Difference between returning from `main` and `System.exit(2)`?
3. When should libraries call `System.exit`? (Hint: almost never.)

## 19. Exercises

### Beginner

1. Run MainDeepDive with no args, blank arg, and a name.
2. Modify ArgsEcho to print args in reverse order.

### Intermediate

3. Accept exactly two args `min max` as strings and print them labeled (no parsing yet).
4. Use exit code 3 for “name too long” if length > 20.

### Advanced

5. Research `Runtime.addShutdownHook` and explain one use case.
6. Compare IDE Program arguments field vs CLI quoting on Windows PowerShell.

## 20. Interview Questions

1. **Signature of main?** — `public static void main(String[] args)`.  
2. **What is args?** — Command-line tokens as strings.  
3. **Exit code 0 vs nonzero?** — Success vs failure convention.  

## 21. Solutions

1. Match verified table above.  
2. Loop `i` from `args.length - 1` down to `0`.  
3. Check `args.length == 2`.  
4. After trim, if `name.length() > 20` exit 3.  
5. Cleanup on JVM shutdown (flush logs)—with caveats.  
6. Note how your shell parses quotes.

## 22. Chapter Summary

`main` is the process entry point: public, static, named `main`, taking `String[]`. Use args carefully, print errors to stderr, and signal failure with nonzero exit codes.

## 23. Key Terminology

Entry point · args · stdout · stderr · exit code · System.exit

## 24. References and Further Reading

- JLS: method declarations / application launch discussions in Java SE docs
- JDK `java` launcher documentation

## 25. Next Steps

[05-project-structure-and-jshell.md](05-project-structure-and-jshell.md)

## Review Checklist

- [x] ArgsEcho and MainDeepDive verified with real exit codes
- [x] Depth gate passed
- [x] Revision and Mastery included
