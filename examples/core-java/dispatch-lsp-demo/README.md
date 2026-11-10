# DispatchLspDemo

Module 07 Ch.03 — virtual method dispatch on `Shape[]`, LSP-friendly `Square` (not a mutable `Rectangle` subclass).

```bash
javac DispatchLspDemo.java
java DispatchLspDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
Rectangle area=12
Square area=25
poly area=4
```

Concepts: compile-time type `Shape`, runtime types `Rectangle` / `Square`, overridden `area()` selected at call time, polymorphic reference `Shape squareAsShape`.
