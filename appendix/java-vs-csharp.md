# Java vs C# (for Java learners)

> **Status:** Completed  
> **Purpose:** Map familiar C# / .NET ideas onto Java 21 accurately—**not** a C# course  
> **Target:** Java 21; HotSpot / Spring / ASP.NET notes labeled  
> **Last updated:** 2026-11-11  
> **Related:** [14-exceptions](../14-exceptions-and-error-handling/README.md) · [15-records](../15-enums-records-and-sealed-types/02-records.md) · [17-streams](../17-stream-api/README.md) · [24-concurrency](../24-concurrency-and-multithreading/README.md) · [36-spring](../36-spring-framework/README.md)

You do **not** need C# to learn Java. This appendix helps developers who already know .NET, or who see C# mentioned in discussions, transfer intuition without confusing the runtimes.

---

## Snapshot

| Dimension | Java (21) | C# / .NET (typical modern) |
|-----------|-----------|------------------------------|
| Runtime | JVM (HotSpot common) | CLR / CoreCLR |
| Language family | Java SE | C# on .NET |
| Checked exceptions | Yes (language feature) | No (exceptions are unchecked-style) |
| Data carriers | `record` | `record` / `record struct` |
| Query style | Stream API + collectors | LINQ |
| Async style | Threads, `CompletableFuture`, **virtual threads** | `async`/`await` + `Task` |
| Web frameworks | Spring MVC / WebFlux (ecosystem) | ASP.NET Core |
| Nullability | Historically pervasive `null`; annotations / Optional | Nullable reference types (compiler feature) |

---

## CLR vs JVM

Both are managed runtimes with garbage collection, JIT (and sometimes AOT), and rich standard libraries.

| Concern | JVM | CLR |
|---------|-----|-----|
| Primary artifact | Bytecode in `.class` / JAR | IL in assemblies |
| Generics | Type erasure (mostly) | Reified generics |
| Tooling culture | Maven/Gradle, JDK tools, JFR | `dotnet` CLI, Visual Studio / Rider |
| Cross-language | JVM languages (Kotlin, Scala, …) | Many .NET languages (C#, F#, …) |

**HotSpot note:** tiered compilation, G1/ZGC, and escape analysis are JVM-implementation topics ([Modules 26–28](../26-jvm-internals/README.md)). Do not assume CLR GC/JIT behavior when answering Java interview questions.

**Transfer tip:** Your mental model of “managed code + GC + JIT warm-up” transfers. Details of class loading, the JMM, and erasure are Java-specific and must be learned freshly.

---

## Checked exceptions

Java distinguishes **checked** exceptions (part of a method’s compile-time contract) from **unchecked** ones ([Module 14](../14-exceptions-and-error-handling/01-throwable-hierarchy.md)). C# does not have checked exceptions; callers are not forced by the compiler to handle specific exception types.

### Illustrative side-by-side (error handling)

**Illustrative** — shapes only.

```csharp
// C# — exceptions unchecked by the compiler
try {
    return await File.ReadAllBytesAsync(path);
} catch (IOException ex) {
    throw new InvalidOperationException("read config", ex);
}
```

```java
// Java — IOException is checked at the API boundary
try (var in = Files.newInputStream(path)) {
    return in.readAllBytes();
} catch (IOException e) {
    throw new UncheckedIOException("read config", e);
}
```

**Practical advice for Java:**

- Use checked exceptions sparingly for recoverable, caller-relevant failures at API boundaries.
- Prefer unchecked exceptions for programming errors and many domain failures inside applications—consistent with much modern Java framework practice.
- Do not “fix” checked exceptions by empty `catch` blocks; that is worse than C#’s model.

---

## Records vs C# records

Both languages added **records** as concise, value-oriented data carriers with compiler-generated equality and string representations.

| Aspect | Java `record` | C# `record` (class) / `record struct` |
|--------|---------------|----------------------------------------|
| Mutability default | Shallowly immutable components | `init`/immutable by default for positional records; structs differ |
| Inheritance | Can implement interfaces; limited extension | Inheritance patterns differ (e.g. withers / `with`) |
| Pattern matching | Record patterns in `switch` / `instanceof` (Java 21) | Pattern matching evolving in C# |

Learn Java records on Java terms: canonical constructor, compact constructor, accessors named after components—not as a 1:1 copy of C# syntax ([Module 15 Ch.02](../15-enums-records-and-sealed-types/02-records.md)).

---

## LINQ vs Streams

LINQ provides query comprehension over sequences/enumerables, often with deferred execution. Java’s **Stream API** is a lazy pipeline of intermediate and terminal operations ([Module 17](../17-stream-api/README.md)).

| Idea | Java Streams | LINQ |
|------|--------------|------|
| Laziness | Intermediate ops lazy until terminal | Deferred execution common |
| Parallelism | `parallel()` with caveats | PLINQ |
| Mutating collectors | `collect(Collectors…)` | Materializing operators (`ToList`, …) |
| Checked exceptions | Awkward inside lambdas | Different exception culture |

**Transfer tip:** `Where` ≈ `filter`, `Select` ≈ `map`, `SelectMany` ≈ `flatMap`, `Aggregate` ≈ `reduce`/`collect`. Prefer idiomatic collectors and readability over forcing LINQ query-comprehension style into Java.

---

## async/await vs CompletableFuture / virtual threads

C#’s `async`/`await` is a first-class language feature over `Task` (and friends), shaping how libraries expose I/O.

Java historically composed async work with callbacks, executors, and **`CompletableFuture`** ([Module 24 Ch.03](../24-concurrency-and-multithreading/03-executors-and-futures.md)). **Java 21 virtual threads** change the recommended default for many server apps: write blocking-style code on virtual threads instead of turning every API async.

### Illustrative concurrency mental models

**Illustrative** — idioms, not Verified equivalence.

```csharp
// C# — async/await composition
async Task<string> LoadAsync(string id) {
    var a = await client.GetAsync(id);
    var b = await repo.FindAsync(id);
    return a + b;
}
```

```java
// Java 21 — blocking style on a virtual thread (common server default)
String load(String id) {
    var a = client.get(id);   // blocking I/O
    var b = repo.find(id);
    return a + b;
}
// Fan-out still uses CompletableFuture / structured concurrency when useful
```

| Need | Prefer in modern Java 21 |
|------|---------------------------|
| Huge numbers of blocking I/O tasks | Virtual threads + structured concurrency APIs |
| Fan-out composition / reactive-shaped APIs | `CompletableFuture` or reactive stacks when appropriate |
| CPU-bound parallelism | Platform thread pools sized to cores |

**Framework note:** Spring WebFlux and servlet stacks have different threading stories; know which you are on ([Module 37](../37-spring-boot/README.md)).

Do not assume Java has C#-identical `async`/`await` syntax. Project Loom’s virtual threads are the strategic answer for many “async for scalability” cases.

---

## Spring vs ASP.NET (awareness)

Both are dominant web/application frameworks on their platforms—not language features.

| Concern | Spring / Spring Boot | ASP.NET Core |
|---------|----------------------|--------------|
| DI | First-class IoC container | Built-in DI |
| HTTP APIs | Spring MVC controllers / WebFlux | Controllers / minimal APIs |
| Config | `application.properties`/`yaml`, profiles | `appsettings`, environments |
| Security | Spring Security | ASP.NET Core Identity / middleware |

Learn Spring’s **IoC, proxies, bean lifecycle, and transactions** as Java-ecosystem mechanisms ([Module 36](../36-spring-framework/README.md)). ASP.NET experience helps with HTTP and middleware intuition; it does not replace Spring’s proxy/AOP transaction model.

---

## Packaging and tooling

| Concern | Java | C# / .NET |
|---------|------|-----------|
| CLI | `javac` / `java` + Maven/Gradle | `dotnet` build/test/run |
| Project file | `pom.xml` / `build.gradle(.kts)` | `.csproj` / `.sln` |
| Package feed | Maven Central (and privates) | NuGet |
| Test | JUnit / TestNG via build tool | xUnit / NUnit / MSTest |
| Format / analyze | Spotless, Checkstyle, Error Prone | `dotnet format`, analyzers / Roslyn |
| Diagnostics | JFR, `jcmd`, heap dumps | dotnet-trace, dump, PerfView culture |
| AOT / native | GraalVM native image (opt-in) | Native AOT evolving on .NET |
| DI in frameworks | Spring IoC (ecosystem) | Built-in + ASP.NET extensions |
| Config culture | `application.yml` / profiles / env | `appsettings` / environments / secrets managers |

**Transfer tip:** `dotnet restore/build/test` maps mentally to Maven/Gradle lifecycle phases; learn Java dependency *scopes* (compile/runtime/test/provided) explicitly—NuGet’s model will not translate by muscle memory alone.

---

## Other sharp edges for .NET → Java transfers

1. **Properties vs getters:** Java uses methods (`getX`/`setX` or records’ accessors); fields are not C# properties.
2. **`==` on references:** Reference identity unless operators overloaded (Java does not overload `==` for arbitrary types); use `equals` for logical equality ([Module 06](../06-classes-and-objects/04-equals-hashcode-and-tostring.md)).
3. **Generics erasure:** CLR reification intuition fails for Java runtime casts and `new T()` restrictions ([Module 13](../13-generics/03-type-erasure-and-restrictions.md)).
4. **`var`:** Local-variable type inference exists in both; Java `var` is still static typing ([Module 02](../02-java-fundamentals/04-type-conversion-and-inference.md)).
5. **Nullable reference types:** C#’s compiler feature is stronger than Java’s historical model; Java relies on discipline, annotations, Optional, and analysis tools ([Module 18](../18-optional-and-null-handling/README.md)).

---

## Common migration mistakes (.NET → Java)

1. **Expecting `async`/`await` everywhere.** Prefer virtual threads for many I/O services; use `CompletableFuture` when composition demands it.
2. **Assuming reified generics.** `List<String>` is not a distinct runtime class; `new T()` and runtime casts behave differently than on the CLR.
3. **Using `==` for string/value equality.** Prefer `equals` / `Objects.equals` (and beware interning folklore).
4. **Mapping properties 1:1 onto public fields.** Idiomatic Java uses accessors, records, or libraries (Lombok is optional culture—not language).
5. **Treating Spring like ASP.NET middleware only.** Learn proxies, self-invocation traps, and transaction boundaries.
6. **Swallowing checked exceptions to “feel like C#.”** Wrap with context or redesign the API; do not empty-catch.

---

## When each shines

### Prefer Java when

- Organization standardized on the JVM / Spring ecosystem
- You need Java’s library breadth, Android-adjacent skills (separate ecosystem), or polyglot JVM services
- Deploying on platforms where JVM ops expertise already exists

### Prefer C# / .NET when (awareness)

- Team and cloud tooling are centered on .NET
- You want language-integrated `async`/`await` and reified generics throughout
- Windows-heavy enterprise stacks historically tied to .NET (less exclusive than before)

### Learning advice

Treat Java’s JMM, erasure, class loading, and exception model as first-class study topics. Borrow architectural taste from .NET freely; do not borrow incorrect runtime assumptions.

---

## See also

- Exceptions: [14/README](../14-exceptions-and-error-handling/README.md)
- Streams: [17/README](../17-stream-api/README.md)
- Virtual threads: [24/05](../24-concurrency-and-multithreading/05-virtual-threads-and-structured-concurrency.md)
- Spring IoC: [36/01](../36-spring-framework/01-ioc-and-dependency-injection.md)
