# First Java Program Deep Dive

> **Status:** Completed  
> **Module:** 01 — Introduction to Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (`main` contract, args, exit codes)  
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
| Examples | Design trade-offs *(strongly encouraged for Technical)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

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

When you run `java MainDeepDive`, the JVM loads `MainDeepDive` and looks for a method with a specific shape. The form you will see in almost every codebase is:

```java
public static void main(String[] args)
```

Read it **outside-in**, like a normal method declaration:

| Piece | Meaning |
|-------|---------|
| `public` | Any caller may invoke the method. The `java` launcher is outside your class, so the entry point must be visible—not `private` or package-private. |
| `static` | Belongs to the **class**, not to one object instance. The launcher calls `MainDeepDive.main(...)` without doing `new MainDeepDive()` first. (Instance methods need an object; `main` is the bootstrap.) |
| `void` | The method returns **no Java value** to its caller. Process success/failure is communicated separately via **exit codes** (§4.4), not via `return 0` like C. |
| `main` | The **conventional name** the application launcher searches for on the class you named on the command line. Wrong name ⇒ “main method not found” style failures. |
| `String[] args` | An array of **strings**: one element per command-line token **after** the class name. Shell quoting rules decide how tokens split; Java does not reinterpret them. |

**`String[]` syntax:** `String` is the element type (text); `[]` means array. You will also see the older equivalent `String args[]`; prefer `String[] args` for consistency with modern style.

**Class and file name:** A `public` class `MainDeepDive` must live in `MainDeepDive.java`. The launcher’s class argument matches the public class name, not the file path string with slashes.

Exact language rules for launchable methods have expanded slightly over time (for example single-file source-code programs on the command line). This handbook’s primary path teaches the classic form first—the one servers, libraries, and build tools assume.

### 4.2 Arguments

Everything after the class name becomes `args` elements:

```bash
java ArgsEcho hello Java 21
```

→ `args[0]="hello"`, `args[1]="Java"`, `args[2]="21"`.

Arguments are always **strings**, even when they look like numbers. The launcher does not parse integers or booleans for you. If you need an `int`, parse explicitly and handle bad input:

```java
public class AgePreview {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java AgePreview <age>");
            System.exit(1);
        }
        try {
            int age = Integer.parseInt(args[0].trim());
            System.out.println("Next year: " + (age + 1));
        } catch (NumberFormatException e) {
            System.err.println("Not a valid integer: " + args[0]);
            System.exit(1);
        }
    }
}
```

`Integer.parseInt` is part of the standard library (Module 02 covers parsing, validation, and better error messages in depth). The pattern here: **validate length → trim if needed → parse → catch format errors**.

Shell note: `java AgePreview 21` passes the string `"21"`, which parses fine; `java AgePreview twenty-one` fails parsing unless you add custom logic.

### 4.3 Standard streams

| Stream | Java | Typical use |
|--------|------|-------------|
| stdout | `System.out` | Normal program output |
| stderr | `System.err` | Errors/diagnostics |
| stdin | `System.in` | Interactive input (later) |

Scripts can redirect them separately. Put usage errors on `stderr`.

### 4.4 Exit codes

When the JVM process ends, the operating system receives an **exit code** (also called **status code**): a small integer. Shells and automation use it to decide whether a step succeeded.

| Convention | Meaning |
|------------|---------|
| **0** | Success—`main` returned normally without calling `System.exit` with another value, or you called `System.exit(0)`. |
| **Nonzero** (often 1, 2, …) | Failure or abnormal completion; exact meaning is **your program’s contract** unless you document otherwise. |

`System.exit(int)` **immediately** terminates the entire JVM with that code—use it at the top level of command-line apps when usage is wrong. Libraries and reusable components usually **throw exceptions** instead of exiting, so callers can recover (Module 14).

**Observing exit codes:** After `java MainDeepDive` in PowerShell, check `$LASTEXITCODE`; in Bash, `echo $?`. Scripts chain commands with `&&` only when exit code is 0. The MainDeepDive example (§8.2) uses **1** for missing args and **2** for a blank name so scripts can distinguish cases if needed.

## 5. Mechanism and Execution Flow

From the OS’s view, launching `java MainDeepDive arg1 arg2` is one process. Inside the JVM, the classic entry path is:

1. **Tokenize** — The launcher splits the command line: class name `MainDeepDive`, remaining tokens become the `String[] args` array (`args.length` equals token count; each element is a `String`, never pre-parsed numbers).
2. **Load class** — Binary name `MainDeepDive` must match the public class in `MainDeepDive.java` (Module 01 Ch.01 classpath rules apply when packages appear).
3. **Resolve `main`** — The JVM searches for `public static void main(String[] args)`. Each keyword is required for this contract:
   - **`public`** — launcher is outside the class;
   - **`static`** — no instance exists yet;
   - **`void`** — no Java return value (exit code is separate);
   - **`main`** — conventional name the launcher probes;
   - **`String[] args`** — array of argument strings (equivalent `String args[]` form is legal but non-idiomatic).
4. **Invoke body** — Your code runs on the main thread; `System.out` / `System.err` map to OS streams.
5. **Terminate** — Normal `return` from `main` yields exit **0**; `System.exit(n)` ends the whole JVM immediately with **n**.

**Parsing preview:** When a program needs numbers, it calls library parsers (`Integer.parseInt`, etc.) **inside** `main` or helpers—after checking `args.length` and handling `NumberFormatException` (see §4.2 `AgePreview`). The launcher never does that step for you.

## 6. Terminology

| Term | Meaning |
|------|---------|
| Entry point | Method where execution starts (`main`) |
| Exit code / status | Integer process result for the OS/scripts |
| stdout / stderr | Standard output / error streams |
| Launcher | The `java` tool that starts the JVM |

## 7. Syntax and Language Rules

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

## 8. Step-by-Step Examples

### 8.1 Minimal — args echo

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

### 8.2 Realistic — validate args and exit codes

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

**Verified** (Temurin JDK 21.0.12.1; PowerShell `$LASTEXITCODE`):

| Command | Output | Exit |
|---------|--------|------|
| `java MainDeepDive` | Usage on stderr | 1 |
| `java MainDeepDive ""` | Name must not be blank | 2 |
| `java MainDeepDive Ada` | `Welcome, Ada!` | 0 |

### 8.3 Advanced note — `return` after `exit`

`System.exit` terminates the JVM; `return` afterward is unreachable in practice but can satisfy readers/compilers about control flow. Prefer clear structure; Module 14 covers exceptions as another failure style for libraries (not always for process exit).

## 9. Real-World Use Cases

- CLI utilities (`git`-style commands)
- Container entrypoints: exit nonzero so orchestrators restart/fail the pod
- Passing config paths as args (prefer env/files for secrets—Module 43)

## 10. Internal Behavior

The launcher loads the class and invokes `main` using reflection-like internal mechanisms. Your program’s threads must finish (or you call `System.exit`) for the JVM to end—non-daemon threads can keep it alive (Module 24).

## 11. Edge Cases and Limitations

- Args with spaces need shell quoting
- Empty string arg `""` is length 1, not 0
- `System.exit` skips ordinary shutdown patterns you may want in larger apps (hooks exist—advanced)
- Single-file `java MainDeepDive.java` launch is convenient but not the multi-file mental model

## 12. Common Mistakes

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

## 13. Best Practices

- Document usage in the error message.
- Use distinct exit codes for distinct failure classes when scripts care.
- Keep `main` thin: parse args, then call other methods (Module 04).

## 14. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| Exit codes | Script-friendly | Easy to forget |
| Exceptions from `main` | Stack traces | Less clean for CLIs unless handled |
| Flags libraries | Rich CLIs | Dependency weight |

## 15. Performance Considerations

Irrelevant for arg parsing at learning scale.

## 16. Security Considerations

Treat args as untrusted input. Don’t pass them unsanitized into shells, SQL, or file paths (Modules 21, 33, 43).

## 17. Java Version and Compatibility Notes

Classic `main` is stable. Validated on **21.0.9**. Newer conveniences (compact source files in later releases) are documented in Module 19 when relevant—primary path stays classic.

## 18. Related Language Comparisons

Similar to `func main()` in Go or static `Main` in C#—every language has an entry ritual.

## 19. Revision and Mastery

### Questions

1. Why is `main` static?
2. Difference between returning from `main` and `System.exit(2)`?
3. When should libraries call `System.exit`? (Hint: almost never.)

## 20. Exercises

### Beginner

1. Run MainDeepDive with no args, blank arg, and a name.
2. Modify ArgsEcho to print args in reverse order.

### Intermediate

3. Accept exactly two args `min max` as strings and print them labeled (no parsing yet).
4. Use exit code 3 for “name too long” if length > 20.

### Advanced

5. Research `Runtime.addShutdownHook` and explain one use case.
6. Compare IDE Program arguments field vs CLI quoting on Windows PowerShell.

## 21. Interview Questions

1. **Signature of main?** — `public static void main(String[] args)`.  
2. **What is args?** — Command-line tokens as strings.  
3. **Exit code 0 vs nonzero?** — Success vs failure convention.  

## 22. Solutions

1. Match verified table above.  
2. Loop `i` from `args.length - 1` down to `0`.  
3. Check `args.length == 2`.  
4. After trim, if `name.length() > 20` exit 3.  
5. Cleanup on JVM shutdown (flush logs)—with caveats.  
6. Note how your shell parses quotes.

## 23. Chapter Summary

`main` is the process entry point: public, static, named `main`, taking `String[]`. Use args carefully, print errors to stderr, and signal failure with nonzero exit codes.

## 24. Key Terminology

Entry point · args · stdout · stderr · exit code · System.exit

## 25. References and Further Reading

- JLS: method declarations / application launch discussions in Java SE docs
- JDK `java` launcher documentation

## 26. Next Steps

[05-project-structure-and-jshell.md](05-project-structure-and-jshell.md)

## Review Checklist

**Mandatory Depth Review:** Technical profile; entry point contract at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained (§5 launcher → main → exit)
- [x] First-appearance concepts explained now (`main` keywords, `parseInt` preview in §4.2/§5)
- [x] Terminology defined before use (§6)
- [x] Examples include sufficient context to compile and run
- [x] Verification status accurately labeled (Verified / Compile-verified / Illustrative)
- [x] Explanations distinguish specification guarantees from implementation details (§10)
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included (§19)
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-10)

**Omissions (section → reason):**

- **Mermaid diagrams** — omitted; §5 ordered list is sufficient for launch contract at this depth.
- **§ Internal Behavior (deep reflection)** — kept brief in §10; full launcher internals deferred to JVM/module material.
