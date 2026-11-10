# java.time

> **Status:** Completed  
> **Module:** 22 — Date, Time, and Internationalization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Model dates and times with **`LocalDate`**, **`LocalTime`**, **`LocalDateTime`**, **`Instant`**, **`Duration`**, **`Period`**.
- Avoid legacy **`java.util.Date`** and **`Calendar`** in new code.
- Perform arithmetic and comparisons in the type-safe API (JSR-310).

## 2. Prerequisites

- Module **02** numeric types; Module **20** time overview.

## 3. Why This Concept Exists

Legacy date APIs were mutable, confusing (month 0-based), and not thread-safe. **`java.time`** (Java 8+) provides clear types: local vs instant vs zoned (Ch.02).

## 4. Conceptual Explanation

| Type | Use when |
|------|----------|
| `LocalDate` | Calendar date without time zone (birthday, due date) |
| `LocalTime` | Time of day without date |
| `LocalDateTime` | Local date-time without zone |
| `Instant` | UTC timeline point (logs, DB UTC) |
| `Duration` | Time-based amount (hours, minutes) |
| `Period` | Date-based amount (years, months, days) |

All core `java.time` classes are **immutable**.

## 5. Step-by-Step Examples

Demo: [java-time-demo](../../examples/core-java/java-time-demo/).

**Verified output:**

```text
ageYears=36
durationMinutes=150
instantPlus=2026-11-10T10:00:00Z
useJavaTime=true
```

`Period.between` computes years between dates. `Duration.between` for meeting length. `Instant.parse` + `plus` for UTC timeline.

## 6. Real-World Use Cases

- Subscription billing periods (`Period`).
- SLA timeouts (`Duration`).
- Audit logs stored as `Instant` in UTC.

## 7. Edge Cases and Limitations

- **Month lengths** — `Period` of 1 month added to Jan 31 differs from 30-day `Duration`.
- **Leap seconds** — `Instant` follows UTC-SLS simplified model.
- Do not use `LocalDateTime` alone for global events—add `ZoneId` (Ch.02).

## 8. Common Mistakes

### Incorrect

```java
new Date() // legacy in new code
```

### Corrected

```java
Instant.now();
```

## 9. Best Practices

- Store **`Instant`** or offset/zoned in DB; convert at edges.
- Use **`Clock`** injection in tests instead of hard-coded `now()`.

## 10. Chapter Summary

`java.time` separates **local**, **instant**, and (next chapter) **zoned** concerns with immutable types—replace legacy date usage.

## 11. Next Steps

[02-time-zones-and-formatting.md](02-time-zones-and-formatting.md)

## Review Checklist

- [x] No legacy Date in examples  
- [x] Verified output
