# Glossary

Shared terminology for this handbook. Prefer linking here instead of redefining terms inconsistently.

| Term | Definition | Introduced |
|------|------------|------------|
| **Programming** | Writing precise instructions that a computer can execute to solve a problem or perform a task. | [Module 00 Ch.01](00-programming-foundations/01-what-is-programming-and-what-is-java.md) |
| **Source code** | Human-readable program text written in a programming language. | Module 00 Ch.01 |
| **Compiler** | A program that translates source code into another form (for Java: into bytecode). | Module 00 Ch.01 |
| **Bytecode** | Platform-neutral instructions stored in `.class` files; executed by a JVM. | Module 00 Ch.01 |
| **JDK** | Java Development Kit — tools and libraries for developing Java programs (includes compiler). | Module 00 Ch.01 |
| **JRE** | Java Runtime Environment — components needed to run Java applications. | Module 00 Ch.01 |
| **JVM** | Java Virtual Machine — runtime that loads bytecode and executes it. | Module 00 Ch.01 |
| **Algorithm** | A finite, unambiguous sequence of steps to solve a problem. | Module 00 Ch.01 |
| **HotSpot** | Oracle’s widely used JVM implementation (implementation detail, not the language itself). | Module 00 Ch.01 |
| **LTS** | Long-Term Support Java release (e.g. 8, 11, 17, 21, 25). | [Version history](appendix/java-version-history.md) |
| **Process** | OS-managed running instance of a program. | [Module 00 Ch.02](00-programming-foundations/02-how-computers-execute-programs.md) |
| **Thread** | A sequence of execution within a process. | Module 00 Ch.02 |
| **Call stack** | Structure tracking nested method calls for a thread. | Module 00 Ch.02 |
| **Pseudocode** | Informal structured description of an algorithm. | [Module 00 Ch.03](00-programming-foundations/03-algorithms-and-problem-solving.md) |
| **Edge case** | Unusual but valid input that often breaks naive code. | Module 00 Ch.03 |
| **State** | The data a program (or component) holds at a moment in time. | [Module 00 Ch.04](00-programming-foundations/04-data-and-basic-structures.md) |
| **Array** | Fixed-length indexed sequence of elements of one type. | Module 00 Ch.04 |
| **Big-O** | Notation classifying how cost grows as input size grows. | [Module 00 Ch.05](00-programming-foundations/05-complexity-and-debugging-mindset.md) |
| **Off-by-one** | Boundary error in loops or indexes (wrong by a single step). | Module 00 Ch.05 |
| **Logic bug** | Program runs without crashing but produces incorrect results. | Module 00 Ch.05 |
| **PATH** | OS list of directories searched for executables. | [Module 01 Ch.02](01-java-getting-started/02-installing-java-and-environment.md) |
| **JAVA_HOME** | Environment variable pointing at the JDK root directory. | Module 01 Ch.02 |
| **IDE** | Integrated Development Environment (editor + project + run/debug). | [Module 01 Ch.03](01-java-getting-started/03-ide-and-command-line-workflow.md) |
| **Exit code** | Integer status a process returns to the OS (0 often means success). | [Module 01 Ch.04](01-java-getting-started/04-first-java-program-deep-dive.md) |
| **Package** | Namespace for Java types; usually mirrors directories. | [Module 01 Ch.05](01-java-getting-started/05-project-structure-and-jshell.md) |
| **Classpath** | Locations where the JVM searches for user classes. | Module 01 Ch.05 |
| **JShell** | Official Java REPL included with the JDK. | Module 01 Ch.05 |
| **Primitive type** | One of Java’s eight built-in non-reference value types. | [Module 02 Ch.01](02-java-fundamentals/01-variables-and-types.md) |
| **Literal** | Source-code representation of a fixed value (e.g. `42`, `"hi"`). | Module 02 Ch.01 |
| **Expression** | Code that produces a value. | [Module 02 Ch.02](02-java-fundamentals/02-operators-and-expressions.md) |
| **Short-circuit** | Skipping evaluation of the right operand when the result is already known. | Module 02 Ch.02 |
| **Scope** | Region of a program where a declared name is visible. | [Module 02 Ch.03](02-java-fundamentals/03-scope-naming-and-comments.md) |
| **Widening** | Numeric conversion to a wider type without a cast. | [Module 02 Ch.04](02-java-fundamentals/04-type-conversion-and-inference.md) |
| **Narrowing** | Numeric conversion that may lose information; requires a cast. | Module 02 Ch.04 |
| **var** | Local-variable type inference (Java 10+); still statically typed. | Module 02 Ch.04 |

Add new terms when chapters introduce them. Keep definitions beginner-accessible and technically precise.
