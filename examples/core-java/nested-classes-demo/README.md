# NestedClassesDemo

Module 06 Ch.02 — static nested, inner, local, and anonymous classes.

```bash
javac NestedClassesDemo.java
java NestedClassesDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
StaticNested:static-nested
Inner sees outer-1
Hello Ada from outer-1
anon from outer-1
```

Concepts: `new Outer.StaticNested(...)`, `outer.new Inner()`, local class in a method, anonymous `Task` implementation capturing outer state.
