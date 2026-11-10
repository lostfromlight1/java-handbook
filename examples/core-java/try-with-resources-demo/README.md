# TryWithResourcesDemo

Module 14 Ch.02 — `try` / `catch` / `finally`, then try-with-resources on `AutoCloseable` / `Closeable`.

```bash
javac TryWithResourcesDemo.java
java TryWithResourcesDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
ok-path
finally-ran
catch2=boom
finally2-ran
open=R1
data-R1
close=R1
```

Concepts: `finally` always runs, exception path, resource `close()` after try block, `DemoResource` lifecycle.
