# Assessment: Module 09 — Encapsulation and Design

> **Type:** Module  
> **Maps to:** Module 09 chapters 01–03  
> **Time guide:** 45–75 minutes  
> **Solutions:** [09-encapsulation-and-design-assessment-solutions.md](09-encapsulation-and-design-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.

## Part A — Conceptual

1. List the four Java access levels and who can access `protected` members from another package.
2. What is the difference between a type’s **public API** and its **implementation details**?
3. Why are **public fields** on domain types discouraged?
4. Define **coupling** and **cohesion** in one sentence each.
5. What is **SRP** (informal wording)?
6. Name two forms of **tight coupling** (e.g. content vs data).
7. How does **constructor injection** of an interface reduce coupling?
8. What is **composition** vs **inheritance** (has-a vs is-a)?
9. What is **delegation**?
10. What is the **fragile base class** problem?
11. When is **inheritance** appropriate vs **composition**?
12. How does **LSP** relate to choosing inheritance?
13. Why is `class Car extends Engine` a modeling mistake?
14. What is **information hiding**?
15. What is wrong with `getItems()` returning the live internal `List`?

## Part B — Predict (AccessControlDemo)

16. After `credit(100)` and `credit(50)`, what is `balance()`?
17. What is `entryCount()` after those calls?
18. Can `main` call `ledger.apply(10)`? Why?
19. If `credit(-1)` is called, what happens (per demo code)?
20. Are `balance` and `entries` fields visible outside `Ledger`?

## Part C — Predict (CouplingCohesionDemo)

21. `totalCents(2, 500, 50)`?
22. `format("ORD-9", 1050)`?
23. Does `InvoiceFormatter` call methods on `OrderCalculator`?
24. `totalCents(1, 1000, 0)`?
25. If you change cents suffix to `USD` in formatter only, must calculator change?

## Part D — Predict (CompositionVsInheritanceDemo)

26. `describe()` for Sedan + 180hp engine?
27. `start=` line?
28. Third through fifth lines after `execute("save")` with `ConsoleLogger`?
29. Return value of `execute("save")`?
30. Would subclassing `Engine` for `Car` make `Car` substitutable where `Engine` is expected?

## Part E — Debugging / Design

31. `public class X { private int n; protected int getN() { return n; } }` — who can call `getN()` from another package?
32. Narrower visibility on override — allowed?
33. `class Report extends ArrayList<String>` — coupling concern?
34. Merge calculator + formatter into one class — how many reasons to change?
35. `AuditedService extends ConsoleLogger` — better than composition? Why?

## Part F — Coding

36. Add `debit(int amount)` to `Ledger` that refuses overdraft; keep fields private.
37. Split a sketch class that both computes tax and writes HTML into two types.
38. Implement `Car` with `Engine` field and `start()` delegation only (no `extends`).
39. Sketch `FakeLogger` for tests and inject into `AuditedService`.

## Part G — Advanced

40. JPMS `exports` vs package-private — one sentence each.
41. Decorator vs subclass for adding logging around `PaymentProcessor`.
42. When would **sealed** inheritance beat composition for a closed domain?

## Scoring guide

| Band | Next |
|------|------|
| 0–60% | Re-read Ch.01–02; rerun Part B–C demos |
| 60–85% | Ch.03 + coding 36–39 |
| 85%+ | Module 10; try `CachingLogger` decorator |

## Integrity

Solutions are separate. Discuss approaches without copying full code in group settings.
