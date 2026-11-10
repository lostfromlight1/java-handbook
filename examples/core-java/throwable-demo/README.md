# ThrowableDemo

Module 14 Ch.01 — `Throwable` hierarchy, unchecked `IllegalArgumentException`, checked `ConfigException`, `instanceof` checks.

```bash
javac ThrowableDemo.java
java ThrowableDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
caughtUnchecked=bad-arg
caughtChecked=missing-file
runtimeIsUnchecked=true
errorIsThrowable=true
```

Concepts: `throw`, `catch`, checked vs unchecked, `RuntimeException` vs `Exception`, `Error` as `Throwable`.
