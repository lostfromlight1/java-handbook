# Delivery Guarantees and Outbox

> **Status:** Completed  
> **Module:** 45 — Messaging and Event-Driven Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Explain **at-most-once**, **at-least-once**, and **effectively-once** semantics.
- Implement **transactional outbox** pattern with Java/JPA mental model.
- Use **idempotent consumers** and **deduplication** (Module 44).
- Recognize **exactly-once** broker claims vs application responsibility.

## 2. Prerequisites

- Chapters 01–02; Module 33 transactions; Module 44 sagas.

## 3. Why This Concept Exists

Network and process failures mean messages may be **lost** or **duplicated**. Business invariants require knowing which guarantee you actually have—usually **at-least-once + idempotent handler**, not magic exactly-once everywhere.

## 4. Delivery semantics

| Guarantee | Behavior | Typical approach |
|-----------|----------|------------------|
| **At-most-once** | May lose message, no duplicate | Fire-and-forget; metrics only |
| **At-least-once** | No loss; duplicates possible | Ack after process + retry; idempotency |
| **Effectively-once** | Duplicates invisible to business | Idempotency + dedup store |

Brokers may offer **exactly-once** within narrow broker-transaction scopes (Kafka EOS with streams)—application side still needs correct handlers.

## 5. Producer pitfalls

**Dual write problem:**

```text
1. DB commit order
2. publish to Kafka
```

If JVM dies between 1 and 2, event never published—downstream stale.

If publish then DB rolls back—**phantom event**.

## 6. Transactional outbox

**Pattern:** in one **database transaction**, write business rows and an **outbox** row; a separate **relay** publishes to the broker and marks the row sent.

```java
@Transactional
public void placeOrder(Order order) {
    orders.save(order);
    outbox.insert(new OutboxRow("OrderPlaced", serialize(order)));
}
```

Relay process (polling or log-based CDC):

```text
SELECT * FROM outbox WHERE sent_at IS NULL FOR UPDATE SKIP LOCKED
→ publish to broker → UPDATE sent_at = now()
```

**Benefits:** atomicity with domain data; at-least-once publish.

### Incorrect — publish in controller before transaction commits

Race for consumers reading uncommitted data.

## 7. Consumer idempotency

Unique constraint on `processed_event_id`:

```sql
CREATE UNIQUE INDEX ux_processed ON inbox(event_id);
```

Handler:

```java
@Transactional
public void handle(OrderPlacedEvent e) {
    if (inbox.alreadyProcessed(e.eventId())) return;
    inventory.reserve(e.orderId());
    inbox.markProcessed(e.eventId());
}
```

## 8. Ordering and duplicates

- **Ordering:** per-partition (Kafka) or single consumer per queue.
- **Duplicates:** expect them; design for idempotency.

## 9. Dead letter queue (DLQ)

After max retries, move message to DLQ for manual triage—alert on depth (Module 47).

## 10. Real-World Use Cases

- E-commerce outbox for `OrderPlaced` → search + warehouse.
- Payment webhooks: inbox table for provider event ids.
- Module 44 saga steps driven by outbox relay.

## 11. Edge Cases

- **Relay lag:** monitors on oldest unsent outbox row age.
- **Poison payload:** schema mismatch—DLQ + schema versioning.

## 12. Revision and Mastery

- Why dual write fails?
- Outbox vs change data capture relay?
- At-least-once + idempotency = ?

## 13. Exercises

1. DDL for `outbox` and `inbox` tables.
2. Sequence diagram: place order with outbox relay.
3. Define retry and DLQ policy for consumer.

## 14. Chapter Summary

Reliable Java event-driven systems use transactional outbox, at-least-once delivery, idempotent consumers, and DLQ operations—not hope that HTTP and messaging share one transaction.

## Review Checklist

- [x] Semantics table; outbox flow; idempotent consumer pattern
