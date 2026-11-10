# Java Learning Textbook and Engineering Handbook

A comprehensive, technically accurate Java textbook and professional reference—from absolute beginner programming foundations through advanced JVM internals, Spring Boot, and production backend engineering.

**Primary curriculum version:** Java 21 LTS  
**Local validation JDK (Phase 1):** Oracle JDK 21.0.9  
**Latest LTS (documented, not primary):** Java 25  
**Latest feature release (awareness):** Java 27 (non-LTS)

## Who this is for

| Track | Audience | How to use |
|-------|----------|------------|
| **Track A — Complete beginner** | Never programmed before | Follow [LEARNING-ROADMAP.md](LEARNING-ROADMAP.md) linearly from Module 00 |
| **Track B — Revision and mastery** | Existing Java developers | Take the [diagnostic](assessments/diagnostic/README.md), then revise gap chapters; use **Revision and Mastery** sections |

## How to navigate

1. Read [LEARNING-ROADMAP.md](LEARNING-ROADMAP.md) for prerequisites and both learning tracks.
2. Check [PROGRESS.md](PROGRESS.md) for what is actually written (planned ≠ complete). **Through Module 12** (collections hierarchy, lists, sets, queues, maps); **next up:** Module 13.
3. Follow [DOCUMENTATION-STANDARDS.md](DOCUMENTATION-STANDARDS.md) if you contribute content.
4. Use [GLOSSARY.md](GLOSSARY.md) for shared terminology.
5. Prefer official sources listed in [REFERENCES.md](REFERENCES.md).

## Repository map

| Path | Purpose |
|------|---------|
| `00-…`–`53-…` | Curriculum modules (indexes first; chapters when written) |
| `examples/` | Runnable Java examples validated on JDK 21 |
| `assessments/` | Diagnostics, module assessments, challenges |
| `projects/` | Progressive project specifications |
| `appendix/` | Quick reference, version history, language comparisons |
| `templates/` | Chapter, exercise, project, and assessment templates |
| `.cursor/` | Rules, agents, and documentation skill for consistent authoring |

## Core principles

- **Quality over quantity** — no shallow chapters; no fabricated compiler or benchmark results.
- **Explain it now, expand it later** — introduce concepts properly when they first appear; deepen them in later modules.
- **Standalone chapters** — each completed chapter should be usable for learning and revision without constantly opening other files.
- **Core Java before Spring Boot** — frameworks explain mechanisms; they do not replace fundamentals.
- **Spec vs implementation** — distinguish JLS, JVMS, HotSpot, and framework behavior.
- **Honest progress** — only mark work complete after the quality checklist passes.

## Start here

- Beginners: [00-programming-foundations/01-what-is-programming-and-what-is-java.md](00-programming-foundations/01-what-is-programming-and-what-is-java.md)
- Experienced developers: [assessments/diagnostic/README.md](assessments/diagnostic/README.md)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) and `.cursor/skills/java-documentation/SKILL.md`.
