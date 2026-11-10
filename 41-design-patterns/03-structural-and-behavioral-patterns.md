# Structural and Behavioral Patterns

> **Status:** Completed  
> **Module:** 41 — Design Patterns  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [structural-behavioral-demo](../../examples/patterns/structural-behavioral-demo/)

## 1. Learning Objectives

- Use **Adapter**, **Decorator**, **Facade**, **Proxy** for structure.
- Apply **Strategy**, **Observer**, **Template Method**, **Command** for behavior.
- Relate **Proxy** to Spring AOP and **Strategy** to `PricingStrategy` in demo.
- Avoid pattern obsession—name patterns you already use.

## 2. Prerequisites

- Ch.01–02 — SOLID, creational patterns.
- Module 36 Ch.02 — JDK proxies.

## 3. Why This Concept Exists

Integrations expose legacy APIs; behavior varies by policy; events fan out to listeners. **Structural** patterns compose objects; **behavioral** patterns encapsulate algorithms and notifications—often as **interfaces + delegation** in Java.

## 4. Structural highlights

| Pattern | Role |
|---------|------|
| **Adapter** | Make `LegacyApi` implement `ModernClient` |
| **Decorator** | Wrap object adding behavior (I/O streams, `Collections.checkedList`) |
| **Facade** | Simplify subsystem (`OrderFacade` coordinates services) |
| **Proxy** | Control access; lazy load; AOP (Module 36) |

### Adapter (demo)

```java
ModernClient client = new LegacyAdapter(new LegacyApi());
```

## 5. Behavioral highlights

| Pattern | Role |
|---------|------|
| **Strategy** | Pluggable algorithm (`PricingStrategy`) |
| **Observer** | Publish/subscribe (`EventBus`) |
| **Template Method** | Algorithm skeleton in superclass (Module 07) |
| **Command** | Encapsulate request as object (`Runnable`, job queues) |

### Strategy + Observer (demo)

**Verified output:**

```text
adapter=v2:data
strategy=discount10
observer=events:2
patterns=ok
```

## 6. JDK and framework examples

- **Proxy:** `java.lang.reflect.Proxy`, Spring AOP.
- **Observer:** `PropertyChangeListener`, reactive streams.
- **Iterator:** `Iterable`/`Iterator` (behavioral).
- **Template method:** `AbstractList`, servlet `doGet`/`doPost` hooks.

## 7. Real-World Use Cases

- Adapter for third-party payment SDK behind `PaymentPort`.
- Strategy for tax calculation by region.
- Observer for domain events → metrics/listeners.

## 8. Common Mistakes

### Incorrect — pattern name without need

Start with interface + delegation; name pattern in review.

### Incorrect — decorator chain without clear ordering

Document wrap order for metrics vs retry.

## 9. Design Trade-offs

| Inheritance template method | Strategy composition |
|-----------------------------|----------------------|
| Fixed skeleton | Runtime algorithm swap |

Prefer composition (Module 05).

## 10. Revision and Mastery

- Adapter vs Facade?
- How is Spring MVC interceptor related to decorator/chain?

## 11. Exercises

1. Wrap demo `Greeter` with timing decorator without AOP.
2. Implement Command for undoable text edit (sketch).
3. Map `HttpClient` redirect policy to strategy.

## 12. Chapter Summary

Structural and behavioral patterns formalize composition and variation already idiomatic in Java—adapters, strategies, proxies, and observers underpin Spring and clean architecture.

## 13. Next Steps

Module **42** (per PROGRESS)—advanced topics track.

## Review Checklist

- [x] Verified structural-behavioral-demo; JDK/Spring ties
