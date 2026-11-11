# Module 47 — Observability and Monitoring

**Status:** Completed — fourth EXPAND-PRESERVE deepen pass 2026-11-11 (Technical Levels 2–3).  
**Last reviewed:** 2026-11-11  

| # | Chapter | Status |
|---|---------|--------|
| 01 | [Logging, Metrics, and Tracing](01-logging-metrics-and-tracing.md) | Completed |
| 02 | [OpenTelemetry and Prometheus](02-opentelemetry-and-prometheus.md) | Completed |
| 03 | [SLOs and Incidents](03-slos-and-incidents.md) | Completed |

**Prerequisites:** Modules 31, 37, 44–45 helpful; Module 46 for load-test/SLO linkage.

**Assessment:** [47-observability-assessment.md](../assessments/module-assessments/47-observability-assessment.md)

**Examples:** [structured-logging-demo](../examples/observability/structured-logging-demo/) — Verified on Oracle JDK 21.0.9 (2026-11-11; matches prior Temurin baseline fields): `event=order.placed`; `structured-logging=ok` (timestamps/`Map.of` order may vary).

**Depth note (fourth deepen 2026-11-11):** Textbook-depth expansions of pillars, OTel/Prometheus architecture, and SLO/incident mechanisms. OTel collector lab remains optional; no fabricated scrape/OTLP Verified output.
