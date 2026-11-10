# Solutions: Module 14 — Exceptions and Error Handling

> **Assessment:** [14-exceptions-assessment.md](14-exceptions-assessment.md)

## Part A

1. **`Throwable`**
2. **`RuntimeException`** (and its subclasses); **`Error`** is also unchecked but not under `Exception`.
3. **No** — `IllegalArgumentException` is unchecked (`RuntimeException`).
4. Serious JVM resource failure; masking prevents proper shutdown/diagnostics; fix memory/config instead.
5. Caller must **catch** or **declare `throws`** (or handle in lambda boundary via wrap).
6. **`throw`** raises an instance; **`throws`** on a declaration lists checked types that may propagate.
7. **Yes** — `IllegalStateException` extends `RuntimeException` extends `Exception`.
8. **Unchecked** — caller gave bad argument; not an external recoverable condition.
9. The **underlying** throwable passed to `super(message, cause)` (or `initCause`), may be `null`.
10. Operations/debugging need driver detail in logs without exposing `SQLException` in domain signatures.
11. **`AutoCloseable`** (or `Closeable`).
12. Attached as **suppressed** on the primary exception (`addSuppressed` / `getSuppressed()`).

## Part B

13. `bad-arg`  
14. `missing-file`  
15. `true`  
16. `true`

## Part C

17. `ok-path` then `finally-ran`  
18. `catch2=boom` then `finally2-ran`  
19. `open=R1`, `data-R1`, `close=R1`

## Part D

20. `60`  
21. `need 40 more`  
22. `40`

## Part E

23. **Compile error** — `IOException` handler unreachable (subtype after supertype).
24. **Swallowed failures** — outages silent, data loss, hard-to-debug production issues.
25. Balance may be **wrong** (partial update) before throw — violates validate-then-act.
26. Checked when caller **routinely recovers** (retry, alternate workflow); `IllegalArgumentException` for argument contracts.
27. Exceptions are costly; “not found” is often **expected** — use `Optional` or 404 mapping without throw on every miss.
28. **Yes** — `finally` runs after `catch` completes (unless JVM halt/`finally` throws/etc.).
29. **`B` then `A`** — reverse declaration order.
30. **`IllegalStateException`** (also accept other valid JDK examples with justification).

## Remediation map

| Question buckets | Chapter |
|------------------|---------|
| 1–8, 13–16, 23, 26, 28, 30 | Ch.01 |
| 11–12, 17–19, 24, 28–29 | Ch.02 |
| 9–10, 20–22, 25–27 | Ch.03 |
