# equals, hashCode, and toString

> **Status:** Completed  
> **Module:** 06 — Classes and Object Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (equals/hashCode contracts, `HashSet` behavior)  
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

- Contrast **reference equality** (`==`) with **value equality** (`equals`).
- State the **`equals` contract** (reflexive, symmetric, transitive, consistent, null-handling).
- State the **`hashCode` contract** and why it must align with `equals`.
- Implement `equals`, `hashCode`, and `toString` for a **value type** using **`Objects.equals`** / **`Objects.hash`** (or `Integer.hashCode` etc.).
- Predict **`HashSet`** / **`HashMap`** behavior when duplicates are **equal** but not **identical** references.
- Use **`@Override`** and **`instanceof` pattern matching** (Java 16+) safely in `equals`.

## 2. Prerequisites

- **Module 05 Ch.01** — references, `==`, `new`, distinct objects with same-looking data.
- **Module 02 Ch.02** — `==` on references vs primitives; introductory mention of `equals` on `String`.
- **Module 06 Ch.03** — immutable value objects (`Sku`-style types).
- **Collections preview:** `Set` stores **unique** elements; `HashSet` uses `hashCode` for bucketing (Module 18 full treatment).

Brief refresher: two variables **`==`** compare **references** (same object or both `null`). **`equals`** is an **instance method** (usually overridden) asking whether two objects are **logically the same value** for the type’s purpose.

## 3. Why This Concept Exists

Business logic cares whether two `Sku("ABC", 1)` instances represent the **same product version**, not whether they are the same heap allocation. **`==`** answers identity; **`equals`** answers value.

`java.util` hash-based collections (`HashSet`, `HashMap`, `HashTable`, `ConcurrentHashMap` buckets) use **`hashCode`** to pick a bucket and **`equals`** to distinguish keys in that bucket. If you override `equals` but not `hashCode` (or break the contract), sets duplicate entries, maps retrieve wrong values, and tests fail in subtle ways.

**`toString`** supports logging, debugging, and assertions—without it you often see `ClassName@1a2b3c4d` hex identity, which is useless in production logs.

## 4. Conceptual Explanation

### 4.1 Reference identity vs value equality

```java
Sku a = new Sku("ABC", 1);
Sku b = new Sku("ABC", 1);
boolean sameRef = (a == b);      // false — two objects
boolean sameVal = a.equals(b);   // true — when equals overridden correctly
```

`String` overrides `equals` to compare character content, so literal-like strings behave as values.

### 4.2 Default `Object.equals` and `hashCode`

Every class inherits from **`Object`**. Default **`equals`** is **`==`**; default **`hashCode`** typically derives from the object’s identity (HotSpot implementation—not something to rely on across JVMs for your types).

When you override **`equals`** to use fields, you **must** override **`hashCode`** so equal objects share the same hash code (required contract).

### 4.3 The equals contract (JLS / `Object` API)

For non-null references `x`, `y`, `z` of your type when used with `equals`:

| Property | Meaning |
|----------|---------|
| Reflexive | `x.equals(x)` is `true` |
| Symmetric | `x.equals(y)` ⇔ `y.equals(x)` |
| Transitive | If `x.equals(y)` and `y.equals(z)` then `x.equals(z)` |
| Consistent | Multiple calls return same result if state unchanged |
| `null` | `x.equals(null)` is `false` |

**Subclass symmetry:** if you use `instanceof` instead of `getClass()` (as in this handbook’s style for value types), maintain Liskov semantics—do not make equality asymmetric across subtypes unless you deliberately design for it (rare).

### 4.4 The hashCode contract

From **`Object.hashCode`** specification:

- If two objects are **equal** according to `equals`, they **must** have the **same** `hashCode`.
- **Unequal** objects **may** share a hash code (collision)—collections must then use `equals` to disambiguate.
- **`hashCode`** need not stay stable across JVM runs unless you rely on persistent hash stores (serialization scenarios—special care).

### 4.5 Implementing equals (idiomatic Java 21)

```java
@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (!(o instanceof Sku other)) {
        return false;
    }
    return version == other.version && code.equals(other.code);
}
```

Steps:

1. **Reference shortcut** — same object.
2. **Type check** — `instanceof` pattern binds `other`.
3. **Field comparison** — primitives with `==`, references with `Objects.equals` when nullable.

For **non-comparable value types**, prefer **`getClass() == o.getClass()`** if you reject equality with subclasses—choosing `instanceof` vs `getClass()` is a design decision (Effective Java discusses trade-offs).

### 4.6 Implementing hashCode

Combine fields that appear in `equals`:

```java
@Override
public int hashCode() {
    return Objects.hash(code, version);
}
```

`Objects.hash` uses `Arrays.hashCode` on boxed arguments—fine for moderate fields; for hot paths, hand-tuned mixing (e.g. `31 * result + fieldHash`) is optional optimization.

### 4.7 toString

Human-readable, stable enough for logs; avoid secrets. Demo:

```java
@Override
public String toString() {
    return "Sku[" + code + "@" + version + "]";
}
```

Not a substitute for structured logging, but invaluable in tests and dev.

## 5. Mechanism and Execution Flow

### 5.1 Calling `equals` at runtime

1. Evaluate the receiver and argument references.
2. If either side is `null`, reference equality short-circuits (`==` false; `equals` on non-null returns false for null argument per contract).
3. **Fast path:** if `this == o`, return `true`.
4. **Type check:** `instanceof` (or `getClass`) rejects unrelated types.
5. Compare participating fields with `==` for primitives and `Objects.equals` for references.

Overridden `equals` is **virtual dispatch**—the runtime type of the receiver decides which implementation runs (dynamic dispatch preview from Module 05).

### 5.2 `HashSet.add` and hash-based lookup (OpenJDK model)

When `set.add(x)` runs on a `HashSet`:

1. Compute `hash = x.hashCode()` (mask/sign handling is implementation detail).
2. Map hash to a **bucket** (table index).
3. Walk the bucket’s linked chain (or tree for long chains) comparing **`equals`** on candidates with matching hash.
4. If an **equal** element exists, `add` returns `false` and size is unchanged—demo: `a` and `b` equal → `setSize=2`.
5. If no equal element, insert and grow size.

**Contract bridge:** step 3 assumes equal objects share hash codes—otherwise equal `b` might land in a different bucket and appear as a duplicate entry (broken `hashCode` while `equals` overridden).

Module 18 deepens table resizing, load factor, and `LinkedHashSet`/`TreeSet` alternatives—this chapter's goal is the **call sequence** collections rely on.

### 5.3 `toString` in diagnostics

Default `Object.toString` uses class name + `@` + identity hash—fine for identity types, poor for values. Overridden `toString` does not affect `equals`/`hashCode` unless you mistakenly include unstable fields.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Identity equality** | `==` on references |
| **Value equality** | Logical sameness via overridden `equals` |
| **Equals contract** | Reflexive, symmetric, transitive, consistent, null false |
| **Hash code** | `int` summary of object; used by hash tables |
| **Collision** | Distinct unequal objects sharing same `hashCode` |
| **Value type** | Type defined by field values (`Sku`, `Money`) |

## 7. Syntax and Language Rules

- Override with **`@Override`** so compiler catches signature typos.
- **`equals` parameter type is `Object`**, not your class—required for polymorphism.
- Do not make `equals` **overloaded** with `equals(Sku other)`—that **hides**; `set.contains` still uses `Object.equals`.
- **`record`** generates `equals`/`hashCode`/`toString` from components automatically.

## 8. Step-by-Step Examples

### 8.1 Minimal: broken identity-only equality

```java
class BadSku {
    private final String code;
    // no equals override — only == works
}
```

Two `BadSku` with same code are **not** equal under `equals`.

### 8.2 Module demo and HashSet (**Verified**)

**Source:** [`examples/core-java/equals-hashcode-demo/EqualsHashCodeDemo.java`](../examples/core-java/equals-hashcode-demo/EqualsHashCodeDemo.java)

```bash
cd examples/core-java/equals-hashcode-demo
javac EqualsHashCodeDemo.java
java EqualsHashCodeDemo
```

**Verified (Temurin JDK 21.0.12.1):**

```text
a.equals(b)=true
a.equals(c)=false
a.hashCode()==b.hashCode()=true
a=Sku[ABC@1]
setSize=2
```

Interpretation:

- **`a`** and **`b`** are distinct objects but **equal values** → `equals` true.
- **`c`** differs in `code` → `equals` false.
- Matching **hash codes** for equal objects → contract satisfied (`true` after `a.equals(b)`).
- **`toString`** → readable `Sku[ABC@1]`.
- **`HashSet`**: `add(a)`, `add(b)`, `add(c)` — **`b` does not increase size** because `b` equals `a`; **`c` is distinct** → **`setSize=2`**.

Critical snippet:

```java
set.add(a);
set.add(b); // equal to a — should not grow
set.add(c);
System.out.println("setSize=" + set.size());
```

If `hashCode` were **not** overridden while `equals` was, `HashSet` might store **both** `a` and `b` → size 3—classic bug.

### 8.3 Advanced: null-safe fields

```java
return Objects.equals(this.code, other.code) && this.version == other.version;
```

Use when either side could be null by design (not in `Sku` demo where `code` is `requireNonNull`).

## 9. Real-World Use Cases

| Area | Need |
|------|------|
| **Entity identity** | Database ID in `equals`/`hashCode` for ORM-managed entities (only when detached—JPA has rules) |
| **Cache keys** | Immutable keys with correct hash |
| **Set aggregation** | Deduplicate events by business key |
| **Map lookups** | `Map<Sku, Integer>` stock counts |
| **Logging** | `toString` for support tickets |

API JSON equality is **not** automatic—serialize then compare, or compare domain objects after parsing.

## 10. Internal Behavior

**`HashSet.add`** (OpenJDK 21): compute `hashCode`, find bucket, walk bucket list comparing **`equals`** on candidates. Contract ensures equal elements replace or reject duplicate per `Set` spec.

**Performance:** poor `hashCode` distribution → long bucket chains → O(n) degradations; **unequal** objects should try to spread hashes (not a language guarantee, engineering goal).

Label: **Java Collections Framework specification** + **OpenJDK implementation** details.

## 11. Edge Cases and Limitations

- **Mutable fields in equals/hashCode:** if `Sku.code` could change after insertion into a `HashSet`, bucket lookup breaks—**do not mutate fields used in equals** (immutability helps).
- **Subclass equality:** symmetry breaks if parent and child compare differently.
- **Floating point:** use `Double.compare` in `equals`, `Double.hashCode` in `hashCode`—raw `==` problematic for NaN.
- **Inheritance + `getClass`:** subclasses not equal to superclass instances.
- **Enums:** `equals` is identity; do not override.

## 12. Common Mistakes

### Incorrect: override `equals` only

```java
@Override public boolean equals(Object o) { ... }
// hashCode still identity-based → HashSet breaks
```

### Corrected: override both with same fields

### Incorrect: overloaded `equals`

```java
public boolean equals(Sku other) { ... } // not an override!
```

### Corrected: `public boolean equals(Object o)` with `@Override`.

### Incorrect: compare classes with `==` on strings from user input without `equals`

Use `Objects.equals` or `String.equals`.

## 13. Best Practices

- Override **`equals` and `hashCode` together** whenever you override either for value semantics.
- Use **`Objects.requireNonNull`** on required fields in constructor.
- Prefer **immutable** keys in hash maps/sets.
- Generate with IDE or use **`record`** when appropriate—hand-write for learning and custom rules.
- **Do not** use significant fields that change over object lifetime.

## 14. Design Trade-offs

| Strategy | Pros | Cons |
|----------|------|------|
| `instanceof` | Subclass-friendly | Can violate symmetry if poorly designed |
| `getClass()` | Strict type match | Subclass instances never equal superclass |
| `record` | Less boilerplate | All components participate |
| Business-key only in `equals` | Matches DB uniqueness | Ignore non-key fields intentionally—document |

## 15. Performance Considerations

`equals` and `hashCode` run on **every** map get and set add. Keep them cheap—avoid N+1 I/O. Cache `hashCode` in a `final` field only if fields are immutable and you measure benefit.

## 16. Security Considerations

**`equals` must not** use secrets alone without constant-time comparison where timing attacks matter (cryptographic keys use `MessageDigest.isEqual` on bytes, not `String.equals` timing).

`toString` must **not** leak passwords, tokens, or PII in production logs—override with redaction.

## 17. Java Version and Compatibility Notes

- **`instanceof` patterns** — Java 16+ (`if (!(o instanceof Sku other))`).
- **`Objects.hash` / `Objects.equals`** — Java 7+.
- **Records** — generated equality from state components (Java 16+).

## 18. Related Language Comparisons

Python `__eq__` / `__hash__` (mutables often unhashable); Kotlin `data class`; C# `Equals`/`GetHashCode`. Java’s explicit contracts mirror `Object` documentation.

## 19. Revision and Mastery

- **Interview classic:** why `equals` and `hashCode` together?
- **Trick:** two equal objects in `HashSet` — how many entries?
- **Gap:** thinking `String` uses `==` for content.
- **Advanced:** canonicalization and interning (String pool) for memory and equality performance.

## 20. Exercises

### Beginner

1. Without running, predict `setSize` if `hashCode` is not overridden.
2. What does `a.equals(null)` return for proper `Sku`?

### Intermediate

3. Add `equals`/`hashCode` to `Money` from Ch.03 (cents + currency).
4. Write `toString` for `HttpRequest` avoiding logging Authorization headers.

### Advanced

5. Explain why changing only `equals` to include a new field without updating `hashCode` breaks persisted hash sets (conceptually).

## 21. Interview Questions

1. `==` vs `equals`?
2. `equals` contract in one sentence each property?
3. Why must equal objects share hash code?
4. What happens adding equal objects to `HashSet`?
5. When use `record` instead of hand-written equality?

## 22. Solutions

1. Likely **3** — `HashSet` uses `Object.hashCode` identity for `b` vs value-based `equals` mismatch path (broken contract).
2. **`false`**.
3. Standard `Objects.hash(cents, currency)` + field compares; check currency mismatch in `equals`.
4. Omit or mask sensitive header fields.
5. Stored buckets keyed by old hash no longer find object—rehash needed; illustrates immutability of equality fields.

## 23. Chapter Summary

Use **`==`** for **identity**. Override **`equals`** for **value** sameness honoring the contract. Override **`hashCode`** with the **same fields**. **`toString`** aids humans. **`HashSet`** proves the contract: equal **`b`** does not duplicate **`a`**. Immutable value types and consistent equality underpin reliable collections and domain logic.

## 24. Key Terminology

Reference equality · value equality · equals contract · hashCode contract · collision · `HashSet` · `@Override` · `Objects.hash`

## 25. References and Further Reading

- [Java SE — `Object.equals`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object))
- [Java SE — `Object.hashCode`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html#hashCode())
- [Java SE — `Objects`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html)
- [Java SE — `HashSet`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/HashSet.html)
- Effective Java (Bloch) — Items 10–11 (equals/hashCode)

## 26. Next Steps

- Module 07 — inheritance and `equals` across subtypes.
- Module 18 — collections interfaces and hash table internals.
- Module 12 — `enum` identity semantics.

## Review Checklist

**Mandatory Depth Review:** Technical profile; equality and hashing at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (`HashSet.add` steps §5.2)
- [x] First-appearance concepts explained now; M18 table internals named, not deferred-only
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 for EqualsHashCodeDemo)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- None beyond template-conditional sections already absent in this chapter.

**Verification note:** EqualsHashCodeDemo lines through `setSize=2` match Temurin JDK 21.0.12.1 (2026-11-11).
