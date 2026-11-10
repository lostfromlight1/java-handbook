# Documentation Standards

These standards govern every chapter, appendix section, assessment, and example in this handbook.

**Target Java version:** 21 LTS (examples validated on the JDK recorded in `PROGRESS.md`)

## Audiences

- **Track A:** Absolute beginners—define every term before using it; no unexplained jargon.
- **Track B:** Experienced developers—every major chapter includes a **Revision and Mastery** section.

Serve both audiences in the same document. Beginner explanations must remain accessible. Advanced sections must remain technically meaningful. Do not assume that professional experience implies mastery of equality/hashing, generics erasure, collections internals, memory management, the JMM, class loading, GC, JIT, or concurrency.

## Core teaching principles

### Explain it now. Expand it later.

When a concept first appears, give a **complete enough** explanation for the current chapter: what it is, why it exists, what problem it solves, how it behaves at a basic level, and a simple example when useful.

You may say that a later chapter goes deeper. You must **never** use a future chapter as a substitute for explaining the idea now.

Forbidden as a replacement for explanation:

- “We will learn this in a later chapter.”
- “This is beyond the scope of this chapter.”
- “See the next chapter for an explanation.”

Allowed: a short current explanation, then a link for deeper study.

### Standalone readability

Every completed chapter should work as a useful learning and revision resource on its own. A reader should not need to constantly open other chapters just to understand the current one.

When depending on another concept:

1. Briefly explain the prerequisite.
2. Give enough background for the current topic.
3. Include a simple example if appropriate.
4. Link to the dedicated chapter for deeper study.

Cross-links **supplement** explanations; they never replace them.

### Progressive depth across the curriculum

Important concepts appear more than once. Do not copy-paste the same paragraph into every chapter. Deepen intentionally:

| Stage | What to teach |
|-------|----------------|
| First introduction | Meaning, purpose, basic behavior, simple example |
| Intermediate | Practical rules, common uses, common mistakes |
| Advanced | Internals, design, performance, edge cases, trade-offs |
| Specialized deep dive | JVM mechanisms, implementation details, advanced engineering |

Example path: JVM appears in Module 00 (purpose), Module 01 (tooling), Module 26 (internals), then again with GC and performance.

### Detailed definitions for major concepts

Do not stop at dictionary-style one-liners for important ideas. For each major concept, cover when applicable:

- What it is and what it means
- Why it exists and what problem it solves
- What it does and how it works
- How it is used in Java
- When and why developers use it
- Internal behavior when relevant (labeled carefully)
- Limitations and trade-offs

Example: do not define an interface only as “a contract.” Explain what a contract means in programming, why interfaces exist, how classes implement them, how they differ from abstract classes, and where they help in real systems.

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

Length is not the goal. **Complete explanations** are the goal. Add detail when it improves understanding; do not add filler sections.

## Five explanation levels

1. **Conceptual** — what it is and why it exists
2. **Language-level** — syntax, types, rules, semantics
3. **Practical** — complete runnable examples
4. **Internal** — JVM / library behavior (spec vs HotSpot labeled)
5. **Professional** — production use, performance, security, maintainability

## Explain before using

Do not introduce unfamiliar terminology, syntax, APIs, classes, methods, annotations, patterns, or JVM mechanisms in an example without explaining their purpose.

When introducing unfamiliar syntax:

- Explain what it means
- Explain what each important part does
- Explain why it is needed
- Show how it behaves
- Note common variations when useful

## Progressive examples

Where appropriate, move from:

1. Minimal example introducing the concept
2. Practical example showing normal usage
3. Realistic example closer to real development
4. Advanced example showing extra behavior or complexity

Explain important lines. Prefer in-chapter snippets plus links to `examples/` over “see the demo” alone when the demo is required to understand the point.

## Reasoning and trade-offs

Explain not only how to write code, but:

- Why this approach is used
- Why an alternative may be worse
- What happens if a different approach is chosen
- What trade-offs matter in real projects

## Practical context

Connect concepts to real development when it adds value: backend apps, REST APIs, databases, enterprise systems, concurrency, distributed systems, production operations, performance-sensitive code. Do not force strained examples.

## Chapter format

Use [templates/chapter-template.md](templates/chapter-template.md). Keep educational sections unless a short orientation chapter truly does not need one (justify omissions in review notes).

A well-developed chapter generally includes learning objectives, prerequisites, introduction/context, definitions, motivation, how it works, terminology, syntax, beginner-friendly explanation, progressive examples, practical use cases, rules/behavior, internals when relevant, common mistakes, edge cases, best practices, performance/security when relevant, comparisons, version notes, revision material, knowledge checks, references, summary, and related chapters.

Do **not** add empty sections just to satisfy a checklist. Every included section must teach something.

Required spirit remains in force:

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

## Curriculum philosophy

- Preserve core Java and foundations before Spring Boot and heavy frameworks
- Spring Boot is an important practical section, not a replacement for understanding Java
- Keep exercises, assessments, and projects separate from main explanatory chapters where the architecture supports that
- Main documentation must serve as both textbook and revision reference

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
Never mark a chapter `Completed` merely because it was reviewed or lightly edited—the depth gate and review checklist must pass.

## Assessments and solutions

- Keep solutions in separate `*-solutions.md` files
- Do not place answers immediately after every exercise if that spoils practice
- Map diagnostic questions to handbook chapters
- After all chapters in a module are `Completed`, create the module assessment
