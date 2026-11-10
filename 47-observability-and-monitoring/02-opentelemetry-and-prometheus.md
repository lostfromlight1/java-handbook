# OpenTelemetry and Prometheus

> **Status:** Completed  
> **Module:** 47 — Observability and Monitoring  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3

**Note:** Full stack (OTel Collector + Prometheus + Grafana) is a lab exercise; this chapter documents **architecture and contracts**, not fabricated scrape or OTLP output.

## 1. Learning Objectives

- Describe **OpenTelemetry** (OTel) traces, metrics, logs signals and Java agent/SDK roles.
- Explain **Prometheus** pull model and **PromQL** basics for Spring Boot Actuator exports.
- Build **Grafana** dashboards and **alerting** rules at a high level.
- Avoid vendor lock-in via **OTLP** export.

## 2. Prerequisites

- [01-logging-metrics-and-tracing.md](01-logging-metrics-and-tracing.md) — pillars, Micrometer types.
- **Module 37** — Spring Boot Actuator awareness.

## 3. Why This Concept Exists

Every vendor once shipped proprietary agents. **OpenTelemetry** standardizes instrumentation and export; **Prometheus** excels at pull-based metrics and alerting in Kubernetes. Java teams need one mental model: instrument once, export via **OTLP**, route in the **collector**.

## 4. Conceptual Explanation

### OpenTelemetry overview

| Component | Role |
|-----------|------|
| **API** | App code creates spans/metrics |
| **SDK** | Implements export pipelines |
| **Instrumentation** | Auto for JDBC, HTTP, etc. |
| **Collector** | Receives OTLP; fans out to backends |
| **OTLP** | gRPC/HTTP protocol to collector |

Java options:

- **Java agent** (`-javaagent:opentelemetry-javaagent.jar`) — low-code HTTP/JDBC spans.
- **Manual** `Span` in critical business paths.

**Conceptual without local install:** plan header propagation (`traceparent`) and naming conventions (`http.server.duration`).

### Spring Boot integration (awareness)

Spring Boot 3.x aligns with Micrometer tracing + OTel bridge:

```properties
# conceptual application.properties
management.tracing.sampling.probability=1.0
management.otlp.tracing.endpoint=http://collector:4317
```

Export to Jaeger, Tempo, or vendor via collector—not directly from app to many backends.

### Prometheus model

- **Pull:** Prometheus scrapes `GET /actuator/prometheus` periodically.
- **Time series:** `metric_name{label="value"} value timestamp`.
- **PromQL:** `rate(http_server_requests_seconds_count[5m])`.

Common JVM metrics:

- `jvm_memory_used_bytes`
- `http_server_requests_seconds` histogram
- `hikaricp_connections_active`

## 5. Mechanism and Execution Flow

### Java service → backends

```mermaid
flowchart LR
  JVM[Spring Boot JVM] --> A[/actuator/prometheus]
  JVM --> O[OTLP exporter]
  P[Prometheus] -->|scrape| A
  O --> C[OTel Collector]
  C --> J[Jaeger/Tempo/vendor]
  P --> G[Grafana]
  G --> AL[Alerts]
```

### Trace propagation across HTTP

1. Incoming filter reads `traceparent` (or generates trace).
2. Outbound `RestTemplate`/`WebClient` injects headers.
3. Spans exported via agent or Micrometer bridge to collector.

Sampling: 100% traces expensive—use probability in prod, full in staging.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **OTLP** | OpenTelemetry Protocol |
| **ServiceMonitor** | K8s CRD for Prometheus scrape config (Module 49) |
| **PromQL** | Prometheus query language |

## 7. Alerting

Alertmanager rules (examples):

- `error_rate > 1%` for 5m on checkout.
- `hikaricp_connections_pending > 10` for 2m.

Tie alerts to **runbooks** (Ch.03).

## 8. Dashboards

Grafana panels:

- RPS and p95 latency by endpoint.
- Saturation: CPU, heap, pool wait.
- Business KPI: orders/min (counter).

## 9. Real-World Use Cases

- K8s: ServiceMonitor scrapes each pod; same labels for HPA.
- Canary deploy: compare error metric canary vs stable.
- JDBC trace shows N+1 query span pattern.

## 10. Edge Cases

- **Sampling:** partial traces can mislead—correlate with logs by trace id.
- **Cardinality:** avoid high-cardinality labels on Prometheus metrics.
- **Multiple agents:** conflicting bytecode instrumentation—use one pipeline.

## 11. Common Mistakes

| Mistake | Fix |
|---------|-----|
| Multiple conflicting agents | One OTel pipeline |
| Scraping without auth | Network policy + mTLS |
| Dashboards without SLO | Start from user journeys (Ch.03) |

## 12. Design Trade-offs

| Java agent auto-instrument | Manual spans only |
|------------------------------|-------------------|
| Broad coverage | Precise business events |

| Push gateway | Prometheus pull |
|--------------|-----------------|
| Short jobs | Long-lived services |

## 13. Revision and Mastery

- Pull vs push metrics for a 24/7 Spring service?
- Where does OTLP land before Jaeger?
- Actuator path for Prometheus scrape?

## 14. Exercises

1. Sketch diagram: JVM → Actuator → Prometheus → Grafana.
2. Write PromQL for 5xx rate over 5m.
3. List three auto-instrumented libraries via OTel agent.

## 15. Chapter Summary

OpenTelemetry standardizes Java instrumentation and OTLP export; Prometheus scrapes Micrometer metrics for dashboards and alerts—deploy collectors in ops environments to realize the stack.

## 16. Next Steps

[03-slos-and-incidents.md](03-slos-and-incidents.md)

## Review Checklist

- [x] Technical L2–3; mechanism diagram; OTel/Prometheus conceptual
- [x] No fabricated scrape data; preserved alerting/dashboard topics
