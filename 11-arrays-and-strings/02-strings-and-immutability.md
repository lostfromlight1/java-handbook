# Strings and Immutability

> **Status:** Completed  
> **Module:** 11 — Arrays and Strings  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

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

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Immutability** | State cannot change after construction |
| **String pool** | Special set of interned literals / interned strings |
| **Identity** | Same object reference (`==`) |
| **Content equality** | Same character sequence (`equals`) |
| **Code unit** | 16-bit `char` in `String`’s sequence |
| **Literal** | `"..."` in source |

## 6. Syntax and Language Rules

```java
String empty = "";
String multi = "line1\nline2";
String fromChars = new String(new char[] { 'A', 'd', 'a' });

if (s.equals("expected")) { /* ... */ }  // not s == "expected"
```

- **`+` concatenation** uses `StringBuilder` under the compiler for simple cases; still creates new strings.
- **`String` implements `CharSequence`** — polymorphic APIs accept `String`.

## 7. Step-by-Step Examples

### 7.1 Minimal — immutability

```java
String path = "/api";
path = path + "/v1";   // new String bound to path; old "/api" unchanged if referenced elsewhere
```

### 7.2 Realistic — StringsDemo

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

### 7.3 Advanced — `formatted`

```java
String msg = "Hello %s, jdk %d".formatted("Ada", 21);
```

Prefer **`formatted`** / `String.format` over manual concatenation for structured messages.

## 8. Real-World Use Cases

- **Map keys** — immutable strings are safe keys if `equals`/`hashCode` stable.
- **Config and env** — compare with `equals`, never `==` unless identity is intentional.
- **SQL / logging** — build messages with care; immutability prevents accidental mutation after passing to another layer.

## 9. Internal Behavior

`String` in modern JDKs uses **compressed strings** (byte array + coder flag) for Latin-1 / UTF-16 backing where enabled (JEP 254, JDK 9+). This is **implementation detail**—logical model remains Unicode code unit sequence.

Literal loading uses the constant pool; **`invokedynamic`** / concat factories optimize dynamic concatenation (JEP 280+ evolution).

## 10. Edge Cases and Limitations

- **`null`** — calling `s.equals(x)` if `s` is null throws **`NullPointerException`**; use **`"expected".equals(s)`** or **`Objects.equals`**.
- **Supplementary characters** — one code point may use **two** `char` values (surrogate pair); `length()` counts **code units**, not grapheme clusters (Ch.04).
- **`toUpperCase()`** without **`Locale`** — uses default locale; can surprise with Turkish `i`.

## 11. Common Mistakes

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

## 12. Best Practices

- Use **`equals`** / **`equalsIgnoreCase`** with documented locale rules.
- Use **`isEmpty()`** / **`isBlank()`** (11+) for validation.
- Prefer **`text blocks`** or builders for large assembly (Ch.03).
- Do not call **`new String(String)`** without reason—it usually wastes an extra object.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Immutable `String` | Safe sharing, compact VM optimizations | Each edit allocates |
| Pooling literals | Memory savings for repeated text | Identity surprises with `==` |
| `substring` (historical) | Fast views (pre-JDK 7u6 shared array) | Modern copies—predictable but allocating |

## 14. Performance Considerations

Repeated **`+` in loops** creates many interim strings—use **`StringBuilder`** (Ch.03). **`length()`** and **`charAt`** are constant time per access.

## 15. Security Considerations

Immutability helps when passing tokens or file paths you do not want callee code to modify—**they still must validate content**. Never compare security secrets with **`==`** on dynamically created strings.

## 16. Java Version and Compatibility Notes

`String` since 1.0. **`isBlank`**, **`strip`**, **`lines`** — Java 11+. **Text blocks** — 15+ (Ch.03). **String templates** (preview in later JDKs) are **not** final in Java 21—do not teach as stable API.

## 17. Related Language Comparisons

JavaScript strings are also immutable. Python `str` is immutable. C# `string` is immutable. None of these make `==`/`===` behave like Java’s reference `==` vs `equals`.

## 18. Revision and Mastery

- **Gaps:** using `==` after `new String`; ignoring locale in case folding.
- **You should answer:** Why is `String` safe to share across threads without synchronization?
- **Advanced:** `StringLatin1` vs `StringUTF16` internals; `equals` short-circuit on length.

## 19. Exercises

### Beginner

1. Predict all `StringsDemo` lines without running.
2. When is `"a" == "a"` true?

### Intermediate

3. Write `startsWithIgnoreCase(String s, String prefix)`.
4. Explain why map keys should not be `StringBuilder`.

### Advanced

5. When does `substring` share backing storage vs copy (post-Java 7u6 behavior)?

## 20. Interview Questions

1. Immutable vs final class?
2. Difference between string pool and heap `String`?
3. Why not use `==` for user input?

## 21. Solutions

1. See verified output block above.
2. When both are same interned literal (or explicit `intern`).
3. Locale-aware prefix loop or `regionMatches`.
4. Mutable keys would break map invariants if content changes after insert.
5. Modern JDK copies for predictable isolation—do not rely on shared backing.

## 22. Chapter Summary

**`String`** is **immutable** UTF-16 text. Use **`equals`** for content, **`==`** only for intentional identity. **`StringsDemo`** contrasts **pool** literals with **`new String`**. Common methods transform text by returning **new** instances. Next: **`StringBuilder`** and **text blocks** for efficient and readable construction.

## 23. Key Terminology

Immutability · string pool · equals · literal · code unit · `CharSequence`

## 24. References and Further Reading

- [JLS §3.10.5 — String Literals](https://docs.oracle.com/javase/specs/jls/se21/html/jls-3.html#jls-3.10.5)
- [Java SE `java.lang.String`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/String.html)

## 25. Next Steps

- [03-stringbuilder-and-text-blocks.md](03-stringbuilder-and-text-blocks.md)
- Module 06 Ch.04 — `equals`/`hashCode` on immutable fields

## Review Checklist

- [x] Learning objectives covered
- [x] StringsDemo verified Temurin 21.0.12.1
- [x] Depth gate; identity vs equals emphasized
- [x] Revision and Mastery included
