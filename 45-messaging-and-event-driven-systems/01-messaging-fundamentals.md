# Messaging Fundamentals

> **Status:** Completed  
> **Module:** 45 — Messaging and Event-Driven Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Distinguish **message queue**, **topic**, **event**, and **command**.
- Compare **point-to-point** vs **pub/sub** semantics.
- Map messaging to **hexagonal adapters** (Module 40).
- Understand **brokers** as external infrastructure (conceptual when not installed).

## 2. Prerequisites

- Module 44 — distributed fundamentals.
- Module 14 — concurrency.

## 3. Why This Concept Exists

**Asynchronous messaging** decouples producers and consumers in time and scale. Order placement returns quickly while email, analytics, and inventory catch up via messages.

## 4. Conceptual Explanation

| Term | Meaning |
|------|---------|
| **Producer** | Publishes a message |
| **Consumer** | Processes a message |
| **Broker** | Stores and routes (Kafka, RabbitMQ, etc.) |
| **Topic/queue** | Named channel |
| **Partition** | Ordered sub-log within topic (Kafka) |
| **Consumer group** | Cooperating consumers sharing load |

**Command message:** one consumer should handle (`ChargePayment`).
**Event message:** something happened (`OrderPlaced`); many subscribers may react.

## 5. Point-to-point vs pub/sub

| Model | Routing | Use |
|-------|---------|-----|
| **Queue** | One consumer per message (competing consumers) | Work distribution |
| **Topic** | All subscribers receive copy | Notifications, audit |

Java integration: **JMS** (javax.jakarta.jms) API; Spring `JmsTemplate` / `@KafkaListener` as adapters.

## 6. Message shape in Java

Prefer **versioned schema** (JSON with schema registry, Avro, or protobuf):

```java
public record OrderPlacedEvent(String eventId, String orderId, Instant occurredAt) {}
```

Include **metadata**: `correlationId`, `causationId`, `tenantId` for tracing (Module 47).

## 7. Adapter pattern

```java
public interface OrderEventPublisher {
    void publish(OrderPlacedEvent event);
}

// KafkaOrderEventPublisher implements adapter — broker is infrastructure
```

Domain depends on port; broker client is adapter.

## 8. Real-World Use Cases

- **Order placed** → email, warehouse, fraud scoring.
- **CDC** (change data capture) streams DB rows to search index.
- **Dead letter** queue for poison messages.

## 9. Edge Cases

- **Ordering:** global order impossible at scale; partition by key.
- **Poison message:** failing deserialization—DLQ after N attempts.

## 10. Common Mistakes

| Mistake | Fix |
|---------|-----|
| Giant JSON blob without version | `schemaVersion` field + migrations |
| Business logic in listener only | Core handler testable without broker |
| No correlation id | Propagate from HTTP header |

## 11. Performance

Batching producer sends; consumer **prefetch** tuning; avoid massive messages—reference by id.

## 12. Revision and Mastery

- Command vs event example?
- Why partition by `orderId`?
- Where does hexagonal port sit?

## 13. Exercises

1. Design events for order lifecycle.
2. Sketch adapter interface for in-memory test broker fake.
3. List metadata fields for cross-service trace.

## 14. Chapter Summary

Messaging fundamentals separate commands and events, use brokers as infrastructure adapters, and prepare for delivery guarantees in later chapters.

## 15. Next Steps

[02-kafka-and-rabbitmq.md](02-kafka-and-rabbitmq.md)

## Review Checklist

- [x] Terminology; adapters; conceptual broker note
