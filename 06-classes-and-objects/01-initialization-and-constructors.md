# Initialization and Constructors

> **Status:** Completed  
> **Module:** 06 — Classes and Object Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (constructors, initialization order, validation)  
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

- Explain what a **constructor** does and how it differs from ordinary methods.
- Write **overloaded constructors** and chain them with **`this(...)`** so validation stays in one place.
- Describe **initialization order**: static fields and static blocks (once per class), then instance fields, instance initializer blocks, and constructors (per object).
- Use **`this`** to refer to the object under construction or the current receiver.
- Validate constructor arguments and reject illegal state with **`IllegalArgumentException`**.
- Relate constructor design to **invariants** introduced in Module 05 encapsulation.

## 2. Prerequisites

- **Module 05 Ch.01** — classes, objects, `new`, fields, instance methods, basic constructor syntax.
- **Module 05 Ch.02** — `private` fields, invariants, `IllegalArgumentException`.
- **Module 04 Ch.02** — method **overloading** (same name, different parameter lists).
- **Module 04 Ch.04** — **`static`** members vs instance members (static fields belong to the class, not to each object).

Brief refresher: when you write `new User("ada")`, the JVM allocates storage for a new `User` object, then runs the matching **constructor** before your code can use the reference. If no constructor is written and none is inherited in a way that blocks it, the compiler may supply a **default no-argument constructor**—but only while you have not declared any other constructors.

## 3. Why This Concept Exists

Objects must start in a **valid** state. Fields cannot always have sensible defaults (a `username` cannot be empty; a `Point` needs coordinates). A constructor is the dedicated mechanism that runs **once per new object** to assign fields, check rules, and optionally register the object (for example incrementing a creation counter).

Without constructors, every caller would duplicate setup logic—violating encapsulation and making bugs likely when a new field is added. Without a clear **initialization order**, static counters and `final` fields would be hard to reason about, especially when subclasses and `super()` chaining appear in later modules.

## 4. Conceptual Explanation

### 4.1 What a constructor is

A **constructor** is a special member that:

| Property | Rule |
|----------|------|
| Name | Same as the class (including for inner classes) |
| Return type | None—not even `void` |
| Purpose | Initialize a new instance before it is used |
| Invocation | Only via `new ClassName(...)` (or reflection / serialization APIs later) |

Constructors are not “methods you call from `main`.” They are invoked by the runtime as part of object creation.

### 4.2 Default constructor

If a class declares **no** constructors, the compiler generates a **public no-argument constructor** that takes no action beyond what field initializers and superclass construction require.

The moment you declare **any** constructor (for example `User(String username)`), the compiler **stops** generating the default. Callers must then use one of **your** constructors.

### 4.3 Overloading constructors

**Overloading** applies to constructors the same way as to methods: multiple constructors in one class, same class name, **different parameter lists** (number or types of parameters). Typical pattern: a convenient short form delegates to a full form:

```java
User(String username) {
    this(username, username); // chain to two-argument constructor
}
```

### 4.4 Constructor chaining with `this(...)`

**`this(...)`** in a constructor body must be the **first statement** (when present). It calls **another constructor in the same class**, passing arguments. That second constructor may itself chain, but eventually one constructor must assign fields directly.

Benefits:

- **One place** for validation (`username` non-blank, null display name defaulting).
- Fewer duplicated lines when adding a third constructor later.

You cannot use both `this(...)` and `super(...)` as the first statement in the same constructor; one chains within the class, the other chains to a superclass constructor (Module 07).

### 4.5 `this` for fields vs parameters

Inside an instance constructor or method, **`this`** is a reference to the **current object**. When a parameter has the same name as a field (`username`), write `this.username = username` so the field on the left is unambiguous.

### 4.6 Static vs instance initialization

| Kind | Runs when | Runs how often |
|------|-----------|----------------|
| Static field initializers and **`static { }` blocks** | First time the class is **used** (JLS “initialization” of the class) | Once per class loader |
| Instance field initializers and **`{ }` instance blocks** | Before the constructor body for **each** `new` | Once per object |
| Constructor body | After instance initializers, as part of the same `new` | Once per object |

Order for one object (simplified, no subclass):

1. If needed, superclass constructor (not shown in this chapter’s demos).
2. Instance field initializers and instance initializer blocks (source order).
3. Constructor body.

For static members, all static initializers run **before** any instance creation of that class.

### 4.7 `final` fields and constructors

A **`final` instance field** must be **definitely assigned** before the constructor completes—usually in the constructor body (or field initializer). After construction, reassignment is forbidden. That supports **immutable** pieces of state (preview; full immutability in Ch.03).

## 5. Mechanism and Execution Flow

Object creation is a **sequence** of language-defined steps (JLS §12.4–§12.5), not a single jump into the constructor body. The mental model below covers this chapter’s scope; subclass **`super(...)`** chaining extends the same pipeline (Module 07).

### 5.1 Phases from `new User(...)` to a usable reference

When `main` executes `new User("ada")` (no superclass in the demo):

1. **Class initialization (once per class loader)** — if `User` has not been initialized, static field initializers and `static { }` blocks run in source order (JLS §12.4). Demo: `created = 0` before any instance exists.
2. **Allocation** — the JVM reserves storage for one `User` instance (typically on the heap; **JVMS** defines semantics, **HotSpot** chooses arenas and fast paths).
3. **Instance initializers** — instance field initializers and bare `{ }` blocks run in source order, **before** the constructor body for that `new`.
4. **Constructor entry** — the matching constructor runs. If the first statement is `this(...)`, the JVM **delegates** to another constructor in the **same** class; that callee must finish (or throw) before the caller’s remaining statements run.
5. **Completion** — when the constructor chain finishes without throwing, the reference from `new` is valid for ordinary use.

If step 4 throws (for example `IllegalArgumentException` on blank `username`), the object is not left in a callable state—callers must not use the reference.

### 5.2 `this(...)` as explicit delegation

`this(username, username)` is not a method call. It is **constructor delegation**: control transfers to the two-argument constructor, which performs validation and field assignment. Rules:

- At most one of `this(...)` or `super(...)` as the **first** statement in a constructor (they cannot both appear).
- Delegation must end in a constructor that assigns fields (no infinite `this` chain).

Module 07 adds **`super(...)`**, which delegates to a **superclass** constructor before the subclass body continues—the same “first statement” rule applies, and initialization order becomes: superclass constructors and fields, then subclass instance initializers and constructors.

### 5.3 Static vs instance work (per object)

| Step | Scope | Frequency |
|------|-------|-----------|
| Static initializers | Entire class | Once (per loader) |
| Instance initializers + constructors | One object | Every `new` |

The static counter `created` in the demo illustrates class-scoped state updated inside an instance constructor—legal, but shared mutable statics complicate tests (§14).

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Constructor** | Member that initializes a new instance; name matches the class; no return type |
| **Default constructor** | Compiler-generated no-arg constructor when you declare none |
| **Constructor chaining** | One constructor calling another via `this(...)` |
| **Static initializer** | `static { ... }` block run once when the class is initialized |
| **Instance initializer** | `{ ... }` block at class body level, run before each constructor |
| **Definite assignment** | Language rule: variables (including `final` fields) must be assigned before use |

## 7. Syntax and Language Rules

```java
class Example {
    private static int count;           // static field
    private final String id;            // final instance field

    static {
        count = 0;                      // static initializer
    }

  {                                   // instance initializer (uncommon but legal)
        // runs before each constructor
    }

    Example() {
        this("default");                // must be first statement if present
    }

    Example(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id required");
        }
        this.id = id;
        count++;
    }
}
```

Rules to remember:

- `this(...)` or `super(...)`—if used—must be the **first** statement in that constructor.
- Constructors cannot be `static`, `final`, `abstract`, or `synchronized`.
- Access modifiers on constructors control who may call `new` (package-private constructor → factory in same package).

## 8. Step-by-Step Examples

### 8.1 Minimal constructor

```java
class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

`final` coordinates are set once; no setters needed for a simple value holder.

### 8.2 Realistic: chaining, validation, static counter (**Verified**)

The module demo combines overload, `this(...)`, a static block, and `toString`:

**Source:** [`examples/core-java/constructors-demo/ConstructorsDemo.java`](../examples/core-java/constructors-demo/ConstructorsDemo.java)

```bash
cd examples/core-java/constructors-demo
javac ConstructorsDemo.java
java ConstructorsDemo
```

**Verified (Temurin JDK 21.0.12.1):**

```text
ada/ada
lin/Lin
created=2
point=(3,4)
```

Reading the output:

- `new User("ada")` uses the one-argument constructor, which chains to the two-argument form with the same string for display name → `ada/ada`.
- `new User("lin", "Lin")` sets distinct username and display name → `lin/Lin`.
- `created=2` shows the **static** counter incremented twice—shared across all `User` instances.
- `point=(3,4)` is from `Point`’s `toString()` (Ch.04 deepens `toString` contracts).

Important lines in `User`:

```java
User(String username) {
    this(username, username); // constructor chaining
}
```

```java
static {
    created = 0;
}
```

The explicit `static` block is optional here (field could be `private static int created = 0;`) but illustrates **class-wide** setup running once.

### 8.3 Advanced: trace tied to §5

Walk through §5.1 with `new User("ada")`: class init runs once; allocation; one-arg constructor immediately delegates via `this("ada", "ada")`; the two-arg constructor validates, assigns `final` fields, increments `created`; control returns to `main`. For failure paths, see §5.1 step 4—a thrown validation aborts before `created` increments.

## 9. Real-World Use Cases

| Scenario | Constructor role |
|----------|------------------|
| **JPA / Hibernate entities** | No-arg constructor sometimes required by frameworks; often `protected` with richer constructors for app code |
| **DTO / record-style types** | Constructor or builder captures all required fields up front |
| **Value objects** | Validate in constructor so invalid money, email, or SKU never exists |
| **Singleton / controlled creation** | Private constructor + static factory method limits who can instantiate |
| **Dependency injection** | Framework chooses constructor with injectable dependencies (Module 40+) |

Backend services often construct domain objects only after parsing HTTP input; constructor validation is the first line of defense before business logic runs.

## 10. Internal Behavior

**Language specification:** instance creation is defined in JLS §12 (creation of new class instances). Class initialization (static members) is JLS §12.4.

**HotSpot (typical, not a language guarantee):** allocation commonly uses a fast path on the Eden heap; `<init>` bytecode runs the chained constructors and initializers on that thread’s stack. Failed construction may leave allocated memory for GC if an exception escapes—callers must not assume a partially constructed object is usable.

Label claims: **JLS** defines order and rules; **HotSpot** implements performance details.

## 11. Edge Cases and Limitations

- **Recursive `this(...)`** without a terminating constructor → compile-time error (infinite chain).
- **Calling an instance method from a constructor** before `final` fields are set: legal but can expose `this` to other code prematurely—avoid publishing `this` from constructors in concurrent code (Module 23).
- **Static initializers throwing:** fails class initialization; subsequent use of the class throws `ExceptionInInitializerError`.
- **Enum types:** constructors are `private`; constants are created by the enum machinery (Module 12).
- **Records (Java 16+):** canonical constructor generated from components; compact constructors add validation in a special form (later module on modern syntax).

## 12. Common Mistakes

### Incorrect: duplicated validation in every constructor

```java
User(String username) {
    if (username == null || username.isBlank()) throw ...
    this.username = username;
}
User(String username, String displayName) {
    if (username == null || username.isBlank()) throw ...
    // duplicated rules
}
```

### Corrected: chain to one validated constructor

```java
User(String username) {
    this(username, username);
}
User(String username, String displayName) {
    // single validation block
}
```

### Incorrect: expecting a default no-arg constructor after adding one

```java
class Widget {
    Widget(String id) { ... }
}
// Widget w = new Widget(); // compile error — no no-arg constructor
```

### Corrected: add explicit no-arg constructor if needed

```java
Widget() {
    this("unknown");
}
```

## 13. Best Practices

- Keep **validation and invariant enforcement** in the constructor (or the constructor it chains to).
- Prefer **constructor chaining** over copy-paste validation.
- Use **`final`** for fields that must not change after construction when modeling immutable data.
- Throw **`IllegalArgumentException`** for bad caller input; reserve other exception types for impossible internal states.
- Document which constructor is the **canonical** one if several exist.

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Many overloaded constructors | Convenient call sites | Proliferation; harder to extend |
| Single constructor + builder (Ch.03) | Flexible optional fields | More types and boilerplate |
| Static counter in domain object | Simple telemetry | Global mutable static state—test isolation issues |
| Telescoping constructors | Progressive API | Becomes unwieldy beyond 3–4 parameters → builder |

## 15. Performance Considerations

Constructor work runs on the **critical path** of every `new`. Heavy I/O or network calls inside constructors make tests slow and hide failures. Prefer cheap validation in constructors; defer expensive work to explicit `init()` or factory methods when appropriate.

Object allocation cost is usually small compared to business logic; clarity and invariants matter more than micro-optimizing constructor bodies.

## 16. Security Considerations

Constructors that accept **strings from users** must validate length, charset, and format to prevent injection into logs, SQL, or HTML later. Never trust that “only our code” calls `new`—frameworks and reflection can too.

A **private constructor** prevents arbitrary instantiation, supporting sealed invariants for security-sensitive types (secret handles, capability tokens).

## 17. Java Version and Compatibility Notes

- Constructor rules are stable since Java 1.0.
- **Pattern matching for `instanceof`** (Java 16+) appears in `equals` implementations (Ch.04), not constructors.
- **Records** and **compact constructors** (Java 16+) are syntactic sugar over canonical constructors with validation hooks.

## 18. Related Language Comparisons

C++ allows initializer lists; Python uses `__init__` without `new` syntax; Go has no classes—struct literals fill a similar “create with values” role. Java’s explicit `new` + constructor chain is verbose but makes allocation and initialization phases visible.

## 19. Revision and Mastery

For experienced developers:

- **Gaps:** confusing `this` field access with `this()` chaining; forgetting that **any** declared constructor removes the default; ignoring static init order when tests fail mysteriously.
- **Misunderstood:** believing constructors “return” the object (`new` yields the reference; the constructor does not return).
- **Advanced:** subclass constructor must call `super(...)` first unless chained via `this`; how `final` field assignment interacts with deserialization (Module 31).
- **You should answer:** In what order do static init, instance init, and constructors run? When must `this(...)` be first?

## 20. Exercises

### Beginner

1. Add a `User` constructor that takes only `displayName` and uses `"guest"` as username (chain appropriately).
2. Predict `created` after three `new User(...)` calls.

### Intermediate

3. Write `Email` with `private final String value`; constructor rejects null or missing `@`.
4. Add an instance initializer block that logs `"init"` and predict log order with a constructor that prints `"ctor"`.

### Advanced

5. Explain why a static initializer is the wrong place to read environment-specific config that tests override per run.

## 21. Interview Questions

1. Difference between a constructor and a method?
2. When does the default constructor disappear?
3. What is `this(...)` and where must it appear?
4. Static vs instance initializers—when do they run?
5. Can a constructor return a value?

## 22. Solutions

1. `User(String displayName) { this("guest", displayName); }` (or swap parameter semantics if username is primary).
2. `created` is 3 if all three constructions succeed through the counting constructor.
3. Normalize with `trim().toLowerCase()` if desired; throw on invalid format.
4. Output order: `init` then `ctor` for each `new`.
5. Static init runs once per class; per-test env belongs in instance setup or test hooks, not static blocks that cache the first environment seen.

## 23. Chapter Summary

Constructors establish **valid initial state** for every object. **Overloading** and **`this(...)` chaining** keep validation centralized. **Static** initialization configures class-wide data once; **instance** initialization and constructors run for each `new`. Combined with **`final`** fields and exceptions for bad input, constructor design is the foundation of reliable domain models—before equality, collections, and immutability patterns in the rest of Module 06.

## 24. Key Terminology

Constructor · default constructor · overloading · `this(...)` · `this` · static initializer · instance initializer · definite assignment · invariant

## 25. References and Further Reading

- [JLS §8.8 — Constructor Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.8)
- [JLS §12.5 — Creation of New Class Instances](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.5)
- [JLS §12.4 — Initialization of Classes and Interfaces](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.4)
- Effective Java (Bloch) — items on constructors and telescoping constructors

## 26. Next Steps

- [02-nested-and-inner-classes.md](02-nested-and-inner-classes.md) — types declared inside types; captures outer state.
- [03-immutability-and-builders.md](03-immutability-and-builders.md) — when constructors multiply into builders.
- Module 07 — `super()` and subclass construction order.

## Review Checklist

**Mandatory Depth Review:** Technical profile; constructor and initialization at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (init-order table §4.6, §5)
- [x] First-appearance concepts explained now (not deferred away); `super()` pipeline named for M07
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 for ConstructorsDemo)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **§15 Performance Considerations** — omitted; allocation micro-benchmarks are HotSpot-specific and distract from language rules.
- **§16 Security Considerations** — omitted; constructor hardening for deserialization and gadgets belongs in later security/Java IO modules.

**Verification note:** ConstructorsDemo output `ada/ada`, `lin/Lin`, `created=2`, `point=(3,4)` matches Temurin JDK 21.0.12.1 (2026-11-11).
