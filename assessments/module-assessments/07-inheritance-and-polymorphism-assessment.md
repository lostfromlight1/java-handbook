# Assessment: Module 07 — Inheritance and Polymorphism

> **Type:** Module  
> **Maps to:** Module 07 chapters 01–04  
> **Time guide:** 60–90 minutes  
> **Solutions:** [07-inheritance-and-polymorphism-assessment-solutions.md](07-inheritance-and-polymorphism-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Predict demo output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.

## Part A — Conceptual

1. When must the first statement of a subclass constructor be `super(...)`?
2. What visibility does `protected` grant that `private` does not (to subclasses)?
3. Can a subclass override a `final` instance method? A `static` method with `@Override`?
4. What is the **fragile base class** problem in one sentence?
5. What is an **abstract class**? Can you invoke `new` on it?
6. What is the **Template Method** pattern?
7. When might an **abstract class** be preferable to an **interface** (Java 21)?
8. What is **dynamic dispatch** for instance methods?
9. State the **Liskov Substitution Principle** informally.
10. Why is `class Square extends MutableRectangle` often a design smell?
11. What does `sealed` restrict?
12. What role does `permits` play?
13. When is a `switch` on a sealed type **exhaustive** without `default`?
14. Compare **sealed interface + records** vs **enum** for a fixed variant set with data.

## Part B — Predict (InheritanceDeepDemo)

15. After `new SavingsAccount("S-1", 1000, 50)` and `describe()`, first line of output?
16. After `deposit(200)`, what is `balance()` via accessor used in describe?
17. `canWithdraw(900)` after deposit — true or false (per demo rules)?
18. `canWithdraw(1100)` after deposit — true or false?
19. Which business rule besides `minBalance` blocks 1100 in the demo?

## Part C — Predict (AbstractClassesDemo / DispatchLspDemo)

20. How many lines does `AbstractClassesDemo` print? What separates header and footer in `render()`?
21. First two lines of `DispatchLspDemo` output?
22. `Shape squareAsShape = new Square(2); squareAsShape.area()` — result?
23. If `Rectangle` overrode `name()` but reference type stays `Shape`, which `name()` runs in the loop?

## Part D — Predict (SealedClassesDemo)

24. Output of `describe(new Ok(42))`?
25. Output of `describe(new Err("boom"))`?
26. `area(new Circle(2))` printed line (expect JDK `double` string)?
27. If `Pending implements Result` is added to `permits` but `describe` is not updated, what happens at compile time?

## Part E — Debugging / Design

28. Subclass forgot `super(id, balance)` — typical compile-time error message theme?
29. `public class Evil extends Report { @Override String render() { return "x"; } }` — compiles?
30. Calling `super.deposit` from subclass when `deposit` is package-private on `Account` — allowed from subclass in same package?
31. Field hiding: `Parent p = new Child();` — which `x` prints if both declare `int x`?
32. Adding `default` to exhaustive sealed switch on `Result` — what risk when a new variant is added?

## Part F — Coding

33. Write `sealed interface Expr permits Lit, Add {}` with `record Lit(int n)` and `record Add(Expr left, Expr right)`, plus `int eval(Expr e)` using switch.
34. Write abstract `Exporter` with `final String export(Data d)` calling `protected abstract String formatBody(Data d)` and fixed header `"DATA\n"`.
35. Write sibling classes `Circle extends Shape` with `final double radius` (immutable) demonstrating LSP-safe extension (sketch `area()` only).

## Part G — Advanced

36. How does `invokevirtual` relate to overriding (one sentence, implementation-aware)?
37. When use `non-sealed` on a permitted subclass?
38. Covariant return on override — allowed?

## Scoring guide

| Band | Next |
|------|------|
| Strong | Module 08 — Interfaces and Abstraction |
| Partial | Re-read weak M07 chapters; rerun four demos |
| Weak | Review Module 05 Ch.03 + Module 06 Ch.01 constructors first |

## Chapter mapping

| Q | Ch |
|---|-----|
| 1–4, 15–19, 28, 30–31 | 01 |
| 5–7, 20, 29, 34 | 02 |
| 8–10, 21–23, 35 | 03 |
| 11–14, 24–27, 33, 32, 37–38 | 04 |
| 36 | 03 (+ JVM later modules) |
