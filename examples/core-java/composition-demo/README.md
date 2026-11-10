# CompositionDemo

Module 05 Ch.04 — has-a relationships, composition, shared references, simple object graphs.

## Prerequisites

- Classes, constructors, arrays (fixed-size line buffer), `StringBuilder` basics.

## Compile and run

```bash
javac CompositionDemo.java
java CompositionDemo
```

Verified on Temurin JDK 21.0.12.1 (`javac 21.0.12.1`):

```text
Civic with I4 150hp
start -> true
Sherlock @ 12 Baker St, London
person still: Sherlock @ 12 Baker St, London
ORD-1 total=7500 cents [keyboard x2 =7000 cents; cable x1 =500 cents]
```

## Concepts demonstrated

| Idea | Where in code |
|------|----------------|
| Composition (part-of) | `Car` holds `Engine` |
| Delegation | `car.start()` → `engine.start()` |
| Has-a reference | `Person` → `Address` |
| Shared vs rebound reference | `home` variable reassigned; `person` keeps original `Address` |
| Object graph | `Order` → many `OrderLine` |
