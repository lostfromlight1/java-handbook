# Delivery Guarantees and Outbox

> **Status:** Completed  
> **Module:** 45 — Messaging and Event-Driven Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

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

## 5. Mechanism and Execution Flow *(mandatory)*

**Outbox relay:**

```text
@Transactional placeOrder
  INSERT orders
  INSERT outbox (status=PENDING)
COMMIT
Relay polls outbox → publish → mark SENT
```

Consumer **at-least-once** → handler checks `processed_message_id` table before side effects.

## 21. FAQ — Delivery semantics *(mandatory depth)*

**Q: Exactly-once illusion?** Application idempotency achieves effective-once.

**Q: Outbox vs dual write?** Outbox avoids JVM crash gap.

**Q: CDC relay?** Debezium reads binlog instead of polling—Illustrative infra.

**Q: Kafka EOS?** Narrow scope—handlers still idempotent.

**Q: Ordering across aggregates?** Partition by business key.


## Depth appendix — progressive teaching (Messaging)

### Incorrect vs corrected patterns

Documenting failure modes accelerates review. Each pair below states **why** the wrong approach breaks client or operator expectations.

### Operational checklist before marking API complete

- OpenAPI matches deployed routes (Ch.03).
- Error envelope consistent (ProblemDetail or documented custom schema).
- AuthN/Z enforced on mutations (Module 39).
- Examples in `examples/` compiled on Temurin 21.0.12.1 with recorded output.
- Versioning/deprecation policy published for partners.

### Revision drills (Track B)

1. Explain one design trade-off you reversed after load testing.
2. Write three MockMvc or plain HTTP negative tests not in the happy path.
3. Identify one boundary where DTO mapping must never leak entities.

### References (official hierarchy)

- RFC 9110 — HTTP semantics (methods, status).
- Jakarta EE / Spring reference docs for Illustrative framework binding only.
- OWASP ASVS — verification levels for security chapters (Module 43).

**Mechanism merge note:** Where earlier sections already diagram execution flow, this appendix reinforces **operational** depth without duplicating definitions.

### Message envelope template

```json
{
  "schemaVersion": 1,
  "eventId": "uuid",
  "occurredAt": "2026-11-11T00:00:00Z",
  "correlationId": "uuid",
  "payload": { }
}
```

### Handler idempotency store

```sql
CREATE TABLE processed_events (idempotency_key VARCHAR PRIMARY KEY, processed_at TIMESTAMP);
```

Insert before side effects; duplicate delivery becomes no-op.

### Broker operations

Monitor consumer lag, DLQ depth, disk usage on Kafka brokers; Rabbit queue depth and unacked messages.

## Technical L3 supplement — teaching depth

### Motivation recap (concrete scenario)

A team ships a Java 21 service without documented HTTP contracts, security rules, or messaging guarantees. Mobile clients cache error shapes; a deploy renames a field and checkout silently fails. Operations cannot trace requests across services. The material in this module prevents that class of failure by making **boundaries explicit**: REST semantics, DTOs, auth filters, architecture ports, patterns, clean code, security controls, resilience, and delivery semantics.

### Mechanism trace (how this chapter shows up at runtime)

1. **Ingress** — HTTP or message enters an adapter (controller, listener).
2. **Policy** — auth, validation, and rate limits run before domain logic (fail closed).
3. **Application** — use case coordinates domain + ports inside a transaction or saga step.
4. **Egress** — DTO mapping, event publish (outbox), or outbound HttpClient with timeouts.
5. **Observation** — structured logs with correlation id; metrics on error rate (Module 47).

This sequence is framework-agnostic; Spring/Boot annotations are **Illustrative** wiring for the same pipeline taught in earlier modules.

### Progressive example ladder

| Stage | Focus | Verification label |
|-------|--------|-------------------|
| Minimal | JDK-only demo or pure unit logic | Verified / Compile-verified |
| Realistic | Layered service with DTO + port | Illustrative Spring config |
| Advanced | Failure modes: timeout, duplicate message, 409 conflict | Test + documented output |

Never label **Verified** without Temurin 21.0.12.1 compile/run recorded in chapter or `PROGRESS.md`.

### Wrong vs right (review board)

| Wrong | Why it hurts | Right |
|-------|----------------|-------|
| Skip server AuthZ | IDOR incidents | Check principal vs resource every mutation |
| Expose JPA graph in JSON | Lazy-load storms, schema coupling | Map to DTO in service |
| Infinite HttpClient wait | Thread pile-up, incident duration | Connect + request timeouts |
| Retry all POSTs | Duplicate charges | Idempotency keys + dedup store |
| Dual write DB + broker | Lost or phantom events | Transactional outbox |

### Design tensions engineers negotiate

- **Consistency vs availability** during partitions (Module 44).
- **Stateless JWT vs revocable sessions** (Module 39).
- **Modular monolith vs microservices** operational cost (Module 40).
- **Kafka vs Rabbit** retention and routing (Module 45).
- **Strict SOLID vs delivery speed** (Modules 41–42).

Document the choice in ADRs; the handbook gives options, not one universal answer.

### Java 21 notes

- **Records** for immutable DTOs and events (`Module 08`, `15`).
- **Virtual threads** improve blocking HTTP and JDBC throughput but do not remove timeouts or auth checks.
- **Pattern matching** and **sealed types** clarify domain result models (`PaymentResult` in SOLID chapters).

### Compatibility and sources

Prefer **RFC 9110** for HTTP, **RFC 7807** for errors, **OWASP ASVS** for verification language, and vendor docs for brokers labeled Illustrative. Distinguish **JLS/API guarantees** from **Spring/HotSpot** behavior as in `DOCUMENTATION-STANDARDS.md`.

### Track B — mastery questions

1. Where is the transaction boundary in your current project, and why?
2. Which dependency would you invert first behind a port for testing?
3. What status code and error shape would clients see for your most common validation failure?
4. How would you prove an idempotent consumer in tests?
5. What metric would fire before users notice a downstream slowdown?

### Track B — field exercise (no solution in chapter)

Implement or extend one `examples/` demo for this module: add a negative-path assertion (4xx, breaker OPEN, or rejected input), compile with `javac -source 21`, run on Temurin 21.0.12.1, paste output into `PROGRESS.md` only after a real run.

### Cross-module links

| Need | Module |
|------|--------|
| HTTP client timeouts | 32 |
| JDBC transactions | 33–35 |
| Spring DI / Security | 36–37 |
| Testing & CI | 30 |
| Observability | 47 |

### Glossary touchpoints

Add unfamiliar terms encountered here to `GLOSSARY.md` when stabilizing definitions across the handbook (repository workflow in java-documentation skill).

### Explicit omissions (this supplement)

- Vendor-specific console screenshots and cloud provisioning scripts.
- Exploit reproduction steps (defensive security only in Module 43).
- Exhaustive broker tuning guides (operational manuals remain external).

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Semantics table; outbox flow; idempotent consumer pattern
