# Production Incidents

> **Status:** Completed  
> **Module:** 31 — Debugging and Troubleshooting  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Conceptual runbook; ties to Ch.02 dumps and Module **32** resilience

## 1. Learning Objectives

- Follow a structured **incident response** loop for JVM services.
- Map symptoms (latency, errors, CPU) to **first tools** (metrics, logs, dumps, JFR).
- Apply **post-incident** learning without blaming individuals.

## 2. Prerequisites

- Ch.01–02 — debugging and dumps.
- Module **28** — profiling mindset.

## 3. Why This Concept Exists

Production failures combine code, configuration, infrastructure, and traffic. A **runbook** reduces panic and documents what the JVM can tell you while the service is still running or shortly after failure.

## 4. Conceptual Explanation

**Triage loop:**

1. **Detect** — alerts on SLO burn (error rate, latency p99).
2. **Mitigate** — rollback, scale, feature flag, circuit breaker (Module **32**).
3. **Diagnose** — logs (correlation IDs), metrics, thread dump, JFR, heap if OOME.
4. **Fix** — patch, config, capacity.
5. **Postmortem** — timeline, root cause, action items.

| Symptom | First JVM checks |
|---------|------------------|
| Sudden 500s | App logs + stack traces; recent deploy? |
| High CPU | Thread dump; JFR CPU samples; GC logs |
| Memory climb | Heap trend; dump; leak patterns (Module **27** Ch.03) |
| Stuck requests | Thread dump for blocked threads; DB pool exhaustion (Module **33**) |

## 5. Real-World Use Cases

- JDBC pool exhausted under traffic — thread dumps show many threads in `getConnection()`.
- GC pause storm — unified GC logs (`-Xlog:gc*`).
- Deadlock — thread dump reports cycle.

## 6. Edge Cases

- **Restart clears evidence** — capture dumps before rolling restart when possible.
- **Sampling bias** — one thread dump may miss rare races; take several.
- Container CPU limits throttle JVM — symptoms look like Java slowness but are cgroup caps.

## 7. Common Mistakes

### Incorrect

Immediately `kill -9` without capturing `jcmd Thread.print`.

### Corrected

Documented escalation: snapshot diagnostics, then restart if needed.

## 8. Best Practices

- **Structured logging** with trace IDs across HTTP and JDBC.
- **Runbooks** linked from alerts.
- Blameless postmortems with measurable follow-ups.

## 9. Chapter Summary

Production incidents combine JVM diagnostics with operational discipline; dumps and JFR from Ch.02 are primary Java-specific weapons.

## 10. Next Steps

Module **32** — Networking and HTTP.

## Review Checklist

- [x] Runbook depth; no fabricated incident timelines
