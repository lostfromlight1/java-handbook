# Algorithms and Problem Solving

> **Status:** Completed  
> **Module:** 00 — Programming Foundations  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Chapter profile:** Conceptual  
> **Depth target:** Levels 1–2 (algorithms before syntax fluency)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Define algorithm, pseudocode, and flowchart at a beginner level.
- Break a problem into inputs, outputs, and steps before coding.
- Express a simple algorithm in pseudocode and then in Java.
- Trace a linear scan (find maximum) by hand and in code.
- Recognize common problem-solving techniques: decomposition, pattern recognition, debugging-as-thinking.
- Explain why algorithm choice matters before optimizing Java syntax.

## 2. Prerequisites

- [01-what-is-programming-and-what-is-java.md](01-what-is-programming-and-what-is-java.md)
- [02-how-computers-execute-programs.md](02-how-computers-execute-programs.md)
- Willingness to use basic `for` / `if` (explained in §7 below; the **Control Flow** module `03-control-flow` deepens every branching and looping form)

## 3. Why This Concept Exists

Typing Java without a plan produces fragile programs. Professional work starts with:

1. Understanding the problem
2. Designing a method (algorithm)
3. Only then encoding it in a language

This chapter builds that habit before syntax becomes the main focus.

## 4. Conceptual Explanation

### 4.1 What an algorithm is

An **algorithm** is a finite, unambiguous sequence of steps that solves a problem or computes a result.

Properties that matter:

| Property | Meaning |
|----------|---------|
| Finite | It stops (for the cases we care about) |
| Unambiguous | Each step is clear—no “sort of do this” |
| Effective | Steps are doable with available operations |
| Input / output | Takes data in; produces a result or effect |

Example problem: “Given a list of test scores, what is the highest score?”

### 4.2 Pseudocode

**Pseudocode** is structured plain language for algorithms—not real Java, but close enough to translate.

```text
FIND-MAX(scores):
  if scores is empty, signal an error
  set best to the first score
  for each remaining score s:
    if s > best:
      set best to s
  return best
```

Pseudocode lets you think without fighting braces and types.

### 4.3 Flowcharts (optional visual)

A **flowchart** shows steps and decisions as shapes and arrows. Useful for beginners; professionals often prefer pseudocode or clear functions. Use whichever helps you reason—then delete the ceremony and keep the clarity.

```mermaid
flowchart TD
  start[Start]
  init[best = first score]
  more{More scores?}
  cmp{score greater than best?}
  update[best = score]
  done[Return best]
  start --> init --> more
  more -->|yes| cmp
  cmp -->|yes| update --> more
  cmp -->|no| more
  more -->|no| done
```

### 4.4 Problem-solving techniques

1. **Restate the problem** in your own words; list inputs/outputs/constraints.
2. **Work a tiny example by hand** (three scores: 5, 9, 2 → max 9).
3. **Decompose** into smaller steps (validate input; scan; return).
4. **Recognize patterns** (scanning, counting, searching, transforming).
5. **Implement the simplest correct idea first** (“make it work”).
6. **Test edge cases** (empty list, one element, duplicates).
7. **Improve** only after correctness (Modules 05+ and 28).

### 4.5 Programming paradigms (preview)

| Paradigm | Idea | Java relevance |
|----------|------|----------------|
| Imperative | Step-by-step commands | Everyday Java |
| Object-oriented | Model with objects | Core of Java style (Module 05+) |
| Functional | Compose transformations | Lambdas/streams (Module 16–17) |

You will mostly write imperative Java at first. Algorithms exist independently of paradigm.

## 5. Mechanism and Execution Flow

This section walks the **find-max** algorithm as executed by the JVM—connecting pseudocode to frames, loops, and conditionals.

### 5.1 From pseudocode to bytecode path

| Pseudocode step | Java location | When it runs |
|-----------------|---------------|--------------|
| `best = first score` | `int best = values[0];` | Once on entry to `findMax` |
| `for each remaining score` | `for (int i = 1; i < values.length; i++)` | Condition checked before each iteration; `i++` after body |
| `if s > best` | `if (values[i] > best)` | Each iteration; branch only when condition true |
| `return best` | `return best;` | After loop completes |

The **entry frame** for `findMax` holds `values` (reference on heap), `best`, and loop index `i`. `main`'s frame waits underneath with `maximum` unchanged until `findMax` returns.

### 5.2 How `for` and `if` execute (in-chapter control flow)

**`for` loop mechanics:** initialization (`int i = 1`) runs once. Before each body execution, the JVM evaluates the condition (`i < values.length`). If false, jump past the loop. After a body run, the update (`i++`) runs, then the condition is checked again.

**`if` mechanics:** evaluate the boolean condition. If true, run the block; if false, skip the block with no `else` required. In `findMax`, when `values[i] > best`, the assignment updates local `best` in the current frame only—`main` does not see the new value until the method returns.

### 5.3 Trace to observable output

Calling `findMax(scores)` with `{72, 91, 85, 91, 60}` performs one linear pass (§8.2). The JVM prints `Maximum score: 91` because the last assignment to `best` happened when reading `91` at index `1`; later equal values do not replace `best` under a strict `>` test.


## 6. Terminology

| Term | Meaning |
|------|---------|
| Algorithm | Finite clear steps to solve a problem |
| Pseudocode | Informal structured algorithm description |
| Trace | Step through with concrete values |
| Edge case | Unusual but valid input (empty, tiny, huge) |
| Decomposition | Breaking a problem into parts |
| Correctness | Produces the right result for required cases |

## 7. Syntax and Language Rules

This chapter uses a small subset of Java. Each construct is enough to read `findMax` without opening another chapter first.

### 7.1 Arrays and indexing

- `int[] scores = { 72, 91, 85 };` — creates an **array** of `int` with three elements.
- `scores.length` — element **count** (here `3`). Not a method: no parentheses.
- `scores[i]` — element at index `i`. Valid indexes: `0` through `length - 1`.

**Pitfall:** The first index is `0`, not `1`. Off-by-one errors are classic ([Chapter 05](05-complexity-and-debugging-mindset.md)).

### 7.2 The `for` loop (counted repetition)

```java
for (int i = 1; i < scores.length; i++) {
    // body runs once per iteration while condition is true
}
```

| Part | Role |
|------|------|
| `int i = 1` | **Initialization:** create loop variable `i` once before the loop starts. Here we start at `1` because we already used `scores[0]` as the initial `best`. |
| `i < scores.length` | **Condition:** checked **before** each iteration. When false, the loop stops. |
| `i++` | **Update:** runs after each iteration (`i` becomes `i + 1`). |
| `{ ... }` | **Body:** statements to repeat. |

Equivalent English: “For each index from `1` up to but not including `length`, run the body.”

### 7.3 The `if` statement (conditional action)

```java
if (values[i] > best) {
    best = values[i];
}
```

When the **condition** (`values[i] > best`) is **true**, the block runs. There is no `else` here—when the condition is false, Java simply skips the block and continues. Parentheses around the condition are required.

### 7.4 Method call and `return`

```java
static int findMax(int[] values) {
    ...
    return best;
}
```

`findMax(scores)` passes the array reference into the method; `return best` sends the final `int` back to the caller. Module **04** covers methods, parameters, and return types in depth.

## 8. Step-by-Step Examples

### 8.1 Minimal — hand trace

Scores: `72, 91, 85`

| Step | best | looking at | action |
|------|------|------------|--------|
| start | 72 | — | initialize |
| 1 | 72 | 91 | 91 > 72 → best = 91 |
| 2 | 91 | 85 | 85 > 91? no |
| done | 91 | | return 91 |

### 8.2 Realistic — Java implementation

Runnable: [examples/beginner/find-max/](../examples/beginner/find-max/)

```java
public class FindMax {
    public static void main(String[] args) {
        int[] scores = {72, 91, 85, 91, 60};
        int maximum = findMax(scores);
        System.out.println("Maximum score: " + maximum);
    }

    static int findMax(int[] values) {
        int best = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > best) {
                best = values[i];
            }
        }
        return best;
    }
}
```

**Verified output (Temurin JDK 21.0.12.1):**

```text
Maximum score: 91
```

Duplicates (`91` twice) are fine: “greater than” does not require unique values.

### 8.3 Advanced for this level — empty input

The code above **assumes** a non-empty array. Empty input would throw when reading `values[0]`. A production algorithm must define behavior: throw a clear exception, return an `Optional`, or document the precondition. Module 14 covers exceptions; Module 18 covers `Optional`.

**Design trade-off:** preconditions vs defensive checks—document one and test it.

## 9. Real-World Use Cases

- Pricing: find minimum eligible rate
- Monitoring: maximum latency in a sample window
- Games: high score
- Data pipelines: scan batches for anomalies

Same algorithmic pattern; different domains.

## 10. Internal Behavior

A linear scan visits each element about once. On typical JVMs this is a simple loop in bytecode (`goto`/`if_icmpgt` patterns). Exact bytecode is not required yet; complexity thinking deepens in Chapter 05.

## 11. Edge Cases and Limitations

| Case | Issue |
|------|--------|
| Empty array | `values[0]` fails |
| Single element | Loop body may not run; answer is that element |
| All equal | Still correct |
| Negatives | `>` still works for signed integers |
| Unsorted vs sorted | Scan works unsorted; sorted data allows faster strategies later |

## 12. Common Mistakes

### Incorrect

Start coding before stating inputs/outputs.

### Corrected

Write one sentence: “Given non-empty `int[]`, return the largest value.”

### Incorrect

Initialize `best` to `0` always.

### Corrected

That fails if all scores are negative. Initialize from the first element (or use a proper empty policy).

### Incorrect

Confuse “algorithm” with “whatever code compiled.”

### Corrected

Many programs compile and still implement the wrong algorithm.

## 13. Best Practices

- Hand-trace before and after coding.
- Name methods after the problem (`findMax`), not vague verbs (`doStuff`).
- Test edges: empty (if allowed), one element, duplicates, negatives.
- Prefer clear loops over clever tricks while learning.

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Simple linear scan | Easy to prove, few bugs | Slower on huge data than specialized structures |
| Sort then take last | Uses library sort | Extra work if you only need max |
| Keep a running max as data arrives | Online/streaming friendly | Must handle first value carefully |

## 15. Performance Considerations

For `n` elements, linear max is proportional to `n` (Chapter 05: big-O intuition). For classroom sizes, clarity beats micro-optimization.

## 16. Security Considerations

If scores come from users/files/network, validate size and ranges before trusting them. Algorithms on hostile input need limits (Module 43).

## 17. Java Version and Compatibility Notes

Array loops and `if` used here are long-standing Java. Validated on **21.0.12.1**.

## 18. Related Language Comparisons

Pseudocode → code is universal. Python might express the same algorithm with `max(scores)`; Java’s standard library also has utilities later—learning the loop builds transferable skill.

## 19. Revision and Mastery

### What you should already understand

How to specify preconditions; how to prove a loop invariant informally (“`best` is max of elements seen so far”).

### Common gaps

- Shipping code without edge-case tests
- Mixing problem statement with implementation accidents
- Jumping to “optimal” algorithms before measuring need

### Questions

1. What is the loop invariant for `findMax`?
2. How do you handle empty input in an API you design?
3. When is sorting before finding max justified?

## 20. Exercises

### Beginner

1. Write pseudocode for “count how many scores are >= 60”.
2. Hand-trace `findMax` on `{ -3, -9, -1 }`.

### Intermediate

3. Implement `findMin` similarly; verify with a small main.
4. Modify `findMax` to return the **index** of the maximum (first index if ties).

### Advanced

5. Write two algorithms for “are all elements unique?”—one nested loop, one with a strategy you’ll learn later (`Set`). Compare clarity.
6. Specify and implement behavior for empty arrays without crashing unexpectedly.

## 21. Interview Questions

1. **What is an algorithm?** — Finite unambiguous steps to solve a problem.  
2. **Why pseudocode?** — Design without language noise.  
3. **How do you find a max in an unsorted array?** — Linear scan; track best-so-far.  
4. **What is an edge case?** — Unusual but valid input that often breaks naive code.  

## 22. Solutions

1. Initialize `count = 0`; for each score, if `score >= 60` increment; return count.  
2. best starts -3; -9 no; -1 yes → answer -1.  
3. Same structure with `<` instead of `>`.  
4. Track `bestIndex`; update when `values[i] > values[bestIndex]`.  
5. Nested compare all pairs O(n²); `HashSet` add-and-check later in Module 12.  
6. e.g. throw `IllegalArgumentException` if `values.length == 0`, documented in Javadoc.

## 23. Chapter Summary

Algorithms are clear step sequences. Use pseudocode, hand traces, and decomposition before Java syntax. Linear scan for maximum is a foundational pattern with important edge cases.

## 24. Key Terminology

Algorithm · pseudocode · flowchart · trace · edge case · decomposition · precondition · correctness

## 25. References and Further Reading

- Any standard intro CS text on algorithms (conceptual)
- Next: [04-data-and-basic-structures.md](04-data-and-basic-structures.md); deeper branching/loops: [03-control-flow](../03-control-flow/README.md)
- Later: Module 12 collections; Module 52 interview algorithms

## 26. Next Steps

Continue to data representation: [04-data-and-basic-structures.md](04-data-and-basic-structures.md)

## Review Checklist

**Mandatory Depth Review:** Levels 1–2 for Conceptual profile — algorithm design before control-flow module.

- [x] Learning objectives covered
- [x] Prerequisites identified
- [x] Chapter profile **Conceptual** and depth target **Levels 1–2** recorded
- [x] Depth gate passed
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow (Section 5) for findMax/for/if
- [x] In-chapter `for`/`if` teaching in §7
- [x] FindMax **Verified** on Temurin JDK 21.0.12.1
- [x] Revision and Mastery included
- [x] Exercises/solutions separated
- [x] Conditional omissions recorded below

**Omissions (section → reason):**

- **Internal Behavior** — no JVM-specific algorithm internals at this level.
