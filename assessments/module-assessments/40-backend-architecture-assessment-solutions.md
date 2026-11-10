# Solutions: Module 40 — Backend Architecture

1. Port is application-needed interface; adapter is technology implementation (JPA, HTTP client).
2. Logic only in services; entities are data bags without behavior.
3. External references only by root id; invariants enforced inside aggregate.
4. Sales “Customer” vs Support “Customer” with different models/terms.
5. Monolith: local ACID; microservices: sagas/eventual consistency often required.
6. Many deployables sharing one tightly coupled database with chatty calls.
7. Application service layer (use case), not controller or repository implementation.
8. Value object equality by attributes immutable; entity identity persists over time.
9. Incrementally replace legacy by routing traffic to new module/service.
10. Business capabilities / bounded contexts, not technical layers only.
