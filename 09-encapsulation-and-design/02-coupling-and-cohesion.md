# Coupling and Cohesion

> **Status:** Completed  
> **Module:** 09 — Encapsulation and Design  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

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

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Coupling** | Degree of interdependence between software units |
| **Cohesion** | Degree to which elements inside one unit belong to one purpose |
| **Loose coupling** | Depend on little of another module’s internals |
| **Tight coupling** | Hard to change one module without changing others |
| **SRP** | Single Responsibility Principle — one reason to change per class |
| **God class** | Type that accumulates unrelated responsibilities |

## 6. Syntax and Language Rules

No special keywords for coupling. Patterns that **reduce** coupling in Java:

```java
interface Clock { long nowMillis(); }           // abstraction
record LineItem(String sku, int unitCents) {} // data carrier
class Service {
    Service(Repository repo) { ... }            // ctor injection
}
```

**Final fields** for collaborators prevent accidental rebinding after construction.

## 7. Step-by-Step Examples

### 7.1 Minimal — split calculation and print

```java
int total = qty * price;
System.out.println("total=" + total); // presentation mixed in main only
```

In real code, move print to a formatter class.

### 7.2 Realistic — demo

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

### 7.3 Advanced — inject formatter for tests

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

## 8. Real-World Use Cases

- **Spring `@Service` + `@Repository`:** service layer depends on repository **interface**; JDBC impl in another package.
- **Hexagonal architecture:** **ports** (interfaces) on domain; **adapters** implement IO.
- **Microservices:** loose coupling between services via **HTTP/events** and versioned schemas—not shared database tables as hidden API.
- **UI separation:** Android ViewModel holds state; View renders—same cohesion idea.

## 9. Internal Behavior

Coupling is a **design** property, not a JVM feature. Compiler still resolves **concrete** types at compile time unless you use **interface** references—then **dynamic dispatch** applies at calls (Module 07–08).

## 10. Edge Cases and Limitations

- **Over-splitting:** ten one-method classes with no domain name hurt readability—cohesion can be **too low** if concepts are fragmented.
- **DTO proliferation:** every call with five primitives may need a **record** for clarity—balance stamp coupling vs named types.
- **Framework magic** (reflection, proxies) can hide coupling—read wiring config.
- **Circular dependencies:** `A` needs `B`, `B` needs `A`—extract shared interface or third type.

## 11. Common Mistakes

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

## 12. Best Practices

- Ask: **“What is the one job of this class?”** Name classes as **nouns** doing that job (`OrderCalculator`, not `Helper`).
- Pass **abstractions** or **minimal data** across boundaries.
- Keep **formatting, IO, persistence** out of pure domain rules when possible.
- Use **package structure** to reflect layers (`domain`, `adapters`, `web`).
- Measure coupling with **import fan-out** and **architectural tests** (optional, later modules).

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Many small cohesive classes | Clear testing boundaries | More types to navigate |
| One large class | Fewer files | Hard to test and change |
| Interface at seam | Swappable impl | Extra type |
| Concrete everywhere | Fast prototype | Refactor pain |

## 14. Performance Considerations

Extra indirection through interfaces is negligible vs I/O. **Do not** merge unrelated logic into one hot loop for “performance” without profiling.

## 15. Security Considerations

Tight coupling to **authentication details** in every method spreads security bugs. Centralize **authZ** in one filter/interceptor; keep domain rules **unaware** of HTTP headers except at boundaries.

## 16. Java Version and Compatibility Notes

Principles are language-agnostic. **Records** (16+) encourage **immutable data carriers** that reduce shared mutable coupling. **Sealed hierarchies** (17+) document closed variants with less accidental extension.

## 17. Related Language Comparisons

Go packages and small interfaces encourage loose coupling similarly. PHP legacy code often shows **god classes**—Java enterprise patterns (layers, DI) aim to prevent that.

## 18. Revision and Mastery

- **Gaps:** confusing SRP with “never more than one method”; thinking interfaces always means low coupling (fat interfaces still couple to unused methods—Interface Segregation, Module 08).
- **You should answer:** Why can `InvoiceFormatter` change currency suffix without touching `OrderCalculator`?
- **Advanced:** metrics for coupling (Ca, Ce); package cycles detection.

## 19. Exercises

### Beginner

1. Compute `totalCents(3, 200, 25)` by hand; predict formatter line for id `ORD-1`.
2. List two reasons a merged `OrderEverything` class would change.

### Intermediate

3. Introduce `ShippingPolicy` interface with `shippingCents(order)`; inject into calculator.
4. Sketch a bad version where formatter imports `OrderCalculator` and calls `totalCents` internally—what breaks cohesion?

### Advanced

5. One paragraph: SRP vs “one public class per file” rule in Java.

## 20. Interview Questions

1. Define coupling and cohesion in one sentence each.
2. What is a god class?
3. How does DIP reduce coupling?
4. Name two types of coupling.
5. Why pass `int totalCents` instead of `Order` to a formatter?

## 21. Solutions

1. `3*200+25=625` → `ORD-1 => 625 cents`.
2. Pricing rule changes; invoice template changes (also email if merged).
3. Calculator takes `ShippingPolicy` in ctor; pricing reason to change localizes.
4. Formatter now does calculation + presentation—two reasons to change; tests need calculator.
5. SRP is responsibility; file rule is compilation unit organization—related but not identical.

## 22. Chapter Summary

**Cohesion** keeps classes focused; **coupling** measures how entangled they are. **SRP** and **DIP** guide you toward **small public APIs** and **injected abstractions**. The demo separates **pricing** from **formatting** with **data coupling** via primitives. Next: **composition** vs **inheritance** for reusing behavior without fragile **is-a** relationships.

## 23. Key Terminology

Coupling · cohesion · loose coupling · tight coupling · SRP · god class

## 24. References and Further Reading

- Robert C. Martin — SOLID principles (SRP, DIP)
- [Module 08 Ch.04](../08-interfaces-and-abstraction/04-api-design-with-interfaces.md) — DIP in code

## 25. Next Steps

- [03-composition-versus-inheritance.md](03-composition-versus-inheritance.md)
- Module 10 — typing and pattern matching for safer boundaries

## Review Checklist

- [x] Learning objectives covered
- [x] Coupling/cohesion types and SRP explained
- [x] CouplingCohesionDemo verified Temurin 21.0.12.1
- [x] Exercises, Revision and Mastery
- [x] Depth gate met
