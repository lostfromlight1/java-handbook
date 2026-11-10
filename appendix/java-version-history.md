# Java Version History (Seed)

> **Status:** Drafted (seed)  
> **Purpose:** Support the handbook version policy—not a full historical essay yet.

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

Phase 1 examples were validated with:

```text
java version "21.0.9" 2025-10-21 LTS
javac 21.0.9
```

Update this section when the validation JDK changes.
