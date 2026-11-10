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

## Mandatory Depth Review (Levels 1–4)

**Do not confuse topic coverage with topic understanding.** Mentioning a concept is not the same as teaching it. A chapter must not be marked `Completed` merely because it has expected headings, short definitions, and a few code samples.

### Quality levels

| Level | Name | What the reader gains |
|-------|------|------------------------|
| **1** | Introduction | Basic definitions and awareness that the topic exists |
| **2** | Understanding | Clear purpose, behavior, and explained examples |
| **3** | Practical mastery | Real-world usage, common problems, limitations, and trade-offs |
| **4** | Technical depth | Internal mechanisms, implementation details, performance/security implications, and advanced edge cases where relevant |

### Required level by topic

- Simple orientation topics may stop at Level 2 if they are genuinely introductory.
- Core language topics (types, OOP, collections, generics, exceptions) normally require **at least Level 3**.
- JVM, concurrency, memory/GC, class loading, Spring lifecycle, AOP/proxies, transactions, Boot autoconfiguration, and similar mechanism-heavy topics require **Level 3 with Level 4 on the critical mechanisms**.
- When unsure, aim higher: prefer teaching understanding over finishing quickly.

### Ten-question teaching review

Before marking any chapter `Completed`, answer these for every important concept in the chapter:

1. Is the concept properly defined (not a one-liner dictionary entry)?
2. Is its purpose and motivation explained?
3. Does the reader understand **why** it exists?
4. Is the underlying mechanism explained at the level this topic requires?
5. Are important terms and related concepts introduced **before** they are used?
6. Are there enough examples to show how it works (progressive when warranted)?
7. Are the examples **explained**, not merely presented?
8. Are practical use cases, limitations, and common mistakes covered?
9. Can a beginner understand the explanation without relying on a future chapter as a substitute?
10. Does the chapter provide enough technical depth to be useful to an experienced Java developer (Revision and Mastery / internals where appropriate)?

If important answers are **no**, the chapter stays `Needs revision` or `In progress`.

### How authors and agents achieve the required depth

Do not “pad” to hit a line count. For each major concept, write in this order:

1. **Motivation** — what breaks or hurts without this idea (concrete scenario).
2. **Definition** — what it is, in full sentences, with a concrete collaborator/example.
3. **Mechanism** — how it works step by step (control flow, object graph, runtime, framework pipeline).
4. **Contrast** — wrong or tightly coupled alternative; what changes when you swap implementations.
5. **Progressive examples** — minimal → realistic → advanced; explain important lines.
6. **Limits and trade-offs** — when not to use it; production pitfalls; version/framework caveats labeled.
7. **Revision** — Track B gaps, interview-style checks, and links that deepen rather than replace.

Anti-patterns that fail this review:

- Tables of terms with one-sentence definitions and no walkthrough
- Annotation recipes without the Java mechanism underneath (interfaces, reflection, proxies, class loading)
- “See Module N” used instead of a complete-enough current explanation
- Diagrams with no prose explaining each stage
- Marking `Completed` because the template headings exist

### Relationship to the depth gate

The depth gate lists **content ingredients**. This Mandatory Depth Review judges whether those ingredients actually produce **understanding** at the required Level. Both must pass.

## Chapter profiles

All profiles share the same teaching philosophy. They differ in **expected depth and weight**, not in whether understanding is required.

| Profile | Suitable topics | Expected depth |
|---------|-----------------|----------------|
| **Conceptual** | Variables, operators, basic syntax, simple enums, orientation | Levels **1–2** (Level 2 preferred when the topic still needs clear purpose + examples) |
| **Technical** | Collections, generics, streams, I/O, JDBC usage, most API modules | Levels **2–3** (normally **≥ Level 3** for core library topics) |
| **Deep-dive** | JVM, concurrency, class loading, GC, Spring lifecycle/AOP/transactions, Boot autoconfig | Levels **3–4** with Level 4 on critical mechanisms |

Record the profile in the chapter header. Deep-dive chapters spend more space on **Mechanism and Execution Flow**, diagrams, internals, edge cases, and trade-offs. Conceptual chapters must not pad with empty Security/Performance/Internals sections.

**Editorial judgment:** The template is not a substitute for judgment. A chapter about operators and a chapter about garbage collection should not have the same structure or depth merely because they use the same template.

**Guiding standard:** Explain every important concept when it first appears. Show how it works. Demonstrate how to use it. Explain where it breaks down. Then deepen the technical understanding according to the topic’s complexity and profile.

## Mechanism and Execution Flow

Between **conceptual understanding** and **internal/implementation behavior**, most Technical and Deep-dive chapters need an explicit **mechanism** layer:

1. **Conceptual** — what the idea is and why it exists
2. **Mechanism and execution flow** — sequence of events, responsibilities, interactions (pipelines, object graphs, call order)
3. **Internal behavior** — JLS/JVMS/HotSpot/framework details, labeled carefully

Use diagrams or step traces when they improve understanding (stream pipelines, DI graphs, bean lifecycle, AOP proxies, GC, threads). If no meaningful separate mechanism exists, merge a short execution note into the conceptual section and record the merge in the review checklist.

## Five explanation levels (writing dimensions)

Use these dimensions while drafting; they complement Levels 1–4 above (quality outcome):

1. **Conceptual** — what it is and why it exists
2. **Language-level** — syntax, types, rules, semantics
3. **Practical** — complete runnable examples
4. **Internal** — JVM / library / framework behavior (spec vs HotSpot / framework labeled)
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

For each substantial example, when applicable: state filename/package; include imports/config; show compile/run commands; record actual output; explain important lines; explain why the example is written this way; show an incorrect variation when that teaches something useful.

Prefer in-chapter snippets plus links to `examples/` over “see the demo” alone when the demo is required to understand the point.

### Example verification levels

| Label | Meaning |
|-------|---------|
| **Verified** | Compiled **and** executed on the documented JDK; output recorded from that run |
| **Compile-verified** | Compiled successfully; not executed |
| **Illustrative** | Conceptual snippet; not independently runnable |

Never invent output. Never label an example **Verified** because the output merely looks plausible.

## Reasoning and trade-offs

Explain not only how to write code, but:

- Why this approach is used
- Why an alternative may be worse
- What happens if a different approach is chosen
- What trade-offs matter in real projects

## Practical context

Connect concepts to real development when it adds value: backend apps, REST APIs, databases, enterprise systems, concurrency, distributed systems, production operations, performance-sensitive code. Do not force strained examples.

## Chapter format

Use [templates/chapter-template.md](templates/chapter-template.md) as the **master template**.

**Sections are conditional unless marked mandatory.** Include a section when it adds meaningful learning value. Do not manufacture content to fill headings. For omitted sections, record the reason in the chapter review checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow (or merge note) | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs (strongly encouraged for Technical / Deep-dive) |
| Common mistakes | Compatibility details |
| Summary and references | — |

Required spirit remains in force:

- Learning objectives, prerequisites, why the concept exists
- Beginner-friendly explanation; Revision and Mastery for major / Technical / Deep-dive chapters
- Progressive examples with accurate verification labels
- Common mistakes; references that support important claims

### Visual explanations

Require diagrams when they genuinely improve understanding—especially for JVM memory and execution, GC, thread synchronization, class loading, Spring bean lifecycle, DI, AOP proxies, transactions, Boot autoconfiguration, and stream pipelines/parallelism. Do not require diagrams for simple topics merely to satisfy a checklist. Explain every diagram in prose.

## Code examples

- Compile and run on the documented JDK (primary: Java 21) when claiming **Verified**
- Meaningful names; necessary imports; consistent formatting
- Prefer self-contained programs under `examples/<area>/...`
- Label verification status accurately; never invent output
- Record `java -version` / `javac -version` when documenting Verified results
- Identify simplified examples; never present insecure code as production-ready
- Show incorrect implementations with corrections when teaching pitfalls

## Guarantees vs implementation

- Distinguish Java Language Specification (JLS) from Java SE APIs
- Distinguish JVM Specification requirements from HotSpot details
- Distinguish framework behavior from language/runtime guarantees
- Label preview, incubator, deprecated, and removed features
- Never present HotSpot behavior as a universal JVM guarantee
- Never present a Spring (or other framework) implementation detail as a Java language rule

## Version policy

- Primary learning path: **stable Java 21** features
- Document newer LTS (25) and feature releases (e.g. 27) in version notes / Module 19
- For each version-sensitive claim: version introduced, status (final / preview / incubator / deprecated / removed), production notes

## Curriculum philosophy

- Preserve core Java and foundations before Spring Boot and heavy frameworks
- Spring Boot is an important practical section, not a replacement for understanding Java
- Keep exercises, assessments, and projects separate from main explanatory chapters where the architecture supports that
- Main documentation must serve as both textbook and revision reference

## Citations and technical source hierarchy

Prefer sources in this order for technical claims:

1. **JLS** — Java language semantics and rules
2. **JVMS** — JVM specification and runtime contracts
3. **Official Java API documentation** — API behavior
4. **OpenJDK source and documentation** — implementation details
5. **Framework documentation and source** — framework behavior (Spring, etc.)
6. **Authoritative books and technical articles** — explanations and supplementary context

Maintain [REFERENCES.md](REFERENCES.md); cite in chapters when claims are version-sensitive or easy to get wrong. Never fabricate citations, API behavior, or test results. References should support important claims throughout the chapter—not only the Internal Behavior section.

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
Never mark a chapter `Completed` merely because it was reviewed or lightly edited—the **depth gate**, **Mandatory Depth Review (Levels 1–4)**, and review checklist must all pass.  
Never mark a chapter `Completed` at Level 1 awareness when the topic requires Level 3 or 4.

## Assessments and solutions

- Keep solutions in separate `*-solutions.md` files
- Do not place answers immediately after every exercise if that spoils practice
- Map diagnostic questions to handbook chapters
- After all chapters in a module are `Completed`, create the module assessment
- Solutions must explain **reasoning**, not only final code: intended concept, step-by-step explanation, complete code where applicable, expected output, why it works, and useful alternatives/trade-offs

## Pilot lessons (Module 17 Streams)

Validated by the Module 17 Streams pilot (2026-11-10) under these standards:

- **Technical** profile chapters need a real **Mechanism and Execution Flow** section (pipeline stages, laziness, short-circuit, collector stages) even when API tables already exist.
- **Deep-dive** chapters (parallel streams) must label Fork/Join and common-pool sizing as **JDK/HotSpot implementation** context, not language or JVMS law.
- Conditional omission works: Stream chapters omitted Security and language-comparison sections with checklist notes—no filler.
- Example **Verified** labels must come from real runs; parallel demos must warn that lucky correct sizes (`badSize=5`) are not safety proofs.
- Profile assignment matters: Ch.01–03 as Technical (~430–490 lines of teaching) vs Ch.04 as Deep-dive (~700 lines) with heavier internals—do not force identical depth.
- Learning objectives can stay stable during expand-preserve retrofits; structure and mechanism prose are what change.

Refine this subsection when later pilots change durable rules. **Do not** retrofit other modules without an explicit scoped pass.
