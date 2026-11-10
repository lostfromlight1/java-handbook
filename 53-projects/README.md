# Module 53 — Practical Projects

**Status:** Completed  
**Last reviewed:** 2026-11-11  
**Chapter profile:** Technical (capstone specs; learner implementations)  
**Depth target:** Levels 2–3 (spec clarity and verification criteria)

Hands-on **specifications** live under [projects/](../projects/). Complete handbook chapters first; use projects to integrate skills across CLI, JDBC, HTTP, concurrency, and Spring-shaped layering. This module does **not** ship full reference implementations—learners build from specs unless an example exists under `examples/`.

## Documentation standards for projects

| Expectation | Detail |
|-------------|--------|
| **Scope** | Each spec defines goals, milestones, acceptance criteria, and verification steps aligned with `DOCUMENTATION-STANDARDS.md`. |
| **JDK** | Target **Java 21** (Temurin 21.0.12.1 recommended); record `java -version` in your README when you implement. |
| **Verification labels** | Learner READMEs should label runs **Verified** only after real compile/run on JDK 21—not copied from handbook text. |
| **No invented solutions** | Handbook agents do not add complete project code under `projects/`; only spec clarifications and cross-links. |
| **Progress** | Track your own implementation status in your repo; handbook marks **specs** complete, not your portfolio build. |

| Level | Specs | Spec status |
|-------|-------|-------------|
| Beginner | [01 CLI expense tracker](../projects/beginner/01-cli-expense-tracker.md), [02 Gradebook CLI](../projects/beginner/02-student-gradebook-cli.md) | Completed |
| Intermediate | [01 Library JDBC](../projects/intermediate/01-library-catalog-jdbc.md), [02 Bookmark API](../projects/intermediate/02-bookmark-api-jdk-http.md) | Completed |
| Advanced | [01 Download scheduler](../projects/advanced/01-concurrent-download-scheduler.md), [02 Layered inventory](../projects/advanced/02-layered-inventory-service.md) | Completed |
| Expert | [01 Modular orders](../projects/expert/01-modular-order-platform.md), [02 Production capstone](../projects/expert/02-production-readiness-capstone.md) | Completed |

**Prerequisites:** See each project; generally Modules 00–38 before advanced backend projects; Modules 46–51 before expert capstones.

**Portfolio assessment:** [53-projects-assessment.md](../assessments/module-assessments/53-projects-assessment.md)

**After Module 53:** Revisit module assessments, diagnostic, and expert projects; deepen appendix topics as authored.
