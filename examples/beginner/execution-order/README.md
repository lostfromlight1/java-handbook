# Example: ExecutionOrder

## Purpose

Show sequential execution and method-call nesting.

## Prerequisites

Module 00 Chapter 02; JDK 21

## Compile and run

```bash
javac ExecutionOrder.java
java ExecutionOrder
```

## Expected output (verified JDK 21.0.9)

```text
1. main starts
2. inside greet: Hello, Ada
3. main continues after greet returns
4. inside add
5. add returned: 5
6. main ends
```
