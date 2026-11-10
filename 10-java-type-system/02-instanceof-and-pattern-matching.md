# instanceof and Pattern Matching

> **Status:** Completed  
> **Module:** 10 — Java Type System  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Level 3 (`instanceof`, patterns, switch type patterns, dominance)  
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

- Use **`instanceof`** as a **type test** that guards **narrowing** of reference types.
- Apply **pattern matching for `instanceof`** (`if (x instanceof String s)`) without redundant casts.
- Branch on **heterogeneous values** with **`switch` type patterns** and **`case null`**.
- Combine patterns with **boolean guards** (`&&`, `when`) safely.
- Explain when the compiler rejects overlapping or unreachable patterns.
- Connect type tests to **exhaustiveness** with **sealed** hierarchies (Module 07 / 15).

## 2. Prerequisites

- **Module 10 Ch.01** — declared type vs runtime class.
- **Module 03 Ch.04** — switch patterns and sealed `Shape` examples (control-flow angle).
- **Module 02 Ch.04** — explicit casts and `ClassCastException`.
- **Module 07 Ch.04** — sealed types limit permitted subtypes.

Brief refresher: when a reference is typed as `Object` or a **supertype**, the compiler only knows the **supertype API**. To call **subtype-specific** methods, you must **narrow**—either with an explicit cast (risky) or with a **type test** that proves the runtime class.

## 3. Why This Concept Exists

Frameworks routinely pass **unknown payloads**: event bus messages, JSON trees before mapping, plugin handles, or legacy `Object` return types. You need a disciplined way to ask: “Is this value a `String`? A number? `null`?” and only then bind a **typed name**.

Classic Java required:

```java
if (obj instanceof String) {
    String s = (String) obj;
    ...
}
```

Two steps, two names, easy to desynchronize. **Pattern matching** folds the test and the binding into one form the compiler understands—fewer casts, clearer control flow, better exhaustiveness on sealed types.

## 4. Conceptual Explanation

### 4.1 `instanceof` as a type test

**`instanceof`** evaluates whether a reference’s **runtime class** is assignment-compatible with the type on the right (JLS §15.19). It is `false` for `null`—no exception.

```java
if (value instanceof String) {
    // compiler still types value as Object here unless pattern form is used
}
```

### 4.2 Pattern matching for `instanceof` (Java 16+)

```java
if (value instanceof String s) {
    System.out.println(s.toUpperCase()); // s in scope
}
```

**`s` is only in scope** where the compiler can prove the test succeeded (including extended `if` blocks with `&&` guards in many cases—see JLS flow scoping rules).

Guards:

```java
if (box instanceof String s && s.length() > 3) {
    System.out.println("long=" + s.toLowerCase());
}
```

If the `instanceof` fails, the right side of `&&` is not evaluated—same short-circuit rules as other `&&` chains.

### 4.3 Type patterns in `switch` (Java 21)

**Switch** can take **type patterns** as cases:

```java
static String describe(Object value) {
    return switch (value) {
        case null -> "null";
        case String s -> "string:" + s;
        case Integer i -> "int:" + i;
        case Double d -> "double:" + d;
        default -> "other:" + value;
    };
}
```

Each `case` tries a **pattern**: null match, or runtime class match with binding. **`default`** catches everything not listed—required for `Object` and other **open** type universes.

### 4.4 Dominance and ordering

When patterns **overlap**, the **first match wins**. More specific cases must appear before general ones:

```java
// case Object o before case String s would swallow strings — dominance / reachability errors
```

The compiler reports **dominance** problems when a case can never run. With **sealed** supertype and cases covering every permit, **`default` may be omitted**—exhaustiveness is provable (Module 07).

### 4.5 Type tests vs `getClass() ==`

**`instanceof`** respects inheritance: `new Integer(1) instanceof Number` is **true**. **`getClass() == Integer.class`** is **false** for a `Long` instance even if you meant “numeric.” Prefer **`instanceof`** for domain branching unless you intentionally need **exact** class equality.

### 4.6 Relation to casting

An **unchecked cast** `(String) obj` tells the compiler “trust me.” **`instanceof` + pattern** proves the shape first. Casting without a test is acceptable only when **invariants** already guarantee the type (e.g. immediately after you constructed the value).

## 5. Mechanism and Execution Flow

Type tests bridge **compile-time declared types** and **runtime shapes**; patterns add **flow-sensitive scoping** so the compiler tracks narrowed types.

### 5.1 Runtime `instanceof` evaluation

For `value instanceof String` (JLS §15.19):

1. If the reference is **`null`**, the result is **`false`** (no exception).
2. Otherwise the JVM tests whether the **runtime class** is assignable to `String` (subtyping), not merely exact class equality.
3. On `true`, classic form leaves the variable’s **declared type** unchanged unless a pattern introduces a binding.

At bytecode level this is an **`instanceof`** instruction followed optionally by cast logic; patterns fold cast + test in source.

### 5.2 Pattern binding and definite assignment

For `if (value instanceof String s) { ... s ... }`:

1. Compiler evaluates the `instanceof` test.
2. On the **true** branch, **`s` is in scope** with type `String` (flow analysis, JLS §14.30).
3. On the **false** branch, `s` does not exist—using it is a compile error.
4. With `&&` guards, short-circuit applies: failed `instanceof` skips the guard expression.

This is how patterns replace redundant manual casts.

### 5.3 Switch type patterns ([`InstanceofPatternsDemo`](../../examples/core-java/instanceof-patterns-demo/))

For `switch (value)` with `case String s`:

1. **`case null`** is tested when listed (explicit null handling).
2. Remaining cases are tried in **source order**; the **first matching pattern wins** (dominance rules prevent unreachable overlaps).
3. Each successful match **binds** the pattern variable in that arrow body.
4. `default` catches open universes (`Object` inputs) where exhaustiveness cannot be proved.

Sealed hierarchies (Module 07) let the compiler prove **exhaustiveness** without `default`.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Type test** | Runtime check that a reference is compatible with a type (`instanceof`) |
| **Narrowing reference conversion** | Treating a supertype reference as a subtype after a safe proof |
| **Type pattern** | `case String s` or `instanceof String s` — test + bind |
| **Exhaustiveness** | Compiler proof that all allowed values are handled |
| **Dominance** | Ordering rule: first matching `case` wins; unreachable cases are errors |

## 7. Syntax and Language Rules

```java
// Classic (still valid)
if (x instanceof List<?> list) { ... }

// Switch type patterns + null (Java 21)
String label = switch (o) {
    case null -> "missing";
    case String s -> s;
    default -> o.toString();
};

// Guards in switch (when)
static String grade(Shape shape) {
    return switch (shape) {
        case Circle c when c.radius() > 5 -> "big";
        case Circle c -> "small";
        default -> "?";
    };
}
```

- **`null`**: `instanceof AnyType` is **false**; handle `null` explicitly in switches that accept nullable selectors.
- Patterns do **not** match primitive `int` in `Object` switch unless boxed—`case Integer i` matches `Integer`, not unboxed `int` passed where `Object` expected (autoboxing applies at call site).

## 8. Step-by-Step Examples

### 8.1 Minimal — pattern replaces cast

```java
void printLen(Object o) {
    if (o instanceof String s) {
        System.out.println(s.length());
    }
}
```

### 8.2 Realistic — InstanceofPatternsDemo

Full source: [`InstanceofPatternsDemo.java`](../../examples/core-java/instanceof-patterns-demo/InstanceofPatternsDemo.java).

**Verified output (Temurin 21.0.12.1):**

```text
string:Ada
int:21
null
double:3.5
long=java
```

**Important lines:**

- `describe("Ada")` → `case String s` binds and prefixes `string:`.
- `describe(21)` → `Integer` matches `case Integer i` (autoboxed when passed as `Object`).
- `describe(null)` → explicit `case null`; **`instanceof` would not enter** a `String` case.
- `describe(3.5)` → `double` literal is `Double` when boxed as `Object`.
- `box instanceof String s && s.length() > 3` → `"JAVA".length() == 4` → prints `long=java`.

### 8.3 Advanced — plugin-style dispatch

```java
sealed interface Command permits Save, Quit {}
record Save(String path) implements Command {}
record Quit() implements Command {}

String handle(Command cmd) {
    return switch (cmd) {
        case Save s -> "write:" + s.path();
        case Quit q -> "bye";
    };
}
```

No `default`—**sealed** + full coverage. Contrasts with open `Object` switches needing `default`.

## 9. Real-World Use Cases

- **Event handlers** — `switch (event)` on sealed `sealed interface Event` with `OrderPlaced`, `PaymentFailed`.
- **AST or expression evaluators** — tree walkers with one case per node type.
- **JSON / `JsonNode`** — type-specific extraction after `instanceof` / pattern.
- **Logging formatters** — branch on `Throwable` subtypes with different detail.

## 10. Internal Behavior

See **§5** for evaluation order, pattern scope, and switch matching. This section notes lowering and optimization.

**`instanceof`** is a JVM bytecode instruction testing assignability to a **reference type**. Pattern forms do not change runtime semantics versus test + manual cast; they affect **compile-time scope** and **exhaustiveness analysis**.

Switch pattern matching lowers to a chain of tests (with possible tableswitch optimization for sealed enums/records in later compiler versions—implementation detail).

## 11. Edge Cases and Limitations

- **Generic types and erasure**: `instanceof List<String>` is illegal (or historically limited); at runtime `List` is raw after erasure—Module 13 / Ch.03.
- **`null` pattern** only in switches that allow nullable selectors—not the same as `Optional`.
- **Primitive selectors** in switch use different pattern kinds (guarded constants)—not `Object` type patterns.
- **Overlapping cases** with poorly ordered `when` clauses — dominance errors.

## 12. Common Mistakes

### Incorrect — cast without test

```java
String s = (String) payload; // ClassCastException if payload is Integer
```

### Corrected

```java
if (payload instanceof String s) {
    use(s);
}
```

### Incorrect — forgetting null in switch

```java
return switch (maybe) { // NPE if maybe is null and no case null
    case String s -> s;
    default -> "";
};
```

### Corrected

```java
case null -> "";
```

### Incorrect — using `getClass()` for extensibility

```java
if (n.getClass() == Integer.class) { } // fails for other Number strategies
```

### Corrected

```java
if (n instanceof Integer i) { }
```

## 13. Best Practices

- Prefer **sealed** supertypes + **exhaustive switch** for closed domains (payments states, IR nodes).
- For open `Object` payloads, keep a **`default`** that logs or rejects unknown types.
- **Centralize** narrowing in one function (`describe`, `handle`, `toDto`) instead of scattering casts.
- Align with Module 03 patterns but keep **type-system** reasoning here: what is provable at compile time?

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| `instanceof` chains | Simple | Verbose; easy to miss a type |
| Switch patterns | Readable multi-way type dispatch | Needs Java 17+/21 features |
| Visitor pattern | Open extension without modifying switch | More classes/boilerplate |
| Catch-all `default` | Safe on open types | Loses exhaustiveness proof |

## 15. Performance Considerations

`instanceof` is cheap on HotSpot (often folded after profiling). Long chains of tests may become **profiled branches**; sealed switches may compile to efficient dispatch. Micro-optimize only with evidence—clarity first.

## 16. Security Considerations

Type tests do not validate **content**—a `String` might still be malicious input. Use patterns for **shape**, then **validate** length, charset, and allowlists at trust boundaries.

## 17. Java Version and Compatibility Notes

- **Pattern `instanceof`**: final in **16+**.
- **Switch patterns, `case null`, `when`**: **21** (this handbook’s baseline).
- Preview history (14–17) — do not use old `--enable-preview` flags on 21; features here are **final**.

## 18. Related Language Comparisons

Kotlin **`when`** with smart casts resembles switch patterns. C# **`is` patterns** and **`switch` expressions** are analogous. Go uses type switches on interfaces with similar goals but different syntax.

## 19. Revision and Mastery

- **Gaps:** treating `default` as optional on `Object`; confusing `==` with type tests.
- **You should answer:** Why `describe(null)` prints `null` but `null instanceof String` is false? When can you drop `default`?
- **Advanced:** Refutability and flow-sensitive typing in JLS; generic `instanceof` future notes (JEP 394+ awareness only).

## 20. Exercises

### Beginner

1. Predict four `describe(...)` lines from the demo without running.
2. What is printed if `box` were `Integer.valueOf(100)` in the guarded `instanceof` block?

### Intermediate

3. Add `case Long l` to `describe`; predict `describe(21L)`.
4. Rewrite a two-branch `if/else instanceof` chain as a switch expression.

### Advanced

5. Why does `default -> "other:" + value` run for `Boolean.TRUE` in the demo?

## 21. Interview Questions

1. Difference between pattern `instanceof` and manual cast?
2. Can `switch` on `Object` be exhaustive without `default`?
3. What happens for `null` with classic `switch` vs `case null`?
4. `instanceof` vs `getClass() ==` — when prefer each?

## 22. Solutions

1. As verified output block in §7.2.
2. Guard fails (`Integer` not `String`); no `long=` line (or skip block entirely).
3. `long:21` if you use `case Long l -> "long:" + l;` ordering before `Integer` if both could apply—only `Long` matches `21L`.
4. Sample: `return switch (x) { case String s -> ...; case Integer i -> ...; default -> ... };`
5. `Boolean` is not `String`, `Integer`, or `Double`; `default` appends `other:true`.

## 23. Chapter Summary

**`instanceof`** proves runtime type compatibility before **narrowing**. **Patterns** bind typed variables in `if` and **`switch`**, eliminating redundant casts. **`case null`** handles missing references explicitly. **`InstanceofPatternsDemo`** shows open-universe dispatch on `Object` plus a guarded **`instanceof`**. **Sealed** types enable exhaustiveness without `default`. Next: **variance**, **wildcards**, and an **erasure preview**.

## 24. Key Terminology

Type test · type pattern · narrowing · exhaustiveness · dominance

## 25. References and Further Reading

- [JLS §15.19 — `instanceof`](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.19)
- [JLS §14.30 — Patterns](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.30)
- [JEP 394: Pattern Matching for instanceof](https://openjdk.org/jeps/394)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)

## 26. Next Steps

- [03-variance-wildcards-and-erasure-preview.md](03-variance-wildcards-and-erasure-preview.md)
- Module 15 — records/sealed in domain models
- Module 03 Ch.04 — additional control-flow pattern examples

## Review Checklist

**Mandatory Depth Review:** Technical profile; `instanceof`, patterns, and switch type patterns at **Level 3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (dominance §4.4; switch flow §5.3)
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
- [x] Complements Module 03 without duplicating it verbatim
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **Full sealed exhaustiveness proof walkthrough** — cross-linked to Module 07; switch rules taught here with `default` on `Object`.

**Verification note:** `InstanceofPatternsDemo` (`string:Ada`, `int:21`, `null`, `double:3.5`, `long=java`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
