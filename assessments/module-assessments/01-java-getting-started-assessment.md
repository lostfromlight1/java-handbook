# Assessment: Module 01 — Introduction to Java

> **Type:** Module  
> **Maps to:** Module 01 chapters 01–05  
> **Time guide:** 60–90 minutes  
> **Solutions:** [01-java-getting-started-assessment-solutions.md](01-java-getting-started-assessment-solutions.md)

## Purpose

Verify JDK tooling, environment setup, CLI/IDE workflow, `main`/args/exit codes, packages/classpath, and JShell awareness.

## Part A — Conceptual

1. Explain JDK vs JVM vs JRE in plain language.
2. What should `JAVA_HOME` point to? What goes on `PATH`?
3. Name four JDK command-line tools and one purpose each.
4. Why learn the CLI if you use IntelliJ?
5. Explain each part of `public static void main(String[] args)`.
6. When do you use `System.err` and a nonzero exit code?
7. What is a package, and how does it relate to directories?
8. What does `import` do and *not* do?

## Part B — Predict / environment

9. You have two `java.exe` entries on `PATH`. Which one runs? How do you check on Windows?
10. Predict output: `java ArgsEcho a "b c"` — how many args if the shell preserves the quote grouping?
11. Why might `java app.App` fail even though `out/app/App.class` exists?

## Part C — Debugging

12. `javac` works but `java Main` says could not find/load main class. List three checks.
13. Program prints usage help but scripts treat the run as success. What is wrong?

## Part D — Coding

14. Write `SumTwo` that expects exactly two integer-looking args, prints their sum as text concatenation for now **or** parse with `Integer.parseInt` if you already know it; on wrong arity print usage to stderr and exit 1.
15. Create two packages `lab` and `lab.util` with a greeter method and a main that calls it; show compile/run commands using `-d` and `-cp`.

## Part E — Advanced (optional)

16. What is JShell good for, and when is it the wrong tool?
17. Explain one risk of an untrusted entry early on the classpath.

## Scoring guide

| Band | Next step |
|------|-----------|
| Strong | Module 02 |
| Partial | Re-read mapped chapters; redo weak parts |
| Weak | Reinstall/verify JDK; redo all Module 01 examples |

## Chapter mapping

| Q | Chapter |
|---|---------|
| 1, 3 | 01 |
| 2, 9 | 02 |
| 4 | 03 |
| 5, 6, 10, 13–14 | 04 |
| 7, 8, 11–12, 15–17 | 05 |
