# SealedClassesDemo

Module 07 Ch.04 — `sealed interface`, permitted `record` implementations, exhaustive `switch` with pattern matching.

```bash
javac SealedClassesDemo.java
java SealedClassesDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
ok:42
err:boom
area=12.566370614359172
```

Concepts: `sealed interface Result permits Ok, Err`, record carriers, `switch` expressions on sealed types (no `default` needed when exhaustively covered), second sealed hierarchy `Shape2D` with `Circle` area using `Math.PI`.
