# Solutions: Module 31

1. IDE/debugger attaches to JVM (Java Debug Wire Protocol).
2. Step over runs current line without entering callee; step into enters callee.
3. `Thread.print` (e.g. `jcmd <pid> Thread.print`).
4. `GC.heap_dump <path>` (HotSpot jcmd).
5. `.jfr`
6. Large files, may contain sensitive data in objects.
7. Detect (or mitigate depending on variant — detect is first in handbook loop).
8. Thread dump / JFR CPU samples / profiling.
9. Cannot hot-swap method signature changes; need restart.
10. Avoid stopping every iteration in tight loops.

11. `count=1`
12. `threadCount=7` on validation host (your count may differ slightly).
