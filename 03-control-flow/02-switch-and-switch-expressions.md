# Switch and Switch Expressions

> **Status:** Completed  
> **Module:** 03 — Control Flow  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Write classic `switch` statements with `case`, `break`, and `default`.
- Explain fall-through and when it is intentional vs accidental.
- Use **switch expressions** with arrows (`->`) and multi-labels.
- Choose `if` vs `switch` appropriately.
- Know that pattern switches deepen in Chapter 04 / Module 15.

## 2. Prerequisites

- [01-conditionals.md](01-conditionals.md)
- Module 02 types (integrals, `String`, enums later)

## 3. Why This Concept Exists

When one value maps to many discrete outcomes, long `else if` chains are noisy. `switch` states “branch on this selector.” Modern switch expressions make that mapping a value-producing expression with less boilerplate and fewer fall-through bugs.

## 4. Conceptual Explanation

### 4.1 Classic switch statement

```java
switch (day) {
    case "MON":
    case "TUE":
        System.out.println("weekday");
        break;
    default:
        System.out.println("other");
        break;
}
```

**Fall-through:** without `break`, execution continues into the next case. Sometimes intentional (grouped cases); often a bug.

### 4.2 Selector types (classic)

Historically: integrals, `enum`, `String`. Pattern matching expands this (Chapter 04).

### 4.3 Switch expressions (Java 14+)

```java
String kind = switch (day) {
    case "MON", "TUE", "WED", "THU", "FRI" -> "weekday";
    case "SAT", "SUN" -> "weekend";
    default -> "unknown";
};
```

Arrow labels do **not** fall through. The switch **yields a value**. Completeness rules apply for enums/sealed types later.

### 4.4 Blocks in switch expressions

Use `yield` when a case needs statements:

```java
case "MON" -> {
    System.out.println("logging");
    yield "weekday";
}
```

## 5. Terminology

| Term | Meaning |
|------|---------|
| Fall-through | Continuing into the next case |
| Switch expression | Switch that produces a value |
| Arrow label | `case L ->` form without fall-through |
| Exhaustiveness | All possible values handled (enums/sealed) |

## 6. Syntax and Language Rules

- Classic colon cases need `break` (or `return`) to avoid fall-through.
- Arrow cases cannot fall through.
- `null` on classic `String` switch throws `NullPointerException`; newer pattern/`case null` handling appears in modern switches (Chapter 04 demo).

## 7. Step-by-Step Examples

[examples/core-java/switch-demo/](../examples/core-java/switch-demo/)

```bash
java SwitchDemo MON
```

**Verified:**

```text
day=MON
legacy=weekday
kind=weekday
label=two
```

## 8. Real-World Use Cases

HTTP method routing sketches, enum state machines, command parsers, mapping status codes to messages.

## 9. Internal Behavior

Compilers may emit lookup/table switches for dense ints. Treat as optimization detail (HotSpot/compiler), not something you micro-manage early.

## 10. Edge Cases and Limitations

- Forgetting `default` on classic switch may be OK; switch *expressions* often require completeness.
- `String` case labels are case-sensitive.
- Prefer enums over raw strings when the set is closed (Module 15).

## 11. Common Mistakes

### Incorrect

Missing `break` causing accidental fall-through.

### Corrected

Add `break`, or use arrow switch expressions.

### Incorrect

Assuming `"mon"` matches `"MON"`.

### Corrected

Normalize case or use enums.

## 12. Best Practices

- Prefer switch expressions for value mapping.
- Group labels with commas in arrow form.
- Keep case bodies small; call methods for heavy work.

## 13. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| if/else | Open ranges easy | Verbose for many discrete values |
| switch | Clear discrete map | Awkward for ranges (unless patterns/guards) |
| Map/polymorphism | Extensible | More structure |

## 14. Performance Considerations

Usually irrelevant vs clarity. Dense int switches can be fast; measure only hot paths (Module 28).

## 15. Security Considerations

Don’t switch on unsanitized input for security decisions without a default deny path.

## 16. Java Version and Compatibility Notes

| Feature | Status |
|---------|--------|
| Classic switch | Long-standing |
| Switch expressions | Final since **Java 14** |
| Pattern switch | Chapter 04 / **Java 21** primary path |

Validated on **21.0.9**.

## 17. Related Language Comparisons

Similar to C# switch expressions / Kotlin `when`. Fall-through is a C heritage footgun Java still carries in classic form.

## 18. Revision and Mastery

Exhaustiveness with sealed types; `yield`; pattern/`when` guards (Ch.04).

## 19. Exercises

### Beginner

1. Run SwitchDemo with `SAT` and `XYZ`.
2. Convert an if/else chain of three statuses to a switch expression.

### Intermediate

3. Classic switch that groups 1–5 as `"small"` using fall-through intentionally—then rewrite with arrows.
4. Handle `default` for unknown commands printing to stderr conceptually.

### Advanced

5. Research why switch expressions require exhaustiveness for enums.
6. Write a switch expression using a block/`yield`.

## 20. Interview Questions

1. **What is fall-through?** — Execution continuing to next case without break.  
2. **Arrow vs colon?** — Arrows don’t fall through; often used in expressions.  
3. **Switch on String?** — Yes since Java 7.  

## 21. Solutions

1. weekend / unknown.  
3. Comma-separated arrow labels preferred.  

## 22. Chapter Summary

Classic switch needs careful `break`s; switch expressions map values safely without fall-through. Prefer expressions for modern Java 21 code.

## 23. Key Terminology

Fall-through · switch expression · arrow label · default · yield

## 24. References and Further Reading

- JEP 361 (switch expressions)  
- Next: [03-loops.md](03-loops.md)

## 25. Next Steps

[03-loops.md](03-loops.md)

## Review Checklist

- [x] SwitchDemo verified
- [x] Version notes for switch expressions
