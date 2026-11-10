# Project: Production Readiness Capstone

> **Level:** Expert  
> **Status:** Completed  
> **Prerequisites:** Modules 47, 48–51, any advanced API project  
> **Last reviewed:** 2026-11-10

## Goals

Take an existing handbook-style Java service (your inventory or order project) and deliver **operational artifacts**: SLOs, dashboards checklist, runbook, load test script, and hardened container deploy.

## Learning objectives

- Define measurable SLOs and map to Actuator/Micrometer metrics (conceptual if no Boot — JVM metrics via JFR flags).
- Write runbook with rollback steps tied to image digest.
- Configure probes, resource limits, and `MaxRAMPercentage` in K8s YAML.
- Execute load test (k6, JMeter, or `hey`) and capture **real** p95 on your machine in README.

## Deliverables (not code-only)

| Artifact | Content |
|----------|---------|
| `docs/SLO.md` | Availability, latency, error budget |
| `docs/RUNBOOK.md` | Failures, dashboards links placeholders, escalation |
| `deploy/deployment.yaml` | probes, resources, env |
| `Dockerfile` | multi-stage Temurin 21 |
| `docs/LOADTEST.md` | command + actual results table |

## Milestones

1. Container builds and runs with non-root user.
2. Readiness fails during intentional 30s slow start flag.
3. Load test 5 min at target RPS; note CPU and GC in `jstat` or logs.
4. Dependency CVE scan step documented in CI snippet.
5. Game day: simulate DB down → document observed behavior + runbook steps.

## Acceptance criteria

- [ ] All docs present; no TBD sections.
- [ ] Load test numbers recorded with JDK version and date (not fabricated).
- [ ] Connection pool × replicas ≤ documented DB max in `docs/SLO.md`.
- [ ] Secrets not in git; example uses env placeholders.

## Verification steps

```bash
docker build -t capstone:local .
kubectl apply -f deploy/   # or kind/minikube
# run load tool; paste output in LOADTEST.md
```

## Extension

OpenTelemetry Java agent sidecar note; compare startup with/without agent (Module 50).
