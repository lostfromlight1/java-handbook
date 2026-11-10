# Variance, Wildcards, and Erasure Preview

> **Status:** Completed  
> **Module:** 10 — Java Type System  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

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

## 5. Terminology

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

## 6. Syntax and Language Rules

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

## 7. Step-by-Step Examples

### 7.1 Minimal — invariance error

```java
List<Integer> ints = List.of(1, 2);
// List<Number> nums = ints; // does not compile
```

### 7.2 Realistic — VarianceErasurePreviewDemo

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

### 7.3 Advanced — API sketch

```java
public static <T> void sort(List<T> list, Comparator<? super T> c) {
    list.sort(c);
}
```

`Comparator<? super T>` is **contravariant** — can compare supertypes of `T`. This is the same PECS logic in JDK utility signatures you will see everywhere.

## 8. Real-World Use Cases

- **`Collections.copy(dest, src)`** — `dest` consumer, `src` producer wildcards.
- **Guava / Apache** — `Function<? super F, ? extends T>` in transforms.
- **Stream API** — `map`, `filter`, `collect` signatures use wildcards and bounds (Module 16).
- **Jackson `TypeReference`** — fights erasure for JSON generic collections.

## 9. Internal Behavior

**javac** rewrites generic code to **erased** signatures in `.class` files, inserting casts at bytecode boundaries. **Bridge methods** preserve polymorphism when erasure changes overriding signatures (details Module 13).

HotSpot does not store `List<Integer>` vs `List<String>` per instance—only the raw class `ArrayList`.

## 10. Edge Cases and Limitations

- **`null`** elements are allowed in any generic list unless forbidden by policy.
- **Primitive** type parameters are illegal—use `int` via `List<Integer>` and boxing.
- **`List<?>`** — read as `Object?`; almost no `add` except `null`—rarely the best bound; prefer extends/super.
- **Heap pollution** when mixing raw and generic—unchecked warnings at call site.

## 11. Common Mistakes

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

## 12. Best Practices

- Name methods with PECS: **inputs that you read from** → `extends`; **outputs you write to** → `super`.
- Avoid **raw** types in new code; suppress warnings only with a comment explaining legacy interop.
- Prefer **specific** type parameters on public classes; wildcards on methods that only need flexibility.
- Document **null** and **immutability** of collections returned from APIs (Module 09).

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Invariant `List<Integer>` | Safe writes | No direct assign to `List<Number>` |
| Wildcards on methods | Flexible APIs | Harder signatures for beginners |
| Erasure | JVM compatibility | Weaker runtime reflection |
| Reified generics (other langs) | Runtime type args | Different JVM model—not Java |

## 14. Performance Considerations

Generics erase to casts the JVM optimizes away after warmup. Wildcards do not add per-call overhead versus concrete types. **Autoboxing** in `List<Integer>` still costs—use specialized primitive collections in hot numeric code when needed.

## 15. Security Considerations

Unchecked casts from user-controlled data into generic collections can cause **ClassCastException** or logic bugs—validate before cast. Do not trust deserialized raw collections.

## 16. Java Version and Compatibility Notes

Generics since **Java 5**; diamond `<>` since 7; **`var`** with generics since 10. No change to erasure model in 21. **Valhalla** / future proposals may evolve the platform—treat as awareness, not baseline.

## 17. Related Language Comparisons

- **C#** — reified generics with some covariance on interfaces (`IEnumerable<out T>`).
- **Kotlin** — declaration-site variance (`in` / `out`).
- **TypeScript** — structural variance in type checker only (erased at runtime like Java generics conceptually).

## 18. Revision and Mastery

- **Gaps:** memorizing PECS without producer/consumer story; assuming generics work like arrays at runtime.
- **You should answer:** Why `sum(List<Integer>)` compiles? Why can't you add to `? extends`? What does erasure imply for `getClass()`?
- **Advanced:** Wildcard capture; recursive type bounds; `Enum<E extends Enum<E>>` (Module 13).

## 19. Exercises

### Beginner

1. Predict four lines of `VarianceErasurePreviewDemo` output.
2. Is `List<Object>` a valid argument for `addNumber`? Why?

### Intermediate

3. Write `firstElement(List<? extends Number> nums)` returning `Number`.
4. Explain PECS for `Collections.copy`.

### Advanced

5. Why does Java reject `List<Integer> nums = new ArrayList<Number>()`?

## 20. Interview Questions

1. What is PECS?
2. Difference between `List<? extends Number>` and `List<Number>`?
3. What is type erasure in one paragraph?
4. Covariant arrays vs invariant `List` — what can go wrong?

## 21. Solutions

1. See verified block in §7.2.
2. Yes — `Object` is a supertype of `Integer`; consumer `? super Integer` accepts the sink.
3. `return nums.isEmpty() ? null : nums.get(0);`
4. `copy(dest, src)` — `dest` is `? super T` (consumer), `src` is `? extends T` (producer).
5. `ArrayList<Number>` could hold `Double`; assigning to `List<Integer>` would allow retrieving non-`Integer` elements—violates type safety.

## 22. Chapter Summary

Generic types are **invariant** at declaration. **Wildcards** express **use-site variance**: **`? extends`** for **producers** (read), **`? super`** for **consumers** (write). **PECS** names that rule. **Erasure** removes type parameters from runtime class objects—**Module 13** deepens bridges, bounds, and safe patterns. **`VarianceErasurePreviewDemo`** ties sum/first/add together and shows **`ArrayList`** without `Integer` at runtime. This completes Module 10’s type-system foundation before **arrays/strings** (Module 11) and full **generics** (Module 13).

## 23. Key Terminology

Invariance · wildcard · PECS · erasure · producer · consumer

## 24. References and Further Reading

- [JLS §4.5 — Parameterized Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5)
- [JLS §5.1.2 — Generic Invariance](https://docs.oracle.com/javase/specs/jls/se21/html/jls-5.html)
- [Java Tutorials — Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html)
- Effective Java — Items on generics and PECS

## 25. Next Steps

- [Module 11 — Arrays and Strings](../11-arrays-and-strings/README.md)
- [Module 13 — Generics](../13-generics/) (erasure, bounds, bridges)
- [Module 18 — Collections](../18-collections/) — applying wildcards in APIs

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; PECS + erasure preview
- [x] VarianceErasurePreviewDemo verified on Temurin 21.0.12.1
- [x] Module 13 identified for deepening, not deferred without explanation
- [x] Exercises, Revision and Mastery, references
