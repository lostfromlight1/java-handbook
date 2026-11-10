# IDE Debugging

> **Status:** Completed  
> **Module:** 31 — Debugging and Troubleshooting  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (debug-target-demo output)

## 1. Learning Objectives

- Use **breakpoints**, **step over/into**, **watches**, and **conditional breakpoints**.
- Connect **logic bugs** vs **crashes** (exceptions).
- Debug with **virtual threads** awareness (many threads in one dump).

## 2. Prerequisites

- Module **00** Ch.05 — debugging mindset.
- Module **14** — exceptions.

## 3. Why This Concept Exists

Logs show symptoms; a **debugger** lets you pause execution, inspect **stack frames**, locals, and field values at the failing instant. IDE debuggers use **JDWP** (Java Debug Wire Protocol) to talk to the JVM — API/product behavior, not language semantics.

## 4. Conceptual Explanation

| Action | Effect |
|--------|--------|
| Breakpoint | Pause before a line executes |
| Step over | Run current line, stop at next in same method |
| Step into | Enter called method |
| Evaluate expression | Inspect arbitrary code in current frame |
| Conditional breakpoint | Pause only when predicate true (e.g. `word.length() == 3`) |

**Remote debugging:** JVM started with `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005` attaches from IDE — common for containers.

## 5. Step-by-Step Examples

Demo: [debug-target-demo](../../examples/debugging/debug-target-demo/).

**Verified output (buggy code):**

```text
count=1
expectedCorrect=1
```

The bug uses `word.length() > minLength` instead of `>=`. Set a breakpoint on the `if`, inspect `word` and `minLength` when `minLength=3`: words of length exactly 3 are incorrectly excluded (demo words: `a`, `ab`, `abc`, `abcd` — only `abcd` matches `> 3`).

**Fix:** use `>=` for “at least” semantics — then `count=2` for lengths 3 and 4.

## 6. Real-World Use Cases

- Stepping through payment approval when state machine mis-fires.
- Inspecting **Optional** misuse before NPE.
- Comparing expected vs actual in failing JUnit without rerunning entire suite blindly.

## 7. Edge Cases

- **Heisenbugs** vanish under debugger timing — reproduce with logging or stress tests.
- Optimized code may not match source line exactly — disable optimizations when needed (`-Xdebug` legacy term; modern JDWP handles most cases).
- Production debugging prefers **read-only** attach with caution — Module **31** Ch.03.

## 8. Common Mistakes

### Incorrect

Changes code while debugger confused about hot swap limits — expects massive redefines.

### Corrected

Hot swap handles method body edits; signature changes need restart.

## 9. Best Practices

- Reproduce with smallest input (`List.of("abc")` case).
- Use **conditional breakpoints** on loops with thousands of iterations.
- Combine debugger with **unit test** pinning the bug (Module 30).

## 10. Chapter Summary

IDE debugging is the primary interactive tool for understanding control flow and state; pair it with the debug-target demo’s intentional off-by-one predicate bug.

## 11. Next Steps

[02-dumps-and-flight-recorder.md](02-dumps-and-flight-recorder.md)

## Review Checklist

- [x] Verified debug-target-demo output documented
