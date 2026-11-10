# Solutions: Module 50

1. **RUNTIME** visible to reflection at runtime; **SOURCE** discarded after compile (e.g. `@Override`).
2. Target must implement **interface(s)** proxied.
3. Real exception is **cause** inside target method; outer exception is wrapper.
4. Stricter typing, often better performance; invokedynamic/lambda linkage.
5. Volatile field write/read semantics without `synchronized`.
6. Memory freed when arena **closed**; confined to creating thread.
7. `javac --enable-preview --release 21`; `java --enable-preview`.
8. Names class whose `premain` runs before `main` when using `-javaagent`.
9. Bytecode verify rules, module opens, transformer incompatibility with new class file version.
10. Standard JVMTI agents target HotSpot; native image uses Substrate VM—agents generally incompatible.

11. `reflectionDemo=ok`
12. `methodHandlesDemo=ok`
13. `ffmDemo=ok`
14. `agentDemo=ok`
