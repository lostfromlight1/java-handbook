# Circuit breaker demo

Plain-Java **CLOSED / OPEN / HALF_OPEN** sketch (Module 44).

```bash
javac -d out *.java
java -cp out CircuitBreakerDemo
```

**Verified (Temurin 21.0.12.1):** lines include `fail state=OPEN`, `fast state=OPEN reason=fail-fast`, ends with `circuit-breaker=ok`.
