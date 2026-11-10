# In-memory LRU cache demo

Access-order `LinkedHashMap` cache (Module 46; not production thread-safe).

```bash
javac -d out *.java
java -cp out InMemoryCacheDemo
```

**Verified (Temurin 21.0.12.1):** `user:2=miss`; `in-memory-cache=ok`.
