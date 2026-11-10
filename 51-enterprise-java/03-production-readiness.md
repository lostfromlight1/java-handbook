# Production Readiness

> **Status:** Completed  
> **Module:** 51 — Enterprise and Large-Scale Engineering  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Use a **production readiness checklist** before launch (SRE-style).
- Define **SLOs**, **runbooks**, **on-call**, and **capacity** for JVM services.
- Plan **security** hardening, **secrets**, **backups**, and **disaster recovery**.
- Connect handbook topics into a **go-live** review.

## 2. Prerequisites

- **Modules 31, 47** — debugging, observability.
- **Modules 39, 48** — security auth patterns, deploy/CI.
- **Module 37** — Actuator, health.

## 3. Why This Concept Exists

“Works in demo” differs from **production**: failure modes, abuse, data loss, and 3 a.m. pages. **Production readiness** is the gate that asks whether operators can **run**, **observe**, **restore**, and **secure** the Java system.

## 4. Conceptual Explanation

### Readiness dimensions

| Area | Questions |
|------|-----------|
| **Reliability** | SLOs, error budget, redundancy, probes |
| **Performance** | Load test, pool sizes, GC under peak |
| **Security** | AuthZ, TLS, dependency CVEs, least privilege |
| **Operability** | Dashboards, logs, traces, runbooks |
| **Data** | Backups, restore tested, migration rollback |
| **Compliance** | PII handling, retention, audit logs |

### SLO sketch for REST API

- **Availability:** 99.9% monthly (exclude planned maintenance).
- **Latency:** p99 < 500ms under expected RPS.
- **Error rate:** < 0.1% 5xx excluding client 4xx.

Measure with metrics from Actuator/Micrometer + APM (Module 47 themes).

### Runbook essentials

1. **Service overview** — what it does, dependencies.
2. **Dashboards** — links, golden signals (latency, traffic, errors, saturation).
3. **Common failures** — DB down, pool exhausted, GC thrash — with steps.
4. **Rollback** — image digest, migration reversal policy.
5. **Contacts** — ownership, escalation.

### Security baseline

- Dependency scan in CI (Module 30/48).
- Secrets in vault; rotate JDBC passwords.
- TLS termination and HSTS at ingress (Module 32).
- Rate limiting / auth on public endpoints (Module 39).

### Capacity

| Input | Derived |
|-------|---------|
| Peak RPS | Thread pool + CPU |
| DB connections | `replicas × poolMax` < DB max |

Load test **before** launch; JVM heap and GC logs under sustained load.

### Launch process

1. Pre-prod soak test.
2. Checklist sign-off (eng + ops).
3. Canary or rolling deploy (Module 48).
4. Hypercare window with elevated monitoring.

## 5. Real-World Use Cases

- Financial service: mandatory DR drill restoring DB + app tier.
- B2B API: customer-facing status page tied to SLO burn alerts.

## 6. Edge Cases

- **Thundering herd** on cache expiry — coordinate TTL jitter.
- **Certificate expiry** — automate cert-manager renewal alerts.

## 7. Common Mistakes

### Incorrect — no restore drill

Backups exist but untested; restore fails in incident.

### Corrected — quarterly restore to isolated env.

### Incorrect — alerting on every log ERROR

Alert fatigue; miss real outages.

### Corrected — SLO-based paging on user-visible symptoms.

## 8. Design Trade-offs

| Higher SLO | Cost |
|------------|------|
| Multi-AZ, redundancy | Infra spend |

## 9. Revision and Mastery

- Four golden signals?
- Minimum three runbook sections for a JDBC Spring service?

## 10. Exercises

1. Draft SLO table for a handbook REST demo scaled to 1k RPS.
2. Production readiness gap list from Modules 30–48 you have not implemented.

## 11. Chapter Summary

Production readiness unifies testing, observability, security, deploy strategy, and operational docs so Java services survive real traffic and failures—not just compile.

## Review Checklist

- [x] Checklist; SLO; runbooks; security; capacity
