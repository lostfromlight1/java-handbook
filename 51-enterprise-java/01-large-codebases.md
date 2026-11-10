# Large Codebases

> **Status:** Completed  
> **Module:** 51 — Enterprise and Large-Scale Engineering  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3

## 1. Learning Objectives

- Apply **modular boundaries** (packages, JPMS modules, Maven modules) to tame growth.
- Use **ownership**, **CODEOWNERS**, and **architecture tests** to prevent erosion.
- Navigate **legacy** with strangler patterns without stopping feature work.
- Balance **refactoring** with delivery using incremental steps.

## 2. Prerequisites

- **Module 40** — layered/hexagonal architecture, modular monolith.
- **Module 41** — SOLID, patterns.
- **Module 29** — multi-module Maven/Gradle.

## 3. Why This Concept Exists

Enterprise Java systems accumulate **years** of features: multiple teams, inconsistent styles, circular dependencies, and “god services.” Without structure, compile times, onboarding, and defect rates rise. **Large-codebase engineering** keeps change safe at scale.

## 4. Conceptual Explanation

### Symptoms of an unhealthy monolith

| Symptom | Risk |
|---------|------|
| 30-minute full builds | Slow feedback |
| Everything imports everything | Hidden coupling |
| No domain language | Wrong abstractions |
| Flaky shared test DB | CI mistrust |

### Structural tactics

| Tactic | Mechanism |
|--------|-----------|
| **Package by feature** | `com.acme.billing.*` not layer-only dumps |
| **Maven/Gradle modules** | Enforce dependency direction (`domain` ← `infra`) |
| **ArchUnit** (optional) | Tests forbid forbidden edges (controller → entity direct) |
| **API modules** | Published client JAR with DTOs only |

Module 40 **modular monolith** often beats premature microservices: one deploy, clear seams.

### Team practices

- **Trunk-based development** with short-lived branches vs long-lived forks.
- **CODEOWNERS** for sensitive packages (auth, payments).
- **RFCs** for cross-cutting changes (schema, security).
- Onboarding maps handbook modules to domain areas.

## 5. Mechanism and Execution Flow

### Strangler fig routing

```mermaid
flowchart LR
  Client --> Facade
  Facade --> Legacy
  Facade --> NewImpl
  NewImpl --> DB[(Shared or new store)]
  Legacy --> DB
```

Traffic shifts incrementally behind **facade** or API gateway; parity tests gate cutover.

### ArchUnit enforcement cycle

1. Team agrees dependency rule (e.g. `domain` must not depend on `spring`).
2. ArchUnit test fails on violation in CI.
3. Fix dependency or revise rule via RFC—not silence the test.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Strangler fig** | Replace legacy by routing new work to new implementation |
| **Modular monolith** | Single deploy with enforced module boundaries |
| **Shared kernel** | Small common model across teams—watch for drift |

## 7. Real-World Use Cases

- Bank core: dependency reports nightly.
- SaaS: extract `payments` module before separate deployable.

## 8. Edge Cases

- **Generated code** (JPA metamodel, OpenAPI)—exclude from noisy rules; pin generation.
- **Shared kernel** drift between services—version client libraries (Module 51 Ch.02).

## 9. Common Mistakes

### Incorrect — big-bang rewrite

Years of freeze, high failure rate.

### Corrected — strangler + measurable milestones.

### Incorrect — ArchUnit rules nobody updates

False green when architecture already violated.

### Corrected — fix violations or narrow rules with team agreement.

## 10. Design Trade-offs

| Micro-split early | Modular monolith longer |
|-------------------|-------------------------|
| Ops overhead | Faster refactor across modules |

## 11. Revision and Mastery

- Package-by-feature vs layer-by-type?
- One ArchUnit rule for a Spring REST project?

## 12. Exercises

1. Sketch module graph for handbook-style REST + JPA app.
2. Identify three strangler steps for legacy SOAP adapter.

## 13. Chapter Summary

Large Java codebases need enforced boundaries, ownership, incremental modernization, and architecture feedback loops—not heroic rewrites.

## Next Steps

[02-compatibility-and-migrations.md](02-compatibility-and-migrations.md)

## Review Checklist

- [x] Technical L2–3; strangler + ArchUnit mechanism
- [x] Structure, teams, mistakes preserved
