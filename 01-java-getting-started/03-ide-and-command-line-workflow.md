# IDE and Command-Line Workflow

> **Status:** Completed  
> **Module:** 01 — Introduction to Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Contrast learning on the command line with working in an IDE (IntelliJ IDEA as the primary example).
- Perform the core CLI edit → compile → run loop confidently.
- Configure an IDE to use JDK 21 and run a main class.
- Explain what the IDE automates (classpath, compile, run configurations).
- Choose CLI or IDE deliberately for each learning task.

## 2. Prerequisites

- [02-installing-java-and-environment.md](02-installing-java-and-environment.md)
- Ability to compile HelloWorld in a terminal

## 3. Why This Concept Exists

IDEs hide important mechanics. If you only click green arrows, classpath and compile errors feel like magic. If you only use Notepad forever, you miss navigation and debugging aids. Professionals use **both**: CLI for truth and scripts; IDE for productivity.

## 4. Conceptual Explanation

### 4.1 Command-line workflow

```text
edit source → javac → java → observe output/errors → repeat
```

**Why use the CLI early:** Every IDE ultimately invokes the same kind of compile and launch steps. On the command line you see the **exact** classpath, class name, and working directory that succeeded or failed—so “works on my machine” bugs map to real flags instead of hidden GUI state.

Benefits:

- **Reproducibility** — CI servers, Docker images, and SSH boxes rarely have your IDE; they run `mvn`, `gradle`, or raw `javac`/`java`.
- **Ground truth** — Directory names, package dots vs slashes, and `-cp` mistakes surface as compiler/launcher errors you learn to read once.
- **Sharing** — Paste a three-line command into a ticket; teammates can rerun it without your project file.

Costs:

- No completion or refactor shortcuts; typos cost more until you are comfortable.
- You must set **classpath** and **working directory** yourself (see below).

**Classpath (in this chapter):** The list of folders/JARs where the JVM finds your compiled classes. In the CLI you pass `-cp out` explicitly; in an IDE the **module/content root** and build settings assemble the same idea behind the scenes. If the IDE runs but the terminal fails, compare IDE “classpath” or module output path to your `-cp`.

**Working directory:** The folder the OS treats as “current” when the process starts. Java exposes it as `System.getProperty("user.dir")`. Relative paths in your code (`new File("data.txt")`) and some launcher behavior depend on it—not on where your `.java` source file lives. CLI: whatever directory you `cd` to before `java`. IDE: the **Working directory** field on the Run configuration (often the project root, but you can change it).

### 4.2 IDE workflow (IntelliJ IDEA)

An **IDE** (Integrated Development Environment) combines editor, project model, build/run, debugger, and inspections.

Typical IntelliJ flow for a simple lesson:

1. Create/open a project
2. Set **Project SDK** to JDK 21
3. Create a class with `main`
4. Run via gutter icon or Run configuration
5. Read Run tool window output

IntelliJ is recommended in this handbook’s curriculum list; VS Code + Java extensions or Eclipse also work if you keep the same JDK 21 discipline.

### 4.3 What the IDE automates

| Concern | CLI | IDE |
|---------|-----|-----|
| Finding classes | `-cp` / working dir | Module/content roots |
| Recompile | You run `javac` | Build on run/save |
| Main class | You type it | Run configuration |
| Stack traces | Terminal | Clickable navigation |

Automation is good—as long as you can still do the CLI version.

### 4.4 When to prefer which (and why)

| Prefer CLI when… | Why |
|------------------|-----|
| Learning compile/run fundamentals | You see `javac`/`java` errors and flags directly; no hidden defaults. |
| Reproducing handbook or bug reports | Commands are copy-paste portable across machines. |
| Debugging `PATH`, `JAVA_HOME`, or wrong JDK | IDEs can point at a different SDK than your shell. |
| Authoring CI/build scripts | Pipelines mirror terminal invocations. |

| Prefer IDE when… | Why |
|------------------|-----|
| Navigating many classes/packages | Jump to definition, hierarchy, and search scale past manual `grep`. |
| Refactoring/renames across files | IDE updates references; CLI alone does not. |
| Breakpoint debugging (Module 31) | Inspect variables and step without sprinkling `println`. |
| Exploring unfamiliar APIs | Completion and inline Javadoc reduce doc-tab churn. |

Professionals **switch deliberately**: reproduce failures on the CLI, then return to the IDE for editing at scale—keeping the same JDK 21 in both places.

## 5. Terminology

| Term | Meaning |
|------|---------|
| IDE | Integrated Development Environment |
| Project SDK / JDK | IDE’s configured Java platform |
| Run configuration | Saved “what to run / with which args” |
| Working directory | OS “current folder” for the process; becomes `user.dir` in Java |
| Classpath | Where the JVM finds your compiled classes; CLI: `-cp`; IDE: module/output paths |
| Source root | Folder the IDE treats as Java source |

## 6. Syntax and Language Rules

No new Java syntax. CLI pattern:

```bash
cd path/to/examples/beginner/args-echo
javac ArgsEcho.java
java ArgsEcho one two
```

## 7. Step-by-Step Examples

### 7.1 Minimal CLI loop

Use [ArgsEcho](../examples/beginner/args-echo/):

```bash
javac ArgsEcho.java
java ArgsEcho hello Java 21
```

**Verified:**

```text
Argument count: 3
args[0] = hello
args[1] = Java
args[2] = 21
```

### 7.2 Realistic — IDE checklist

1. Install IntelliJ IDEA (Community is enough for this module).
2. New Project → Language Java → JDK 21.
3. Create `ArgsEcho` with the same source as the example.
4. Run → Edit Configurations → Program arguments: `hello Java 21`.
5. Confirm output matches CLI.

If output differs, compare IDE SDK and working directory to the terminal.

### 7.3 Advanced — same bug, two views

Introduce a typo (`System.out.printl`) and compare:

- `javac` error text in terminal
- IDE red gutter + inspection message

Same failure; different presentation.

## 8. Real-World Use Cases

- Local feature work in IDE; CI compile with Maven/Gradle CLI
- Pairing: share CLI reproduction steps in tickets
- Teaching juniors fundamentals before IDE magic

## 9. Internal Behavior

IDEs maintain an internal project model and often invoke the same compiler APIs/`javac`-equivalent toolchain under the hood. They are not a different Java language. Misconfigured SDK ⇒ different language level or libraries.

## 10. Edge Cases and Limitations

- “Works in IDE, fails in CI” → missing files, wrong root, or env-only IDE settings
- Preview features enabled in IDE but not in CLI
- Multiple modules confuse beginners—stay single-module until Module 29

## 11. Common Mistakes

### Incorrect

Never looking at the raw `javac` error.

### Corrected

Reproduce once on CLI when confused.

### Incorrect

Running the wrong main class from a stale Run configuration.

### Corrected

Check configuration’s main class and module/classpath.

## 12. Best Practices

- Keep handbook examples runnable from their folders via CLI.
- Align IDE JDK with handbook 21.
- Commit sources, not IDE caches (`.idea` often gitignored here).

## 13. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| IDE-only | Speed | Weaker mental model |
| CLI-only | Clarity | Slower on large code |
| Hybrid | Best of both | Discipline to switch |

## 14. Performance Considerations

IDE indexing uses RAM/CPU; irrelevant to Java program complexity—don’t confuse them.

## 15. Security Considerations

IDE plugins add supply-chain risk—install deliberately. Don’t paste secrets into Run configurations that get shared.

## 16. Java Version and Compatibility Notes

IntelliJ versions evolve; always set Project SDK explicitly to **21** for this handbook.

## 17. Related Language Comparisons

Similar to VS Code + Go or Visual Studio + C#: GUI productivity over a CLI toolchain you should still understand.

## 18. Revision and Mastery

### Questions

1. Name three things an IDE Run configuration stores.
2. Why might IDE and CLI disagree on `user.dir`?
3. When is CLI mandatory in professional workflows?

## 19. Exercises

### Beginner

1. Run ArgsEcho on CLI with two arguments.
2. Create the same class in an IDE and run with the same arguments.

### Intermediate

3. Break a print statement; capture CLI and IDE diagnostics.
4. Change working directory in a Run configuration and print `user.dir` via RuntimeInfo.

### Advanced

5. Export/share a run configuration carefully without secrets.
6. Document how your IDE shows the exact `java` command line it uses (IntelliJ: modify options / print command line if available).

## 20. Interview Questions

1. **Why learn CLI if IDEs exist?** — CI/servers, reproduction, fundamentals.  
2. **What is a Run configuration?** — Saved launch settings for a main/app.  
3. **IDE vs compiler?** — IDE orchestrates; compiler still checks language rules.  

## 21. Solutions

1. Example: main class, args, working dir, JRE, env vars.  
2. Different cwd settings.  
3. CI, SSH servers, containers, scripts.  
4–6. Environment-specific—write what you observed.

## 22. Chapter Summary

Use the CLI to understand truth; use an IDE for speed. Keep JDK 21 aligned in both. Reproduce confusing failures on the command line.

## 23. Key Terminology

IDE · Project SDK · Run configuration · source root · working directory

## 24. References and Further Reading

- IntelliJ IDEA help: configuring JDK / run applications (JetBrains docs)
- JDK tool docs for `javac` / `java`

## 25. Next Steps

[04-first-java-program-deep-dive.md](04-first-java-program-deep-dive.md)

## Review Checklist

- [x] CLI example verified (ArgsEcho)
- [x] IDE guidance without requiring one vendor exclusively
- [x] Revision and Mastery included
