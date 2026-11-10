# InheritanceDeepDemo

Module 07 Ch.01 — `extends`, `super(...)`, `protected` accessors, `final` superclass methods, overriding with strengthened invariants.

```bash
javac InheritanceDeepDemo.java
java InheritanceDeepDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
Savings(S-1) bal=1000 min=50
after deposit balance=1200
canWithdraw(900)=true
canWithdraw(1100)=false
```

Concepts: subclass constructor chains to `Account` via `super`, `protected` helpers instead of exposing private fields, `@Override` on `canWithdraw` and `describe`, combined business rules (per-transaction cap + minimum balance).
