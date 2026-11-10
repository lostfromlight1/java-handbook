# Solutions: Module 26 — JVM Internals

1. JVMS = spec; HotSpot = one implementing product JVM.
2. Link (verify/prepare/resolve) and initialize (after load).
3. Bootstrap loader (`null` for core classes).
4. `iadd` after `iload` operands.
5. Stack (JVM stack per thread).
6. Typically excessive stack depth (e.g. unbounded recursion).
7. Disassembled class file bytecode (implementation tool).
8. MXBean API yes for standard beans; `jcmd` HotSpot/JDK specific.
9. `null` ClassLoader reference.
10. String concatenation / bootstrap methods (Java 11+).

11. `42`
12. `100`
