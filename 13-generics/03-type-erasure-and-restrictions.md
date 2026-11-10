# Type Erasure and Restrictions

> **Status:** Completed  
> **Module:** 13 — Generics  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Deep-dive
> **Depth target:** Levels 3–4 (erasure, bridges, reifiable restrictions)
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

- State **type erasure** rules: bounds become upper limits; unbounded `T` becomes **`Object`**.
- Explain why **`List<String>`** and **`List<Integer>`** share one **runtime class** (`ArrayList`).
- Describe **bridge methods** and why generic **overrides** still polymorph correctly.
- List major **restrictions**: no `new T()`, no generic array creation, limited **`instanceof`** on parameterized types.
- Recognize **heap pollution** warnings and use **`@SafeVarargs`** deliberately.

## 2. Prerequisites

- **Ch.01–02** — type parameters and wildcards.
- **Module 10 Ch.03** — erasure preview; bridges named there, detailed here.
- **Module 07** — overriding and method signatures.

## 3. Why This Concept Exists

The JVM and bytecode existed **before** generics (Java 5). **Reification**—keeping `List<String>` as a distinct runtime type—would have broken existing libraries and class files. **Erasure** implements generics in the **compiler** while reusing a mostly unchanged **runtime model**.

That choice buys **backward compatibility** but imposes **restrictions** you feel when reflecting, serializing, or asking `instanceof List<String>`. Understanding erasure explains Module 12 behavior at runtime and why **unchecked** warnings still appear at legacy borders.

## 4. Conceptual Explanation

### 4.1 Erasure mapping (conceptual)

For a type parameter **`T extends Number`**:

- Fields of type `T` become type **`Number`** (or first bound).
- Methods using `T` in signatures erase to bounds (with bridges where needed).
- Casts may be inserted at call sites when reading from raw or `Object`.

For **unbounded `T`**:

- Erases to **`Object`** with casts to `T` at boundaries.

**Wildcards** erase to their bounds (`? extends Number` → `Number` for many purposes in signatures).

### 4.2 One runtime class

```java
List<String> strings = new ArrayList<>();
List<Integer> ints = new ArrayList<>();
boolean same = strings.getClass() == ints.getClass();  // true
```

**`getClass()`** returns the raw **`ArrayList`**. No `List<String>.class` distinct from `List.class` in the JVM model (use **`Type`** APIs for reflective type tokens—Ch.04).

### 4.3 Bridge methods (override preservation)

Suppose:

```java
class Node<T> {
    T data;
    void setData(T data) { this.data = data; }
}

class StringNode extends Node<String> {
    @Override
    void setData(String data) { this.data = data; }
}
```

After erasure, parent shows `setData(Object)` while child has `setData(String)`. **`javac`** emits a **synthetic bridge** `setData(Object)` that casts and delegates so polymorphic dispatch stays correct ([JLS §8.4.4](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.4)).

### 4.4 Restrictions developers hit daily

| Restriction | Reason |
|-------------|--------|
| No `new T()` | No default constructor known at runtime for erased `T` |
| No `T[]` arrays | Arrays are reified; generic arrays break soundness |
| No `instanceof List<String>` (parameterized) | `String` argument erased |
| Cannot catch `T` | `T` is not a reifiable catch type |
| Cannot overload on erased signature only | Same bytecode signature |

### 4.5 Varargs and heap pollution

```java
@SafeVarargs
static <T> List<T> makeList(T... values) {
    List<T> list = new ArrayList<>();
    for (T v : values) {
        list.add(v);
    }
    return list;
}
```

Varargs array is **`Object[]`** under the hood—if someone sneakily mutates it with wrong element types, **heap pollution** can break `List<String>`. **`@SafeVarargs`** documents that the method does not expose the array unsafely (JLS; compiler suppresses related warnings).

## 5. Mechanism and Execution Flow

### 5.1 Erasure translation (`javac`)

For `List<String>`:

1. Replace type args with **erased bound** (`String` stays as `Object` at JVM level for many fields; signatures use bound rules in JLS §4.6).
2. Insert **casts** at reads from generic APIs.
3. Emit **bridge methods** when overrides would otherwise collide after erasure.

### 5.2 Runtime class identity (`GenericsErasureDemo`)

1. **`new ArrayList<String>()`** and **`new ArrayList<Integer>()`** share **`ArrayList.class`**.
2. **`sameRawClass=true`** — generic parameter is **not** a distinct runtime type tag.
3. Reflective **`Type`** (`ParameterizedType`) can recover compile-time shape—Ch.04 API design.

### 5.3 Bridge dispatch

On **`StringNode.setData(String)`** overriding **`Node<T>`**:

1. Bytecode exposes **`setData(Object)`** bridge calling the `String` overload.
2. Virtual dispatch on **`Node`** reference still reaches **`StringNode`** implementation—polymorphism preserved despite erasure.

### 5.4 Why restrictions exist

| Compile-time rule | Runtime without reification |
|-------------------|-------------------------------|
| No `new T()` | Cannot know constructor for erased `T` |
| No `T[]` | Arrays are reified; would bypass generic checks |
| No `instanceof List<String>` | No `String` tag at runtime |

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Erasure** | Removal of type arguments in bytecode and reflective class metadata |
| **Reifiable** | Runtime-representable type (e.g. `String`, `int[]`) |
| **Bridge method** | Synthetic method preserving override after signature erasure |
| **Unchecked cast warning** | Compiler cannot prove cast at generic boundary |
| **Heap pollution** | Runtime contamination of a generic container via arrays or raw types |
| **Raw type** | Generic declaration used without type arguments—interoperability escape hatch |

## 7. Syntax and Language Rules

```java
// Legal
if (list instanceof List<?>) { }  // wildcard OK
if (list instanceof ArrayList<?>) { }

// Illegal (compile error)
// if (list instanceof List<String>) { }

// Cannot
// T obj = new T();
// T[] arr = new T[10];

// Workaround for arrays — typed array from Class<T> (reflection)
@SuppressWarnings("unchecked")
static <T> T[] genericArray(Class<T> clz, int len) {
    return (T[]) java.lang.reflect.Array.newInstance(clz, len);
}
```

## 8. Step-by-Step Examples

### 8.1 Minimal — `GenericsErasureDemo`

Path: [examples/core-java/generics-erasure-demo/](../examples/core-java/generics-erasure-demo/)

**Verified output (Temurin 21.0.12.1):**
```text
sameRawClass=true
s=hi
i=7
built=[a, b]
```

- **`sameRawClass=true`** — runtime class identity independent of type arguments.
- **`identity`** erases to `Object`/`Serializable`-bounded shape with casts; overload resolution still uses generics at compile time.
- **`makeList`** uses varargs with **`@SafeVarargs`**; building **`List`** instead of **`T[]`** avoids illegal generic array creation.

### 8.2 Realistic — unchecked warning at legacy border

```java
List<String> names = new ArrayList<>();
@SuppressWarnings("unchecked")
List<String> fromLegacy = (List<String>) (List) legacyRawList();
```

Prefer **generics at the source**; suppress only at the seam with a comment.

### 8.3 Advanced — inspecting bridges

```bash
javap -private StringNode.class
```

Look for `bridge void setData(java.lang.Object)` delegating to `setData(String)`.

## 9. Real-World Use Cases

- **Serialization frameworks** — need reflective **Type** information (`java.lang.reflect.ParameterizedType`) because `Class` alone loses arguments.
- **JSON mappers** — carry `TypeReference<List<User>>` patterns (libraries) to recover erased types at parse time.
- **JUnit / matchers** — `instanceof` on concrete classes, not on type arguments.
- **Interop with pre-generics APIs** — raw `Map` from old JARs forces explicit checks at boundaries.

## 10. Internal Behavior

**javac** performs **type erasure** as specified in [JLS §4.6](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.6). **Verification** operates on erased signatures. **HotSpot** does not store type arguments on ordinary `Class` objects for parameterized types; optional reflective metadata may expose them via **`getGenericSuperclass()`** etc.

## 11. Edge Cases and Limitations

- **Primitive type arguments** erase to box types in signatures where needed (`T` bound might still force boxing).
- **`Class<T>`** is reifiable—often used as a **type token** when erasure blocks `T`.
- **Arrays of generic types** — `List<String>[]` is illegal; `new ArrayList<?>[10]` likewise problematic.
- **`Object[]` vs generic collections** — storing a `String[]` as `Object[]` still allowed; arrays remain covariant footguns (Module 11).

## 12. Common Mistakes

### Incorrect — `instanceof List<String>`

```java
// if (x instanceof List<String>) { }  // compile error
```

### Corrected

```java
if (x instanceof List<?> list) {
    // pattern — element types unknown at runtime; validate elements separately
}
```

### Incorrect — `new T()`

```java
static <T> T create() {
    // return new T();  // compile error
}
```

### Corrected

```java
static <T> T create(Supplier<T> factory) {
    return factory.get();
}
// or Supplier + bounded constructor (Class<T> with reflexive no-arg)
```

## 13. Best Practices

- Treat **runtime** type knowledge as **`Class<?>`** or library **TypeToken** patterns—not stringly casts.
- **Eliminate raw types** upstream so `@SuppressWarnings` stays rare and localized.
- Prefer **`List.of`**, **`copyOf`**, **`ArrayList`** constructors over varargs helpers unless **`@SafeVarargs`** is justified.
- When overriding generic methods, trust the compiler to emit bridges—do not “fix” with raw signatures.

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Erasure | JVM compatibility | No reified generics |
| Reified generics (hypothetical) | Better reflection | Historical bytecode break |
| Class token pattern | Runtime checks possible | Boilerplate at call sites |

## 15. Performance Considerations

Erasure removes compile-time checks but also avoids **specialization explosion** at bytecode level (contrast C++ templates). **Autoboxing** and collection algorithms dominate generic list performance, not erasure itself.

## 16. Security Considerations

**Heap pollution** and unchecked deserialization can inject wrong types into collections typed at compile time only. Validate at trust boundaries; use immutable snapshots **`List.copyOf`**.

## 17. Java Version and Compatibility Notes

Erasure unchanged since Java 5. **Pattern matching `instanceof`** (Java 16+) works with **`List<?>`**, not with parameterized **type arguments**. Preview features unrelated to erasure in Java 21—this chapter documents stable rules.

## 18. Related Language Comparisons

C# **reification** keeps `List<int>` at runtime. Java **erasure** trades runtime knowledge for migration path. **TypeScript** erases similarly at emit time but is structurally, not nominally, typed.

## 19. Revision and Mastery

- **Gaps:** believing generics are “only syntax sugar” with **zero** runtime effect—casts and bridges matter.
- **You should answer:** Why `sameRawClass=true` in `GenericsErasureDemo`?
- **Advanced:** read `javap` bridges on a generic subclass; explain why `EnumSet` uses trickery not `new E[]`.

## 20. Exercises

### Beginner

1. Predict `sameRawClass=` without running.
2. List three things you cannot do with a type parameter `T`.

### Intermediate

3. Write `boolean allStrings(List<?> maybe)` using pattern matching and `instanceof String` per element.
4. When does the compiler emit an **unchecked** warning on `Map` assignment?

### Advanced

5. Sketch how bridge methods relate to Module 10 assessment question on overriding `Node<T>`.

## 21. Interview Questions

1. What is type erasure?
2. Why are generic array creations illegal?
3. What problem do bridge methods solve?

## 22. Solutions

1. `true`.
2. Example: `new T()`, `instanceof T`, `T[]` creation.
3. Loop; return false if any element not a `String`.
4. Assigning raw `HashMap` to `HashMap<String,Integer>` or unchecked cast from `Map`.
5. Subclass override must remain callable through erased superclass reference—bridge delegates with cast.

## 23. Chapter Summary

**Type erasure** removes type arguments from the JVM’s view of generic declarations while **javac** preserves safety at compile time via checking, **casts**, and **bridge methods**. **`GenericsErasureDemo`** shows identical runtime classes, generic method inference before erasure, and safe **`List`** building instead of **`T[]`**. **Restrictions** follow directly from erasure—not arbitrary annoyances. **API design** (Ch.04) uses these facts at module boundaries.

## 24. Key Terminology

Type erasure · reifiable · bridge method · heap pollution · `@SafeVarargs` · raw type

## 25. References and Further Reading

- [JLS 4.6 — Type erasure](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.6)
- [JLS 8.4.4 — Bridge methods](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.4)
- [Java Tutorial — Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)
- [Module 10 Ch.03](../10-java-type-system/03-variance-wildcards-and-erasure-preview.md)

## 26. Next Steps

- [04-generic-api-design.md](04-generic-api-design.md) — repositories, `Optional`, and safe boundaries
- Module 14 — exceptions on generic APIs that fail at runtime borders

## Review Checklist

**Mandatory Depth Review:** Deep-dive profile; erasure translation and bridge synthesis at **Level 4**.

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

- **Reified generics proposals** — not Java 21 language.

**Verification note:** `GenericsErasureDemo` (`sameRawClass=true`; `s=hi`; `i=7`; `built=[a, b]`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
