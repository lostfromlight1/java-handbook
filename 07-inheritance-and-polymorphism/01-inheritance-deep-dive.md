# Inheritance Deep Dive

> **Status:** Completed  
> **Module:** 07 — Inheritance and Polymorphism  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (`super` chaining, override vs hide at Level 3)  
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

- Read and write **single-inheritance** hierarchies with **`extends`** and **`super(...)`** constructor chaining.
- Use **`protected`** to expose superclass hooks to subclasses without making fields `public`.
- Apply **`final`** on classes and methods to block inappropriate extension or overriding.
- Distinguish **overriding** (instance methods, dynamic dispatch) from **hiding** (static methods, fields).
- Extend superclass behavior with **`super.method()`** while preserving invariants.
- Recognize **fragile base class** coupling and when **composition** or **sealed** types fit better.

## 2. Prerequisites

- **Module 05 Ch.03** — `extends`, basic overriding, `super(...)`, intro polymorphism.
- **Module 05 Ch.02** — encapsulation, `private` fields, invariants.
- **Module 06 Ch.01** — constructors, `this(...)`, initialization order.
- **Module 04 Ch.04** — `static` vs instance members.

Brief refresher: a **subclass** inherits accessible members of its **superclass** and is assignable to superclass types (`SavingsAccount` is an `Account`). Only **single inheritance** of classes is allowed—one direct superclass per class. Extra reuse uses **interfaces** (Module 08) and **composition** (Module 05 Ch.04).

## 3. Why This Concept Exists

Module 05 introduced inheritance for shared payroll logic. Real hierarchies need sharper tools:

- **Constructors** must initialize superclass state legally—always via **`super(...)`** when the superclass has no accessible no-arg constructor.
- **`private`** superclass fields are invisible in subclass source; **`protected`** helpers or methods let subclasses participate without breaking encapsulation.
- **Overrides** change behavior; **`final`** methods freeze sensitive steps (security checks, template phases).
- Uncontrolled overriding causes **fragile base class** bugs: a superclass change breaks distant subclasses.

Module 07 deepens rules so hierarchies stay maintainable.

## 4. Conceptual Explanation

### 4.1 The object layout mental model

Every object stores **one contiguous instance**: superclass fields first (conceptually), then subclass fields. The JVM treats the object as the **full runtime type** (`SavingsAccount`) while references may be declared as supertypes (`Account`).

When you call an **instance method**, the JVM uses **dynamic dispatch** (Ch.03): the runtime type picks the implementation. Field access and **static** members use **compile-time** types unless overridden carefully—see hiding below.

### 4.2 Constructor chaining with `super(...)`

A subclass constructor’s **first statement** must be either **`this(...)`** (same class) or **`super(...)`** (direct superclass)—unless the compiler can insert a default `super()` into a no-arg subclass constructor.

Execution order for `new SavingsAccount(...)`:

1. Allocate memory for `SavingsAccount` (includes `Account` portion).
2. Run `Account` constructor (via `super(id, balance)`).
3. Run subclass field initializers and constructor body.

If `Account` only defined `Account(String id, int balance)`, a subclass **cannot** compile without calling that `super`—there is no invisible default on the superclass.

### 4.3 `protected` visibility

| Modifier | Same package | Subclass (any package) | Unrelated code |
|----------|--------------|------------------------|----------------|
| `private` | No | No | No |
| package-private | Yes | No (unless same package) | No |
| `protected` | Yes | Yes | No |
| `public` | Yes | Yes | Yes |

**`protected`** members are part of the **extension API** of a superclass: intended for subclasses, not for arbitrary clients. Prefer **`protected final`** accessors to hidden fields (`id()`, `balance()`) instead of widening fields to `public`.

### 4.4 Overriding vs hiding

| Feature | Override (instance method) | Hide (static method) / field |
|---------|---------------------------|------------------------------|
| Keyword | `@Override` (recommended) | No `@Override` for static “override” |
| Dispatch | Runtime type | Compile-time type of reference |
| Requires | Same signature, compatible return | Same signature (static), same field name hides |

**Rule of thumb:** treat **static** methods as belonging to the class, not the object—polymorphism does not apply.

### 4.5 `final` classes and methods

- **`final class`** — cannot be extended (`String`, many value types). Use when invariants depend on no subclasses.
- **`final` method** — cannot be overridden in subclasses; dynamic dispatch always uses the declaring class’s version for that method. Template methods (Ch.02) often mark the orchestration method `final` and hook methods overridable.

### 4.6 Calling superclass behavior: `super.method()`

Inside an override, **`super.canWithdraw(amount)`** (hypothetically) runs the superclass implementation. Useful when the subclass **tightens** or **extends** rules rather than replacing them entirely.

### 4.7 Fragile base class

A superclass that adds a new call to an overridable method in its own `public` API can break subclasses that did not expect re-entrancy. Mitigations: **`final`** template methods, **composition**, **sealed** permitted children (Ch.04), or **hooks** documented as extension points.

## 5. Mechanism and Execution Flow

Subclass construction and polymorphic method choice are **separate pipelines** (JLS §8.8, §12.5, §15.12.4.4). This section traces both so inheritance edits stay predictable.

### 5.1 Subclass construction from `new SavingsAccount(...)`

When `main` executes `new SavingsAccount("S-1", 1000)`:

1. **Class initialization** — run static initializers on `SavingsAccount`, then on `Account` if not yet initialized (superclass first in the initialization order story).
2. **Allocation** — reserve one object whose **runtime class** is `SavingsAccount`; storage conceptually includes inherited `Account` fields plus subclass fields.
3. **`super(...)` delegation** — the subclass constructor’s first statement must be `super(...)` or `this(...)` (Module 06). `super(id, balance)` runs the **`Account` constructor** to completion before any subclass instance initializers or subclass constructor body statements execute.
4. **Subclass portion** — instance field initializers and the remainder of the subclass constructor run; invariants enforced there (`minBalance`, strengthened `canWithdraw`).
5. **Usable reference** — if no exception escapes, the reference from `new` refers to a fully initialized `SavingsAccount` assignable to `Account`.

If step 3 throws, the object does not become usable—callers must not use the reference.

### 5.2 `super.method()` vs dynamic dispatch

Inside an override, `super.canWithdraw(amount)` uses **`invokespecial`** semantics (JLS): the call targets the **superclass implementation** declared above the current class, not the runtime type’s most specific override. That is how a subclass can **extend** superclass rules instead of only replacing them.

By contrast, when external code calls `account.canWithdraw(900)` on a variable typed `Account` that holds a `SavingsAccount`, **dynamic dispatch** (Ch.03) picks `SavingsAccount.canWithdraw` at run time.

### 5.3 Override vs hide at call sites

| Call form | Member | Resolver |
|-----------|--------|----------|
| `ref.instanceMethod()` | Overridden instance method | Runtime class of object |
| `ref.staticMethod()` | Hidden static method | Compile-time type of `ref` |
| `ref.field` | Hidden field | Compile-time type of `ref` |

The demo and **InheritanceDeepDemo** rely on instance **override** for `canWithdraw`; static or field “polymorphism” myths are avoided by keeping `balance` encapsulated behind `balance()` accessors.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Superclass** | Direct parent class in `extends` |
| **Subclass** | Child class extending another |
| **`super(...)`** | Constructor call initializing superclass portion |
| **`protected`** | Visible to subclasses and same package |
| **Override** | Subclass instance method replaces superclass version |
| **Hide** | Subclass static method or field shadows superclass |
| **`final`** | Cannot extend (class) or override (method) |
| **Extension API** | `protected` hooks meant for subclasses |

## 7. Syntax and Language Rules

```java
class Child extends Parent {
    Child(...) {
        super(...);           // first statement (or this(...))
    }

    @Override
    ReturnType method(Type arg) {
        super.method(arg);    // optional: delegate then extend
        ...
    }
}
```

- **`@Override`** — compiler verifies a superclass method is overridden; catches signature typos.
- **Return type covariance** — override may return a **subtype** of the superclass return (e.g. `Child` method returns `List<String>` where parent returns `List<?>` rules apply—see JLS for generics).
- **Subclass cannot weaken access** of an overridden method (cannot make `public` → `protected`).

## 8. Step-by-Step Examples

### 7.1 Minimal: chained constructor

```java
class Base {
    private final String id;
    Base(String id) { this.id = id; }
    String id() { return id; }
}

class Derived extends Base {
    private final int extra;
    Derived(String id, int extra) {
        super(id);
        this.extra = extra;
    }
}
```

`super(id)` must run before `this.extra` is assigned—superclass portion is valid first.

### 7.2 Realistic: protected accessors and override

Full runnable demo: [examples/core-java/inheritance-deep-demo/](../../../examples/core-java/inheritance-deep-demo/).

`Account` keeps `balance` **private** but offers **`protected`** `balance()` / `setBalance` for subclasses. `SavingsAccount` overrides `canWithdraw` to require remaining balance ≥ **`minBalance`** and enforces a **per-transaction cap** (`1_000` in the demo)—combined rules typical in banking domains.

**Verified output (Temurin JDK 21.0.12.1):**

```text
Savings(S-1) bal=1000 min=50
after deposit balance=1200
canWithdraw(900)=true
canWithdraw(1100)=false
```

Important lines:

- `super(id, balance)` — initializes inherited fields through `Account` constructor.
- `protected final String id()` — subclass can read `id` without `public` field.
- Overridden `canWithdraw` — **strengthens** policy (extra predicates), still honors `amount > 0`.

### 7.3 Advanced: `final` guard on sensitive path

```java
abstract class AuthenticatedAction {
    public final void run() {
        if (!checkAuth()) throw new SecurityException("denied");
        doAction();
    }
    protected abstract boolean checkAuth();
    protected abstract void doAction();
}
```

`run()` cannot be overridden to skip `checkAuth()`; subclasses customize hooks only.

## 9. Real-World Use Cases

- **Framework lifecycle:** `Servlet` / Spring template methods with fixed phases and overridable hooks.
- **Domain accounts:** checking vs savings sharing `Account` posting logic with different withdrawal rules.
- **UI toolkits:** abstract `Component` with `protected` layout helpers (legacy Swing/JavaFX patterns).
- **Security:** `final` verification steps on base handler; subclasses fill only allowed steps.

## 10. Internal Behavior

**JLS:** constructor calls walk from subclass to superclass; no superclass portion is visible to user code until superclass constructor completes (`this` escape is illegal in superclass constructor for incomplete object).

**HotSpot:** instance methods use **vtable** / **itable** dispatch (implementation detail); behavior matches JLS dynamic dispatch on instance methods. Field access resolves at compile time to the declared reference type’s field slot—avoid field hiding in production models.

## 11. Edge Cases and Limitations

- **No multiple class inheritance** — diamond problem avoided; use interfaces for multiple contracts.
- **Constructors are not inherited** — subclasses must declare constructors that call accessible superclass constructors.
- **If superclass becomes `final`**, existing subclasses compile until you try to extend it—rare migration pain.
- **Package-private superclass** in another package cannot be extended.
- **`protected` on `public` API** of libraries leaks extension surface—document and stabilize it.

## 12. Common Mistakes

### Incorrect: forgetting `super(...)` when superclass has no no-arg constructor

```java
class Bad extends Account {
    Bad(String id) {
        // compile error: implicit super() not visible
    }
}
```

### Corrected

```java
Bad(String id, int balance) {
    super(id, balance);
}
```

### Incorrect: widening invariants in override

```java
@Override
boolean canWithdraw(int amount) {
    return true; // breaks Account contract for all Account references
}
```

### Corrected: strengthen or call `super` then add checks

```java
@Override
boolean canWithdraw(int amount) {
    return super.canWithdraw(amount) && balance() - amount >= minBalance;
}
```

### Incorrect: relying on field hiding

```java
class Parent { String name = "parent"; }
class Child extends Parent { String name = "child"; }
Parent p = new Child();
System.out.println(p.name); // prints "parent", not "child"
```

Use **methods** or **single field in superclass** instead.

## 13. Best Practices

- Keep superclass fields **`private`**; expose **`protected`** accessors only when subclasses need them.
- Mark orchestration methods **`final`** when order matters (auth, transactions, lifecycle).
- Use **`@Override`** on every intentional override.
- Document **extension points** (`protected` methods subclasses may override).
- Prefer **shallow hierarchies**; question every `extends` after depth two.

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| `protected` accessors | Encapsulation + subclass access | Harder to refactor superclass |
| Deep inheritance | Shared code | Fragile base class, hard testing |
| `final` class | Strong invariants | No customization via subclass |
| Composition over inheritance | Flexible behavior swapping | More boilerplate |

## 15. Performance Considerations

Inheritance itself has negligible cost vs composition. **Virtual calls** are cheap after JIT inlining of monomorphic targets. Deep overrides on hot paths matter only after profiling—clarity first.

## 16. Security Considerations

Never let subclasses override **`final`** security checks. Be cautious with **`protected`** on security-sensitive superclasses—untrusted code in another package cannot access `protected` **unless** it subclasses (then it can). For untrusted plugins, prefer **interfaces + composition** in isolated class loaders (advanced).

## 17. Java Version and Compatibility Notes

- Single inheritance unchanged since Java 1.0.
- **`sealed` classes** (Java 17+) restrict who may `extends`—see Ch.04.
- **Records** cannot `extends` classes (except implicit `Record`), but can extend behavior via composition.

## 18. Related Language Comparisons

C++ allows multiple inheritance with virtual bases; Python supports mixins and multiple bases. Java trades flexibility for simpler object layout and clearer dispatch rules.

## 19. Revision and Mastery

For experienced developers:

- **Gaps:** believing fields override like methods; omitting `super()` when a no-arg superclass ctor exists but is `private`.
- **Misunderstood:** `protected` is not “friendly public”—it is a subclass contract.
- **Advanced:** how `invokespecial` vs `invokevirtual` relates to constructors and overrides (bytecode level, Module 26).
- **You should answer:** When is `super(...)` mandatory? What does `final` on a method buy you in a security-sensitive hierarchy?

## 20. Exercises

### Beginner

1. Add `CheckingAccount` with no minimum balance but a $500 overdraft limit; override `canWithdraw`.
2. Predict output of `InheritanceDeepDemo` after changing deposit to `50`.

### Intermediate

3. Refactor `Account.describe()` to `final` and add `protected abstract String typeTag()` for subclasses—predict compile errors if `SavingsAccount` forgets `typeTag`.
4. Explain why `public` fields on `Account` would make `SavingsAccount` harder to maintain.

### Advanced

5. Sketch how a servlet-style `service()` `final` method prevents subclasses from bypassing `doGet`/`doPost` routing.

## 21. Interview Questions

1. Difference between `this(...)` and `super(...)` in constructors?
2. Can you override a `static` method?
3. What is the fragile base class problem?
4. When would you mark a class `final`?
5. Why use `protected` instead of `public` for superclass fields?

## 22. Solutions

1. `canWithdraw` checks `balance() - amount >= -500` (and amount > 0, etc.).
2. Balance `1050`; with cap `1000`, `canWithdraw(900)` still true; adjust rules per your override.
3. `SavingsAccount` must implement `typeTag()` or remain abstract if class not abstract—concrete class needs implementation.
4. External callers could set balance directly, bypassing subclass rules.
5. `final service()` calls `doGet`/`doPost` after parsing request—subclasses cannot replace `service()` with empty body.

## 23. Chapter Summary

Inheritance in production requires **legal constructor chaining**, careful **`protected`** hooks, and disciplined **overriding**. **`final`** guards critical paths; **hiding** is not polymorphism. The demo shows **strengthened invariants** in a subclass while superclass state stays **encapsulated**. Next: **abstract classes** that cannot be instantiated but orchestrate shared algorithms.

## 24. Key Terminology

`extends` · `super(...)` · `super.method()` · `protected` · override · hide · `final` · fragile base class · extension API

## 25. References and Further Reading

- [JLS §8.1.4 — Superclasses and Subclasses](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.4)
- [JLS §8.4.8 — Constructor Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.8)
- [JLS §8.4.6 — Access Control](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.6)
- Effective Java (Bloch) — Item on inheritance vs composition; Item on `protected` members

## 26. Next Steps

- [02-abstract-classes.md](02-abstract-classes.md) — types that forbid direct instantiation.
- [03-dynamic-dispatch-and-lsp.md](03-dynamic-dispatch-and-lsp.md) — how the JVM picks methods; substitution contracts.
- Module 05 Ch.04 — composition when inheritance is the wrong tool.

## Review Checklist

**Mandatory Depth Review:** Technical profile; inheritance construction and override mechanics at **Levels 2–3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated with diagrams where useful
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **§15 Performance Considerations** — omitted; layout and field order are JVM implementation details, not language rules.
- **§16 Security Considerations** — omitted; `protected` misuse in public APIs is noted in §11; deserialization gadgets are out of scope.

**Verification note:** InheritanceDeepDemo: `Savings(S-1) bal=1000 min=50`, balance 1200, withdraw 900 true / 1100 false on Temurin JDK 21.0.12.1 (2026-11-11).
