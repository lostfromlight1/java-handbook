# Composition versus Inheritance

> **Status:** Completed  
> **Module:** 09 — Encapsulation and Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (composition, inheritance, delegation, fragile base class)  
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

- Contrast **composition** (has-a) with **inheritance** (is-a) for reusing behavior.
- **Delegate** to composed objects instead of subclassing for implementation reuse.
- Explain why **subclassing concrete classes** is risky (fragile base class).
- Relate composition to **LSP** (Module 07): substitutability for true subtypes only.
- Wire cross-cutting behavior (`logging`, `auditing`) via **composed** `Logger` rather than deep inheritance trees.
- Run and explain [`CompositionVsInheritanceDemo`](../../examples/core-java/composition-vs-inheritance-demo/) output.

## 2. Prerequisites

- **Module 05 Ch.03–04** — inheritance intro; composition intro.
- **Module 07** — overriding, abstract classes, **LSP**.
- **Module 08** — `Logger`-style **interfaces** for seams.
- **Ch.01–02** — encapsulation and loose coupling.

Brief refresher: **inheritance** shares an **is-a** relationship and a **single superclass** chain. **Composition** places **collaborators** in fields and **forwards** calls.

## 3. Why This Concept Exists

Inheritance was the first OOP reuse mechanism taught: “make `SavingsAccount` extend `Account`.” That works for **genuine specialization** with stable contracts. It fails when teams inherit only to **reuse a few methods** or model **has-a** as **is-a**:

```java
class Car extends Engine { } // wrong is-a — a car is not an engine type
```

Subclassing ties you to **superclass implementation**: a new `final` method or changed hook in the base can break subclasses without your code changing—**fragile base class problem**.

**Composition** says: hold an `Engine` inside `Car`, expose `start()` by delegating to `engine.start()`. You can **swap** engines, **mock** them in tests, and **avoid** spurious type relationships.

## 4. Conceptual Explanation

### 4.1 Inheritance (is-a)

**Inheritance** (`extends`) models **specialization**:

- `SavingsAccount` **is an** `Account`.
- Subclass **inherits** members; **overrides** selected behavior.
- **One** direct superclass in Java.

Use when **LSP** holds: anywhere `Account` is expected, `SavingsAccount` behaves as callers expect.

### 4.2 Composition (has-a)

**Composition** places one object **inside** another:

```java
class Car {
    private final Engine engine;
    boolean start() { return engine.start(); }
}
```

**Car has an Engine.** Callers talk to `Car`; `Car` **delegates** to `Engine`.

**Delegation** = forwarding a call to another object that performs the work.

### 4.3 When inheritance fits

| Inheritance reasonable | Prefer composition |
|------------------------|-------------------|
| True **subtype** with shared protocol | Reuse **implementation** only |
| Framework **hooks** (`abstract` template) | Mix unrelated capabilities |
| **Sealed** small hierarchy (Module 07) | Cross-cutting logging/metrics |
| Polymorphic **substitution** needed | Want to **swap** behavior at runtime |

### 4.4 Composition + interfaces

`AuditedService` **has a** `Logger` (interface). Production uses `ConsoleLogger`; tests use a **fake** collecting messages—same pattern as `UserRepository` in Module 08.

```java
class AuditedService {
    private final Logger logger;
    String execute(String action) {
        logger.log("begin " + action);
        logger.log("end " + action);
        return "done:" + action;
    }
}
```

No need for `AuditedService extends ConsoleLoggingService extends Service`.

### 4.5 Inheritance for extension vs for reuse

**For extension (good):** superclass defines algorithm; subclasses fill hooks—**Template Method** (Module 07).

**For reuse only (suspect):** subclass to call `protected helper()` on a utility base—often better as a **private final Helper helper** field.

### 4.6 `Car` demo recap

- `describe()` returns `model + " / " + engine.describe()` → `Sedan / 180hp`.
- `start()` delegates to `engine.start()` → `true`.

## 5. Mechanism and Execution Flow

Inheritance and composition differ in **how calls reach implementation code** and **what the type system promises** to callers.

### 5.1 Inheritance call chain (`extends`)

When a subclass overrides a method:

1. The caller holds a reference typed as the **superclass** (polymorphism).
2. The JVM resolves the receiver’s **runtime class** and dispatches to the **most specific override** (Module 07).
3. An implicit or explicit **`super.method()`** continues the chain to the superclass implementation—subclass and superclass bodies are **linked** in one inheritance hierarchy.

The **is-a** contract means every override must preserve caller expectations (**LSP**).

### 5.2 Composition and delegation (`has-a`)

For [`CompositionVsInheritanceDemo`](../../examples/core-java/composition-vs-inheritance-demo/):

1. Caller invokes `car.start()` — compile-time type `Car`.
2. `Car.start()` forwards to `engine.start()` — a normal instance call on the **composed** field.
3. `Car` is **not** a subtype of `Engine`; substituting a `Car` where an `Engine` is required is a **compile error**.

Swapping `Engine` implementations changes behavior without altering the `Car` type line—**no fragile superclass hook** required.

### 5.3 Audited cross-cutting path

`AuditedService.execute(action)`:

1. Logs through the **`Logger` interface** reference (composition).
2. Returns a `String` result—no inheritance of logging behavior.
3. Tests inject a **fake logger** without subclassing production service classes.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Composition** | Object holds references to other objects as parts or collaborators |
| **Inheritance** | Subclass `extends` superclass; is-a type relationship |
| **Delegation** | Outer object forwards work to an internal collaborator |
| **Has-a** | Compositional relationship (Car has Engine) |
| **Is-a** | Inheritance relationship (Square is-a Rectangle — design carefully) |
| **Fragile base class** | Superclass change breaks subclasses unexpectedly |

## 7. Syntax and Language Rules

```java
class Car {
    private final Engine engine;
    Car(String model, Engine engine) { ... }
}

class CarBad extends Engine { } // compiles but wrong design

interface Logger { void log(String msg); }

class AuditedService {
    private final Logger logger;
    AuditedService(Logger logger) { this.logger = logger; }
}
```

**Prefer `private final` collaborators** set in constructor (immutable wiring).

## 8. Step-by-Step Examples

### 8.1 Minimal — delegate one method

```java
class Printer {
    void print(String s) { System.out.println(s); }
}
class Greeter {
    private final Printer p;
    Greeter(Printer p) { this.p = p; }
    void hello(String name) { p.print("hi " + name); }
}
```

### 8.2 Realistic — full demo

```java
Engine engine = new Engine(180);
Car car = new Car("Sedan", engine);
System.out.println(car.describe());
System.out.println("start=" + car.start());

Logger logger = new ConsoleLogger();
AuditedService svc = new AuditedService(logger);
System.out.println(svc.execute("save"));
```

**Verified output (Temurin 21.0.12.1):**

```text
Sedan / 180hp
start=true
log:begin save
log:end save
done:save
```

**Important lines:**

- `Car` does not subclass `Engine`; **`describe`** composes strings from model and engine.
- `ConsoleLogger.log` prefixes `log:` — matches audit lines.
- `execute` returns **`done:`** + action without inheritance from logger.

### 8.3 Advanced — decorator via composition

```java
class CachingPayment implements PaymentProcessor {
    private final PaymentProcessor inner;
  /* delegate pay after cache check */
}
```

**Decorator** wraps an interface—composition over subclassing `CardProcessor`.

## 9. Real-World Use Cases

- **`java.io`:** `BufferedInputStream` wraps `InputStream` — **Decorator** pattern.
- **Spring:** `@Service` classes **inject** repositories; few domain `extends` chains.
- **UI widgets:** contain layout managers rather than extend them.
- **Testing:** `AuditedService` + fake `Logger` — no subclass of production service.

## 10. Internal Behavior

See **§5** for inheritance dispatch vs delegation. This section restates the JVM angle.

Delegation is a normal **instance method call**—`car.start()` → `engine.start()` uses same virtual dispatch as inheritance overrides, but **without** exposing `Car` as subtype of `Engine`.

## 11. Edge Cases and Limitations

- **Deep delegation chains** can obscure control flow—name collaborators clearly.
- **Inheritance + composition together:** `class X extends Y { private Z z; }` is common; question each `extends`.
- **`super` calls** in constructor order (Module 06) — subclass **must** initialize composed fields after `super()`.
- **Covariant returns** in overrides — inheritance-only feature; composition uses forwarding types explicitly.

## 12. Common Mistakes

### Incorrect — inheritance for reuse

```java
class Report extends ArrayList<String> { } // List leaks; fragile API
```

### Corrected

```java
class Report {
    private final List<String> lines = new ArrayList<>();
    void add(String line) { lines.add(line); }
    List<String> snapshot() { return List.copyOf(lines); }
}
```

### Incorrect — `Car extends Engine`

Inherits `start()` signature but wrong **substitution**: not every `Engine` is a `Car`.

### Corrected — composition as in demo

### Incorrect — deep tree for logging

`LoggedService extends Service extends BaseService` — better `AuditedService` with `Logger`.

## 13. Best Practices

- **Favor composition** when reuse does not imply **is-a**.
- **Design for inheritance or prohibit it** (`final` class) — Effective Java guidance.
- Use **interfaces + composition** for capabilities (Module 08).
- **Delegate** to small collaborators; keep outer type as **facade** for its domain.
- When using inheritance, document **hooks** subclasses may override and invariants they must preserve (**LSP**).

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Inheritance | Polymorphism, shared algorithm | Fragile base; single parent |
| Composition | Flexible wiring; testing | More boilerplate forwarding |
| interface + composition | Maximum swap | More types |
| Deep inheritance trees | Quick early coding | Hard maintenance |

## 15. Performance Considerations

One extra delegation call is cheap. Inheritance override vs explicit delegate is similar JVM cost. Choose for **design**, not nanoseconds.

## 16. Security Considerations

Subclassing security-sensitive classes (`ClassLoader`, `Permission`) was historically abused. Prefer **composition** of vetted API types; do not expose overridable hooks on security-critical paths without review.

## 17. Java Version and Compatibility Notes

Since Java 1.0. **Records** are not for inheritance-heavy hierarchies—use with composition. **Sealed classes** limit inheritance to known subtypes—pair with composition for behavior mix-ins.

## 18. Related Language Comparisons

Many style guides (“prefer composition over inheritance”) come from **Gang of Four** and **Effective Java**. Ruby mixins use modules instead of deep subclassing; Java uses **interfaces + composition**.

## 19. Revision and Mastery

- **Gaps:** defaulting to `extends` for every shared method; forgetting **LSP** when choosing inheritance.
- **You should answer:** Why is `Car extends Engine` wrong for the domain? How would you test `AuditedService` without console output?
- **Advanced:** `ForwardingX` wrapper pattern; contrast with Kotlin `by` delegation.

## 20. Exercises

### Beginner

1. Replace `Engine(180)` with `Engine(200)`; predict `describe()` output for same `Sedan` model.
2. Add `NullLogger` that discards messages; what prints from `execute("save")`?

### Intermediate

3. Implement `TimedLogger` decorator that logs duration around a delegate `Logger`.
4. Explain why `Stack extends Vector` is considered a design mistake.

### Advanced

5. Sketch `PaymentProcessor` decorator chain: `FraudChecking(Persistence(CcardProcessor))` — no inheritance between decorators.

## 21. Interview Questions

1. Composition vs inheritance — when each?
2. What is delegation?
3. Fragile base class problem?
4. How does LSP relate to inheritance choices?
5. Decorator vs subclass?

## 22. Solutions

1. `Sedan / 200hp`.
2. Only `done:save` (no `log:` lines).
3. Log start time, call inner.log, log elapsed.
4. Stack is-a Vector exposes full Vector API (insert in middle, etc.).
5. Each decorator implements interface and holds inner `PaymentProcessor`.

## 23. Chapter Summary

Reuse behavior with **composition and delegation** unless a true **is-a** subtype earns **inheritance**. **`Car` + `Engine`** models **has-a**; **`AuditedService` + `Logger`** adds cross-cutting behavior without subclass explosion. This closes Module 09’s design thread: **boundaries** (Ch.01), **focused classes** (Ch.02), **flexible structure** (Ch.03). Next: Module 10 — **type system** and safe handling of heterogeneous values.

## 24. Key Terminology

Composition · inheritance · delegation · has-a · is-a · fragile base class

## 25. References and Further Reading

- Effective Java — Favor composition over inheritance; Item on inheritance
- Gamma et al. — Decorator pattern
- [Module 07 Ch.03](../07-inheritance-and-polymorphism/03-dynamic-dispatch-and-lsp.md) — LSP

## 26. Next Steps

- [Module 10 — Java Type System](../10-java-type-system/README.md)
- Revisit Module 08 — decorators on repository interfaces

## Review Checklist

**Mandatory Depth Review:** Technical profile; composition, inheritance, delegation at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (is-a vs has-a §4; call paths §5)
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 where demos exist)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **Decorator pattern walkthrough** — cited in references; full pattern examples in Module 15+.

**Verification note:** `CompositionVsInheritanceDemo` (`Sedan / 180hp`, `start=true`, audit lines, `done:save`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
