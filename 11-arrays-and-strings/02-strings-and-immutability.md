# Strings and Immutability

> **Status:** Completed  
> **Module:** 11 — Arrays and Strings  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3 (immutability, literals, `equals` / pooling)
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. Do **not** manufacture content to fill headings. For omitted sections, record the reason in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow *(or merge note)* | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs *(strongly encouraged for Technical / Deep-dive)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain **`String`** as an **immutable** sequence of UTF-16 **code units**.
- Compare **`==`** (reference identity) with **`equals`** (character sequence).
- Describe **string literal pooling** and when **`new String`** breaks identity.
- Use common APIs: **`length`**, **`isEmpty`**, **`charAt`**, **`substring`**, **`toUpperCase`**, **`strip`**, **`formatted` / `format`**.
- Recognize when immutability helps **thread safety** and **hashing** (Module 06 Ch.04).

## 2. Prerequisites

- **Module 02 Ch.02** — `==` on references vs value comparison for primitives.
- **Module 03** — loops for character processing when needed.
- **Module 06** — `equals`/`hashCode` contracts.

**`char` in Java** is a 16-bit UTF-16 **code unit** (not always one Unicode **code point**—Ch.04).

## 3. Why This Concept Exists

Text is everywhere: HTTP, JSON, logs, UI. Java models text as **`java.lang.String`**, a **final** class you cannot subclass for security and optimization. **Immutability** means after construction, the character sequence does not change—every “change” produces a **new** `String`. That makes sharing safe: multiple threads can read the same instance without locks.

## 4. Conceptual Explanation

### 4.1 Immutability

```java
String s = "java";
String t = s.toUpperCase();  // new String "JAVA"; s still "java"
```

No `setCharAt` on `String`. APIs return new instances or views that obey the same rules.

### 4.2 Identity vs content

`==` asks whether two references point to the **same object**. **`equals`** asks whether two strings have the same **length and characters in order**.

Literals with the same content are often **interned** into the string pool, so `==` may appear to work—**do not rely on that** for business logic.

### 4.3 String pool

Compile-time literals such as `"java"` may be placed in a **pool** (JVMS §2.4.6). Another literal `"java"` may reuse that instance. **`new String("java")`** typically creates a **heap** object with the same characters but a **different identity**.

### 4.4 `intern()`

`s.intern()` returns a pooled canonical copy; useful rarely; can affect memory and class-loader behavior in advanced scenarios.

### 4.5 Comparison methods

- **`equals(Object)`** — `false` for `null`.
- **`equalsIgnoreCase`** — locale-sensitive pitfalls; prefer **`toLowerCase(Locale)`** with explicit locale for stable ordering when needed.

## 5. Mechanism and Execution Flow

`String` behavior is **immutable state** plus **reference sharing** through the pool—never confuse the two layers.

### 5.1 Literal and `new String` construction

1. A **literal** `"Java"` may be **interned** (constant pool / `String.intern` semantics)—multiple references can share one object.
2. **`new String("Java")`** always constructs a **fresh heap `String`** (unless optimized away in dead code)—`==` can differ from a literal reference even when `equals` is true.
3. After construction, **no public API** mutates the `byte[]`/`char[]` backing sequence in place.

### 5.2 Concatenation and transforming methods

For `s.toUpperCase()` or `s + "!"`:

1. The receiver **`String` instance is unchanged**—immutability is per object.
2. The operation allocates a **new `String`** with the transformed sequence (or uses builder optimizations under the hood for `+` chains).
3. Callers must **reassign** or use the return value; ignoring the result leaves the original text.

### 5.3 Equality checks (`StringsDemo`)

1. **`==`** compares **references** (identity)—true only when both sides point at the same object (pool/literal effects matter).
2. **`equals`** compares **logical character sequence**—works across distinct objects with same content.
3. **`hashCode`** is derived from content so equal strings hash the same—critical for `HashMap` keys in Module 12.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Immutability** | State cannot change after construction |
| **String pool** | Special set of interned literals / interned strings |
| **Identity** | Same object reference (`==`) |
| **Content equality** | Same character sequence (`equals`) |
| **Code unit** | 16-bit `char` in `String`’s sequence |
| **Literal** | `"..."` in source |

## 7. Syntax and Language Rules

```java
String empty = "";
String multi = "line1\nline2";
String fromChars = new String(new char[] { 'A', 'd', 'a' });

if (s.equals("expected")) { /* ... */ }  // not s == "expected"
```

- **`+` concatenation** uses `StringBuilder` under the compiler for simple cases; still creates new strings.
- **`String` implements `CharSequence`** — polymorphic APIs accept `String`.

## 8. Step-by-Step Examples

### 8.1 Minimal — immutability

```java
String path = "/api";
path = path + "/v1";   // new String bound to path; old "/api" unchanged if referenced elsewhere
```

### 8.2 Realistic — StringsDemo

Full source: [`StringsDemo.java`](../../examples/core-java/strings-demo/StringsDemo.java).

```bash
javac StringsDemo.java && java StringsDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
a==b=true
a==c=false
a.equals(c)=true
a=java
upper=JAVA
sub=hand
```

**Important lines:**

- `String a = "java"; String b = "java";` — likely **same pooled** reference → `a==b` true.
- `new String("java")` — **new object** → `a==c` false, **`equals`** true.
- **`substring(0, 4)`** on `"handbook"` → `"hand"` (end index exclusive).

### 8.3 Advanced — `formatted`

```java
String msg = "Hello %s, jdk %d".formatted("Ada", 21);
```

Prefer **`formatted`** / `String.format` over manual concatenation for structured messages.

## 9. Real-World Use Cases

- **Map keys** — immutable strings are safe keys if `equals`/`hashCode` stable.
- **Config and env** — compare with `equals`, never `==` unless identity is intentional.
- **SQL / logging** — build messages with care; immutability prevents accidental mutation after passing to another layer.

## 10. Internal Behavior

`String` in modern JDKs uses **compressed strings** (byte array + coder flag) for Latin-1 / UTF-16 backing where enabled (JEP 254, JDK 9+). This is **implementation detail**—logical model remains Unicode code unit sequence.

Literal loading uses the constant pool; **`invokedynamic`** / concat factories optimize dynamic concatenation (JEP 280+ evolution).

## 11. Edge Cases and Limitations

- **`null`** — calling `s.equals(x)` if `s` is null throws **`NullPointerException`**; use **`"expected".equals(s)`** or **`Objects.equals`**.
- **Supplementary characters** — one code point may use **two** `char` values (surrogate pair); `length()` counts **code units**, not grapheme clusters (Ch.04).
- **`toUpperCase()`** without **`Locale`** — uses default locale; can surprise with Turkish `i`.

## 12. Common Mistakes

### Incorrect — `==` for content

```java
String input = readLine();
if (input == "quit") { /* never true for typical input */ }
```

### Corrected

```java
if ("quit".equals(input)) { /* ... */ }
```

### Incorrect — mutating assumption

```java
char[] data = s.toCharArray();
data[0] = 'X';  // does not change s
```

### Corrected

Treat `String` as read-only; copy to `char[]` only when you need a mutable buffer and understand the copy is independent.

## 13. Best Practices

- Use **`equals`** / **`equalsIgnoreCase`** with documented locale rules.
- Use **`isEmpty()`** / **`isBlank()`** (11+) for validation.
- Prefer **`text blocks`** or builders for large assembly (Ch.03).
- Do not call **`new String(String)`** without reason—it usually wastes an extra object.

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Immutable `String` | Safe sharing, compact VM optimizations | Each edit allocates |
| Pooling literals | Memory savings for repeated text | Identity surprises with `==` |
| `substring` (historical) | Fast views (pre-JDK 7u6 shared array) | Modern copies—predictable but allocating |

## 15. Performance Considerations

Repeated **`+` in loops** creates many interim strings—use **`StringBuilder`** (Ch.03). **`length()`** and **`charAt`** are constant time per access.

## 16. Security Considerations

Immutability helps when passing tokens or file paths you do not want callee code to modify—**they still must validate content**. Never compare security secrets with **`==`** on dynamically created strings.

## 17. Java Version and Compatibility Notes

`String` since 1.0. **`isBlank`**, **`strip`**, **`lines`** — Java 11+. **Text blocks** — 15+ (Ch.03). **String templates** (preview in later JDKs) are **not** final in Java 21—do not teach as stable API.

## 18. Related Language Comparisons

JavaScript strings are also immutable. Python `str` is immutable. C# `string` is immutable. None of these make `==`/`===` behave like Java’s reference `==` vs `equals`.

## 19. Revision and Mastery

- **Gaps:** using `==` after `new String`; ignoring locale in case folding.
- **You should answer:** Why is `String` safe to share across threads without synchronization?
- **Advanced:** `StringLatin1` vs `StringUTF16` internals; `equals` short-circuit on length.

## 20. Exercises

### Beginner

1. Predict all `StringsDemo` lines without running.
2. When is `"a" == "a"` true?

### Intermediate

3. Write `startsWithIgnoreCase(String s, String prefix)`.
4. Explain why map keys should not be `StringBuilder`.

### Advanced

5. When does `substring` share backing storage vs copy (post-Java 7u6 behavior)?

## 21. Interview Questions

1. Immutable vs final class?
2. Difference between string pool and heap `String`?
3. Why not use `==` for user input?

## 22. Solutions

1. See verified output block above.
2. When both are same interned literal (or explicit `intern`).
3. Locale-aware prefix loop or `regionMatches`.
4. Mutable keys would break map invariants if content changes after insert.
5. Modern JDK copies for predictable isolation—do not rely on shared backing.

## 23. Chapter Summary

**`String`** is **immutable** UTF-16 text. Use **`equals`** for content, **`==`** only for intentional identity. **`StringsDemo`** contrasts **pool** literals with **`new String`**. Common methods transform text by returning **new** instances. Next: **`StringBuilder`** and **text blocks** for efficient and readable construction.

## 24. Key Terminology

Immutability · string pool · equals · literal · code unit · `CharSequence`

## 25. References and Further Reading

- [JLS §3.10.5 — String Literals](https://docs.oracle.com/javase/specs/jls/se21/html/jls-3.html#jls-3.10.5)
- [Java SE `java.lang.String`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/String.html)

## 26. Next Steps

- [03-stringbuilder-and-text-blocks.md](03-stringbuilder-and-text-blocks.md)
- Module 06 Ch.04 — `equals`/`hashCode` on immutable fields

## Review Checklist

**Mandatory Depth Review:** Technical profile; immutable `String` identity vs content at **Level 3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 where demos exist)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **Compact strings / coder byte[] layout** — OpenJDK implementation detail named in §10 only.

**Verification note:** `StringsDemo` (pool/`equals` lines; `upper=JAVA`; `sub=hand`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
