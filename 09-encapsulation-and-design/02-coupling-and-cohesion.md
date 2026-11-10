# Coupling and Cohesion

> **Status:** Completed  
> **Module:** 09 — Encapsulation and Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (coupling, cohesion, SRP, demo separation)  
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

- Define **coupling** (how much one unit depends on another) and **cohesion** (how focused a unit’s responsibilities are).
- Recognize **tight coupling** smells: concrete types everywhere, shared globals, formatting mixed with business rules.
- Apply **Single Responsibility Principle (SRP)** at class level: one reason to change.
- Connect **loose coupling** to **interfaces** and **constructor injection** (Module 08).
- Refactor a tangled class into **cohesive** collaborators with narrow interactions.
- Predict demo output for **pricing** vs **presentation** separation.

## 2. Prerequisites

- **Module 05 Ch.04** — composition.
- **Module 08 Ch.04** — Dependency Inversion; depend on abstractions.
- **Ch.01** — hide internals; expose small APIs.

Brief refresher: **coupling** is about **dependencies between** classes or modules. **Cohesion** is about **how related** the duties inside one class are. Good designs **lower coupling** and **raise cohesion** without drowning in tiny classes.

## 3. Why This Concept Exists

Early projects grow one “god class” that calculates tax, formats PDFs, sends email, and reads JDBC. A change to currency rounding forces edits in the same file as HTML templates. **Merge conflicts** explode; **tests** need a database to assert a string format.

**Coupling and cohesion** give vocabulary and goals:

- **High cohesion** — each class does **one well-defined job** (price order lines; format an invoice string).
- **Loose coupling** — classes interact through **small, stable** interfaces (ints, DTOs, or interfaces), not through each other’s private fields.

Java does not enforce this at language level—you apply it through **design** and **code review**.

## 4. Conceptual Explanation

### 4.1 Cohesion

**Cohesion** measures whether elements of a module belong together.

| Kind | Idea | Java example |
|------|------|----------------|
| **Functional** | All methods serve one task | `OrderCalculator.totalCents(...)` |
| **Sequential** | Output of step A feeds step B | Pipeline parser → validator |
| **Communicational** | Methods touch same data | `Account` deposits and balance |
| **Temporal** | Run together at one lifecycle phase | `@PostConstruct` setup group |
| **Logical** | Grouped only by category | `Util` class with unrelated statics — **weak** |

Aim for **functional** or **communicational** cohesion in domain types. **`Util`** bags of unrelated static methods are a cohesion warning.

### 4.2 Coupling

**Coupling** is strength of **dependency** between modules.

| Type | Meaning |
|------|---------|
| **Content** | One module reaches into another’s internals (private fields) — worst |
| **Common** | Shared global mutable state |
| **Control** | One module passes flags that change another’s behavior (`if (isAdmin)`) |
| **Stamp** | Passes rich object when only part is needed |
| **Data** | Passes simple data (primitives, DTOs) — often acceptable |

**Loose coupling** prefers **data coupling** or **interface coupling** over content coupling.

### 4.3 Single Responsibility Principle (SRP)

**SRP (informal):** a class should have **one reason to change**—one **axis of responsibility**.

`OrderCalculator` changes when **pricing rules** change. `InvoiceFormatter` changes when **display format** changes. Merging them means **any** pricing tweak retests string templates.

SRP is not “one method per class.” It is **one purpose** per type at the abstraction level you maintain.

### 4.4 DIP and coupling

**Dependency Inversion** (Module 08): high-level `OrderService` depends on `PaymentProcessor`, not `CardProcessor`. That **reduces coupling** to a **contract**, not a vendor class.

Constructor injection:

```java
class OrderService {
    private final PaymentProcessor pay;
    OrderService(PaymentProcessor pay) { this.pay = pay; }
}
```

makes coupling **explicit** and **swappable**.

### 4.5 What “tangled” looks like

```java
class OrderEverything {
    int totalCents(int qty, int unit, int ship) { ... }
    String formatInvoice(String id, int total) { ... }
    void sendEmail(String body) { ... }
}
```

Tests for `formatInvoice` construct the whole object; email failures block pricing tests. **Split** by responsibility.

### 4.6 Demo design

[`CouplingCohesionDemo`](../../examples/core-java/coupling-cohesion-demo/):

- `OrderCalculator` — **high cohesion**, pricing only.
- `InvoiceFormatter` — **high cohesion**, presentation only.
- **Loose coupling** — formatter takes `String id` and `int totalCents`; no reference to calculator type.

## 5. Mechanism and Execution Flow

Coupling and cohesion are **design properties**, but their benefits show up in how the compiler wires **dependencies** at build time and how objects **exchange data** at run time.

### 5.1 Compile-time dependency edges

When `main` (or a framework) constructs collaborators:

1. The compiler checks that each **constructor argument** type exists and is visible.
2. If `OrderService` declares `PaymentProcessor pay`, only types **assignable** to `PaymentProcessor` may be passed—concrete wiring is explicit in source or DI configuration.
3. Replacing `CardProcessor` with `FakePay` for tests does not require editing `OrderService` if both implement the interface—**the compile-time edge** points at the abstraction, not the vendor class.

### 5.2 Demo execution path (pricing → presentation)

[`CouplingCohesionDemo`](../../examples/core-java/coupling-cohesion-demo/):

1. `OrderCalculator.totalCents(qty, unit, ship)` runs **only** pricing rules; it returns an `int`.
2. `InvoiceFormatter.format(id, totalCents)` receives **data** (`String`, `int`), not the calculator instance—no shared mutable state between types.
3. `main` prints both lines independently; changing tax logic does not recompile the formatter unless the **data contract** (`int` cents) changes.

This is **data coupling**: modules communicate through simple, stable values.

### 5.3 Injected seams (constructor path)

```java
OrderService(PaymentProcessor pay) { this.pay = pay; }
```

1. Caller supplies the implementation at construction.
2. `OrderService` methods invoke `pay` through the **interface** vtable—runtime class may differ from compile-time reference type (Module 07–08).
3. Coupling is **to the contract**, not to one concrete class file.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Coupling** | Degree of interdependence between software units |
| **Cohesion** | Degree to which elements inside one unit belong to one purpose |
| **Loose coupling** | Depend on little of another module’s internals |
| **Tight coupling** | Hard to change one module without changing others |
| **SRP** | Single Responsibility Principle — one reason to change per class |
| **God class** | Type that accumulates unrelated responsibilities |

## 7. Syntax and Language Rules

No special keywords for coupling. Patterns that **reduce** coupling in Java:

```java
interface Clock { long nowMillis(); }           // abstraction
record LineItem(String sku, int unitCents) {} // data carrier
class Service {
    Service(Repository repo) { ... }            // ctor injection
}
```

**Final fields** for collaborators prevent accidental rebinding after construction.

## 8. Step-by-Step Examples

### 8.1 Minimal — split calculation and print

```java
int total = qty * price;
System.out.println("total=" + total); // presentation mixed in main only
```

In real code, move print to a formatter class.

### 8.2 Realistic — demo

```java
OrderCalculator calc = new OrderCalculator();
int total = calc.totalCents(2, 500, 50);

InvoiceFormatter fmt = new InvoiceFormatter();
System.out.println(fmt.format("ORD-9", total));
```

**Verified output (Temurin 21.0.12.1):**

```text
total=1050
ORD-9 => 1050 cents
```

**Math:** `2 * 500 + 50 = 1050` cents. Formatter does not know **how** total was computed—only the **value**.

### 8.3 Advanced — inject formatter for tests

```java
interface InvoiceSink {
    void emit(String line);
}

class ReportingInvoiceFormatter {
    private final InvoiceSink sink;
    ReportingInvoiceFormatter(InvoiceSink sink) { this.sink = sink; }
    void send(String id, int totalCents) {
        sink.emit(id + " => " + totalCents + " cents");
    }
}
```

Production uses `ConsoleSink`; tests use `ListSink` capturing lines **without** console.

## 9. Real-World Use Cases

- **Spring `@Service` + `@Repository`:** service layer depends on repository **interface**; JDBC impl in another package.
- **Hexagonal architecture:** **ports** (interfaces) on domain; **adapters** implement IO.
- **Microservices:** loose coupling between services via **HTTP/events** and versioned schemas—not shared database tables as hidden API.
- **UI separation:** Android ViewModel holds state; View renders—same cohesion idea.

## 10. Internal Behavior

See **§5** for dependency edges and the demo data path. This section notes JVM dispatch context.

Coupling is a **design** property, not a JVM feature. Compiler still resolves **concrete** types at compile time unless you use **interface** references—then **dynamic dispatch** applies at calls (Module 07–08).

## 11. Edge Cases and Limitations

- **Over-splitting:** ten one-method classes with no domain name hurt readability—cohesion can be **too low** if concepts are fragmented.
- **DTO proliferation:** every call with five primitives may need a **record** for clarity—balance stamp coupling vs named types.
- **Framework magic** (reflection, proxies) can hide coupling—read wiring config.
- **Circular dependencies:** `A` needs `B`, `B` needs `A`—extract shared interface or third type.

## 12. Common Mistakes

### Incorrect — static singleton for everything

```java
Database.INSTANCE.loadOrder(id); // entire app coupled to one concrete DB
```

### Corrected

```java
class OrderService {
    private final OrderRepository orders;
    OrderService(OrderRepository orders) { this.orders = orders; }
}
```

### Incorrect — passing entire `Order` to format footer only

```java
fmt.formatFooter(order); // formatter now coupled to all Order fields
```

### Corrected

```java
fmt.formatFooter(order.id(), order.totalCents());
```

### Incorrect — testing format inside calculator test

Assert string layout in `OrderCalculatorTest` — wrong responsibility.

## 13. Best Practices

- Ask: **“What is the one job of this class?”** Name classes as **nouns** doing that job (`OrderCalculator`, not `Helper`).
- Pass **abstractions** or **minimal data** across boundaries.
- Keep **formatting, IO, persistence** out of pure domain rules when possible.
- Use **package structure** to reflect layers (`domain`, `adapters`, `web`).
- Measure coupling with **import fan-out** and **architectural tests** (optional, later modules).

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Many small cohesive classes | Clear testing boundaries | More types to navigate |
| One large class | Fewer files | Hard to test and change |
| Interface at seam | Swappable impl | Extra type |
| Concrete everywhere | Fast prototype | Refactor pain |

## 15. Performance Considerations

Extra indirection through interfaces is negligible vs I/O. **Do not** merge unrelated logic into one hot loop for “performance” without profiling.

## 16. Security Considerations

Tight coupling to **authentication details** in every method spreads security bugs. Centralize **authZ** in one filter/interceptor; keep domain rules **unaware** of HTTP headers except at boundaries.

## 17. Java Version and Compatibility Notes

Principles are language-agnostic. **Records** (16+) encourage **immutable data carriers** that reduce shared mutable coupling. **Sealed hierarchies** (17+) document closed variants with less accidental extension.

## 18. Related Language Comparisons

Go packages and small interfaces encourage loose coupling similarly. PHP legacy code often shows **god classes**—Java enterprise patterns (layers, DI) aim to prevent that.

## 19. Revision and Mastery

- **Gaps:** confusing SRP with “never more than one method”; thinking interfaces always means low coupling (fat interfaces still couple to unused methods—Interface Segregation, Module 08).
- **You should answer:** Why can `InvoiceFormatter` change currency suffix without touching `OrderCalculator`?
- **Advanced:** metrics for coupling (Ca, Ce); package cycles detection.

## 20. Exercises

### Beginner

1. Compute `totalCents(3, 200, 25)` by hand; predict formatter line for id `ORD-1`.
2. List two reasons a merged `OrderEverything` class would change.

### Intermediate

3. Introduce `ShippingPolicy` interface with `shippingCents(order)`; inject into calculator.
4. Sketch a bad version where formatter imports `OrderCalculator` and calls `totalCents` internally—what breaks cohesion?

### Advanced

5. One paragraph: SRP vs “one public class per file” rule in Java.

## 21. Interview Questions

1. Define coupling and cohesion in one sentence each.
2. What is a god class?
3. How does DIP reduce coupling?
4. Name two types of coupling.
5. Why pass `int totalCents` instead of `Order` to a formatter?

## 22. Solutions

1. `3*200+25=625` → `ORD-1 => 625 cents`.
2. Pricing rule changes; invoice template changes (also email if merged).
3. Calculator takes `ShippingPolicy` in ctor; pricing reason to change localizes.
4. Formatter now does calculation + presentation—two reasons to change; tests need calculator.
5. SRP is responsibility; file rule is compilation unit organization—related but not identical.

## 23. Chapter Summary

**Cohesion** keeps classes focused; **coupling** measures how entangled they are. **SRP** and **DIP** guide you toward **small public APIs** and **injected abstractions**. The demo separates **pricing** from **formatting** with **data coupling** via primitives. Next: **composition** vs **inheritance** for reusing behavior without fragile **is-a** relationships.

## 24. Key Terminology

Coupling · cohesion · loose coupling · tight coupling · SRP · god class

## 25. References and Further Reading

- Robert C. Martin — SOLID principles (SRP, DIP)
- [Module 08 Ch.04](../08-interfaces-and-abstraction/04-api-design-with-interfaces.md) — DIP in code

## 26. Next Steps

- [03-composition-versus-inheritance.md](03-composition-versus-inheritance.md)
- Module 10 — typing and pattern matching for safer boundaries

## Review Checklist

**Mandatory Depth Review:** Technical profile; coupling, cohesion, SRP, and demo separation at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (cohesion/coupling tables §4; demo path §5.2)
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

- **Architectural metrics** (afferent/efferent coupling counts) — optional analytics; design vocabulary suffices at this level.

**Verification note:** `CouplingCohesionDemo` output `total=1050`, `ORD-9 => 1050 cents` on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
