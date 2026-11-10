# JUnit and Mockito

> **Status:** Completed  
> **Module:** 30 — Testing and Code Quality  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1; JUnit 5.11.4 via Maven (h2-jdbc-demo)

## 1. Learning Objectives

- Run **JUnit Jupiter** tests with Maven Surefire.
- Use **`@Test`**, **`assertEquals`**, and lifecycle basics.
- Explain **Mockito** mocks for isolating collaborators (syntax + when to mock).

## 2. Prerequisites

- Ch.01 — AAA pattern.
- Module **29** Ch.02 — Maven `test` phase.

## 3. Why This Concept Exists

JUnit is the de facto Java **test framework** on the JVM: annotations, assertions, lifecycle hooks, parameterized tests, and IDE/CI integration. **Mockito** creates **test doubles** so unit tests avoid slow or nondeterministic collaborators (databases, payment gateways).

## 4. Conceptual Explanation

**JUnit 5 structure:**

| Annotation | Role |
|------------|------|
| `@Test` | Marks a test method Jupiter should run |
| `@BeforeEach` / `@AfterEach` | Per-test setup/teardown |
| `org.junit.jupiter.api.Assertions.*` | `assertEquals`, `assertThrows`, etc. |

**Mockito basics** (requires `mockito-core` dependency — not run standalone in handbook demos):

```java
interface ClockPort { Instant now(); }
ClockPort clock = mock(ClockPort.class);
when(clock.now()).thenReturn(Instant.parse("2026-01-01T00:00:00Z"));
```

`mock` returns a proxy; `when`/`thenReturn` stub behavior; `verify(clock).now()` asserts interaction.

Mockito uses **bytecode** subclassing / inline mocking (implementation detail). Do not mock **final** types without inline mock maker configuration; prefer interfaces for ports.

## 5. Step-by-Step Examples

Verified JUnit run — [h2-jdbc-demo](../../examples/backend/h2-jdbc-demo/) `SumPositiveTest`:

```java
@Test
void sumsOnlyPositiveValues() {
    assertEquals(6, sumPositive(new int[] {1, -1, 2, 3}));
}
```

**Verified:** `mvn -q test` exits 0 on Temurin 21.0.12.1 with Maven 3.9.6.

To add Mockito in your project, declare:

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.14.2</version>
    <scope>test</scope>
</dependency>
```

## 6. Real-World Use Cases

- Service layer tests with mocked repositories.
- Verifying email gateways were called with correct template IDs.
- `@ParameterizedTest` for exhaustive input tables.

## 7. Edge Cases and Limitations

- **Over-mocking** couples tests to implementation — prefer testing outcomes.
- Static mocks require `mockito-inline` — use sparingly.
- Tests that start real servers belong in integration tests, not unit tests with mocks.

## 8. Common Mistakes

### Incorrect

```java
@Test
void everything() {
    // asserts database, HTTP, and email in one method
}
```

### Corrected

Split unit tests with mocks from integration tests with Testcontainers or H2 (Module **33**).

## 9. Best Practices

- Use **`assertThrows`** for expected exceptions with message checks.
- Name tests as specifications: `withdraw_insufficientBalance_throws`.
- Keep Mockito for **boundaries**; use real objects for value types.

## 10. Chapter Summary

JUnit Jupiter supplies the test runtime; Mockito isolates collaborators. The handbook verifies JUnit via Maven Surefire in `h2-jdbc-demo`.

## 11. Next Steps

[03-static-analysis-and-ci.md](03-static-analysis-and-ci.md)

## Review Checklist

- [x] JUnit verified via mvn test
- [x] Mockito taught conceptually with version note
