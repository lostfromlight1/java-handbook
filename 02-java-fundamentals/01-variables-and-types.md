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

A **variable** is a named storage location for a value of a given type.

```java
int count = 42;
```

| Piece | Role |
|-------|------|
| `int` | Type |
| `count` | Name |
| `42` | Initial value |

You can declare without initializing in some contexts, but local variables must be assigned before use (compiler-enforced).

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

`String` and arrays are **references**: the variable holds a handle to an object, not the object bits themselves (Module 00 preview; Module 05+ deepens).

```java
String label = "Java";
```

`String` is not a primitive—even though it looks basic.

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

Local variables: effectively must be initialized before read. Fields get default values (Module 05)—don’t rely on that confusion yet; initialize explicitly while learning.

## 7. Step-by-Step Examples

### 7.1 Minimal

```java
int lives = 3;
lives = lives - 1;
```

### 7.2 Realistic — primitives tour

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

### 7.3 Edge — `long` without `L`

`long big = 3000000000;` fails to compile: the literal is treated as `int` and is out of range. Use `3000000000L`.

## 8. Real-World Use Cases

Counters, money as integer cents (avoid binary float for currency—Module 20/`BigDecimal` later), feature flags (`boolean`), IDs as `long`.

## 9. Internal Behavior

Primitives are not objects; they have no methods. Autoboxing to wrapper types (`Integer`, …) appears later. Floating-point follows IEEE-754-style binary fractions—`0.1 + 0.2` surprises are normal (Module 02 ops / later numerics).

## 10. Edge Cases and Limitations

- Integer overflow wraps silently for `int`/`long` arithmetic (two’s complement)—validate ranges when needed.
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

Primitives avoid object headers; micro-optimizing `byte` vs `int` rarely matters—clarity first.

## 15. Security Considerations

Don’t trust external strings as numbers without parsing/validation (Modules 14, 43).

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
