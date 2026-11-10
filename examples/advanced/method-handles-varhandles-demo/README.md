# Method handles and VarHandle demo

Invokes a static method via `MethodHandle` and updates a field via `VarHandle` (volatile semantics).

```bash
javac MethodHandlesVarHandlesDemo.java
java MethodHandlesVarHandlesDemo
```

**Verified (Temurin 21.0.12.1):** `fromHandle=Hello, Ada`; `counter=1`; `methodHandlesDemo=ok`
