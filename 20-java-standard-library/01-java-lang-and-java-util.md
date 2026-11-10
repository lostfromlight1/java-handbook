# java.lang and java.util

> **Status:** Completed  
> **Module:** 20 — Java Standard Library  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Navigate core **`java.lang`** (`String`, `Objects`, wrappers, `Math`, exceptions) and **`java.util`** collections/utilities.
- Use **immutable factory methods** (`List.of`, `Map.of`) and **sequenced** list accessors safely.
- Choose appropriate collection interfaces for everyday tasks.

## 2. Prerequisites

- Modules **02**, **11**, **12** — types, strings, collections.

## 3. Why This Concept Exists

Every Java program imports `java.lang` implicitly. Productivity depends on knowing **String** text methods, **Objects** utilities, and **Collection** framework types without reinventing helpers.

## 4. Conceptual Explanation

### 4.1 java.lang essentials

- **`String`** — immutable UTF-16 sequence; use `strip()`, `isBlank()`, `formatted()` / `String.format`.
- **`Objects`** — `equals`, `hash`, `requireNonNull`, `isNull`/`nonNull`.
- **Wrappers** — `Integer`, `Long`, …; autoboxing (Module 02).
- **`Math`** / **`StrictMath`** — numeric helpers.

### 4.2 java.util collections

- **`List`**, **`Set`**, **`Map`** — interfaces; prefer interface types for fields/parameters.
- **Factory methods** — `List.of("a","b")` returns immutable lists; no null elements.
- **Mutability** — `new ArrayList<>()` for growth; `List.copyOf` for defensive snapshots.
- **Sequenced API (21)** — `getFirst()`, `getLast()` on lists with defined encounter order.

### 4.3 Other java.util staples

- **`Optional`** (Module 18), **`Stream`** (Module 17), **`Comparator`**, **`Collections`**, **`Arrays`**.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Immutable collection | Cannot be structurally modified after creation |
| Encounter order | Order elements are traversed |
| Factory method | Static constructor like `List.of` |

## 6. Step-by-Step Examples

Demo: [java-lang-util-demo](../../examples/core-java/java-lang-util-demo/).

**Verified output:**

```text
strip=Ada Lovelace
isBlank=false
copyOf=[Bob, Ada, Lin]
first=Bob
mapAda=95
join=Bob-Ada-Lin
hashConsistent=true
```

`strip=` trims Unicode whitespace; `copyOf` is immutable; `first=` uses sequenced list API; `join=` uses `String.join`.

## 7. Real-World Use Cases

- Normalizing user input before validation.
- Returning immutable DTO snapshots from services.
- Building CSV fragments with `String.join`.

## 8. Edge Cases and Limitations

- `List.of` throws on **null** elements or duplicate keys in `Map.of`.
- Immutability does not deep-copy mutable elements inside a list.
- `String` equality: content with `equals`, reference with `==` only when intended.

## 9. Common Mistakes

### Incorrect

```java
List.of(items.toArray(new String[0])); // NPE if any null
```

### Corrected

Filter nulls or use mutable `ArrayList` when nulls are possible.

## 10. Best Practices

- Declare **`List<String>`** not `ArrayList` in APIs.
- Use **`Objects.requireNonNull`** at boundaries.
- Prefer **`strip()`** over `trim()` for Unicode-aware trimming.

## 11. Design Trade-offs

Immutable factories reduce bugs but forbid in-place edits—clone to mutable when needed.

## 12. Revision and Mastery

- When is `List.copyOf` vs `Collections.unmodifiableList`?
- Why `Map.of` limited to 10 key-value pairs (use `Map.ofEntries`)?

## 13. Exercises

Normalize and join a list of usernames; compute stable hash for a record key.

## 14. Chapter Summary

`java.lang` and `java.util` form the daily toolkit: text, objects, and collections—with Java 21 sequenced list conveniences.

## 15. Next Steps

[02-io-nio-net-and-time-overview.md](02-io-nio-net-and-time-overview.md)

## Review Checklist

- [x] Verified demo output  
- [x] Depth gate for core APIs
