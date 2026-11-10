---
name: java-documentation
description: Builds and maintains the Java Learning Textbook—new chapters, expansions, research, examples, JVM notes, exercises, reviews, assessments, and progress updates. Use when working on handbook markdown, examples, appendix, assessments, roadmap, or PROGRESS.md.
---

# Java documentation skill

Reusable workflow for this repository. Details live in linked files; keep this skill actionable.

## Before any write

1. Read `PROGRESS.md` and the relevant module `README.md`.
2. Read `LEARNING-ROADMAP.md` for prerequisites—do not skip foundations.
3. Read `DOCUMENTATION-STANDARDS.md` and `templates/chapter-template.md`.
4. Inspect existing files; do not overwrite completed correct content casually.

## Create or expand a chapter

1. **Curriculum** — scope, prerequisites, objectives (`curriculum-architect` role).
2. **Research** — official JLS/JVMS/JDK/OpenJDK docs; note claims to verify (`java-internals-researcher`).
3. **Draft** — full template sections; apply the **depth gate** (`java-instructor`).
4. **Internals** — verify JVM/runtime claims; label guarantees vs HotSpot (`jvm-specialist` when relevant).
5. **Backend** — production examples when the module level allows (`backend-engineer`).
6. **Examples** — place under `examples/`; compile and run on JDK 21; record real output (`java-code-reviewer`).
7. **Review** — apply [review-checklist.md](review-checklist.md); fix issues (`documentation-reviewer`).
8. **Progress** — update statuses only after a real pass.

## Depth gate (mandatory)

For each major concept include when applicable:

- Detailed conceptual explanation
- Progressive examples (minimal → realistic → advanced)
- Real-world use cases
- Edge cases and limitations
- Incorrect implementations and why they are wrong
- Internal behavior when relevant
- Practical and advanced applications
- Design trade-offs

## Assessments

- After all chapters in a module are `Completed`, create `assessments/module-assessments/NN-*-assessment.md`.
- Put solutions in `NN-*-assessment-solutions.md` only.
- Use `templates/assessment-template.md`.
- Do not create empty assessment placeholders for unfinished modules.

## Diagrams and citations

- Mermaid only when it teaches structure or flow; explain every diagram.
- Source priority: JLS/JVMS → official JDK docs → JEPs/release notes → OpenJDK → reputable books.
- Never fabricate citations or execution results.

## Additional resources

- Standards: [DOCUMENTATION-STANDARDS.md](../../../DOCUMENTATION-STANDARDS.md)
- Chapter template: [templates/chapter-template.md](../../../templates/chapter-template.md)
- Assessment template: [templates/assessment-template.md](../../../templates/assessment-template.md)
- Review checklist: [review-checklist.md](review-checklist.md)
