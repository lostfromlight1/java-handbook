# java.time

> **Status:** Completed  
> **Module:** 22 — Date, Time, and Internationalization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (type roles, immutability, arithmetic)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

**Section policy:** Omissions in Review Checklist.

## 1. Learning Objectives *(mandatory)*

After completing this chapter, you should be able to:

- Choose among **`LocalDate`**, **`LocalTime`**, **`LocalDateTime`**, **`Instant`**, **`Duration`**, and **`Period`**.
- Explain why **`java.time`** types are **immutable** and thread-safe by design.
- Perform comparisons and arithmetic without legacy **`Date`** / **`Calendar`**.
- Use **`Instant`** as a UTC timeline for logs and persistence boundaries.

## 2. Prerequisites *(mandatory)*

- Module **02** — numeric types; `long` for epoch seconds/nanos.
- Module **20** — stdlib overview may mention legacy time APIs—this module replaces them.

## 3. Why This Concept Exists *(mandatory)*

`java.util.Date` was mutable, confused **local** vs **UTC**, and used 0-based months. **`java.time`** (JSR-310, Java 8+) models separate concerns: civil dates without zones, instants on the UTC line, and zone rules in Ch.02.

## 4. Conceptual Explanation *(mandatory)*

| Type | Use when |
|------|----------|
| `LocalDate` | Birthday, invoice due date (no zone) |
| `LocalTime` | Time-of-day without date |
| `LocalDateTime` | Local meeting time **without** global uniqueness |
| `Instant` | Machine timeline UTC (logs, DB `timestamptz` mental model) |
| `Duration` | Elapsed time (hours, minutes, nanos between instants/times) |
| `Period` | Calendar amounts (years/months/days) |

**API / design:** Instances are **immutable**; every `plus`/`with` returns a new value.

## 5. Mechanism and Execution Flow *(mandatory)*

1. Parse or construct a value (`LocalDate.parse`, `Instant.parse` for ISO-8601).
2. Combine types explicitly (`atTime`, `atZone` in Ch.02 when zones appear).
3. Compare with `isBefore` / `compareTo`; arithmetic with `plus`/`minus`.

No separate JVM mechanism—pure library types on the heap like any object.

## 6. Terminology *(mandatory)*

| Term | Meaning |
|------|---------|
| Civil date | Year-month-day in a locale/calendar |
| Instant | Point on UTC timeline |
| ISO-8601 | Standard string formats (`Instant.parse`) |

## 7. Syntax and Language Rules *(include when the topic is language/API surface)*

```java
LocalDate birth = LocalDate.of(1990, 1, 15);
Period age = Period.between(birth, LocalDate.now());
Duration meeting = Duration.between(startTime, endTime);
Instant logEvent = Instant.parse("2026-11-10T08:00:00Z");
```

Inject **`Clock`** in tests instead of hard-coded `now()` (shown in demo flag `useJavaTime`).

## 8. Step-by-Step Examples *(mandatory)*

Demo: [java-time-demo](../../examples/core-java/java-time-demo/).

**Verified output (Temurin JDK 21.0.12.1):**

```text
ageYears=36
durationMinutes=150
instantPlus=2026-11-10T10:00:00Z
useJavaTime=true
```

**Important lines:** `Period.between` uses calendar rules (years), not fixed 365-day math. `Duration.between` for same-day times yields minute precision in demo.

### 8.2 Incorrect

```java
new Date(); // legacy in new code
```

### 8.3 Corrected

```java
Instant.now();
```

## 9. Real-World Use Cases *(conditional)*

- Subscription periods (`Period`); SLA timeouts (`Duration`).
- Audit logs as `Instant` in UTC.

## 11. Edge Cases and Limitations *(strongly encouraged)*

- **Month arithmetic** — `plusMonths` adjusts end-of-month (Jan 31 + 1 month).
- **Leap seconds** — simplified UTC-SLS in API; rare operational issue.
- **`LocalDateTime` alone** does not identify global moments—add `ZoneId` (Ch.02).

## 12. Common Mistakes *(mandatory)*

Mixing `Duration` and `Period` semantics (months vs 30 days).

## 13. Best Practices *(include when guidance adds value)*

- Store **`Instant`** or offset types at persistence boundaries.
- Use **`Clock`** in unit tests.

## 17. Java Version and Compatibility Notes *(conditional)*

- `java.time` final since Java 8; primary path Java 21 unchanged.

## 19. Revision and Mastery *(Technical)*

**Question:** When must you add a zone to a type?

## 23. Chapter Summary *(mandatory)*

**java.time** separates local civil time, elapsed amounts, and UTC **instants** with immutable types—foundation for zones (Ch.02) and locale formatting (Ch.03).

## 25. References and Further Reading *(mandatory)*

- [Java SE `java.time` package summary](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/package-summary.html)

## 26. Next Steps

[02-time-zones-and-formatting.md](02-time-zones-and-formatting.md)

## Review Checklist

- [x] Technical profile; Verified Temurin 21.0.12.1
- [x] Mechanism: value construction flow
- [x] Omissions: Internal JVM, Security, Performance
