# Kafka and RabbitMQ

> **Status:** Completed  
> **Module:** 45 — Messaging and Event-Driven Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Compare **Apache Kafka** and **RabbitMQ** architectures and when each fits.
- Understand **consumer groups**, **exchanges/queues**, and **retention**—conceptually without requiring local install.
- Map Java clients: **Kafka `KafkaConsumer`**, Spring Kafka, **AMQP** via Rabbit template.
- Plan **operational** concerns: monitoring, topic design, access control.

## 2. Prerequisites

- Ch.01 messaging fundamentals.
- Module 44 — partitioning and ordering.

## 3. Why This Concept Exists

Brokers differ: Kafka is a **distributed commit log**; RabbitMQ is a **message broker** with flexible routing. Picking wrong tool creates operational pain.

## 4. Kafka (conceptual architecture)

- **Topic** split into **partitions** (ordered logs).
- **Producers** append records with key (optional) for partition affinity.
- **Consumer groups:** each partition consumed by one consumer instance in group.
- **Retention:** messages kept by time/size—replay possible.

**Strengths:** high throughput, replay, stream processing (Kafka Streams conceptually).

**Java:** `org.apache.kafka:kafka-clients` — not bundled in JDK; add dependency in real projects.

```java
// Conceptual producer shape (requires kafka-clients on classpath)
// props: bootstrap.servers, key.serializer, value.serializer
// new KafkaProducer<>(props).send(new ProducerRecord<>("orders", orderId, json));
```

**When to choose:** event streams, audit log, many consumers reading history, Analytics pipelines.

## 5. RabbitMQ (conceptual architecture)

- **Exchanges** route to **queues** via bindings (direct, topic, fanout).
- **Acknowledgments** (`basicAck`) confirm processing.
- Messages **removed** after ack (default)—not a long replay log like Kafka.

**Strengths:** flexible routing, classic task queues, per-message TTL, priority queues.

**Java:** `com.rabbitmq:amqp-client` or Spring AMQP.

```java
// Conceptual: channel.basicPublish("orders.exchange", "order.placed", props, body);
```

**When to choose:** work queues, RPC-style patterns, complex routing rules.

## 6. Comparison table

| Dimension | Kafka | RabbitMQ |
|-----------|-------|----------|
| Model | Log / stream | Broker / queue |
| Replay | Native | Not default |
| Routing | Topic + key | Exchanges |
| Ordering | Per partition | Per queue |
| Ops profile | Cluster + ZooKeeper/KRaft | Erlang cluster |

Hybrid architectures exist: Kafka for events, Rabbit for tasks.

## 7. Spring integration (awareness)

- `@KafkaListener(topics = "orders")` — method consumes deserialized payload.
- `@RabbitListener(queues = "order-email")` — AMQP consumer.

Configure **error handlers**, **retry**, **DLQ** in Spring—not only annotations.

## 8. Security

- **TLS** and **SASL** for broker connections (Module 43).
- **ACLs** per topic/queue; separate credentials per service.

## 9. Real-World Use Cases

- Kafka: `clickstream`, `OrderPlaced` domain event bus.
- Rabbit: email worker pool, delayed retries with TTL + DLX.

## 10. Edge Cases

- **Hot partitions:** skewed keys—salting or re-key.
- **Rebalance storm:** cooperative sticky assignors in Kafka consumers.

## 11. Common Mistakes

| Mistake | Fix |
|---------|-----|
| Kafka as job queue without compaction policy | Use Rabbit or tune retention |
| Giant messages in Kafka | Store blob in object store; message has URL |
| No consumer idempotency | Module 44 idempotency store |

## 12. This handbook without broker install

Chapters remain **conceptual** for broker-specific APIs. Validate designs with diagrams and tests using **in-memory fakes** (Ch.01 port pattern). Install Kafka/Rabbit locally when running integration labs.

## 13. Revision and Mastery

- Replay scenario—Kafka or Rabbit?
- Consumer group purpose?
- Exchange type for broadcast notifications?

## 14. Exercises

1. Design topic/partition count for 10k orders/min.
2. Sketch Rabbit exchange bindings for fraud + email subscribers.
3. List metrics to alert on (lag, DLQ depth).

## 15. Chapter Summary

Kafka favors durable ordered logs and replay; RabbitMQ favors routed queues and task distribution—both integrate Java via client libraries and Spring adapters behind hexagonal ports.

## 16. Next Steps

[03-delivery-guarantees-and-outbox.md](03-delivery-guarantees-and-outbox.md)

## 5. Mechanism and Execution Flow *(mandatory)*

**Kafka record path:** producer chooses partition by key → broker appends log → consumer group assigns partitions → offset commit after processing.

**Rabbit path:** publisher → exchange → binding → queue → consumer ack → message removed.

Client code examples remain **Illustrative** (require broker JARs and running cluster).

## 21. FAQ — Broker choice *(mandatory depth)*

**Q: Kafka strength?** Replay, throughput, stream processing.

**Q: Rabbit strength?** Routing patterns, classic work queues.

**Q: Ordering guarantee?** Per partition (Kafka) or single consumer per queue.

**Q: Ops cost?** Kafka cluster footprint vs Rabbit HA pair.

**Q: Spring Kafka listener?** Illustrative adapter—test handler without broker using port fake.


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
- [x] Compared brokers; conceptual Java; no fabricated broker run output
