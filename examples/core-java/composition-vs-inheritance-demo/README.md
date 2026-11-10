# CompositionVsInheritanceDemo

Module 09 Ch.03 — `Car` composes `Engine`; `AuditedService` composes `Logger`.

```bash
javac CompositionVsInheritanceDemo.java
java CompositionVsInheritanceDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
Sedan / 180hp
start=true
log:begin save
log:end save
done:save
```

Concepts: delegation; interface `Logger`; avoid `Car extends Engine`.
