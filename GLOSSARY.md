# Glossary

Shared terminology for this handbook. Prefer linking here instead of redefining terms inconsistently.

| Term | Definition | Introduced |
|------|------------|------------|
| **Programming** | Writing precise instructions that a computer can execute to solve a problem or perform a task. | [Module 00 Ch.01](00-programming-foundations/01-what-is-programming-and-what-is-java.md) |
| **Source code** | Human-readable program text written in a programming language. | Module 00 Ch.01 |
| **Compiler** | A program that translates source code into another form (for Java: into bytecode). | Module 00 Ch.01 |
| **Bytecode** | Platform-neutral instructions stored in `.class` files; executed by a JVM. | Module 00 Ch.01 |
| **JDK** | Java Development Kit — tools and libraries for developing Java programs (includes compiler). | Module 00 Ch.01 |
| **JRE** | Java Runtime Environment — components needed to run Java applications. | Module 00 Ch.01 |
| **JVM** | Java Virtual Machine — runtime that loads bytecode and executes it. | Module 00 Ch.01 |
| **Algorithm** | A finite, unambiguous sequence of steps to solve a problem. | Module 00 Ch.01 |
| **HotSpot** | Oracle’s widely used JVM implementation (implementation detail, not the language itself). | Module 00 Ch.01 |
| **LTS** | Long-Term Support Java release (e.g. 8, 11, 17, 21, 25). | [Version history](appendix/java-version-history.md) |
| **Process** | OS-managed running instance of a program. | [Module 00 Ch.02](00-programming-foundations/02-how-computers-execute-programs.md) |
| **Thread** | A sequence of execution within a process. | Module 00 Ch.02 |
| **Call stack** | Structure tracking nested method calls for a thread. | Module 00 Ch.02 |
| **Pseudocode** | Informal structured description of an algorithm. | [Module 00 Ch.03](00-programming-foundations/03-algorithms-and-problem-solving.md) |
| **Edge case** | Unusual but valid input that often breaks naive code. | Module 00 Ch.03 |
| **State** | The data a program (or component) holds at a moment in time. | [Module 00 Ch.04](00-programming-foundations/04-data-and-basic-structures.md) |
| **Array** | Fixed-length indexed sequence of elements of one type. | Module 00 Ch.04 |
| **Big-O** | Notation classifying how cost grows as input size grows. | [Module 00 Ch.05](00-programming-foundations/05-complexity-and-debugging-mindset.md) |
| **Off-by-one** | Boundary error in loops or indexes (wrong by a single step). | Module 00 Ch.05 |
| **Logic bug** | Program runs without crashing but produces incorrect results. | Module 00 Ch.05 |
| **PATH** | OS list of directories searched for executables. | [Module 01 Ch.02](01-java-getting-started/02-installing-java-and-environment.md) |
| **JAVA_HOME** | Environment variable pointing at the JDK root directory. | Module 01 Ch.02 |
| **IDE** | Integrated Development Environment (editor + project + run/debug). | [Module 01 Ch.03](01-java-getting-started/03-ide-and-command-line-workflow.md) |
| **Exit code** | Integer status a process returns to the OS (0 often means success). | [Module 01 Ch.04](01-java-getting-started/04-first-java-program-deep-dive.md) |
| **Package** | Namespace for Java types; usually mirrors directories. | [Module 01 Ch.05](01-java-getting-started/05-project-structure-and-jshell.md) |
| **Classpath** | Locations where the JVM and `javac` search for user classes (directories and JARs). | [Module 01 Ch.01](01-java-getting-started/01-jdk-jre-jvm-and-tooling.md) / Ch.05 |
| **Garbage collection (GC)** | JVM automatic reclaiming of unreachable objects from the heap. | Module 00 Ch.01 / Module 01 Ch.01 |
| **Heap** | JVM-managed memory region for objects. | Module 00 Ch.02 |
| **JShell** | Official Java REPL included with the JDK. | Module 01 Ch.05 |
| **Reference type** | Type whose values are references to objects (or `null`), not inline primitive bits. | Module 02 Ch.01 |
| **Autoboxing** | Automatic conversion between a primitive and its wrapper type (e.g. `int` ↔ `Integer`). | Module 02 Ch.01 |
| **Primitive type** | One of Java’s eight built-in non-reference value types. | [Module 02 Ch.01](02-java-fundamentals/01-variables-and-types.md) |
| **Literal** | Source-code representation of a fixed value (e.g. `42`, `"hi"`). | Module 02 Ch.01 |
| **Expression** | Code that produces a value. | [Module 02 Ch.02](02-java-fundamentals/02-operators-and-expressions.md) |
| **Short-circuit** | Skipping evaluation of the right operand when the result is already known. | Module 02 Ch.02 |
| **Scope** | Region of a program where a declared name is visible. | [Module 02 Ch.03](02-java-fundamentals/03-scope-naming-and-comments.md) |
| **Widening** | Numeric conversion to a wider type without a cast. | [Module 02 Ch.04](02-java-fundamentals/04-type-conversion-and-inference.md) |
| **Narrowing** | Numeric conversion that may lose information; requires a cast. | Module 02 Ch.04 |
| **var** | Local-variable type inference (Java 10+); still statically typed. | Module 02 Ch.04 |
| **Fall-through** | Classic switch continuing into the next case without `break`. | [Module 03 Ch.02](03-control-flow/02-switch-and-switch-expressions.md) |
| **Switch expression** | A `switch` that produces a value (arrow form common). | Module 03 Ch.02 |
| **Enhanced for** | For-each loop over an array or `Iterable`. | [Module 03 Ch.03](03-control-flow/03-loops.md) |
| **Pattern matching** | Type test that binds a typed variable when it succeeds. | [Module 03 Ch.04](03-control-flow/04-branching-and-pattern-matching.md) |
| **Exhaustiveness** | All possible values/types of a switch selector are handled. | Module 03 Ch.04 |
| **Method** | Named block of code on a class that can take parameters and optionally return a value. | [Module 04 Ch.01](04-methods-and-functions/01-methods-basics.md) |
| **Overloading** | Multiple methods in the same class sharing a name but differing in parameter lists. | [Module 04 Ch.02](04-methods-and-functions/02-overloading-varargs-and-recursion.md) |
| **Varargs** | Trailing `Type... name` parameter accepting zero or more arguments as an array. | Module 04 Ch.02 |
| **Recursion** | A method calling itself (directly or indirectly) with a base case that stops the chain. | Module 04 Ch.02 |
| **Pass-by-value** | Method argument mechanism where the callee receives a copy of each argument value. | [Module 04 Ch.03](04-methods-and-functions/03-pass-by-value-and-stack-frames.md) |
| **Stack frame** | Storage for one active method invocation on a thread’s JVM stack (locals, parameters, return linkage). | Module 00 Ch.02 / Module 04 Ch.03 |
| **Static method** | Method belonging to a class, invoked without a receiver object. | [Module 04 Ch.04](04-methods-and-functions/04-static-instance-and-method-references.md) |
| **Instance method** | Method invoked on an object; implicit `this` receiver. | Module 04 Ch.04 |
| **Method reference** | `Type::name` or `obj::name` shorthand for a lambda that calls an existing method. | Module 04 Ch.04 |
| **Class** | Blueprint defining fields and methods for a type. | [Module 05 Ch.01](05-object-oriented-programming/01-oop-fundamentals.md) |
| **Object** | Runtime instance of a class, typically created with `new`. | Module 05 Ch.01 |
| **Encapsulation** | Hiding representation and controlling access so invariants can be enforced. | [Module 05 Ch.02](05-object-oriented-programming/02-encapsulation-and-abstraction.md) |
| **Abstraction** | Exposing essential operations while hiding implementation details. | Module 05 Ch.02 |
| **Inheritance** | `extends` relationship where a subclass is-a specialized superclass. | [Module 05 Ch.03](05-object-oriented-programming/03-inheritance-and-polymorphism-intro.md) |
| **Polymorphism** | Same call site; runtime type decides which overridden instance method runs. | Module 05 Ch.03 |
| **Dynamic dispatch** | JVM selects the overridden instance method using the receiver object’s runtime class. | [Module 07 Ch.03](07-inheritance-and-polymorphism/03-dynamic-dispatch-and-lsp.md) |
| **Liskov Substitution Principle (LSP)** | Subtypes must be replaceable for their supertypes without breaking caller expectations. | Module 07 Ch.03 |
| **Abstract class** | Class declared `abstract`; may declare abstract methods; cannot be instantiated with `new`. | [Module 07 Ch.02](07-inheritance-and-polymorphism/02-abstract-classes.md) |
| **Template method** | Superclass `final` algorithm invoking overridable hook methods in subclasses. | Module 07 Ch.02 |
| **Sealed type** | Class or interface restricting which types may directly extend or implement it (`permits`). | [Module 07 Ch.04](07-inheritance-and-polymorphism/04-sealed-classes.md) |
| **`protected`** | Member visible to subclasses and same-package code; part of an inheritance extension API. | [Module 07 Ch.01](07-inheritance-and-polymorphism/01-inheritance-deep-dive.md) |
| **Composition** | Has-a design where an object uses/owns another object as a part or collaborator. | [Module 05 Ch.04](05-object-oriented-programming/04-composition-and-object-relationships.md) |
| **Constructor chaining** | One constructor invoking another in the same class via `this(...)` as the first statement. | [Module 06 Ch.01](06-classes-and-objects/01-initialization-and-constructors.md) |
| **Static initializer** | `static { }` block or static field initializers run once when the class is initialized. | Module 06 Ch.01 |
| **Static nested class** | Nested class declared `static`; no implicit reference to an enclosing instance. | [Module 06 Ch.02](06-classes-and-objects/02-nested-and-inner-classes.md) |
| **Inner class** | Non-static nested class; each instance is tied to an enclosing outer instance. | Module 06 Ch.02 |
| **Anonymous class** | Class without a name, declared and instantiated in one `new SuperType() { }` expression. | Module 06 Ch.02 |
| **Immutability** | Object state is not intended to change after construction; updates modeled by new instances. | [Module 06 Ch.03](06-classes-and-objects/03-immutability-and-builders.md) |
| **Value object** | Type defined by its field values rather than object identity; often immutable. | Module 06 Ch.03 |
| **Builder pattern** | Mutable helper assembles optional configuration, then produces an immutable product in `build()`. | Module 06 Ch.03 |
| **Equals contract** | Reflexive, symmetric, transitive, consistent equality; `null` yields false. | [Module 06 Ch.04](06-classes-and-objects/04-equals-hashcode-and-tostring.md) |
| **hashCode contract** | Equal objects must share the same hash code; used by hash-based collections. | Module 06 Ch.04 |
| **Interface** | Reference type declaring operations (`implements`) that concrete classes must fulfill; enables multiple independent contracts per class. | [Module 08 Ch.01](08-interfaces-and-abstraction/01-interface-fundamentals.md) |
| **Programming contract** | Obligations at a type boundary: syntactic signatures plus documented behavioral guarantees callers and implementors must honor. | Module 08 Ch.01 |
| **`implements`** | Keyword declaring that a class satisfies one or more interface contracts. | Module 08 Ch.01 |
| **Default method** | Interface instance method with a body; inherited by implementors unless overridden (Java 8+). | [Module 08 Ch.02](08-interfaces-and-abstraction/02-default-static-and-private-methods.md) |
| **Functional interface** | Interface with exactly one abstract instance method (SAM); target type for lambdas and method references. | [Module 08 Ch.03](08-interfaces-and-abstraction/03-functional-and-marker-interfaces.md) |
| **Single abstract method (SAM)** | The one abstract instance method a functional interface exposes to lambda implementations. | Module 08 Ch.03 |
| **Marker interface** | Interface declaring no methods; tags a type for frameworks or domain rules (e.g. `Serializable`). | Module 08 Ch.03 |
| **Dependency Inversion Principle (DIP)** | High-level modules depend on abstractions; concrete details implement those abstractions at wiring time. | [Module 08 Ch.04](08-interfaces-and-abstraction/04-api-design-with-interfaces.md) |
| **Public API** | Types and members intended for use outside the owning library or package; stable dependency surface. | [Module 09 Ch.01](09-encapsulation-and-design/01-access-control-and-api-boundaries.md) |
| **Information hiding** | Concealing internal representation so callers cannot bypass invariants. | Module 09 Ch.01 |
| **Coupling** | Degree of interdependence between software units; lower is usually easier to change. | [Module 09 Ch.02](09-encapsulation-and-design/02-coupling-and-cohesion.md) |
| **Cohesion** | Degree to which elements inside one module belong to a single, focused purpose. | Module 09 Ch.02 |
| **Single Responsibility Principle (SRP)** | A class should have one reason to change—one axis of responsibility at its abstraction level. | Module 09 Ch.02 |
| **Delegation** | An object forwards a request to a collaborator that performs the work. | [Module 09 Ch.03](09-encapsulation-and-design/03-composition-versus-inheritance.md) |
| **Fragile base class** | Superclass change that breaks subclasses without edits to subclass code. | Module 09 Ch.03 |
| **Static typing** | Types of variables and expressions are checked at compile time before the program runs. | [Module 10 Ch.01](10-java-type-system/01-static-and-strong-typing.md) |
| **Strong typing** | Incompatible types do not mix silently; mismatches are compile errors or require explicit casts. | Module 10 Ch.01 |
| **Declared type** | The type written in source for a variable, parameter, or field; used by the compiler. | Module 10 Ch.01 |
| **Runtime class** | The `Class<?>` of the object actually referenced at run time; may differ from declared type under polymorphism. | Module 10 Ch.01 |
| **Type test** | Runtime check that a reference is assignment-compatible with a type, chiefly `instanceof`. | [Module 10 Ch.02](10-java-type-system/02-instanceof-and-pattern-matching.md) |
| **Type pattern** | Pattern such as `case String s` or `instanceof String s` that tests a type and binds a variable when it matches. | Module 10 Ch.02 |
| **Narrowing reference conversion** | Treating a supertype reference as a subtype after a safe proof (cast or pattern). | Module 10 Ch.02 |
| **Invariance (generics)** | `G<A>` is not a subtype of `G<B>` even when `A` extends `B`; prevents unsafe writes through the wrong static type. | [Module 10 Ch.03](10-java-type-system/03-variance-wildcards-and-erasure-preview.md) |
| **Wildcard** | Unknown or flexible type argument in generics: `?`, `? extends T`, or `? super T`. | [Module 13 Ch.02](13-generics/02-bounds-and-wildcards.md) (preview M10 Ch.03) |
| **PECS** | Producer Extends, Consumer Super—use `? extends T` when reading, `? super T` when writing. | Module 13 Ch.02 |
| **Type erasure** | Compile-time generic type information is removed in bytecode; type parameters become their bounds or `Object` at runtime. | [Module 13 Ch.03](13-generics/03-type-erasure-and-restrictions.md) (preview M10 Ch.03) |
| **Type parameter** | Formal placeholder on a generic class or method declaration (`T` in `Box<T>`). | [Module 13 Ch.01](13-generics/01-generic-types-and-methods.md) |
| **Type argument** | Concrete type substituted for a type parameter at a use site (`String` in `Box<String>`). | Module 13 Ch.01 |
| **Raw type** | Generic type or interface used without type arguments—weakens static checking for legacy interop. | Module 13 Ch.01 |
| **Bounded type parameter** | Type parameter constrained with `extends` (`T extends Number`) or intersection (`T extends A & B`). | [Module 13 Ch.02](13-generics/02-bounds-and-wildcards.md) |
| **Bridge method** | Synthetic method emitted so overriding remains correct after type erasure changes erased signatures. | Module 13 Ch.03 |
| **Heap pollution** | Illegal element types present at runtime in a generically typed collection due to arrays, raw types, or unsafe casts. | Module 13 Ch.03 |
| **Wildcard capture** | Compiler technique introducing a fresh type variable so code operating on wildcard parameters type-checks. | Module 13 Ch.02 |
| **Array** | Fixed-length, indexed sequence of elements of one type; length set at creation and exposed via `length` field. | [Module 11 Ch.01](11-arrays-and-strings/01-arrays.md) |
| **String immutability** | After construction, a `String`'s character sequence cannot change; mutating APIs return new strings. | [Module 11 Ch.02](11-arrays-and-strings/02-strings-and-immutability.md) |
| **String pool** | JVM-managed set of canonical string literals and interned strings that may share identity for equal content. | Module 11 Ch.02 |
| **StringBuilder** | Mutable, growable character buffer for efficient string assembly; not thread-safe (unlike `StringBuffer`). | [Module 11 Ch.03](11-arrays-and-strings/03-stringbuilder-and-text-blocks.md) |
| **Text block** | Multiline string literal delimited by `"""` with compiler rules for incidental indentation stripping (JEP 378). | Module 11 Ch.03 |
| **Code unit (UTF-16)** | 16-bit `char` value stored in a `String`; one code point may use one or two code units. | [Module 11 Ch.04](11-arrays-and-strings/04-encoding-and-string-performance.md) |
| **Code point** | Unicode scalar value (U+XXXX); may span one or two UTF-16 code units in a `String`. | Module 11 Ch.04 |
| **Charset** | Named mapping between sequences of bytes and characters; e.g. `UTF-8` for interchange. | Module 11 Ch.04 |
| **Collection (framework)** | Standard `java.util` interfaces and implementations for grouped data (`List`, `Set`, `Map`, …). | [Module 12 Ch.01](12-collections-framework/01-collection-hierarchy.md) |
| **List** | Ordered `Collection` with positional access, duplicates allowed (`get`, `add`, indexed insert). | Module 12 Ch.02 |
| **ArrayList** | Default `List` backed by a growable array; O(1) random access, amortized tail append. | Module 12 Ch.02 |
| **LinkedList** | Doubly-linked `List` and `Deque`; fast head/tail ops, slow indexed `get`. | Module 12 Ch.02 |
| **Set** | `Collection` with no duplicate elements (uniqueness by `equals`). | [Module 12 Ch.03](12-collections-framework/03-sets-and-queues.md) |
| **Queue** | Collection for producer–consumer handoff; typically FIFO (`offer`, `poll`, `peek`). | Module 12 Ch.03 |
| **Deque** | Double-ended queue; add/remove at both ends (`ArrayDeque`, `LinkedList`). | Module 12 Ch.03 |
| **Map** | Key-to-value associations; not a `Collection` of elements. | [Module 12 Ch.04](12-collections-framework/04-maps-and-hashing.md) |
| **HashMap** | Hash table map; bucket index from `hashCode`, key match via `equals`. | Module 12 Ch.04 |
| **Fail-fast iterator** | Iterator that throws `ConcurrentModificationException` on detected structural change during iteration. | [Module 12 Ch.05](12-collections-framework/05-internals-and-choosing-structures.md) |
| **Throwable** | Root type for all errors and exceptions in Java; carries message, optional cause, and stack trace. | [Module 14 Ch.01](14-exceptions-and-error-handling/01-throwable-hierarchy.md) |
| **Checked exception** | `Exception` subtype that is not a `RuntimeException`; callers must catch or declare `throws`. | Module 14 Ch.01 |
| **Unchecked exception** | `RuntimeException` or `Error`; no compile-time requirement to catch or declare on callers. | Module 14 Ch.01 |
| **Try-with-resources** | `try` declaring `AutoCloseable` resources; `close()` runs automatically in defined order. | [Module 14 Ch.02](14-exceptions-and-error-handling/02-try-catch-finally-and-try-with-resources.md) |
| **Suppressed exception** | Secondary failure recorded on a primary throwable (e.g. `close()` after body throws) via `getSuppressed()`. | Module 14 Ch.02 |
| **Stack trace** | Ordered list of call frames captured when a throwable is created, used for debugging. | Module 14 Ch.01 |

Add new terms when chapters introduce them. Keep definitions beginner-accessible and technically precise.
