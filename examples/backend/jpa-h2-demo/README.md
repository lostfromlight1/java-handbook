# Example: jpa-h2-demo

Minimal Jakarta Persistence with Hibernate and embedded H2 (no Spring).

```bash
cd examples/backend/jpa-h2-demo
mvn -q compile exec:java
```

Expected: `name=Lin`, `idNotNull=true`, `jpaH2=ok`.

Requires Maven and first-time dependency download.
