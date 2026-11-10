# Testing Fundamentals

> **Status:** Completed  
> **Module:** 30 — Testing and Code Quality  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Apply **Arrange–Act–Assert** (AAA) in unit tests.
- Distinguish **unit**, **integration**, and **end-to-end** tests.
- Write a minimal test harness without a framework (then adopt JUnit in Ch.02).

## 2. Prerequisites

- Modules **04–06** — methods, OOP, encapsulation for testable design.

## 3. Why This Concept Exists

Software changes constantly. **Automated tests** encode expectations so refactors and bug fixes do not silently break behavior. Tests also document intended use and support **regression** detection in CI (Ch.03).

## 4. Conceptual Explanation

| Test type | Scope | Typical speed |
|-----------|--------|----------------|
| Unit | One class or function; collaborators mocked or stubbed | Milliseconds |
| Integration | Real JDBC, HTTP, or filesystem boundaries | Slower |
| End-to-end | Full user flow through deployed system | Slowest |

**AAA pattern:**

1. **Arrange** — create inputs and dependencies.
2. **Act** — invoke the code under test.
3. **Assert** — verify outcomes (return values, state, interactions).

**Test double** — generic term for fake collaborators (mocks, stubs, spies). Mockito (Ch.02) implements doubles.

**Flaky test** — passes/fails non-deterministically (timing, shared static state, real network). Fix by controlling time, ports, and isolation.

## 5. Step-by-Step Examples

Demo: [testing-fundamentals-demo](../../examples/testing/testing-fundamentals-demo/).

**Verified output:**

```text
pass:mixed
pass:empty
pass:allNegative
allTests=ok
```

The demo throws `AssertionError` on failure — same failure signal JUnit uses internally.

## 6. Real-World Use Cases

- Payment calculation rules with table-driven tests.
- Password policy validators with edge-case matrices.
- CI gates blocking merge when coverage or tests fail (Ch.03).

## 7. Design for testability

- Prefer **pure functions** and **injected dependencies** over hard-coded `new ConcreteService()`.
- Avoid static mutable globals (Module **24** concurrency conflicts).
- Keep **deterministic** clocks (`Clock` in Module **22** for time-dependent logic).

## 8. Common Mistakes

### Incorrect

Tests the private method directly via reflection instead of behavior through public API.

### Corrected

Test observable outcomes; refactor if API cannot be exercised.

## 9. Best Practices

- One logical assertion theme per test method name (`rejectsNegativeAmount`).
- Independent tests — no order dependency.
- Fail fast with clear messages (`expected=6 actual=5`).

## 10. Chapter Summary

Testing fundamentals—AAA, test types, determinism—underpin JUnit/Mockito and CI pipelines in later chapters.

## 11. Next Steps

[02-junit-and-mockito.md](02-junit-and-mockito.md)

## Review Checklist

- [x] Verified testing-fundamentals-demo
