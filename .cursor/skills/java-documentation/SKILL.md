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

1. **Curriculum** — scope, prerequisites, objectives; choose **chapter profile** (Conceptual / Technical / Deep-dive); note first introductions vs later deep dives (`curriculum-architect`).
2. **Research** — follow source hierarchy: JLS → JVMS → API docs → OpenJDK → framework docs → authoritative books (`java-internals-researcher`).
3. **Draft** — use master template; include **mandatory** sections; omit conditional sections that lack value (record omissions); write **Mechanism and Execution Flow** when applicable (`java-instructor`).
4. **Explain-before-use** — define terminology and unfamiliar syntax before examples rely on them.
5. **Standalone pass** — reader can understand without constantly opening others; brief prerequisite refreshers + links for depth.
6. **Internals** — verify JVM/runtime claims; label guarantees vs HotSpot; never treat HotSpot as universal JVM law (`jvm-specialist` when relevant).
7. **Backend** — production examples when the module level allows (`backend-engineer`).
8. **Examples** — place under `examples/`; label **Verified** / **Compile-verified** / **Illustrative**; compile and run Verified on JDK 21; record real output; explain important lines and why (`java-code-reviewer`).
9. **Review** — apply [review-checklist.md](review-checklist.md); fix issues (`documentation-reviewer`).
10. **Progress** — update statuses only after depth gate, Mandatory Depth Review, and checklist pass.

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

## Mandatory Depth Review (Levels 1–4) and profiles

Coverage ≠ understanding. Before `Completed`, apply `DOCUMENTATION-STANDARDS.md` Levels 1–4, chapter profiles, and the ten-question teaching review.

| Profile | Typical depth |
|---------|---------------|
| Conceptual | Levels 1–2 |
| Technical | Levels 2–3 (core APIs usually ≥ 3) |
| Deep-dive | Levels 3–4 (Level 4 on critical mechanisms) |

Reject Level-1-only drafts that only list terms, tables, and annotation recipes. Do not force identical section sets across profiles.

### How to achieve depth (writing order per major concept)

1. Motivation (concrete problem)
2. Full definition with a concrete collaborator/scenario
3. Mechanism and execution flow (then internals if needed)
4. Contrast with the wrong approach
5. Progressive examples with verification labels and important lines explained
6. Limits, trade-offs, version/framework caveats (labeled)
7. Revision and Mastery for Track B / Technical / Deep-dive

Do not inflate line counts with repetition. Expand explanations; do not regenerate whole chapters without cause when improving existing work.

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

- Mermaid when it teaches structure or flow (required for complex pipelines, concurrency, DI/lifecycle, GC, etc. when useful); explain every diagram; never force diagrams for simple topics.
- Source hierarchy: JLS → JVMS → official API docs → OpenJDK → framework docs/source → authoritative books/articles.
- Never fabricate citations or execution results; never label Verified without a real run.

## Additional resources

- Standards: [DOCUMENTATION-STANDARDS.md](../../../DOCUMENTATION-STANDARDS.md)
- Chapter template: [templates/chapter-template.md](../../../templates/chapter-template.md)
- Assessment template: [templates/assessment-template.md](../../../templates/assessment-template.md)
- Review checklist: [review-checklist.md](review-checklist.md)
