# Assessment: Module 20 — Java Standard Library

> **Maps to:** Module 20 chapters 01–03  
> **Solutions:** [20-java-standard-library-assessment-solutions.md](20-java-standard-library-assessment-solutions.md)

## Part A — Conceptual

1. `List.of` — mutable or immutable? Null elements allowed?
2. `String.strip()` vs `trim()` — Unicode awareness?
3. Package for `Path` and `Files`?
4. `Instant` models what kind of time?
5. `@Retention(RetentionPolicy.RUNTIME)` — when visible?
6. `ProcessBuilder` vs `Runtime.exec(String)` security preference?
7. `ByteBuffer.position()` after `putInt` in demo — why 4?
8. Module for deep `java.time`?
9. NIO.2 preferred over `java.io.File` for new code?
10. Reflection `getMethod` requires what for invocation?

## Part B — Predict (JavaLangUtilDemo)

11. `first=`?
12. `join=`?

## Part C — Predict (ReflectionAnnotationsProcessDemo)

13. `reflectInvoke=`?
14. `processArgs=` first element?

## Part D — Design

15. Field type `ArrayList` vs `List` in public API?
16. Run untrusted shell from user input in ProcessBuilder?

## Scoring

| Score | Next focus |
|-------|------------|
| 85%+ | Module 21 file I/O |
| 60–84% | Ch.01 collections + Ch.03 reflection |
| &lt;60% | Module 12 collections refresh |
