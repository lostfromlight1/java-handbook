# Solutions: Module 08 — Interfaces and Abstraction

> **Assessment:** [08-interfaces-and-abstraction-assessment.md](08-interfaces-and-abstraction-assessment.md)

## Part A — Conceptual

1. **Syntactic:** method names, parameters, returns, exceptions. **Semantic:** behavioral promises (substitutability, null rules, side effects).
2. No — interface has no instantiable implementation by itself.
3. One direct superclass; many interfaces.
4. `WalletProcessor.pay` — dynamic dispatch on runtime type.
5. Class cannot extend two classes sharing conflicting implementation; interfaces add types without inheriting conflicting instance state from two classes.
6. Instance method on interface with body; backward-compatible API evolution on existing implementors.
7. `InterfaceName.method(...)` — qualified by interface type.
8. Only other methods in the same interface (defaults/privates).
9. Exactly one abstract instance method (SAM).
10. Compiler error if more than one abstract instance method.
11. Empty tag interface; e.g. `Serializable`.
12. High-level policy depends on abstractions; low-level modules implement them.
13. Tests and production swap storage without changing service logic.
14. Clients should not depend on methods they do not use — split interfaces.
15. Overrides conflicting default by delegating to chosen `A.super.m()` or merged behavior.

## Part B

16. `card:1500`
17. `wallet:900`
18. `email:paid` (line to stdout)
19. `audit=CARD-1`
20. No — only `PaymentProcessor`.

## Part C

21. `hello Ada`
22. `HELLO ADA`
23. `*** team ***`
24. `greetLoud` (inherited default); also `banner`/`shout` not overridden on class (static/private on interface).
25. Yes — unless override changes call pattern; default `greetLoud` calls `greet` on `this`.

## Part D

26. `5`
27. `6`
28. `10`
29. `true`
30. No — interface no longer functional; lambdas invalid.

## Part E

31. `created:ada`
32. `exists:ada`
33. `find=ada` (demo returns username string `ada`)
34. `created:bob`
35. Returns `null` from `find`; service maps to `"missing"` if called that way — demo `find` on service returns `ada` or `missing` string.

## Part F

36. No — missing `pay` implementation.
37. No — `PaymentProcessor` has no `auditId`.
38. Override the method or merge with qualified super calls — compile error if ignored.
39. No — not functional (two abstract methods).
40. Poor — leaks JDBC into domain seam; keep repository domain-focused.

## Part G

41. `OrderService(Clock clock)` uses `clock.nowMillis()` for expiry checks; fake returns constant for deterministic tests.
42. If only one abstract method besides defaults, yes; two abstract methods would break functional status.
43. `exists`: compare stored to username; `save`: assign field; `find`: return stored or null per exists logic (demo pattern).
44. `public int v() { return A.super.v() + B.super.v(); }` or pick one — must override.

## Part H

45. `invokeinterface` — interface method; `invokevirtual` — class instance method (simplified).
46. Shared mutable fields + `final` template workflow in one hierarchy.
47. Same semantics across adapters: exists/save/find behavior, null rules, idempotence documented in tests.
