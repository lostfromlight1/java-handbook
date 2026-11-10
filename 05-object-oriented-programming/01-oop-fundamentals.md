# OOP Fundamentals

> **Status:** Completed  
> **Module:** 05 — Object-Oriented Programming  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain what **object-oriented programming (OOP)** is and why software models domains as objects with **state** and **behavior**.
- Distinguish a **class** (blueprint) from an **object** (instance in memory).
- Read and write **fields** (state), **methods** (behavior), and **constructors** enough to create objects with `new`.
- Use **references** to hold objects, create multiple instances, and reason about **identity** (`==`) at an introductory level (value equality / `equals` deepens in Module 06).
- Preview **encapsulation** with `private` fields and controlled access (full treatment in [02-encapsulation-and-abstraction.md](02-encapsulation-and-abstraction.md)).
- Connect classes to typical **backend** structure: entities with data, services with operations.

## 2. Prerequisites

- **Module 01** — compiling and running with `javac` / `java`, `main` as entry point.
- **Module 02** — variables, primitive and reference types, `final`, basic naming.
- **Module 03** — `if` / `else`, boolean conditions.
- **Module 04** — method declarations, parameters, `return`, `void` vs value-returning methods; **`static` vs instance** preview in Ch.04 (you will call **instance** methods on objects here).

Brief refresher: a **reference type** variable holds a reference (like an address) to an object on the heap, or `null` if it points nowhere. Primitives hold values directly. Methods from Module 04 become **instance methods** when they operate on a particular object’s fields.

## 3. Why This Concept Exists

Before OOP, large programs often mixed **data** and **operations** in global structures. Changing one part broke others; the same logical “thing” (a customer, an order) was scattered across files and variable names.

**Object-oriented programming** organizes code around **objects**: bundles of **state** (what the thing knows) and **behavior** (what the thing can do). A bank account **knows** a balance; it **can** deposit or withdraw. That mirrors how people talk about systems—and how backend teams model **entities** (data) and **services** (use cases).

Java is predominantly class-based OOP: you define **types** as **classes**, create **instances** at runtime, and collaborate through references. This chapter establishes that model; later chapters add inheritance, polymorphism, and composition.

## 4. Conceptual Explanation

### 4.1 What OOP is (at this chapter’s level)

**Object-oriented programming** is a way to structure programs so that:

| Idea | Meaning |
|------|---------|
| **Object** | A concrete value in memory with its own state and callable behavior |
| **Class** | The code template that describes what fields and methods every instance of that type has |
| **State** | Data stored in **fields** (also called attributes or properties) |
| **Behavior** | Operations defined as **methods** on the class |
| **Message / call** | One object (or `main`) invokes a method on another object |

The goal is **modeling**: your types should resemble concepts in the problem domain (accounts, users, shipments), not only “bags of global variables.”

### 4.2 Class vs object

Think of a **class** as a **blueprint** and an **object** as a **house built from that blueprint**.

```java
class BankAccount {
    private final String owner;
    private int balanceCents;
    // methods ...
}
```

The class `BankAccount` does not hold Alice’s balance by itself. When you write:

```java
BankAccount alice = new BankAccount("Alice", 100);
```

the **`new` expression** creates a **new object** (an instance), and `alice` is a **reference** to it. A second call creates a **different** object:

```java
BankAccount bob = new BankAccount("Bob", 50);
```

`alice` and `bob` refer to two distinct instances, each with its own `balanceCents`.

**Terminology:** some authors say “object” and “instance” interchangeably. “Class” always means the type definition in source code.

### 4.3 Fields, methods, and constructors

**Fields** store per-object state:

```java
private int balanceCents;
```

**Methods** define behavior and can read or update fields:

```java
boolean withdraw(int cents) {
    if (cents <= 0 || cents > balanceCents) {
        return false;
    }
    balanceCents -= cents;
    return true;
}
```

A **constructor** is a special block that runs when an object is created. It sets up initial state. Java has no separate “constructor” keyword—the name matches the class:

```java
BankAccount(String owner, int balanceCents) {
    this.owner = owner;
    this.balanceCents = balanceCents;
}
```

If you do not write any constructor, the compiler supplies a **no-argument default constructor** (only if you also do not declare other constructors). Once you add a constructor like the one above, the default is **not** generated—you must call the one you defined.

**`this`** inside an instance constructor or method refers to **the current object** being constructed or invoked. In the constructor, `this.owner = owner` distinguishes the field `this.owner` from the parameter `owner`.

Module 06 deepens constructors (chaining, `super`, initialization order). Here, you only need: **call `new TypeName(args)` and the matching constructor runs before you use the object.**

### 4.4 `new`, references, and identity

**References** are variables whose type is a class (or interface):

```java
BankAccount alice = new BankAccount("Alice", 100);
```

Steps at a high level (JVMS view, simplified):

1. Evaluate the constructor arguments.
2. Allocate memory for the new object (heap).
3. Run the constructor on that object.
4. Return a reference; assign it to `alice`.

Another variable can refer to the **same** object:

```java
BankAccount sameRef = alice;
```

There are still two variables (`alice`, `sameRef`) but **one** account object in memory—updates through either reference are visible through both.

**Identity** asks: “Are these two references pointing at the **same** object?” Java’s **`==` operator** on reference types compares **references** (identity), not “do these objects have the same field values?”

```java
sameRef == alice   // true — same object
bob == alice       // false — different objects
```

**Preview — `equals`:** For **logical** equality (“same account number,” “same text content”), types often override `equals(Object)` from `java.lang.Object`. The default `equals` behaves like `==` (identity). Module 06 and dedicated material on `equals` / `hashCode` show how to compare **content** correctly. For now: use `==` when you care about **same instance**; use `equals` when you care about **equivalent value** (and only when the type defines it).

**`null`:** A reference with no object. Calling a method on `null` causes `NullPointerException`. Prefer validating references before use in production code.

### 4.5 Encapsulation preview

**Encapsulation** means grouping state inside the object and **not** exposing internal representation blindly. In Java, you start with **`private` fields** so code outside the class cannot read or assign `balanceCents` directly:

```java
private int balanceCents;
```

Callers use **methods** (`deposit`, `withdraw`) so the class can enforce rules (non-negative balance, positive amounts). Chapter 02 covers access modifiers, getters/setters, and invariants in depth. One sentence to carry forward: **encapsulation is “hide the representation, force interaction through the type’s API.”**

### 4.6 How this connects to Java backends

In typical server applications:

| Concept in this chapter | Common backend role |
|-------------------------|---------------------|
| Class with fields | **Entity** or **domain model** — `User`, `Order`, `Payment` |
| Methods on that class | Domain behavior on the entity, or logic in a **service** class |
| `new` / references | Objects created per request, injected by a framework, or loaded from a database mapper |
| Encapsulation | Keeps domain rules in one place instead of scattered `if` checks in controllers |

Controllers often stay thin: parse HTTP, call a **service** object, map results to JSON. The **service** holds references to repositories and other collaborators (also objects). You do not need Spring to practice this pattern—the same object model appears in plain Java SE.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Class | Type definition: fields, constructors, methods |
| Object / instance | Runtime value created from a class |
| Field | Variable declared in a class; holds per-instance state (unless `static`) |
| Instance method | Method called on a specific object; can use instance fields |
| Constructor | Initializes a new object; same name as the class, no return type |
| Reference | Variable holding the location of an object (or `null`) |
| `new` | Operator/expression that creates an instance |
| Identity (`==`) | Whether two references denote the same object |
| Encapsulation | Bundling data with behavior and restricting direct access to internal state |

## 6. Syntax and Language Rules

**Class body** (JLS §8.1):

```text
[modifiers] class ClassName {
    [fields]
    [constructors]
    [methods]
}
```

**Object creation (class instance creation, JLS §15.13):**

```java
ClassName identifier = new ClassName(argumentList);
```

**Instance method call:**

```java
identifier.methodName(argumentList);
```

**Rules you use immediately:**

- A non-`static` field belongs to each instance; a non-`static` method requires a receiver object (or `this` inside the class).
- Constructors are not inherited; subclasses get different rules (Module 07+).
- Until you add `public`/`private`/`protected`, members are **package-private** (visible within the same package). Demos use `private` fields for encapsulation preview.

**Default constructor:** If your class has **no** constructor declarations, Java inserts `public ClassName() { }` (or package-private in a package-private class). If you declare **any** constructor, you must provide what callers need—no automatic no-arg constructor.

## 7. Step-by-Step Examples

Full program: [examples/core-java/oop-fundamentals-demo/](../examples/core-java/oop-fundamentals-demo/)

```bash
javac OopFundamentalsDemo.java
java OopFundamentalsDemo
```

### 7.1 Minimal: one class, one object

```java
class Greeter {
    private final String name;

    Greeter(String name) {
        this.name = name;
    }

    String greet() {
        return "Hello, " + name + "!";
    }
}

public class MinimalOop {
    public static void main(String[] args) {
        Greeter g = new Greeter("Ada");
        System.out.println(g.greet());
    }
}
```

- `Greeter` is the class; `g` is a reference to one object.
- `greet()` is an **instance** method: it uses `name` from **that** object.

### 7.2 Realistic: accounts and behavior

The demo defines `BankAccount` with owner and balance, deposit/withdraw rules, and `summary()` for display:

```java
BankAccount alice = new BankAccount("Alice", 100);
BankAccount bob = new BankAccount("Bob", 50);

alice.deposit(25);
boolean ok = alice.withdraw(40);
```

- `withdraw` returns `boolean` instead of throwing—valid design when “insufficient funds” is an expected outcome (contrast with validation errors in Ch.02).
- `bob` is unaffected when `alice` withdraws—separate state.

Important lines from the demo `main`:

```java
BankAccount sameRef = alice;
System.out.println("sameRef == alice: " + (sameRef == alice));
System.out.println("bob == alice: " + (bob == alice));
```

### 7.3 Advanced: why not public fields?

### Incorrect — balance can be corrupted from outside

```java
class BrokenAccount {
    int balanceCents;  // package-private / public — anyone can set
}

// elsewhere:
account.balanceCents = -1_000_000;
```

### Corrected — behavior enforces rules

```java
class BankAccount {
    private int balanceCents;

    boolean withdraw(int cents) {
        if (cents <= 0 || cents > balanceCents) {
            return false;
        }
        balanceCents -= cents;
        return true;
    }
}
```

The corrected version is the encapsulation preview; Ch.02 adds accessors and validation exceptions.

**Verified output (Temurin JDK 21.0.12.1):**

```text
alice withdraw ok=true
Alice balance=85 cents
Bob balance=50 cents
sameRef == alice: true
bob == alice: false
```

## 8. Real-World Use Cases

- **Domain entities** — `Invoice`, `Shipment` with fields and methods that keep business rules local.
- **Service classes** — `PaymentService` with references to `GatewayClient`, `LedgerRepository`; methods orchestrate use cases.
- **DTOs vs rich models** — some layers use anemic data holders; domain models combine state + behavior. OOP fundamentals support the rich style.
- **Testing** — construct objects with `new` in unit tests, call methods, assert on results without starting a server.

## 9. Internal Behavior

**Language (JLS):** Class instance creation evaluates arguments, allocates storage, initializes the new instance (constructor chain and field initializers—details in Module 06), then the reference is assigned (JLS §15.13, §12.5).

**JVM (JVMS §2):** Objects live in the **heap**. References in local variables or fields are copied **by value** when passed to methods (Module 04 Ch.03): the method receives a copy of the reference, but that copy still points at the same object—so instance methods can mutate object state.

**HotSpot (implementation note):** Object allocation is fast-path optimized (often **TLAB**—thread-local allocation buffers). This is not a language guarantee; it explains why creating many small short-lived objects is often acceptable until profiling says otherwise.

## 10. Edge Cases and Limitations

- **No object without class** — Java has no standalone “object” keyword like some languages; everything is an instance of a class (records and arrays are special class-like constructs—later modules).
- **Constructor name must match class** — typos create ordinary methods that are **not** constructors (subtle bug).
- **`==` on `Integer` and strings** — reference types need `equals` for value comparison; autoboxing creates new objects sometimes; do not use `==` for content (Module 06).
- **Aliasing** — two references, one object: defensive copies when exposing mutable internals (Ch.02).

## 11. Common Mistakes

### Incorrect — treating class name like an object

```java
BankAccount.deposit(100);  // error: deposit is not static
```

### Corrected

```java
BankAccount account = new BankAccount("Ada", 0);
account.deposit(100);
```

### Incorrect — `==` for string content

```java
String a = new String("Ada");
String b = new String("Ada");
System.out.println(a == b);  // false — different objects
```

### Corrected

```java
System.out.println(a.equals(b));  // true — same characters
```

### Incorrect — forgetting `new`

```java
BankAccount alice;  // reference only, no object yet
alice.deposit(10);    // NullPointerException if never assigned
```

### Corrected

```java
BankAccount alice = new BankAccount("Alice", 100);
alice.deposit(10);
```

## 12. Best Practices

- Model **nouns** from the domain as classes; **verbs** as methods.
- Keep fields **`private`** unless you have a deliberate API reason otherwise.
- Prefer **small, focused** methods over giant “god” objects (composition in Ch.04 of this module).
- Return meaningful types (`boolean` for success/failure, optional types later) instead of magic numbers.
- Name classes with **PascalCase**, fields and methods with **lowerCamelCase**.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Rich domain objects (methods on entity) | Rules live with data | Heavier than plain DTOs for JSON APIs |
| Return `boolean` vs exception for failures | Clear for expected failures | Caller must check return value |
| Public fields | Less boilerplate | Breaks invariants, hard to evolve |
| Many instances with `new` | Simple mental model | Lifecycle and sharing need care in concurrent apps |

## 14. Performance Considerations

Object creation is cheap at moderate rates on modern JVMs; prefer clarity. Micro-optimizing away objects without measurement often hurts readability. Long-lived caches of mutable objects need thread-safety analysis (later concurrency modules).

## 15. Security Considerations

If sensitive state (tokens, keys) is stored in objects, **encapsulation** and controlled methods reduce accidental leakage. Do not expose mutable collections or internal arrays without copying when security depends on immutability (Ch.02, immutability modules).

## 16. Java Version and Compatibility Notes

Classes, objects, `new`, and identity are core Java since 1.0. Examples validated on **21.0.12.1** (Temurin). Records (Java 16+) are compact class-like types—Module 09+.

## 17. Related Language Comparisons

- **C:** `struct` has data but no methods attached in the type; Java unifies them in classes.
- **Python:** attributes can be added at runtime; Java fields are fixed by the class declaration (with reflection as an escape hatch).
- **JavaScript:** prototypes vs Java’s class syntax (ES6) — Java classes are stricter at compile time.

## 18. Revision and Mastery

**You should already understand:**

- Difference between **class** and **instance**.
- Why `static void main` is not an instance method on your demo class.
- Pass-by-value of references from Module 04 when handing objects to methods.

**Common gaps:**

- Assuming `==` compares balances or names instead of identity.
- Thinking the constructor “returns” something—it runs during `new`.
- Confusing **compile-time type** of a reference with **runtime class** of the object (inheritance in Ch.03).

**Questions you should answer:**

1. How many `BankAccount` objects exist after `new` twice and `sameRef = alice`?
2. If `alice.withdraw` succeeds, can `sameRef` see the new balance?
3. When is `==` the right choice for references?

**Practical check:** Add a `transferTo(BankAccount other, int cents)` method that withdraws from `this` and deposits to `other` only when funds suffice.

## 19. Exercises

### Beginner

1. Run `OopFundamentalsDemo` and state which objects exist after `main` finishes.
2. Add `printOwner()` to `BankAccount` and call it from `main`.
3. Create a third account and compare `==` with the others in a `println`.

### Intermediate

4. Implement `transferTo` as described in Revision.
5. Add `deposit` that rejects non-positive amounts without changing balance (mirror `withdraw` style).
6. Sketch a `OrderService` class with two fields (references) to other collaborator types—no Spring required.

### Advanced

7. Without running, predict whether `new BankAccount("Ada", 0) == new BankAccount("Ada", 0)`.
8. Read JLS §15.13 summary: list three steps of instance creation in your own words.

## 20. Interview Questions

1. **What is the difference between a class and an object?**
2. **What does `new` do?**
3. **What does `==` compare for reference types?**
4. **Why make fields private?**
5. **What is `this` in an instance method?**

## 21. Solutions

1. Two `BankAccount` instances (`alice`/`sameRef` share one; `bob` is separate).
2. Example: `void printOwner() { System.out.println(owner); }` — note `owner` is `private` but visible inside the class.
3. Third account: `==` false vs `alice` unless assigned `alice`.
4. `transferTo`: if `withdraw(cents)` then `other.deposit(cents)`.
5. `if (cents <= 0) return;` at start of `deposit`.
6. Example fields: `private final OrderRepository orders; private final Clock clock;` — illustrates services as objects holding references.
7. **False** — two distinct `new` expressions create two objects.
8. Typical answer: evaluate constructor arguments, allocate instance, run constructor initializer (simplified).

## 22. Chapter Summary

OOP organizes programs around objects combining state (fields) and behavior (methods). A **class** defines the shape; **`new`** creates **instances** referenced by variables. **`==`** tests **identity**; logical equality uses `equals` when defined. **`private` fields** preview **encapsulation**, which Chapter 02 deepens. Backend code uses the same idea: domain objects and services collaborating through references.

## 23. Key Terminology

Class · object · instance · field · method · constructor · `new` · reference · identity · `==` · `this` · encapsulation (preview) · heap (preview)

## 24. References and Further Reading

- [JLS §8 — Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- [JLS §12.5 — Creation of New Class Instances](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.5)
- [JLS §15.13 — Class Instance Creation Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.13)
- [JVMS §2 — Structures](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html) (objects, references)

## 25. Next Steps

- [02-encapsulation-and-abstraction.md](02-encapsulation-and-abstraction.md) — access modifiers, invariants, getters/setters, abstraction.
- Module 06 — constructors and `equals` / `hashCode` in depth.
- [03-inheritance-and-polymorphism-intro.md](03-inheritance-and-polymorphism-intro.md) — subtype relationships (when published).

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate passed; progressive §7 examples
- [x] OopFundamentalsDemo verified on Temurin JDK 21.0.12.1
- [x] Encapsulation and `equals` previewed with current explanation
- [x] Revision and Mastery, exercises, solutions, JLS references
- [x] Backend connection included
