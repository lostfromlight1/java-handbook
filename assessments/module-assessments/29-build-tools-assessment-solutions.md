# Solutions: Module 29 — Build Tools

1. Search user classes and resources in listed directories/JARs.
2. Thin = app only; fat/uber bundles dependencies into one JAR.
3. Identify artifact in repositories and dependency graphs.
4. `test` (part of default lifecycle through `test`).
5. Available on test compile/runtime classpath, not main compile.
6. `requires`.
7. Which packages other modules may read at compile and runtime.
8. `java --module-path mods -m module.name/pkg.Main`.
9. Encapsulation and duplicate/split package risks; loader layering confusion.
10. `compile` removed; use `implementation`/`api` for modern Java plugin.

11. `greet=Hello, Handbook!`
12. `module=app`
