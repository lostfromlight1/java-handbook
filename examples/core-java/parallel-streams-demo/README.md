# ParallelStreamsDemo

Module 17 Ch.04 — `parallelStream`, shared-mutation hazard.

```bash
javac ParallelStreamsDemo.java
java ParallelStreamsDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
sequential=[10, 20, 30, 40, 50]
parallel=[10, 20, 30, 40, 50]
sideEffect=5
badSize=5
```

Concepts: same numeric result for pure `map`; **do not** mutate shared `ArrayList` from parallel `forEach` (data races / lost updates on larger data).
