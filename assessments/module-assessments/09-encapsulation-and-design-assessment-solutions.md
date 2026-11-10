# Solutions: Module 09 — Encapsulation and Design

> **Assessment:** [09-encapsulation-and-design-assessment.md](09-encapsulation-and-design-assessment.md)

## Part A — Conceptual

1. `private`, package-private, `protected`, `public`. `protected`: subclasses in other packages + same package + class body.
2. Public API: what clients may depend on. Implementation: private/package helpers and representation.
3. Bypass validation; lock callers to field layout.
4. Coupling: interdependence between units. Cohesion: how focused one unit’s responsibilities are.
5. One reason to change per class (one axis of responsibility).
6. Examples: content (touching private internals); common (shared globals); control (boolean flags).
7. Depends on contract, not concrete class; swappable at wiring time.
8. Composition: has-a collaborator. Inheritance: is-a subtype via `extends`.
9. Outer object forwards call to internal collaborator.
10. Superclass changes break subclasses without subclass edits.
11. Inheritance: true subtype, template hooks, sealed domains. Composition: reuse, mix behaviors, swap impl.
12. Subtypes must substitute for supertypes without surprising callers—inherit only when true.
13. Car is not a kind of Engine; wrong substitutability and API surface.
14. Hide representation; expose controlled operations.
15. Callers mutate list and break invariants—return copy or unmodifiable view.

## Part B

16. `150`
17. `2`
18. No — `apply` is private.
19. `IllegalArgumentException` for `"amount"`.
20. No.

## Part C

21. `1050`
22. `ORD-9 => 1050 cents`
23. No.
24. `1000`
25. No.

## Part D

26. `Sedan / 180hp`
27. `start=true`
28. `log:begin save`, `log:end save`, `done:save` (third line is first log line)
29. `done:save`
30. Yes syntactically, but wrong domain—is-a misuse.

## Part E

31. Subclasses of `X` in other packages; not arbitrary classes unless they subclass.
32. Not allowed — must be same or wider.
33. Exposes full `ArrayList` mutability as `Report` API.
34. At least two (pricing + formatting).
35. No — mixes service with logger implementation; harder to test and swap.

## Part F

36. Check `amount <= balance` then subtract via private helper; throw or return boolean per your sketch.
37. `TaxCalculator` + `HtmlReportWriter` with data passed between.
38. Standard demo pattern: `private final Engine engine`; `return engine.start();`.
39. `FakeLogger implements Logger` appending to `List<String>`; `new AuditedService(fake)`.

## Part G

40. `exports` — module API to other modules. Package-private — same package only within module.
41. Decorator holds inner `PaymentProcessor`; subclass ties to one hierarchy.
42. Closed set of variants with exhaustiveness (e.g. sealed `Shape`) — inheritance documents allowed subtypes.
