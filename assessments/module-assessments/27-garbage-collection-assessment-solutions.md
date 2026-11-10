# Solutions: Module 27 — Garbage Collection

1. Unreachable from GC roots.
2. G1 (`UseG1GC=true` ergonomic).
3. G1 Young Generation; G1 Concurrent GC; G1 Old Generation.
4. Hint only; not reliable production control.
5. `-XX:+UseZGC` (among others).
6. No — measure.
7. Unintended strong references keeping objects alive.
8. Max heap size.
9. Stack refs, statics, JNI, etc.
10. No — implementation choice.

11. `3`
12. `1`
