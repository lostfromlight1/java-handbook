# Example: HelloWorld

## Purpose

Demonstrate the smallest complete Java program: a class with a `main` method that prints a line of text.

## Prerequisites

- JDK 21 installed (`javac` and `java` on your `PATH`)
- Chapter: [What Is Programming and What Is Java?](../../../00-programming-foundations/01-what-is-programming-and-what-is-java.md)

## Concepts demonstrated

- Source file vs bytecode (`.java` → `.class`)
- `public class` and `public static void main(String[] args)`
- `System.out.println`

## How to compile

From this directory:

```bash
javac HelloWorld.java
```

## How to run

```bash
java HelloWorld
```

## Expected output (verified)

```text
Hello, Java!
```

## Validation record

| Item | Value |
|------|-------|
| JDK | Oracle JDK 21.0.9 |
| `javac` | 21.0.9 |
| Date | 2026-11-10 |
| Result | Compiles and prints `Hello, Java!` |

Do not commit `HelloWorld.class`.
