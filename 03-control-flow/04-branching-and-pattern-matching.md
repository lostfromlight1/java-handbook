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

A **sealed** type declares which classes may implement it; only those subtypes can appear at runtime. **Records** (Module 15) supply compact data carriers—here they implement a small `Shape` hierarchy used in the demo and below (read top to bottom before the `switch`):

```java
sealed interface Shape permits Circle, Rectangle {}

record Circle(double radius) implements Shape {}

record Rectangle(double width, double height) implements Shape {}
```

**Sealed** means “only `Circle` and `Rectangle` exist”; **permits** lists them. The compiler uses that closed set for exhaustiveness checking.

```java
static double area(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
    };
}
```

Each `case` is a **type pattern**: if `shape` is a `Circle`, bind `c` and run the first arrow; if a `Rectangle`, bind `r`. No `default` is required because every permitted subtype has a `case`.

### 4.3 `when` guards in `switch`

Besides `instanceof` guards (`&& d > 0` above), **switch** cases can add a **`when`** clause—extra boolean condition after the pattern:

```java
static String sizeHint(Shape shape) {
    return switch (shape) {
        case Circle c when c.radius() > 10 -> "large circle";
        case Circle c -> "small circle";
        case Rectangle r when r.width() == r.height() -> "square";
        case Rectangle r -> "rectangle";
    };
}
```

More specific cases (large circle) must appear **before** broader ones (`Circle` without `when`) so the compiler’s **dominance** rules are satisfied—the first matching case wins.

### 4.4 `case null`

Modern switches can handle null explicitly instead of always NPE-ing on the selector:

```java
case null -> "missing";
```

### 4.5 Labeled break/continue (brief)

```java
outer:
for (...) {
  for (...) {
    break outer;
  }
}
```

Valid but easy to abuse. Prefer methods returning results from inner searches.

### 4.6 What this chapter is not

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

Progression for branching on a `String` held as `Object`:

**1. Classic cast style (still valid, verbose):**

```java
Object value = "Java";
if (value instanceof String) {
    String s = (String) value;
    System.out.println(s.length());
}
```

**2. Pattern matching for `instanceof` (Java 16+):**

```java
if (value instanceof String s) {
    System.out.println(s.length());  // s in scope only here
}
```

**3. `switch` with patterns and null** (demo `describe` / `trafficLight` / `area`):

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

### Incorrect — dominance (case order)

```java
// switch (obj) {
//     case Object o -> "anything";
//     case String s -> "text";  // compile error: String is covered by Object above
// }
```

A **general** pattern (`Object`) before a **specific** one (`String`) makes the `String` case unreachable—the compiler rejects it. Put **more specific** cases first.

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
| Separate handler per type | Clear extension points | More classes/files |

*Track B:* The **Visitor** pattern is another way to dispatch on multiple types; pattern switches often replace small visitor hierarchies with less boilerplate.

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

Record patterns, nested patterns, `when` guards, dominance rules, sealed switches vs spreading behavior across virtual methods.

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

Type pattern · guard · `when` · exhaustiveness · sealed · labeled break

## 24. References and Further Reading

- JEP 394 (instanceof patterns), JEP 441 (pattern switch)  
- Module 15 enums/records/sealed; Module 10 type system  

## 25. Next Steps

[Module 03 assessment](../assessments/module-assessments/03-control-flow-assessment.md) → [Module 04 methods](../04-methods-and-functions/README.md)

## Review Checklist

- [x] PatternControlDemo verified on JDK 21.0.9
- [x] Feature versions labeled
- [x] Depth gate passed
