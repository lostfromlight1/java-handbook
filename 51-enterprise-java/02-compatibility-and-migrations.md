# Compatibility and Migrations

> **Status:** Completed  
> **Module:** 51 — Enterprise and Large-Scale Engineering  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Plan **JDK upgrades** (8→11→17→21) with dependency and bytecode checks.
- Maintain **binary and semantic compatibility** for published APIs.
- Execute **database** and **API** migrations with expand/contract.
- Use **deprecation**, feature flags, and dual-read/write where needed.

## 2. Prerequisites

- **Module 19** — modern Java features and version policy.
- **Module 38** — API versioning.
- **Module 48 Ch.03** — deploy/migration ordering.

## 3. Why This Concept Exists

Enterprises cannot stop the world for every JDK or schema change. **Compatibility** preserves callers (other teams, mobile apps, integration partners). **Migrations** move data and behavior safely across versions.

## 4. Conceptual Explanation

### JDK migration checklist

| Step | Action |
|------|--------|
| Inventory | `jdeps`, `jdeprscan` on application JAR |
| Build | Set `--release 21` or `maven.compiler.release` |
| Dependencies | Upgrade frameworks (Spring 6 needs 17+) |
| Removed APIs | JAXB, JavaEE modules → standalone deps |
| JPMS | `--add-opens` for reflection-heavy libs |
| Test | Full CI on Temurin 21 |

**jdeps --jdk-internals** flags illegal JDK internal access.

### API compatibility

| Change type | Client impact |
|-------------|----------------|
| Add method to interface | Breaks unless `default` method |
| Narrow return type | Binary break |
| Add optional JSON field | Usually safe |
| Rename field | Break unless versioned media type |

Use **semantic versioning** for public JARs; **consumer-driven contracts** for REST.

### Schema expand/contract (recap)

1. Add nullable column → deploy reader/writer tolerating null.
2. Backfill job.
3. Make NOT NULL → remove old code path → drop old column later.

### Library ecosystem lag

Hibernate, bytecode agents, and application servers must match target JDK. Validate **native agents** and **Graal** metadata if used (Modules 49–50).

## 5. Real-World Use Cases

- LTS jump coordinated with Spring Boot parent BOM bump.
- Mobile app on v1 API while v2 rolls out with sunset header.

## 6. Edge Cases

- **Serialization** across versions — `serialVersionUID`, avoid Java serialization for public boundaries when possible.
- **Time zone** rule changes (JDK updates) — retest `java.time` assumptions.

## 7. Common Mistakes

### Incorrect — compile on 21 but run on 11 server

`UnsupportedClassVersionError`.

### Corrected — align CI, image, prod JDK.

### Incorrect — breaking API without version bump

Silent mobile client crashes.

### Corrected — `/v2` prefix or negotiate version header.

## 8. Design Trade-offs

| Big-bang JDK upgrade weekend | Rolling team-by-team migration |
|------------------------------|--------------------------------|
| Short intense window | Longer dual support |

## 9. Revision and Mastery

- What does `jdeprscan` help find?
- Safe order: DB migration vs app deploy for column rename?

## 10. Exercises

1. List handbook modules to revisit before JDK 21 production cutover.
2. Write expand/contract steps to rename `email` column to `email_address`.

## 11. Chapter Summary

Compatibility is explicit versioning, staged schema change, and JDK/dependency alignment tested in CI on Temurin 21—not hope that clients tolerate silent breaks.

## Next Steps

[03-production-readiness.md](03-production-readiness.md)

## Review Checklist

- [x] JDK, API, schema migration; mistakes
