# Java agent premain demo

Minimal **Java agent** loaded with `-javaagent` before `main` runs.

```bash
javac AgentMain.java AppMain.java
jar cfm agent.jar MANIFEST.MF AgentMain.class
java -javaagent:agent.jar AppMain
```

**Verified (Temurin 21.0.12.1):** lines include `agent=premain`, `app=running`, `agentDemo=ok` (loaded class count varies by JVM).

Agents use the **Instrumentation** API (Module 50). Production agents add bytecode transformation via `ClassFileTransformer`; this sample only reports loaded class count.
