# Progress Tracker

**Target Java version:** 21 LTS  
**Validation JDK (Phase 1):** Oracle JDK 21.0.9 (`javac 21.0.9`)  
**Module 04 validation JDK:** Temurin 21.0.12.1 (`javac 21.0.12.1`)  
**Quality-pass JDK check (2026-11-10):** Temurin 21.0.12.1 (language-level rechecks; Phase 1 example outputs remain those recorded on 21.0.9)  
**Last updated:** 2026-11-11 (Modules 38–45 EXPAND-PRESERVE; Modules 29–32; curriculum through M53)

## Status values

`Not started` · `In progress` · `Drafted` · `Needs review` · `Needs revision` · `Completed`

Only mark `Completed` after the quality checklist in `DOCUMENTATION-STANDARDS.md` passes.

---

## Infrastructure

| Item | Status | Notes |
|------|--------|-------|
| Repository structure | Completed | Modules 00–53 indexes; examples; assessments; projects |
| DOCUMENTATION-STANDARDS.md | Completed | Profiles, Mechanism flow, verification labels, source hierarchy, M17 pilot lessons (2026-11-10) |
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
| [01-what-is-programming-and-what-is-java.md](00-programming-foundations/01-what-is-programming-and-what-is-java.md) | Completed | Standards-alignment pass 2026-11-10 (Temurin re-verify) | — |
| [02-how-computers-execute-programs.md](00-programming-foundations/02-how-computers-execute-programs.md) | Completed | Standards-alignment pass 2026-11-10 (Temurin re-verify) | — |
| [03-algorithms-and-problem-solving.md](00-programming-foundations/03-algorithms-and-problem-solving.md) | Completed | Standards-alignment pass 2026-11-10 (Temurin re-verify) | — |
| [04-data-and-basic-structures.md](00-programming-foundations/04-data-and-basic-structures.md) | Completed | Standards-alignment pass 2026-11-10 (Temurin re-verify) | — |
| [05-complexity-and-debugging-mindset.md](00-programming-foundations/05-complexity-and-debugging-mindset.md) | Completed | Standards-alignment pass 2026-11-10 (Temurin re-verify) | — |
| [Module assessment](assessments/module-assessments/00-programming-foundations-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

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
| [01-jdk-jre-jvm-and-tooling.md](01-java-getting-started/01-jdk-jre-jvm-and-tooling.md) | Completed | Standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [02-installing-java-and-environment.md](01-java-getting-started/02-installing-java-and-environment.md) | Completed | Standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [03-ide-and-command-line-workflow.md](01-java-getting-started/03-ide-and-command-line-workflow.md) | Completed | Standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [04-first-java-program-deep-dive.md](01-java-getting-started/04-first-java-program-deep-dive.md) | Completed | Standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [05-project-structure-and-jshell.md](01-java-getting-started/05-project-structure-and-jshell.md) | Completed | Standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [Module assessment](assessments/module-assessments/01-java-getting-started-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

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
| [01-variables-and-types.md](02-java-fundamentals/01-variables-and-types.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | References, defaults, overflow, autoboxing |
| [02-operators-and-expressions.md](02-java-fundamentals/02-operators-and-expressions.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | Precedence, short-circuit, `==`/`equals` |
| [03-scope-naming-and-comments.md](02-java-fundamentals/03-scope-naming-and-comments.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | Shadowing, Javadoc, parameter scope |
| [04-type-conversion-and-inference.md](02-java-fundamentals/04-type-conversion-and-inference.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | Widening ladder, `var` failures, casts |
| [Module assessment](assessments/module-assessments/02-java-fundamentals-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `examples/core-java/primitives-demo` | All primitive samples + String printed |
| `examples/core-java/operators-demo` | `a/b=3`, precedence 14/20, postfix/prefix `++` |
| `examples/core-java/scope-demo` | Block/loop scope as documented |
| `examples/core-java/conversion-demo` | widen 42.0; narrow 3; `5/2=2`; `5/2.0=2.5`; var |

**Module 02 status:** Completed  
**Next recommended chapter after M02:** Module 03 (Completed below)

---

## Module 03 — Control Flow

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-conditionals.md](03-control-flow/01-conditionals.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | Grades, guards, ternary guidance |
| [02-switch-and-switch-expressions.md](03-control-flow/02-switch-and-switch-expressions.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | Fall-through, exhaustiveness, `yield` |
| [03-loops.md](03-control-flow/03-loops.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | Continue/break walk-through, labels |
| [04-branching-and-pattern-matching.md](03-control-flow/04-branching-and-pattern-matching.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | Sealed Shape, `when`, dominance |
| [Module assessment](assessments/module-assessments/03-control-flow-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

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
| [01-methods-basics.md](04-methods-and-functions/01-methods-basics.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [02-overloading-varargs-and-recursion.md](04-methods-and-functions/02-overloading-varargs-and-recursion.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [03-pass-by-value-and-stack-frames.md](04-methods-and-functions/03-pass-by-value-and-stack-frames.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [04-static-instance-and-method-references.md](04-methods-and-functions/04-static-instance-and-method-references.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
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
| [01-oop-fundamentals.md](05-object-oriented-programming/01-oop-fundamentals.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [02-encapsulation-and-abstraction.md](05-object-oriented-programming/02-encapsulation-and-abstraction.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [03-inheritance-and-polymorphism-intro.md](05-object-oriented-programming/03-inheritance-and-polymorphism-intro.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
| [04-composition-and-object-relationships.md](05-object-oriented-programming/04-composition-and-object-relationships.md) | Completed | standards-alignment pass 2026-11-10 Temurin re-verify | — |
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
| [01-initialization-and-constructors.md](06-classes-and-objects/01-initialization-and-constructors.md) | Completed | standards-alignment pass 2026-11-11 Temurin re-verify | `super()` subclass order (§5.2 → M07) |
| [02-nested-and-inner-classes.md](06-classes-and-objects/02-nested-and-inner-classes.md) | Completed | standards-alignment pass 2026-11-11 Temurin re-verify | Lambda bytecode/compare (§5.4 → M09) |
| [03-immutability-and-builders.md](06-classes-and-objects/03-immutability-and-builders.md) | Completed | standards-alignment pass 2026-11-11 Temurin re-verify | Record vs builder depth (§5.3; records module later) |
| [04-equals-hashcode-and-tostring.md](06-classes-and-objects/04-equals-hashcode-and-tostring.md) | Completed | standards-alignment pass 2026-11-11 Temurin re-verify | Hash table resizing/load factor (§5.2 → M18) |
| [Module assessment](assessments/module-assessments/06-classes-and-objects-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `constructors-demo` | `ada/ada`, `lin/Lin`, `created=2`, `point=(3,4)` |
| `nested-classes-demo` | StaticNested, inner, local, anonymous lines as documented |
| `immutability-builder-demo` | `1250 USD`; GET request line |
| `equals-hashcode-demo` | equals/hash true/false; `setSize=2` |

**Module 06 status:** Completed  
**Next recommended chapter:** Module 07 (Completed below)

---

## Module 07 — Inheritance and Polymorphism

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-inheritance-deep-dive.md](07-inheritance-and-polymorphism/01-inheritance-deep-dive.md) | Completed | Standards-alignment 2026-11-11; Technical L2–3; §5 mechanism | Interfaces in M08 |
| [02-abstract-classes.md](07-inheritance-and-polymorphism/02-abstract-classes.md) | Completed | Standards-alignment 2026-11-11; Technical L2–3; §5 mechanism | default methods compare M08 |
| [03-dynamic-dispatch-and-lsp.md](07-inheritance-and-polymorphism/03-dynamic-dispatch-and-lsp.md) | Completed | Standards-alignment 2026-11-11; dispatch/LSP Level 3; §5 mechanism | CHA / bytecode in later JVM module |
| [04-sealed-classes.md](07-inheritance-and-polymorphism/04-sealed-classes.md) | Completed | Standards-alignment 2026-11-11; Technical L2–3; §5 mechanism | Module patterns in M15 |
| [Module assessment](assessments/module-assessments/07-inheritance-and-polymorphism-assessment.md) | Completed | Solutions separate | — |

**Examples (Verified Temurin JDK 21.0.12.1, re-run 2026-11-11):**

| Example | Result |
|---------|--------|
| `inheritance-deep-demo` | Savings line; balance 1200; withdraw 900 true / 1100 false |
| `abstract-classes-demo` | REPORT Q1 pdf/csv blocks with END |
| `dispatch-lsp-demo` | Rectangle 12; Square 25; poly area 4 |
| `sealed-classes-demo` | ok/err lines; area=12.566370614359172 |

**Module 07 status:** Completed (Technical profile; Ch.03 dispatch/LSP at Level 3)  
**Next recommended chapter:** Module 08 (Completed below)

---

## Module 08 — Interfaces and Abstraction

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-interface-fundamentals.md](08-interfaces-and-abstraction/01-interface-fundamentals.md) | Completed | Standards-alignment 2026-11-11; Technical L2–3; §5 mechanism | Streams / `java.util.function` in M16 |
| [02-default-static-and-private-methods.md](08-interfaces-and-abstraction/02-default-static-and-private-methods.md) | Completed | Standards-alignment 2026-11-11; Technical L2–3; §5 mechanism | JVM default resolution in later JVM module |
| [03-functional-and-marker-interfaces.md](08-interfaces-and-abstraction/03-functional-and-marker-interfaces.md) | Completed | Standards-alignment 2026-11-11; Technical L2–3; §5 mechanism | Deep stream usage M16 |
| [04-api-design-with-interfaces.md](08-interfaces-and-abstraction/04-api-design-with-interfaces.md) | Completed | Standards-alignment 2026-11-11; Technical L2–3; §5 mechanism | DI frameworks in later modules |
| [Module assessment](assessments/module-assessments/08-interfaces-and-abstraction-assessment.md) | Completed | Solutions separate | — |

**Examples (Verified Temurin JDK 21.0.12.1, re-run 2026-11-11):**

| Example | Result |
|---------|--------|
| `interfaces-fundamentals-demo` | card:1500; wallet:900; email:paid; audit=CARD-1 |
| `interface-methods-demo` | hello Ada; HELLO ADA; *** team *** |
| `functional-marker-demo` | add=5; mul=6; sum=10; entity=true |
| `api-interfaces-demo` | created:ada; exists:ada; find=ada |

**Module 08 status:** Completed (Technical profile; Levels 2–3)  
**Next recommended chapter:** Module 09 (Completed below)

---

## Module 09 — Encapsulation and Design

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-access-control-and-api-boundaries.md](09-encapsulation-and-design/01-access-control-and-api-boundaries.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | JPMS deep dive in later modules |
| [02-coupling-and-cohesion.md](09-encapsulation-and-design/02-coupling-and-cohesion.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Architectural metrics optional later |
| [03-composition-versus-inheritance.md](09-encapsulation-and-design/03-composition-versus-inheritance.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Decorator pattern examples in M15+ |
| [Module assessment](assessments/module-assessments/09-encapsulation-and-design-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `access-control-demo` | `balance=150`; `entries=2` |
| `coupling-cohesion-demo` | `total=1050`; `ORD-9 => 1050 cents` |
| `composition-vs-inheritance-demo` | `Sedan / 180hp`; `start=true`; audit log lines; `done:save` |

**Module 09 status:** Completed  
**Next recommended chapter:** Module 10 (Completed below)

---

## Module 10 — Java Type System

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-static-and-strong-typing.md](10-java-type-system/01-static-and-strong-typing.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Generics depth in M13 |
| [02-instanceof-and-pattern-matching.md](10-java-type-system/02-instanceof-and-pattern-matching.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Overlaps M03 Ch.04 by design |
| [03-variance-wildcards-and-erasure-preview.md](10-java-type-system/03-variance-wildcards-and-erasure-preview.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Bridges/bounds in M13 |
| [Module assessment](assessments/module-assessments/10-java-type-system-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `typing-demo` | `3 items`; `number=42`; `runtime=Integer`; `len=5` |
| `instanceof-patterns-demo` | `string:Ada`; `int:21`; `null`; `double:3.5`; `long=java` |
| `variance-erasure-preview-demo` | `sum=3`; `first=1`; `objs=[7]`; `listClass=ArrayList` |

**Module 10 status:** Completed
**Next recommended chapter:** Module 11 (Completed below)

---

## Module 11 — Arrays and Strings

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-arrays.md](11-arrays-and-strings/01-arrays.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | `List` depth in M12 |
| [02-strings-and-immutability.md](11-arrays-and-strings/02-strings-and-immutability.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Grapheme/ICU in advanced appendix |
| [03-stringbuilder-and-text-blocks.md](11-arrays-and-strings/03-stringbuilder-and-text-blocks.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | String templates stay preview-only |
| [04-encoding-and-string-performance.md](11-arrays-and-strings/04-encoding-and-string-performance.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | NIO charset depth in M17 |
| [Module assessment](assessments/module-assessments/11-arrays-and-strings-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `arrays-demo` | `len=3`; `max=95`; `sorted=[85, 90, 95]`; `grid01=b` |
| `strings-demo` | `a==b=true`; `a.equals(c)=true`; `upper=JAVA`; `sub=hand` |
| `stringbuilder-textblock-demo` | `Hello Java!`; `jsonLines=4`; JSON block as documented |
| `encoding-perf-demo` | `chars=4`; `utf8Bytes=5`; `roundtripOk=true`; `built=012` |

**Module 11 status:** Completed
**Next recommended chapter:** Module 12 (Completed below)

---

## Module 12 — Collections Framework

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-collection-hierarchy.md](12-collections-framework/01-collection-hierarchy.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Generics depth in M13 |
| [02-lists.md](12-collections-framework/02-lists.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Concurrent lists in JUC modules |
| [03-sets-and-queues.md](12-collections-framework/03-sets-and-queues.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | `BlockingQueue` in concurrency track |
| [04-maps-and-hashing.md](12-collections-framework/04-maps-and-hashing.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | `ConcurrentHashMap` later |
| [05-internals-and-choosing-structures.md](12-collections-framework/05-internals-and-choosing-structures.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | External cache libs out of scope |
| [Module assessment](assessments/module-assessments/12-collections-framework-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `collections-hierarchy-demo` | `collectionSize=2`; `listSize=2`; `setSize=2`; `containsY=true` |
| `lists-demo` | `arrayList=[one, 1.5, two]`; `get1=1.5`; `linked=[head, tail]`; `poll=head`; `linkedAfter=[tail]` |
| `sets-queues-demo` | `hashSize=2`; `linkedOrder=[b, a]`; `treeOrder=[a, b]`; `peek=1`; `poll=1`; `queue=[2]` |
| `maps-hashing-demo` | `Ada=37`; `size=2`; `containsLin=true`; `keyedSize=1`; `keyedA=two` |
| `collections-choice-demo` | `index2=3`; `kb=5`; `odds=[1, 3]` |

**Module 12 status:** Completed

---

## Module 13 — Generics

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-generic-types-and-methods.md](13-generics/01-generic-types-and-methods.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Primitive generics (Valhalla) out of scope |
| [02-bounds-and-wildcards.md](13-generics/02-bounds-and-wildcards.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Declaration-site variance (Kotlin) comparison only |
| [03-type-erasure-and-restrictions.md](13-generics/03-type-erasure-and-restrictions.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Reified generics not in Java 21 |
| [04-generic-api-design.md](13-generics/04-generic-api-design.md) | Completed | Standards alignment 2026-11-11; Temurin re-verify | Framework-specific DAO patterns later |
| [Module assessment](assessments/module-assessments/13-generics-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `generics-basics-demo` | `value=hello`; `first=1`; `pair=a:1` |
| `generics-bounds-demo` | `sum=6.0`; `nums=[1, 2, 3]`; `producer0=1` |
| `generics-erasure-demo` | `sameRawClass=true`; `s=hi`; `i=7`; `built=[a, b]` |
| `generics-api-demo` | `found=Ada`; `missing=false`; `count=1` |

**Module 13 status:** Completed
**Next recommended chapter:** [16-lambdas-and-functional-programming/](16-lambdas-and-functional-programming/) (lambdas, functional interfaces)

---

## Module 14 — Exceptions and Error Handling

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-throwable-hierarchy.md](14-exceptions-and-error-handling/01-throwable-hierarchy.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | Virtual threads and structured concurrency cite later modules |
| [02-try-catch-finally-and-try-with-resources.md](14-exceptions-and-error-handling/02-try-catch-finally-and-try-with-resources.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | try-with-resources on custom types only |
| [03-custom-exceptions-and-design.md](14-exceptions-and-error-handling/03-custom-exceptions-and-design.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | Bean Validation / framework handlers in later modules |
| [Module assessment](assessments/module-assessments/14-exceptions-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `throwable-demo` | `caughtUnchecked=bad-arg`; `caughtChecked=missing-file`; `runtimeIsUnchecked=true`; `errorIsThrowable=true` |
| `try-with-resources-demo` | `ok-path`; `finally-ran`; `catch2=boom`; `finally2-ran`; `open=R1`; `data-R1`; `close=R1` |
| `custom-exceptions-demo` | `balance=60`; `error=need 40 more`; `shortfall=40` |

**Module 14 status:** Completed (standards alignment: profile, Mechanism, master checklist; demos re-verified Temurin 21.0.12.1)  
**Next recommended chapter:** [16-lambdas-and-functional-programming/](16-lambdas-and-functional-programming/) (lambdas, functional interfaces)

---

## Module 15 — Enums, Records, and Sealed Types

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-enums.md](15-enums-records-and-sealed-types/01-enums.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | `EnumSet`/`EnumMap` advanced tuning in later collections notes |
| [02-records.md](15-enums-records-and-sealed-types/02-records.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | JPA/serialization deep dives in later modules |
| [03-sealed-types-and-patterns.md](15-enums-records-and-sealed-types/03-sealed-types-and-patterns.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | JPMS cross-module `permits` in advanced modules |
| [Module assessment](assessments/module-assessments/15-enums-records-sealed-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `enums-demo` | `name=HIGH`; `ordinal=2`; `label=High`; `parse=LOW`; `isHigh=true` |
| `records-demo` | `Point[x=3, y=4]`; `x=3`; `equals=true`; `hashEqual=true`; `contains3=true`; `contains9=false` |
| `sealed-patterns-demo` | `eval=5`; `desc=const:7`; `descAdd=add` |

**Module 15 status:** Completed (standards alignment: profile, Mechanism, master checklist; demos re-verified Temurin 21.0.12.1)  
**Next recommended chapter:** [16-lambdas-and-functional-programming/](16-lambdas-and-functional-programming/) (lambdas, SAM types)

---

## Module 16 — Lambdas and Functional Programming

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-functional-fundamentals.md](16-lambdas-and-functional-programming/01-functional-fundamentals.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | Virtual-thread executors in later concurrency modules |
| [02-lambdas-and-functional-interfaces.md](16-lambdas-and-functional-programming/02-lambdas-and-functional-interfaces.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | Custom SAM in framework modules as needed |
| [03-method-references-and-composition.md](16-lambdas-and-functional-programming/03-method-references-and-composition.md) | Completed | Standards alignment Technical L2–3 2026-11-11 | — |
| [Module assessment](assessments/module-assessments/16-lambdas-functional-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `functional-fundamentals-demo` | `apply=15`; `sorted=[fig, kiwi, apple]`; `transformed=[10, 20, 30]` |
| `lambdas-interfaces-demo` | `greeting=Hello`; `lenAda=3`; `nonBlankCount=2`; `mapped=[ADA, BOB]` |
| `method-references-demo` | `normalized=[ADA, LIN]`; `pipeline=14`; `compose=14` |

**Module 16 status:** Completed (standards alignment: profile, Mechanism, expanded teaching depth Ch.01–03, master checklist; demos re-verified Temurin 21.0.12.1)  
**Next recommended chapter:** Module 17 (Completed below)

---

## Module 17 — Stream API

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-stream-fundamentals.md](17-stream-api/01-stream-fundamentals.md) | Completed | Template pilot Technical L3 2026-11-10 | Reactive streams out of scope |
| [02-intermediate-and-terminal-operations.md](17-stream-api/02-intermediate-and-terminal-operations.md) | Completed | Template pilot Technical L3 2026-11-10 | Primitive stream performance tuning in perf module |
| [03-collectors-and-grouping.md](17-stream-api/03-collectors-and-grouping.md) | Completed | Template pilot Technical L3 2026-11-10 | `teeing` collector in advanced notes later |
| [04-parallel-streams-and-pitfalls.md](17-stream-api/04-parallel-streams-and-pitfalls.md) | Completed | Template pilot Deep-dive L3–4 2026-11-10 | Custom `ForkJoinPool` wrapper in concurrency module |
| [Module assessment](assessments/module-assessments/17-stream-api-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `stream-fundamentals-demo` | `listCount=3`; `rangeSum=15`; lazy `filter:` Ada/Lin; `firstL=Lin` |
| `stream-operations-demo` | `evensDouble=[8, 16, 20]`; `sum=32`; `max=10`; `anyBig=true` |
| `collectors-grouping-demo` | `byDept={ops=[Zoe], eng=[Ada, Lin]}`; `csv=Ada, Lin, Zoe`; `counts={ops=1, eng=2}` |
| `parallel-streams-demo` | `sequential`/`parallel` mapped lists; `sideEffect=5`; `badSize=5` (**not** proof of safety; anti-pattern documented) |

**Module 17 status:** Completed (master-template pilot: Technical Ch.01–03, Deep-dive Ch.04; demos Verified Temurin 21.0.12.1)  
**Next recommended chapter:** Module 18 (Completed below)

---

## Module 18 — Optional and Null Handling

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-null-and-nullpointerexception.md](18-optional-and-null-handling/01-null-and-nullpointerexception.md) | Completed | Master-template align 2026-11-11 (Technical L2–3) | JSpecify/nullness annotations in advanced tooling module |
| [02-optional-api.md](18-optional-and-null-handling/02-optional-api.md) | Completed | Master-template align 2026-11-11 (Technical L3) | — |
| [03-optional-design-and-misuse.md](18-optional-and-null-handling/03-optional-design-and-misuse.md) | Completed | Master-template align 2026-11-11 (Technical L3) | Jackson Optional config in web modules |
| [Module assessment](assessments/module-assessments/18-optional-null-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `null-npe-demo` | `safe=unknown`; `npe=NullPointerException`; `user=Ada`; `display=<anonymous>` |
| `optional-api-demo` | `present=Ada`; `emptyIsEmpty=true`; `upper=ADA`; `fromEmpty=fallback`; `len=2` |
| `optional-design-demo` | `good=Ada`; `missing=unknown`; `hasUser1=true` |

**Module 18 status:** Completed (master-template align: profile + mechanism + Review Checklist; demos re-Verified Temurin 21.0.12.1)  
**Next recommended chapter:** Module 19 (Completed below)

---

## Module 19 — Modern Java Features

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-java-21-feature-tour.md](19-modern-java-features/01-java-21-feature-tour.md) | Completed | Master-template align 2026-11-11 (Technical L3) | Carrier pinning ops detail in concurrency module |
| [02-features-after-java-21.md](19-modern-java-features/02-features-after-java-21.md) | Completed | Master-template align 2026-11-11 (Technical L2–3) | Expand JEP table when appendix history grows |
| [03-preview-and-incubator-features.md](19-modern-java-features/03-preview-and-incubator-features.md) | Completed | Master-template align 2026-11-11 (Technical L3) | Re-verify JEP phase on JDK upgrades |
| [Module assessment](assessments/module-assessments/19-modern-java-features-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `java21-features-demo` | `virtualThread=vt-ready`; `sequenced=head/tail`; `recordPattern=3+4=7`; `switchExpr=default`; `textBlockLines=2` |
| `features-after-21-demo` | `feature=21`; `interim=0`; `update=12`; `handbookPrimary=21-LTS` |
| `preview-incubator-demo` | `structuredConcurrency=scoped-result` (with `--enable-preview --release 21`) |

**Module 19 status:** Completed (master-template align; preview flags documented; demos re-Verified Temurin 21.0.12.1)  
**Next recommended chapter:** Module 20 (Completed below)

---

## Module 20 — Java Standard Library

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-java-lang-and-java-util.md](20-java-standard-library/01-java-lang-and-java-util.md) | Completed | Master-template align 2026-11-11 (Technical L3) | — |
| [02-io-nio-net-and-time-overview.md](20-java-standard-library/02-io-nio-net-and-time-overview.md) | Completed | Master-template align 2026-11-11 (Technical L2–3) | HTTP client depth in networking modules |
| [03-reflection-annotations-and-process-apis.md](20-java-standard-library/03-reflection-annotations-and-process-apis.md) | Completed | Master-template align 2026-11-11 (Deep-dive L3–4) | Annotation processors in build module |
| [Module assessment](assessments/module-assessments/20-java-standard-library-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `java-lang-util-demo` | `strip=Ada Lovelace`; `copyOf=[Bob, Ada, Lin]`; `first=Bob`; `join=Bob-Ada-Lin`; `hashConsistent=true` |
| `stdlib-overview-demo` | `pathFileName=notes.txt`; `byteBufferPos=4`; `uriHost=example.com`; `instantEpoch=true` |
| `reflection-annotations-process-demo` | `annotation=sample-bean`; `reflectInvoke=hello Ada`; `processRan=true`; `processArgs=[java, -version]` |

**Module 20 status:** Completed (master-template align; demos re-Verified Temurin 21.0.12.1)  
**Next recommended chapter:** Module 21 (Completed below)

---

## Module 21 — File I/O and Serialization

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-files-and-streams.md](21-file-io-and-serialization/01-files-and-streams.md) | Completed | Master-template align 2026-11-11 (Technical L3) | WatchService in advanced I/O |
| [02-nio2.md](21-file-io-and-serialization/02-nio2.md) | Completed | Master-template align 2026-11-11 (Technical L3) | — |
| [03-serialization-and-data-formats.md](21-file-io-and-serialization/03-serialization-and-data-formats.md) | Completed | Master-template align 2026-11-11 (Deep-dive L3–4) | Jackson module cross-link later |
| [Module assessment](assessments/module-assessments/21-file-io-serialization-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `files-streams-demo` | `tempLines=3`; `bufferedTransform=ONE`/`TWO`; `readBytes=17` |
| `nio2-demo` | `exists=true`; `size=9`; `read=nio2-line`; `listCount=2` |
| `serialization-formats-demo` | `serializedBytes=99`; `deserialized=Ada:97`; `propertiesStored=true`; `jsonBytes=25` |

**Module 21 status:** Completed (master-template align; demos re-Verified Temurin 21.0.12.1)  
**Next recommended chapter:** Module 22 (Completed below)

---

## Module 22 — Date, Time, and Internationalization

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-java-time.md](22-date-time-and-internationalization/01-java-time.md) | Completed | Third deepen EXPAND-PRESERVE 2026-11-11 (Technical L2–3; was ~93 → ~409) | `Clock` testing patterns in unit-test module |
| [02-time-zones-and-formatting.md](22-date-time-and-internationalization/02-time-zones-and-formatting.md) | Completed | Third deepen EXPAND-PRESERVE 2026-11-11 (Technical L2–3; was ~57 → ~358) | More coded DST gap/overlap lab optional |
| [03-locale-and-internationalization.md](22-date-time-and-internationalization/03-locale-and-internationalization.md) | Completed | Third deepen EXPAND-PRESERVE 2026-11-11 (Technical L2–3; was ~52 → ~356) | `MessageFormat` plural rules |
| [Module assessment](assessments/module-assessments/22-date-time-i18n-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1 baseline; re-verified 2026-11-11 on Oracle JDK 21.0.9 — identical output; Temurin not on PATH this session):**

| Example | Result |
|---------|--------|
| `java-time-demo` | `ageYears=36`; `durationMinutes=150`; `instantPlus=2026-11-10T10:00:00Z` |
| `zones-formatting-demo` | `utc=15:30`; `yangon=22:00`; `formatted=2026-11-10 22:00 MMT` |
| `locale-i18n-demo` | `usPrice=$1,999.50`; `greeting=Hello`; `usdCode=USD`; `deCurrency=EUR` |

**Module 22 status:** Completed (third deepen pass 2026-11-11)  
**Next recommended chapter:** Module 23 (Completed below)

---

## Module 23 — Regular Expressions

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-regex-fundamentals.md](23-regular-expressions/01-regex-fundamentals.md) | Completed | Third deepen EXPAND-PRESERVE 2026-11-11 (Technical L2–3; was ~50 → ~342) | Unicode category flags deep dive |
| [02-java-regex-api.md](23-regular-expressions/02-java-regex-api.md) | Completed | Third deepen EXPAND-PRESERVE 2026-11-11 (Technical L2–3; was ~61 → ~374) | `appendReplacement` hands-on in a dedicated log-pipeline demo |
| [Module assessment](assessments/module-assessments/23-regular-expressions-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1 baseline; re-verified 2026-11-11 on Oracle JDK 21.0.9 — identical output; Temurin not on PATH this session):**

| Example | Result |
|---------|--------|
| `regex-fundamentals-demo` | `emailValid=true`; `capturedId=1042`; `splitCount=3`; `secondWord=beta` |
| `java-regex-api-demo` | `match=ENG-42/OPS-7`; `namedId=42`; `fullMatch=false`; `replaced=[ENG#42]` |

**Module 23 status:** Completed (third deepen pass 2026-11-11)  
**Next recommended chapter:** Module 24 (Completed below)

---

## Module 24 — Concurrency and Multithreading

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-threads-and-runnables.md](24-concurrency-and-multithreading/01-threads-and-runnables.md) | Completed | Third EXPAND-PRESERVE deepen 2026-11-11 (Deep-dive; ~189 → 360 lines) | — |
| [02-synchronization-and-atomics.md](24-concurrency-and-multithreading/02-synchronization-and-atomics.md) | Completed | EXPAND-PRESERVE 2026-11-11 (Deep-dive L4 JMM; ~448 lines; left intact third pass) | `java.util.concurrent.locks` depth later |
| [03-executors-and-futures.md](24-concurrency-and-multithreading/03-executors-and-futures.md) | Completed | Third EXPAND-PRESERVE deepen 2026-11-11 (Technical; ~123 → 361 lines) | `CompletableFuture` patterns in backend modules |
| [04-concurrent-collections.md](24-concurrency-and-multithreading/04-concurrent-collections.md) | Completed | Third EXPAND-PRESERVE deepen 2026-11-11 (Technical; ~106 → 384 lines) | BlockingQueue hands-on in projects |
| [05-virtual-threads-and-structured-concurrency.md](24-concurrency-and-multithreading/05-virtual-threads-and-structured-concurrency.md) | Completed | Third EXPAND-PRESERVE deepen 2026-11-11 (Deep-dive; ~109 → 438 lines; STS preview labeled) | Carrier pinning ops tuning in production notes |
| [Module assessment](assessments/module-assessments/24-concurrency-multithreading-assessment.md) | Completed | Solutions separate | — |

**Examples compiled / re-verified (Oracle JDK 21.0.9, 2026-11-11 third deepen pass; Temurin 21.0.12.1 not installed on this host; outputs match prior Temurin 21.0.12.1 recordings):**

| Example | Result |
|---------|--------|
| `threads-runnable-demo` | `mainThread=main`; `daemonDefault=false`; `workerName=handbook-worker`; `workerAlive=true`; `afterJoinAlive=false` |
| `synchronization-atomics-demo` | Prior Temurin: `unsafeCounter` varies; `atomicCounter=40000`; `syncCounter=40000` (not re-run this pass — Ch.02 untouched) |
| `executors-futures-demo` | `futureResult=42`; `done=true`; `poolShutdown=ok` |
| `concurrent-collections-demo` | `Ada=2`; `Lin=2`; `size=2` |
| `virtual-threads-demo` | `isVirtual=true`; `vtName=` (empty); `executorVirtual=true`; `platformIsVirtual=false` |
| `StructuredTaskScope` preview probe | **Verified** with `--enable-preview`: `a=A b=B` (not a shipped examples/ demo) |

**Module 24 status:** Completed (third deepen pass 2026-11-11 on residual short chapters 01/03/04/05)  
**Next recommended chapter:** Module 25 (Completed below)

---

## Module 25 — Java Memory Model

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-stack-heap-and-references.md](25-java-memory-model/01-stack-heap-and-references.md) | Completed | Third deepen pass 2026-11-11 EXPAND-PRESERVE (Deep-dive L3–4; was ~72 → textbook depth) | Off-heap / FFM in advanced modules |
| [02-happens-before-and-visibility.md](25-java-memory-model/02-happens-before-and-visibility.md) | Completed | Third deepen pass 2026-11-11 EXPAND-PRESERVE (Deep-dive L4 JLS HB; was ~77 → textbook depth) | `VarHandle` explicit orders in advanced concurrency |
| [03-safe-publication.md](25-java-memory-model/03-safe-publication.md) | Completed | Third deepen pass 2026-11-11 EXPAND-PRESERVE (Deep-dive L3–4; was ~48 → textbook depth) | — |
| [Module assessment](assessments/module-assessments/25-java-memory-model-assessment.md) | Completed | Solutions separate; may need depth alignment after chapter expand | Spot-check assessment vs third deepen pass |

**Examples compiled (Temurin JDK 21.0.12.1 Verified outputs preserved; re-checked 2026-11-11):**

| Example | Result |
|---------|--------|
| `stack-heap-references-demo` | `sameRef=false`; `equalContent=true` |
| `happens-before-demo` | `payload=42`; `publishDone=true` |
| `safe-publication-demo` | `finalFieldValue=99`; `published=true` |

**Module 25 status:** Completed (third deepen pass 2026-11-11)
**Next recommended chapter:** Module 26 (Completed below)

---

## Module 26 — JVM Internals

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-jvm-architecture.md](26-jvm-internals/01-jvm-architecture.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~347 lines; was ~61) | JPMS deep dive in module 30+ |
| [02-class-loading-and-linking.md](26-jvm-internals/02-class-loading-and-linking.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~332 lines; was ~58) | Custom class loaders in framework modules |
| [03-bytecode-and-execution.md](26-jvm-internals/03-bytecode-and-execution.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~342 lines; was ~85) | ASM/bytebuddy in advanced tooling |
| [04-runtime-data-areas.md](26-jvm-internals/04-runtime-data-areas.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~318 lines; was ~48) | — |
| [05-diagnostics-and-agents.md](26-jvm-internals/05-diagnostics-and-agents.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~329 lines; was ~59) | Full JFR lab exercises later |
| [Module assessment](assessments/module-assessments/26-jvm-internals-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `jvm-architecture-demo` | `javaVersion=21`; `jvmName=OpenJDK 64-Bit Server VM` |
| `class-loading-demo` | `stringLoader=null`; `demoLoaded=true` |
| `bytecode-demo` | `sum=42`; `javap` output recorded in Ch.03 |
| `runtime-data-areas-demo` | `stackDepth=100` |
| `diagnostics-demo` | `threadCount=7`; `threadsListed=7` |

**Module 26 status:** Completed  
**Next recommended chapter:** Module 27 (Completed below)

---

## Module 27 — Garbage Collection

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-gc-fundamentals.md](27-garbage-collection/01-gc-fundamentals.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~326 lines; was ~71) | Unified logging cookbook |
| [02-collectors-g1-zgc-shenandoah.md](27-garbage-collection/02-collectors-g1-zgc-shenandoah.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~298 lines; was ~65) | Compare ZGC vs G1 with measured logs |
| [03-tuning-and-memory-leaks.md](27-garbage-collection/03-tuning-and-memory-leaks.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~289 lines; was ~58) | Heap dump walkthrough |
| [Module assessment](assessments/module-assessments/27-garbage-collection-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `gc-fundamentals-demo` | `gcBeanCount=3`; G1 collector MXBean names |
| `collectors-overview-demo` | `vmVendor=Eclipse Adoptium`; `UseG1GC=true` flag verified |
| `memory-leak-pattern-demo` | `leakListAfter=1`; `reachable=true` |

**Module 27 status:** Completed  
**Next recommended chapter:** Module 28 (Completed below)

---

## Module 28 — JIT and Performance

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-hotspot-and-tiered-compilation.md](28-jit-and-performance/01-hotspot-and-tiered-compilation.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~290 lines; was ~56) | `PrintCompilation` walkthrough optional |
| [02-jmh-and-benchmarking.md](28-jit-and-performance/02-jmh-and-benchmarking.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~297 lines; was ~68) | Runnable JMH sample in build module |
| [03-profiling.md](28-jit-and-performance/03-profiling.md) | Completed | Deep-dive EXPAND-PRESERVE 2026-11-11 (~267 lines; was ~58) | JFR event catalog |
| [Module assessment](assessments/module-assessments/28-jit-performance-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `tiered-compilation-demo` | `result=124750`; `elapsedMs=6` (machine-specific) |
| `jmh-awareness-demo` | `warmedSample=1806` |
| `profiling-awareness-demo` | `cpuTimeSupported=true` |

**Module 28 status:** Completed  
**Next recommended chapter:** Module 29 (Completed below)

---

## Module 29 — Build Tools and Dependency Management

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-classpath-jars-and-modules.md](29-build-tools-and-dependency-management/01-classpath-jars-and-modules.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~263 lines; was ~78) | Gradle wrapper lab optional |
| [02-maven.md](29-build-tools-and-dependency-management/02-maven.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~219 lines; was ~72) | Multi-module POM later |
| [03-gradle.md](29-build-tools-and-dependency-management/03-gradle.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~215 lines; was ~71) | Runnable Gradle project when wrapper added |
| [04-java-platform-module-system.md](29-build-tools-and-dependency-management/04-java-platform-module-system.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~214 lines; was ~58) | jlink exercise later |
| [Module assessment](assessments/module-assessments/29-build-tools-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `classpath-jars-demo` | `greet=Hello, Handbook!`; `fromJar=true` |
| `maven-awareness-demo` | `mavenCompiled=ok`; `javaVersion=21.0.12.1` (Maven 3.9.6) |
| `jpms-hello-demo` | `greeting=jpms-ok`; `module=app` |

**Module 29 status:** Completed (EXPAND-PRESERVE L2–3, 2026-11-11)  
**Next recommended chapter:** Module 30 (Completed below)

---

## Module 30 — Testing and Code Quality

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-testing-fundamentals.md](30-testing-and-quality/01-testing-fundamentals.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~205 lines; was ~59) | Parameterized tests expanded later |
| [02-junit-and-mockito.md](30-testing-and-quality/02-junit-and-mockito.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~254 lines; was ~77) | Dedicated Mockito runnable module optional |
| [03-static-analysis-and-ci.md](30-testing-and-quality/03-static-analysis-and-ci.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~202 lines; was ~62) | Sonar sample pipeline later |
| [Module assessment](assessments/module-assessments/30-testing-quality-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `testing-fundamentals-demo` | three `pass:` lines; `allTests=ok` |
| `h2-jdbc-demo` (exec) | `name=Ada`; `rowCount=1`; `jdbcH2=ok` (2026-11-11) |
| `h2-jdbc-demo` (JUnit) | `mvn -q test` BUILD SUCCESS (Maven 3.9.6 validation host) |

**Module 30 status:** Completed (EXPAND-PRESERVE L2–3, 2026-11-11)  
**Next recommended chapter:** Module 31 (Completed below)

---

## Module 31 — Debugging and Troubleshooting

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-ide-debugging.md](31-debugging-and-troubleshooting/01-ide-debugging.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~189 lines; was ~56) | Remote JDWP lab |
| [02-dumps-and-flight-recorder.md](31-debugging-and-troubleshooting/02-dumps-and-flight-recorder.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~184 lines; was ~67) | Capture live `jcmd` excerpt in appendix |
| [03-production-incidents.md](31-debugging-and-troubleshooting/03-production-incidents.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~178 lines; was ~51) | — |
| [Module assessment](assessments/module-assessments/31-debugging-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `debug-target-demo` | `count=1`; `expectedCorrect=1` |
| `diagnostics-awareness-demo` | `threadCount=7`; `jvmName=OpenJDK 64-Bit Server VM` |

**Module 31 status:** Completed (EXPAND-PRESERVE L2–3, 2026-11-11)  
**Next recommended chapter:** Module 32 (Completed below)

---

## Module 32 — Networking and HTTP

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-sockets-and-tcp.md](32-networking-and-http/01-sockets-and-tcp.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~195 lines; was ~55) | NIO selector module later |
| [02-http-and-httpclient.md](32-networking-and-http/02-http-and-httpclient.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~210 lines; was ~57) | HTTP/2 push notes optional |
| [03-tls-and-resilience-basics.md](32-networking-and-http/03-tls-and-resilience-basics.md) | Completed | EXPAND-PRESERVE 2026-11-11 (~205 lines; was ~66) | Resilience4j hands-on in backend track |
| [Module assessment](assessments/module-assessments/32-networking-http-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `tcp-loopback-demo` | `echo=echo:ping`; `port=19042` |
| `http-client-demo` | `status=200`; `bodyStartsWith=true`; `headerServer=cloudflare` |

**Module 32 status:** Completed (EXPAND-PRESERVE L2–3, 2026-11-11)  
**Next recommended chapter:** Module 33 (Completed below)

---

## Module 33 — Databases and JDBC

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-relational-and-sql-basics.md](33-databases-and-jdbc/01-relational-and-sql-basics.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | Testcontainers note in Spring modules |
| [02-jdbc-fundamentals.md](33-databases-and-jdbc/02-jdbc-fundamentals.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | — |
| [03-transactions-and-pooling.md](33-databases-and-jdbc/03-transactions-and-pooling.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | HikariCP numeric tuning lab |
| [Module assessment](assessments/module-assessments/33-databases-jdbc-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1; H2 2.3.232; re-run 2026-11-11):**

| Example | Result |
|---------|--------|
| `h2-jdbc-demo` | `name=Ada`; `rowCount=1`; `jdbcH2=ok` |

**Module 33 status:** Completed (EXPAND-PRESERVE L3 depth 2026-11-11)  
**Next recommended chapter:** Module 34 (Completed below)

---

## Module 34 — SQL and Data Access

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-sql-for-java-developers.md](34-sql-and-data-access/01-sql-for-java-developers.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | Window function exercises |
| [02-query-design-and-migrations.md](34-sql-and-data-access/02-query-design-and-migrations.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | Flyway sample in project track |
| [Module assessment](assessments/module-assessments/34-sql-data-access-assessment.md) | Completed | Solutions separate | — |

**Module 34 status:** Completed (EXPAND-PRESERVE L3 depth 2026-11-11; SQL via `h2-jdbc-demo` count query)  
**Next recommended chapter:** Module 35 (Completed below)

---

## Module 35 — Hibernate and JPA

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-orm-and-entities.md](35-hibernate-and-jpa/01-orm-and-entities.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | Spring Data in Module 36 |
| [02-relationships-and-fetching.md](35-hibernate-and-jpa/02-relationships-and-fetching.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | Relationship demo module optional |
| [03-transactions-caching-and-performance.md](35-hibernate-and-jpa/03-transactions-caching-and-performance.md) | Completed | EXPAND-PRESERVE L3 depth 2026-11-11 | 2LC tuning out of scope |
| [Module assessment](assessments/module-assessments/35-hibernate-jpa-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1; Hibernate 6.6.4.Final; re-run 2026-11-11):**

| Example | Result |
|---------|--------|
| `jpa-h2-demo` | `name=Lin`; `idNotNull=true`; `jpaH2=ok` |

**Module 35 status:** Completed (EXPAND-PRESERVE L3 depth 2026-11-11)  
**Next recommended chapter:** Module 36 (Completed below)

---

## Module 36 — Spring Framework

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-ioc-and-dependency-injection.md](36-spring-framework/01-ioc-and-dependency-injection.md) | Completed | Headers + Mechanism §5 deepen 2026-11-11; Deep-dive L3+L4 | Composition root, bean definitions, constructor resolution |
| [02-bean-lifecycle-and-aop.md](36-spring-framework/02-bean-lifecycle-and-aop.md) | Completed | Headers + Mechanism section titles deepen 2026-11-11; Deep-dive L3+L4 | Lifecycle stages, proxies, `@Transactional` |
| [03-spring-data-mvc-security-overview.md](36-spring-framework/03-spring-data-mvc-security-overview.md) | Completed | Headers + Mechanism §5 deepen 2026-11-11; Deep-dive L3–4 preview | MVC pipeline, DTO/OSIV, security overview |
| [Module assessment](assessments/module-assessments/36-spring-framework-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `manual-di-container` | `greet=Hello, Lin`; `diContainer=ok` |
| `jdk-dynamic-proxy-demo` | `before=greet`; `result=Hello, Ada`; `proxyAop=ok` |

**Module 36 status:** Completed (Chapter profile + Mechanism headings 2026-11-11; no full regenerate)  
**Next recommended chapter:** Module 37 (Completed below)

---

## Module 37 — Spring Boot

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-spring-boot-fundamentals.md](37-spring-boot/01-spring-boot-fundamentals.md) | Completed | Chapter profile Deep-dive + Mechanism §5 title 2026-11-11 | `spring-boot-minimal` via `mvnw` |
| [02-configuration-and-profiles.md](37-spring-boot/02-configuration-and-profiles.md) | Completed | Chapter profile Technical + Mechanism §5 title 2026-11-11 | — |
| [03-web-layer-and-validation.md](37-spring-boot/03-web-layer-and-validation.md) | Completed | Chapter profile Technical + Mechanism §5 MVC 2026-11-11 | — |
| [04-actuator-testing-and-production.md](37-spring-boot/04-actuator-testing-and-production.md) | Completed | Chapter profile Technical + Mechanism §5 probes 2026-11-11 | Testcontainers optional lab |
| [Module assessment](assessments/module-assessments/37-spring-boot-assessment.md) | Completed | Solutions separate | — |

**Examples verified (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `rest-httpserver-demo` | `GET /users/1 status=200`; `POST /users status=201`; `restDemo=ok` |
| `spring-boot-minimal` | `mvnw test`: 1 passed; fat JAR `GET /hello` → `{"message":"bootMinimal=ok"}` |

**Module 37 status:** Completed (`spring-boot-minimal` verified with Maven Wrapper; headers/Mechanism deepen 2026-11-11)  
**Next recommended chapter:** Module 38 (Completed below)

---

## Module 38 — REST API Development

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-rest-design.md](38-rest-api-development/01-rest-design.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [02-dtos-errors-and-versioning.md](38-rest-api-development/02-dtos-errors-and-versioning.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [03-openapi-and-api-testing.md](38-rest-api-development/03-openapi-and-api-testing.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | springdoc optional Illustrative |
| [Module assessment](assessments/module-assessments/38-rest-api-development-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `rest-httpserver-demo` | `GET /users/1 status=200 body={"id":1,"name":"Lin"}`; `POST /users status=201`; `restDemo=ok` |

**Module 38 status:** Completed (EXPAND-PRESERVE ~310 lines/chapter avg)  
**Next recommended chapter:** Module 39 (Completed below)

---

## Module 39 — Authentication and Authorization

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-authentication-and-authorization-basics.md](39-authentication-and-authorization/01-authentication-and-authorization-basics.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [02-sessions-jwt-and-oauth.md](39-authentication-and-authorization/02-sessions-jwt-and-oauth.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [03-spring-security-patterns.md](39-authentication-and-authorization/03-spring-security-patterns.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | Spring tests Illustrative |
| [Module assessment](assessments/module-assessments/39-authentication-and-authorization-assessment.md) | Completed | Solutions separate | — |

**Module 39 status:** Completed (EXPAND-PRESERVE ~297 lines/chapter avg)  
**Next recommended chapter:** Module 40 (Completed below)

---

## Module 40 — Backend Architecture

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-layered-and-hexagonal-architecture.md](40-backend-architecture/01-layered-and-hexagonal-architecture.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | ArchUnit sample optional |
| [02-ddd-basics.md](40-backend-architecture/02-ddd-basics.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [03-modular-monolith-versus-microservices.md](40-backend-architecture/03-modular-monolith-versus-microservices.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | Spring Modulith Illustrative |
| [Module assessment](assessments/module-assessments/40-backend-architecture-assessment.md) | Completed | Solutions separate | — |

**Module 40 status:** Completed (EXPAND-PRESERVE ~297 lines/chapter avg)  
**Next recommended chapter:** Module 41 (Completed below)

---

## Module 41 — Design Patterns

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-solid-and-principles.md](41-design-patterns/01-solid-and-principles.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [02-creational-patterns.md](41-design-patterns/02-creational-patterns.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [03-structural-and-behavioral-patterns.md](41-design-patterns/03-structural-and-behavioral-patterns.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [Module assessment](assessments/module-assessments/41-design-patterns-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `creational-demo` | `factory=csv:rows`; `builder=Report{title=T,rows=2}`; `creational=ok` |
| `structural-behavioral-demo` (`PatternsDemo`) | `adapter=v2:data`; `strategy=discount10`; `observer=events:2`; `patterns=ok` |

**Module 41 status:** Completed (EXPAND-PRESERVE ~285 lines/chapter avg)  
**Next recommended chapter:** Module 42 (Completed below)

---

## Module 42 — Clean Code and SOLID

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-clean-code-practices.md](42-clean-code-and-solid/01-clean-code-practices.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [02-solid-deep-dive.md](42-clean-code-and-solid/02-solid-deep-dive.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [03-anti-patterns.md](42-clean-code-and-solid/03-anti-patterns.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [Module assessment](assessments/module-assessments/42-clean-code-and-solid-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `readability-demo` | `total=19.99`; `lineTotal=19.99`; `readability=ok` |

**Module 42 status:** Completed (EXPAND-PRESERVE ~332 lines/chapter avg)  
**Next recommended chapter:** Module 43 (Completed below)

---

## Module 43 — Security

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-secure-coding-and-owasp.md](43-security/01-secure-coding-and-owasp.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [02-crypto-tls-and-secrets.md](43-security/02-crypto-tls-and-secrets.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [03-security-testing.md](43-security/03-security-testing.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [Module assessment](assessments/module-assessments/43-security-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `input-guard-demo` | `valid=true`; `invalid=false`; `input-guard=ok` |

**Module 43 status:** Completed (EXPAND-PRESERVE ~288 lines/chapter avg)  
**Next recommended chapter:** Module 44 (Completed below)

---

## Module 44 — Microservices and Distributed Systems

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-distributed-fundamentals.md](44-microservices-and-distributed-systems/01-distributed-fundamentals.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [02-resilience-patterns.md](44-microservices-and-distributed-systems/02-resilience-patterns.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [03-consistency-and-sagas.md](44-microservices-and-distributed-systems/03-consistency-and-sagas.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [Module assessment](assessments/module-assessments/44-microservices-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1, re-verified 2026-11-11):**

| Example | Result |
|---------|--------|
| `circuit-breaker-demo` | `fail state=CLOSED`; `OPEN fail-fast`; `circuit-breaker=ok` |

**Module 44 status:** Completed (EXPAND-PRESERVE ~291 lines/chapter avg)  
**Next recommended chapter:** Module 45 (Completed below)

---

## Module 45 — Messaging and Event-Driven Systems

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-messaging-fundamentals.md](45-messaging-and-event-driven-systems/01-messaging-fundamentals.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [02-kafka-and-rabbitmq.md](45-messaging-and-event-driven-systems/02-kafka-and-rabbitmq.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | Broker labs Illustrative |
| [03-delivery-guarantees-and-outbox.md](45-messaging-and-event-driven-systems/03-delivery-guarantees-and-outbox.md) | Completed | EXPAND-PRESERVE L3 2026-11-11 | — |
| [Module assessment](assessments/module-assessments/45-messaging-assessment.md) | Completed | Solutions separate | — |

**Module 45 status:** Completed (EXPAND-PRESERVE ~295 lines/chapter avg)  
**Next recommended chapter:** Module 46 (Completed below)

---

## Module 46 — Caching and Performance Engineering

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-caching-strategies.md](46-caching-and-performance/01-caching-strategies.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 (~266 lines) | — |
| [02-redis-and-invalidation.md](46-caching-and-performance/02-redis-and-invalidation.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 (~225 lines) | Redis lab optional |
| [03-load-testing-and-capacity.md](46-caching-and-performance/03-load-testing-and-capacity.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 (~200 lines) | — |
| [Module assessment](assessments/module-assessments/46-caching-performance-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `in-memory-cache-demo` | `user:2=miss`; `in-memory-cache=ok` |

**Module 46 status:** Completed (deepen pass 2 2026-11-11; demos re-verified)  
**Next recommended chapter:** Module 47 (Completed below)

---

## Module 47 — Observability and Monitoring

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-logging-metrics-and-tracing.md](47-observability-and-monitoring/01-logging-metrics-and-tracing.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [02-opentelemetry-and-prometheus.md](47-observability-and-monitoring/02-opentelemetry-and-prometheus.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | OTel collector lab optional |
| [03-slos-and-incidents.md](47-observability-and-monitoring/03-slos-and-incidents.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [Module assessment](assessments/module-assessments/47-observability-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `structured-logging-demo` | `event=order.placed`; `structured-logging=ok` |

**Module 47 status:** Completed  
**Next recommended chapter:** Module 48 (Completed below)

---

## Module 48 — DevOps and Deployment

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-containers-and-the-jvm.md](48-devops-and-deployment/01-containers-and-the-jvm.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | Live docker smoke optional |
| [02-ci-cd.md](48-devops-and-deployment/02-ci-cd.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | Sample Actions workflow file optional |
| [03-deployment-strategies.md](48-devops-and-deployment/03-deployment-strategies.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [Module assessment](assessments/module-assessments/48-devops-deployment-assessment.md) | Completed | Solutions separate | — |

**Module 48 status:** Completed  
**Next recommended chapter:** Module 49 (Completed below)

---

## Module 49 — Cloud and Containerization

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-cloud-native-java.md](49-cloud-and-containerization/01-cloud-native-java.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [02-kubernetes-basics-for-java.md](49-cloud-and-containerization/02-kubernetes-basics-for-java.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | kind/minikube lab optional |
| [03-graalvm-and-native-image-concepts.md](49-cloud-and-containerization/03-graalvm-and-native-image-concepts.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | Native build CI optional |
| [Module assessment](assessments/module-assessments/49-cloud-containerization-assessment.md) | Completed | Solutions separate | — |

**Module 49 status:** Completed  
**Next recommended chapter:** Module 50 (Completed below)

---

## Module 50 — Advanced Java

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-reflection-proxies-and-annotations.md](50-advanced-java/01-reflection-proxies-and-annotations.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Deep-dive L3–4 | Demo re-verified |
| [02-method-handles-varhandles-and-ffm.md](50-advanced-java/02-method-handles-varhandles-and-ffm.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Deep-dive L3–4 | FFM preview flags on JDK 21 |
| [03-bytecode-and-agents.md](50-advanced-java/03-bytecode-and-agents.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Deep-dive L3–4 | `agentDemo=ok` re-verified |
| [Module assessment](assessments/module-assessments/50-advanced-java-assessment.md) | Completed | Solutions separate | — |

**Examples compiled (Temurin JDK 21.0.12.1):**

| Example | Result |
|---------|--------|
| `reflection-annotations-demo` | `reflectionDemo=ok` |
| `method-handles-varhandles-demo` | `methodHandlesDemo=ok` |
| `ffm-arena-demo` | `ffmDemo=ok` (with `--enable-preview`) |
| `java-agent-premain-demo` | `agentDemo=ok` |

**Module 50 status:** Completed  
**Next recommended chapter:** Module 51 (Completed below)

---

## Module 51 — Enterprise and Large-Scale Engineering

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-large-codebases.md](51-enterprise-java/01-large-codebases.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | ArchUnit sample optional |
| [02-compatibility-and-migrations.md](51-enterprise-java/02-compatibility-and-migrations.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [03-production-readiness.md](51-enterprise-java/03-production-readiness.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [Module assessment](assessments/module-assessments/51-enterprise-java-assessment.md) | Completed | Solutions separate | — |

**Module 51 status:** Completed  
**Next recommended chapter:** Module 52 (Completed below)

---

## Module 52 — Interview Preparation

| Chapter | Status | Review | Outstanding |
|---------|--------|--------|-------------|
| [01-core-java-interviews.md](52-interview-preparation/01-core-java-interviews.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [02-jvm-and-concurrency-interviews.md](52-interview-preparation/02-jvm-and-concurrency-interviews.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [03-spring-and-system-design-interviews.md](52-interview-preparation/03-spring-and-system-design-interviews.md) | Completed | EXPAND-PRESERVE deepen pass 2 2026-11-11; Technical L2–3 | — |
| [Module assessment](assessments/module-assessments/52-interview-preparation-assessment.md) | Completed | Solutions separate | — |

**Module 52 status:** Completed  
**Next recommended chapter:** Module 53 (Completed below)

---

## Module 53 — Practical Projects

| Item | Status | Review | Outstanding |
|------|--------|--------|-------------|
| [53-projects/README.md](53-projects/README.md) | Completed | EXPAND-PRESERVE 2026-11-11; standards note | — |
| [projects/README.md](projects/README.md) | Completed | Aligned with Module 53 standards 2026-11-11 | — |
| Beginner specs (×2) | Completed | 2026-11-10 | Reference implementations optional (learner-owned) |
| Intermediate specs (×2) | Completed | 2026-11-10 | — |
| Advanced specs (×2) | Completed | 2026-11-10 | — |
| Expert specs (×2) | Completed | 2026-11-10 | — |
| [Portfolio assessment](assessments/module-assessments/53-projects-assessment.md) | Completed | Solutions separate | — |

**Module 53 status:** Completed  
**Next recommended:** Implement [projects/](projects/); re-take module assessments; maintain appendix and diagnostic sets.
