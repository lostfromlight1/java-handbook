# Documentation Standards

These standards govern every chapter, appendix section, assessment, and example in this handbook.

**Target Java version:** 21 LTS (examples validated on the JDK recorded in `PROGRESS.md`)

## Audiences

- **Track A:** Absolute beginners—define every term before using it; no unexplained jargon.
- **Track B:** Experienced developers—every major chapter includes a **Revision and Mastery** section.

## Depth gate (non-negotiable)

A major concept is **incomplete** if it only has a short definition and one code sample.

For every major concept, include when applicable:

1. Detailed conceptual explanation
2. Multiple progressively complex code examples (minimal → realistic → advanced)
3. Real-world use cases
4. Edge cases and limitations
5. Incorrect implementations and why they fail
6. Internal / runtime behavior when relevant (labeled carefully)
7. Practical and advanced applications
8. Clear design trade-offs

Chapters that fail this gate stay `Needs revision`, never `Completed`.

## Five explanation levels

1. **Conceptual** — what it is and why it exists
2. **Language-level** — syntax, types, rules, semantics
3. **Practical** — complete runnable examples
4. **Internal** — JVM / library behavior (spec vs HotSpot labeled)
5. **Professional** — production use, performance, security, maintainability

## Chapter format

Use [templates/chapter-template.md](templates/chapter-template.md). Keep educational sections unless a short orientation chapter truly does not need one (justify omissions in review notes). Required spirit of the master curriculum chapter format remains in force, including:

- Learning objectives, prerequisites, why the concept exists
- Beginner-friendly explanation plus Revision and Mastery
- Progressive examples, common mistakes, best practices
- Version notes, exercises, interview questions, references, summary

## Code examples

- Compile and run on the documented JDK (primary: Java 21)
- Meaningful names; necessary imports; consistent formatting
- Prefer self-contained programs under `examples/<area>/...`
- Verify output by running; never invent output
- Record `java -version` / `javac -version` when documenting results
- Identify simplified examples; never present insecure code as production-ready
- Show incorrect implementations with corrections when teaching pitfalls

## Guarantees vs implementation

- Distinguish Java Language Specification (JLS) from Java SE APIs
- Distinguish JVM Specification requirements from HotSpot details
- Distinguish framework behavior from language/runtime guarantees
- Label preview, incubator, deprecated, and removed features

## Version policy

- Primary learning path: **stable Java 21** features
- Document newer LTS (25) and feature releases (e.g. 27) in version notes / Module 19
- For each version-sensitive claim: version introduced, status (final / preview / incubator / deprecated / removed), production notes

## Citations

- Prefer official Oracle / OpenJDK / JLS / JVMS / JDK API / framework docs
- Maintain [REFERENCES.md](REFERENCES.md); cite in chapters
- Never fabricate citations, API behavior, or test results

## Status vocabulary (`PROGRESS.md`)

| Status | Meaning |
|--------|---------|
| Not started | Planned only |
| In progress | Actively being written |
| Drafted | Full draft exists; not yet reviewed |
| Needs review | Awaiting technical/editorial review |
| Needs revision | Review found gaps |
| Completed | Passed quality checklist |

Never mark a module `Completed` because one chapter is finished.

## Assessments and solutions

- Keep solutions in separate `*-solutions.md` files
- Do not place answers immediately after every exercise if that spoils practice
- Map diagnostic questions to handbook chapters
