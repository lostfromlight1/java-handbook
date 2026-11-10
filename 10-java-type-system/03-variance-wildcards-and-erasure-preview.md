# Variance, Wildcards, and Erasure Preview

> **Status:** Completed  
> **Module:** 10 — Java Type System  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Level 3 (invariance, wildcards, PECS, erasure preview)  
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

- Explain **invariance** of generic types such as `List<Integer>` vs `List<Number>`.
- Contrast **covariance** and **contravariance** at a conceptual level (with arrays as a cautionary preview).
- Read and write **wildcards** `? extends T` and `? super T`.
- Apply **PECS** (Producer `extends`, Consumer `super`) to method signatures.
- Describe **type erasure** at preview depth: what survives in bytecode vs what is lost.
- Know which problems move to **Module 13** (full generics, bridges, bounded type parameters).

## 2. Prerequisites

- **Module 10 Ch.01** — static types and subtyping on reference types.
- **Module 05 Ch.03** — inheritance between classes.
- **Module 00 Ch.04** — arrays are covariant (and can fail at runtime).
- Collections used informally so far; formal `List<E>` rules start here and continue in Module 18.

Brief refresher: **subtyping** on classes follows `extends`—`Integer` is a `Number`. Generic types like `List<Integer>` are **not** subtypes of `List<Number>` in Java—that is **invariance**.

## 3. Why This Concept Exists

Collections and APIs need **one abstraction** (`List`, `Comparator`, callbacks) that works for many type arguments without copying the whole library per type. **Generics** add type parameters: `List<Integer>`.

If `List<Integer>` were assignable to `List<Number>`, you could **read** it as numbers but **break** the list by inserting a `Double`. Java forbids that assignment at compile time. **Wildcards** recover flexibility where you only **read** or only **write**, encoded as `? extends` / `? super`.

At runtime, the JVM predates reified generics; **erasure** removes most type arguments from bytecode—explaining `instanceof` limits and `ClassCastException` at generic borders.

## 4. Conceptual Explanation

### 4.1 Generic type parameters

```java
List<Integer> ints = new ArrayList<>();
ints.add(1);
// List<Number> nums = ints; // COMPILE ERROR — invariance
```

`List<Integer>` and `List<Number>` are **different** static types even though `Integer` extends `Number`.

### 4.2 Variance vocabulary

| Term | Meaning (type argument position) |
|------|----------------------------------|
| **Covariant** | `? extends Number` — you mainly **read** `Number` |
| **Contravariant** | `? super Integer` — you mainly **write** `Integer` |
| **Invariant** | `List<Integer>` only accepts `Integer` elements |

Java generics are **invariant** at the declaration site. Wildcards add **use-site** variance.

### 4.3 Arrays vs generics (preview)

```java
Number[] nums = new Integer[3]; // covariant arrays — legal
// nums[0] = 3.5;              // ArrayStoreException at runtime
```

Arrays carry **runtime** element type; generics do **not** (erasure). Hence generics are safer by default; arrays retain this footgun—Module 11 revisits arrays.

### 4.4 Upper bounded wildcard — `? extends T`

```java
static int sum(List<? extends Number> values) {
    int total = 0;
    for (Number n : values) {
        total += n.intValue();
    }
    return total;
}
```

You may **read** elements as `Number` (or `T`). You generally **cannot add** `Integer` to `List<? extends Number>`—the compiler cannot prove which concrete list you might break (except `null` in some APIs).

**Producer** — the collection **produces** `Number` values for you to read.

### 4.5 Lower bounded wildcard — `? super T`

```java
static void addNumber(List<? super Integer> sink, Integer value) {
    sink.add(value);
}
```

You may **write** `Integer` into the sink. Reading yields `Object` unless you know more.

**Consumer** — the collection **consumes** values you add.

### 4.6 PECS

**PECS** = **P**roducer **`extends`**, **C**onsumer **`super`**.

| Role | Wildcard | Demo method |
|------|----------|-------------|
| Producer (read) | `? extends Number` | `sum` |
| Consumer (write) | `? super Integer` | `addNumber` |

Mnemonic: ask whether the parameter **gives** you items or **takes** items from you.

### 4.7 Type erasure (preview)

At compile time, the compiler checks generics rigorously. In bytecode, **most** type parameters are **erased** to their **bounds** (`Object` if unbounded). So at runtime:

```java
List<Integer> ints = new ArrayList<>();
System.out.println(ints.getClass().getSimpleName()); // ArrayList — no Integer in class name
```

You **cannot** reliably do `if (list instanceof List<String>)` (illegal / unhelpful). Casts like `(List<String>) list` may insert **unchecked** warnings—the check is partial at runtime.

**Module 13** covers erasure rules, **bridge methods**, heap pollution, and `TypeToken`-style patterns. Here: erasure explains **why** PECS and compile-time checks exist.

### 4.8 Raw types (legacy)

Pre-generics `List list = new ArrayList()` **weakens** checking. Interop with old APIs sometimes forces raw types—minimize and localize; prefer wildcards over raw.

## 5. Mechanism and Execution Flow

Wildcards encode **use-site variance**; erasure explains why **compile-time** generic discipline does not survive unchanged at **runtime**.

### 5.1 Rejecting invariant assignment (`javac`)

For `List<Integer> ints` and `List<Number> nums`:

1. The compiler treats `List<Integer>` and `List<Number>` as **unrelated** parameterized types (invariance).
2. Assignment `nums = ints` fails at **compile time**, preventing a path where code could `add(3.5)` into a list of integers.
3. Subtyping on `Integer` extends `Number` does **not** lift to `List<>`—this is the core invariant design choice.

### 5.2 `? extends` producer path ([`VarianceErasurePreviewDemo`](../../examples/core-java/variance-erasure-preview-demo/))

For `sum(List<? extends Number> values)`:

1. Compiler models elements as **upper-bounded** by `Number` for **reads**.
2. Enhanced `for` binds each element as `Number` and calls `intValue()`.
3. **Writes** of concrete numbers are rejected (except `null` in some signatures)—the compiler cannot prove which concrete `List` implementation backs the wildcard.

**Producer:** the parameter **gives** you `Number` values to consume.

### 5.3 `? super` consumer path

For `addNumber(List<? super Integer> sink, Integer value)`:

1. `sink.add(value)` is legal because `Integer` is a **lower bound**—any element type **known to accept `Integer`** works.
2. Reads without further knowledge yield **Object** (or the bound)—you read when the API is a **sink**, not a source.

**PECS** chooses `extends` vs `super` by whether you **read** or **write** through the parameter.

### 5.4 Erasure at class load

After successful compilation:

1. Type arguments are **erased** to their bounds (often `Object`) in bytecode signatures.
2. `ints.getClass().getSimpleName()` is `ArrayList`, not `ArrayList<Integer>`—no per-instance generic tag at runtime on HotSpot.
3. `javac` inserts **casts** at heap boundaries where erased code must recover static types; mistakes surface as `ClassCastException`.

**Module 13** covers bridge methods and full erasure rules; here the mechanism explains **why** PECS and compile-time checks exist.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Type parameter** | `E` in `List<E>` |
| **Invariance** | `G<A>` not a subtype of `G<B>` even when `A` extends `B` |
| **Wildcard** | `?`, `? extends T`, `? super T` |
| **Upper bound** | `extends` limit on type argument |
| **Lower bound** | `super` limit on type argument |
| **PECS** | Producer extends, Consumer super |
| **Erasure** | Removal of type parameter detail from runtime representation |
| **Reifiable type** | Type describable at runtime (e.g. raw `List`, not `List<String>`) |

## 7. Syntax and Language Rules

```java
void copy(List<? extends Number> src, List<? super Number> dst) {
    for (Number n : src) {
        dst.add(n); // OK if dst is Number or Object sink — Number is assignable to ? super Number consumer
    }
}

// Multiple bounds (preview)
static <T extends Number & Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}
```

- **`? extends Number`** — read as `Number`; `add` restricted.
- **`? super Integer`** — accept `Integer`; read as `Object` typically.
- **Cannot** instantiate `new List<?>()` or `new T()` under erasure (Module 13).

## 8. Step-by-Step Examples

### 8.1 Minimal — invariance error

```java
List<Integer> ints = List.of(1, 2);
// List<Number> nums = ints; // does not compile
```

### 8.2 Realistic — VarianceErasurePreviewDemo

Full source: [`VarianceErasurePreviewDemo.java`](../../examples/core-java/variance-erasure-preview-demo/VarianceErasurePreviewDemo.java).

**Verified output (Temurin 21.0.12.1):**

```text
sum=3
first=1
objs=[7]
listClass=ArrayList
```

**Important lines:**

- `sum(ints)` — `List<Integer>` matches `List<? extends Number>` (producer/read).
- `nums.get(0)` — `nums` declared `List<? extends Number>`; read returns `Number` / unboxed `1`.
- `addNumber(objs, 7)` — `List<Object>` is a consumer for `Integer` via `? super Integer`.
- `getClass().getSimpleName()` → `ArrayList` — **no** `Integer` type argument at runtime (erasure).

### 8.3 Advanced — API sketch

```java
public static <T> void sort(List<T> list, Comparator<? super T> c) {
    list.sort(c);
}
```

`Comparator<? super T>` is **contravariant** — can compare supertypes of `T`. This is the same PECS logic in JDK utility signatures you will see everywhere.

## 9. Real-World Use Cases

- **`Collections.copy(dest, src)`** — `dest` consumer, `src` producer wildcards.
- **Guava / Apache** — `Function<? super F, ? extends T>` in transforms.
- **Stream API** — `map`, `filter`, `collect` signatures use wildcards and bounds (Module 16).
- **Jackson `TypeReference`** — fights erasure for JSON generic collections.

## 10. Internal Behavior

See **§5** for wildcard read/write paths and erasure summary. This section notes bridges (Module 13).

**javac** rewrites generic code to **erased** signatures in `.class` files, inserting casts at bytecode boundaries. **Bridge methods** preserve polymorphism when erasure changes overriding signatures (details Module 13).

HotSpot does not store `List<Integer>` vs `List<String>` per instance—only the raw class `ArrayList`.

## 11. Edge Cases and Limitations

- **`null`** elements are allowed in any generic list unless forbidden by policy.
- **Primitive** type parameters are illegal—use `int` via `List<Integer>` and boxing.
- **`List<?>`** — read as `Object?`; almost no `add` except `null`—rarely the best bound; prefer extends/super.
- **Heap pollution** when mixing raw and generic—unchecked warnings at call site.

## 12. Common Mistakes

### Incorrect — treating `List<? extends Number>` as `List<Number>`

```java
void bad(List<? extends Number> a, List<? extends Number> b) {
    // a.addAll(b); // or add(3.14) — compile errors
}
```

### Corrected — read-only merge into consumer

```java
void good(List<? super Number> sink, List<? extends Number> src) {
    for (Number n : src) {
        sink.add(n);
    }
}
```

### Incorrect — PECS reversed

```java
void readSink(List<? super Integer> src) {
    // Integer x = src.get(0); // not safe without cast
}
```

### Corrected

Use `? extends Integer` (or `Integer` directly) to read; `? super Integer` to write.

### Incorrect — expecting runtime generic check

```java
// if (list instanceof List<String>) { } // illegal
```

### Corrected

Validate elements at boundaries; use typed APIs; Module 13 for patterns (`Class`, `Type`, libraries).

## 13. Best Practices

- Name methods with PECS: **inputs that you read from** → `extends`; **outputs you write to** → `super`.
- Avoid **raw** types in new code; suppress warnings only with a comment explaining legacy interop.
- Prefer **specific** type parameters on public classes; wildcards on methods that only need flexibility.
- Document **null** and **immutability** of collections returned from APIs (Module 09).

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Invariant `List<Integer>` | Safe writes | No direct assign to `List<Number>` |
| Wildcards on methods | Flexible APIs | Harder signatures for beginners |
| Erasure | JVM compatibility | Weaker runtime reflection |
| Reified generics (other langs) | Runtime type args | Different JVM model—not Java |

## 15. Performance Considerations

Generics erase to casts the JVM optimizes away after warmup. Wildcards do not add per-call overhead versus concrete types. **Autoboxing** in `List<Integer>` still costs—use specialized primitive collections in hot numeric code when needed.

## 16. Security Considerations

Unchecked casts from user-controlled data into generic collections can cause **ClassCastException** or logic bugs—validate before cast. Do not trust deserialized raw collections.

## 17. Java Version and Compatibility Notes

Generics since **Java 5**; diamond `<>` since 7; **`var`** with generics since 10. No change to erasure model in 21. **Valhalla** / future proposals may evolve the platform—treat as awareness, not baseline.

## 18. Related Language Comparisons

- **C#** — reified generics with some covariance on interfaces (`IEnumerable<out T>`).
- **Kotlin** — declaration-site variance (`in` / `out`).
- **TypeScript** — structural variance in type checker only (erased at runtime like Java generics conceptually).

## 19. Revision and Mastery

- **Gaps:** memorizing PECS without producer/consumer story; assuming generics work like arrays at runtime.
- **You should answer:** Why `sum(List<Integer>)` compiles? Why can't you add to `? extends`? What does erasure imply for `getClass()`?
- **Advanced:** Wildcard capture; recursive type bounds; `Enum<E extends Enum<E>>` (Module 13).

## 20. Exercises

### Beginner

1. Predict four lines of `VarianceErasurePreviewDemo` output.
2. Is `List<Object>` a valid argument for `addNumber`? Why?

### Intermediate

3. Write `firstElement(List<? extends Number> nums)` returning `Number`.
4. Explain PECS for `Collections.copy`.

### Advanced

5. Why does Java reject `List<Integer> nums = new ArrayList<Number>()`?

## 21. Interview Questions

1. What is PECS?
2. Difference between `List<? extends Number>` and `List<Number>`?
3. What is type erasure in one paragraph?
4. Covariant arrays vs invariant `List` — what can go wrong?

## 22. Solutions

1. See verified block in §7.2.
2. Yes — `Object` is a supertype of `Integer`; consumer `? super Integer` accepts the sink.
3. `return nums.isEmpty() ? null : nums.get(0);`
4. `copy(dest, src)` — `dest` is `? super T` (consumer), `src` is `? extends T` (producer).
5. `ArrayList<Number>` could hold `Double`; assigning to `List<Integer>` would allow retrieving non-`Integer` elements—violates type safety.

## 23. Chapter Summary

Generic types are **invariant** at declaration. **Wildcards** express **use-site variance**: **`? extends`** for **producers** (read), **`? super`** for **consumers** (write). **PECS** names that rule. **Erasure** removes type parameters from runtime class objects—**Module 13** deepens bridges, bounds, and safe patterns. **`VarianceErasurePreviewDemo`** ties sum/first/add together and shows **`ArrayList`** without `Integer` at runtime. This completes Module 10’s type-system foundation before **arrays/strings** (Module 11) and full **generics** (Module 13).

## 24. Key Terminology

Invariance · wildcard · PECS · erasure · producer · consumer

## 25. References and Further Reading

- [JLS §4.5 — Parameterized Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5)
- [JLS §5.1.2 — Generic Invariance](https://docs.oracle.com/javase/specs/jls/se21/html/jls-5.html)
- [Java Tutorials — Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html)
- Effective Java — Items on generics and PECS

## 26. Next Steps

- [Module 11 — Arrays and Strings](../11-arrays-and-strings/README.md)
- [Module 13 — Generics](../13-generics/) (erasure, bounds, bridges)
- [Module 18 — Collections](../18-collections/) — applying wildcards in APIs

## Review Checklist

**Mandatory Depth Review:** Technical profile; invariance, wildcards, PECS, erasure preview at **Level 3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (PECS table §4.6; erasure path §5.4)
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
- [x] Module 13 identified for deepening, not deferred without explanation
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **Bridge method bytecode listings** — deferred to Module 13 with erasure preview motivation in §5.4.

**Verification note:** `VarianceErasurePreviewDemo` (`sum=3`, `first=1`, `objs=[7]`, `listClass=ArrayList`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
