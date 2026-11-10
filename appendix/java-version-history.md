# Java Version History (Seed)

> **Status:** Drafted (seed)  
> **Purpose:** Support the handbook version policy—not a full historical essay yet.  
> **Last updated:** 2026-11-10

## Why this appendix exists

Java evolves on a predictable cadence. Without a clear version policy, chapters disagree about which APIs are “current,” and learners confuse **language features**, **JDK tools**, and **vendor builds**. This seed defines the handbook’s primary target and how later versions appear in notes.

A fuller narrative history (motivations per release, major JEPs) can expand this file later without changing the policy below.

## Key terms

| Term | Meaning |
|------|---------|
| **GA** | General Availability—the public release date of a Java SE version |
| **LTS** | Long-Term Support—releases intended for longer production support windows (for example 17, 21, 25) |
| **Feature release (non-LTS)** | Six-month cadence releases with a shorter support window; useful for early access to features, not the main curriculum path |
| **Preview / incubator** | Features shipping for feedback; not final. Label them; do not treat them as production defaults in main-path chapters |
| **Distribution** | A concrete JDK build (Oracle JDK, Eclipse Temurin, Amazon Corretto, …) implementing Java SE |

## Version policy for this handbook

| Role | Version | Notes |
|------|---------|--------|
| **Primary curriculum** | **Java 21 LTS** | All main-path examples target Java 21 |
| Latest LTS (documented) | Java 25 | Prefer documenting differences; not required to follow main path |
| Latest feature release (awareness) | Java 27 (non-LTS) | Short support window; see Oracle roadmap |

Sources (verified November 2026):

- [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/) — JDK 27 latest feature release; JDK 25 latest LTS
- [Oracle Java SE Support Roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) — LTS releases include 8, 11, 17, 21, 25; next planned LTS Java 29 (September 2027)

## LTS releases (selected)

| Version | GA (approx.) | Role in this handbook |
|---------|--------------|------------------------|
| 8 | 2014 | Legacy production; teach differences when relevant |
| 11 | 2018 | Legacy LTS |
| 17 | 2021 | Common production LTS |
| **21** | **2023** | **Primary learning version** |
| 25 | 2025 | Current LTS; covered in modern-features / migration notes |

## Feature releases after 21 (awareness)

Non-LTS feature releases arrive roughly every six months. Once superseded, they are not the main teaching target. Document post-21 features in Module 19 with **introduced-in** and **status** labels (final / preview / incubator / deprecated / removed).

| Version | Type | Handbook treatment |
|---------|------|--------------------|
| 22–24 | Non-LTS | Version notes only |
| 25 | LTS | Differences from 21 |
| 26 | Non-LTS | Awareness |
| 27 | Non-LTS (current feature as of Nov 2026) | Awareness |

## Local validation

Phase 1 module examples (Modules 00–03) were validated primarily with:

```text
java version "21.0.9" 2025-10-21 LTS
javac 21.0.9
```

Language-level rechecks during the 2026-11-10 documentation quality pass also ran on Temurin JDK **21.0.12.1** (same Java SE 21 language level). Prefer recording the exact `java -version` when documenting new example output.

Update this section when the validation JDK changes.
