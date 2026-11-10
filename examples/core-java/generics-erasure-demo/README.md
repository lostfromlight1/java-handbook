# GenericsErasureDemo

Module 13 Ch.03 — runtime class identity, generic methods, `@SafeVarargs` list builder.

```bash
javac GenericsErasureDemo.java
java GenericsErasureDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
sameRawClass=true
s=hi
i=7
built=[a, b]
```

Concepts: erasure (`getClass()`), `identity`, varargs to `List` (not `T[]`).
