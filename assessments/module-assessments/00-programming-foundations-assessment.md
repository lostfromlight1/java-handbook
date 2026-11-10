# Assessment: Module 00 — Programming Foundations

> **Type:** Module  
> **Maps to:** Module 00 chapters 01–05  
> **Time guide:** 60–90 minutes  
> **Solutions:** [00-programming-foundations-assessment-solutions.md](00-programming-foundations-assessment-solutions.md)

## Purpose

Check that you can explain programming/Java basics, execution, algorithms, data, complexity, and debugging—without treating this score as a measure of professional worth.

## Instructions

Attempt without opening solutions. Use JDK 21 for coding tasks.

## Part A — Conceptual

1. Define programming in one or two sentences without using unexplained jargon.
2. What are JDK, JRE, and JVM? How do they relate?
3. What is bytecode, and why does Java use it?
4. What is an algorithm? Give one non-code example and one programming example.
5. What is program state? Give a tiny example of state changing.
6. Explain O(1), O(n), and O(n²) with one example each.
7. Name three bug categories (compile-time, runtime, logic) with one example each.

## Part B — Code reading / predict the output

8. Predict the output:

```java
public class Q8 {
    public static void main(String[] args) {
        System.out.println("A");
        show();
        System.out.println("C");
    }
    static void show() {
        System.out.println("B");
    }
}
```

9. Given `int[] a = {4, 9, 1};`, hand-trace a linear `findMax` and state the result.

10. Why is this loop suspicious for summing `1..n` inclusive?

```java
for (int i = 1; i < n; i++) {
    sum += i;
}
```

## Part C — Debugging

11. Symptoms: `sumUpTo(5)` prints `10` but should print `15`. List investigation steps and the likely root cause.
12. A beginner writes `for (int i = 0; i <= names.length; i++)` over an array. What runtime failure is likely? How do you fix it?

## Part D — Practical coding

13. Write a Java class `CountPasses` with a method that counts how many values in a non-empty `int[]` are `>= 60`. Include a `main` that prints the count for `{59, 60, 100}`.
14. Write `FindMin` analogous to `FindMax` for `{72, 91, 60}` and print the minimum.

## Part E — Advanced (optional)

15. Explain one HotSpot-specific idea that must not be confused with a Java language guarantee.
16. Design an API decision for `findMax` on an empty array: pick a behavior, document it, and justify it.

## Scoring guide

| Band | Suggested next steps |
|------|----------------------|
| Strong on A–D | Proceed to Module 01; skim Revision and Mastery in M00 |
| Partial | Re-read mapped chapters below for missed items; redo those parts |
| Weak | Replay Module 00 in order with examples; re-assess later |

## Chapter mapping

| Questions | Chapter |
|-----------|---------|
| 1–3, 8 | 01, 02 |
| 4, 9, 13–14 | 03 |
| 5, 12 | 04 |
| 6–7, 10–11 | 05 |
| 15–16 | 01–03 + mastery |
