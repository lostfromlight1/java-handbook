# Java Version History

> **Status:** Completed  
> **Profile:** Conceptual / Technical reference (Levels 2–3)  
> **Purpose:** Handbook version policy, LTS feature landmarks, and validation JDK notes—not a full curriculum chapter.  
> **Last updated:** 2026-11-11  
> **Primary target:** Java 21 LTS

## Why this appendix exists

Java evolves on a fixed six-month feature cadence with selected **Long-Term Support (LTS)** releases. Without a clear version policy, chapters disagree about which APIs are “current,” and learners confuse **language features**, **JDK tools**, and **vendor builds**. This appendix fixes the handbook’s target version, summarizes what each major LTS brought, and records how preview/incubator material is handled.

For deeper treatment of Java 21+ APIs, see [Module 19 — Modern Java Features](../19-modern-java-features/README.md). For the official support windows, prefer Oracle’s roadmap over secondary blogs.

## Key terms

| Term | Meaning |
|------|---------|
| **GA** | General Availability—the public release date of a Java SE version |
| **LTS** | Long-Term Support—releases intended for longer production support windows (for example 8, 11, 17, 21, 25) |
| **Feature release (non-LTS)** | Six-month cadence releases with a shorter support window; useful for early access to features, not the main curriculum path |
| **JEP** | JDK Enhancement Proposal—the design document for a JDK change |
| **Preview** | Language or API feature shipping for feedback under `--enable-preview`; may change or be withdrawn |
| **Incubator** | Module shipped for feedback (often under `jdk.incubator.*`); not a production default |
| **Distribution** | A concrete JDK build (Oracle JDK, Eclipse Temurin, Amazon Corretto, …) implementing Java SE |

**GA vs LTS:** Every six months Oracle ships a feature release. Only some of those are designated LTS. Non-LTS releases are still real Java SE versions; they simply leave the “main support” path sooner. The handbook teaches **stable Java 21** as the primary path and documents later LTS differences separately.

## Version policy for this handbook

| Role | Version | Notes |
|------|---------|--------|
| **Primary curriculum** | **Java 21 LTS** | All main-path examples target Java 21 |
| Latest LTS (documented) | Java 25 | Prefer documenting differences; not required for the main path |
| Latest feature release (awareness) | Java 27 (non-LTS) | Short support window; see Oracle roadmap |
| Next planned LTS | Java 29 (September 2027) | Subject to Oracle’s published plan |

Sources (checked November 2026):

- [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/) — JDK 27 as latest feature release; JDK 25 as latest LTS
- [Oracle Java SE Support Roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) — LTS releases include 8, 11, 17, 21, 25; next planned LTS Java 29 (September 2027)

### How version-sensitive claims appear in chapters

1. State the **version introduced** when it matters (for example “final in Java 21”).
2. Label status: **final** / **preview** / **incubator** / **deprecated** / **removed**.
3. Prefer stable Java 21 APIs in main-path examples.
4. Put post-21 material in Module 19 or short version notes—not as silent defaults in beginner chapters.

## Preview and incubator policy

**Preview** and **incubator** features exist so the platform can gather feedback before freezing semantics.

| Kind | Typical enablement | Handbook rule |
|------|--------------------|---------------|
| Preview language/API | `--enable-preview` (and often `--release N` / `--source N`) | Teach with an explicit label; never present as the default production style in Track A chapters |
| Incubator module | `--add-modules jdk.incubator.…` | Mention for awareness; do not build core exercises on incubator APIs |
| Finalized later | Same feature may reappear without flags | Update Module 19 and this appendix when status changes |

**Why the policy matters:** Preview syntax can change between releases. A chapter that silently uses a preview construct on JDK 21 will break or mislead readers on another machine that omits the flag. Module 19 Chapter 03 covers the mechanics; this appendix states the curriculum rule.

**Practical checklist for authors:**

- Prefer the final form if it exists on Java 21.
- If a demo must use preview (for example structured concurrency awareness), record the exact `javac`/`java` flags and label the example **Verified** only after a real run with those flags.
- Do not invent “Verified” output for preview runs you did not execute.

## LTS landscape (selected)

| Version | GA (approx.) | Role in this handbook |
|---------|--------------|------------------------|
| 8 | March 2014 | Legacy production baseline; teach differences when relevant |
| 11 | September 2018 | Legacy LTS still common in enterprises |
| 17 | September 2021 | Common production LTS |
| **21** | **September 2023** | **Primary learning version** |
| 25 | September 2025 | Current LTS; covered in modern-features / migration notes |

Oracle’s roadmap also lists Premier/Extended support end dates per release. Treat those dates as vendor-specific: other distributions may offer different commercial support even when the language level is the same.

## Major feature highlights by LTS

The lists below are **landmarks for orientation**, not complete release notes. Prefer OpenJDK JEP indexes and Oracle release notes when you need exhaustiveness.

### Java 8 (2014) — modern language baseline

Java 8 is the oldest LTS many production shops still discuss. It introduced the style that later modules assume as “ordinary Java.”

| Area | Highlights |
|------|------------|
| Language | Lambda expressions, method references, default/static interface methods |
| Libraries | Stream API, `Optional`, `java.time` (JSR-310) |
| Concurrency | `CompletableFuture` foundations for async composition |
| Tooling culture | Functional pipelines alongside classic OOP |

**Handbook angle:** Modules 16–18 deepen lambdas, streams, and Optional. Do not treat Java 8 as “obsolete trivia”—many interview and migration conversations still start here.

### Java 11 (2018) — first post-module LTS

Java 11 is the first LTS after the Java 9 module system. Day-to-day application code often still looks “classpath classic,” but the platform underneath changed permanently.

| Area | Highlights |
|------|------------|
| Language | Local-variable type inference (`var`) for locals |
| Libraries | Standard `HttpClient`, useful `String` helpers (`isBlank`, `lines`, `strip`, `repeat`) |
| Platform | Module system (introduced in 9) is the LTS baseline; removed several Java EE / CORBA modules from the JDK |
| Runtime | Continued HotSpot evolution; flight-recorder related tooling matured across this era |

**Handbook angle:** `var` appears in fundamentals; HTTP client work belongs with networking modules. When readers say “we’re on 11,” ask whether they mean language level, bytecode target, or the installed JDK distribution.

### Java 17 (2021) — data-oriented language features

Java 17 consolidated several multi-release language projects into a widely adopted LTS.

| Area | Highlights |
|------|------------|
| Language | Sealed classes; pattern matching for `instanceof`; switch expressions (finalized earlier, ubiquitous by 17); text blocks; records (final in 16, mainstream by 17) |
| Libraries | Strong encapsulation of JDK internals by default (continued from 16); useful pseudo-random generators API |
| Platform | Context for migrating off 8/11; many organizations standardized here before 21 |

**Handbook angle:** Modules 03, 07, and 15 teach sealed types and patterns. Records and sealed hierarchies are preferred teaching tools for closed domain models on Java 21 as well.

### Java 21 (2023) — primary curriculum target

Java 21 is this handbook’s **primary learning version**. Main-path examples assume a Java 21 language level unless a section explicitly says otherwise.

| Area | Highlights (final unless noted) |
|------|----------------------------------|
| Concurrency | Virtual threads (JEP 444) |
| Language | Pattern matching for `switch` (JEP 441); record patterns (JEP 440) |
| Collections | Sequenced collections (JEP 431) |
| GC (HotSpot) | Generational ZGC (JEP 439) as a HotSpot collector option |
| Other | UTF-8 by default (from 18); Key Encapsulation Mechanism API (JEP 452) |

**Preview in 21 (examples only with flags):** structured concurrency, scoped values (later finalized on a newer release—see Java 25), unnamed patterns/variables, and other evolving JEPs. Always check the exact JDK 21 release notes for the build you run.

**Handbook angle:** Module 19 Chapter 01 is the feature tour; Module 24 covers virtual threads in depth; Module 27 covers collectors including ZGC awareness.

Authoritative starting points:

- [JEPs in JDK 21 since JDK 17](https://openjdk.org/projects/jdk/21/jeps-since-jdk-17)
- [JDK 21 release notes](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
- [JDK 21 API](https://docs.oracle.com/en/java/javase/21/docs/api/)

### Java 25 (2025) — current LTS (differences from 21)

Java 25 is the **current LTS** as of this writing. The handbook documents important differences but does **not** retarget the whole curriculum to 25.

| Area | Highlights (orientation) |
|------|--------------------------|
| Language | Module import declarations; compact source files and instance main methods; flexible constructor bodies |
| Concurrency / libraries | Scoped values finalized; structured concurrency still evolving via preview JEPs on the 25 line—verify status on your build |
| HotSpot / runtime | Compact object headers as a product feature; AOT ergonomics/profiling JEPs; Generational Shenandoah |
| Observability | Additional JFR-related JEPs (cooperative sampling, method timing/tracing)—HotSpot tooling |

**Handbook angle:** Use Module 19 Chapter 02 for post-21 notes. When citing a 25-only API in an appendix tip, label the version. Do not silently require JDK 25 for Module 00–18 exercises.

Starting points:

- [OpenJDK JDK 25 project](https://openjdk.org/projects/jdk/25/)
- [JDK 25 release notes](https://www.oracle.com/java/technologies/javase/25-relnote-issues.html)
- [Oracle announcement](https://www.oracle.com/news/announcement/oracle-releases-java-25-2025-09-16/)

## Feature releases after 21 (awareness)

Non-LTS feature releases arrive roughly every six months. Once superseded, they are not the main teaching target. Document post-21 features in Module 19 with **introduced-in** and **status** labels.

| Version | Type | Handbook treatment |
|---------|------|--------------------|
| 22–24 | Non-LTS | Version notes only |
| 25 | LTS | Differences from 21 |
| 26 | Non-LTS | Awareness |
| 27 | Non-LTS (current feature release as of Nov 2026) | Awareness |
| 28 | Non-LTS (planned) | Awareness |
| 29 | Planned LTS (Sep 2027) | Future curriculum decision |

**Migration mindset:** Moving 21 → 25 is usually about adopting new language conveniences and runtime improvements, not rewriting every class. Moving 8 → 21 is a larger jump (modules, removed APIs, stronger encapsulation, new idioms).

## Language level vs installed JDK vs distribution

Three different questions get conflated:

1. **What `javac --release` / language level am I compiling for?** (for example 21)
2. **Which JDK binary is on `PATH`?** (`java -version`)
3. **Which vendor built it?** (Oracle, Temurin, Corretto, …)

You can install JDK 25 and still compile with `--release 21`. Handbook **Verified** examples record the JDK that actually ran the demo. Matching language level across vendors is expected for standard SE APIs; matching every diagnostic flag or GC log format is a HotSpot/distribution concern—label those carefully.

## Local validation (handbook JDK notes)

Phase 1 module examples (Modules 00–03) were validated primarily with:

```text
java version "21.0.9" 2025-10-21 LTS
Java(TM) SE Runtime Environment (build 21.0.9+7-LTS-338)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.9+7-LTS-338, mixed mode, sharing)
javac 21.0.9
```

Language-level rechecks during the 2026-11-10 documentation quality pass also ran on Eclipse Temurin JDK **21.0.12.1** (same Java SE 21 language level). Later module passes recorded either Oracle 21.0.9 or Temurin 21.0.12.1 in `PROGRESS.md` and chapter headers.

**Author rules:**

- Prefer recording the exact `java -version` / `javac -version` when documenting new example output.
- Never invent **Verified** output.
- Update this section when the primary validation JDK changes.
- See also the validation lines at the top of [PROGRESS.md](../PROGRESS.md).

## Related reading

| Topic | Where |
|-------|-------|
| Java 21 feature tour | [19-modern-java-features/01-java-21-feature-tour.md](../19-modern-java-features/01-java-21-feature-tour.md) |
| Features after 21 | [19-modern-java-features/02-features-after-java-21.md](../19-modern-java-features/02-features-after-java-21.md) |
| Preview / incubator | [19-modern-java-features/03-preview-and-incubator-features.md](../19-modern-java-features/03-preview-and-incubator-features.md) |
| JDK / JRE / JVM basics | [01-java-getting-started/01-jdk-jre-jvm-and-tooling.md](../01-java-getting-started/01-jdk-jre-jvm-and-tooling.md) |
| Central bibliography | [REFERENCES.md](../REFERENCES.md) |

## References

1. Oracle, [Java SE Support Roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)
2. Oracle, [Java Downloads](https://www.oracle.com/java/technologies/downloads/)
3. OpenJDK, [JDK 21](https://openjdk.org/projects/jdk/21/) and [JDK 25](https://openjdk.org/projects/jdk/25/)
4. Oracle, [JDK 21 release notes](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
5. Oracle, [JDK 25 release notes](https://www.oracle.com/java/technologies/javase/25-relnote-issues.html)
6. JLS / JVMS indexes via [Java SE Specifications](https://docs.oracle.com/javase/specs/)
