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
5. If improving existing docs: identify shallow definitions, unexplained first appearances, and improper deferrals before rewriting.

## Create or expand a chapter

1. **Curriculum** — scope, prerequisites, objectives; note which concepts need a first introduction here vs a later deep dive (`curriculum-architect` role).
2. **Research** — official JLS/JVMS/JDK/OpenJDK docs; note claims to verify (`java-internals-researcher`).
3. **Draft** — full template sections that add value; apply the **depth gate** (`java-instructor`).
4. **Explain-before-use** — define terminology and unfamiliar syntax before examples rely on them.
5. **Standalone pass** — ensure a reader can understand this chapter without constantly opening others; brief prerequisite refreshers + links for depth.
6. **Internals** — verify JVM/runtime claims; label guarantees vs HotSpot (`jvm-specialist` when relevant).
7. **Backend** — production examples when the module level allows (`backend-engineer`).
8. **Examples** — place under `examples/`; compile and run on JDK 21; record real output; explain important lines (`java-code-reviewer`).
9. **Review** — apply [review-checklist.md](review-checklist.md); fix issues (`documentation-reviewer`).
10. **Progress** — update statuses only after a real pass of the depth gate and checklist.

## Depth gate (mandatory)

For each major concept include when applicable:

- Detailed conceptual explanation (what / why / how / when / trade-offs)
- Progressive examples (minimal → realistic → advanced)
- Real-world use cases
- Edge cases and limitations
- Incorrect implementations and why they are wrong
- Internal behavior when relevant
- Practical and advanced applications
- Design trade-offs

## Progressive explanation rules

- **First appearance:** meaning, purpose, basic behavior, simple example—even if a dedicated chapter comes later.
- **Later dedicated chapter:** deepen with rules, internals, edge cases, and professional practice.
- Never use “later chapter / beyond scope / see next” as a substitute for the current explanation.
- Revisit concepts at deeper levels; do not copy-paste unchanged text across modules.

## Self-contained chapters

- Briefly explain prerequisites in place.
- Link to related chapters for deeper study.
- Prefer in-chapter progressive snippets plus `examples/` links over demo-only teaching.

## Reviewing existing documentation

1. Read the whole chapter.
2. Flag shallow/dictionary definitions.
3. Flag concepts introduced without enough context.
4. Flag improper deferrals to future chapters.
5. Expand explanations; improve examples; add practical context where useful.
6. Verify technical accuracy; preserve good existing content.
7. Update `PROGRESS.md` only to match reality—editing alone does not equal `Completed`.

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
