# Module 48 — DevOps and Deployment

**Status:** Completed — fourth EXPAND-PRESERVE deepen pass 2026-11-11 (Technical Levels 2–3).  
**Last reviewed:** 2026-11-11  

| # | Chapter | Status |
|---|---------|--------|
| 01 | [Containers and the JVM](01-containers-and-the-jvm.md) | Completed |
| 02 | [CI/CD for Java](02-ci-cd.md) | Completed |
| 03 | [Deployment Strategies](03-deployment-strategies.md) | Completed |

**Prerequisites:** Modules 26–27, 29–30, 37–38; Modules 46–47 helpful for capacity and SLO-gated canaries.

**Assessment:** [48-devops-deployment-assessment.md](../assessments/module-assessments/48-devops-deployment-assessment.md)

**Examples:** [diagnostics-awareness-demo](../examples/debugging/diagnostics-awareness-demo/) — Verified on Oracle JDK 21.0.9 (2026-11-11): `jvmName=Java HotSpot(TM) 64-Bit Server VM`; `note=Use jcmd/jfr...`. Live Docker smoke remains optional (no fabricated container output).

**Depth note (fourth deepen 2026-11-11):** Textbook-depth expansions of cgroup/JVM packaging, CI/CD immutability & gates, and rollout/canary/migration mechanisms.
