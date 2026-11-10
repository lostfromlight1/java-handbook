# CustomExceptionsDemo

Module 14 Ch.03 — checked `InsufficientFundsException` with domain field `shortfall`, `AccountService.withdraw`.

```bash
javac CustomExceptionsDemo.java
java CustomExceptionsDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
balance=60
error=need 40 more
shortfall=40
```

Concepts: custom exception message, extra state on exception type, transaction stopping mid-`try` on failure.
