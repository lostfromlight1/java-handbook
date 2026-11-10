# Solutions: Module 15 — Enums, Records, and Sealed Types

> **Assessment:** [15-enums-records-sealed-assessment.md](15-enums-records-sealed-assessment.md)

## Part A

1. **No** — enum types are implicitly final and extend `Enum`.
2. Throws **`IllegalArgumentException`** — `valueOf` matches constant **names** case-sensitively (`HIGH` not `high`); use `from("low")` style parser for messy input.
3. Each constant is a **singleton**; reference identity matches the intended constant.
4. Any two of: **`equals`**, **`hashCode`**, **`toString`**, component **accessors** (`x()`, `y()`).
5. **Validate** invariants on implicit canonical parameters without re-declaring the parameter list.
6. **No** — only components in the header (plus static fields allowed).
7. Only **listed permitted types** may directly extend/implement; they must be `final`, `sealed`, or `non-sealed`.
8. When cases cover **every permitted subtype** of the sealed selector (compiler exhaustiveness).
9. Both are **sealed ADTs** with **record** variants; `Result` is flat auth/result modeling, `Expr` is recursive evaluation (Ch.03 deepens M07).
10. Fixed named constants with **uniform** shape / little per-variant data.
11. **`x()`** (component accessor).
12. **Reordering** enum declarations changes ordinal and breaks order-based logic silently.

## Part B

13. `HIGH`  
14. `2`  
15. `LOW`  
16. `true`

## Part C

17. `Point[x=3, y=4]`  
18. `equals=true`, `hashEqual=true`  
19. `contains3=true`, `contains9=false`

## Part D

20. `5`  
21. `const:7`  
22. `add`

## Part E

23. **Yes** — caller can mutate the shared list; shallow immutability broken; use `List.copyOf` in compact ctor.
24. **Invalid** — third parties can implement; switch cannot be proven exhaustive without `default`.
25. **Compile error** — `String` case unreachable (dominance).
26. **`permits` clause**, every exhaustive **`switch`** on `Expr`, and any factory/API producing `Expr`.
27. **Avoid** — use classes for entities; records for DTOs/value objects.
28. **Compact representation** / efficient set ops on enum universe (bit vector).
29. **Reopens** unrestricted subclassing below that branch.
30. **Module 03** Ch.04 (control-flow); Module 10 Ch.02 for type-system angle.

## Remediation map

| Missed | Revisit |
|--------|---------|
| Part B | Ch.01 `valueOf` vs custom `from` |
| Part C | Ch.02 compact ctor, equals contract |
| Part D | Ch.03 + `sealed-patterns-demo` source |
| Part E 24–26 | M07 Ch.04 + Ch.03 §4.2 |
