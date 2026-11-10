# Time Zones and Formatting

> **Status:** Completed  
> **Module:** 22 — Date, Time, and Internationalization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Convert instants across **`ZoneId`** with **`ZonedDateTime`**.
- Format and parse with **`DateTimeFormatter`** (pattern and localized).
- Explain **same instant, different local time** clearly.

## 2. Prerequisites

- Ch.01 `java.time` types.

## 3. Why This Concept Exists

Global systems must display local times while storing a single timeline. **`ZonedDateTime`** binds local fields to a zone ruleset; **`DateTimeFormatter`** controls human-readable text.

## 4. Conceptual Explanation

- **`ZoneId`** — region rules (`America/New_York`, `Asia/Yangon`) or offsets.
- **`ZonedDateTime`** — local date-time + zone; `withZoneSameInstant` converts.
- **`DateTimeFormatter`** — pattern letters (`yyyy-MM-dd HH:mm z`) or styles.
- Prefer **named zones** over fixed offsets when rules change (DST).

## 5. Step-by-Step Examples

Demo: [zones-formatting-demo](../../examples/core-java/zones-formatting-demo/).

**Verified output:**

```text
utc=15:30
yangon=22:00
formatted=2026-11-10 22:00 MMT
legacyDateAvoided=true
```

UTC 15:30 equals 22:00 in Asia/Yangon (UTC+6:30). Formatter includes zone abbreviation.

## 6. Real-World Use Cases

- Meeting scheduler showing attendee local times.
- Support dashboards formatting user locale + zone from profile.
- Parsing ISO-8601 from APIs (`Instant.parse` / formatter with offset).

## 7. Edge Cases and Limitations

- **DST gaps/overlaps** — local times may be invalid or ambiguous; use `ZonedDateTime` rules.
- **Abbreviations** like `MMT` are display-only—not unique globally.
- Do not persist `ZonedDateTime` without clarity—often persist `Instant` + zone id.

## 8. Common Mistakes

### Incorrect

Storing wall-clock time without zone for distributed events.

### Corrected

Store `Instant` + `ZoneId` or offset separately.

## 9. Best Practices

- Use **`DateTimeFormatter.ISO_*`** constants when possible.
- Thread-safe formatters: immutable `DateTimeFormatter` instances.

## 10. Chapter Summary

Zones translate instants to local civil time; formatters render and parse text—always know which zone context applies.

## 11. Next Steps

[03-locale-and-internationalization.md](03-locale-and-internationalization.md)

## Review Checklist

- [x] java.time only  
- [x] Verified demo
