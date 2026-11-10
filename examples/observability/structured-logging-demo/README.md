# Structured logging demo

`key=value` log lines without third-party libs (Module 47).

```bash
javac -d out StructuredLoggingDemo.java
java -cp out StructuredLoggingDemo
```

**Verified (Temurin 21.0.12.1):** lines with `event=order.placed`; ends `structured-logging=ok`.
