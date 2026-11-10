# Java Regex API

> **Status:** Completed  
> **Module:** 23 — Regular Expressions  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Compile patterns with **`Pattern`**, match with **`Matcher`**.
- Iterate **`find()`** matches; use **`matches()`** for entire region.
- Use **named groups** and replacement with **`replaceAll`**.

## 2. Prerequisites

- Ch.01 regex fundamentals.

## 3. Why This Concept Exists

`String` regex methods hide `Pattern` reuse and iterative matching. The **`java.util.regex`** API exposes flags, groups, and `Matcher` state for parsers and transformers.

## 4. Conceptual Explanation

- **`Pattern.compile(regex)`** — immutable, thread-safe pattern.
- **`Matcher`** — stateful per input; `find()` scans; `matches()` anchors to entire sequence.
- **Groups** — `group(1)` numbered; `(?<name>...)` with `group("name")`.
- **Flags** — `CASE_INSENSITIVE`, `MULTILINE`, `DOTALL`, etc.

## 5. Step-by-Step Examples

Demo: [java-regex-api-demo](../../examples/core-java/java-regex-api-demo/).

**Verified output:**

```text
match=ENG-42 dept=ENG
match=OPS-7 dept=OPS
replaced=[ENG#42]
fullMatch=false
findOnce=true
namedId=42
```

Multiple `find()` on input with two tokens. `replaceAll` with group refs. `matches()` false for partial remainder string; single token `find` yields `namedId=42`.

## 6. Real-World Use Cases

- Tokenizing structured tags (`DEPT-ID`).
- Redacting PII in logs with `appendReplacement`.
- Validating password rules with explicit `Matcher` walks.

## 7. Edge Cases and Limitations

- **`Matcher` not thread-safe** — create per thread or synchronize.
- Reusing `Matcher` with `reset(CharSequence)` for performance.
- Lookbehind length limits in Java regex engine.

## 8. Common Mistakes

Calling `matches()` expecting substring find—use `find()`.

## 9. Best Practices

- Cache **`Pattern`** statically for hot paths.
- Use **named groups** for readable replacements.
- Bound input length before regex on user content (ReDoS mitigation).

## 10. Security Considerations

Untrusted regex + untrusted input can cause **ReDoS**—timeout or limit length; use possessive quantifiers where appropriate.

## 11. Revision and Mastery

- Difference `find` vs `matches`?
- When is named group clearer than `group(2)`?

## 12. Chapter Summary

`Pattern`/`Matcher` provide iterative matching, groups, and replacements beyond `String` one-shots.

## 13. Next Steps

Module 24+ per roadmap (concurrency, etc.).

## Review Checklist

- [x] Verified demo  
- [x] ReDoS awareness
