# Nested and Inner Classes

> **Status:** Completed  
> **Module:** 06 — Classes and Object Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (four nesting forms, enclosing instances, captures)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. Do **not** manufacture content to fill headings. For omitted sections, record the reason in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow *(or merge note)* | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs *(strongly encouraged for Technical / Deep-dive)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Distinguish **static nested**, **inner (non-static nested)**, **local**, and **anonymous** classes.
- Instantiate each kind with correct syntax (`Outer.Inner`, `outer.new Inner()`, local class in a method, anonymous class or lambda where appropriate).
- Explain how an **inner class** holds an implicit reference to the **enclosing instance** and what that means for memory and API design.
- Choose nested types for **cohesion** (helper tied to one outer type) vs top-level types for reuse.
- Read framework code that uses **anonymous** implementations of single-method interfaces (and relate them to lambdas previewed in Module 04).

## 2. Prerequisites

- **Module 05 Ch.01** — classes, objects, references.
- **Module 04 Ch.04** — **`static`** vs instance members.
- **Module 06 Ch.01** — constructors and `this` (outer instance supplied implicitly to inner classes).

Brief refresher: a **top-level class** is declared directly in a `.java` file (one public top-level class per file, name matches file). A **nested class** is a class declared inside another class or interface body. Nesting keeps related helpers private to the outer type’s implementation.

## 3. Why This Concept Exists

Some types only make sense **in the context of another type**: an iterator inside a collection, a builder inside an immutable product, a one-off event handler in UI code. Putting them at package level pollutes the namespace and weakens encapsulation.

Java offers several nesting forms with different **ties to the outer object**:

| Need | Typical nesting |
|------|-----------------|
| Helper that does **not** need outer instance state | **static nested** class |
| View tied to **one outer object’s fields** | **inner** class |
| One-time algorithm inside a method | **local** class |
| Quick implementation of an interface | **anonymous** class (or lambda on **functional interfaces**) |

Understanding the differences prevents memory leaks (inner class holding outer), serialization surprises, and incorrect `new` syntax in interviews and code reviews.

## 4. Conceptual Explanation

### 4.1 Static nested class

A **static nested class** is a `static` member class. It behaves like a normal class nested for naming only: it **does not** have an implicit reference to an outer instance.

Instantiation:

```java
Outer.StaticNested sn = new Outer.StaticNested("tag");
```

No `Outer` instance required. It can access **static** members of the outer class (per access rules) but not instance fields of an outer object unless passed explicitly.

### 4.2 Inner class (non-static nested)

An **inner class** is a nested class **without** `static`. Each inner instance is associated with an **enclosing instance** of the outer class:

```java
Outer outer = new Outer("outer-1");
Outer.Inner inner = outer.new Inner();
```

Inside `Inner`, code can read `name` from the outer object directly—the compiler attaches a synthetic field (often named like `this$0`) referencing the outer instance.

### 4.3 Local class

A **local class** is declared **inside a method body** (or initializer). It is visible only in that block—like a local variable. It can access **effectively final** local variables and parameters from the enclosing method (Module 09 deepens closures).

### 4.4 Anonymous class

An **anonymous class** is an unnamed class declaration combined with `new`:

```java
return new Task() {
    @Override
    public String run() {
        return "anon from " + name;
    }
};
```

It declares a new class inline and instantiates it in one expression. For **functional interfaces** (one abstract method), a **lambda** or **method reference** often replaces anonymous classes today—but anonymous classes still support multiple methods, state fields, and overriding default methods in place.

### 4.5 Comparison table

| Kind | `static`? | Outer instance needed? | Typical use |
|------|-----------|------------------------|-------------|
| Static nested | Yes | No | Nested namespace, helper types |
| Inner | No | Yes | Callbacks reading outer state |
| Local | No (not a class member) | For non-static locals: enclosing instance if method is instance method | One-off helpers in one method |
| Anonymous | No | Same as inner if created in instance context | Single-use interface implementation |

## 5. Mechanism and Execution Flow

Each nesting form changes **how** `new` binds instances to outer state and **what** the compiler synthesizes.

### 5.1 Static nested class — plain `new` on a member type

`new Outer.StaticNested("tag")` allocates only the nested object. No hidden outer reference is passed—bytecode resembles a top-level class nested for naming (`javap` shows no `this$0`). The nested type can access **static** members of `Outer` per visibility rules; instance fields of an `Outer` require an explicit reference parameter.

### 5.2 Inner and anonymous classes — enclosing instance wiring

For non-static nested types, **`outer.new Inner()`** (or `new Outer.Inner(outer) { }` in anonymous form) passes the **enclosing instance** into a synthetic constructor parameter (often visible as `this$0` in `javap -private`). Inside the nested body, reads of `name` on the outer become field accesses through that stored reference.

**Lifetime consequence:** an inner instance kept in a long-lived cache holds its outer alive—§8.3. Prefer **static nested** plus explicit data when no outer callback is needed.

### 5.3 Local classes and captures

A **local class** inside a method is instantiated with the same enclosing-instance rules as a member inner class when the method is an instance method. Additionally, locals used from the nested body must be **effectively final** (JLS); the compiler may emit synthetic fields or accessors—preview of **closure lowering** (Module 09 deepens capture rules).

### 5.4 Anonymous class vs lambda (preview for Module 09)

For a **functional interface** (one abstract method), both patterns produce a callable implementation:

| Mechanism | What gets generated | When to prefer |
|-----------|---------------------|----------------|
| **Anonymous class** | New class + `new` + instance | Multiple methods, extra fields, overriding defaults on the spot |
| **Lambda** | `invokedynamic` bootstrap → often no separate `.class` per site | Simple one-method bodies, composition with streams |

Lambdas capture **effectively final** locals like local classes; anonymous classes can declare instance state and additional methods. Module 09 compares bytecode, serialization, and debugging trade-offs in depth—here, choose anonymous when the type is not a single abstract method interface or when you need a mini-class, not a single expression.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Nested class** | Any class declared inside another class or interface |
| **Static nested class** | Nested class declared `static`; no implicit outer `this` |
| **Inner class** | Non-static nested class; holds reference to enclosing instance |
| **Local class** | Class declared inside a method or block |
| **Anonymous class** | Class with no name, created at a `new InterfaceOrClass() { }` site |
| **Enclosing instance** | The outer object an inner or anonymous instance is tied to |

## 7. Syntax and Language Rules

Static nested:

```java
class Outer {
    static class Helper { }
}
// new Outer.Helper()
```

Inner:

```java
class Outer {
    class Helper { }
}
// outer.new Helper()  — outer must be an instance reference
```

Local (inside method):

```java
void demo() {
    class Greeter { String hi() { return "Hi"; } }
    new Greeter().hi();
}
```

Anonymous:

```java
Runnable r = new Runnable() {
    @Override public void run() { }
};
```

Access rules: nested types can be `private` to hide implementation from the rest of the package—strong encapsulation.

## 8. Step-by-Step Examples

### 8.1 Minimal static nested

```java
class Set {
    static class Entry {
        // does not see each Set instance's fields unless passed in
    }
}
```

### 8.2 Module demo: all four forms (**Verified**)

**Source:** [`examples/core-java/nested-classes-demo/NestedClassesDemo.java`](../examples/core-java/nested-classes-demo/NestedClassesDemo.java)

```bash
cd examples/core-java/nested-classes-demo
javac NestedClassesDemo.java
java NestedClassesDemo
```

**Verified (Temurin JDK 21.0.12.1):**

```text
StaticNested:static-nested
Inner sees outer-1
Hello Ada from outer-1
anon from outer-1
```

Line by line:

1. **Static nested** — `new Outer.StaticNested(...)` without an `Outer` instance; `label()` returns stored tag.
2. **Inner** — `outer.new Inner()` binds to `outer`; `label()` reads `name` → `Outer-1`’s field.
3. **Local** — `Greeter` inside `localGreeting` closes over `who` and outer `name`.
4. **Anonymous** — `Task` implementation captures outer `name` for `run()`.

Syntax highlight for inner instantiation:

```java
Outer outer = new Outer("outer-1");
Outer.Inner inner = outer.new Inner();
```

The `outer.new` qualifier is required; writing `new Outer.Inner()` without an outer reference is a compile error for non-static nested classes.

### 8.3 Advanced: when to prefer static nested

If `Helper` does not need outer instance fields, make it **`static`**. An inner class that outlives the outer (stored in a cache, registered with a long-lived listener) can **prevent garbage collection** of the outer object—a classic leak in older Android and event-handler code. Fix: static nested + pass only needed data, or weak references (specialized topic).

## 9. Real-World Use Cases

| Domain | Pattern |
|--------|---------|
| **Collections** | `Map.Entry` (interface nested type; implementations often static or private inner) |
| **Builders** | `HttpRequest.Builder` as static nested (Ch.03) |
| **Swing / JavaFX** | Anonymous listeners (legacy UI); lambdas today |
| **Streams / functional code** | Lambdas instead of anonymous classes where interface has one abstract method |
| **Tests** | Private static nested fakes mimicking collaborators |

Backend services rarely expose nested types in public REST models, but they appear inside framework adapters, JDBC row mappers, and gRPC stubs.

## 10. Internal Behavior

The compiler generates extra constructors and synthetic fields for inner and anonymous classes:

- Non-static nested classes receive the **enclosing instance** as a constructor parameter (hidden).
- **Local and anonymous** classes may produce synthetic accessor methods for captured locals.

**JLS** defines semantics; **javac** output can be inspected with `javap -c -private` on lesson examples (implementation detail).

## 11. Edge Cases and Limitations

- **Inner class in static method:** cannot instantiate non-static nested class without an outer instance—usually a design smell.
- **`this` inside inner class:** inner’s `this`, not outer’s; use `Outer.this.field` to qualify outer (when needed).
- **Same class name** at multiple nesting levels: confusing; avoid.
- **Serializable inner classes:** serialization captures outer—surprising graph shape.
- **Generics on nested types:** valid; raw types and wildcards follow normal rules (Module 15).

## 12. Common Mistakes

### Incorrect

```java
Outer.Inner i = new Outer.Inner(); // compile error
```

### Corrected

```java
Outer o = new Outer("x");
Outer.Inner i = o.new Inner();
```

### Incorrect: non-static nested when static would do

```java
class Outer {
    class HeavyHelper { /* no outer fields used */ }
}
// Every HeavyHelper carries an unnecessary outer reference
```

### Corrected

```java
class Outer {
    static class HeavyHelper { }
}
```

## 13. Best Practices

- Default to **static nested** unless you **need** outer instance fields.
- Keep nested types **`private`** when they are implementation details.
- For single-method interfaces, prefer **lambda** (Java 8+) when readability improves.
- Avoid deep nesting towers—extract top-level package-private types if files grow unwieldy.

## 14. Design Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| Inner class | Direct access to outer state | Lifetime coupling; extra memory |
| Static nested | No outer coupling | Must pass context explicitly |
| Anonymous | Local, one-off | Hard to name in stack traces; bulky |
| Top-level type | Reusable, testable | Wider visibility |

## 15. Performance Considerations

Each inner instance carries an extra reference. For millions of small helpers, prefer static nested. Anonymous classes generate separate `.class` files (`Outer$1.class`, etc.)—class loading cost is usually negligible unless in extreme hot loops.

## 16. Security Considerations

**Private nested classes** prevent other packages from subclassing or constructing implementation details—useful for security-sensitive parsers that validate input in one place.

Do not expose inner instances that indirectly expose mutable outer state to untrusted code without defensive copying.

## 17. Java Version and Compatibility Notes

- Nested classes since Java 1.1; lambdas (Java 8+) often replace anonymous classes for functional interfaces.
- **Records** cannot be declared locally in some versions—check current JLS for record local class rules if mixing features.

## 18. Related Language Comparisons

Java does not have true “inner functions” as first-class citizens; local and anonymous classes approximate closure behavior with **effectively final** capture. Kotlin’s `inner class` is similar; C# has nested types with less ceremony. Java’s `outer.new Inner()` syntax is distinctive.

## 19. Revision and Mastery

- **Gaps:** treating `Map.Entry` as an inner class of every map instance (it’s an interface nested **type**; btree nodes differ by implementation).
- **Interview favorite:** difference between static nested and inner class; why `outer.new`?
- **Advanced:** how compilers desugar anonymous classes vs invokedynamic for lambdas on HotSpot.

## 20. Exercises

### Beginner

1. Change demo `StaticNested` to require an `int id` in its constructor; instantiate without `Outer`.
2. From `main`, can you call `outer.localGreeting` without creating `Outer`? Why?

### Intermediate

3. Write `static class Pair` inside `Outer` holding two strings; no outer access.
4. Rewrite `anonymousTask` using a lambda—does it still capture `name`?

### Advanced

5. Sketch why holding `Inner` in a static cache can leak `Outer`.

## 21. Interview Questions

1. Static nested vs inner class?
2. Syntax to create an inner class instance?
3. What is an anonymous class?
4. When would you use a local class?
5. Lambdas vs anonymous classes—when still need anonymous?

## 22. Solutions

1. `new Outer.StaticNested("t", 42)` pattern with your constructor.
2. No—`localGreeting` is an instance method; need `new Outer(...)`.
3. `Outer.Pair p = new Outer.Pair("a","b");` (static nested).
4. Yes if `Task` is a functional interface: `return () -> "anon from " + name;`
5. Long-lived `Inner` references reachable from static field keep outer reachable; clearing cache or static nested fixes.

## 23. Chapter Summary

Java nests classes to group cohesive helpers. **Static nested** types stand alone; **inner** types read outer state through a hidden link. **Local** and **anonymous** classes support method-scoped and one-shot implementations. Choose nesting to protect invariants and names—not to avoid simple top-level types when reuse matters.

## 24. Key Terminology

Nested class · static nested class · inner class · local class · anonymous class · enclosing instance · capture

## 25. References and Further Reading

- [JLS §8.1.3 — Inner Classes and Enclosing Instances](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.3)
- [JLS §14.3 — Local Class Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.3)
- [JLS §15.9.5 — Anonymous Class Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.9.5)
- Java tutorials — nested classes (Oracle)

## 26. Next Steps

- [03-immutability-and-builders.md](03-immutability-and-builders.md) — static nested `Builder`.
- Module 09 — lambdas and effectively final locals.
- Module 16 — functional interfaces in depth.

## Review Checklist

**Mandatory Depth Review:** Technical profile; nested type mechanics at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (comparison table §4.5)
- [x] First-appearance concepts explained now; lambda vs anonymous contrast for M09 (§5.4)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 for NestedClassesDemo)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **§14 Performance Considerations** — omitted; synthetic accessor overhead is implementation-specific.
- **§15 Security Considerations** — omitted; serialization gadget chains with inner classes belong in advanced security material.

**Verification note:** NestedClassesDemo four-line output matches Temurin JDK 21.0.12.1 (2026-11-11).
