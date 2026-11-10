# Variables and Types

> **Status:** Completed  
> **Module:** 02 — Java Fundamentals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare variables and constants (`final`) with clear names.
- List Java’s eight primitive types and when to use each at a beginner level.
- Contrast primitives with reference types (`String`, arrays).
- Use numeric and character literals, including underscores and suffixes (`L`, `f`).
- Explain why Java’s static typing catches many mistakes before runtime.

## 2. Prerequisites

- Module 01 (JDK tooling, `main`, packages)
- Module 00 data/state ideas

## 3. Why This Concept Exists

Programs manipulate **typed** data. If you do not understand variables and types, every later topic—methods, objects, generics—feels like memorization. Types are how Java documents intent and rejects nonsense early.

## 4. Conceptual Explanation

### 4.1 Variables

A **variable** is a named storage location for a value of a given type. The **type** is not decoration: it tells the compiler which operations are legal, how much space the value needs, and (for references) what kind of object the name may point at. That is why Java is **statically typed**—many category errors are caught at compile time instead of failing in production.

```java
int count = 42;
```

| Piece | Role |
|-------|------|
| `int` | Type |
| `count` | Name |
| `42` | Initial value |

**Local variables** are declared inside a method or block. They exist only while that block runs and must be **definitely assigned** before you read them (the compiler enforces this).

**Fields** (also called instance variables or class variables) are declared on a type and belong to an object or class. They can live for the whole lifetime of that object. Module 05 covers fields in depth; at this stage, know that locals and fields follow different initialization rules (see §4.7 and §6).

You can declare without assigning in some contexts (for example `int x;` as a field), but a **local** must receive a value on every path before its first use.

### 4.2 Constants with `final`

```java
final int MAX_SCORE = 100;
```

`final` means the variable cannot be reassigned. Prefer named constants over magic numbers.

### 4.3 Primitive types

| Type | Kind | Typical use | Size (JLS) |
|------|------|-------------|------------|
| `byte` | integer | Rare; compact storage | 8-bit |
| `short` | integer | Rare | 16-bit |
| `int` | integer | Default whole numbers | 32-bit |
| `long` | integer | Large whole numbers | 64-bit |
| `float` | floating | Rare; prefer `double` | 32-bit IEEE |
| `double` | floating | Default decimals | 64-bit IEEE |
| `char` | character | Single UTF-16 code unit | 16-bit |
| `boolean` | truth | `true` / `false` | not numeric |

**Beginner defaults:** `int`, `long`, `double`, `boolean`, `char` when needed. Reach for `byte`/`short`/`float` only with a reason.

### 4.4 Reference types (first contact)

`String` and arrays are **reference types**: the variable does not hold the string’s characters inline. It holds a **reference** (a handle) to an object that lives elsewhere. The JVM compares references when you use `==` on objects; **value** comparison for `String` uses `equals` (Module 06 deepens this).

**Value vs reference in one contrast:**

```java
int n = 5;
n = 6;           // n’s slot now holds 6; the old 5 is gone from this variable

String a = "hi";
String b = a;    // b references the same object as a
b = b + "!";     // b may now reference a new "hi!"; a may still be "hi"
```

Primitives store the bits of the value in the variable’s slot. Reference variables store a reference to a heap object (or `null`). Both kinds are typed; only references can be `null`.

```java
String label = "Java";
```

`String` is not a primitive—even though it looks basic. Literals like `"Java"` are still real objects with methods such as `length()`.

### 4.5 Literals

```java
int n = 1_000_000;   // underscores OK for readability
long big = 3_000_000_000L; // L required when value exceeds int range
float f = 3.14f;     // f suffix
double d = 3.14;     // default floating literal is double
char c = 'J';
boolean ok = true;
```

### 4.6 Static typing

Java checks types at compile time. `"hi" + 1` becomes string concatenation; `"hi" * 2` is a compile error (unlike some dynamic languages). That discipline scales to large codebases.

### 4.7 Field default values (preview)

If you declare a **field** and omit an initializer, Java assigns a **default** before your code runs. **Locals** have no default—you must assign explicitly.

| Type kind | Default when uninitialized as a field |
|-----------|----------------------------------------|
| Numeric primitives (`byte`, `short`, `int`, `long`, `float`, `double`) | `0` (or `0.0` for floating) |
| `boolean` | `false` |
| `char` | `'\u0000'` (null character) |
| Reference types (`String`, arrays, any class) | `null` |

Module 05 explains fields, constructors, and when relying on defaults is unsafe. While learning, **initialize fields and locals on purpose** so intent stays visible.

### 4.8 Autoboxing (preview)

Primitives have matching **wrapper** classes (`int` → `Integer`, `boolean` → `Boolean`, …). When an `int` is needed where an `Integer` is expected (for example a generic collection), the compiler inserts **autoboxing**: `Integer.valueOf(n)`. The reverse is **unboxing**. Autoboxing is convenient but can hide `NullPointerException` if you unbox a null `Integer`. Wrappers, identity, and collections are covered in Module 05 and Module 09; for now, prefer primitives in simple locals and know that `Integer` is an object, not a primitive.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Variable | Named typed storage |
| Primitive | Built-in non-reference value type |
| Reference type | Type whose values are references to objects |
| Literal | Source representation of a fixed value |
| `final` | Not reassignable (for variables) |

## 6. Syntax and Language Rules

```java
type name = expression;
type name;
name = expression;
```

Local variables: must be initialized before read on every code path. Fields receive the defaults in §4.7 if you omit an initializer—initialize explicitly while learning.

## 7. Step-by-Step Examples

### 7.1 Minimal — counter in a local

Locals are the usual place for short-lived state inside a method:

```java
int lives = 3;
lives = lives - 1; // still int; assignment replaces the value in lives
```

### 7.2 Realistic — fields on a tiny profile

Fields hold data that outlives one statement (full rules in Module 05):

```java
class UserProfile {
    String displayName = "Guest"; // reference field; default would be null
    int loginCount;                 // field default 0 without writing "= 0"
    final int maxSessions = 5;    // final field: assign once
}
```

Explicit initializers like `displayName = "Guest"` avoid accidentally shipping `null` names.

### 7.3 Primitives tour (runnable)

[examples/core-java/primitives-demo/](../examples/core-java/primitives-demo/)

**Verified output:**

```text
byte=100
short=10000
int=42
long=3000000000
float=3.14
double=3.141592653589793
char=J
boolean=true
String=Java
```

### 7.4 Edge — `long` literal without `L`

`long big = 3000000000;` fails to compile: the literal is treated as `int` and is out of range. Use `3000000000L` (as in PrimitivesDemo).

## 8. Real-World Use Cases

- **Counters** — `int` or `long` locals (`requestCount`, `retriesRemaining`).
- **Money as integer cents** — store `$12.34` as `int cents = 1234` (or `long` for large ledgers). Floating `double` cannot represent every decimal exactly; ledgers and taxes need exact arithmetic (`BigDecimal` in Module 20). At fundamentals level: prefer whole minor units over `double` for currency.
- **Feature flags** — `boolean enabled`.
- **IDs** — `long` when values can exceed ~2 billion (`int` range).

## 9. Internal Behavior

Primitives are not objects; they have no methods (until autoboxed). Floating-point follows IEEE-754-style binary fractions—`0.1 + 0.2` surprises are normal (operators chapter and later numerics).

**Integer overflow (two’s complement).** For `int` and `long`, arithmetic is wrapped modulo 2³² or 2⁶⁴ per the JLS—there is no exception on overflow. Adding one past the maximum value flips to the minimum:

```java
int wrapped = Integer.MAX_VALUE + 1; // -2147483648 (same as Integer.MIN_VALUE)
```

Verified on JDK 21: `Integer.MAX_VALUE + 1` prints `-2147483648`. Financial and security code must check ranges or use wider types / `BigInteger` when overflow is unacceptable.

## 10. Edge Cases and Limitations

- Integer overflow wraps silently for `int`/`long` arithmetic (two’s complement)—validate ranges when needed (see §9).
- `char` is a code unit, not a full Unicode code point for all characters (Module 11).
- `null` applies to references, not primitives.

## 11. Common Mistakes

### Incorrect

Using `float` for money.

### Corrected

Integer minor units or `BigDecimal` later.

### Incorrect

`String s = null;` then using without check.

### Corrected

Initialize meaningfully; null handling Module 18.

## 12. Best Practices

- Prefer `int`/`double`/`boolean` defaults.
- Name by meaning (`itemCount`, not `n1`).
- Use `final` for fixed domain values.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Primitive `int` | Fast, simple | No `null`; overflow |
| `Integer` object | Nullable, collections | Later complexity |
| `double` | Convenient decimals | Binary rounding |

## 14. Performance Considerations

Primitives live directly in stack slots or inside objects without per-value object headers. That keeps access cheap and GC pressure low compared with boxing every number in `Integer`. Micro-optimizing `byte` vs `int` for CPU rarely matters on modern HotSpot—prefer `int` unless you have a measured memory layout reason. Hot paths that mix primitives and collections will autobox; be aware when profiling (Module 09).

## 15. Security Considerations

Types are not validation. A variable typed `int` can still hold attacker-controlled data after parsing. Reject or bound-check external input before arithmetic (overflow can turn a large positive into a negative). Never parse secrets from strings in logs. Parsing user text into numbers and validating ranges is covered with I/O and web modules; here, remember that **static typing prevents syntax errors, not malicious values**.

## 16. Java Version and Compatibility Notes

Primitives are foundational. Underscores in literals since Java 7. Validated on **21.0.9**.

## 17. Related Language Comparisons

Unlike Python, variables have declared types. Unlike C, no unsigned integer primitives in standard Java.

## 18. Revision and Mastery

- Default values of fields vs locals  
- Wrapper identity vs equality  
- `char` vs code points  

## 19. Exercises

### Beginner

1. Declare `final double TAX_RATE = 0.05;` and print it.
2. Run PrimitivesDemo; change `long` to drop `L` and read the error.

### Intermediate

3. Explain why `String` is listed separately from primitives.
4. Store your age in an appropriate type and print it.

### Advanced

5. Research integer overflow for `Integer.MAX_VALUE + 1` and record the result.
6. When would you choose `long` over `int` for IDs?

## 20. Interview Questions

1. **List primitive types.** — byte short int long float double char boolean.  
2. **Is String a primitive?** — No.  
3. **What does final on a variable mean?** — Cannot reassign.  

## 21. Solutions

1–2. Compile error without `L` for out-of-range int literal.  
3. Reference type / object.  
5. Wraps to `Integer.MIN_VALUE`.  
6. Values that may exceed ~2 billion.

## 22. Chapter Summary

Variables hold typed values. Master the eight primitives, literals, `final`, and the primitive vs `String`/array distinction.

## 23. Key Terminology

Variable · primitive · reference · literal · final · static typing

## 24. References and Further Reading

- [JLS — types, values, variables](https://docs.oracle.com/javase/specs/)
- [JDK 21 API](https://docs.oracle.com/en/java/javase/21/docs/api/)

## 25. Next Steps

[02-operators-and-expressions.md](02-operators-and-expressions.md)

## Review Checklist

- [x] PrimitivesDemo verified on JDK 21.0.9
- [x] Depth gate passed
