# Progress Tracker

**Target Java version:** 21 LTS  
**Validation JDK (Phase 1):** Oracle JDK 21.0.9 (`javac 21.0.9`)  
**Last updated:** 2026-11-10 (Module 01 completed)

## Status values

`Not started` · `In progress` · `Drafted` · `Needs review` · `Needs revision` · `Completed`

Only mark `Completed` after the quality checklist in `DOCUMENTATION-STANDARDS.md` passes.

---

## Infrastructure

| Item | Status | Notes |
|------|--------|-------|
| Repository structure | Completed | Modules 00–53 indexes; examples; assessments; projects |
| DOCUMENTATION-STANDARDS.md | Completed | |
| README / CONTRIBUTING / GLOSSARY / REFERENCES | Completed | Glossary seeded from Ch.01 |
| LEARNING-ROADMAP.md | Completed | Dual tracks + dependency map |
| Templates | Completed | chapter, exercise, project, assessment |
| Cursor rules | Completed | 6 rules |
| Cursor agents | Completed | 7 agents |
| java-documentation skill | Completed | SKILL + review-checklist |
| Diagnostic framework | Completed | README + scoring + area index |
| appendix/java-version-history.md | Drafted | Seed table |
| Git initialized | Completed | Local only; no remote required |

---

## Appendix

| Document | Status | Review | Outstanding |
|----------|--------|--------|-------------|
| java-version-history.md | Drafted | — | Expand history narrative later |
| java-quick-reference.md | Not started | | |
| jvm-reference.md | Not started | | |
| common-errors.md | Not started | | |
| interview-glossary.md | Not started | | |
| java-vs-go.md | Not started | | |
| java-vs-csharp.md | Not started | | |
| java-vs-python.md | Not started | | |

---

## Module 00 — Programming Foundations

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-what-is-programming-and-what-is-java.md](00-programming-foundations/01-what-is-programming-and-what-is-java.md) | Completed | Self-review 2026-11-10 | — |
| [02-how-computers-execute-programs.md](00-programming-foundations/02-how-computers-execute-programs.md) | Completed | Self-review 2026-11-10 | — |
| [03-algorithms-and-problem-solving.md](00-programming-foundations/03-algorithms-and-problem-solving.md) | Completed | Self-review 2026-11-10 | — |
| [04-data-and-basic-structures.md](00-programming-foundations/04-data-and-basic-structures.md) | Completed | Self-review 2026-11-10 | — |
| [05-complexity-and-debugging-mindset.md](00-programming-foundations/05-complexity-and-debugging-mindset.md) | Completed | Self-review 2026-11-10 | — |
| [Module assessment](assessments/module-assessments/00-programming-foundations-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Oracle JDK 21.0.9):**

| Example | Result |
|---------|--------|
| `examples/beginner/hello-world` | `Hello, Java!` |
| `examples/beginner/execution-order` | Ordered prints 1–6 as documented |
| `examples/beginner/find-max` | `Maximum score: 91` |
| `examples/beginner/scoreboard` | Names/scores + average `91.66666666666667` |
| `examples/beginner/debug-off-by-one` | Bug `10`, fixed `15` |

**Module 00 status:** Completed  
**Next recommended chapter after M00:** Module 01 (Completed below)

---

## Module 01 — Introduction to Java

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-jdk-jre-jvm-and-tooling.md](01-java-getting-started/01-jdk-jre-jvm-and-tooling.md) | Completed | Self-review 2026-11-10 | — |
| [02-installing-java-and-environment.md](01-java-getting-started/02-installing-java-and-environment.md) | Completed | Self-review 2026-11-10 | — |
| [03-ide-and-command-line-workflow.md](01-java-getting-started/03-ide-and-command-line-workflow.md) | Completed | Self-review 2026-11-10 | — |
| [04-first-java-program-deep-dive.md](01-java-getting-started/04-first-java-program-deep-dive.md) | Completed | Self-review 2026-11-10 | — |
| [05-project-structure-and-jshell.md](01-java-getting-started/05-project-structure-and-jshell.md) | Completed | Self-review 2026-11-10 | — |
| [Module assessment](assessments/module-assessments/01-java-getting-started-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Oracle JDK 21.0.9):**

| Example | Result |
|---------|--------|
| `examples/beginner/runtime-info` | `java.version = 21.0.9`, `java.home` = jdk-21 |
| `examples/beginner/args-echo` | 0 args tip; 3 args `hello`/`Java`/`21` |
| `examples/beginner/main-deep-dive` | exits 1 / 2 / 0 as documented |
| `examples/beginner/classpath-demo` | `Hello, Handbook!` with `-d out` / `-cp out` |

**Module 01 status:** Completed  
**Next recommended chapter:** `02-java-fundamentals/01-variables-and-types.md`

---

## Modules 02–53

Remaining modules: **Not started** (indexes only). See each module `README.md` for planned chapter lists. Do not treat indexes as written chapters.
