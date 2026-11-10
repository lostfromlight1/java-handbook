# Bounds and Wildcards

> **Status:** Completed  
> **Module:** 13 — Generics  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Deep-dive
> **Depth target:** Levels 3–4 (bounds, wildcards, PECS, capture)
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

- Write **bounded type parameters** `<T extends Number>` and **intersection bounds** `<T extends A & B>`.
- Apply **wildcards** `?`, `? extends T`, and `? super T` at **use sites**.
- Use **PECS** (Producer **Extends**, Consumer **Super**) to design flexible method signatures.
- Implement **`copy`**, **`sum`**, and similar utilities over Module 12 collections safely.
- Explain **wildcard capture** and why `dest.add(t)` needs a helper with a bounded type parameter.

## 2. Prerequisites

- **[Ch.01 — Generic Types and Methods](01-generic-types-and-methods.md)** — type parameters and invariance.
- **Module 10 Ch.03** — PECS preview; this chapter is the full treatment.
- **Module 12** — `List`, `ArrayList`, enhanced `for`.

Brief refresher: **invariance** blocks `List<Integer>` where `List<Number>` is required. **Wildcards** express what you will **read** (producer → `extends`) or **write** (consumer → `super`) without breaking concrete lists.

## 3. Why This Concept Exists

Utilities such as “sum all numbers in any list” or “copy elements between lists” appear in every codebase. If signatures demand **`List<Number>`**, callers with **`List<Integer>`** cannot pass arguments—yet summing only **reads** numbers, and copying from ints into **`List<Number>`** only **writes** into the destination at the right level.

**Bounded type parameters** constrain what types may be substituted (`T extends Comparable<T>`). **Wildcards** loosen **parameter** types while keeping **call-site** types exact (`List<Integer>` stays `List<Integer>`).

## 4. Conceptual Explanation

### 4.1 Upper bounds on type parameters

```java
static <T extends Number> double doubleValue(T n) {
    return n.doubleValue();
}
```

`T` is not unlimited—it must be **`Number`** or a subclass. **`Integer`**, **`Double`**, etc. qualify.

**Intersection bound** (multiple upper bounds, first may be class, rest interfaces):

```java
static <T extends Comparable<T> & Serializable> void storeSorted(T item) { }
```

### 4.2 Wildcards — unknown type at use site

| Syntax | Role | Typical use |
|--------|------|-------------|
| `?` | Any unknown type | Read-only pass-through, `Class<?>` |
| `? extends T` | Upper bounded wildcard | **Producer** — read as `T` |
| `? super T` | Lower bounded wildcard | **Consumer** — write `T` |

`List<?>` is **not** the same as `List<Object>`. You cannot `add("x")` to either arbitrarily, but assignment rules differ: `List<?>` accepts any `List<T>`.

### 4.3 PECS in depth

**Joshua Bloch’s rule:** If the parameter only **produces** items you read, use **`? extends T`**. If it only **consumes** items you write, use **`? super T`**.

```java
// Producer — read Number out
static double sum(List<? extends Number> values) {
    double total = 0;
    for (Number n : values) {
        total += n.doubleValue();
    }
    return total;
}

// src produces T; dest consumes T
static <T> void copy(List<? extends T> src, List<? super T> dest) {
    for (T item : src) {
        dest.add(item);
    }
}
```

- **`sum(List<Integer>)`** works: you only **read** `Integer` as `Number`.
- **`copy(ints, nums)`** with `List<Integer>` and `List<Number>` works: source **extends** `Integer`, dest **super** `Integer`.

### 4.4 Why you cannot add to `List<? extends Number>`

The compiler does not know whether the list is `List<Integer>`, `List<Double>`, or `List<Short>`. Adding **any** concrete `Number` might violate the actual type argument. **`null`** may be allowed; non-null adds are rejected (except in some capture helpers).

### 4.5 Wildcard capture

Inside `copy`, the type of elements read from `src` is a **captured** unknown subtype of `T`. The enhanced `for` binds `T item` because the **method** declares `<T>` and ties `src`’s extends bound to `T`. Without the method type parameter, `dest.add(item)` would not type-check—this is **wildcard capture** ([JLS capture conversion](https://docs.oracle.com/javase/specs/jls/se21/html/jls-5.html#jls-5.1.10)).

## 5. Mechanism and Execution Flow

Wildcards are resolved in **`javac`** with **capture conversion**—runtime still sees erased bounds.

### 5.1 Upper bound read (`? extends Number`)

For `double sum(List<? extends Number> nums)`:

1. Compiler treats elements as **read-only `Number`** (except `null`).
2. **`nums.add(1)`** is rejected—could break caller's `List<Integer>`.
3. Loop in **`GenericsBoundsDemo`** reads and widens to **`double`** safely.

### 5.2 Lower bound write (`? super Integer`)

For `void copy(List<? super Integer> dest, List<Integer> src)`:

1. **`dest.add`** accepts **`Integer`** (and supertypes' slots).
2. Reading from `dest` without bound yields **`Object`**—capture limits known element type.
3. PECS: **Producer `extends`, Consumer `super`.**

### 5.3 Capture conversion (Level 4)

When a wildcard appears in a method body, the compiler may introduce a **fresh type variable** so multiple reads share a consistent upper bound—explains why two `? extends T` parameters are not always interchangeable without helper generics.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Upper bound** | `T extends X` — `T` must be substitutable for `X` |
| **Lower bound** | `? super X` — wildcard accepts types **supertier** to `X` |
| **Use-site variance** | Flexibility introduced at method parameter with wildcards |
| **Declaration-site invariance** | `List<Integer>` only stores `Integer` |
| **PECS** | Producer **extends**, Consumer **super** |
| **Wildcard capture** | Compiler introducing fresh type variables to type-check wildcard bodies |

## 7. Syntax and Language Rules

```java
// Bounded type parameter
public class NumericBox<T extends Number> { }

// Multiple bounds (type parameter)
<T extends Number & Comparable<Number>>

// Wildcards at use site only — not on class declaration (except rare cases)
void fill(List<? super Integer> sink) { }

// NO: class Bad<?> { }  — illegal
// NO: new ArrayList<?>() with intent to add varying types — use raw or typed list
```

**`get`** from `List<? extends T>` yields **`T`**. **`add`** to `List<? super T>` accepts **`T`** (and supertypes of `T` where applicable).

## 8. Step-by-Step Examples

### 8.1 Minimal — `GenericsBoundsDemo`

Path: [examples/core-java/generics-bounds-demo/](../examples/core-java/generics-bounds-demo/)

```java
List<Integer> ints = new ArrayList<>(List.of(1, 2, 3));
System.out.println("sum=" + sum(ints));

List<Number> nums = new ArrayList<>();
copy(ints, nums);
System.out.println("nums=" + nums);

List<? extends Number> producer = ints;
System.out.println("producer0=" + producer.get(0));
```

**Verified output (Temurin 21.0.12.1):**
```text
sum=6.0
nums=[1, 2, 3]
producer0=1
```

- **`sum`** treats any numeric list as a **producer** of `Number`.
- **`copy`** is the textbook **PECS** pair: `? extends T` / `? super T`.
- **`producer`** assignment shows **covariant read** position without widening `ints` to `List<Number>`.

### 8.2 Realistic — `Collections.addAll` shape

```java
public static <T> boolean addAll(Collection<? super T> c, T... elements) {
    boolean modified = false;
    for (T element : elements) {
        if (c.add(element)) {
            modified = true;
        }
    }
    return modified;
}
```

The collection **consumes** `T`; varargs **produce** concrete `T` instances.

### 8.3 Advanced — `Comparator` contravariance

```java
Comparator<Object> cmp = (a, b) -> a.toString().compareTo(b.toString());
List<String> names = new ArrayList<>();
names.sort(cmp);  // Comparator<? super String> accepts Comparator<Object>
```

**Comparator** is **contravariant** on its type parameter: you may pass a comparator that compares **wider** types.

## 9. Real-World Use Cases

- **Bulk APIs** — `addAll`, `containsAll`, `retainAll` on `Collection<? extends E>`.
- **Stream pipelines** — `Stream<? extends Number>` mapped to `double` statistics.
- **Copy-on-write** — copying DTO lists between layers with different declared element supertypes.
- **Guava / JDK** — `Function<? super T, ? extends R>` in `map` operations.

## 10. Internal Behavior

Wildcards exist only in **type-checking**; erasure replaces them with bounds (Ch.03). The compiler inserts **capture conversions** so bodies type-check as if they used fresh type variables. **Spec** (JLS) defines assignability; **javac** implements capture in attribution.

## 11. Edge Cases and Limitations

- **`List<?>`** cannot receive `add` of arbitrary objects (except `null`).
- **Neither extends nor super** on both parameters for the same `T` in naive APIs—pick read vs write role per parameter.
- **Arrays** still covariant—do not confuse `Number[] x = new Integer[3]` with generic lists.
- **Recursive bounds** — `static <T extends Comparable<T>> T max(T a, T b)` requires a total order compatible with `T`.

## 12. Common Mistakes

### Incorrect — `List<Number>` for “any number list”

```java
void process(List<Number> all) { }
// process(listOfIntegers);  // error
```

### Corrected — producer wildcard

```java
void process(List<? extends Number> all) { }
```

### Incorrect — PECS reversed

```java
void badDest(List<? extends String> dest) {
    // dest.add("x");  // compile error — dest is not a consumer
}
```

### Corrected

```java
void goodDest(List<? super String> dest) {
    dest.add("x");
}
```

## 13. Best Practices

- Apply **PECS** when designing **public** methods over collections.
- Use **bounded type parameters** when a single coherent type `T` must flow through multiple parameters.
- Prefer **`List.copyOf`** / **`List.of`** for immutable snapshots after transformations.
- In APIs that both read and write the same `T`, declare **`<T>`** with unbounded or bounded `T`, not wildcards on both sides unless roles differ (src/dst).

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| `List<T>` parameter | Callers can add `T` | Less flexible for all number lists |
| `List<? extends T>` | Accept subtypes | No non-null `add` |
| `List<? super T>` | Flexible sinks | `get` returns `Object` or bound |
| Bounded `<T extends X>` | Call methods on `X` inside body | Callers must match bound |

## 15. Performance Considerations

Wildcards do not add runtime dispatch. Enhanced `for` over lists still uses **Iterator**; choosing structure (`ArrayList` vs `LinkedList`) dominates (Module 12 Ch.05).

## 16. Security Considerations

Accepting **`List<?>`** from untrusted code and reflecting on elements still runs your handlers on **Object** references—validate before privileged operations. PECS does not sanitize content.

## 17. Java Version and Compatibility Notes

Wildcards and bounds since **Java 5**; rules stable in **Java 21**.

## 18. Related Language Comparisons

Kotlin **declaration-site variance** (`out T`, `in T`) contrasts with Java’s **use-site** wildcards. Scala has **variance annotations** on type parameters at declaration. Java chose wildcards for JDK binary compatibility.

## 19. Revision and Mastery

- **Gaps:** memorizing **extends/super** without producer/consumer story; reversing PECS.
- **You should answer:** Why does `copy` compile for `List<Integer>` → `List<Number>`?
- **Advanced:** `Enum<E extends Enum<E>>`; typing `Class<? extends Enum<?>>`.

## 20. Exercises

### Beginner

1. Predict `sum=` and `nums=` in `GenericsBoundsDemo`.
2. Write `max(List<? extends Comparable<?>> scores)` returning greatest element.

### Intermediate

3. Explain why `List<? extends Object>` does not help you add strings.
4. Implement `partition(List<T> source, Predicate<T> test)` without wildcards—when would wildcards appear in callers?

### Advanced

5. Sketch capture conversion for `copy` as a fresh type variable `CAP#1`.

## 21. Interview Questions

1. State **PECS** and give a collections example.
2. Difference between `<T extends Number>` and `List<? extends Number>`?
3. Why is `Comparator<? super T>` the right bound for `List.sort`?

## 22. Solutions

1. `6.0`; `[1, 2, 3]`.
2. Loop, track best via `Comparable.compareTo`.
3. **Extends** is read-only for adds—only `null` or capture tricks help.
4. Single `T` throughout source list; callers pass `List<User>` etc.
5. Compiler treats `src` as `List<? extends CAP#1>` where `CAP#1 extends T`, enabling `dest.add` with type `T`.

## 23. Chapter Summary

**Bounds** constrain type parameters; **wildcards** loosen parameter types using **upper** and **lower** bounds at the use site. **PECS** maps **read** paths to **`? extends T`** and **write** paths to **`? super T`**, unlocking Module 12 **`List<Integer>`** and **`List<Number>`** interop in utilities like **`GenericsBoundsDemo`**. **Wildcard capture** ties the loop variable to the destination’s super bound. Next: **erasure** and language restrictions.

## 24. Key Terminology

Upper bound · lower bound · wildcard · PECS · producer · consumer · capture conversion

## 25. References and Further Reading

- [JLS 4.5 — Parameterized types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5)
- Effective Java, 3rd ed. — Item 31 (PECS)
- [Module 10 Ch.03 — preview](../10-java-type-system/03-variance-wildcards-and-erasure-preview.md)

## 26. Next Steps

- [03-type-erasure-and-restrictions.md](03-type-erasure-and-restrictions.md) — runtime reality and compiler restrictions
- [04-generic-api-design.md](04-generic-api-design.md) — applying PECS in public APIs

## Review Checklist

**Mandatory Depth Review:** Deep-dive profile; wildcard capture and PECS at **Level 4** where APIs mix producers and consumers.

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

- **Declaration-site variance (Kotlin `out`/`in`)** — comparison only in §17.

**Verification note:** `GenericsBoundsDemo` (`sum=6.0`; `nums=[1, 2, 3]`; `producer0=1`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
