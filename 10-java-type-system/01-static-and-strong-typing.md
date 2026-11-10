# Static and Strong Typing

> **Status:** Completed  
> **Module:** 10 — Java Type System  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Level 3 (static vs strong typing, declared type vs runtime class)  
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

- Distinguish **static typing** (types fixed at compile time) from **dynamic typing**.
- Explain **strong typing** in Java: limited implicit conversions between unrelated types.
- Relate **compile-time type** (declared type) to **runtime class** (actual object class).
- Predict what `javac` rejects versus what the JVM runs.
- Use superclass/interface references (`Number`, `CharSequence`) with subtype instances safely at the API boundary.

## 2. Prerequisites

- **Module 02 Ch.01** — primitive vs reference types, wrappers.
- **Module 02 Ch.04** — widening, narrowing, `var` (still static).
- **Module 05 Ch.03** — polymorphism: variable type vs object class.
- **Module 07 Ch.03** — dynamic dispatch on instance methods.

Brief refresher: a **reference variable** has a **declared type** the compiler uses for every call. The **object** it points at has a **runtime class** chosen when you wrote `new` or unboxed/wrapped values.

## 3. Why This Concept Exists

Programs mix **numbers**, **text**, **domain records**, and **collections**. Without a type system, you could store anything in anything and only discover mistakes when a method is missing—or when data corrupts silently.

Java’s answer is **static** and **strong** typing:

- The compiler checks that you assign compatible types, call methods that exist, and pass arguments that match **before** you ship bytecode.
- Many incorrect programs never reach production because `javac` fails with a clear error.

That is a deliberate trade: more upfront discipline in exchange for fewer mysterious runtime failures in large codebases.

## 4. Conceptual Explanation

### 4.1 Static typing

**Static typing** means every variable, parameter, field, and most expressions has a type that is **known when you compile**. The compiler walks your source and rejects code such as assigning a `String` to an `int` or calling a method that does not exist on the declared type.

This does **not** mean types are frozen at runtime. Polymorphism still allows:

```java
Number n = Integer.valueOf(42);  // declared Number, runtime class Integer
```

The compiler types `n` as `Number`, so only `Number` methods compile (e.g. `intValue()`). The JVM still creates an `Integer` instance.

### 4.2 Strong typing

**Strong typing** (in the everyday Java sense) means the language does **not** silently treat unrelated types as interchangeable. You cannot use a `String` as an `int`. Narrowing conversions require an **explicit cast** when you intentionally downcast references (Ch.02).

Contrast with some dynamically typed languages: in Python, `x = 42` then `x = "hi"` is normal. In Java, the **same variable** keeps one declared type for its whole lifetime:

```java
int count = 3;
// count = "three";  // compile error — strong + static
```

`var` does **not** weaken this—it only infers the **same** static type locally (Module 02).

### 4.3 Compile-time type vs runtime class

| Concept | When decided | Used for |
|---------|----------------|----------|
| **Declared / compile-time type** | At compile time | Overload resolution, field access, interface calls |
| **Runtime class** | When object is created | `getClass()`, `instanceof`, virtual method dispatch |

For **instance methods**, the JVM selects the implementation from the **runtime class** of the receiver (dynamic dispatch). For **which methods compile**, the **declared type** of the reference matters.

The demo [`TypingDemo`](../../examples/core-java/typing-demo/):

- `Number n = Integer.valueOf(42)` — declared `Number`, runtime `Integer`.
- `CharSequence cs = "hello"` — interface type for a `String` instance.

### 4.4 What static typing does not guarantee alone

Static typing catches **shape** errors (wrong type, missing method). It does **not** prove:

- `null` safety (unless you use annotations and tooling).
- Business rules (e.g. non-negative balance)—you still encode invariants in methods (Module 09).
- All casts are safe—bad casts can still throw `ClassCastException` at runtime (Ch.02, Ch.02 of this module).

### 4.5 Type system and API design

Public APIs should use **meaningful declared types** (`List<Order>`, `Optional<Money>`) so callers get compile-time help. Hiding everything behind `Object` removes static checks unless you add back tests (`instanceof`, patterns—Ch.02).

## 5. Mechanism and Execution Flow

Static typing is a **compiler pipeline** plus a **runtime dispatch** story—the two layers must not be conflated.

### 5.1 `javac` type-check passes

When you build a compilation unit:

1. **Name resolution** binds identifiers to fields, methods, and types.
2. **Type checking** validates assignments, method calls, and conversions (JLS §5) against **declared types**.
3. **Definite assignment** and reachability analysis run on top of those types.
4. The compiler emits bytecode whose call sites reference **resolved signatures** from the declared-type view of the program.

Programs that violate static rules **do not compile**—there is no “try at runtime” for ordinary source-level mistakes.

### 5.2 Assignment and widening path

For `Number n = Integer.valueOf(42)` in [`TypingDemo`](../../examples/core-java/typing-demo/):

1. `Integer.valueOf` returns reference type `Integer`.
2. **Widening reference conversion** to `Number` is implicit (subtype direction).
3. Variable `n` is **statically** a `Number`; only members of `Number` are callable without a cast.
4. The heap object remains class `Integer`; `n.getClass()` reports `Integer`.

The reverse (`Number` → `Integer`) requires an explicit **narrowing cast** and may fail at runtime.

### 5.3 Virtual calls: declared type vs runtime class

For `n.intValue()` on `Number n` holding an `Integer`:

1. Compile-time: `intValue()` must exist on `Number` (or the call is rejected).
2. Runtime: the JVM loads the receiver’s **actual class** (`Integer`) and dispatches to the most specific `intValue` implementation.

Static typing chooses **which names compile**; dynamic dispatch chooses **which body runs**.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Static typing** | Types attached to program bindings are checked at compile time |
| **Strong typing** | No silent conversion between incompatible types; mismatches are errors or explicit casts |
| **Declared type** | Type written in source for a variable, parameter, or field |
| **Runtime class** | `Class<?>` of the object instance in the heap |
| **Type check** | Compiler pass ensuring operations respect declared types |
| **Liskov substitution** | Subtype values usable where supertype is expected (Module 07)—static checks assume this |

## 7. Syntax and Language Rules

```java
String label = "items";     // reference type
int count = 3;              // primitive type — not object references
Number n = 42;              // OK: int widens/boxes to Integer, assigned to Number
// String s = n;            // ERROR: Number is not a String

Object o = "Ada";
String t = (String) o;      // explicit cast — may fail at runtime if o isn't String
```

- **Primitives** are not subtypes of reference types; boxing bridges to wrappers.
- **Assignment compatibility**: reference assignable if compile-time type of RHS is subtype of LHS (widening reference conversions per JLS).
- **`final` references** still have static type; they cannot be reassigned but the **object’s fields** may still be mutable.

## 8. Step-by-Step Examples

### 8.1 Minimal — compile-time rejection

```java
void demo() {
    int x = 1;
    // x = "one";     // incompatible types: String cannot be converted to int
}
```

The error appears in the IDE and at `javac`—no `.class` file for the broken compilation unit.

### 8.2 Realistic — TypingDemo

Full source: [`TypingDemo.java`](../../examples/core-java/typing-demo/TypingDemo.java).

```bash
javac TypingDemo.java && java TypingDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
3 items
number=42
runtime=Integer
len=5
```

**Important lines:**

- `count + " " + label` — `int` is promoted for string concatenation; result type is `String`.
- `Number n = Integer.valueOf(42)` — static type `Number` limits visible API; `intValue()` is on `Number`.
- `n.getClass().getSimpleName()` — reflection reports **runtime** class `Integer`, not `Number`.
- `CharSequence cs = "hello"` — interface as declared type; `length()` resolves to `String`’s implementation at runtime.

### 8.3 Advanced — same API, multiple runtime classes

```java
void printNumeric(Number n) {
    System.out.println(n.getClass().getSimpleName() + ":" + n);
}

printNumeric(7);              // Integer (autobox)
printNumeric(3.14);           // Double
printNumeric(new BigInteger("99")); // BigInteger
```

Call sites must pass a **Number** or subtype; inside, runtime class varies. Overloading on `int` vs `Integer` vs `long` is resolved by **compile-time types** of arguments (Module 04).

## 9. Real-World Use Cases

- **REST clients** — Jackson/Gson map JSON into typed DTOs; wrong field types fail early in tests, not silently in prod.
- **JdbcTemplate / JPA** — row mappers return typed entities instead of `Object[][]`.
- **Payment rails** — `Money` / `Currency` types prevent adding cents to a string id.
- **Build systems** — `javac` failures on CI catch entire classes of integration mistakes before deployment.

## 10. Internal Behavior

See **§5** for the compile-vs-dispatch split. This section adds bytecode verifier and HotSpot notes.

**javac** performs **type checking** and **flow analysis** (definite assignment, reachability). It emits bytecode where method calls reference **constant pool** entries for resolved signatures based on **compile-time types**.

The JVM verifies bytecode (JVMS §4.10) for stack/type consistency but does not re-run full Java source type rules. **Illegal bytecode** from non-`javac` tools can still fail verification.

For virtual calls, HotSpot resolves the target method using the receiver’s **runtime** class (CHA and inline caches—deeper in later JVM modules).

## 11. Edge Cases and Limitations

- **`null`** is assignable to any reference type; dereferencing `null` is a runtime `NullPointerException`.
- **Raw types** (pre-generics style `List` without type argument) weaken static checking—avoid in new code; Module 13 covers generics.
- **Unchecked warnings** when mixing legacy raw APIs with generics—still compile, but casts may hide errors.
- **`Object` everywhere** — compiles, pushes failures to runtime casting.

## 12. Common Mistakes

### Incorrect — assuming runtime type from declared type

```java
Number n = Integer.valueOf(1);
// String s = (String) n;  // compiles with cast but throws ClassCastException
```

### Corrected — test or pattern first

```java
if (n instanceof Integer i) {
    System.out.println(i);
}
```

(Ch.02 expands `instanceof` and patterns.)

### Incorrect — thinking `var` is dynamic typing

```java
var x = 1;
// x = "two";  // ERROR: x is inferred as int
```

### Corrected

Use explicit types when multiple shapes are intentional:

```java
Object payload = fetch();
```

## 13. Best Practices

- Prefer **specific declared types** at boundaries (`Path`, `Duration`, domain types) over `Object`.
- When you must hold heterogenous values, **document** allowed runtime classes and centralize narrowing (Ch.02).
- Run **`javac -Xlint:unchecked`** when migrating generic code.
- Treat compiler errors as **specification feedback**, not noise to cast around.

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Strict static types | Safer refactors; better IDE support | More ceremony; learning curve |
| Everything `Object` | Flexible containers | Runtime failures; no exhaustiveness |
| Heavy use of wrappers | Uniform APIs | Boxing cost; null on primitives |
| `var` locally | Shorter code | Harder to read if abused on non-obvious types |

## 15. Performance Considerations

Static typing itself has negligible runtime cost—checks happen at compile time. **Autoboxing** when using `Number` or collections of primitives can add allocation pressure; primitives and specialized streams (Module 16) address hot paths.

## 16. Security Considerations

Static types do not sandbox code. They reduce accidental misuse; **validation** at trust boundaries (network input) remains mandatory. Casting untrusted `Object` without checks is a common source of logic bugs, not memory corruption in safe Java.

## 17. Java Version and Compatibility Notes

Java has been statically typed since 1.0. **`var`** (10+) and **pattern matching** (16+/21) add syntax sugar but retain static checking. Records and sealed types (Module 15) strengthen **exhaustive** switching—still compile-time checked.

## 18. Related Language Comparisons

- **Python / JavaScript** — dynamic types at runtime; optional static checkers (mypy, TypeScript) approximate Java’s compile phase.
- **Go** — static, strong; structural typing differs from Java’s nominal types.
- **C#** — similar static model; `dynamic` keyword is the exception path.

## 19. Revision and Mastery

- **Gaps:** conflating `var` with dynamic typing; ignoring declared type when reading polymorphic code.
- **You should answer:** Why does `Number n = 42` compile? Why might `getClass()` disagree with the variable’s declared type?
- **Advanced:** Capture conversion in lambdas; intersection types in generics (Module 13).

## 20. Exercises

### Beginner

1. Which line fails `javac`: `int a = 3;` then `double b = a;`?
2. Predict `TypingDemo` line 1 output without running.

### Intermediate

3. Write a method `void log(Number n)` printing runtime class and `doubleValue()`.
4. Explain why `CharSequence cs = "hello"; cs.length()` compiles though `cs` might conceptually be many implementations.

### Advanced

5. When is widening reference conversion from `Integer` to `Number` allowed but `Number` to `Integer` requires a cast?

## 21. Interview Questions

1. Static vs dynamic typing—in one sentence each?
2. What is the difference between compile-time type and runtime class?
3. Does Java’s type system prevent all `ClassCastException`s?
4. What does `var` change about Java’s type system?

## 22. Solutions

1. Neither fails—`int` to `double` is widening primitive conversion.
2. `3 items`
3. Sample uses `n.getClass()` and `n.doubleValue()`.
4. `length()` is declared on `CharSequence`; all implementors provide it; dispatch uses runtime class.
5. Assignment upward in hierarchy is implicit; downward is narrowing reference conversion requiring cast.

## 23. Chapter Summary

Java is **statically** typed: the compiler enforces types on every use. It is **strongly** typed for everyday development: incompatible types do not silently mix. **Declared types** guide compilation; **runtime classes** drive dispatch and reflection. **`TypingDemo`** contrasts `Number`/`Integer` and interface-typed `String`. Next: **type tests** and **pattern matching** for safe narrowing.

## 24. Key Terminology

Static typing · strong typing · declared type · runtime class · type check

## 25. References and Further Reading

- [JLS §4.2 — Primitive Types and §4.3 — Reference Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html)
- [JLS §5 — Conversions and Contexts](https://docs.oracle.com/javase/specs/jls/se21/html/jls-5.html)
- [JLS §15.19 — Type Comparison Operator `instanceof`](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.19)

## 26. Next Steps

- [02-instanceof-and-pattern-matching.md](02-instanceof-and-pattern-matching.md)
- Module 03 Ch.04 — control-flow patterns (complementary)
- Module 13 — generics deepen static guarantees for collections

## Review Checklist

**Mandatory Depth Review:** Technical profile; static/strong typing and compile vs runtime class at **Level 3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (declared vs runtime §4.3; pipeline §5)
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

- **CHA / inline-cache internals** — named as implementation detail in §10; JVM modules deepen later.

**Verification note:** `TypingDemo` (`3 items`, `number=42`, `runtime=Integer`, `len=5`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
