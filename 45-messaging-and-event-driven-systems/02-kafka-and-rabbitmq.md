# Kafka and RabbitMQ

> **Status:** Completed  
> **Module:** 45 — Messaging and Event-Driven Systems  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

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

## Review Checklist

- [x] Compared brokers; conceptual Java; no fabricated broker run output
