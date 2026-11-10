# Encoding and String Performance

> **Status:** Completed  
> **Module:** 11 — Arrays and Strings  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Distinguish **Unicode code points**, UTF-16 **code units**, and **grapheme clusters**.
- Explain why **`String.length()`** may not equal “visible character count.”
- Encode and decode strings with **`Charset`** / **`StandardCharsets.UTF_8`** via **`getBytes`** and constructors.
- Choose **`StringBuilder`**, **`StringJoiner`**, and compiler **`+`** appropriately in hot paths.
- Avoid common performance traps: **concatenation in loops**, redundant **`new String`**, **`toUpperCase`** in tight loops without need.

## 2. Prerequisites

- **Ch.02–03** — immutability, builders, text blocks.
- **Module 02** — `byte` vs `char`.

## 3. Why This Concept Exists

Programs interchange text as **bytes** (files, HTTP, databases) but process it in memory as **`String`** (UTF-16). Misunderstanding **encoding** causes mojibake (`cafÃ©`), split surrogate pairs, and wrong length limits. Misunderstanding **performance** causes GC pressure from accidental quadratic string building.

## 4. Conceptual Explanation

### 4.1 UTF-16 in `String`

`String` indexes **`char`** (16-bit). Basic multilingual plane characters fit one `char`. Others (e.g. some emoji) use **surrogate pairs**—**two** `char` values, **one** code point.

### 4.2 Code points API

```java
int cpCount = text.codePointCount(0, text.length());
int cp = text.codePointAt(0);
String one = new String(Character.toChars(cp));
```

Use **`codePointAt` / `codePoints()`** when traversing by Unicode code point, not raw `charAt` alone.

### 4.3 UTF-8 on the wire

**UTF-8** is variable-length: ASCII letters are 1 byte; `é` is typically **2** bytes; many emoji are **4**. **`text.getBytes(StandardCharsets.UTF_8)`** returns a **new `byte[]`**; it is not the in-memory UTF-16 form.

Round trip:

```java
byte[] raw = text.getBytes(StandardCharsets.UTF_8);
String back = new String(raw, StandardCharsets.UTF_8);
```

Use **explicit charset**—never **`getBytes()`** without argument (platform default charset).

### 4.4 Performance sketch

| Pattern | Typical cost |
|---------|----------------|
| Few known `+` | Compiler may fuse; fine |
| Loop `+` on growing string | Quadratic copies — use builder |
| Builder with presize | Near linear |
| `String.format` every log line | Parser overhead — guard log level |

Measure in production with profilers; microbenchmarks lie easily.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Code point** | Unicode scalar value (U+XXXX) |
| **Code unit** | UTF-16 `char` storage unit |
| **Charset** | Rules mapping bytes ↔ characters (`UTF_8`, `ISO-8859-1`, …) |
| **Mojibake** | Corrupted text from wrong charset decode |
| **Surrogate pair** | Two `char`s encoding one supplementary code point |

## 6. Syntax and Language Rules

```java
import java.nio.charset.StandardCharsets;

byte[] utf8 = s.getBytes(StandardCharsets.UTF_8);
String s2 = new String(utf8, StandardCharsets.UTF_8);

int lenChars = s.length();
int lenPoints = s.codePointCount(0, s.length());
```

- **`StandardCharsets.UTF_8`** — constant, no checked exception (unlike some `Charset.forName` paths).
- **`Files.readString(path, StandardCharsets.UTF_8)`** — preferred file reads (Module 17).

## 7. Step-by-Step Examples

### 7.1 Minimal — supplementary character length

```java
String emoji = "\uD83D\uDE00";  // 😀 as surrogate pair
System.out.println(emoji.length());           // 2 code units
System.out.println(emoji.codePointCount(0, emoji.length())); // 1 code point
```

### 7.2 Realistic — EncodingPerfDemo

Full source: [`EncodingPerfDemo.java`](../../examples/core-java/encoding-perf-demo/EncodingPerfDemo.java).

```bash
javac EncodingPerfDemo.java && java EncodingPerfDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
chars=4
utf8Bytes=5
roundtripOk=true
built=012
```

**Important lines:**

- **`caf\u00e9`** — four **UTF-16 code units** (ASCII letters + one accented letter).
- **UTF-8 size 5** — `c,a,f` one byte each; **`é`** two bytes in UTF-8.
- **Round trip** preserves logical text when charset matches.
- **Builder loop** builds `"012"` without interim `String` garbage.

**Console note:** use UTF-8 terminal encoding so `é` displays correctly; logical value is correct regardless of glyph rendering.

### 7.3 Advanced — iterating code points

```java
text.codePoints().forEach(cp -> System.out.printf("U+%04X%n", cp));
```

## 8. Real-World Use Cases

- **REST/JSON** — UTF-8 on the wire; Jackson assumes Unicode strings in memory.
- **File exports** — specify charset in format docs; BOM policies for Excel.
- **Database `VARCHAR` limits** — may count bytes or characters—know your engine.
- **Log aggregation** — avoid megabyte strings built per request.

## 9. Internal Behavior

`String.getBytes(Charset)` encodes through **`CharsetEncoder`** (may replace unmappable chars per `CodingErrorAction`). JDK uses optimized intrinsics for UTF-8 on recent releases.

`StringBuilder` may use **byte[]** compact representation like `String` (implementation detail).

## 10. Edge Cases and Limitations

- **Default charset** — `Charset.defaultCharset()` varies by OS/locale; **always specify UTF-8** for portable apps.
- **Malformed UTF-8** on decode — replacement char or exception per decoder policy.
- **Normalization** — `é` as one code point vs `e` + combining acute are **different sequences** with equal display—compare with **`Normalizer`** when required.
- **`String.length()` for limits** — Twitter-style “character counts” need grapheme logic (ICU4J), not `length()`.

## 11. Common Mistakes

### Incorrect — platform default bytes

```java
byte[] data = s.getBytes();  // depends on OS
```

### Corrected

```java
byte[] data = s.getBytes(StandardCharsets.UTF_8);
```

### Incorrect — splitting on `charAt` for emoji

```java
String half = s.substring(0, 1);  // may split surrogate pair
```

### Corrected

```java
int end = s.offsetByCodePoints(0, 1);
String first = s.substring(0, end);
```

## 12. Best Practices

- **UTF-8 everywhere** for new systems unless a standard mandates otherwise.
- Presize **`StringBuilder`** when length estimate known.
- Use **`Reader`/`Writer`** with charset for large text (Module 17).
- Log **byte length** and **code point count** separately in APIs with limits.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| UTF-8 storage on disk | Compact for English | Encode/decode CPU |
| UTF-16 in `String` | Fast `charAt` for BMP | Larger for mostly-ASCII if not compressed |
| `byte[]` for binary text | Exact wire form | Not `String`—decode explicitly |

## 14. Performance Considerations

- Avoid **`String` in tight numeric loops** when a builder or `byte[]` suffices.
- **`String.intern()`** on untrusted input — memory exhaustion risk.
- **Defensively copy** only when crossing trust boundaries, not “always” for micro-wins.

## 15. Security Considerations

- **Homoglyph attacks** — visually similar Unicode in URLs/usernames.
- **Charset sniffing** — attacker-controlled bytes interpreted under wrong charset.
- **Very large strings** — DoS via huge concatenation; cap input size before building.

## 16. Java Version and Compatibility Notes

`StandardCharsets` since Java 7. UTF-8 default for **JDK 18+** standard APIs in many paths (JEP 400)—still specify charset in your code for clarity on older deployments.

## 17. Related Language Comparisons

Go `string` is UTF-8 in memory; indexing is by byte. Java keeps UTF-16 `String` API; know your language’s model when porting.

## 18. Revision and Mastery

- **Gaps:** equating `length()` with glyphs; ignoring charset on `getBytes`.
- **You should answer:** Why is UTF-8 byte length 5 for `café` but `length()` 4?
- **Advanced:** `StringCoder`, vectorized UTF-8 intrinsics, `CharsetDecoder` malform handling.

## 19. Exercises

### Beginner

1. Predict `chars=` and `utf8Bytes=` from EncodingPerfDemo.
2. Encode `"A"` and report UTF-8 length.

### Intermediate

3. Write `truncateToCodePoints(String s, int maxPoints)` without splitting surrogates.
4. Refactor a logging loop from `+=` to `StringBuilder`.

### Advanced

5. When would you store `byte[]` instead of `String` for text?

## 20. Interview Questions

1. Difference between UTF-8 and UTF-16?
2. Why not use `String.length()` for user-visible “characters”?
3. What does `getBytes()` without charset use?

## 21. Solutions

1. `4` and `5`
2. `1`
3. Sample uses `offsetByCodePoints`
4. Linear vs quadratic copy argument
5. Streaming crypto/hash over raw bytes before decode

## 22. Chapter Summary

**Strings** live in UTF-16 **code units**; **UTF-8** encodes wire/storage bytes. **`EncodingPerfDemo`** shows **`length()` 4**, **UTF-8 5 bytes**, **round trip**, and builder **`012`**. Specify **charset explicitly**; traverse supplementary text with **code point** APIs. Module 11 complete—next **Collections** (Module 12).

## 23. Key Terminology

UTF-8 · UTF-16 · code point · code unit · charset · surrogate pair

## 24. References and Further Reading

- [Java SE `Charset`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/charset/Charset.html)
- [JEP 400: UTF-8 by Default](https://openjdk.org/jeps/400)
- [Unicode terminology](https://www.unicode.org/glossary/)

## 25. Next Steps

- [Module 12 — Collections Framework](../12-collections-framework/README.md)
- Module 17 — NIO charset and readers/writers

## Review Checklist

- [x] Learning objectives covered
- [x] EncodingPerfDemo verified Temurin 21.0.12.1
- [x] UTF-8 vs UTF-16 depth gate
- [x] Security and performance sections
