# Inheritance and Polymorphism Intro

> **Status:** Completed  
> **Module:** 05 — Object-Oriented Programming  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain **inheritance** as an **is-a** relationship and read `class Sub extends Super`.
- Use **subclass** / **superclass** (child / parent) vocabulary correctly.
- Chain **superclass constructors** with `super(...)` from a subclass constructor.
- Distinguish **method overriding** (same instance method in subclass) from **overloading** (same name, different parameter list in one class).
- Apply **`@Override`** and know why it helps the compiler.
- Describe **polymorphism**: one reference type, several runtime types; the JVM selects the correct **instance** method at runtime.
- Use **`instanceof`** to test runtime type (intro level; pattern matching is a later topic).
- Choose when inheritance helps vs when **composition** (has-a) is safer—preview of [04-composition-and-object-relationships.md](04-composition-and-object-relationships.md) and Module 07.
- Name the **fragile base class** problem at a high level.

## 2. Prerequisites

- **Classes and objects** — fields, constructors, `this`, instance methods ([01-oop-fundamentals.md](01-oop-fundamentals.md), [02-encapsulation-and-abstraction.md](02-encapsulation-and-abstraction.md); also [Module 04 Ch.04](../04-methods-and-functions/04-static-instance-and-method-references.md)).
- **Methods** — declarations, `return`, calling conventions ([01-methods-basics.md](../04-methods-and-functions/01-methods-basics.md)).
- **Overloading** — same class, different parameter lists ([02-overloading-varargs-and-recursion.md](../04-methods-and-functions/02-overloading-varargs-and-recursion.md)); contrast with overriding here.
- **Arrays** — `Employee[] team` in the demo (Module 02+ array basics).

Brief refresher: an **object** is a value of a class type created with `new`. A **reference** variable (`Employee e`) holds a pointer to an object. That reference has a **compile-time type** (what the variable is declared as) and a **runtime type** (what `new` actually constructed)—polymorphism lives in that gap.

## 3. Why This Concept Exists

Programs model domains: employees, payments, shapes, HTTP handlers. Without inheritance you **duplicate** shared fields and behavior in every similar class (`name`, `baseSalary`, `annualPay()` copied into `Manager`, `Contractor`, …). Inheritance lets one **superclass** hold common state and operations; **subclasses** add or specialize.

**Polymorphism** lets you write one piece of code—a payroll loop over `Employee`—that still runs correct `Manager` logic when the array holds managers. Call sites stay stable (`e.annualPay()`); behavior varies by **actual object**. That is how frameworks invoke your overrides without knowing your concrete class names.

Trade-off: inheritance is powerful and easy to misuse. A deep hierarchy couples subclasses to superclass details—that motivates **composition**, **sealed** types (Module 07 / 15), and interface-based design (Module 08).

## 4. Conceptual Explanation

### 4.1 Is-a and the `extends` keyword

**Inheritance** means a class **is a specialized kind of** another class:

- A **Manager** **is an** **Employee** for payroll rules that apply to all staff.
- Syntax: `class Manager extends Employee { ... }`
- **Superclass** (base, parent): `Employee`
- **Subclass** (derived, child): `Manager`

The subclass **inherits** accessible members of the superclass (fields and methods not blocked by access modifiers). The subclass **is assignable** to the superclass type:

```java
Manager m = new Manager("Riley", 50_000, 10_000);
Employee e = m;   // legal: every Manager is an Employee
```

The reverse is not automatic: `Manager mm = e;` is a compile error unless you **cast** (and prove runtime type—Module 07).

### 4.2 What subclasses add or change

A subclass typically:

1. Adds **new fields** (`bonus` on `Manager`).
2. Adds **new methods** only managers need (not in our minimal demo).
3. **Overrides** superclass methods when behavior differs (`annualPay()` includes bonus).

It must still satisfy the superclass contract: constructors must initialize inherited state legally (see `super` below).

### 4.3 `super(...)` and constructor chaining

A subclass constructor **must** initialize the superclass portion of the object. If the superclass has **no** accessible no-argument constructor, the subclass constructor’s **first statement** must be a call to another constructor—usually `super(arguments)`:

```java
Manager(String name, int baseSalary, int bonus) {
    super(name, baseSalary);   // runs Employee(String, int) first
    this.bonus = bonus;
}
```

Order of execution:

1. `super(...)` runs the superclass constructor (sets `name`, `baseSalary`).
2. Subclass field initializers and remaining constructor body run (`bonus`).

If you omit `super(...)` and the superclass only offers parameterized constructors, the compiler inserts an implicit `super()` and the code **fails to compile**. Module 07 covers default constructors, `this(...)`, and chaining edge cases in depth.

### 4.4 Overriding vs overloading

| | **Overriding** | **Overloading** |
|---|----------------|-----------------|
| Where | Subclass vs superclass | Same class (or inherited + new overload) |
| Method | Same name, same parameter list, compatible return | Same name, **different** parameter list |
| Resolution | **Runtime** (virtual dispatch) for instance methods | **Compile time** |
| Annotation | `@Override` on replacing instance method | No `@Override` (different signature) |

```java
// Overriding (subclass)
@Override
int annualPay() {
    return baseSalary() + bonus;
}

// Overloading (Module 04) — same class, different parameters
static int max(int a, int b) { ... }
static int max(int a, int b, int c) { ... }
```

**Do not confuse** overriding with **hiding**: a `static` method in a subclass with the same signature as a superclass `static` method is **hidden**, not overridden—calls through the reference type pick the static method of the **declared** type. Instance overriding is the polymorphic case this chapter emphasizes.

### 4.5 `@Override`

`@Override` is a **marker annotation**. It tells the compiler: “this method is intended to override a superclass instance method.” If you misspell the name or change the parameter list, you get a **compile error** instead of silently creating an unrelated new method.

```java
@Override
String label() {
    return "Manager(" + name() + ")";
}
```

It is optional in the JLS sense but **strongly recommended** on every intentional override.

### 4.6 Polymorphism and virtual dispatch (intro)

**Polymorphism** (“many forms”): code uses a **supertype** reference, but the JVM runs behavior from the **actual object’s class**.

```java
Employee asEmployee = new Manager("Casey", 60_000, 5_000);
System.out.println(asEmployee.annualPay());   // 65000 — Manager’s override
```

- **Compile-time type** of `asEmployee`: `Employee`
- **Runtime type** of the object: `Manager`
- Call `asEmployee.annualPay()` — the JVM uses **virtual method invocation** (`invokevirtual` in bytecode) to dispatch to `Manager.annualPay()`.

The loop in the demo is the same idea:

```java
for (Employee e : team) {
    System.out.println(e.label() + " pay=" + e.annualPay());
}
```

Each element may be `Employee` or `Manager`; each iteration picks the right `label()` and `annualPay()` without `if (e instanceof Manager)`.

**Language guarantee:** instance method dispatch on non-`private`, non-`static`, non-`final` methods uses the **runtime class** (JLS instance method selection). HotSpot implements that with vtables and related mechanisms—implementation detail, not something you rely on for security.

**Limitation at this depth:** `final` methods cannot be overridden; `private` methods are not inherited for overriding purposes; fields are **not** polymorphic—accessing a field through a superclass-typed reference uses the field declared in the compile-time type (a common mistake; Module 07 expands).

### 4.7 `instanceof` (intro)

The **instanceof** operator tests whether an object is an instance of a type (class or interface):

```java
System.out.println("runtime type Manager? " + (asEmployee instanceof Manager));  // true
```

- Operand left: a reference (may be `null` → `false`)
- Operand right: a type name
- Result: `boolean`

Java 16+ also offers **pattern matching for instanceof** (`if (e instanceof Manager m) { ... }`); Module 19 / control-flow depth covers that. Here, use plain `instanceof` to read runtime type when polymorphic code is not enough (logging, optional downcast).

Downcasting (`(Manager) e`) is legal only when `instanceof` is true; wrong casts throw `ClassCastException` at runtime.

### 4.8 When inheritance helps vs composition (preview)

**Inheritance fits** when the relationship is a stable **is-a** and behavior truly specializes superclass rules:

- `Square` is-a `Rectangle` only if your math model allows it (often debated—design matters).
- `Manager` is-a `Employee` for org-wide payroll.

**Composition fits** when you model **has-a** parts or want to swap behavior without subclass explosion:

- A `Car` **has an** `Engine`; the car is not a kind of engine.
- Prefer **has-a** over **is-a** when you only reuse implementation convenience (“my list behaves like …”) without a true domain is-a.

[04-composition-and-object-relationships.md](04-composition-and-object-relationships.md) develops has-a, ownership, and object graphs. **Module 07** covers Liskov substitution, `protected`, `abstract` classes, and **sealed** hierarchies; **Module 08** shifts many extension points to interfaces.

Rule of thumb: inherit for **substitutability** (“every subclass must honor superclass promises”); compose for **parts and pluggable behavior**.

### 4.9 Fragile base class (brief warning)

A **fragile base class** occurs when the **superclass changes** (new hooks, changed method order, new `final` methods) and **subclasses break** without editing their source—because they depended on superclass internals or timing.

Symptoms: overriding one method accidentally breaks another that called it; superclass adds a field subclass did not know about; template method in base class changes step order.

Mitigations (deeper modules): **favor composition**, **seal** small hierarchies, **program to interfaces**, keep superclass surface minimal and stable, avoid calling overridable methods from constructors (advanced pitfall).

## 5. Terminology

| Term | Meaning |
|------|---------|
| Superclass | General class being extended (`Employee`) |
| Subclass | Specialized class (`extends Employee`) |
| `extends` | Keyword declaring inheritance from one superclass (single inheritance in Java) |
| `super(...)` | Call to superclass constructor from subclass constructor |
| Override | Subclass instance method replaces superclass version (same signature) |
| Overload | Same method name, different parameters in one class |
| Polymorphism | One interface/supertype, many concrete behaviors at runtime |
| Virtual dispatch | Runtime selection of overriding instance method |
| `instanceof` | Runtime type test |
| Upcast | Assign subclass reference to superclass type (`Employee e = manager`) |
| Downcast | Narrow reference to subclass (`(Manager) e`)—requires care |

## 6. Syntax and Language Rules

```java
class SubclassName extends SuperclassName {
    SubclassName(/* params */) {
        super(/* super params */);  // often first statement
        // subclass initialization
    }

    @Override
    ReturnType methodName(ParameterList) {
        // overridden body; may call super.methodName() (Module 07)
    }
}
```

**Rules for this chapter:**

- Java allows **single inheritance** of classes: one `extends` per class.
- Subclass constructors must eventually invoke a superclass constructor (explicit `super(...)` or chained `this(...)` that ends in `super`).
- Instance method overriding requires the same name and parameter types (after generics erasure); return type must be **covariant** (subtype) or same—details in Module 07.
- Only **instance methods** participate in the polymorphic dispatch described here.
- `@Override` applies to methods overriding superclass or interface methods; required for records/interfaces in some cases—ordinary class overrides use it as documentation + safety.

## 7. Step-by-Step Examples

Full program: [examples/core-java/inheritance-poly-demo/](../examples/core-java/inheritance-poly-demo/)

```bash
javac InheritancePolyDemo.java
java InheritancePolyDemo
```

### 7.1 Minimal: inheritance skeleton

```java
class Employee {
    private final String name;
    private final int baseSalary;

    Employee(String name, int baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    int annualPay() {
        return baseSalary;
    }
}

class Manager extends Employee {
    private final int bonus;

    Manager(String name, int baseSalary, int bonus) {
        super(name, baseSalary);
        this.bonus = bonus;
    }

    @Override
    int annualPay() {
        return baseSalary() + bonus;  // baseSalary() accessor on Employee in the demo
    }
}
```

In the full demo, `Employee` exposes package-private `baseSalary()` and `name()` so `Manager` can use inherited state without duplicating fields.

- `extends Employee` — every `Manager` is an `Employee`.
- `super(name, baseSalary)` — initializes inherited `name` and `baseSalary`.
- Without `annualPay()` override, a manager would get base pay only.

### 7.2 Realistic: payroll array (polymorphism)

```java
Employee[] team = {
        new Employee("Sam", 50_000),
        new Manager("Riley", 50_000, 10_000),
        new Employee("Jordan", 40_000)
};

for (Employee e : team) {
    System.out.println(e.label() + " pay=" + e.annualPay());
}
```

The array type is `Employee[]`; elements 0 and 2 are plain employees; element 1 is a `Manager`. The loop never branches on type—overridden methods supply the right pay and label.

### 7.3 Advanced: supertype reference, `instanceof`, polymorphic pay

```java
Employee asEmployee = new Manager("Casey", 60_000, 5_000);
System.out.println("runtime type Manager? " + (asEmployee instanceof Manager));
System.out.println("polymorphic pay=" + asEmployee.annualPay());
```

`asEmployee` is declared `Employee` but points at a `Manager`. `annualPay()` still returns `60_000 + 5_000` = `65000`.

### Verified output (Temurin JDK 21.0.12.1)

```text
Employee(Sam) pay=50000
Manager(Riley) pay=60000
Employee(Jordan) pay=40000
runtime type Manager? true
polymorphic pay=65000
```

**Line guide:** `label()` is overridden in `Manager` for clearer logging; package-private accessors in the demo keep the focus on inheritance (production code might use `protected` or public getters—Module 07).

## 8. Real-World Use Cases

- **Framework callbacks:** servlet `service()`, JUnit `@Test` lifecycle, Spring template methods—you override hooks; the framework calls your code through a base type reference.
- **Domain taxonomies:** `Payment`, `CardPayment`, `WirePayment` with shared `amount()` and specialized `settle()`.
- **Plugin-style APIs:** abstract base class in a library; applications ship concrete subclasses (interfaces often preferred today—Module 08).
- **Collections and streams:** variables typed `List<String>` hold `ArrayList` or `CopyOnWriteArrayList`; method calls dispatch to the runtime implementation (same polymorphic idea on interfaces).

## 9. Internal Behavior

For a virtual instance call, the JVM bytecode is typically **`invokevirtual`** (or `invokeinterface` for interface types). The constant pool entry names the **descriptor** and **class**; at execution, HotSpot resolves the **actual target** using the receiver object’s class (vtable or equivalent). That is why overriding works even when the receiver is stored in a superclass-typed variable.

**Spec vs implementation:** the JLS defines which method is selected; HotSpot’s vtable layout is not an API guarantee. **`invokespecial`** is used for constructors, `private` methods, and `super` calls—not for ordinary polymorphic instance calls on `this`.

## 10. Edge Cases and Limitations

- **`null` and `instanceof`:** `null instanceof Employee` is `false` (no `NullPointerException`).
- **Overriding vs overload in subclass:** `void print(Employee e)` and `void print(Manager m)` in the same class overload; only exact signature match overrides.
- **Fields hide, do not override:** if `Employee` and `Manager` both declare `int bonus`, `((Employee)m).bonus` and `m.bonus` can differ—avoid shadowing fields in hierarchies.
- **`final` class** cannot be extended; **`final` method** cannot be overridden.
- **Constructor calls are not polymorphic**—`new Manager(...)` always runs `Manager` constructors; only instance methods dispatch virtually after construction.

## 11. Common Mistakes

### Incorrect

```java
class BadManager extends Employee {
    BadManager(String name, int base, int bonus) {
        // Forgot super(name, base) when Employee has no no-arg constructor
        this.bonus = bonus;  // compile error
    }
}
```

### Corrected

Call `super(name, base)` as the first statement (or chain to another constructor that does).

### Incorrect

```java
class Employee {
    void work() { System.out.println("employee"); }
}
class Manager extends Employee {
    // Missing @Override; typo:
    void work() { System.out.println("manager"); }  // ERROR if @Override present
}
```

### Corrected

Match the superclass signature exactly; use `@Override` to catch typos.

### Incorrect: expecting overloaded method to run polymorphically

```java
void log(Employee e) { ... }
void log(Manager m) { ... }
Employee ref = new Manager(...);
log(ref);   // calls log(Employee), not log(Manager) — compile-time overload pick
```

Overloading is resolved at **compile time** using the **reference’s compile-time type**.

## 12. Best Practices

- Model **is-a** only when substitutability is true for all callers of the superclass.
- Annotate overrides with `@Override`.
- Keep hierarchies **shallow** early in a project; deep trees are hard to reason about.
- Expose **supertype** in APIs (`List`, `Employee`) when multiple implementations exist; construct **concrete** types at the edges (`new ArrayList<>()`, `new Manager(...)`).
- When reuse is about **containing** another object, prefer composition (next chapter).
- Avoid calling overridable methods from superclass constructors (subclass state may not be ready)—advanced rule, but worth noting.

## 13. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| Inheritance for shared code | DRY fields/methods; polymorphic loops | Tight coupling to superclass; fragile base risk |
| Interface + composition | Flexible replacement; easier testing | More boilerplate; multiple types to learn |
| Deep inheritance trees | Quick short-term modeling | Hard refactors; LSP violations (Module 07) |

## 14. Performance Considerations

Virtual calls are cheap on HotSpot; tiny methods are often **inlined** across overrides when the runtime type is provably stable (JIT optimization, not a language promise). Do not replace inheritance with manual `if (type)` chains for micro-performance without measurement—polymorphic calls are the idiomatic default.

## 15. Security Considerations

**Do not** rely on `instanceof` alone for security authorization when callers can pass arbitrary objects; combine with type-safe APIs, sealed hierarchies, or capability checks (security modules later). Downcasting must follow a successful `instanceof` (or equivalent) to avoid `ClassCastException` in security-sensitive paths.

## 16. Java Version and Compatibility Notes

Class inheritance and `instanceof` are core Java. `@Override` since Java 5; pattern matching `instanceof` since Java 16—this chapter uses classic `instanceof`. **Sealed classes** (Java 17) restrict subclasses—Module 15. Validated on **21.0.12.1** (Temurin).

## 17. Related Language Comparisons

- **C++:** multiple inheritance of classes; Java allows one class superclass, multiple interfaces.
- **Kotlin:** classes `final` by default; inheritance is opt-in (`open`).
- **Go:** composition via struct embedding, not Java-style class extension.

## 18. Revision and Mastery

**You should already understand:**

- Difference between **overloading** (compile time, same class) and **overriding** (runtime, subclass).
- Why `Employee e = new Manager(...); e.annualPay()` can call `Manager` code.
- First statement rules for `super(...)` in constructors.

**Common gaps:**

- Thinking **fields** override like methods (they do not).
- Using **overload** resolution rules when expecting **override** behavior (`log(ref)` example).
- Extending for **code reuse** without a real is-a (prefer composition).

**Questions you should answer:**

1. Can a class `extends` two classes? (No.)
2. Does `@Override` on a wrong signature compile? (No.)
3. What is the runtime type of `new Manager(...)` stored in `Employee e`? (`Manager`.)

**Practical check:** Add `class Contractor extends Employee` with a different `annualPay()`; run the demo loop and confirm dispatch without changing the loop body.

## 19. Exercises

### Beginner

1. Run `InheritancePolyDemo` and mark which lines use upcasting, overriding, and `instanceof`.
2. Add a `Contractor` with flat `annualPay()` of `baseSalary + 5_000`; place one in `team` and predict output before running.
3. Add `@Override` to `Manager.label()` if missing; introduce a typo in the method name and observe the compiler error.

### Intermediate

4. Add `int bonus()` to `Manager` only; try calling `e.bonus()` in the loop—fix with `instanceof` + cast or by redesigning (discuss trade-offs).
5. Explain why `static void compare()` cannot be overridden in the polymorphic sense (hiding only).
6. Sketch a case where inheritance is wrong and a `Car` **has-a** `Engine` is right.

### Advanced

7. Without running, predict output if `Employee` declares `String label()` returning `"Employee"` and `Manager` does not override—what does Riley print?
8. Research: why calling `toString()` from a superclass constructor can surprise subclasses (fragile base class).

## 20. Interview Questions

1. **What is the difference between overloading and overriding?**
2. **Can you override a `static` method?** — Hiding, not overriding; dispatch is not virtual.
3. **What does `super(name, base)` do?**
4. **Why use `@Override`?**
5. **What is polymorphism in Java?** — Supertype reference, subtype object, virtual instance dispatch.

## 21. Solutions

1. Upcast: array assignment and `Employee asEmployee = new Manager(...)`; overriding: `annualPay`, `label`; `instanceof`: third block in `main`.
2. Contractor line shows `Contractor(...)` pay = base + 5000 if you mirror `Manager` pattern.
3. Typo without matching superclass method: compile error with `@Override`.
4. Loop cannot call `bonus()` on `Employee` type—cast after `instanceof Manager` or add behavior to superclass/interface.
5. Static methods belong to the class; no receiver dispatch for overriding semantics.
6. Car is not a type of engine; engine is swappable part—composition chapter.
7. Riley prints `Employee(Riley)` if label not overridden; pay still 60000 if `annualPay` overridden.
8. Subclass fields unset during `super()`; overridden `toString()` may read uninitialized subclass state.

## 22. Chapter Summary

Inheritance (`extends`) models **is-a** relationships and shares superclass implementation. Subclasses chain `super(...)` to initialize inherited state, **override** instance methods for specialized behavior, and mark overrides with `@Override`. **Polymorphism** lets superclass-typed references call subclass methods via **virtual dispatch**. **`instanceof`** inspects runtime type. Use inheritance when substitutability is real; otherwise prefer **composition**. Superclass changes can **break subclasses** (fragile base class)—design hierarchies with care; Module 07 deepens inheritance rules and alternatives.

## 23. Key Terminology

Inheritance · superclass · subclass · `extends` · `super` · override · overload · `@Override` · polymorphism · virtual dispatch · `instanceof` · upcast · fragile base class

## 24. References and Further Reading

- [JLS §8.1.4 — Superclasses and Subclasses](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.4)
- [JLS §8.4.8 — Inheritance, Overriding, and Hiding](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.8)
- [JLS §15.12 — Method Invocation Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.12)
- [Java Tutorial — Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html)
- [Java Tutorial — Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)

## 25. Next Steps

- [04-composition-and-object-relationships.md](04-composition-and-object-relationships.md) — has-a, composition, object graphs, when not to inherit.
- Module 07 — `protected`, `abstract`, Liskov substitution, sealed hierarchies, `super` calls on methods.
- Module 08 — interfaces as flexible extension points.

This chapter introduced inheritance and polymorphism at **intro** depth; later modules add **formal rules**, **design constraints**, and **modern language** features without replacing the behavior described here.

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate passed; progressive §7 examples
- [x] InheritancePolyDemo verified on Temurin JDK 21.0.12.1
- [x] Overriding vs overloading; composition and fragile base previewed with current explanation
- [x] Revision and Mastery, exercises, solutions, JLS references
- [x] Virtual dispatch labeled spec vs HotSpot where relevant
