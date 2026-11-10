# InheritancePolyDemo

Module 05 Ch.03 — `extends`, `super`, `@Override`, polymorphic calls, `instanceof`.

## Prerequisites

- Classes, constructors, instance methods (Module 05 Ch.01–02 or Module 04 Ch.04).

## Compile and run

```bash
javac InheritancePolyDemo.java
java InheritancePolyDemo
```

Verified on Temurin JDK 21.0.12.1 (`javac 21.0.12.1`):

```text
Employee(Sam) pay=50000
Manager(Riley) pay=60000
Employee(Jordan) pay=40000
runtime type Manager? true
polymorphic pay=65000
```

## Concepts demonstrated

| Idea | Where in code |
|------|----------------|
| `extends` / is-a | `Manager extends Employee` |
| `super(...)` constructor chain | `Manager` constructor |
| Method overriding | `annualPay`, `label` in `Manager` |
| Polymorphism | `Employee[]` holding `Manager`; `asEmployee.annualPay()` |
| `instanceof` | `asEmployee instanceof Manager` |
