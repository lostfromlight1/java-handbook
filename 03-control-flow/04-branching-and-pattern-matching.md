# Branching and Pattern Matching in Control Flow

> **Status:** Completed  
> **Module:** 03 — Control Flow  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Use pattern matching for `instanceof` (`if (x instanceof String s)`).
- Apply pattern matching in `switch` (Java 21) for cleaner type-based branching.
- Handle `case null` in modern switches where appropriate.
- Preview how sealed types + switch exhaustiveness work together.
- Know labeled statements exist; prefer clearer structure first.
- Distinguish final Java 21 features from older instanceof+cast boilerplate.

## 2. Prerequisites

- Chapters [01](01-conditionals.md)–[03](03-loops.md)
- Comfort with objects as `Object` references (Module 00–01 level)
- Light preview of records/sealed types (fully taught in Module 15)—used here as motivation

## 3. Why This Concept Exists

Classic Java forced:

```java
if (obj instanceof String) {
    String s = (String) obj;
    ...
}
```

That is noisy and error-prone. Pattern matching binds a typed variable only when the test succeeds. Switch patterns scale type-based control flow without long if/else cast chains—important before generics-heavy and domain-model code.

## 4. Conceptual Explanation

### 4.1 Pattern matching for `instanceof`

```java
if (value instanceof String s) {
    return "string len=" + s.length();
}
```

`s` is in scope where the compiler knows the test passed. You can add guards:

```java
if (value instanceof Double d && d > 0) { ... }
```

### 4.2 Pattern matching for `switch` (Java 21)

```java
return switch (shape) {
    case Circle c -> Math.PI * c.radius() * c.radius();
    case Rectangle r -> r.width() * r.height();
};
```

With a **sealed** hierarchy, the compiler can require exhaustiveness—no silent missed subtype.

### 4.3 `case null`

Modern switches can handle null explicitly instead of always NPE-ing on the selector:

```java
case null -> "missing";
```

### 4.4 Labeled break/continue (brief)

```java
outer:
for (...) {
  for (...) {
    break outer;
  }
}
```

Valid but easy to abuse. Prefer methods returning results from inner searches.

### 4.5 What this chapter is not

Full treatment of records, sealed types, and record patterns lives in Module 15 / 10. Here you learn the **control-flow** payoff with a small sealed demo.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Type pattern | `Type name` in instanceof/switch |
| Guard | Extra boolean constraint (`&&` / `when`) |
| Exhaustiveness | All possibilities covered |
| Sealed type | Restricted subtype set (Module 15) |

## 6. Syntax and Language Rules

- Pattern variables are `final`-like in practice for the binding; don’t reassign casually.
- Dominance/ordering rules apply in pattern switches—more specific cases before general ones.
- `when` clauses appear in switch patterns for guards (Java 21).

## 7. Step-by-Step Examples

[examples/core-java/pattern-control-demo/](../examples/core-java/pattern-control-demo/)

**Verified excerpt:**

```text
string len=4
int=42
positive-double=3.5
null
traffic=go
shape=12.566370614359172
shape=12.0
```

Includes `instanceof` patterns, `case null` in a string switch, and a sealed `Shape` switch.

## 8. Real-World Use Cases

Parsing heterogeneous message payloads, UI command objects, AST evaluation, API error body variants.

## 9. Internal Behavior

The compiler emits type checks and casts safely. Sealed exhaustiveness is a compile-time check—HotSpot still executes ordinary type tests at runtime unless optimized. Label “language feature” vs “runtime optimization” carefully.

## 10. Edge Cases and Limitations

- Pattern switch dominance: a broad `case Object o` can shadow later cases if ordered wrong.
- Null handling differs between constructs—be explicit.
- Don’t overuse `Object` + patterns where proper polymorphism fits better.

## 11. Common Mistakes

### Incorrect

```java
if (value instanceof String) {
    String s = (String) value; // old style still works but verbose
}
```

### Corrected

Prefer `instanceof String s` on Java 16+ / 21 path.

### Incorrect

Non-exhaustive switch on sealed types without handling all permits.

### Corrected

Add cases or a legal default if allowed—compiler will guide you.

## 12. Best Practices

- Prefer patterns over manual casts.
- Keep sealed hierarchies small and domain-meaningful.
- Use polymorphism when behavior varies by type across many methods—not only switches.

## 13. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| Pattern switch | Local, expressive | Can centralize too much logic |
| Virtual methods | Open/OO | More types/classes |
| Visitor | Double dispatch | Ceremony |

## 14. Performance Considerations

Same order as instanceof chains; design for clarity. Optimize with measurement later.

## 15. Security Considerations

Don’t trust unvalidated external type tags; patterns don’t replace authentication/authorization.

## 16. Java Version and Compatibility Notes

| Feature | Version / status |
|---------|------------------|
| Pattern matching for instanceof | Final since **16** |
| Pattern matching for switch | Final in **21** (primary handbook) |
| Record patterns | Final in **21** (deeper in Module 15) |
| Sealed classes | Final since **17** |

Validated on **21.0.9**.

## 17. Related Language Comparisons

Similar spirit to Kotlin `when` / C# pattern matching—Java’s arrived via stepwise JEPs.

## 18. Revision and Mastery

Record patterns, nested patterns, `when` guards, dominance rules, replacing visitor with sealed switches carefully.

## 19. Exercises

### Beginner

1. Run PatternControlDemo; add a `Boolean` branch in `describe`.
2. Rewrite an old instanceof+cast site using a pattern variable.

### Intermediate

3. Add `case "YELLOW" ->` already exists—add a switch on an enum you define for lights.
4. Write a sealed `Result` with `Ok`/`Err` records and switch on it.

### Advanced

5. Show a compiler error from a non-exhaustive sealed switch (remove a case).
6. Compare a pattern switch vs adding an abstract `area()` method on `Shape`.

## 20. Interview Questions

1. **What does `instanceof String s` do?** — Tests type and binds `s` if true.  
2. **Why sealed + switch?** — Exhaustiveness checking.  
3. **Pattern switch Java version?** — Final in 21 for the modern form taught here.  

## 21. Solutions

1. `else if (value instanceof Boolean b) return "bool=" + b;`  
6. Polymorphism spreads behavior; switch keeps variants local—trade-offs.  

## 22. Chapter Summary

Pattern matching modernizes branching: bind typed names in `instanceof` and `switch`, handle null deliberately, and pair with sealed types for exhaustive domain modeling.

## 23. Key Terminology

Type pattern · guard · exhaustiveness · sealed · labeled break

## 24. References and Further Reading

- JEP 394 (instanceof patterns), JEP 441 (pattern switch)  
- Module 15 enums/records/sealed; Module 10 type system  

## 25. Next Steps

[Module 03 assessment](../assessments/module-assessments/03-control-flow-assessment.md) → [Module 04 methods](../04-methods-and-functions/README.md)

## Review Checklist

- [x] PatternControlDemo verified on JDK 21.0.9
- [x] Feature versions labeled
- [x] Depth gate passed
