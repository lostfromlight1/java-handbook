# Solutions: Module 01 — Introduction to Java Assessment

## Part A

1. **JVM** executes bytecode. **JDK** includes compiler/tools + runtime for development. **JRE** historically meant runtime-focused packaging; install a JDK for this handbook.
2. `JAVA_HOME` = JDK root (contains `bin`). `PATH` should include `JAVA_HOME/bin` (or equivalent).
3. Examples: `javac` compile; `java` run; `javap` disassemble; `jshell` REPL; `jar` archives; `jcmd` diagnostics.
4. CI/servers/scripts use CLI; CLI teaches classpath/compile mechanics IDEs hide.
5. `public` accessible; `static` no instance needed; `void` no return value; `main` name; `String[] args` CLI tokens.
6. Usage/errors on stderr; nonzero exit tells scripts/orchestrators the process failed.
7. Package = namespace; directories mirror package segments.
8. `import` shortens names at compile time; it does not by itself put classes on the classpath.

## Part B

9. The first matching executable on `PATH`. Check with `where java` (Windows) / `which -a java` (Unix).
10. Typically 2 args: `a` and `b c` when quotes group—shell-dependent; verify on your shell.
11. Wrong classpath/working directory—need `java -cp out app.App` (or run from `out` correctly).

## Part C

12. Wrong directory; wrong class name/package; missing `-cp`; stale/wrong `.class`; casing issues.
13. Exited 0 after printing help—should `System.exit` nonzero and prefer `System.err`.

## Part D

14. Example shape (with parsing):

```java
public class SumTwo {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java SumTwo <a> <b>");
            System.exit(1);
            return;
        }
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        System.out.println(a + b);
    }
}
```

(If you only concatenate strings, say so—parsing is Module 02 territory but acceptable here.)

15. Example commands:

```bash
javac -d out lab/Main.java lab/util/Hi.java
java -cp out lab.Main
```

## Part E

16. Good: tiny experiments. Wrong: shipping apps, multi-file design, tests as substitute.
17. Class shadowing / malicious classes loaded instead of trusted ones.
