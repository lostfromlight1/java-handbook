# Assessment: Module 06 — Classes and Object Design

> **Type:** Module  
> **Maps to:** Module 06 chapters 01–04  
> **Time guide:** 60–90 minutes  
> **Solutions:** [06-classes-and-objects-assessment-solutions.md](06-classes-and-objects-assessment-solutions.md)

## Purpose

This assessment is a **learning diagnostic**, not a measure of professional worth.

## Instructions

- Attempt without looking at solutions.
- Prefer predicting output on paper before running code.
- Compile demos with Temurin JDK 21 if you verify predictions.

## Part A — Conceptual

1. What is a constructor, and how does it differ from an ordinary method?
2. When does the compiler stop supplying a default no-argument constructor?
3. What does `this(...)` do, and where must it appear in a constructor?
4. In what order do **static** initialization, **instance** initialization, and **constructor bodies** run for a first `new` on a class?
5. Distinguish **static nested** class vs **inner** (non-static nested) class.
6. What syntax creates an inner class instance tied to a particular outer object?
7. What is an **anonymous class**? When might a lambda replace it?
8. List three rules for making a Java class **immutable** at the object level.
9. What problem does a **builder** solve compared to many overloaded constructors?
10. State the **equals contract** properties (reflexive, symmetric, transitive, consistent, null).
11. Why must equal objects have the same `hashCode`?
12. What does `==` compare for reference types? What does `equals` compare when overridden correctly?

## Part B — Predict (ConstructorsDemo / NestedClassesDemo)

13. After `new User("ada")` and `new User("lin", "Lin")`, what is `User.created()`?
14. Output of `new User("ada").describe()`?
15. `new Point(3, 4)` printed with default `toString` if only `Point` overrides `toString` to `(x,y)` — demo output line?
16. Can you write `new Outer.Inner()` without an `Outer` instance reference? Why?
17. After demo `main`, how many lines print, and what is the third line?

## Part C — Predict (Immutability / Equals)

18. `new Money(500, "USD").plus(new Money(750, "USD"))` printed — demo uses 1250; if you call `plus` as in demo setup, result string?
19. `HttpRequest.builder().method("GET").build()` without `url` — exception type/message from demo?
20. `Sku a` and `Sku b` both `("ABC", 1)` — `a.equals(b)` and `a == b`?
21. After adding `a`, `b`, `c` to `HashSet<Sku>` as in demo — `set.size()`?
22. If `Sku` overrides `equals` but not `hashCode`, likely `set.size()` after same three adds?

## Part D — Debugging

23. Constructor validation duplicated in three overloaded constructors — one refactoring fix?
24. Inner class stored in a static cache holds reference to outer Activity — symptom in Android-style apps?
25. `public boolean equals(Sku other)` added but not `equals(Object o)` — does `set.contains` work for value match?
26. Mutable `List` field exposed via getter from “immutable” object — what remains mutable?
27. `toString` logs full credit card number — what category of issue?

## Part E — Coding

28. Write `Rgb` with `final int r,g,b`, constructor 0–255, `toString` as `rgb(r,g,b)`.
29. Write `Circle` with `final double radius`, reject negative, `area()` returning πr² (use `Math.PI`).
30. Write `Version` with `major` and `minor` final ints; implement `equals`, `hashCode`, `toString`.
31. Write static nested `Builder` for `EmailMessage` with `to`, `subject`, `build()` requiring non-blank `to`.

## Part F — Advanced

32. Explain why `final` fields help immutability but do not alone make an object immutable.
33. `getClass()` vs `instanceof` in `equals` — one trade-off sentence each.
34. When might you **not** include all fields in `equals` for a JPA entity?

## Scoring guide

| Band | Next |
|------|------|
| Strong | Module 07 — Inheritance and Polymorphism |
| Partial | Re-read weak M06 chapters; rerun four demos |
| Weak | Review Module 05 OOP + Module 06 Ch.01 constructors first |

## Chapter mapping

| Q | Ch |
|---|-----|
| 1–4, 13–15, 23, 28–29 | 01 |
| 5–7, 16–17, 24 | 02 |
| 8–9, 18–19, 26, 31–32 | 03 |
| 10–12, 20–22, 25, 27, 30, 33–34 | 04 |
