# EqualsHashCodeDemo

Module 06 Ch.04 — `equals`, `hashCode`, `toString`, and `HashSet` deduplication.

```bash
javac EqualsHashCodeDemo.java
java EqualsHashCodeDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
a.equals(b)=true
a.equals(c)=false
a.hashCode()==b.hashCode()=true
a=Sku[ABC@1]
setSize=2
```

Concepts: value equality vs identity, `Objects.hash`, `instanceof` pattern in `equals`, equal second add does not grow `HashSet`.
