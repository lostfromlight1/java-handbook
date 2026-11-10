# Assessment: Module 08 — Interfaces and Abstraction

> **Type:** Module  
> **Maps to:** Module 08 chapters 01–04  
> **Time guide:** 60–90 minutes  
> **Solutions:** [08-interfaces-and-abstraction-assessment-solutions.md](08-interfaces-and-abstraction-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.

## Part A — Conceptual

1. What is a **programming contract** at an interface boundary (syntactic vs semantic)?
2. Can you instantiate an interface with `new`? Why or why not?
3. How many direct superclasses may a Java class have? How many interfaces?
4. When a `PaymentProcessor` variable holds a `WalletProcessor`, which `pay` implementation runs?
5. What is the **diamond problem** for classes, and how do multiple interfaces avoid it?
6. What is a **default method**? Why did Java add it?
7. How do you call a **static** method declared on an interface?
8. Who can invoke a **private** method declared on an interface?
9. What makes an interface **functional**?
10. What does **`@FunctionalInterface`** enforce at compile time?
11. What is a **marker interface**? Give a JDK example.
12. State **Dependency Inversion Principle** informally.
13. Why should `UserService` depend on `UserRepository` rather than `InMemoryUserRepository`?
14. What is **Interface Segregation** in one sentence?
15. How can **`Interface.super.m()`** be used?

## Part B — Predict (InterfacesFundamentalsDemo)

16. Output of `card.pay(1500)` when `card` is `new CardProcessor()`?
17. Output of `wallet.pay(900)` for `WalletProcessor`?
18. After `n.notify("paid")` on `EmailNotifier`, what prints?
19. Value printed for `audit=` when `Auditable a = new CardProcessor()`?
20. Does `WalletProcessor` implement `Auditable` in the demo?

## Part C — Predict (InterfaceMethodsDemo)

21. Output of `g.greet("Ada")` on `FriendlyGreeter`?
22. Output of `g.greetLoud("Ada")`?
23. Output of `Greeter.banner("team")`?
24. Which method is **not** overridden in `FriendlyGreeter`?
25. If `greetLoud` were overridden to return `"!" + name`, would `greet("Ada")` still run the same way when called from the override?

## Part D — Predict (FunctionalMarkerDemo)

26. `add.apply(2, 3)`?
27. `mul.apply(2, 3)`?
28. `reduce(new int[]{1,2,3,4}, 0, add)`?
29. `p instanceof Entity` for `new Product("P1", 10)`?
30. If `IntOp` gained a second abstract method, would existing lambdas still compile?

## Part E — Predict (ApiInterfacesDemo)

31. First `register("ada")` return string?
32. Second `register("ada")` return string?
33. `find("ada")` after both registrations?
34. If `register("bob")` runs before any `ada` call, first line for `bob`?
35. Does `InMemoryUserRepository.find` return `null` or throw when user missing (per demo code)?

## Part F — Debugging / Design

36. `class X implements PaymentProcessor { }` — compiles?
37. `PaymentProcessor p = new WalletProcessor(); p.auditId();` — compiles?
38. Two interfaces with conflicting defaults — what must class `C` do?
39. `@FunctionalInterface interface F { void a(); void b(); }` — compiles?
40. Putting SQL `Connection` in a `UserRepository` method return type — good seam design? Why?

## Part G — Coding

41. Declare `interface Clock { long nowMillis(); }` and `class SystemClock implements Clock` plus a fake returning fixed value; sketch a `OrderService` taking `Clock` in constructor.
42. Add `default void logAndNotify(Notifier n, String msg)` on a sketched `LoggingNotifier` that prints then calls `n.notify(msg)` — must it stay functional?
43. Write `InMemoryUserRepository` method bodies for `exists`, `save`, `find` matching demo semantics (single user slot).
44. Resolve diamond: interfaces `A` and `B` both `default int v() { return 1; }` and `2` — class `C implements A,B` with explicit `v()`.

## Part H — Advanced

45. `invokeinterface` vs `invokevirtual` — one sentence each for instance calls.
46. When prefer **abstract class** over **interface + default** for shared workflow?
47. Contract test idea for `UserRepository` adapters — what to assert?

## Scoring guide

| Band | Next |
|------|------|
| 0–60% | Re-read Ch.01–02; redo Part B–C demos |
| 60–85% | Ch.03–04; coding parts 41–44 |
| 85%+ | Module 09; try decorator `CachingUserRepository` |

## Integrity

Solutions are separate. Discuss approaches without copying full code in group settings.
