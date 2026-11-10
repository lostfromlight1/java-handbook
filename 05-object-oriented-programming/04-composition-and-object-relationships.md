# Composition and Object Relationships

> **Status:** Completed  
> **Module:** 05 — Object-Oriented Programming  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (has-a, delegation, object graphs)  
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

- Contrast **has-a** (composition / association) with **is-a** (inheritance from [Ch.03](03-inheritance-and-polymorphism-intro.md)).
- Model **composition**: an object **owns or uses** another as a part (`Car` and `Engine`).
- Explain **aggregation-style** sharing: multiple references to the same object vs reassigning one reference locally (`Person` and `Address`).
- State **“favor composition over inheritance”** in practical terms (parts, pluggable behavior, stability).
- Read simple **object graphs** in backend domains (`Order` → `OrderLine` items).
- Reason about **lifecycle and ownership** intuitively—what references mean—without overstating garbage-collector “ownership” rules.
- Use UML vocabulary lightly: composition, aggregation, association (conceptual; not exam-focused).

## 2. Prerequisites

- **Classes and objects** — fields, constructors, methods, `final` references ([Module 04 Ch.04](../04-methods-and-functions/04-static-instance-and-method-references.md)).
- **Inheritance intro** — [03-inheritance-and-polymorphism-intro.md](03-inheritance-and-polymorphism-intro.md) for is-a contrast.
- **References** — a variable holds a reference to an object; assignment copies the reference ([03-pass-by-value-and-stack-frames.md](../04-methods-and-functions/03-pass-by-value-and-stack-frames.md)).
- **Arrays** — fixed-size `OrderLine[]` buffer in the demo (resizable collections in Module 12).

## 3. Why This Concept Exists

Not every relationship is **is-a**. A **`Car` is not an `Engine`**; it **contains** one. Forcing `class Car extends Engine` would break substitutability and confuse every API that expects an engine in a test stand.

**Composition** (object A has a field referring to object B) models **parts**, **collaborators**, and **services** with clear boundaries. You can **swap** implementations (`Engine` → electric drivetrain) without a deep inheritance tree. Backend services routinely build **graphs**: an `Order` references many `OrderLine` values; a `Controller` holds a `Service` reference injected at construction.

Inheritance shares **identity as a type**; composition shares **behavior by delegation** (`car.start()` calls `engine.start()`). Most maintainable systems use **both**: shallow inheritance where is-a is real; composition everywhere else.

## 4. Conceptual Explanation

### 4.1 Has-a vs is-a

| Relationship | Question | Java mechanism | Example |
|--------------|----------|----------------|---------|
| **Is-a** | Can every instance of B be used wherever A is expected? | `extends` / `implements` | `Manager` is an `Employee` |
| **Has-a** | Does A use or contain B as part of its structure? | Field reference | `Car` has an `Engine` |

If the domain phrase sounds wrong as English (“a car **is an** engine”), inheritance is probably wrong.

### 4.2 Composition: part-of and delegation

**Composition** (strong **has-a**): the **whole** is built with a **part** that belongs to its logical structure. In code:

```java
class Car {
    private final Engine engine;

    Car(String model, Engine engine) {
        this.model = model;
        this.engine = engine;
    }

    boolean start() {
        return engine.start();   // delegation
    }
}
```

- `Car` **does not** inherit engine methods; it **calls** the engine.
- The car’s lifecycle is tied to **having** an engine reference for as long as the car object exists in the model (here `final` enforces that reference does not change after construction).

UML often draws composition as a filled diamond on the **whole** side when the part’s lifetime is tied to the whole **in the model**. In Java, that is a **design intent**—the language does not enforce that destroying `Car` destroys a **shared** `Engine` instance used elsewhere.

### 4.3 Aggregation-style sharing and association

**Aggregation** (looser has-a): the part can exist **outside** the whole’s lifecycle in the domain model—**shared** references.

```java
Address home = new Address("12 Baker St", "London");
Person person = new Person("Sherlock", home);
```

`Person` **has an** `Address`. If another `Person` or a `Company` also held a reference to the **same** `Address` object, a change through one reference would be visible through the others (mutability permitting).

The demo shows a subtler point about **variables**:

```java
home = new Address("12 Baker St", "Westminster");
System.out.println("person still: " + person.describe());
```

Reassigning the local variable `home` does **not** change which `Address` `person` holds—`person` still references the **original** `London` object. Only **mutating** the shared object (e.g. `address.setCity(...)` if city were mutable) would affect every holder.

That distinction is **reference semantics**, not a special JVM “ownership” mode.

**Association** is the general term: objects know about each other (field, parameter, return value) without implying strict part-of. Composition and aggregation are **graded** associations.

### 4.4 Prefer composition over inheritance

The phrase **“favor composition over inheritance”** (often attributed to the Gang of Four) means:

- Use **inheritance** for true **is-a** and **substitutability**.
- Use **composition** when you want to **reuse implementation** or **combine behaviors** without freezing a class hierarchy.

| Situation | Prefer |
|-----------|--------|
| Specialized employee payroll rules | Inheritance or interface + strategy (Module 08) |
| Car with swappable engine | Composition |
| Stack implemented “by reusing” array | **Composition** (`Deque` delegate), not `class Stack extends ArrayList` |
| Testing with fake mail sender | Constructor-injected `MailSender` reference |

[Ch.03](03-inheritance-and-polymorphism-intro.md) warned about **fragile base class**; composition limits blast radius when part behavior changes.

### 4.5 Object graphs in backends

A **object graph** is a network of objects linked by references:

```text
Order "ORD-1"
  ├── OrderLine (keyboard, qty 2)
  └── OrderLine (cable, qty 1)
```

In services:

- **JPA/Hibernate** entities mirror graphs (`Order` ↔ `@OneToMany` lines)—persistence layers care about reachability and cascades (Module 31+).
- **DTOs** for REST responses often flatten graphs to JSON trees.
- **Domain models** keep invariants in the **aggregate root** (`Order` validates lines before confirm).

The demo’s `Order` uses a fixed array and `addLine`—pedagogically simple; production code uses `List<OrderLine>` and validation rules.

**Mechanism:** wiring lines in the constructor/`addLine` and aggregating with `totalCents()` is traced step-by-step in §5.4.

### 4.6 Lifecycle and ownership intuition (not GC law)

Developers ask: “Who owns this object?” In Java:

- Objects live on the **heap** until **no strong reference** is reachable (GC collects unreachable objects—Module 26+).
- **Ownership** in design terms means **who is responsible** for creating, configuring, and releasing a resource (files, sockets) or **who holds the canonical reference** in the domain.

Rules of thumb at this level:

- If only the `Car` should construct its `Engine` in your API, use a **private** constructor path or factory on `Car`—callers pass specs, not a shared engine instance.
- If multiple objects **share** a mutable `Address`, document that updates are visible everywhere.
- **`final` on a field** means the **reference** does not change; it does not make the referenced object immutable unless the type is immutable (`String`, well-designed value objects).

Do **not** claim that “when `Car` is GC’d, `Engine` is always destroyed”—if another variable still references that `Engine`, it remains reachable.

## 5. Mechanism and Execution Flow

Composition builds **object graphs** at construction time and acts through **delegation** at call time.

### 5.1 Wiring parts in a constructor

`new Car("Civic", new Engine("I4", 150))`:

1. Evaluate arguments; construct `Engine` (may itself allocate heap storage).
2. Construct `Car`, store **`final Engine engine`** reference (the link is fixed for the car’s lifetime).
3. `Car` does not subclass `Engine`; it **holds** a reference—has-a.

Reassigning a local variable that pointed at the engine does not change `car`’s field unless `car` exposes a mutator for that reference (usually avoided for core parts).

### 5.2 Delegation call chain

`car.start()`:

1. Receiver is `Car`.
2. `Car.start()` forwards to `engine.start()` using the **stored reference**.
3. State changes live in the part object (`Engine`); the whole exposes a single domain verb.

This is how services delegate to repositories or HTTP clients without inheriting from them.

### 5.3 Shared references (aggregation-style)

`Person` and `Address`: two `Person` references can point at **one** `Address` object. Updating street through one person’s `getAddress()` is visible through the other—**alias semantics** from shared references (Module 04 pass-by-value of references).

### 5.4 Object graphs and consistency

An `Order` holds `OrderLine[]` (later `List<OrderLine>`). **`addLine`** mutates the graph; **`totalCents`** aggregates by traversing references. The **aggregate root** (`Order`) is the entry point that keeps the graph consistent—typical backend modeling before persistence layers (Module 27+).

## 6. Terminology

| Term | Meaning |
|------|---------|
| Composition | Strong has-a; whole built from parts; often `final` part references |
| Aggregation | Weaker has-a; parts may be shared outside the whole |
| Association | Any structural link between objects |
| Delegation | Whole forwards work to a part (`car.start()` → `engine.start()`) |
| Object graph | Objects connected by references |
| Aggregate root | Domain object that guards consistency of its graph (DDD term; used in backends) |

## 7. Syntax and Language Rules

Composition is ordinary **field declarations** and **constructor injection**:

```java
class Whole {
    private final Part part;

    Whole(Part part) {
        this.part = Objects.requireNonNull(part);
    }
}
```

- **Has-a:** non-primitive field.
- **Immutability of link:** `final` field set once in constructor.
- **Null safety:** validate required parts in constructors (preview `Objects.requireNonNull`; Module 14 expands).

No special keyword beyond class and reference types. **Records** (Module 15) compact composition of data components.

## 8. Step-by-Step Examples

Full program: [examples/core-java/composition-demo/](../examples/core-java/composition-demo/)

```bash
javac CompositionDemo.java
java CompositionDemo
```

### 8.1 Minimal: composition and delegation

```java
Engine engine = new Engine("I4", 150);
Car car = new Car("Civic", engine);
System.out.println(car.describe());
System.out.println("start -> " + car.start());
```

`Car` stores `Engine`; `describe()` and `start()` **delegate** to the engine for horsepower text and startup logic.

### 8.2 Realistic: person and address (shared reference semantics)

```java
Address home = new Address("12 Baker St", "London");
Person person = new Person("Sherlock", home);
System.out.println(person.describe());

home = new Address("12 Baker St", "Westminster");
System.out.println("person still: " + person.describe());
```

First line prints London. Rebinding `home` leaves `person`’s internal `address` field pointing at the first `Address` instance.

### 8.3 Advanced: order graph and total

```java
Order order = new Order("ORD-1");
order.addLine(new OrderLine("keyboard", 2, 3500));
order.addLine(new OrderLine("cable", 1, 500));
System.out.println(order.summary());
```

`Order` aggregates line references in an array; `totalCents()` walks the graph and `summary()` formats a human-readable aggregate.

**Verified (Temurin JDK 21.0.12.1):**

```text
Civic with I4 150hp
start -> true
Sherlock @ 12 Baker St, London
person still: Sherlock @ 12 Baker St, London
ORD-1 total=7500 cents [keyboard x2 =7000 cents; cable x1 =500 cents]
```

## 9. Real-World Use Cases

- **Dependency injection:** `OrderService` has-a `OrderRepository` and `Clock` (test doubles swap implementations without subclassing the service).
- **Layering:** `RestController` has-a `ApplicationService`; service has-a domain factories.
- **Value composition:** `Money` has amount + currency; `Duration` has seconds (or use records).
- **UI / document models:** `Panel` contains `List<Component>`; trees of widgets are composition graphs.
- **Avoiding subclass explosion:** `Flying` + `Swimming` behaviors as composed interfaces or strategy objects instead of `class Duck extends FlyingSwimmingAnimal extends ...`.

## 10. Internal Behavior

At runtime, a composed object is a **single heap object** with fields holding **references** (addresses) to other heap objects. Reading `person.describe()` follows references: `person` → `address` → `street`/`city` strings.

The JVM does not tag fields as “composition” vs “aggregation”—only **reachability** matters for GC. **Escape analysis** (HotSpot optimization) may stack-allocate **non-escaping** objects; that is invisible in source and not a design tool.

## 11. Edge Cases and Limitations

- **Shared mutable parts:** one `Address` mutated through a getter affects every holder—defensive copies or immutable value types mitigate (Module 09 / 15).
- **Circular references:** `A` references `B`, `B` references `A`—GC still collects when the cycle is unreachable (no reference counting in standard HotSpot).
- **Composition depth:** deep graphs make serialization and debugging harder—flatten for APIs when appropriate.
- **Primitive fields:** `int qty` on `OrderLine` is not “composed object” but still part of the model—composition discussions usually focus on object references.

## 12. Common Mistakes

### Incorrect: inheritance for has-a

```java
class Car extends Engine { }  // "Car is an Engine" — wrong model
```

### Corrected

```java
class Car {
    private final Engine engine;
}
```

### Incorrect: assuming reassignment ripples

```java
Address home = person.getAddress();
home = new Address("New", "City");  // only local variable changes
// person unchanged unless person.setAddress(home) or mutate shared object
```

### Corrected

Call a setter on `Person` or mutate the **shared** instance deliberately with clear API.

### Incorrect: null part

```java
new Car("X", null);  // NPE later on start()
```

### Corrected

Reject `null` in constructor or use optional parts with explicit empty behavior.

## 13. Best Practices

- Name relationships in domain language (has-a / is-a) before picking `extends`.
- Prefer **constructor injection** of required parts for testability (framework DI builds on this—Module 47+).
- Make **immutable parts** (`final` fields, immutable types) when shared.
- Expose **narrow interfaces** to collaborators (`MailSender`, not concrete `SmtpMailSender`) when swapping implementations.
- Keep **aggregate invariants** on the root (`Order.addLine` could reject duplicate SKUs—exercise for readers).

## 14. Design Trade-offs

| Choice | Pro | Con |
|--------|-----|-----|
| Composition + delegation | Flexible, localized change | More boilerplate forwarding methods |
| Inheritance | Concise shared API | Tight coupling, fragile base |
| Mutable shared parts | Memory efficient | Aliasing bugs |
| Immutable value parts | Safer sharing | Allocation / update patterns |

## 15. Performance Considerations

Extra indirection (`car` → `engine`) is negligible compared to I/O. **Deep copying** graphs for defensive copies can be expensive—prefer immutable leaves or copy-on-write patterns when needed (profile first).

## 16. Security Considerations

Composed **mutable** domain objects passed across trust boundaries should be **validated** at aggregate boundaries (`Order` should not expose a modifiable line list that bypasses validation). **Immutability** reduces tampering after construction (security modules expand).

## 17. Java Version and Compatibility Notes

Composition uses core references and classes. **Records** (Java 16+) model transparent composition of data. Validated on **21.0.12.1** (Temurin).

## 18. Related Language Comparisons

- **Go:** embedding structs for composition-like syntax without subclassing.
- **Rust:** ownership system enforces unique/mutable borrows—Java relies on conventions and immutability patterns instead.
- **Kotlin:** delegation with `by` keyword for composed behavior.

## 19. Revision and Mastery

**You should already understand:**

- Reference assignment vs mutating shared objects.
- Delegation vs overriding.
- Why `person still: ... London` after `home = new Address(...)`.

**Common gaps:**

- Using inheritance only to reuse a method from an unrelated type.
- Confusing UML diamond notation with GC behavior.
- Exposing internal mutable collections from aggregates without copies.

**Questions you should answer:**

1. Does reassigning `home` change `person`’s address? (No, without mutating shared object or setter.)
2. Is `Order` an `OrderLine`? (No; has-many.)
3. When is composition preferable to inheritance? (Parts, strategies, pluggable deps.)

## 20. Exercises

### Beginner

1. Run `CompositionDemo` and trace references from `person` to street/city strings.
2. Add `boolean running` to `Engine`; make `start()` idempotent and print state from `Car`.
3. Draw a box diagram: `Order` → two `OrderLine` objects.

### Intermediate

4. Change `Address` to hold mutable `city`; mutate through `home` **before** reassignment and observe `person.describe()`.
5. Replace the fixed array in `Order` with `ArrayList<OrderLine>` (Module 12)—keep the same `summary()` output format.
6. Explain why `OrderService extends JdbcTemplate` would be a poor model vs has-a repository.

### Advanced

7. Implement `copyAddress()` on `Person` returning a **new** `Address` with same fields—when is copy needed?
8. Sketch how Spring **constructor injection** wires a graph of singleton beans (conceptual).

## 21. Interview Questions

1. **Composition vs inheritance?**
2. **What is delegation?**
3. **What happens when you reassign a local reference vs mutate a shared object?**
4. **What is an object graph in a domain model?**

## 22. Solutions

1. `person` → `Address` → `street`, `city` on heap.
2. `running` flag; second `start()` returns false or true per your spec—document behavior.
3. One `Order` node, two child `OrderLine` nodes.
4. Mutating shared `Address` updates `person` view; reassignment alone does not.
5. `List` with `add`; loop `lines` in `summary`.
6. Service is not a JDBC template; template is a **tool** the repository **uses**.
7. When exposing address to untrusted code or preventing external mutation of internal state.
8. Container creates beans, satisfies constructor parameters with other bean references—graph at startup.

## 23. Chapter Summary

**Has-a** relationships use **fields and delegation**; **is-a** uses **inheritance**. **Composition** models parts (`Car`/`Engine`); **aggregation-style** links share objects (`Person`/`Address`). Reassigning a reference variable does not alter other variables holding the same object unless you **mutate** the shared instance or call an updater. **Backends** build **object graphs** such as orders and lines; aggregate roots enforce rules. Prefer **composition** for parts and pluggable behavior; prefer **inheritance** when substitutability is real. **Ownership** is a design responsibility; **GC** only sees reachability.

## 24. Key Terminology

Has-a · is-a · composition · aggregation · association · delegation · object graph · aggregate root · reference semantics

## 25. References and Further Reading

- [JLS §4.3.1 — Objects](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.3.1)
- [Java Tutorial — Composition vs Inheritance](https://docs.oracle.com/javase/tutorial/java/concepts/inheritance.html)
- Gamma et al., *Design Patterns* — “Favor object composition over class inheritance”
- Module 09 — encapsulation and aggregate design
- Module 47+ — dependency injection as composition wiring

## 26. Next Steps

- Module 07 — when inheritance hierarchies are justified and how to keep them safe.
- Module 08 — interfaces and role-based composition.
- Module 12 — `List<OrderLine>` instead of fixed arrays for order graphs.
- Module 15 — records for compact composed data carriers.

## Review Checklist

**Mandatory Depth Review:** Technical profile; has-a, delegation, and object graphs at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful (wiring, delegation, graph traversal in §5)
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
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-10; Temurin re-verify)

**Omissions (section → reason):**

- **§ Internal Behavior (GC detail)** — omitted at intro depth; §4.6 and §10 state reachability without GC algorithm specifics (Module 26+).

**Verification note:** `CompositionDemo` output matches Temurin JDK 21.0.12.1 (2026-11-10 re-run).
