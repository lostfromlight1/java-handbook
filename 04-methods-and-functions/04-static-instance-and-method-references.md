# Static, Instance, and Method References

> **Status:** Completed  
> **Module:** 04 — Methods and Functions  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (`static`/`this`, shared state, method references preview)  
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

- Call **static** methods on a type vs **instance** methods on an object.
- Explain why static methods have no `this` and instance methods do.
- Use **`this`** to refer to the current object inside instance methods and constructors.
- Reason about **static fields** shared across all instances (`Counter` pattern).
- Choose static utilities vs instance behavior on object state.
- Read and write **method references** (`Type::method`, `obj::method`) as shorthand for lambdas.
- State minimum viable definitions of **lambda** and **functional interface** before using streams.

## 2. Prerequisites

- **Classes and objects** — basic construction (`new Counter("alpha")`); Module 05 deepens OOP.
- **Methods and pass-by-value** — [03-pass-by-value-and-stack-frames.md](03-pass-by-value-and-stack-frames.md).
- **References and fields** — [Module 02 Ch.01](../02-java-fundamentals/01-variables-and-types.md).

## 3. Why This Concept Exists

Real APIs mix **type-level** operations (`Math.max`, `List.of`) with **per-object** behavior (`account.debit(5)`). Confusing the two produces compile errors (`Cannot make a static reference to the non-static method`) or subtle bugs (static counters vs instance state). Method references tidy stream-style code once you know what a lambda already expresses.

## 4. Conceptual Explanation

### 4.1 Static methods belong to the type

A **static method** is associated with the **class** (`Counter.nextId()`), not with a particular instance. Inside a static method there is **no `this`**—no current object.

Call syntax:

- **Preferred:** `TypeName.staticMethod()` — makes the class owner obvious.
- **Legal from same class:** `staticMethod()` without prefix.
- **Discouraged:** `instance.staticMethod()` — compiles (static hides on instance) but misleads readers.

Static methods are ideal for **utilities**, **factories** (preview; Module 05), and **shared bookkeeping** that does not need per-object state.

### 4.2 Instance methods need an object

An **instance method** is invoked on a **receiver** object:

```java
Counter a = new Counter("alpha");
a.bump();           // receiver is a
```

Inside an instance method, **`this`** is the reference to the receiver (the object on which the method was called). Constructors also use `this` to assign fields: `this.name = name`.

If you forget the receiver: `bump();` only works **inside** another instance method of the same class—there, `bump()` is sugar for `this.bump()`.

### 4.3 Static vs instance fields

| Kind | Belongs to | Shared? |
|------|------------|---------|
| Instance field (`ticks`) | Each object | No — each `Counter` has its own `ticks` |
| Static field (`next`, `created`) | The class | Yes — one storage for all instances |

Static fields outlive any single instance. They are initialized when the class is first loaded (JVMS class initialization).

### 4.4 When to use which

| Use static | Use instance |
|------------|--------------|
| Pure functions of inputs (`max(a,b)`) | Behavior using object fields (`describe()`) |
| Shared counters/IDs/registries | Per-user/per-order state |
| No dependency on receiver state | `this` needed |

**Trade-off:** static state is global within the class loader—harder to test in parallel and easy to overuse. Instance state keeps data near the object it describes.

### 4.5 Method references (what and why)

A **lambda expression** is a concise **anonymous function**: `(x, y) -> x + y`.

A **functional interface** is an interface with **one abstract method** (e.g. `Comparator<T>`, `Consumer<T>`). Lambdas and method references implement that single method.

A **method reference** is syntax sugar when a lambda would only **call one existing method**:

```java
// lambda                    // method reference
s -> s.toUpperCase()    →    String::toUpperCase
(x, y) -> Integer.sum(x,y) → Integer::sum
```

**Why use them:** less noise, clearer intent when the method name carries meaning (`Integer::sum`).

**What they are not:** new kinds of methods—they produce the same functional interface instances lambdas would.

### 4.6 Three common method reference forms (before streams)

| Form | Example | Meaning |
|------|---------|---------|
| Static | `Integer::sum` | `(a,b) -> Integer.sum(a,b)` |
| Instance on arbitrary type | `String::toUpperCase` | `(s) -> s.toUpperCase()` |
| Instance on specific object | `System.out::println` | `x -> System.out.println(x)` |

Module 16 expands composition, constructors (`Type::new`), and custom functional interfaces.

### 4.7 Streams (minimum viable)

A **stream** (`names.stream()`) is a **sequence of elements** with lazy operations (`map`, `sorted`) terminated by something like `forEach`. It is not a new collection—it is a pipeline over data. Module 17 teaches the Stream API fully; here it supports method-reference demos only.

## 5. Mechanism and Execution Flow

### 5.1 Static vs instance dispatch

At a call site the compiler must know the **receiver** (for instance methods) and the **declaring type** (for static methods).

| Call form | Receiver / type | `this` in callee? |
|-----------|-----------------|-------------------|
| `Counter.nextId()` | Type `Counter` | No — static frame has no receiver slot for `this` |
| `a.bump()` | Object `a` | Yes — `this` refers to `a` |
| `Integer::sum` (method reference) | No receiver until invoked | Depends on bound reference type at use site |

**Static context rule:** Inside a `static` method you cannot use `this` or instance members without an explicit object reference—there is no current instance.

### 5.2 Field access and class initialization

**Instance fields** live in each object’s storage; **static fields** live in the class’s runtime metadata area and are initialized on **class initialization** (JVMS §5.5—`<clinit>` runs once per class loader).

`Counter.created++` in a constructor updates shared static state; `ticks++` updates only the receiver’s instance field.

### 5.3 Method references (desugar mental model)

A method reference such as `Integer::sum` does not call the method immediately—it produces a **functional interface** instance whose abstract method **delegates** to `Integer.sum` when invoked. At runtime, invokedynamic/bootstrap (JDK library) wires the target; treat this as **compiler + library sugar**, not a new invocation opcode you write by hand.

The stream demo `names.stream().reduce(0, Integer::sum)` passes `Integer::sum` as the `BiFunction` argument to `reduce`—each step calls the static method on boxed/unboxed `int` values as the API defines.

## 6. Terminology

| Term | Meaning |
|------|---------|
| Static method | Method on the class; no receiver object required |
| Instance method | Method on an object; implicit `this` |
| `this` | Reference to the current object in instance code |
| Static field | One per class, shared by all instances |
| Lambda | Inline implementation of a functional interface |
| Functional interface | Interface with exactly one abstract method |
| Method reference | `::` shorthand for a lambda that delegates to a method |

## 7. Syntax and Language Rules

```java
class Demo {
    static void util() { }
    void instance() { this.instance(); }
    static void caller(Demo d) {
        d.instance();   // needs object d
        util();         // static call
    }
}
```

- **Static context** (static method or static initializer) cannot use instance members without an object reference.
- **Instance context** can call static members on the declaring type.
- Method references require a **compatible** functional interface type (often inferred).

## 8. Step-by-Step Examples

Runnable demo: [examples/core-java/static-instance-demo/](../examples/core-java/static-instance-demo/)

```bash
javac StaticInstanceDemo.java
java StaticInstanceDemo
```

### 8.1 Counter — static shared state vs instance state

```java
class Counter {
    private static int next = 1;
    private static int created = 0;
    private final String name;
    private int ticks;

    Counter(String name) {
        this.name = name;
        created++;
    }

    static int nextId() { return next++; }
    static int createdCount() { return created; }
    void bump() { ticks++; }
    String describe() { return name + " ticks=" + ticks; }
}
```

`main` calls `Counter.nextId()` twice (static), constructs `a` and `b`, calls instance `bump()` on each, then reads static `createdCount()`.

**Verified (Temurin JDK 21.0.12.1):**

```text
Counter.nextId() = 1
Counter.nextId() = 2
alpha ticks=2
beta ticks=1
Counter.createdCount() = 2
ADA
LIN
ZOE
Integer::sum reduce = 6
```

| Output | Explanation |
|--------|-------------|
| `nextId` 1 then 2 | Static `next` incremented across calls; shared class state. |
| `alpha ticks=2` | `a.bump()` twice; instance field `ticks` on `a`. |
| `beta ticks=1` | Separate instance field on `b`. |
| `createdCount = 2` | Constructor incremented static `created` twice. |
| `ADA` `LIN` `ZOE` | Stream sorted uppercase names (lexicographic order). |
| `reduce = 6` | `Integer::sum` as binary operator: 0+1+2+3. |

### 8.2 Minimal — static utility

```java
double hypot = Math.hypot(3, 4); // 5.0 — Math is all static helpers
```

### 8.3 Realistic — instance models entity behavior

```java
order.cancel();          // instance — uses order fields
OrderValidator.validate(order); // static utility in another type
```

### 8.4 Method references in the demo

```java
names.stream()
    .map(String::toUpperCase)   // bound later per element
    .sorted()
    .forEach(System.out::println);

int sum = Arrays.stream(new int[] {1, 2, 3})
    .reduce(0, Integer::sum);
```

- `String::toUpperCase` — each name becomes uppercase via instance method on the stream element.
- `System.out::println` — consumer bound to one `PrintStream` instance.
- `Integer::sum` — static method reference matches `BiFunction`-style reduce step.

Equivalent lambdas for study:

```java
.map(s -> s.toUpperCase())
.forEach(s -> System.out.println(s))
.reduce(0, (a, b) -> Integer.sum(a, b))
```

## 9. Real-World Use Cases

- **Static:** `Objects.requireNonNull`, `List.of`, JSON mappers’ `parse` factories, pure validators.
- **Instance:** domain methods (`Payment.capture()`, `User.lock()`).
- **Static fields:** ID generators, caches (use with care), constant configuration shared by all instances.
- **Method references:** stream pipelines, `Comparator.comparing(Person::getName)`, event handlers `button.setOnAction(this::handle)`.

## 10. Internal Behavior

**JLS:** Static members belong to the class; instance members belong to instances. `this` is only defined in instance contexts.

**JVMS:** Static fields live in the class’s static storage; instance fields in each object. Static methods compile to bytecode that does not take a receiver (`invokestatic`); instance methods use ` invokevirtual` with receiver in slot 0.

**HotSpot:** Class initialization runs once per class loader when the class is first initialized—static fields get default then initializer values.

## 11. Edge Cases and Limitations

- **Calling instance from static:** Need an object: `new Counter("x").bump()` or pass one in.
- **Hidden static via instance:** `a.nextId()` if `nextId` is static—avoid; use `Counter.nextId()`.
- **Static mutable state:** Thread-safety is your responsibility (`next++` is not atomic for concurrent use—Module 44).
- **Method reference arity:** Must match the functional interface (`Integer::sum` needs two `int` arguments in reduce).

## 12. Common Mistakes

### Incorrect — instance call without object

```java
// bump();  // error inside static main
```

### Corrected

```java
Counter c = new Counter("x");
c.bump();
```

### Incorrect — treating static field as per-instance

```java
// expecting each Counter to have its own created count field
```

### Corrected

Use instance field for per-object tally; static only when sharing is intended.

### Incorrect — method reference with wrong type

```java
// List::add needs a list instance, not List::add as UnaryOperator
```

## 13. Best Practices

- Prefer `Type.staticMethod()` at call sites.
- Minimize mutable static fields; document thread expectations.
- Use instance methods when behavior depends on object state.
- Use method references when they read clearer than a one-line lambda.

## 14. Design Trade-offs

| Static utility class | Instance method on type |
|----------------------|-------------------------|
| No state, easy to test | Cohesive with fields |
| Can become a “god” static bag | Requires object lifecycle |

| Lambda | Method reference |
|--------|-------------------|
| Flexible body | Only single delegation |
| More characters | Often clearer name |

## 15. Performance Considerations

Static vs instance call has negligible difference in HotSpot after JIT. Streams add overhead versus hand loops—clarity vs micro-performance (Module 48). Static counters with contention need atomic or synchronized alternatives.

## 16. Security Considerations

Mutable static state can become a covert channel or global tamper point in multi-tenant apps; prefer scoped beans (Module 42) over static singletons for request data.

## 17. Java Version and Compatibility Notes

Static/instance semantics unchanged in Java 21. Method references since Java 8; validated on **21.0.12.1**.

## 18. Related Language Comparisons

Kotlin **`companion object`** groups static-like members. C# **`static`** classes cannot be instantiated—similar intent to Java static-only utility classes (often `final` + private ctor pattern).

## 19. Revision and Mastery

**Gaps to close:**

- Assuming every method needs `static` because `main` is static.
- Forgetting `created++` is static, `ticks++` is per instance.
- Reading `String::toUpperCase` without knowing it’s `(String s) -> s.toUpperCase()`.

**Questions you should answer:**

1. Why does `Counter.nextId()` not need a `Counter` instance?
2. What does `this.name = name` disambiguate in the constructor?
3. Rewrite `System.out::println` as a lambda.

**Drill:** Add `Counter.resetIds()` as static and discuss impact on existing instances.

## 20. Exercises

### Beginner

1. Run `StaticInstanceDemo`; explain `alpha` vs `beta` tick counts.
2. Add an instance method `reset()` that sets `ticks` to 0; call on `a` only.

### Intermediate

3. Replace stream section with an equivalent `for` loop; compare method references in comments only.
4. Write a static `parse(String)` factory returning `Optional<Counter>` with name validation.

### Advanced

5. Implement a **non-static** inner usage: `this` forwarding to another instance method.
6. List which bytecode invoke kinds (`invokestatic`, `invokevirtual`) match static vs instance calls (`javap -c`).

## 21. Interview Questions

1. **Difference static vs instance?** — Receiver required? `this`? Storage on class vs object?
2. **Can static method access instance fields?** — Only through an object reference.
3. **What is `String::length`?** — Method reference equivalent to `s -> s.length()`.
4. **Are static fields inherited?** — Hidden/accessible by name through subclass; still one shared field if not shadowed.

## 22. Solutions

1. `a` bumped twice → 2; `b` once → 1.
2. `void reset() { ticks = 0; }` — affects only that instance’s `ticks`.
3. For loop builds `List`, sort, print uppercase manually.
4. `parse` returns `empty` or `new Counter(name)`; static factory pattern preview.
5. `void chain() { this.bump(); }` illustrates receiver.
6. `invokestatic` for `nextId`; `invokevirtual` for `bump`.

## 23. Chapter Summary

**Static** methods and fields belong to the type and share one class-wide storage. **Instance** methods use **`this`** and per-object fields. **Method references** shorten lambdas that delegate to existing methods—three shapes appear in streams and APIs. Use static for type-level utilities; instance for object behavior.

## 24. Key Terminology

Static · instance · `this` · static field · lambda · functional interface · method reference · stream (preview)

## 25. References and Further Reading

- [JLS — Class Members](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- [JLS — Method References](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.13.3)
- `java.util.function` package (Java SE API)
- [Module 16](../16-lambdas-and-functional-programming/) — lambdas and method references in depth
- [Module 17](../17-streams-and-collectors/) — Stream API

## 26. Next Steps

- Module 04 Ch.01–02 — signatures, overloading, varargs, recursion
- [03-pass-by-value-and-stack-frames.md](03-pass-by-value-and-stack-frames.md) — how `this` and parameters live in frames
- Module 05 — classes, encapsulation, constructors

## Review Checklist

**Mandatory Depth Review:** Technical profile; static/instance/method references at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (dispatch table §5.1)
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 for StaticInstanceDemo)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-10; Temurin re-verify)

**Omissions (section → reason):**

- **§ Language comparisons** — omitted; not needed for Java-first readers here.
- **§ Performance Considerations** — retained brief in §15; no distinct static-dispatch tuning story.

**Verification note:** StaticInstanceDemo output matches Temurin JDK 21.0.12.1 re-run 2026-11-10 (ids 1–2, ticks 2/1, `Integer::sum reduce = 6`).
