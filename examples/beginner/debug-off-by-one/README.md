# Example: Off-by-one bug and fix

## Purpose

Logic bug from wrong loop bounds; corrected version for comparison.

## Prerequisites

Module 00 Chapter 05; JDK 21

## Compile and run

```bash
javac OffByOneBug.java OffByOneFixed.java
java OffByOneBug
java OffByOneFixed
```

## Expected output (verified JDK 21.0.9)

```text
sumUpTo(5) = 10
sumUpTo(5) = 15
```

Correct sum for `n = 5` is 15. The bug skips `n` because of `i < n`.
