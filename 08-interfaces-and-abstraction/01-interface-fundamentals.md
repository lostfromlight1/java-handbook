# Interface Fundamentals

> **Status:** Completed  
> **Module:** 08 — Interfaces and Abstraction  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Define a **Java interface** and explain what a **programming contract** means in practice.
- Declare interfaces with **`interface`** and implement them with **`implements`**.
- Use a variable of **interface type** and explain **dynamic dispatch** on interface calls.
- Implement **multiple interfaces** on one class and explain why Java allows multiple interface inheritance but not multiple class inheritance.
- Contrast **interfaces** with **abstract classes** and choose between them at a design level.
- Recognize **constant fields** in interfaces and why modern code often avoids them.

## 2. Prerequisites

- **Module 05** — classes, objects, encapsulation, polymorphism introduction.
- **Module 07 Ch.01** — inheritance, `extends`, overriding, single superclass.
- **Module 07 Ch.02** — abstract classes (shared state and template methods).
- **Module 07 Ch.03** — dynamic dispatch (runtime type selects the implementation).

Brief refresher: when you call an instance method on a reference, the JVM uses the **runtime class** of the object to pick the implementation—whether the reference type is a class or an interface. An **interface** lists **abstract** instance method signatures (unless a `default` method provides a body in a later chapter) that implementing classes must satisfy.

## 3. Why This Concept Exists

Real systems need **pluggable behavior**: card payments vs wallet payments, email vs SMS notifications, in-memory vs database storage. Callers should depend on **what** a collaborator does (`pay`, `notify`, `save`), not **how** (`CardProcessor` vs `WalletProcessor`).

Without interfaces (or equivalent abstractions), code hard-codes concrete classes:

```java
void checkout(CardProcessor only) { /* cannot test with a fake */ }
```

That couples business logic to one implementation, makes **unit testing** painful, and blocks swapping vendors at runtime.

**Interfaces** give Java a first-class way to define a **type contract** separate from any single class hierarchy. Because a class may **`implements`** many interfaces, unrelated capabilities (`PaymentProcessor` + `Auditable`) combine without the **diamond problem** of multiple class parents.

## 4. Conceptual Explanation

### 4.1 What is an interface?

An **interface** is a reference type that declares **members** implementing types must provide (primarily **abstract instance methods**; Ch.02 adds `default`, `static`, and `private` methods). At compile time, the compiler checks that each concrete class **fulfills** the interface.

```java
interface PaymentProcessor {
    String pay(int cents);
}
```

Any **non-abstract** class that `implements PaymentProcessor` must implement `pay` with a compatible signature and return type (Ch.03 covers **covariant returns** in depth where they apply to classes; interface implementation follows the same override rules).

You **cannot** instantiate an interface directly:

```java
PaymentProcessor p = new PaymentProcessor(); // compile error
```

You instantiate a **concrete implementor**:

```java
PaymentProcessor p = new CardProcessor();
```

### 4.2 What “contract” means in programming

In everyday language, a contract is a promise between parties. In software, a **contract** is the set of **obligations and guarantees** visible at a type boundary:

| Party | Obligation / guarantee |
|-------|------------------------|
| **Interface (type)** | Declares **operations** (names, parameters, return types, checked exceptions if any). May document **semantic** expectations (e.g. “`pay` returns a receipt id string and does not charge twice for the same id”). |
| **Implementing class** | Must provide methods that **match** the signatures and honor documented behavior so callers are not surprised. |
| **Caller** | May use only what the interface exposes; must not assume hidden implementation details. |

A contract is **more than a method list**. Signatures are the **syntactic** contract; **behavioral** expectations are the **semantic** contract (documented in Javadoc, tests, and team conventions). The **Liskov Substitution Principle** (Module 07 Ch.03) applies: any implementation should be **substitutable** where the interface type is expected.

Example from the demo: `PaymentProcessor.pay(int cents)` promises a `String` result encoding how payment was taken (`"card:1500"`). A test double can return `"fake:1"` as long as callers only depend on the format they document—production code often defines stricter invariants.

### 4.3 Interface types and polymorphism

Interface-typed variables hold references to **any** object whose class implements that interface:

```java
PaymentProcessor card = new CardProcessor();
PaymentProcessor wallet = new WalletProcessor();
```

Both calls use **dynamic dispatch**: `card.pay(1500)` runs `CardProcessor.pay`; `wallet.pay(900)` runs `WalletProcessor.pay`. The reference type `PaymentProcessor` limits which methods compile; the runtime class decides which body runs.

### 4.4 Multiple interfaces

Java allows **one direct superclass** and **many interfaces**:

```java
class CardProcessor implements PaymentProcessor, Auditable {
    // must implement pay and auditId
}
```

Each interface adds **independent** obligations. This models cross-cutting capabilities without forcing a deep inheritance tree (`PaymentCard extends AuditableThing extends ...`).

### 4.5 Interface vs abstract class (decision guide)

| Factor | Interface | Abstract class |
|--------|-----------|----------------|
| Instance fields (non-`static`) | Not allowed | Allowed |
| Constructors | No instance constructor | Yes |
| Multiple inheritance of type | Many interfaces | One class |
| Shared mutable state in base | No | Yes |
| Typical use | Capability / service seam | Shared workflow + state |

From Java 8 onward, interfaces may have **`default`** methods (Ch.02), so some “shared code in base” moved to interfaces—but **state** still lives in implementing classes or composed helpers.

### 4.6 Constants in interfaces

Interfaces may declare fields that are **`public static final`** implicitly:

```java
interface Limits {
    int MAX = 10_000; // public static final int MAX
}
```

Modern style prefers **`enum`**, **`record`**, or a **`final` class with private constructor** for constants instead of “constant bags” in interfaces, because implementors **inherit** those static members in confusing ways. Know the syntax for reading legacy APIs.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Interface** | Reference type declaring abstract operations (and optionally `default`/`static` methods) that classes implement |
| **Contract** | Promises at a type boundary: signatures plus expected behavior |
| **`implements`** | Keyword declaring that a class fulfills one or more interfaces |
| **Implementor / implementation** | Concrete class that satisfies an interface |
| **Interface type** | Variable or parameter typed as the interface, not a concrete class |
| **Abstract method (in interface)** | Method without body unless `default`; implicitly `public` |

## 6. Syntax and Language Rules

```java
public interface Notifier {
    void notify(String message); // public abstract implicitly
}

public class EmailNotifier implements Notifier {
    @Override
    public void notify(String message) { ... }
}
```

Rules (Java 21):

- Interface methods without bodies are **public** and **abstract** (historically omitted keywords).
- Class methods implementing interface methods should use **`@Override`** (recommended).
- **`implements`** lists interfaces after `extends` if both are present: `class X extends Y implements A, B`.
- A class is **abstract** if it does not implement all inherited abstract/interface methods.
- **Nested interfaces** are legal but less common than top-level types in application code.

## 7. Step-by-Step Examples

### 7.1 Minimal: one interface, one implementor

```java
interface Greeter {
    String greet(String name);
}

class Hello implements Greeter {
  public String greet(String name) { return "hi " + name; }
}
```

### 7.2 Realistic: payments and notification (module demo)

Full runnable code: [interfaces-fundamentals-demo](../examples/core-java/interfaces-fundamentals-demo/).

```java
interface PaymentProcessor {
    String pay(int cents);
}

class CardProcessor implements PaymentProcessor, Auditable {
    @Override
    public String pay(int cents) {
        return "card:" + cents;
    }
    @Override
    public String auditId() {
        return "CARD-1";
    }
}
```

**Important lines:**

- `implements PaymentProcessor, Auditable` — two contracts, one class.
- `PaymentProcessor card = new CardProcessor()` — callers depend on `pay`, not card-specific APIs.
- `Auditable a = new CardProcessor()` — same object, different interface view (`auditId()`).

**Verified output (Temurin 21.0.12.1):**

```text
card:1500
wallet:900
email:paid
audit=CARD-1
```

### 7.3 Advanced: extracting an interface after the fact

When refactoring, **introduce interface** from concrete usages:

1. Identify methods callers actually need.
2. Declare `interface OrderWriter { void write(Order o); }`.
3. Change fields/parameters from `JdbcOrderWriter` to `OrderWriter`.
4. Keep `JdbcOrderWriter implements OrderWriter`.

This preserves behavior while enabling tests with `InMemoryOrderWriter`.

## 8. Real-World Use Cases

- **Payment gateways** — `PaymentProcessor` with card/wallet/BNPL implementations.
- **Repositories** — persistence behind `UserRepository` (Ch.04).
- **Listeners and callbacks** — `ServletRequestListener`, `MouseListener` patterns.
- **JDK platform** — `List`, `Map`, `Comparable`, `Runnable` (executor APIs).
- **Test doubles** — fake `Clock`, `IdGenerator`, `HttpClient` seams.

## 9. Internal Behavior

At compile time, `javac` verifies that each concrete class implements required methods. Bytecode represents interface types as references; **interface method calls** use `invokeinterface` (JVM spec), which resolves the target method on the runtime class (with default-method resolution rules in Ch.02).

HotSpot optimizes monomorphic call sites; performance is comparable to virtual calls on classes for hot paths. Micro-optimizing “interface vs class reference” rarely matters compared to I/O and allocation.

## 10. Edge Cases and Limitations

- **No instance state in the interface itself** — only implementors hold fields.
- **Cannot `extends` multiple classes** — use interfaces for extra types.
- **Visibility** — implementing method must be at least as visible as the interface method (`public`).
- **Generics** — `interface Repository<T>` follows normal generic rules (Module 14).
- **`Object` methods** — interfaces do not inherit `Object`, but all implementing objects are still `Object` instances; `default` methods may call `toString()` on `this`.

## 11. Common Mistakes

### Incorrect: confusing interface type with implementation

```java
CardProcessor p = new WalletProcessor(); // compile error — not a subtype
```

### Corrected

```java
PaymentProcessor p = new WalletProcessor(); // OK
```

### Incorrect: missing `@Override` and wrong signature

```java
class Broken implements PaymentProcessor {
    public String pay(long cents) { return ""; } // does not override — compile error
}
```

### Incorrect: assuming interface guarantees thread safety

The interface does not magically synchronize; document and implement concurrency if needed.

## 12. Best Practices

- Name interfaces as **roles** (`Notifier`, `PaymentProcessor`) or **`Something`** capability (`Closeable`, `Serializable`).
- Keep interfaces **small** (Interface Segregation); split fat interfaces.
- Document **semantic** contract in Javadoc, not only signatures.
- Program to the **interface type** in fields and parameters (`PaymentProcessor`, not `CardProcessor`).
- Prefer **composition** when behavior is not a pure “is-a” capability.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Interface at API boundary | Testability, swapping implementations | More types to navigate |
| Concrete type in API | Fewer files | Harder mocks, tighter coupling |
| Many tiny interfaces | Precise contracts | Verbose `implements` lists |
| One fat interface | One import | Implementors forced to stub unused methods |

## 14. Performance Considerations

Interface dispatch is a normal virtual call path. Prefer clear design over avoiding interfaces for nanosecond-scale fears. Profile before collapsing abstractions.

## 15. Security Considerations

Interfaces do not enforce **authorization**. If `pay` must check caller identity, implement checks inside each implementation or in a **decorator** that wraps `PaymentProcessor`. Do not expose sensitive methods on public interfaces without need.

## 16. Java Version and Compatibility Notes

- Interfaces since Java 1.0; **default/static** methods since Java 8 (Ch.02).
- **Private** interface methods since Java 9.
- **Sealed interfaces** since Java 17 (Module 07 Ch.04) restrict implementors.

## 17. Related Language Comparisons

C# has `interface` with similar goals; multiple interface implementation is common. Go uses **implicit** interface satisfaction (no `implements` keyword)—Java’s explicit `implements` makes fulfillment visible at compile time.

## 18. Revision and Mastery

- **Gaps:** treating interface as “only names” without behavioral contract; forgetting multiple interfaces on one class.
- **Advanced:** when to extract interface vs use `record` + functions; sealed interfaces for closed domains.
- **You should answer:** What is substitutability? Why can’t you `new` an interface?

## 19. Exercises

### Beginner

1. Add `SmsNotifier implements Notifier` to the demo; print `sms:paid` for the same message.
2. Why does `Auditable a = new CardProcessor()` compile but `new Auditable()` does not?

### Intermediate

3. Sketch three classes and two interfaces where one class implements both and another implements one.
4. Write Javadoc for `pay` stating it must not return `null` in production implementations.

### Advanced

5. Compare extracting `PaymentProcessor` interface vs subclassing `AbstractPayment`—one paragraph on testing and coupling.

## 20. Interview Questions

1. Difference between `extends` and `implements`?
2. Can a class implement two interfaces with the same method signature?
3. What is the diamond problem and how do interfaces relate?
4. Is every interface method abstract in Java 21?
5. What does “program to an interface” mean?

## 21. Solutions

1. Mirror `EmailNotifier` with `System.out.println("sms:" + message);`.
2. `CardProcessor` is a concrete class with `auditId`; `Auditable` has no instantiable body.
3. Example: `CardProcessor implements PaymentProcessor, Auditable`; `WalletProcessor implements PaymentProcessor` only.
4. Forces implementors and callers to agree on non-null receipt strings.
5. Interface extraction decouples callers from one hierarchy; abstract class shares code but locks single inheritance.

## 22. Chapter Summary

**Interfaces** define **contracts**: method signatures plus behavioral expectations that implementations must honor. **`implements`** lets classes adopt multiple independent capabilities while callers use **interface types** for polymorphism and testing. The payment demo shows **substitutable** processors and a separate **`Auditable`** view. Next: **`default`**, **`static`**, and **`private`** members on interfaces.

## 23. Key Terminology

Interface · contract · `implements` · implementor · interface type · abstract interface method

## 24. References and Further Reading

- [JLS §9.1 — Interface Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html)
- [JLS §9.2 — Interface Members](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html#jls-9.2)
- Effective Java — Item on interface vs abstract class

## 25. Next Steps

- [02-default-static-and-private-methods.md](02-default-static-and-private-methods.md)
- Module 09 — design principles built on encapsulation and seams

## Review Checklist

- [x] Learning objectives covered
- [x] Contract explained (syntactic + semantic)
- [x] Depth gate met; demo output verified
- [x] InterfacesFundamentalsDemo on Temurin JDK 21.0.12.1
- [x] Exercises, Revision and Mastery, references
