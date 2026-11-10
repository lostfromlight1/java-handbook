# Compatibility and Migrations

> **Status:** Completed  
> **Module:** 51 — Enterprise and Large-Scale Engineering  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3

## 1. Learning Objectives

- Plan **JDK upgrades** (8→11→17→21) with dependency and bytecode checks on Temurin 21.
- Maintain **binary and semantic compatibility** for published APIs.
- Execute **database** and **API** migrations with expand/contract.
- Use **deprecation**, feature flags, and dual-read/write where needed.

## 2. Prerequisites

- **Module 19** — modern Java features and version policy.
- **Module 38** — API versioning.
- **Module 48 Ch.03** — deploy/migration ordering.

## 3. Why This Concept Exists

Enterprises cannot stop the world for every JDK or schema change. **Compatibility** preserves callers (teams, mobile apps, partners). **Migrations** move data and behavior safely across versions.

## 4. Conceptual Explanation

### JDK migration checklist

| Step | Action |
|------|--------|
| Inventory | `jdeps`, `jdeprscan` on application JAR |
| Build | `--release 21` / `maven.compiler.release` |
| Dependencies | Upgrade frameworks (Spring 6 needs 17+) |
| Removed APIs | JAXB, JavaEE modules → standalone deps |
| JPMS | `--add-opens` for reflection-heavy libs (Module 50) |
| Test | Full CI on Temurin 21.0.12.1 |

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

1. Add nullable column → deploy tolerating null.
2. Backfill job.
3. Make NOT NULL → remove old path → drop column later.

### Library ecosystem lag

Hibernate, agents, Graal metadata must match target JDK (Modules 49–50).

## 5. Mechanism and Execution Flow

### Expand/contract column rename

```mermaid
sequenceDiagram
  participant DBA as Migration job
  participant AppV1 as App v1
  participant AppV2 as App v2
  DBA->>DB: ADD email_address NULL
  AppV1->>DB: dual-write email + email_address
  DBA->>DB: backfill
  AppV2->>DB: read email_address only
  DBA->>DB: DROP email (later release)
```

Never deploy code that **requires** new schema before migration runs (Module 48).

## 6. Real-World Use Cases

- LTS jump with Spring Boot parent BOM bump.
- Mobile on v1 API while v2 rolls out with sunset header.

## 7. Edge Cases

- **Serialization** across versions — `serialVersionUID`; avoid Java serialization at public boundaries when possible.
- **Time zone** rule changes in JDK — retest `java.time`.

## 8. Common Mistakes

### Incorrect — compile on 21, run on 11

`UnsupportedClassVersionError`.

### Corrected — align CI, container image, prod JDK.

### Incorrect — breaking API without version bump

Silent client crashes.

### Corrected — `/v2` prefix or version header (Module 38).

## 9. Design Trade-offs

| Big-bang JDK weekend | Rolling team migration |
|----------------------|-------------------------|
| Intense window | Longer dual support |

## 10. Revision and Mastery

- What does `jdeprscan` help find?
- Safe order: DB migration vs app for column rename?

## 11. Exercises

1. List handbook modules to revisit before JDK 21 production cutover.
2. Write expand/contract steps for `email` → `email_address`.

## 12. Chapter Summary

Compatibility is explicit versioning, staged schema change, and JDK/dependency alignment tested in CI on Temurin 21—not silent breaks.

## Next Steps

[03-production-readiness.md](03-production-readiness.md)

## Review Checklist

- [x] Technical L2–3; expand/contract mechanism
- [x] JDK, API, schema migration preserved
