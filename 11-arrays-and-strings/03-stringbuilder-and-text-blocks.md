# StringBuilder and Text Blocks

> **Status:** Completed  
> **Module:** 11 — Arrays and Strings  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Use **`StringBuilder`** (and know **`StringBuffer`**) for **mutable** character assembly.
- Choose **`append`**, **`insert`**, **`delete`**, **`reverse`**, and **`toString`** appropriately.
- Explain when the compiler still optimizes **`+`** vs when you need a builder explicitly.
- Author **text blocks** with **`"""`** delimiters and apply **`stripIndent`** / **`translateEscapes`** when needed.
- Format multiline JSON, SQL, and HTML templates readably without fragile `\n` chains.

## 2. Prerequisites

- **Ch.02** — immutability of `String`; why repeated `+` allocates.
- **Module 03** — loops that append in iteration.

## 3. Why This Concept Exists

Building a large string one **`+`** at a time in a loop creates **O(n²)** copying work in the worst case. **`StringBuilder`** maintains a **growable buffer** and amortizes resizing.

Multiline literals used to require `\n` and `+` noise. **Text blocks** (JEP 378, standard in Java 21) embed newlines directly and apply **uniform indentation stripping** rules so source alignment does not leak into runtime text.

## 4. Conceptual Explanation

### 4.1 StringBuilder

- **Not thread-safe** — faster for single-threaded assembly.
- **`StringBuffer`** — synchronized methods; use only when sharing one builder across threads (rare).

Typical pattern:

```java
StringBuilder sb = new StringBuilder();
sb.append("Hello ").append("Java!");
String result = sb.toString();
```

### 4.2 Capacity

Constructors accept **initial capacity** to reduce resize copies when size is known (`new StringBuilder(256)`).

### 4.3 Text blocks

Opening delimiter `"""` followed by newline; closing `"""` on its own line.

```java
String json = """
    {
      "name": "Ada"
    }
    """;
```

The compiler **removes incidental indentation** shared across lines (JLS §3.10.7). Trailing whitespace on lines may be stripped per rules—**verify output** when whitespace is significant.

### 4.4 Escapes in text blocks

Standard escapes work; line continuation uses `\` at end of line. **`\s`** can force a space where stripping would remove it (Java 15+).

## 5. Terminology

| Term | Meaning |
|------|---------|
| **StringBuilder** | Mutable `char`/`byte` buffer with `append` API |
| **Text block** | Multiline string literal with `"""` |
| **Incidental whitespace** | Leading spaces aligned in source; often stripped |
| **Content whitespace** | Spaces that remain part of the string after processing |

## 6. Syntax and Language Rules

```java
// Builder
var sb = new StringBuilder(32);
sb.append('[').append(id).append(']');

// Text block — closing """ must not be indented past content rules allow
String poem = """
        Roses are red,
        Violets are blue.
        """;
```

- **Single expression** text block can follow `=` on same line only with the two-line form rules (see JLS).
- **`StringBuilder` is not a `String`** — call **`toString()`** before APIs expecting `String`.

## 7. Step-by-Step Examples

### 7.1 Minimal — loop without quadratic cost

```java
StringBuilder csv = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    if (i > 0) csv.append(',');
    csv.append(i);
}
String out = csv.toString();
```

### 7.2 Realistic — StringBuilderTextBlockDemo

Full source: [`StringBuilderTextBlockDemo.java`](../../examples/core-java/stringbuilder-textblock-demo/StringBuilderTextBlockDemo.java).

```bash
javac StringBuilderTextBlockDemo.java && java StringBuilderTextBlockDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
Hello Java!
jsonLines=4
{
  "name": "Ada",
  "lang": "Java"
}
```

**Important lines:**

- **`append` chaining** returns `this` for fluent style.
- Text block JSON spans **four lines** per `lines().count()` including opening `{` line through closing `}`.
- **`System.out.print(json)`** preserves trailing newline from the block’s closing delimiter line unless stripped—demo output matches documented block.

### 7.3 Advanced — `formatted` with text block

```java
String tpl = """
    User: %s
    Role: %s
    """.formatted(name, role);
```

## 8. Real-World Use Cases

- **HTTP/CSV builders** — single pass over records.
- **SQL in tests** — readable multiline queries without external files.
- **OpenAPI/JSON fixtures** — text blocks in unit tests (Module 12+ data parsing later).

## 9. Internal Behavior

`StringBuilder` holds an internal **byte/char array** that **doubles** (approximately) when full—amortized **O(1)** append. **`toString()`** allocates the immutable `String`.

Text blocks are compiled to **constant pool entries** or bootstrap concat sequences like ordinary strings, with newline normalization per JLS.

## 10. Edge Cases and Limitations

- **Reusing one builder** — call **`setLength(0)`** or **`delete(0, length())`** to clear; shrinking capacity is not automatic.
- **Text block closing position** — wrong placement changes stripped indentation; always run once and diff output when migrating.
- **`StringBuilder` in concurrent code** — race without synchronization corrupts buffer.

## 11. Common Mistakes

### Incorrect — `+` in hot loop

```java
String s = "";
for (String p : parts) s += p;
```

### Corrected

```java
StringBuilder sb = new StringBuilder();
for (String p : parts) sb.append(p);
String s = sb.toString();
```

### Incorrect — forgetting `toString`

```java
Map<String, Object> m = Map.of("k", new StringBuilder("v"));  // value type not String
```

### Corrected

```java
Map.of("k", new StringBuilder("v").toString());
```

## 12. Best Practices

- Use **`StringBuilder`** when building in loops or unknown length.
- Use **text blocks** for multiline static templates; use **`stripIndent`** if you disable default stripping.
- For many small known pieces, compiler **`+`** is fine.
- Prefer **`StringJoiner`** / **`String.join`** for delimiter-separated lists when that fits (Module 12).

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| `StringBuilder` | Efficient mutation | Not immutable; not for map keys |
| `StringBuffer` | Thread-safe | Synchronization overhead |
| Text block | Readability | Whitespace rules learning curve |
| External template file | Non-code edits | I/O and classpath loading |

## 14. Performance Considerations

Pre-size builder when length estimate known. **`toString()`** copies—avoid calling it inside tight inner loops if you only need the buffer.

Text blocks have **no runtime penalty** vs equivalent concatenated literal at compile time.

## 15. Security Considerations

Building SQL or HTML by raw **`append` of user input** enables injection. Use parameterized queries and escaping libraries—builders do not sanitize.

## 16. Java Version and Compatibility Notes

- **`StringBuilder`** — since 1.5 (`StringBuffer` since 1.0).
- **Text blocks** — preview 13–14, **standard 15+**, unchanged baseline in **21**.

## 17. Related Language Comparisons

Kotlin triple-quoted strings resemble text blocks. Python `"""` multiline strings do not strip indentation the same way—port carefully.

## 18. Revision and Mastery

- **Gaps:** using `StringBuffer` “by default”; surprise whitespace in text blocks.
- **You should answer:** Why does `append` return `StringBuilder`?
- **Advanced:** `AbstractStringBuilder` shared by builder/string buffer; compact strings backing.

## 19. Exercises

### Beginner

1. Predict `jsonLines=` from the demo text block.
2. Build `"012"` with one builder and a loop (see Ch.04 demo).

### Intermediate

3. Rewrite a five-line `\n` concatenation as a text block; compare outputs.
4. When is `String.join(",", list)` better than a manual builder?

### Advanced

5. Explain incidental vs essential indentation with a sketch.

## 20. Interview Questions

1. `StringBuilder` vs `StringBuffer`?
2. When is `+` in a loop a problem?
3. What delimits a text block?

## 21. Solutions

1. `4`
2. Same as `EncodingPerfDemo` `built=` pattern
3. Output diff in comments; strip rules per JLS
4. When only joining known strings with one delimiter
5. Incidental aligns with closing `"""`; content lines share minimum indent removed

## 22. Chapter Summary

**`StringBuilder`** mutably assembles text efficiently. **Text blocks** make multiline literals maintainable with indentation processing. **`StringBuilderTextBlockDemo`** shows append chain and JSON block with **`lines().count()`**. Next: **encoding**, **UTF-8 bytes**, and **performance** discipline.

## 23. Key Terminology

StringBuilder · text block · append · incidental whitespace · `toString`

## 24. References and Further Reading

- [JEP 378: Text Blocks](https://openjdk.org/jeps/378)
- [JLS §3.10.7 — Text Blocks](https://docs.oracle.com/javase/specs/jls/se21/html/jls-3.html#jls-3.10.7)
- [`java.lang.StringBuilder`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StringBuilder.html)

## 25. Next Steps

- [04-encoding-and-string-performance.md](04-encoding-and-string-performance.md)
- Module 17+ — I/O and `Charset` APIs in depth

## Review Checklist

- [x] Learning objectives covered
- [x] StringBuilderTextBlockDemo verified Temurin 21.0.12.1
- [x] Text block JEP cited
- [x] Exercises and Revision and Mastery
