# Solutions: Module 06 — Classes and Object Design

> **Assessment:** [06-classes-and-objects-assessment.md](06-classes-and-objects-assessment.md)

## Part A — Conceptual

1. Constructor initializes a new instance; same name as class; no return type; invoked only via `new` (or reflection). Methods have return types and are invoked by name.
2. When any explicit constructor is declared.
3. Calls another constructor in the same class; must be the **first** statement in that constructor if used.
4. Class static init once (before first use); then for each instance: instance field init and instance blocks in order, then constructor body.
5. Static nested: no implicit outer instance. Inner: non-static nested; tied to enclosing instance; can access outer instance fields.
6. `outerInstance.new InnerClass()` (or `outer.new Inner()` in demo).
7. Unnamed class at `new Type() { }`; lambda replaces when target is functional interface with one abstract method and no extra state/overrides needed.
8. Sample: `final` fields for state; no setters; defensive copies for mutable components; operations return new instances instead of mutating.
9. Many optional parameters; readable fluent assembly; single validation point in `build()`.
10. Reflexive, symmetric, transitive, consistent (for unchanged state), `x.equals(null)` false.
11. Hash-based collections use hash to find bucket then `equals`; equal keys must land in same bucket logic via same hash.
12. `==` compares references; overridden `equals` compares logical field values per implementation.

## Part B

13. `2`.
14. `ada/ada`.
15. `point=(3,4)` (via `System.out.println("point=" + p)` using `Point.toString()`).
16. No — non-static nested requires enclosing instance.
17. Four lines; third is `Hello Ada from outer-1`.

## Part C

18. `1250 USD` (same as demo’s first line for equivalent construction).
19. `IllegalStateException` with message containing `url required` (demo exact: `url required`).
20. `equals` true; `==` false (distinct objects).
21. `2`.
22. Likely `3` — broken hash contract allows distinct buckets despite `equals` true.

## Part D

23. Chain with `this(...)` to one validated constructor.
24. Outer / Activity not garbage-collected (memory leak) while inner cached.
25. No — `Set` uses `Object.equals`; overloaded `equals(Sku)` is not an override.
26. The list’s contents (and structure if reference mutable) can be mutated by callers unless defensive copy/unmodifiable view.
27. Security / privacy leak in logs.

## Part E — Coding (representative)

28.

```java
final class Rgb {
    private final int r, g, b;
    Rgb(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException("channel 0-255");
        }
        this.r = r; this.g = g; this.b = b;
    }
    @Override public String toString() { return "rgb(" + r + "," + g + "," + b + ")"; }
}
```

29.

```java
final class Circle {
    private final double radius;
    Circle(double radius) {
        if (radius < 0) throw new IllegalArgumentException("negative");
        this.radius = radius;
    }
    double area() { return Math.PI * radius * radius; }
}
```

30. Standard `instanceof` pattern + `Objects.hash(major, minor)` + `toString` like `Version[1.2]`.

31. `EmailMessage` private constructor from builder; `Builder.to(String)` fluent; `build()` throws if `to` null/blank.

## Part F

32. `final` reference can point to mutable object (e.g. `List`); need defensive copy and no mutators.
33. `getClass()`: strict same runtime class, no subclass equality. `instanceof`: allows subclass instances in equality if designed symmetrically.
34. Entity equality often keyed by database id once assigned; transient fields or lazy collections may be excluded by design (document carefully).
