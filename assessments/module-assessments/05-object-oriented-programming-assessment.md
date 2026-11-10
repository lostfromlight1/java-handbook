# Assessment: Module 05 — Object-Oriented Programming

> **Type:** Module  
> **Maps to:** Module 05 chapters 01–04  
> **Time guide:** 60–90 minutes  
> **Solutions:** [05-object-oriented-programming-assessment-solutions.md](05-object-oriented-programming-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Prefer predicting output on paper before running code.

## Part A — Conceptual

1. Distinguish a **class** from an **object**.
2. What are **state** and **behavior** in OOP terms?
3. What does `new BankAccount("Ada", 100)` do at a high level?
4. What does encapsulation protect, and why use `private` fields?
5. How does **abstraction** differ from encapsulation (related but not identical)?
6. What does `extends` mean? Give an is-a example.
7. Overriding vs overloading—one sentence each.
8. What is polymorphism in the `Employee e = new Manager(...); e.annualPay();` pattern?
9. Prefer composition when? Prefer inheritance when?
10. Has-a vs is-a—one example each.

## Part B — Predict

11. Two `BankAccount` objects with the same owner string—does `a == b`?
12. `BankAccount a = ...; BankAccount r = a;` — is `r == a` true?
13. `Temperature.fromCelsius(25).fahrenheit()` — expected value from the demo math?
14. In InheritancePolyDemo, `Manager("Riley", 50000, 10000).annualPay()` → ?
15. `Employee e = new Manager("Casey", 60000, 5000); e.annualPay()` → ?
16. CompositionDemo order with keyboard 2×3500 and cable 1×500 — total cents?

## Part C — Debugging

17. Public field `balance` mutated to a negative value from outside—what design fix?
18. Subclass constructor never calls `super(...)` and superclass has no no-arg constructor—what happens?
19. Method in subclass intended to override has a typo in the name—no `@Override`. Symptom?
20. Modeling `Car` as `extends Engine`—why is that a modeling smell?

## Part D — Coding

21. Write a `Book` class with private `title`/`pages`, a constructor, and `summary()`.
22. Write `Rectangle` with private width/height, validate positive sizes, `area()`.
23. Write `Animal` with `speak()` returning `"..."`, and `Dog extends Animal` overriding to `"woof"`. Print both via an `Animal` reference.
24. Write `Playlist` that **has** a fixed-size array of song title strings and `add`/`size` methods.

## Part E — Advanced

25. Why is `==` identity, not “same money balance,” for accounts? What will Module 06 add?
26. Explain fragile base class in one concrete sentence about changing `Employee.annualPay`.
27. In a REST backend, name one entity-style object and one composition relationship you would expect.

## Scoring guide

| Band | Next |
|------|------|
| Strong | Module 06 — Classes and Object Design |
| Partial | Re-read weak M05 chapters; rerun demos |
| Weak | Redo OopFundamentals + InheritancePoly demos first |

## Chapter mapping

| Q | Ch |
|---|-----|
| 1–3, 11–12, 21 | 01 |
| 4–5, 13, 17, 22 | 02 |
| 6–8, 14–15, 18–19, 23, 25–26 | 03 |
| 9–10, 16, 20, 24, 27 | 04 |
