# Static Analysis and CI

> **Status:** Completed  
> **Module:** 30 — Testing and Code Quality  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** `javac -Xlint:all` on Temurin 21.0.12.1 (compiler warnings)

## 1. Learning Objectives

- Use **`javac -Xlint`** for compiler warnings.
- Describe **Checkstyle**, **SpotBugs**, and **SonarQube** roles.
- Sketch a **CI pipeline** that runs tests and quality gates on every change.

## 2. Prerequisites

- Ch.02 — Maven `verify` phase.
- Module **29** — reproducible builds.

## 3. Why This Concept Exists

Tests catch functional regressions; **static analysis** catches latent bugs (null dereferences, resource leaks, concurrency issues) without running every path. **Continuous Integration (CI)** runs build + tests + analysis on each commit so main stays releasable.

## 4. Conceptual Explanation

| Tool | Inspects | Typical integration |
|------|----------|---------------------|
| `javac -Xlint` | Language-level warnings (deprecation, raw types) | Local + Maven compiler plugin |
| Checkstyle | Formatting/naming conventions | Maven/Gradle plugin |
| SpotBugs | Bytecode bug patterns | `spotbugs-maven-plugin` |
| PMD | Source rules (complexity, unused code) | Maven plugin |
| SonarQube | Aggregated metrics, security hotspots | CI scanner + server |

**CI stages (typical):**

1. Checkout + cache dependencies.
2. `mvn -B verify` (compile, test, package).
3. Upload artifacts / Docker image.
4. Optional: SAST, dependency CVE scan (`OWASP dependency-check`).

## 5. Step-by-Step Examples

Compiler lint on a tiny source file:

```bash
javac -Xlint:all -Werror AssertDemo.java
```

`-Werror` promotes warnings to errors — strict in CI. On handbook `AssertDemo.java`, default compile is clean (no warnings).

**Maven CI command** used in practice:

```bash
mvn -B -q verify
```

`-B` batch mode for non-interactive CI logs.

## 6. Real-World Use Cases

- Blocking merges when Surefire failures or SpotBugs high-priority findings exist.
- Enforcing **Google Java Format** or org Checkstyle rules.
- Tracking technical debt trends in Sonar dashboards.

## 7. Edge Cases and Limitations

- Linters produce **false positives** — tune rule sets.
- Analysis without tests gives false confidence.
- `SNAPSHOT` dependencies break reproducibility in CI caches.

## 8. Common Mistakes

### Incorrect

CI runs only `compile`, skipping `test`.

### Corrected

Use `verify` or explicit `test` + integration-test modules.

## 9. Best Practices

- Fail on **unsuppressed** high-severity findings; document suppressions with justification.
- Run the **same JDK** as production (21 LTS).
- Separate **fast** PR checks from nightly full scans.

## 10. Chapter Summary

Static analysis complements tests; CI automates both on every change. Start with compiler lint and Maven `verify`, then add SpotBugs/Sonar as maturity grows.

## 11. Next Steps

Module **31** — Debugging and Troubleshooting.

## Review Checklist

- [x] javac -Xlint documented; Maven verify aligned with Module 29 demos
