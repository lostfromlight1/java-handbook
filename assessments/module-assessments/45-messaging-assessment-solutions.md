# Solutions: Module 45 — Messaging and Event-Driven Systems

1. Command: one handler should act; event: something happened, many subscribers may react.
2. Consumers cooperate; each partition assigned to one consumer in group for scale.
3. Broadcast same message to multiple queues/subscribers (notifications).
4. DB commit and broker publish not atomic—events or rows can diverge.
5. Write business + outbox row in one TX; relay publishes and marks sent.
6. Idempotent consumer / deduplication store.
7. Hold poison/failed messages for manual analysis after retry exhaustion.
8. Order-related events keyed by `orderId` for per-order ordering.
9. `OrderEventPublisher` in core; Kafka adapter in infrastructure.
10. Kafka retains log for replay; Rabbit default removes messages after ack.

11. Tie distributed logs/traces to one user request.
12. Evolve payload without breaking all consumers at once.
