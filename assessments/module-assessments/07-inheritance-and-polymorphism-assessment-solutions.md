# Solutions: Module 07 — Inheritance and Polymorphism

> **Assessment:** [07-inheritance-and-polymorphism-assessment.md](07-inheritance-and-polymorphism-assessment.md)

## Part A — Conceptual

1. When the superclass has no accessible no-arg constructor (or when you must chain to a specific superclass ctor); must be first statement if not chaining `this(...)`.
2. Subclasses (even in other packages) may access `protected` members; `private` never.
3. No to `final` method. `static` methods are not overridden—`@Override` on static should error; no dynamic dispatch.
4. Superclass changes can break subclasses that rely on overridable hooks in unexpected call order.
5. Type that may declare abstract methods; cannot instantiate with `new`.
6. Fixed algorithm in `final` (or public) method calling overridable hooks in subclasses.
7. Shared instance state / constructors / non-interface template workflow in one hierarchy.
8. JVM selects overridden instance method based on runtime class of object.
9. Subtypes must be substitutable for supertypes without surprising callers.
10. Mutable width/height invariants conflict with square semantics—LSP breaks.
11. Which types may directly extend/implement the sealed type.
12. Lists those allowed direct subtypes.
13. When cases cover every permitted subtype of the sealed domain.
14. Enum when no per-variant fields; sealed+records when variants carry different data shapes.

## Part B

15. `Savings(S-1) bal=1000 min=50`
16. Balance `1200` after deposit.
17. `true`
18. `false`
19. Per-transaction cap `amount <= 1000`.

## Part C

20. Six lines (two reports × three lines each: header, body, footer separated by newlines in one `println` each).
21. `Rectangle area=12` then `Square area=25`
22. `4`
23. `Rectangle` then `Square` overrides — runtime class’s `name()`.

## Part D

24. `ok:42`
25. `err:boom`
26. `area=12.566370614359172` (Temurin 21.0.12.1; verify locally if JDK differs).
27. Compile error — switch not exhaustive.

## Part E

28. Superclass constructor not invoked / no default constructor available.
29. No — `render` is `final`.
30. Yes if subclass in same package as `Account`; `protected`/`package` visibility rules apply.
31. Parent’s field — compile-time type `Parent`.
32. New variant might fall through `default` without compiler forcing an update.

## Part F — Coding (representative)

33.

```java
sealed interface Expr permits Lit, Add {}
record Lit(int n) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}

static int eval(Expr e) {
    return switch (e) {
        case Lit l -> l.n();
        case Add a -> eval(a.left()) + eval(a.right());
    };
}
```

34.

```java
abstract class Exporter {
    final String export(Data d) {
        return "DATA\n" + formatBody(d);
    }
    protected abstract String formatBody(Data d);
}
```

35. `class Circle extends Shape { private final double r; ... int area() { return (int)(Math.PI * r * r); } }` — immutable radius, no conflicting setters.

## Part G

36. `invokevirtual` resolves virtual instance methods using runtime class (after override resolution).
37. When one permitted branch must remain open to further unknown subclasses.
38. Yes — return type may be a covariant subtype of superclass return.
