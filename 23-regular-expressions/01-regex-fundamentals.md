# Regex Fundamentals

> **Status:** Completed  
> **Module:** 23 — Regular Expressions  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (syntax, String helpers, limits)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives *(mandatory)*

- Read literals, classes, quantifiers, anchors, and capturing groups.
- Use **`String.matches`**, **`split`**, **`replaceAll`** for simple tasks.
- Know when regex is the wrong tool (HTML, grammars).

## 2. Prerequisites *(mandatory)*

- Module **11** strings; Java escape rules (`\\d` in source).

## 3. Why This Concept Exists *(mandatory)*

Validation and log tokenization are **pattern-shaped**. Regex offers compact declarative matching at the cost of readability and catastrophic backtracking risk.

## 4. Conceptual Explanation *(mandatory)*

- **Literal** — escape metacharacters (`\\.`, `\\+`).
- **`.`** — any char (line break excluded by default); shorthands `\\d`, `\\w`, `\\s`.
- **Quantifiers** — `*`, `+`, `?`, `{n,m}` greedy by default.
- **Anchors** — `^` start, `$` end; `matches()` implies full-string anchor.
- **Groups** — `(...)` capture; `$1` in replacement strings.

## 5. Mechanism and Execution Flow *(mandatory)*

`String.matches(regex)` compiles pattern internally each call—fine for one-offs; hot paths use `Pattern.compile` (Ch.02). Engine walks NFA/backtracking for alternatives.

## 8. Step-by-Step Examples *(mandatory)*

Demo: [regex-fundamentals-demo](../../examples/core-java/regex-fundamentals-demo/).

**Verified output (Temurin JDK 21.0.12.1):**

```text
emailValid=true
capturedId=1042
splitCount=3
secondWord=beta
```

`replaceAll` uses capture `$1` for order id `1042`. `split` on whitespace yields three tokens; index 1 is `beta`.

## 11. Edge Cases and Limitations *(strongly encouraged)*

- **Catastrophic backtracking** on nested quantifiers.
- **`matches` entire string** — partial scans need `Matcher.find` (Ch.02).
- **`"\\d"`** required in Java source—not `"\d"`.

## 12. Common Mistakes *(mandatory)*

Single backslash shorthands in string literals.

## 13. Best Practices *(include when guidance adds value)*

- Comment complex patterns; unit-test edge strings.
- Prefer dedicated parsers for nested structures.

## 23. Chapter Summary *(mandatory)*

Fundamentals plus `String` helpers cover simple validation; graduate to **`Pattern`/`Matcher`** for control.

## 26. Next Steps

[02-java-regex-api.md](02-java-regex-api.md)

## Review Checklist

- [x] Technical; Verified Temurin 21.0.12.1
- [x] Omissions: Language comparisons
