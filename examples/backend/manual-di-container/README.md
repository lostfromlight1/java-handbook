# Manual DI container demo

Pure-Java **Inversion of Control** teaching demo: register implementations by interface, resolve with constructor injection (no Spring).

```bash
javac -d out src/main/java/com/handbook/di/*.java
java -cp out com.handbook.di.ManualDiDemo
```

**Verified (Temurin 21.0.12.1):** `greet=Hello, Lin`; `diContainer=ok`
