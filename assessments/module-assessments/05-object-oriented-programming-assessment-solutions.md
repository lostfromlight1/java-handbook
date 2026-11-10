# Solutions: Module 05 — Object-Oriented Programming Assessment

## Part A

1. A **class** is the blueprint/type; an **object** is a concrete instance created at runtime (usually with `new`).
2. **State** = data in fields; **behavior** = methods that operate on that state (or related logic).
3. Allocates a `BankAccount` object, runs its constructor with those arguments, returns a reference.
4. Encapsulation hides representation and forces changes through controlled methods so invariants (e.g. non-negative balance) can be enforced.
5. Encapsulation is about **access control / information hiding**. Abstraction is about exposing a **useful interface** while hiding **how** it is implemented. You often use both together.
6. `extends` creates an inheritance (is-a) relationship—e.g. `Manager extends Employee`.
7. **Overriding:** same instance method signature in a subclass, specializing behavior. **Overloading:** same name, different parameter lists (often in one class).
8. Compile-time type is `Employee`; runtime object is `Manager`; the JVM invokes `Manager.annualPay()`.
9. Composition when modeling parts/collaborators (has-a) or to avoid brittle hierarchies. Inheritance when a true specialization needs shared type identity and polymorphic substitution.
10. Has-a: `Car` has an `Engine`. Is-a: `Manager` is an `Employee`.

## Part B

11. No—different objects → `==` false (unless same reference).
12. Yes—same reference.
13. `77.0` (25×9/5+32).
14. `60000` (50000+10000).
15. `65000` (60000+5000).
16. `7500`.

## Part C

17. Make the field `private`; mutate only via methods that validate.
18. Compile error: subclass must invoke an accessible superclass constructor.
19. It becomes a new method, not an override; polymorphic calls still hit the superclass version. `@Override` would have caught it.
20. A car is not a kind of engine; it **has** an engine—use composition.

## Part D

21–24. Accept any clear solution matching the requirements (private fields, validation where asked, override/`Animal` reference, playlist ownership of titles).

## Part E

25. `==` compares references (identity). Equal balances can still be two accounts. Module 06 covers `equals`/`hashCode` for value equality.
26. Changing `Employee.annualPay` can silently break every subclass that depended on the old base behavior or assumptions.
27. Example: `Order` entity; composition `Order` has many `OrderLine`s (or `Customer` has an `Address`).
