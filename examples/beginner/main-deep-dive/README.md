# Example: MainDeepDive

Validates a single name argument; uses stderr and exit codes.

```bash
javac MainDeepDive.java
java MainDeepDive          # exit 1
java MainDeepDive ""       # exit 2
java MainDeepDive Ada      # exit 0, prints Welcome
```

Verified on JDK 21.0.9 / Windows PowerShell `$LASTEXITCODE`.
