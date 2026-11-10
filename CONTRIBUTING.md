# Contributing

Thank you for helping maintain this handbook. Quality and technical accuracy come first.

## Before you write

1. Read [PROGRESS.md](PROGRESS.md) and the relevant module `README.md`.
2. Read [LEARNING-ROADMAP.md](LEARNING-ROADMAP.md) for prerequisites—do not skip foundations.
3. Read [DOCUMENTATION-STANDARDS.md](DOCUMENTATION-STANDARDS.md) and [templates/chapter-template.md](templates/chapter-template.md).
4. Follow `.cursor/skills/java-documentation/SKILL.md`.

## Authoring rules

- Write one substantial chapter at a time; do not create empty chapter stubs.
- Follow the teaching principles in `DOCUMENTATION-STANDARDS.md`: detailed definitions, explain-before-use, standalone chapters, progressive depth, and “explain it now—expand it later.”
- Never use a future chapter as a substitute for explaining a concept when it first appears.
- Validate Java examples on JDK 21 (record the exact version).
- Update glossary terms when introducing new vocabulary.
- Update `PROGRESS.md` to match reality only; editing alone does not make a chapter `Completed`.
- Do not invent compiler output, benchmarks, or JVM guarantees.
- Preserve useful existing content when improving chapters; expand shallow sections instead of regenerating whole documents without cause.

## Review

Use `.cursor/skills/java-documentation/review-checklist.md` before marking a chapter `Completed`.

## Pull requests

- Prefer small, reviewable changes (one chapter or one infrastructure slice).
- Describe what was validated (JDK version, commands, outputs).
