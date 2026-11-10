# DDD Basics

> **Status:** Completed  
> **Module:** 40 — Backend Architecture  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Define **bounded context**, **ubiquitous language**, **entity**, **value object**, and **aggregate**.
- Choose **aggregate roots** and enforce invariants inside them.
- Distinguish **domain events** from integration events.
- Avoid mapping every table to an anemic entity without boundaries.

## 2. Prerequisites

- Ch.01 — layered/hexagonal architecture.
- Module 15 — records for value objects.

## 3. Why This Concept Exists

Large domains (billing, shipping, catalog) overload a single “User” model with conflicting meanings. **Domain-Driven Design (DDD)** splits the model into **bounded contexts** with explicit language and **aggregates** that maintain consistency per transaction.

## 4. Strategic design

| Concept | Meaning |
|---------|---------|
| **Bounded context** | Model boundary with its own terms (Sales vs Support “Customer”) |
| **Ubiquitous language** | Terms devs and experts share in code (`Shipment`, `Fulfillment`) |
| **Context mapping** | How contexts relate (ACL, shared kernel, events) |

Not every app needs multiple contexts day one—start with clear **modules** and split when friction appears.

## 5. Tactical building blocks

### Value object (immutable)

```java
public record Money(long amountCents, Currency currency) {
    public Money {
        if (amountCents < 0) throw new IllegalArgumentException("negative");
    }
    public Money add(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("currency");
        return new Money(amountCents + other.amountCents, currency);
    }
}
```

Equality by **value**, not id—use records carefully with validation.

### Entity

Object defined by **identity** (`OrderId`) over time, not just attribute values.

### Aggregate and root

Cluster treated as **one consistency boundary**. External references only to **aggregate root** by id.

```java
public class Order { // root
    private final OrderId id;
    private final List<OrderLine> lines = new ArrayList<>();

    public void addLine(ProductRef product, int qty, Money unitPrice) {
        if (qty <= 0) throw new IllegalArgumentException("qty");
        lines.add(new OrderLine(product, qty, unitPrice));
    }

    public void cancel(String reason) {
        if (status == Status.SHIPPED) throw new IllegalStateException("shipped");
        this.status = Status.CANCELLED;
        registerEvent(new OrderCancelled(id, reason));
    }
}
```

`OrderLine` not modified from outside the aggregate.

### Domain events

`OrderCancelled` — something happened domain cares about; may publish to message bus **after** commit (outbox pattern in advanced systems).

## 6. Repositories

One per aggregate root: `OrderRepository.save(order)`, `findById(OrderId)`.

Do not expose generic `save(OrderLine)` bypassing root rules.

## 7. Real-World Use Cases

- E-commerce: `Cart` aggregate vs `Inventory` context via events.
- Banking: `Account` transfer invariant inside aggregate.

## 8. Common Mistakes

### Incorrect — giant aggregate spanning all tables

Large locks, contention—size aggregates to true invariants.

### Incorrect — DDD everywhere on CRUD admin screens

Match pattern to complexity; CRUD can stay thin.

## 9. Relation to JPA

JPA `@Entity` can implement domain entity **if** you resist exposing setters that break invariants—many teams map **domain model** ↔ **JPA entity** in infrastructure (two types).

## 10. Revision and Mastery

- Aggregate root vs entity?
- Why only reference other aggregates by id?

## 11. Exercises

1. Model `Shipment` aggregate with status transitions.
2. Find bounded contexts in a food delivery app.
3. List domain events for order lifecycle.

## 12. Chapter Summary

DDD organizes complex business into contexts, value objects, and aggregates with enforced invariants—implemented in plain Java; persistence and HTTP are adapters.

## 13. Next Steps

[03-modular-monolith-versus-microservices.md](03-modular-monolith-versus-microservices.md)

## Review Checklist

- [x] Strategic/tactical terms; aggregate example; JPA note
