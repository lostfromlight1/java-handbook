# Progress Tracker

**Target Java version:** 21 LTS  
**Validation JDK (Phase 1):** Oracle JDK 21.0.9 (`javac 21.0.9`)  
**Module 04 validation JDK:** Temurin 21.0.12.1 (`javac 21.0.12.1`)  
**Quality-pass JDK check (2026-11-10):** Temurin 21.0.12.1 (language-level rechecks; Phase 1 example outputs remain those recorded on 21.0.9)  
**Last updated:** 2026-11-10 (Module 06 completed)

## Status values

`Not started` · `In progress` · `Drafted` · `Needs review` · `Needs revision` · `Completed`

Only mark `Completed` after the quality checklist in `DOCUMENTATION-STANDARDS.md` passes.

---

## Infrastructure

| Item | Status | Notes |
|------|--------|-------|
| Repository structure | Completed | Modules 00–53 indexes; examples; assessments; projects |
| DOCUMENTATION-STANDARDS.md | Completed | Depth gate + explain-now / expand-later (2026-11-10) |
| README / CONTRIBUTING / GLOSSARY / REFERENCES | Completed | Glossary seeded from Ch.01; README/CONTRIBUTING aligned to teaching standards |
| LEARNING-ROADMAP.md | Completed | Dual tracks + dependency map + progressive-explanation table |
| Templates | Completed | chapter template updated for standalone / progressive teaching |
| Cursor rules | Completed | 6 rules updated for depth / standalone / progressive teaching |
| Cursor agents | Completed | 7 agents updated to match new standards |
| java-documentation skill | Completed | SKILL + review-checklist expanded |
| Diagnostic framework | Completed | README + scoring + area index |
| appendix/java-version-history.md | Drafted | Seed table |
| Git initialized | Completed | Local only; no remote required |

---

## Appendix

| Document | Status | Review | Outstanding |
|----------|--------|--------|-------------|
| java-version-history.md | Drafted | Quality pass 2026-11-10 (terms + policy) | Expand full history narrative later |
| java-quick-reference.md | Not started | | |
| jvm-reference.md | Not started | | |
| common-errors.md | Not started | | |
| interview-glossary.md | Not started | | |
| java-vs-go.md | Not started | | |
| java-vs-csharp.md | Not started | | |
| java-vs-python.md | Not started | | |

---

## Module 00 — Programming Foundations

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-what-is-programming-and-what-is-java.md](00-programming-foundations/01-what-is-programming-and-what-is-java.md) | Completed | Quality pass 2026-11-10 | Deeper `main`/bytecode/GC intros |
| [02-how-computers-execute-programs.md](00-programming-foundations/02-how-computers-execute-programs.md) | Completed | Quality pass 2026-11-10 | Stack/heap/thread + `static` helpers |
| [03-algorithms-and-problem-solving.md](00-programming-foundations/03-algorithms-and-problem-solving.md) | Completed | Quality pass 2026-11-10 | In-chapter `for`/`if` teaching |
| [04-data-and-basic-structures.md](00-programming-foundations/04-data-and-basic-structures.md) | Completed | Quality pass 2026-11-10 | References, casting, class preview |
| [05-complexity-and-debugging-mindset.md](00-programming-foundations/05-complexity-and-debugging-mindset.md) | Completed | Quality pass 2026-11-10 | Stronger debugging vocabulary |
| [Module assessment](assessments/module-assessments/00-programming-foundations-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Oracle JDK 21.0.9):**

| Example | Result |
|---------|--------|
| `examples/beginner/hello-world` | `Hello, Java!` |
| `examples/beginner/execution-order` | Ordered prints 1–6 as documented |
| `examples/beginner/find-max` | `Maximum score: 91` |
| `examples/beginner/scoreboard` | Names/scores + average `91.66666666666667` |
| `examples/beginner/debug-off-by-one` | Bug `10`, fixed `15` |

**Module 00 status:** Completed  
**Next recommended chapter after M00:** Module 01 (Completed below)

---

## Module 01 — Introduction to Java

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-jdk-jre-jvm-and-tooling.md](01-java-getting-started/01-jdk-jre-jvm-and-tooling.md) | Completed | Quality pass 2026-11-10 | Classpath + JVM launch + `javap` |
| [02-installing-java-and-environment.md](01-java-getting-started/02-installing-java-and-environment.md) | Completed | Quality pass 2026-11-10 | PATH/JAVA_HOME as problem/solution |
| [03-ide-and-command-line-workflow.md](01-java-getting-started/03-ide-and-command-line-workflow.md) | Completed | Quality pass 2026-11-10 | CLI/IDE trade-offs clarified |
| [04-first-java-program-deep-dive.md](01-java-getting-started/04-first-java-program-deep-dive.md) | Completed | Quality pass 2026-11-10 | `main` keywords + parseInt preview |
| [05-project-structure-and-jshell.md](01-java-getting-started/05-project-structure-and-jshell.md) | Completed | Quality pass 2026-11-10 | Packages/classpath narrative |
| [Module assessment](assessments/module-assessments/01-java-getting-started-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Oracle JDK 21.0.9):**

| Example | Result |
|---------|--------|
| `examples/beginner/runtime-info` | `java.version = 21.0.9`, `java.home` = jdk-21 |
| `examples/beginner/args-echo` | 0 args tip; 3 args `hello`/`Java`/`21` |
| `examples/beginner/main-deep-dive` | exits 1 / 2 / 0 as documented |
| `examples/beginner/classpath-demo` | `Hello, Handbook!` with `-d out` / `-cp out` |

**Module 01 status:** Completed  
**Next recommended chapter after M01:** Module 02 (Completed below)

---

## Module 02 — Java Fundamentals

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-variables-and-types.md](02-java-fundamentals/01-variables-and-types.md) | Completed | Quality pass 2026-11-10 | References, defaults, overflow, autoboxing |
| [02-operators-and-expressions.md](02-java-fundamentals/02-operators-and-expressions.md) | Completed | Quality pass 2026-11-10 | Precedence, short-circuit, `==`/`equals` |
| [03-scope-naming-and-comments.md](02-java-fundamentals/03-scope-naming-and-comments.md) | Completed | Quality pass 2026-11-10 | Shadowing, Javadoc, parameter scope |
| [04-type-conversion-and-inference.md](02-java-fundamentals/04-type-conversion-and-inference.md) | Completed | Quality pass 2026-11-10 | Widening ladder, `var` failures, casts |
| [Module assessment](assessments/module-assessments/02-java-fundamentals-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Oracle JDK 21.0.9):**

| Example | Result |
|---------|--------|
| `examples/core-java/primitives-demo` | All primitive samples + String printed |
| `examples/core-java/operators-demo` | `10/3=3`, precedence 14/20, `++` behavior |
| `examples/core-java/scope-demo` | Block/loop scope as documented |
| `examples/core-java/conversion-demo` | widen 42.0; narrow 3; `5/2=2`; `5/2.0=2.5`; var |

**Module 02 status:** Completed  
**Next recommended chapter after M02:** Module 03 (Completed below)

---

## Module 03 — Control Flow

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-conditionals.md](03-control-flow/01-conditionals.md) | Completed | Quality pass 2026-11-10 | Grades, guards, ternary guidance |
| [02-switch-and-switch-expressions.md](03-control-flow/02-switch-and-switch-expressions.md) | Completed | Quality pass 2026-11-10 | Fall-through, exhaustiveness, `yield` |
| [03-loops.md](03-control-flow/03-loops.md) | Completed | Quality pass 2026-11-10 | Continue/break walk-through, labels |
| [04-branching-and-pattern-matching.md](03-control-flow/04-branching-and-pattern-matching.md) | Completed | Quality pass 2026-11-10 | Sealed Shape, `when`, dominance |
| [Module assessment](assessments/module-assessments/03-control-flow-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Oracle JDK 21.0.9):**

| Example | Result |
|---------|--------|
| `conditionals-demo` | score 85 → grade B |
| `switch-demo` | MON → weekday; label two |
| `loops-demo` | for/while/continue-break as documented |
| `pattern-control-demo` | patterns + sealed shape areas |

**Module 03 status:** Completed  
**Next recommended chapter:** Module 04 (see below)

---

## Module 04 — Methods and Functions

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-methods-basics.md](04-methods-and-functions/01-methods-basics.md) | Completed | Quality pass 2026-11-10 | Guards, return/`void`, static preview |
| [02-overloading-varargs-and-recursion.md](04-methods-and-functions/02-overloading-varargs-and-recursion.md) | Completed | Quality pass 2026-11-10 | Ambiguity, varargs, recursion vs iteration |
| [03-pass-by-value-and-stack-frames.md](04-methods-and-functions/03-pass-by-value-and-stack-frames.md) | Completed | Quality pass 2026-11-10 | Pass-by-value myth; stack frames; JLS/JVMS/HotSpot |
| [04-static-instance-and-method-references.md](04-methods-and-functions/04-static-instance-and-method-references.md) | Completed | Quality pass 2026-11-10 | `this`, static fields, method references preview |
| [Module assessment](assessments/module-assessments/04-methods-and-functions-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `methods-basics-demo` | greet Ada; sum 8; passing/failing; clamp 100/0 |
| `overloading-demo` | max 9/9/2.25; join; factorial 120; sumTo 10 |
| `pass-by-value-demo` | x=10; nums[0]=100; sb=Hi!; s=hello |
| `static-instance-demo` | ids 1–2; ticks 2/1; ADA/LIN/ZOE; sum 6 |

**Module 04 status:** Completed  
**Next recommended chapter:** Module 05 (Completed below)

---

## Module 05 — Object-Oriented Programming

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-oop-fundamentals.md](05-object-oriented-programming/01-oop-fundamentals.md) | Completed | Quality pass 2026-11-10 | Class/object, `new`, identity, encapsulation preview |
| [02-encapsulation-and-abstraction.md](05-object-oriented-programming/02-encapsulation-and-abstraction.md) | Completed | Quality pass 2026-11-10 | Access modifiers, invariants, Temperature API |
| [03-inheritance-and-polymorphism-intro.md](05-object-oriented-programming/03-inheritance-and-polymorphism-intro.md) | Completed | Quality pass 2026-11-10 | `extends`, `super`, polymorphism intro |
| [04-composition-and-object-relationships.md](05-object-oriented-programming/04-composition-and-object-relationships.md) | Completed | Quality pass 2026-11-10 | has-a, object graphs |
| [Module assessment](assessments/module-assessments/05-object-oriented-programming-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `oop-fundamentals-demo` | withdraw ok; Alice 85 cents; Bob 50; sameRef true |
| `encapsulation-demo` | celsius 25.0; fahrenheit 77.0; absolute zero rejected |
| `inheritance-poly-demo` | pays 50000/60000/40000; polymorphic 65000 |
| `composition-demo` | Civic I4; Sherlock London; ORD-1 total 7500 cents |

**Module 05 status:** Completed  
**Next recommended chapter:** Module 06 (Completed below)

---

## Module 06 — Classes and Object Design

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-initialization-and-constructors.md](06-classes-and-objects/01-initialization-and-constructors.md) | Completed | Quality pass 2026-11-10 | `super()` chaining in M07 |
| [02-nested-and-inner-classes.md](06-classes-and-objects/02-nested-and-inner-classes.md) | Completed | Quality pass 2026-11-10 | Lambdas vs anonymous deep dive in M09 |
| [03-immutability-and-builders.md](06-classes-and-objects/03-immutability-and-builders.md) | Completed | Quality pass 2026-11-10 | Records as alternative carriers |
| [04-equals-hashcode-and-tostring.md](06-classes-and-objects/04-equals-hashcode-and-tostring.md) | Completed | Quality pass 2026-11-10 | Collection internals in M18 |
| [Module assessment](assessments/module-assessments/06-classes-and-objects-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `constructors-demo` | `ada/ada`, `lin/Lin`, `created=2`, `point=(3,4)` |
| `nested-classes-demo` | StaticNested, inner, local, anonymous lines as documented |
| `immutability-builder-demo` | `1250 USD`; GET request line |
| `equals-hashcode-demo` | equals/hash true/false; `setSize=2` |

**Module 06 status:** Completed  
**Next recommended chapter:** `07-inheritance-and-polymorphism/` (advanced inheritance, contracts)

---

## Modules 07–53

Remaining modules: **Not started** (indexes only). Do not treat indexes as written chapters. Future chapters must follow the explain-now / expand-later standards in `DOCUMENTATION-STANDARDS.md` and the progressive-explanation table in `LEARNING-ROADMAP.md`.
