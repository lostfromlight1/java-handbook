# Assessment: Module 48 — DevOps and Deployment

> **Type:** Module  
> **Maps to:** Module 48  
> **Time guide:** 45–60 minutes  
> **Solutions:** [48-devops-deployment-assessment-solutions.md](48-devops-deployment-assessment-solutions.md)

## Part A — Conceptual

1. Container vs VM — one difference that matters for JVM memory sizing?
2. Why use multi-stage Dockerfiles for Java?
3. What does `MaxRAMPercentage` address in cgroups?
4. CI vs CD — define each in one sentence.
5. Why run `./mvnw verify` instead of `package` alone in CI?
6. Name two deployment strategies and when you would pick each.
7. Readiness vs liveness probe — different failure effect?
8. SIGTERM relevance to Java in Kubernetes?
9. Immutable artifact promotion — what stays same staging→prod?
10. One mistake when JDBC pool size × replicas exceeds DB limit?

## Part B — Scenario

11. Container OOM (exit 137) but heap metrics low — name two non-heap consumers.
12. Outline pipeline stages from git push to deploy for a Boot JAR.

## Scoring

| Band | Next steps |
|------|------------|
| 85%+ | Module 49; skim Revision sections in 48 |
| 70–84% | Re-read Ch.01 cgroup memory, Ch.03 probes |
| <70% | Ch.01–03 full pass; Module 29 build recap |

## Chapter mapping

| Q | Chapter |
|---|---------|
| 1–4, 11 | 01 |
| 5–6, 12 | 02 |
| 7–10 | 03 |
