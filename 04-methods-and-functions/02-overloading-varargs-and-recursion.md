# Overloading, Varargs, and Recursion

> **Status:** Completed  
> **Module:** 04 — Methods and Functions  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Define **method overloading** and explain how the compiler picks an overload.
- State why overload resolution ignores **return type alone**.
- Recognize **ambiguity** pitfalls when multiple overloads could apply.
- Use **varargs** (`Type... name`) for zero-or-more trailing arguments and know they become arrays inside the body.
- Write **recursive** methods with a **base case** and **recursive case**.
- Contrast recursion with iteration and explain `StackOverflowError` risk.
- Compare recursive and iterative `factorial` implementations for correctness and depth.

## 2. Prerequisites

- [01-methods-basics.md](01-methods-basics.md) — declarations, parameters, `return`, calling conventions.
- Module 03 — conditionals and loops (`for` used in varargs demo and iterative factorial).
- Arrays as a concept (Module 02+): varargs desugar to array parameters.

## 3. Why This Concept Exists

APIs evolve: sometimes you need `max(a, b)` and sometimes `max(a, b, c)` without inventing awkward names like `max3`. **Overloading** lets one **intent** (`max`, `join`) map to several parameter shapes. **Varargs** avoids explosive overload counts for “any number of trailing values.” **Recursion** expresses problems whose definition refers to itself (factorial, tree walks)—but Java’s call stack is finite, so engineers must know when iteration is safer.

## 4. Conceptual Explanation

### 4.1 Method overloading

**Overloading** means multiple methods in the **same class** share a **name** but differ in the **parameter list** (count and/or types). The compiler chooses the **most specific applicable** overload at compile time.

```java
static int max(int a, int b) { ... }
static int max(int a, int b, int c) { ... }
static double max(double a, double b) { ... }
```

The **signature** for resolution is name + parameter types (JLS §8.4.2). Return type is **not** part of that choice. These two declarations cannot coexist:

```java
// static int parse(String s);
// static double parse(String s);  // ERROR: methods have same erasure
```

### 4.2 How the compiler chooses

At a call site `max(3, 9)`:

1. Find overloads named `max` whose parameters are **applicable** (arguments assignable after conversions).
2. If more than one applies, pick the **most specific** (narrower parameter types win over wider).
3. If still tied, the call is **ambiguous** (compile error).

`max(3, 9)` picks `max(int, int)`. `max(1.5, 2.25)` picks `max(double, double)` because `3` and `9` are `int` literals but `1.5` is `double`.

### 4.3 Ambiguity pitfalls

Ambiguity appears when two overloads are equally specific—common with widening conversions and varargs:

```java
static void f(int x, long y) { }
static void f(long x, int y) { }
// f(1, 1);  // ambiguous
```

Another pattern: overloads that could match through varargs vs a fixed parameter. Design overload sets deliberately; add an unambiguous overload or rename when the compiler complains.

### 4.4 Varargs (`Type... identifier`)

A **variable arity** parameter is written with `...` after the type:

```java
static String join(String... parts) { ... }
```

**Meaning:**

- Callers may pass **zero or more** arguments of that type: `join()`, `join("a")`, `join("a", "b", "c")`.
- Inside the method, `parts` is a **array** (`String[]`).
- Only **one** varargs parameter per method, and it must be **last** in the parameter list.

```java
static String join(String... parts) {
    if (parts.length == 0) {
        return "";
    }
    ...
}
```

You may also pass an explicit array: `join(new String[] { "x", "y" })`.

### 4.5 Recursion

A **recursive** method calls **itself** (direct recursion) or another method that eventually calls back (indirect). Every recursive definition needs:

| Piece | Purpose |
|-------|---------|
| **Base case** | Stops recursion (no further self-call) |
| **Recursive case** | Smaller subproblem + self-call |

```java
static long factorial(int n) {
    if (n <= 1) {
        return 1L;           // base
    }
    return n * factorial(n - 1);  // recursive
}
```

Without a base case, calls never stop until the stack runs out.

### 4.6 Call stack growth (preview)

Each active call gets a **stack frame** (locals, return address). Recursive `factorial(5)` builds frames for `n = 5, 4, 3, 2, 1` then unwinds as each `return` completes. Depth equals recursion depth. [03-pass-by-value-and-stack-frames.md](03-pass-by-value-and-stack-frames.md) diagrams frames; here: **deeper recursion = more frames = more stack memory**.

### 4.7 Recursion vs iteration

| Approach | When it helps | Risk |
|----------|----------------|------|
| Recursion | Natural for trees, divide-and-conquer, short depth | `StackOverflowError` on deep input |
| Iteration | Numeric loops, long sequences, hot paths | Can be harder to read for nested structures |

```java
static long factorialIter(int n) {
    long result = 1L;
    for (int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}
```

For `factorial(5)`, both versions return `120`. For `factorial(1_000_000)`, recursion typically fails; iteration still runs (subject to `long` overflow, not stack).

### 4.8 Composing overloads

`max(int, int, int)` reuses `max(int, int)`:

```java
static int max(int a, int b, int c) {
    return max(max(a, b), c);
}
```

That is **not** recursion on one signature—it is overloading plus ordinary calls. True recursion is a method calling **itself** with the same name (`factorial`, `sumTo`).

## 5. Terminology

| Term | Meaning |
|------|---------|
| Overloading | Same method name, different parameter lists in one class |
| Applicable method | Overload that can accept the argument types (with conversions) |
| Most specific | Overload no other applicable overload is strictly more specific than |
| Ambiguous call | Two or more equally specific overloads |
| Varargs | `Type...` parameter accepting zero or more arguments |
| Recursion | Method invokes itself (directly or indirectly) |
| Base case | Recursion stop condition |
| Stack overflow | Too many nested calls; JVM throws `StackOverflowError` |

## 6. Syntax and Language Rules

**Overloading:**

- Differ by parameter count/types; not by return type alone.
- `static` vs instance matters for resolution but is not “overloading” by itself—you need distinct parameter lists.

**Varargs:**

```java
void m(int first, String... rest) { }
```

- `rest` is `String[]`.
- Cannot place varargs before required parameters.
- Cannot have two varargs parameters.

**Recursion:**

- Legal for any method to call itself if visibility allows.
- Tail-call optimization is **not** a JLS guarantee; do not rely on infinite depth.

## 7. Step-by-Step Examples

Full program: [examples/core-java/overloading-demo/](../examples/core-java/overloading-demo/)

```bash
javac OverloadingDemo.java
java OverloadingDemo
```

### 7.1 Minimal: two overloads of `max`

```java
static int max(int a, int b) {
    return a >= b ? a : b;
}

static double max(double a, double b) {
    return a >= b ? a : b;
}
```

Ternary picks the larger value; same logic, different numeric type overloads.

### 7.2 Realistic: third `int` overload and varargs `join`

```java
static int max(int a, int b, int c) {
    return max(max(a, b), c);
}

static String join(String... parts) {
    if (parts.length == 0) {
        return "";
    }
    StringBuilder sb = new StringBuilder(parts[0]);
    for (int i = 1; i < parts.length; i++) {
        sb.append(',').append(parts[i]);
    }
    return sb.toString();
}
```

`StringBuilder` is a mutable string buffer from the Java SE API (efficient repeated append). Zero arguments yield `""`; one yields that string alone; many join with commas.

### 7.3 Advanced: recursion `factorial`, `sumTo`, and iterative twin

```java
static long factorial(int n) {
    if (n < 0) {
        throw new IllegalArgumentException("n must be >= 0");
    }
    if (n <= 1) {
        return 1L;
    }
    return n * factorial(n - 1);
}

static int sumTo(int n) {
    if (n <= 0) {
        return 0;
    }
    return n + sumTo(n - 1);
}
```

`sumTo(4)` computes `4 + 3 + 2 + 1 + 0 = 10`. Negative `factorial` input fails fast with an exception instead of silent wrong answers.

**Verified output (Temurin JDK 21.0.12.1):**

```text
max(3, 9) = 9
max(3, 9, 2) = 9
max(1.5, 2.25) = 2.25
join() = ''
join("a") = 'a'
join("a", "b", "c") = 'a,b,c'
factorial(5) = 120
factorialIter(5) = 120
sumTo(4) = 10
```

## 8. Real-World Use Cases

- **Overloading** — `PrintStream`/`String` concatenation overloads; logging frameworks with `debug(String)` vs `debug(String, Object...)`.
- **Varargs** — `String.format("%s %s", a, b)`, `List.of(e1, e2, ...)`, test builders appending many IDs.
- **Recursion** — directory walks, JSON/XML trees, divide-and-conquer algorithms; production numeric code often prefers loops for depth safety.

## 9. Internal Behavior

**Overload resolution** happens at **compile time** (statically typed). The bytecode invokes a specific method descriptor; no runtime overload table per call (unlike some dynamic languages).

**Varargs** are implemented by the compiler packing trailing arguments into an array (synthetic array creation at call sites when you pass individual values).

**Recursion** uses the JVM stack (JVMS §2.6). HotSpot does not promise tail-call elimination for arbitrary Java methods; deep recursion remains a stack risk.

## 10. Edge Cases and Limitations

- **`factorial` overflow** — `long` overflows silently for large `n`; use `BigInteger` or `Math` libraries when range matters.
- **Varargs + generics** — heap pollution warnings with generic varargs (`List<String>...`); advanced topic (Module 09).
- **Null varargs** — passing explicit `null` without a type may fail to compile; `join((String[]) null)` can NPE inside the method.
- **`sumTo(0)`** — returns `0` (base); `sumTo(-1)` also returns `0` with the given base—document intended domain.

## 11. Common Mistakes

### Incorrect — overload differing only by return type

```java
// static int parse(String s) { return Integer.parseInt(s); }
// static long parse(String s) { return Long.parseLong(s); }  // compile error
```

### Corrected

Different names or different parameters: `parseInt(String)` vs `parseLong(String)`.

### Incorrect — varargs not last

```java
// static void bad(String... a, int x) { }  // illegal
```

### Corrected

```java
static void good(int x, String... a) { }
```

### Incorrect — missing base case

```java
static int forever(int n) {
    return n + forever(n - 1);  // never stops → StackOverflowError
}
```

### Corrected

```java
static int safe(int n) {
    if (n <= 0) {
        return 0;
    }
    return n + safe(n - 1);
}
```

### Incorrect — confusing overload with override

Overloading is same-class name reuse; **override** is subclass replacing instance behavior (Module 07). Different ideas.

## 12. Best Practices

- Overload for **clearer call sites**, not micro-optimizations.
- Prefer **one varargs** method over dozens of arity-specific overloads when arguments are homogeneous.
- For numeric kernels and very deep input, prefer **iteration**.
- Validate arguments (`n >= 0`) at API boundaries.
- When overload sets grow confusing, switch to a **builder** or **options object** (later modules).

## 13. Design Trade-offs

| Technique | Pros | Cons |
|-----------|------|------|
| Overloading | Familiar call syntax | Ambiguity, harder API docs |
| Varargs | Flexible arity | Array allocation; weaker typing at call site |
| Recursion | Matches recursive data | Stack depth, performance overhead |
| Iteration | Stack-safe, often faster | Manual state (`result`, loop index) |

## 14. Performance Considerations

Varargs calls may allocate an array each time (JVM may optimize some patterns). Recursive factorial is O(n) time but O(n) stack depth; iterative is O(n) time, O(1) stack. Choose iteration for hot, deep paths.

## 15. Security Considerations

Unbounded recursion can be a **denial-of-service** vector if user input controls depth (malicious huge `n` for naive recursive parsers). Cap depth or use iterative algorithms in networked code.

## 16. Java Version and Compatibility Notes

Overloading, varargs (Java 5+), and recursion are stable in **Java 21**. Demo verified on **21.0.12.1**.

## 17. Related Language Comparisons

- **C++** — overload resolution with templates and ADL is more complex; Java’s rules are simpler and compile-time only.
- **Python** — default arguments and `*args` resemble varargs but are dynamic; last definition wins, not static overloads.
- **Functional languages** — heavy tail-call optimization; Java engineers should not assume it.

## 18. Revision and Mastery

**You should already understand:**

- Why `max(3, 9, 2)` binds to the three-`int` overload.
- That `join()` sees `parts.length == 0`.
- That `factorial(5)` and `factorialIter(5)` match but differ in stack use.

**Common gaps:**

- Believing return type participates in overload resolution.
- Forgetting varargs parameter is already an array (mutating it affects callers if they passed a shared array reference).
- Using recursion for linear loops without reason.

**Answer without running:**

- `sumTo(1)` → `1`
- `join("only")` → `'only'` in demo quoting style → `only` as value

**Stretch:** Rewrite `sumTo` iteratively and argue which you would ship in a payment ledger service.

## 19. Exercises

### Beginner

1. Run `OverloadingDemo` and match each line to an overload or recursive method.
2. Add `static int min(int a, int b)` mirroring `max`; overload `min` for three `int`s.
3. Call `join()` with no args and with three string literals; predict output.

### Intermediate

4. Add `static String join(char delimiter, String... parts)` without breaking existing calls (overload).
5. Write iterative `sumTo` and assert same results for `n` in `0..10`.
6. Create an ambiguous overload pair (widening `int`/`long`) and fix it.

### Advanced

7. Implement recursive `pow(int base, int exp)` for `exp >= 0`; compare stack depth to a loop.
8. Explain why `factorial(20)` still fits in `long` but `factorial(21)` overflows—compute boundary.

## 20. Interview Questions

1. **Can two methods differ only by return type?** — No, in the same class with same parameters.
2. **What is `String...` inside the method?** — `String[]`.
3. **When does recursion throw `StackOverflowError`?** — When stack space for frames is exhausted (typically unbounded or very deep recursion).
4. **Overload vs override?** — Overload: same class, different parameters; override: subclass, same signature, different body.

## 21. Solutions

1. See §7.3 output mapping in chapter text.
2. `min` uses `a <= b ? a : b`; three-arg version composes like `max`.
3. Empty string and `a,b,c` (no comma when one element).
4. Example: overload with delimiter parameter; loop inserts `delimiter` between elements.
5. `for (int i = 1; i <= n; i++) result += i;` with base `n <= 0` → `0`.
6. Remove one overload or change parameter types so one is strictly more specific; or cast at call site.
7. Base `exp == 0` → `1`; recursive `base * pow(base, exp - 1)`; loop multiplies `exp` times.
8. `21!` exceeds `Long.MAX_VALUE`; `20!` is `2432902008176640000L` (fits).

## 22. Chapter Summary

Overloading lets one name cover multiple parameter shapes resolved at compile time; return type alone never distinguishes overloads. Varargs absorb trailing arguments as an array with a flexible arity. Recursion needs base and recursive cases and consumes stack depth—mirror critical numeric work with iteration when depth or performance matters. The demo ties `max`, `join`, `factorial`, and `sumTo` together with verified JDK 21 output.

## 23. Key Terminology

Overloading · signature · applicability · ambiguity · varargs · base case · recursive case · stack frame · `StackOverflowError`

## 24. References and Further Reading

- [JLS §8.4.2 — Method Signature](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.2)
- [JLS §15.12.2 — Compile-Time Step 3: Identify Applicable Methods](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.12.2)
- [JLS §8.4.1 — Formal Parameters](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.1) (varargs)
- [JVMS §2.6 — Frames](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.6)

## 25. Next Steps

- [03-pass-by-value-and-stack-frames.md](03-pass-by-value-and-stack-frames.md) — what copying means for recursive depth and parameters.
- [04-static-instance-and-method-references.md](04-static-instance-and-method-references.md) — overloads on instances and functional style.

## Review Checklist

- [x] Overloading, varargs, recursion depth gate met
- [x] OverloadingDemo verified on Temurin JDK 21.0.12.1
- [x] Ambiguity, stack risk, factorial comparison covered
- [x] Revision and Mastery, exercises, solutions, JLS/JVMS references
