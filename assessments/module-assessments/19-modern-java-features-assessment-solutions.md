# Solutions: Module 19 — Modern Java Features

## Part A

1. JVM-managed lightweight virtual thread (mounted on carrier platform threads).
2. Many concurrent **blocking** tasks (e.g. per-request work).
3. Examples: `getFirst`, `getLast`, `addFirst`, `addLast`, `reversed`.
4. A **record** type with matching components.
5. **Java 21** LTS.
6. **21**.
7. **Awareness** / version notes—not main curriculum default.
8. `javac --enable-preview --release <N>`; run with `java --enable-preview`.
9. **No** — `--release` is a compiler option; use matching JDK + `--enable-preview` on `java`.
10. **Preview** (Structured Concurrency / JEP 453 in 21—re-check JEP when upgrading).

## Part B

11. `vt-ready`
12. `3+4=7` (segment `7`)
13. `2`

## Part C

14. `scoped-result`

## Part D

15. When semantics may change, flags are required, or support/IDE tooling is insufficient—use spikes only.
16. **Poor fit** — use platform pool sized to cores; virtual threads target blocking I/O.
