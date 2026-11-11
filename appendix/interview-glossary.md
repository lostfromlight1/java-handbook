# Interview Glossary (Java)

> **Status:** Completed  
> **Audience:** Interview revision and quick lookup  
> **Target:** Java 21; HotSpot/framework notes labeled  
> **Last updated:** 2026-11-11  
> **Related:** [Module 52 — Interview Preparation](../52-interview-preparation/README.md) · [GLOSSARY.md](../GLOSSARY.md)

Concise definitions of interview-hot Java terms. Each entry is a short explanation (not a one-liner), then a link into the handbook for depth. Organize study by area; do not treat this file as a substitute for the chapters.

---

## How to use this glossary

1. Read the short explanation until you can restate it in your own words.
2. Open the linked chapter when an interviewer probes mechanism, trade-offs, or edge cases.
3. Prefer **language/JVM guarantees** over folklore; treat **HotSpot** and **Spring** details as labeled implementation notes.

---

## Language and type system

### Static typing

Java checks types at compile time. Most misuse of APIs fails before the program runs, which shifts many defects left and enables IDE refactoring. Runtime still has `ClassCastException` and reflection-shaped holes; static typing is not a proof of correctness.

**Deeper:** [10-java-type-system/01-static-and-strong-typing.md](../10-java-type-system/01-static-and-strong-typing.md)

### Type erasure

Generics are largely a compile-time construct. The compiler inserts casts and checks; at runtime most type parameters are erased to their bounds (often `Object`). That is why you cannot write `new T()`, why `List<String>` and `List<Integer>` share one raw runtime class shape, and why reified generics (arrays, some reflection patterns) need care.

**Deeper:** [13-generics/03-type-erasure-and-restrictions.md](../13-generics/03-type-erasure-and-restrictions.md)

### Covariance / contravariance / wildcards

Arrays are covariant and unsafe (`Object[] a = new String[1]`). Generic collections are invariant by default; `? extends T` (producer) and `? super T` (consumer) recover useful variance at API boundaries (PECS). Interview answers should connect wildcards to safety, not only to syntax.

**Deeper:** [13-generics/02-bounds-and-wildcards.md](../13-generics/02-bounds-and-wildcards.md) · [10-java-type-system/03-variance-wildcards-and-erasure-preview.md](../10-java-type-system/03-variance-wildcards-and-erasure-preview.md)

### Record

A `record` is a nominal, shallowly immutable data carrier: components in the header drive accessors, `equals`, `hashCode`, and `toString`. Use records for DTOs and value-shaped types; do not treat them as a full domain-model replacement when identity or mutable lifecycle matters.

**Deeper:** [15-enums-records-and-sealed-types/02-records.md](../15-enums-records-and-sealed-types/02-records.md)

### Sealed types

`sealed` restricts which types may extend or implement a type (`permits`). Combined with pattern switches, they enable compiler-checked exhaustiveness. Prefer sealed hierarchies when the set of variants is closed by design.

**Deeper:** [15-enums-records-and-sealed-types/03-sealed-types-and-patterns.md](../15-enums-records-and-sealed-types/03-sealed-types-and-patterns.md)

### Pass-by-value

Java always passes arguments by value. For reference types the value is a copy of the reference, not a deep copy of the object. Reassigning a parameter does not change the caller’s variable; mutating a shared object through that reference does.

**Deeper:** [04-methods-and-functions/03-pass-by-value-and-stack-frames.md](../04-methods-and-functions/03-pass-by-value-and-stack-frames.md)

### Checked vs unchecked exceptions

Checked exceptions are part of a method’s compile-time contract (`throws` / must handle). Unchecked exceptions (`RuntimeException` and subclasses, plus `Error`) are not. Interviewers often probe API design: when checked exceptions clarify recoverable failure vs when they create noisy call sites.

**Deeper:** [14-exceptions-and-error-handling/01-throwable-hierarchy.md](../14-exceptions-and-error-handling/01-throwable-hierarchy.md)

---

## Memory, GC, and JMM

### Heap vs stack

Each thread has a JVM stack of frames (locals, operands, return linkage). Objects live on the heap (with HotSpot optimizations such as scalar replacement as an implementation detail). Stack frames disappear when methods return; heap objects become reclaimable when unreachable.

**Deeper:** [25-java-memory-model/01-stack-heap-and-references.md](../25-java-memory-model/01-stack-heap-and-references.md) · [26-jvm-internals/04-runtime-data-areas.md](../26-jvm-internals/04-runtime-data-areas.md)

### Java Memory Model (JMM)

The JMM defines which writes one thread is allowed to observe from another, and when reordering is legal. It is a **language/JVM specification** contract, not a description of a particular CPU. Without synchronization, threads may see stale or surprising interleavings even on “obvious” field assignments.

**Deeper:** [25-java-memory-model/README.md](../25-java-memory-model/README.md) · [52-interview-preparation/02-jvm-and-concurrency-interviews.md](../52-interview-preparation/02-jvm-and-concurrency-interviews.md)

### Happens-before (HB)

Happens-before is the JMM’s ordering relation: if action A happens-before action B, B is guaranteed to see A’s effects (among other constraints). Locks, `volatile` write/read pairs, thread start/join, and certain concurrent utilities establish HB edges. HB is transitive along a chain of edges. Saying “volatile means no cache” is imprecise; prefer HB and visibility wording. Interview refresh: HB is a *spec* guarantee about observable order—not a claim that every CPU instruction ran in source order.

**Deeper:** [25-java-memory-model/02-happens-before-and-visibility.md](../25-java-memory-model/02-happens-before-and-visibility.md)

### False sharing

**HotSpot / hardware awareness:** two threads update different fields or array slots that share a CPU cache line; each write invalidates the other’s line and burns bandwidth even though the *logical* data is unshared. Classic when adjacent `long` counters live in one object. Padding, `@Contended` (internal/`jdk.internal`), or redesigning so hot fields are not adjacent are mitigations—measure first; do not pad everything.

**Deeper:** [28-jit-and-performance/03-profiling.md](../28-jit-and-performance/03-profiling.md) · [24-concurrency-and-multithreading/02-synchronization-and-atomics.md](../24-concurrency-and-multithreading/02-synchronization-and-atomics.md)

### Biased locking (historical HotSpot)

**HotSpot historical:** biased locking optimized uncontended `synchronized` by “biasing” a monitor toward one thread so repeated lock/unlock avoided heavyweight atomics. Contended cases revoked bias (costly). Modern HotSpot/JDK lines have **disabled or removed** biased locking by default as virtual threads and lock implementations evolved—treat it as interview *history*, not current tuning advice. Prefer understanding monitors, HB, and contention over quoting obsolete `-XX` folklore.

**Deeper:** [24-concurrency-and-multithreading/02-synchronization-and-atomics.md](../24-concurrency-and-multithreading/02-synchronization-and-atomics.md) · [28-jit-and-performance/01-hotspot-and-tiered-compilation.md](../28-jit-and-performance/01-hotspot-and-tiered-compilation.md)

### Safe publication

Publishing an object so other threads see a fully initialized state requires a safe pattern (static init, `volatile`/`final` fields, locking, concurrent collections, and so on). An improperly published object can expose default field values even if the constructing thread finished writing.

**Deeper:** [25-java-memory-model/03-safe-publication.md](../25-java-memory-model/03-safe-publication.md)

### GC roots

GC roots are starting points from which the collector traces live objects: thread stacks, static fields, JNI references, and similar. An object is unreachable (and eligible for collection) when no path from any root reaches it. “Memory leaks” in Java are usually unintended retention from a root, not failure to free.

**Deeper:** [27-garbage-collection/01-gc-fundamentals.md](../27-garbage-collection/01-gc-fundamentals.md)

### Generational hypothesis / G1 / ZGC

**HotSpot:** Young objects often die young; generational collectors exploit that. G1 is a common default regional collector; ZGC targets low pause times with different trade-offs. Interview answers should separate **algorithm ideas** from **flags and defaults of one JDK build**.

**Deeper:** [27-garbage-collection/02-collectors-g1-zgc-shenandoah.md](../27-garbage-collection/02-collectors-g1-zgc-shenandoah.md)

### Soft / weak / phantom references

Reference strength controls reachability for caches and cleanup. Soft references may linger until memory pressure; weak references do not keep an object alive for their own sake; phantom references support post-mortem cleanup patterns. Prefer clear ownership and bounded caches over clever reference graphs unless you need them.

**Deeper:** [27-garbage-collection/03-tuning-and-memory-leaks.md](../27-garbage-collection/03-tuning-and-memory-leaks.md)

---

## Class loading, bytecode, JIT

### ClassLoader

A `ClassLoader` locates and defines classes, typically forming a hierarchy (bootstrap → platform → application, plus custom loaders). The same binary name loaded by two loaders yields distinct `Class` objects. SPI, plugins, and application servers lean on this model; classloader leaks are a classic production topic.

**Deeper:** [26-jvm-internals/02-class-loading-and-linking.md](../26-jvm-internals/02-class-loading-and-linking.md)

### Linking and initialization

After loading, the JVM verifies, prepares, and optionally resolves symbolic references, then initializes the class (`<clinit>`). Initialization is synchronized per class; static fields and blocks run under well-defined rules. Interview traps include circular initialization and relying on initialization order across classes.

**Deeper:** [26-jvm-internals/02-class-loading-and-linking.md](../26-jvm-internals/02-class-loading-and-linking.md)

### Bytecode / interpreter / JIT

`.class` files contain bytecode. HotSpot typically interprets first, then **JIT**-compiles hot methods to native code. Tiered compilation (C1/C2 on HotSpot) balances startup and peak performance. “Java is always interpreted” is false for production HotSpot after warm-up.

**Deeper:** [26-jvm-internals/03-bytecode-and-execution.md](../26-jvm-internals/03-bytecode-and-execution.md) · [28-jit-and-performance/01-hotspot-and-tiered-compilation.md](../28-jit-and-performance/01-hotspot-and-tiered-compilation.md)

### Escape analysis

**HotSpot optimization:** if an object does not escape a method or thread in a way the JIT can prove, the compiler may scalar-replace it or stack-allocate related state—so `new` is not always a heap allocation in optimized code. This is an optimization, not a language guarantee; microbenchmarks without JMH often mislead.

**Deeper:** [28-jit-and-performance/01-hotspot-and-tiered-compilation.md](../28-jit-and-performance/01-hotspot-and-tiered-compilation.md) · [28-jit-and-performance/02-jmh-and-benchmarking.md](../28-jit-and-performance/02-jmh-and-benchmarking.md)

### Method handles / VarHandle / FFM

Method handles and `VarHandle` are typed, JVM-level access mechanisms used by `invokedynamic`, concurrency utilities, and low-level libraries. The Foreign Function & Memory API (FFM) is the modern path for off-heap memory and native calls (Java 21+ emphasis). Prefer them over obsolete `Unsafe` patterns when discussing modern Java.

**Deeper:** [50-advanced-java/02-method-handles-varhandles-and-ffm.md](../50-advanced-java/02-method-handles-varhandles-and-ffm.md)

---

## Concurrency

### Platform thread vs virtual thread

Platform threads map closely to OS threads (expensive at huge scale). **Virtual threads** (Java 21) are JVM-scheduled lightweight threads suited to high-concurrency blocking I/O. They do not magically make CPU-bound work faster; pin carefully around long native/synchronized sections (**HotSpot/implementation nuances** matter in interviews).

**Deeper:** [24-concurrency-and-multithreading/05-virtual-threads-and-structured-concurrency.md](../24-concurrency-and-multithreading/05-virtual-threads-and-structured-concurrency.md)

### Synchronized / monitors / atomics

`synchronized` provides mutual exclusion and HB edges via monitor enter/exit. `java.util.concurrent.atomic` offers lock-free (or lighter) updates for single variables. Prefer clarity: correctness first, then measure contention.

**Deeper:** [24-concurrency-and-multithreading/02-synchronization-and-atomics.md](../24-concurrency-and-multithreading/02-synchronization-and-atomics.md)

### ExecutorService / Future / CompletableFuture

Thread pools (`ExecutorService`) bound concurrency and reuse platform threads. `Future` is a handle to an asynchronous result; `CompletableFuture` composes async stages. With virtual threads, many designs return to straightforward blocking style; CF remains useful for fan-out/fan-in and callback-shaped APIs.

**Deeper:** [24-concurrency-and-multithreading/03-executors-and-futures.md](../24-concurrency-and-multithreading/03-executors-and-futures.md)

### Concurrent collections

`ConcurrentHashMap`, blocking queues, and related types provide thread-safe data structures with documented consistency guarantees (often weaker than “entire composite operation is atomic”). Know what is atomic (single ops) vs what still needs higher-level coordination.

**Deeper:** [24-concurrency-and-multithreading/04-concurrent-collections.md](../24-concurrency-and-multithreading/04-concurrent-collections.md)

### Deadlock / livelock / starvation

Deadlock: threads wait circularly on locks. Livelock: threads keep changing state but make no progress. Starvation: a thread never gets scheduled resources. Detection (thread dumps) and prevention (lock ordering, timeouts, smaller critical sections) are interview staples.

**Deeper:** [31-debugging-and-troubleshooting/02-dumps-and-flight-recorder.md](../31-debugging-and-troubleshooting/02-dumps-and-flight-recorder.md)

---

## APIs, OOP, and libraries

### equals / hashCode contract

Equal objects must have equal hash codes; hash-based collections rely on this. Override both together; include the same significant fields; beware mutable keys. Identity (`==`) is not logical equality.

**Deeper:** [06-classes-and-objects/04-equals-hashcode-and-tostring.md](../06-classes-and-objects/04-equals-hashcode-and-tostring.md)

### SPI (Service Provider Interface)

SPI is a discovery pattern: an interface plus provider implementations listed for `ServiceLoader` (or framework equivalents). JDBC drivers historically used this style; modern frameworks also use classpath scanning. It decouples API JARs from implementation JARs.

**Deeper:** [20-java-standard-library/03-reflection-annotations-and-process-apis.md](../20-java-standard-library/03-reflection-annotations-and-process-apis.md) · [29-build-tools-and-dependency-management/01-classpath-jars-and-modules.md](../29-build-tools-and-dependency-management/01-classpath-jars-and-modules.md)

### Optional

`Optional` is a container for a value that may be absent, intended mainly as a **return type** to make absence explicit. Using it for fields or method parameters is usually a design smell; `Optional` is not a silver bullet for null safety.

**Deeper:** [18-optional-and-null-handling/02-optional-api.md](../18-optional-and-null-handling/02-optional-api.md)

### Stream pipeline

A stream is a lazy pipeline of intermediate ops ending in a terminal op. Prefer clarity over clever one-liners; parallel streams need care (stateless ops, suitable spliterators, meaningful work). Streams are not a replacement for all loops.

**Deeper:** [17-stream-api/01-stream-fundamentals.md](../17-stream-api/01-stream-fundamentals.md)

### Try-with-resources

`try (Resource r = ...)` ensures `AutoCloseable` resources are closed in reverse order, even when exceptions occur. Prefer it over manual `finally` close blocks for I/O and JDBC-shaped resources.

**Deeper:** [14-exceptions-and-error-handling/02-try-catch-finally-and-try-with-resources.md](../14-exceptions-and-error-handling/02-try-catch-finally-and-try-with-resources.md)

---

## Frameworks and architecture (often Spring-flavored)

### Dependency Injection (DI) / IoC

Instead of a class constructing all dependencies itself, an assembler (container or manual wiring) supplies them—typically via constructors. **Inversion of Control** means the framework calls your code. DI improves testability and boundaries when used for true dependencies, not as a global service locator.

**Framework note:** Spring’s IoC container is the common interview context, not a language feature.

**Deeper:** [36-spring-framework/01-ioc-and-dependency-injection.md](../36-spring-framework/01-ioc-and-dependency-injection.md)

### Proxy (JDK / CGLIB) and AOP

A proxy wraps a target object to add cross-cutting behavior (transactions, security, metrics). JDK proxies implement interfaces; subclass (CGLIB-style) proxies extend concrete classes (**Spring/framework detail**). Self-invocation inside the same class often bypasses the proxy—classic interview trap.

**Deeper:** [36-spring-framework/02-bean-lifecycle-and-aop.md](../36-spring-framework/02-bean-lifecycle-and-aop.md) · [50-advanced-java/01-reflection-proxies-and-annotations.md](../50-advanced-java/01-reflection-proxies-and-annotations.md)

### Bean lifecycle

Spring beans are instantiated, dependencies injected, initialized (`@PostConstruct` / `InitializingBean`), used, then destroyed. Scopes (singleton, request, …) change identity and lifetime. Interview depth usually means constructor injection + proxy implications + circular dependency symptoms.

**Deeper:** [36-spring-framework/02-bean-lifecycle-and-aop.md](../36-spring-framework/02-bean-lifecycle-and-aop.md)

### Connection pool

Opening a TCP + auth handshake per SQL call is expensive. A **connection pool** keeps a bounded set of live DB connections and lends them to callers. Pool size, validation, leak detection, and timeout settings are operational concerns; “max pool = traffic” without measuring often starves or overwhelms the database. In Java backends this is typically HikariCP (Spring Boot default) or similar—not a language feature.

**Deeper:** [33-databases-and-jdbc/03-transactions-and-pooling.md](../33-databases-and-jdbc/03-transactions-and-pooling.md)

### ORM / lazy loading / N+1

Object-relational mappers map tables to entities. Lazy associations defer loads until accessed; outside an open persistence context this can fail or explode into **N+1** queries (one query for parents, then one per child). Fetch joins, entity graphs, batch fetching, and DTO projections are practical remedies. Interview depth: show you can spot N+1 in logs/metrics, not only name it.

**Deeper:** [35-hibernate-and-jpa/02-relationships-and-fetching.md](../35-hibernate-and-jpa/02-relationships-and-fetching.md)

### Transaction boundary

A transaction groups work that should commit or roll back together. In Spring, declarative transactions typically apply at proxy boundaries (public methods on Spring beans). Checked vs unchecked rollback rules and propagation are frequent follow-ups.

**Deeper:** [33-databases-and-jdbc/03-transactions-and-pooling.md](../33-databases-and-jdbc/03-transactions-and-pooling.md) · [35-hibernate-and-jpa/03-transactions-caching-and-performance.md](../35-hibernate-and-jpa/03-transactions-caching-and-performance.md)

---

## Distributed systems (interview awareness)

### CAP (informal interview usage)

Brewer’s CAP idea: under partition, a system emphasizes consistency or availability. Real systems are more nuanced (latency, partial failure, tunable consistency). Use CAP as a vocabulary for trade-offs, not as a three-checkbox taxonomy that “proves” an architecture.

**Deeper:** [44-microservices-and-distributed-systems/01-distributed-fundamentals.md](../44-microservices-and-distributed-systems/01-distributed-fundamentals.md)

### Saga

A saga coordinates a long-running business transaction across services with local transactions plus compensating actions (or orchestration/choreography). It is not a distributed ACID transaction; design for partial failure and idempotency.

**Deeper:** [44-microservices-and-distributed-systems/03-consistency-and-sagas.md](../44-microservices-and-distributed-systems/03-consistency-and-sagas.md)

### Circuit breaker / bulkhead / retry

Resilience patterns limit blast radius: a **circuit breaker** fails fast (open) when a dependency is unhealthy, probes recovery (half-open), then closes on success; a **bulkhead** isolates thread pools/connections so one dependency cannot exhaust the whole process; **retry** with backoff/jitter handles transient faults. Blind retries without **idempotency** amplify outages. Name the failure mode you are defending against—timeouts, overload, or partial dependency death.

**Deeper:** [44-microservices-and-distributed-systems/02-resilience-patterns.md](../44-microservices-and-distributed-systems/02-resilience-patterns.md)

### Outbox / delivery guarantees

At-least-once delivery is common in messaging; exactly-once usually means idempotent consumers plus careful producer design (for example transactional outbox). “The broker guarantees exactly-once” is rarely the full story end-to-end.

**Deeper:** [45-messaging-and-event-driven-systems/03-delivery-guarantees-and-outbox.md](../45-messaging-and-event-driven-systems/03-delivery-guarantees-and-outbox.md)

### Idempotency

An operation is idempotent if repeating it with the same logical intent does not corrupt state. Critical for HTTP retries, consumers, and saga compensations. Often implemented with idempotency keys and deduplication stores.

**Deeper:** [38-rest-api-development/01-rest-design.md](../38-rest-api-development/01-rest-design.md) · [45-messaging-and-event-driven-systems/03-delivery-guarantees-and-outbox.md](../45-messaging-and-event-driven-systems/03-delivery-guarantees-and-outbox.md)

---

## Build and modules

### Classpath vs module path (JPMS)

Classic Java uses the classpath (JARs/directories as a flat world of types). The Java Platform Module System adds strong encapsulation and explicit `requires`/`exports` on the module path. Most applications still lean heavily on classpath tooling; modules matter for the JDK itself and for stronger boundaries.

**Deeper:** [29-build-tools-and-dependency-management/01-classpath-jars-and-modules.md](../29-build-tools-and-dependency-management/01-classpath-jars-and-modules.md) · [29-build-tools-and-dependency-management/04-java-platform-module-system.md](../29-build-tools-and-dependency-management/04-java-platform-module-system.md)

### Maven / Gradle

Build tools resolve dependencies, compile, test, and package. Interviews care that you understand scopes/configurations, reproducible builds, and the difference between compile and runtime classpaths—not memorizing every plugin XML attribute.

**Deeper:** [29-build-tools-and-dependency-management/02-maven.md](../29-build-tools-and-dependency-management/02-maven.md) · [29-build-tools-and-dependency-management/03-gradle.md](../29-build-tools-and-dependency-management/03-gradle.md)

---

## Quick self-check

Before an interview, ensure you can explain—without notes—the difference between:

| Pair | Core distinction |
|------|------------------|
| `synchronized` vs `volatile` | Mutual exclusion + HB vs visibility/ordering without mutual exclusion |
| Checked vs unchecked | Compile-time contract vs runtime-oriented failures |
| Platform vs virtual threads | OS-backed vs JVM-scheduled lightweight concurrency |
| Erasure vs reification | Compile-time generic info mostly erased vs runtime type arguments retained |
| Proxy self-invocation | Internal `this` call bypasses Spring AOP proxy |
| Saga vs ACID TX | Compensating multi-service flow vs single-resource atomic commit |
| False sharing vs true contention | Cache-line ping-pong on *distinct* fields vs competing for the *same* lock/data |
| Connection pool vs ORM session | Bounded live DB sockets vs persistence-context / unit-of-work identity |

**Next:** [52-interview-preparation/01-core-java-interviews.md](../52-interview-preparation/01-core-java-interviews.md)
