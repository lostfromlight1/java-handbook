# Sealed Types and Patterns

> **Status:** Completed  
> **Module:** 15 — Enums, Records, and Sealed Types  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- **Deepen** sealed-type design from [Module 07 Ch.04](../07-inheritance-and-polymorphism/04-sealed-classes.md) with **algebraic data types** and production patterns.
- Combine **`sealed`**, **`record`**, and **`switch` type patterns** for exhaustive, refactor-safe dispatch.
- Apply **record patterns** and **nested patterns** in `switch` and `instanceof` (Java 21).
- Connect to [Module 03 Ch.04](../03-control-flow/04-branching-and-pattern-matching.md) pattern control flow.
- Choose **enum**, **sealed + records**, or **open inheritance** for a closed domain.
- Use **`when` guards** where patterns need extra conditions (M03 refresher).

## 2. Prerequisites

- **Module 07 Ch.04** — `sealed`, `permits`, `final`/`non-sealed` permitted subtypes, exhaustive switch intro.
- **Module 03 Ch.04** — type patterns, `case null`, dominance rules.
- **Module 15 Ch.01–02** — enums vs variant data; records as leaves.
- **Module 10 Ch.02** — `instanceof` patterns.

Brief refresher from M07: a **sealed** class or interface lists **exactly which types** may directly extend or implement it. **Permitted** subtypes must be **final**, **sealed**, or **non-sealed**. With all variants covered, a **`switch`** on the sealed type needs **no `default`** — the compiler proves **exhaustiveness**.

## 3. Why This Concept Exists

Open inheritance makes **closed domain** modeling unsafe:

- A `switch` on `Expression` needs **`default`** because unknown subclasses may appear in another module.
- Visitors and double-dispatch add boilerplate for simple “evaluate” or “render” functions.

**Sealed types** document a **closed world**. **Records** model **variant payloads**. **Pattern matching `switch`** (final in **21**, JEP 441) lets you write **direct, exhaustive** logic:

```java
return switch (expr) {
    case Const c -> c.value();
    case Add a -> eval(a.left()) + eval(a.right());
};
```

Adding a permitted variant **breaks compilation** until every switch is updated — preferable to silent `default` branches.

## 4. Conceptual Explanation

### 4.1 ADT style in Java

```java
sealed interface Expr permits Const, Add, Neg {}

record Const(int value) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
record Neg(Expr inner) implements Expr {}
```

This is Java’s idiom for a **sum type**: an `Expr` **is** exactly one of the permitted forms, each with its own data.

Compare **Module 07** `Result` / `Ok` / `Err` and `Shape2D` — same pattern, smaller teaching hierarchies there; this chapter focuses on **recursive trees** and **pattern depth**.

### 4.2 Exhaustive `switch` on sealed types

When the selector type is sealed and cases list **every permitted subtype** (with patterns or type names per JLS rules), **exhaustiveness** is compile-time checked. No `default` required.

If you omit a variant, `javac` reports the switch is not exhaustive.

### 4.3 Type patterns and record patterns

**Type pattern** — bind when runtime type matches:

```java
case Const c -> ...
```

Here `c` is a `Const` record; use `c.value()`.

**Record pattern** (destructure in place):

```java
case Add(var left, var right) -> eval(left) + eval(right);
```

Java 21 allows listing **nested** patterns when components are records or sealed types:

```java
case Add(Const(var v), var right) -> v + eval(right);
```

Dominance rules apply: **more specific** patterns before **general** ones (Module 03).

### 4.4 `when` guards

```java
case Const c when c.value() > 0 -> "positive";
```

Guard evaluates only if pattern matches. Combine with sealed exhaustiveness — all non-guarded cases must still cover possibilities or use guarded overlap carefully.

### 4.5 `non-sealed` (revisit)

A permitted subtype may be **`non-sealed`**, reopening inheritance below that branch — use when one variant must stay extensible (plugin architecture). Prefer **sealed** branches for core domain; **`non-sealed`** as escape hatch with documentation.

### 4.6 Enum vs sealed + records (Module 15 synthesis)

| Model | Example |
|-------|---------|
| **Enum** | `TrafficLight { RED, YELLOW, GREEN }` — uniform constants |
| **Sealed + records** | `Payment permits Card, Wallet` — different fields per method |
| **Open class** | Framework extension points, plugin hosts |

Module 07 Ch.04 asked “enum or sealed?” — answer by **shape of data**, not headcount of variants.

### 4.7 Relating demos

| Demo | Module | Focus |
|------|--------|-------|
| [sealed-classes-demo](../../examples/core-java/sealed-classes-demo/) | **07** | `Result`, `Shape2D`, first exhaustive switches |
| [pattern-control-demo](../../examples/core-java/pattern-control-demo/) | **03** | `Object`/`Shape` patterns, null cases |
| [sealed-patterns-demo](../../examples/core-java/sealed-patterns-demo/) | **15** | Recursive `Expr`, record patterns, `eval` |

You should recognize the **same language features** at increasing depth — not three unrelated syntaxes.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Permitted subtype** | Class/interface directly listed on a sealed parent |
| **Exhaustive switch** | Compiler-checked coverage of all sealed variants |
| **Record pattern** | Pattern deconstructing record components in `case` or `instanceof` |
| **ADT (algebraic data type)** | Sum of variants, each optionally carrying data |
| **Type pattern** | `Type name` testing and binding in switch/instanceof |

## 6. Syntax and Language Rules

```java
sealed interface Node permits Leaf, Branch {}
final class Leaf implements Node {}
final class Branch implements Node {}

static int size(Node n) {
    return switch (n) {
        case Leaf l -> 1;
        case Branch b -> size(b.left()) + size(b.right());
    };
}
```

- Permitted types must exist and be **direct** subtypes (or be nested / same compilation unit per JLS).
- **`sealed` class** may have state; **sealed interface** + **records** is the common ADT style.
- Pattern `switch` on **non-sealed** types still needs **`default`** unless selector is `null`-handled and all known subtypes listed with patterns — sealing is what enables proof.

## 7. Step-by-Step Examples

### 7.1 Minimal — `SealedPatternsDemo`

[examples/core-java/sealed-patterns-demo/](../examples/core-java/sealed-patterns-demo/)

```java
Expr e = new Add(new Const(2), new Const(3));
System.out.println("eval=" + eval(e));
System.out.println("desc=" + describe(new Const(7)));
System.out.println("descAdd=" + describe(e));
```

**Verified output (Temurin 21.0.12.1):**

```text
eval=5
desc=const:7
descAdd=add
```

- `eval=5` — recursive `switch`: `Add` sums `eval(2)+eval(3)`.
- `describe` uses **type patterns** only (no deconstruction needed for `Const` label).

### 7.2 Realistic — API result type (M07 pattern, production framing)

```java
sealed interface ParseResult permits Ok, Err {}
record Ok(JsonNode node) implements ParseResult {}
record Err(int line, String message) implements ParseResult {}

String format(ParseResult r) {
    return switch (r) {
        case Ok(var node) -> "ok:" + node.size();
        case Err(var line, var msg) -> "L" + line + ": " + msg;
    };
}
```

HTTP layer maps `Err` to 400, `Ok` to handler — no third subclass without updating `permits` and switches.

### 7.3 Advanced — nested record pattern

```java
static boolean isAddOfTwoConsts(Expr e) {
    return switch (e) {
        case Add(Const(var a), Const(var b)) -> true;
        default -> false; // sealed Expr — could list Neg, single Const instead for exhaustiveness
    };
}
```

For **sealed** `Expr`, prefer listing **`Neg`**, **`Const`**, and **`Add(...)`** cases without `default` when possible; use `default` only when selector is wider than sealed root.

Refactor note: on **fully sealed** root, replace `default` with remaining variant cases for exhaustiveness.

## 8. Real-World Use Cases

- **Expression and query ASTs** — SQL, JSONPath, rule engines.
- **Domain events** — `OrderEvent` variants with different payloads.
- **Parser output** — sealed syntax tree, single `interpret` function.
- **Compiler internals** — JDK and IDEs use sealed types extensively (language reflecting on itself).

## 9. Internal Behavior

Pattern matching `switch` (Java 21) is specified by JLS — the compiler type-checks patterns, dominance, and exhaustiveness. Bytecode uses **type tests** and **invokedynamic** / conditional chains (implementation detail). Sealed hierarchies enable **closed switch** bytecode similar to **enum tables** when all types are known in the compilation unit.

No runtime penalty inherent in sealing itself; benefits are **safety and refactor speed**.

## 10. Edge Cases and Limitations

- **Adding a variant** is a **breaking change** for exhaustive switches across the codebase — intentional.
- **Reflection** can still instantiate permitted types; sealing does not block reflection, only **subclassing**.
- **`null`** — pattern switches may need explicit `case null ->` (Module 03).
- **Generics and patterns** — limited combinations; erasure still applies at runtime (Module 13).
- **Cross-module** sealing requires permitted types in agreed packages/modules.

## 11. Common Mistakes

### Incorrect — open hierarchy with “exhaustive” claim

```java
interface Expr {}
record Const(int v) implements Expr {}
// switch needs default — someone else can implement Expr
```

### Corrected

```java
sealed interface Expr permits Const {}
record Const(int v) implements Expr {}
```

### Incorrect — general pattern before specific

```java
switch (o) {
    case Object x -> ...
    case String s -> ... // unreachable — compile error
}
```

### Corrected

Order **specific** patterns first (Module 03 dominance).

## 12. Best Practices

- Keep sealed families **small and domain-meaningful**; split unrelated variants into separate sealed roots.
- Put **record leaves** beside sealed parent in same package when possible.
- Centralize **exhaustive** `switch` helpers (`eval`, `describe`, `validate`) rather than scattering `instanceof` chains.
- Document **`non-sealed`** branches with why openness is required.
- After adding a variant, run tests — compiler catches most switch sites.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Sealed + patterns | Exhaustive, readable ADTs | Closed world; coordinated updates |
| Visitor pattern | Open to new operations without editing variants | Boilerplate, double dispatch |
| Enum | Simplest constants | No per-constant distinct fields (without hacks) |

## 14. Performance Considerations

Recursive `eval` on deep trees risks **stack overflow** — iterative trampolines or explicit stacks for very deep inputs. Pattern switch itself is not the bottleneck in typical business logic.

## 15. Security Considerations

Sealing does not validate **content** — `Const` can hold user-supplied integers; still sanitize on output. Do not deserialize into sealed types without trusted producers.

## 16. Java Version and Compatibility Notes

- **Sealed classes** — final in **17** (JEP 409).
- **Record patterns** — final in **21** (JEP 440).
- **Pattern matching for switch** — final in **21** (JEP 441).
- Module 07 and 03 chapters were written for **21** without preview flags.

## 17. Related Language Comparisons

Haskell/ML **algebraic types**; Kotlin **sealed class**; Rust **enum** with data payloads. Java composes **existing inheritance** with sealing rather than a separate `enum` with payloads keyword.

## 18. Revision and Mastery

- **Gaps:** confusing M07 intro with full pattern depth; using `default` on sealed switches unnecessarily.
- **You should answer:** Why does `SealedPatternsDemo` compile `eval` without `default`?
- **Advanced:** module boundaries and `permits` across JPMS modules; serialization of sealed graphs.

## 19. Exercises

### Beginner

1. Predict three lines of `SealedPatternsDemo`.
2. Add `Neg` to `describe` returning `"neg"` — what breaks if you forget a case in `eval`?

### Intermediate

3. Rewrite `eval` using record pattern `Add(var l, var r)`.
4. Compare [sealed-classes-demo](../../examples/core-java/sealed-classes-demo/) `describe(Result)` with this chapter’s `describe(Expr)` — same mechanism?

### Advanced

5. Model `Option<T>` as `sealed interface Option<T> permits Some, None` vs `java.util.Optional` — trade-offs.

## 20. Interview Questions

1. What does `permits` enforce?
2. When is a `default` branch unnecessary in Java 21?
3. How do record patterns reduce casting?

## 21. Solutions

1. See §7.1.
2. Nothing if `eval` already has `Neg` case; if you add new permitted type, `javac` fails until `eval` and `describe` updated.
3. `case Add(var left, var right) -> eval(left) + eval(right);`
4. Yes — type patterns on sealed interfaces with record implementors.
5. Custom `Option` is closed and null-free by design; JDK `Optional` is a single class, nullable inside, ecosystem-standard.

## 22. Chapter Summary

This chapter **unifies** enums, records, and sealing: **sealed** roots define allowed variants; **records** hold per-variant data; **pattern switches** express exhaustive operations. **`SealedPatternsDemo`** evaluates a small expression language — the same ideas as Module 07’s `Result`/`Shape`, deepened with recursion and record patterns. **Module 16** applies similar functional style to **lambdas** and **streams**.

## 23. Key Terminology

Sealed type · permits · exhaustiveness · type pattern · record pattern · ADT

## 24. References and Further Reading

- [JLS 8.1.4, 8.1.6 — Sealed classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [Module 07 Ch.04 — sealed classes](../07-inheritance-and-polymorphism/04-sealed-classes.md)
- [Module 03 Ch.04 — patterns](../03-control-flow/04-branching-and-pattern-matching.md)

## 25. Next Steps

- [Module 16 — Lambdas and Functional Programming](../16-lambdas-and-functional-programming/README.md)
- Revisit [pattern-control-demo](../../examples/core-java/pattern-control-demo/) for null and guard practice

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; `SealedPatternsDemo` verified Temurin 21.0.12.1
- [x] Cross-links to M07 and M03
- [x] Revision and Mastery, exercises, references
