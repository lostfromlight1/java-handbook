# Regex Fundamentals

> **Status:** Completed  
> **Module:** 23 — Regular Expressions  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Read basic **regex** syntax: literals, character classes, quantifiers, anchors, groups.
- Use **`String.matches`**, **`split`**, **`replaceAll`** for simple tasks.
- Know when regex is the wrong tool (parsing HTML, full grammars).

## 2. Prerequisites

- Module **11** strings.

## 3. Why This Concept Exists

Much validation and extraction is pattern-shaped (emails, IDs, log lines). Regex engines offer compact declarative patterns—but with performance and readability limits.

## 4. Conceptual Explanation

- **Literal** — match exact text (escape metacharacters `\.`).
- **`.`** — any char (except line break by default); **`\\d`**, **`\\w`**, **`\\s`** shorthands.
- **`*` `+` `?`** — quantifiers; `{n,m}` counts.
- **`^` `$`** — start/end anchors for full string.
- **`(...)`** — capturing group; **`$1`** in replacement.
- Java strings need **double backslashes** for regex escapes: `"\\d+"`.

## 5. Step-by-Step Examples

Demo: [regex-fundamentals-demo](../../examples/core-java/regex-fundamentals-demo/).

**Verified output:**

```text
emailValid=true
capturedId=1042
splitCount=3
secondWord=beta
```

`matches` validates email-shaped pattern. `replaceAll` captures order id. `split` on whitespace.

## 6. Real-World Use Cases

- Input validation at API boundary (prefer library validators for email in production).
- Parsing semi-structured log tokens.
- Normalizing delimiters.

## 7. Edge Cases and Limitations

- **Catastrophic backtracking** on nested quantifiers—test performance.
- **`matches` entire string** — partial finds need `Pattern`/`Matcher` (Ch.02).
- Unicode categories need `UNICODE_CHARACTER_CLASS` flag for some patterns.

## 8. Common Mistakes

### Incorrect

Single backslash in Java source: `"\d"` — illegal escape.

### Corrected

`"\\d+"`.

## 9. Best Practices

- Prefer **`Pattern.compile`** once for hot loops (Ch.02).
- Comment complex patterns; add unit tests with edge strings.
- For nested structures, use parser/json path—not regex alone.

## 10. Chapter Summary

Regex fundamentals plus `String` helpers cover simple validation and splitting; graduate to `Pattern`/`Matcher` for control.

## 11. Next Steps

[02-java-regex-api.md](02-java-regex-api.md)

## Review Checklist

- [x] Verified demo
