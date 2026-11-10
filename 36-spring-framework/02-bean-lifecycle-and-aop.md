# Bean Lifecycle and AOP

> **Status:** Completed  
> **Module:** 36 — Spring Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Deep-dive  
> **Depth target:** Level 3 + Level 4 on lifecycle stages, proxies, and `@Transactional`  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [jdk-dynamic-proxy-demo](../../examples/backend/jdk-dynamic-proxy-demo/)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Define **bean lifecycle** and name each major stage from **definition registration** through **destruction**.
- Explain **constructor dependency resolution**, **property population**, and where **initialization callbacks** fit in the pipeline.
- Describe why **`BeanPostProcessor`** exists and what runs **before** vs **after** initialization.
- Compare **`@PostConstruct`**, **`InitializingBean`**, and **`init-method`** (and the shutdown counterparts).
- State when Spring may expose a bean through a **proxy** instead of the raw target instance.
- Contrast **`singleton`** and **`prototype`** lifecycle and shutdown behavior.
- Explain **`request`** and **`session`** scopes in servlet-based web apps without opening another module.
- Teach **cross-cutting concerns** and why plain OOP spreads duplicated infrastructure code.
- Use AOP vocabulary: **aspect**, **advice**, **pointcut**, **join point**, and **advice types**.
- Contrast **Spring AOP** (runtime proxies) with **AspectJ** (full weaving).
- Walk through **JDK dynamic proxy** internals and relate them to the handbook demo.
- Explain **CGLIB** subclass proxies, **`final`** limitations, and **self-invocation** bypass.
- Configure and reason about **`@Transactional`**: boundaries, **propagation**, **isolation**, and **rollback rules**.
- Choose proxy strategies accurately for **Spring Framework 6** / **Spring Boot 3** (JDK vs CGLIB defaults).

## 2. Prerequisites

- **Chapter 01 (IoC/DI)** — beans, constructor injection, `ApplicationContext`. Refresh: a **bean** is a Spring-managed object with a **name/id**, **scope**, and **lifecycle callbacks** managed by the container instead of by scattered `new` calls.
- **Module 21 (reflection)** — `java.lang.reflect.Proxy`, `InvocationHandler`, and `Method.invoke`. Spring’s JDK proxy path uses the same JDK APIs you can run without Spring.
- **Module 25 (concurrency)** — threads and visibility if you combine `@Async` with transactions; transaction context is thread-local by default.
- **Servlet basics (light)** — HTTP request and HTTP session as time-bounded scopes for web beans (expanded in this chapter).

You do not need AspectJ installed to learn Spring AOP; proxy-based AOP is enough for most Boot applications.

## 3. Why This Concept Exists

Enterprise backends repeat the same **infrastructure** around business logic:

- Start/commit/rollback **database transactions**
- **Log** entry, exit, and failures
- Enforce **security** (who may call this method)
- Record **metrics** and **tracing** spans
- Apply **retries**, **timeouts**, or **circuit breakers**

Copy-pasting that logic into every service method violates **DRY**, hides business rules under noise, and makes behavior inconsistent (one team logs JSON, another logs plain text). **Aspect-Oriented Programming (AOP)** extracts **cross-cutting** behavior into **aspects** applied at well-defined **join points** (typically method executions).

Separately, Spring must know **when** to construct beans, **how** to wire dependencies in a safe order, **when** initialization is complete, and **how** to release resources on shutdown. Misunderstanding lifecycle hooks leads to **null collaborators during `@PostConstruct`**, **leaked connections**, or **transactions that never commit**.

This chapter connects **lifecycle** (object birth and death) with **proxies** (how Spring wraps beans so advice can run **around** method calls). That connection is the mental model behind **`@Transactional`**, **`@Cacheable`**, and many other annotations.

## 4. Conceptual Explanation

### 4.1 What “bean lifecycle” means

A **bean lifecycle** is the ordered sequence of container actions from the moment Spring learns that a type should be managed until the instance is destroyed or abandoned.

| Phase | Plain-language meaning |
|-------|------------------------|
| **Definition** | Spring records *what* to create (class, scope, constructor args, property values, init/destroy metadata). |
| **Instantiation** | Spring calls a constructor (or factory) to allocate the raw object. |
| **Dependency injection** | Constructor args and/or setter/field values are applied so collaborators exist. |
| **Aware callbacks** | Optional hooks if the bean implements `*Aware` interfaces (for example `BeanNameAware`). |
| **Post-processing before init** | Every `BeanPostProcessor` may inspect or wrap the instance **before** init callbacks. |
| **Initialization** | `@PostConstruct`, `InitializingBean`, or custom `init-method` run business setup. |
| **Post-processing after init** | `BeanPostProcessor` again—this is where many **AOP proxies** are created. |
| **In use** | Application code calls the bean (often through a **proxy** reference). |
| **Shutdown** | `@PreDestroy`, `DisposableBean`, or `destroy-method` release resources. |

Lifecycle is not only academic. A repository bean might open a pool in `@PostConstruct` and close it in `@PreDestroy`. A cache might warm on init and flush on shutdown. Transaction managers bind to the lifecycle of the surrounding container.

### 4.2 Cross-cutting concerns and why OOP alone struggles

**Object-Oriented Programming** organizes code by **nouns** (classes). Business classes should express **domain rules**. Infrastructure that applies to **many** unrelated classes is a **cross-cutting concern**: it “cuts across” the inheritance and package structure.

Inheritance can centralize behavior (`abstract class LoggingService`), but:

- Java allows **single inheritance**; you cannot extend both `LoggingSupport` and your domain base.
- Subclassing for logging forces a static IS-A relationship unrelated to the domain.
- You still duplicate **call sites** if subclasses do not share a common base.

**Composition** (delegate to a `Logger`) is better than inheritance for logging, yet **every** method still needs boilerplate `logger.info(...)` unless you automate the call.

**AOP** automates “run this before/after many methods matching a predicate” without editing each method body. Spring implements that with **proxies** at runtime (by default), not by rewriting your `.java` sources.

### 4.3 Simplified lifecycle diagram (and its limits)

The diagram below is a **study map**, not the full Spring `AbstractAutowireCapableBeanFactory` pipeline.

```mermaid
flowchart LR
  A[Bean definition] --> B[Instantiate]
  B --> C[Populate dependencies]
  C --> D[BeanPostProcessor before init]
  D --> E[@PostConstruct / init method]
  E --> F[BeanPostProcessor after init]
  F --> G[Bean ready]
  G --> H[@PreDestroy / shutdown]
```

**Explicitly omitted from this diagram** (still part of real containers):

| Omitted stage | Why it matters |
|---------------|----------------|
| **BeanFactoryPostProcessor** / `BeanDefinitionRegistryPostProcessor` | Runs **before** bean creation; can alter **definitions** (for example enable `@Configuration` processing). |
| **Merged bean definition** resolution | Parent definitions, overridden scopes, and factory-bean metadata are resolved before instantiation. |
| **Constructor dependency resolution** as its own step | Distinct from generic “populate”; cycles and `ObjectFactory`/`Provider` injection are handled here. |
| **`Aware` interface callbacks** | `BeanNameAware`, `BeanFactoryAware`, `ApplicationContextAware`, `EnvironmentAware`, etc. |
| **`InstantiationAwareBeanPostProcessor`** | Can short-circuit instantiation or change which constructor is used (`AutowiredAnnotationBeanPostProcessor`). |
| **`@Lookup` / method injection** | Replaces methods on `@Bean` classes at runtime. |
| **Early exposure for circular references** | Singletons may be exposed through a **singleton factory** before fully initialized (singleton-only, controversial—prefer constructor injection without cycles). |
| **DisposableBeanAdapter** ordering | Multiple destroy callbacks are composed with defined precedence. |
| **Context shutdown ordering** and **jakarta/javax lifecycle** on web servers | Listeners and child contexts shut down in phases. |
| **Prototype destruction** | Spring **does not** call destroy callbacks for prototype beans once handed to the client (see §6.10). |
| **Scoped proxies** (`request`, `session`) | May create an extra proxy layer at injection time. |

When debugging “why didn’t my `@PostConstruct` run?”, you often discover an omitted stage: a failed constructor, a rejected definition, or a `BeanPostProcessor` returning `null`.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Bean definition** | Metadata: class name, scope, lazy flag, constructor argument metadata, property values, init/destroy method names. |
| **BeanPostProcessor (BPP)** | Callback interface; container invokes `postProcessBeforeInitialization` / `postProcessAfterInitialization` for **every** bean (powerful; easy to misuse). |
| **Join point** | A point in program execution. In Spring AOP: **method execution** on a Spring bean (not field access by default). |
| **Pointcut** | Predicate selecting which join points receive advice (`execution(...)` expressions). |
| **Advice** | Action at a join point: before, after returning, after throwing, around. |
| **Aspect** | Modular unit combining pointcuts and advice (`@Aspect` class). |
| **Weaving** | Linking aspects into executable code (Spring: proxy at runtime; AspectJ: compile-time/load-time). |
| **Target** | Your plain object with business logic. |
| **Proxy** | Wrapper implementing the same interfaces (JDK) or subclassing the target (CGLIB) that runs interceptors/advice first. |
| **Self-invocation** | A method on the target calls another method on **`this`**; calls do not pass through the proxy shell. |

## 6. Mechanism and Execution Flow — Bean Lifecycle by Stage

### 6.1 Bean definition registration

Before any object exists, Spring stores a **`BeanDefinition`**. Registration paths include:

- **Component scanning** — `@Component`, `@Service`, stereotype meta-annotations; classpath scanning finds classes and registers definitions.
- **`@Bean` methods** — `@Configuration` classes declare factory methods; return values become bean definitions.
- **Programmatic registration** — `BeanDefinitionRegistry.registerBeanDefinition(...)`.
- **XML / Groovy / other legacy wiring** — still supported in plain Spring; Boot apps rarely use XML.

A definition records **scope** (`singleton`, `prototype`, web scopes), **lazy** init, **primary**/`@Qualifier` metadata, and init/destroy method names. `BeanFactoryPostProcessor` beans may **modify definitions** before instantiation (for example `@PropertySource` placeholder resolution on `@Value` fields is prepared via post-processors and `Environment`).

**Design note:** Changing a definition affects **all** instances of that bean name in the container; changing a single instance happens later via `BeanPostProcessor`.

### 6.2 Instantiation

**Instantiation** allocates memory and runs a constructor (or factory). Spring picks:

- **Autowire constructor** — preferred in modern Spring; dependencies become constructor arguments.
- **Factory methods** — `@Bean` or `FactoryBean` implementations.
- **Static factory methods** — less common in Boot apps.

`InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation` may return a **replacement** proxy or alternative object, skipping the default constructor path (rare in application code; used by framework internals).

**`final` classes:** CGLIB subclass proxies cannot extend `final` classes. JDK proxies do not subclass the target at all—they implement **interfaces** and delegate.

### 6.3 Constructor dependency resolution

For constructor injection, Spring resolves each parameter:

1. Find candidate beans by type (`@Autowired` / `@Qualifier` / `@Primary`).
2. Handle **optional** dependencies (`Optional`, `ObjectProvider`, `@Autowired(required = false)`).
3. Detect **ambiguous** or **unsatisfied** dependencies at startup (fail-fast in a healthy Boot app).

**Circular dependencies:** Two singletons that require each other in **constructors** cannot be satisfied (Spring throws `BeanCurrentlyInCreationException`). Legacy **setter** cycles could be resolved via early singleton exposure; **constructor injection avoids the problem** by forcing an acyclic graph. If you hit a cycle, refactor (extract a third bean, use `ObjectProvider` lazily, or rethink boundaries)—do not rely on early exposure.

### 6.4 Property population / dependency injection

After the object exists, Spring applies:

- **Field and setter injection** (discouraged for required dependencies but still seen in legacy code).
- **`@Value`** property injection from `Environment`.
- **`@Resource` / `@Inject`** (JSR-330) when enabled.

Order relative to awareness: population generally completes **before** `Aware` callbacks and **before** initialization callbacks, so `@PostConstruct` sees injected fields.

**Common bug:** Using a collaborator inside a **constructor** that itself needs `@Value` fields populated on **the same** bean—constructor runs before field injection on that instance. Put `@Value` consumption in `@PostConstruct` or use constructor injection of a configuration object.

### 6.5 `BeanFactoryAware` and other `Aware` interfaces

**Aware** interfaces let beans receive infrastructure references without `@Autowired` fields everywhere:

| Interface | Injected reference |
|-----------|-------------------|
| `BeanNameAware` | Bean name in the container |
| `BeanClassLoaderAware` | ClassLoader used for the bean |
| `BeanFactoryAware` | Owning `BeanFactory` |
| `ApplicationContextAware` | Owning `ApplicationContext` |
| `EnvironmentAware` | `Environment` (profiles, properties) |
| `ResourceLoaderAware` | For loading `Resource` handles |
| `ServletContextAware` / `ServletConfigAware` | Web container handles in traditional Spring MVC apps |

Callbacks run **after** property population and **before** initialization hooks. Prefer normal injection in application code; `Aware` is mainly for framework integration or advanced library code.

### 6.6 Why `BeanPostProcessor` exists (before / after init)

`BeanPostProcessor` is Spring’s extension point for **every bean instance**. Examples in the framework itself:

- **`AutowiredAnnotationBeanPostProcessor`** — processes `@Autowired`/`@Value` (and constructor selection).
- **`CommonAnnotationBeanPostProcessor`** — processes `@PostConstruct`, `@PreDestroy`, `@Resource`.
- **`ApplicationContextAwareProcessor`** — handles `Aware` interfaces.
- **`AbstractAutoProxyCreator`** (AOP) — may wrap the bean in a **proxy** after initialization.

**`postProcessBeforeInitialization`** runs **before** `@PostConstruct` / `init-method`. Use cases: validate configuration, set computed defaults, register MBeans.

**`postProcessAfterInitialization`** runs **after** init callbacks. This stage is where Spring often returns a **proxy** instead of the raw target. Application code injecting `MyService` may receive a proxy reference even though the target class is a concrete `@Service`.

A BPP may return **`null`** to veto bean creation (rare, advanced).

### 6.7 `@PostConstruct` and init callbacks

Three initialization styles (only one pattern needed per bean; mixing is allowed with defined order):

| Mechanism | Origin | Typical use |
|-----------|--------|-------------|
| **`@PostConstruct`** | Jakarta Common Annotations (`jakarta.annotation.PostConstruct`) | Preferred for application beans; runs after injection |
| **`InitializingBean.afterPropertiesSet()`** | Spring `InitializingBean` interface | Legacy; couples code to Spring API |
| **`init-method`** | XML/`@Bean(initMethod=...)` or `defaultInitMethod` | External libraries you cannot annotate |

**Order (simplified):** `Aware` → `BeanPostProcessor.before` → `@PostConstruct` / `InitializingBean` / `init-method` → `BeanPostProcessor.after` (possible proxy).

Example:

```java
@Service
public class ReportCache {
    private final ReportRepository repo;
    private Map<String, byte[]> cache;

    public ReportCache(ReportRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    void warm() {
        cache = repo.loadAllIntoMemory(); // safe: repo injected
    }
}
```

### 6.8 When and why a bean may be exposed through a proxy

Callers often hold a **proxy reference**, not the raw target, when:

- **AOP advice** applies (`@Transactional`, `@Cacheable`, custom `@Aspect`).
- **Scoped proxies** — injecting a `request`-scoped bean into a `singleton` requires a proxy that looks up the current request’s instance per call.
- **Security / metrics** aspects wrap service layer methods.

The proxy implements the **same public contract** (interfaces for JDK proxies; subclass for CGLIB). Internally it holds a reference to the **target** and an **interceptor chain**.

**Important:** If you fetch the bean from the container and call an advised **public** method, advice runs. If code inside the class calls `this.otherMethod()`, that is **self-invocation**—no advice (see §9.4).

### 6.9 Shutdown: `@PreDestroy`, `DisposableBean`, `destroy-method`

When the `ApplicationContext` closes (JVM exit, Spring Boot actuator shutdown, WAR undeploy), singleton beans are destroyed in a controlled order.

| Mechanism | Notes |
|-----------|--------|
| **`@PreDestroy`** | Preferred for resource cleanup |
| **`DisposableBean.destroy()`** | Legacy Spring callback |
| **`destroy-method`** | XML/`@Bean(destroyMethod=...)`; Boot often uses `infer` for `close`/`shutdown` on some auto-config beans |

Use shutdown hooks to **close pools**, **flush buffers**, and **cancel scheduled tasks**. Do not start long blocking work in `@PreDestroy` during undeploy.

### 6.10 Singleton vs prototype lifecycle differences

| Topic | `singleton` (default) | `prototype` |
|-------|------------------------|-------------|
| **Instances** | One per container (per bean name) | New instance per `getBean` / injection point |
| **Creation** | Eager by default (unless `@Lazy`) | Created when requested |
| **Destruction** | Container calls destroy callbacks on shutdown | **No automatic destroy** once handed to client code |
| **Injection into singleton** | Stable reference | Same prototype instance injected unless you use scoped provider patterns |
| **AOP proxy** | One proxy shared | New target (and proxy) per retrieval when advised |

**Prototype in a singleton:** If a singleton needs a fresh prototype each time, use `ObjectProvider<MyProtoBean>`, `@Lookup`, or factory pattern—not a single injected prototype field (that injects **one** instance once).

### 6.11 Request and session scopes (standalone explanation)

In servlet-based Spring MVC / Spring Web (traditional **server-side** web apps):

- **`request` scope** — one bean instance per **HTTP request**. Ideal for request-specific state (current user row, idempotency key holder). When the request ends, the instance is discarded.
- **`session` scope** — one instance per **HTTP session** (cookie-bound user session). Useful for shopping cart state; beware memory and clustering (session replication).

A **singleton** controller cannot hold a direct reference to a **request**-scoped bean—the request does not exist at controller construction time. Spring injects a **scoped proxy** that delegates to the correct request instance on each method call.

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestIdHolder {
    private String requestId = UUID.randomUUID().toString();
    public String getRequestId() { return requestId; }
}
```

`proxyMode = TARGET_CLASS` uses CGLIB-style scoped proxies (common in Boot). Interface-based scoped beans may use `INTERFACES` (JDK).

**Limitation:** Request/session scopes tie to **servlet** lifecycle. Reactive WebFlux uses different context mechanisms; do not assume servlet scopes in a pure WebFlux stack without checking docs.

### 6.12 Early singleton exposure and circular dependencies (caveat)

When two **singleton** beans historically depended on each other through **setter** injection, Spring could expose a **not-yet-fully-initialized** singleton through a factory reference so the second bean could call setters on the first. Constructor-injected graphs made that pattern unnecessary for new code.

**Today:** Prefer **acyclic constructor injection**. If you see `BeanCurrentlyInCreationException`, treat it as a **design smell**—extract a third collaborator, pass `ObjectProvider`, or split responsibilities. Do not depend on early exposure in new services; behavior can surprise teams during refactors and complicates testing.

### 6.13 `BeanFactoryPostProcessor` vs `BeanPostProcessor`

Beginners conflate these; they run at different times:

| Type | Operates on | Typical examples |
|------|-------------|------------------|
| **`BeanFactoryPostProcessor`** | **`BeanDefinition` objects** (metadata), **before** any bean instance for that context phase | Property placeholder configurers, `@Configuration` parsing support |
| **`BeanPostProcessor`** | **Bean instances** during creation | Autowiring, `@PostConstruct`, **AOP proxy** creation |

If a value in `@Value` is wrong, trace **`Environment`** and **definition** processing (`BeanFactoryPostProcessor`). If a proxy is missing, trace **`BeanPostProcessor.after`** and aspect registration.

## 7. Aspect-Oriented Programming in Spring

### 7.0 Join points Spring AOP actually supports

Spring’s **proxy-based** model advises **public method executions on Spring beans** (when pointcuts match). It does **not** (by default) advise:

- Method calls **inside** the same target (`this.`)
- **Static** methods (unless woven with AspectJ)
- **Non-Spring** objects you constructed with `new`
- Arbitrary **field read/write** (AspectJ can; Spring AOP does not)

That narrow join-point set keeps runtime predictable but explains many “my advice didn't run” reports.

### 7.1 Advice types

| Advice | Runs |
|--------|------|
| **Before** | Immediately before join point |
| **After returning** | After normal return |
| **After throwing** | After exceptional exit |
| **After (finally)** | After join point, success or failure |
| **Around** | Wraps join point; controls `proceed()` |

**Around** is the most powerful: it can skip the join point, retry, measure time, or translate exceptions.

### 7.2 Spring AOP vs AspectJ

| Dimension | Spring AOP (default) | AspectJ weaving |
|-----------|----------------------|-----------------|
| **Join points** | Method execution on Spring beans (proxy) | Methods, constructors, fields, finer points (with AspectJ language) |
| **Weaving time** | Runtime proxy creation | Compile-time (AJC) or load-time (LTW) |
| **Self-invocation** | Bypasses proxy | Can advise internal calls if woven into bytecode |
| **Build impact** | Minimal | Extra compiler/agent setup |
| **Typical use in Boot** | Transactions, security, logging, metrics | Libraries needing stronger weaving; some legacy enterprise apps |

Most teams stay on **Spring AOP** until a concrete limitation appears (internal method advice without refactor).

### 7.3 Why Spring uses proxies

Spring AOP does not rewrite your source files. At runtime it builds a **proxy** that:

1. Receives the call from the client.
2. Runs registered **interceptors** / advice chain.
3. Invokes the **target** method via reflection (JDK) or super call (CGLIB).

That matches how **`@Transactional`** opens a transaction **before** `proceed()` and commits/rolls back **after**.

```text
Client code
    │
    ▼
TransactionInterceptor (proxy)
    │  begin / commit / rollback
    ▼
Business target object
```

## 8. Mechanism and Execution Flow — JDK and CGLIB Proxies

### 8.1 JDK dynamic proxies (internals)

When the proxied type **implements one or more interfaces**, Spring **may** use **`java.lang.reflect.Proxy`**:

- A generated class implements the **same interfaces** as the target (not the concrete class).
- Calls hit `InvocationHandler.invoke(proxy, method, args)`.
- The handler delegates to the **target** with `method.invoke(target, args)`.

Handbook demo (same idea without Spring): [jdk-dynamic-proxy-demo](../../examples/backend/jdk-dynamic-proxy-demo/).

```java
Greeter target = new SimpleGreeter();
Greeter proxy = (Greeter) Proxy.newProxyInstance(
        Greeter.class.getClassLoader(),
        new Class<?>[] { Greeter.class },
        new LoggingInvocationHandler(target));
```

**Verified output (Temurin JDK 21.0.12.1):**

```text
before=greet
result=Hello, Ada
proxyAop=ok
```

**Line-by-line:**

- `Proxy.newProxyInstance` — JDK generates a proxy class at runtime implementing `Greeter`.
- `LoggingInvocationHandler.invoke` — prints `before=<method>` then forwards to `SimpleGreeter`.
- The client holds type `Greeter` (interface), not `SimpleGreeter` (concrete)—same pattern as injecting an interface in Spring.

**JDK limits:** Only **interface** methods are part of the proxy contract. Methods not on the interface are invisible to callers typed as the interface. `equals`/`hashCode` on proxies have special behavior—do not rely on proxy identity for domain logic.

### 8.2 CGLIB subclass proxies

When proxying a **concrete class** without a suitable interface—or when configured to target class—Spring uses **CGLIB** (bundled as `spring-core`’s repackaged cglib) to generate a **subclass** of the target:

- Overrides **non-final** methods to intercept calls.
- **`final` classes or methods** cannot be overridden → advice may not apply.
- Requires a **default constructor** (or appropriate construction strategy) on the target for subclassing patterns.

Callers may inject the concrete class type (`OrderService` not only `IOrderService`).

### 8.3 JDK vs CGLIB — accurate defaults (Spring Framework 6 / Spring Boot 3)

**Do not conflate “implements interfaces ⇒ JDK” with Spring Boot defaults.**

| Layer | Behavior |
|-------|----------|
| **Spring AOP capability** | Supports **both** JDK interface proxies and CGLIB class-based proxies. |
| **`@EnableAspectJAutoProxy(proxyTargetClass = false)`** (classic default in plain Spring) | **Interface** beans → JDK proxy; **no interface** → CGLIB. |
| **Spring Boot 2.x onward** | Historically sets **`spring.aop.proxy-target-class=true`** (favor **CGLIB** / class-based proxies) so concrete `@Service` classes can be proxied without declaring interfaces—especially important for **`@Transactional` on concrete classes**. |
| **Spring Boot 3.x** | Same property remains the main switch (`spring.aop.proxy-target-class`, default **`true`** in Boot’s auto-configuration). |

**Practical guidance:**

- Declaring **interfaces** for core services is still good design and enables JDK proxies when `proxy-target-class=false`.
- With Boot’s default **`true`**, expect **CGLIB** on many `@Service` beans even if interfaces exist.
- Set `spring.aop.proxy-target-class=false` only when you understand the interface-proxy requirement (for example strict interface-only injection patterns).

**Visibility and `@Transactional` (Spring Framework 6):**

- **JDK proxies:** Only methods declared on the **proxy interface** are intercepted (effectively **public** API of the interface).
- **Class-based (CGLIB) proxies:** Since Spring Framework 5.2, **`@Transactional` on `protected` or package-visible methods** can work on the proxied class when using class-based proxies and **proxy mode** (not `MODE_ASPECTJ`). **`private` methods are not advised** via proxy-based AOP—keep transaction boundaries on **public** service methods for clarity and predictability.

## 9. Method Interception Flow and Pitfalls

### 9.1 Typical call stack (around advice)

```text
1. Client invokes proxy.someMethod(args)
2. Interceptor chain (ordered)
3. TransactionInterceptor / custom AspectJAdvice
4. MethodInvocation.proceed()
5. Target.someMethod(args)  (actual business logic)
6. Interceptors run after logic (commit, log, metrics)
7. Return value propagated to client
```

Ordering matters when multiple aspects apply (`@Order`, `Ordered` interface).

### 9.2 Self-invocation bypass

```java
@Service
public class OrderService {
    @Transactional
    public void place(Order order) {
        save(order); // calls this.save — NOT through proxy
    }

    @Transactional
    public void save(Order order) { /* ... */ }
}
```

`place` runs inside a transaction. `save`’s `@Transactional` attributes are **ignored** on the internal call because `this.save` is a direct call on the target, not on the proxy.

**Correct patterns:**

- Move `save` to another Spring bean and call the injected collaborator.
- Refactor so the public entry method owns the transaction boundary you need.
- Use **`AspectJ` weaving** (heavier) only when proxy limits are unacceptable.
- Avoid “clever” `AopContext.currentProxy()` except as a last resort (couples code to Spring AOP).

### 9.3 Proxy limitations and production problems

| Problem | Symptom |
|---------|---------|
| **Self-invocation** | Transaction or cache annotation “does nothing” on internal calls |
| **`final` methods** | Advice never runs on those methods (CGLIB) |
| **Non-public methods expected to be advised** | Works only in narrow class-proxy cases; **public** service API is the reliable boundary |
| **Calling `this` in constructors** | Proxy may not exist yet; `@Transactional` in constructor is invalid pattern |
| **Exposing concrete type with JDK proxy** | Injection as concrete class fails (`ClassCastException`)—inject the interface |
| **Testing without Spring** | Unit tests call plain objects—no advice unless `@SpringBootTest` / aspect test setup |
| **Multiple class loaders** | CGLIB/proxy generation issues in complex EE deployments |
| **Performance** | Proxy overhead is usually small vs I/O; huge interceptor chains add cost |

## 10. `@Transactional` — Progressive Explanation

`@Transactional` is **declarative transaction management** implemented by Spring’s **`TransactionInterceptor`** (AOP). It binds a **`PlatformTransactionManager`** (often JDBC or JPA) to method boundaries.

### 10.1 Begin, commit, rollback

On an advised method entry (simplified):

1. **Begin** (or join existing) transaction according to **propagation**.
2. Invoke business method.
3. On **normal return** → **commit** (if this method opened the transaction and rollback-only was not set).
4. On **unchecked exception** (by default) → **rollback**.
5. On **checked exception** → commit by default unless configured to roll back.

```java
@Service
public class TransferService {
    private final AccountRepository accounts;

    public TransferService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    @Transactional
    public void transfer(long fromId, long toId, BigDecimal amount) {
        accounts.debit(fromId, amount);
        accounts.credit(toId, amount);
        // both share one transaction; failure rolls back both
    }
}
```

### 10.2 Propagation

**Propagation** defines how a method participates in an existing transaction:

| Propagation | Meaning (practical) |
|-------------|---------------------|
| **`REQUIRED`** (default) | Join current transaction or start new |
| **`REQUIRES_NEW`** | Suspend current; always new transaction (audit log that must commit even if outer rolls back) |
| **`NESTED`** | JDBC savepoint-style nested transaction (if supported) |
| **`MANDATORY`** | Must run inside existing transaction; else exception |
| **`SUPPORTS`** | Use transaction if present; else non-transactional |
| **`NOT_SUPPORTED`** | Suspend transaction; run non-transactionally |
| **`NEVER`** | Must not run in a transaction |

Misuse of `REQUIRES_NEW` causes **connection pool pressure** (two transactions, two connections). Use deliberately.

### 10.3 Isolation

**Isolation** maps to JDBC isolation levels (`READ_COMMITTED`, `REPEATABLE_READ`, etc.). Higher isolation reduces anomalies but increases locking/contention. Default usually follows the database driver (`DEFAULT`).

### 10.4 Rollback rules

By default, rollback occurs on **unchecked** exceptions (`RuntimeException`, `Error`). **Checked** exceptions commit unless you specify:

```java
@Transactional(rollbackFor = { InsufficientFundsException.class })
public void transfer(...) throws InsufficientFundsException { ... }
```

`noRollbackFor` excludes types from rollback. Align rules with business meaning, not only exception superclass.

### 10.5 Read-only optimization

`@Transactional(readOnly = true)` hints ORM/JDBC to avoid unnecessary flush/write operations—valuable for query-heavy service methods. Not a security boundary; still enforce authorization separately.

### 10.6 Where to place `@Transactional`

- **Service layer** (application services) — orchestrates repositories and domain rules.
- Avoid on **controllers** (web layer concerns).
- Avoid on **repositories** unless the team standardizes fine-grained repository transactions—usually one boundary at the service method that represents a **use case**.

Combine with **self-invocation** rules: the **public** service method that represents the use case should own the transaction.

### 10.7 `PlatformTransactionManager` and resource binding

Under the hood, `@Transactional` delegates to a **`PlatformTransactionManager`** bean (for JDBC, `DataSourceTransactionManager`; for JPA, `JpaTransactionManager`). When a transaction starts, Spring binds a **`Connection`** or **`EntityManager`** to the current thread via **`TransactionSynchronizationManager`**.

Implications for application code:

- **Repository calls inside the same `@Transactional` method** share one persistence context / connection until the method completes.
- **Lazy-loading** Hibernate associations often works inside the transaction boundary; accessing lazy collections **after** the service method returns may trigger `LazyInitializationException` unless you use fetch joins or DTO projections.
- **`@Transactional` on tests** (`@SpringBootTest` + default rollback) exercises the same proxy and manager stack—useful for integration tests, not a substitute for plain unit tests of domain logic.

### 10.8 Interaction with `@Async` and thread boundaries

`@Async` runs work on a **thread pool**. The transaction bound to the HTTP thread **does not** automatically hop to the async thread. If async work must be transactional, start a **new** transactional boundary **inside** the async method (with clear propagation) or redesign so async work is non-transactional side effects (events) after commit.

## 11. Step-by-Step Examples

### 11.1 JDK dynamic proxy (runnable AOP analog)

See [jdk-dynamic-proxy-demo](../../examples/backend/jdk-dynamic-proxy-demo/). Compile and run:

```bash
javac -d out -sourcepath src/main/java src/main/java/com/handbook/proxy/*.java
java -cp out com.handbook.proxy.JdkProxyDemo
```

Map each demo piece to Spring:

| Demo piece | Spring equivalent |
|------------|-------------------|
| `Greeter` interface | Service interface |
| `SimpleGreeter` target | Your `@Service` implementation |
| `LoggingInvocationHandler` | `MethodInterceptor` / advice |
| `Proxy.newProxyInstance` | `AbstractAutoProxyCreator` |

### 11.2 Spring `@Aspect` (timing)

```java
@Aspect
@Component
public class TimingAspect {
    @Around("execution(* com.example..*Service.*(..))")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed(); // calls target method
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.println(pjp.getSignature() + " took " + ms + "ms");
        }
    }
}
```

`pjp.proceed()` is the hook that continues the chain—skipping it skips business logic (powerful and dangerous).

### 11.3 Lifecycle callbacks (minimal Boot-style snippet)

```java
@Component
public class StorageClient implements AutoCloseable {
    private Connection conn;

    @PostConstruct
    void open() {
        conn = openConnection(); // inject collaborators before this via constructor
    }

    @PreDestroy
    @Override
    public void close() {
        if (conn != null) {
            conn.close();
        }
    }
}
```

### 11.4 Transaction + self-invocation contrast

**Incorrect — nested transaction expectation on internal call:**

```java
@Service
public class BadOrderService {
    @Transactional
    public void place(Order o) {
        persist(o); // no proxy → @Transactional on persist ignored
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(Order o) { /* ... */ }
}
```

**Correct — separate bean:**

```java
@Service
public class GoodOrderService {
    private final OrderPersistence persistence;
    public GoodOrderService(OrderPersistence persistence) { this.persistence = persistence; }

    @Transactional
    public void place(Order o) {
        persistence.persistInNewTransaction(o); // proxy call
    }
}

@Service
public class OrderPersistence {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistInNewTransaction(Order o) { /* ... */ }
}
```

## 12. Real-World Use Cases

- **Audit logging** — single `@Around` on service layer `execution` pointcut.
- **Idempotent REST handlers** — request-scoped holder + service transaction boundary.
- **Read/write routing** — `@Transactional(readOnly=true)` on query services; write methods default read-write.
- **Metrics/tracing** — Micrometer/OTel aspects wrapping repository and service calls.
- **Retry** — Spring Retry `@Retryable` (also proxy-based) on outbound integrations.
- **Graceful shutdown** — `@PreDestroy` on clients holding HTTP connection pools.

## 13. Internal Behavior (framework)

### 13.1 How Spring registers your `@Service` (conceptual trace)

When component scanning finds `com.example.OrderService`:

1. **Scan** — `ClassPathBeanDefinitionScanner` registers a `ScannedGenericBeanDefinition` with scope `singleton` unless annotated otherwise.
2. **Parse** — `@Transactional` on the class is stored as **transaction attribute metadata** attached to the bean definition (method-level overrides merge later).
3. **Instantiate** — constructor autowiring creates the raw `OrderService`.
4. **Initialize** — `@PostConstruct` runs on the raw instance.
5. **Proxy** — if any advisor matches (`TransactionInterceptor` + pointcut), `AbstractAutoProxyCreator` wraps the instance; the bean registered in the singleton cache is often the **proxy**.
6. **Inject** — other beans receive the proxy when they depend on `OrderService`.

This trace explains why **infrastructure annotations** on the class still affect the object the container exposes after step 5.

Labeled **Spring Framework behavior** (not JLS/JVMS):

- **Proxy creation** usually in `AbstractAutoProxyCreator.postProcessAfterInitialization`.
- **Transaction attribute** resolution reads `@Transactional` metadata (class or method level; method overrides class).
- **`TransactionSynchronizationManager`** binds resources to the current thread per transaction.
- **`@EnableTransactionManagement`** registers infrastructure beans (`TransactionInterceptor`, advisors).

**HotSpot/JDK:** `Proxy` classes are generated in the **runtime metaspace**; repeated proxy creation at startup is normal during context refresh.

## 14. Edge Cases and Limitations

- **`final` methods/classes** — CGLIB cannot advise them.
- **`@Async`** — different thread; transaction context does not propagate unless explicitly configured (`TransactionSynchronizationManager` unbound on async boundary).
- **Reactive stacks** — transactional operators differ from servlet `@Transactional` on blocking services.
- **Calling transactional methods from **non-Spring** objects** — no advice; only calls through the proxy shell count.
- **Two `@Transactional` annotations on class and method** — method attributes override where defined.
- **Serialization of scoped session beans** — clustering requires `Serializable` discipline and careful state size.

## 15. Common Mistakes

### Incorrect — expecting `@Transactional` on a private method

Proxy-based AOP does not advise **private** methods. Even with CGLIB, Spring’s transaction advisor does not treat private methods as join points. Keep boundaries on **public** service methods.

### Incorrect — logging duplicated in every repository method

Use one aspect or filter; keep domain code focused on persistence rules.

### Incorrect — assuming Boot “defaults to JDK proxy when interfaces exist”

Spring Boot **generally favors class-based (CGLIB) proxies** via `spring.aop.proxy-target-class=true`. Interface presence alone does not force JDK proxies in a typical Boot 3 app.

### Incorrect — prototype bean injected once into a singleton expecting fresh instances

Inject `ObjectProvider<PrototypeBean>` or use `@Lookup`.

### Corrected — interface + concrete injection strategy

If you set `proxy-target-class=false`, inject the **interface** type for JDK proxies. If you use Boot defaults (class-based), injecting the concrete `@Service` type usually matches the CGLIB proxy.

## 16. Best Practices

- Prefer **constructor injection**; use **`@PostConstruct`** for setup that needs fully injected state.
- Place **`@Transactional`** on **public** service use-case methods; avoid nesting transaction boundaries via `this.`.
- Declare **interfaces** for core services when it improves testing and API clarity—not because Boot requires them for transactions.
- Document **rollback rules** when checked exceptions carry business failures.
- Enable **`@EnableAspectJAutoProxy`** only in plain Spring; Boot auto-configures AOP when aspects are on the classpath.
- Test transaction behavior with **`@Transactional` tests** + rollback, or integration tests hitting a real database.
- Order aspects explicitly (`@Order`) when **security** must run before **transactions** or when **metrics** must wrap entire advisor chains.

## 17. Design Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| **Proxy AOP (Spring default)** | No AJC/agent in build; works with Boot | Self-invocation; `final` limits |
| **AspectJ weaving** | Stronger join points; internal calls | Build/deploy complexity |
| **Manual decorator/wrapper** | Explicit control; easy unit tests | Boilerplate; easy to forget a layer |
| **Interceptor in web layer only** | Centralized for HTTP | Misses batch jobs, schedulers, messaging |

| Lifecycle hook | Pros | Cons |
|----------------|------|------|
| `@PostConstruct` | Standard, decoupled from Spring | Harder to enforce ordering across beans |
| `InitializingBean` | Framework-native | Ties domain to Spring |
| `init-method` | Good for third-party classes | Indirection in configuration |

## 18. Revision and Mastery

### Track A checkpoints

- Draw lifecycle stages from memory; label where DI happens vs where `@PostConstruct` runs.
- Explain in one paragraph why logging belongs in an aspect.
- Run the JDK proxy demo and point to `invoke` as the Spring analog.

### Track B — gaps and interview depth

1. Why does `orderService.place()` participate in `@Transactional` but `this.persist()` might not?
2. When does Spring choose JDK vs CGLIB, and what does Boot **`spring.aop.proxy-target-class`** change?
3. Which lifecycle callbacks does the simplified mermaid diagram omit (`BeanFactoryPostProcessor`, early circular reference exposure, prototype destroy)?
4. How does `TransactionInterceptor` decide rollback on a checked exception?
5. Why inject a **scoped proxy** for request-scoped beans into singletons?
6. What breaks if you mark a `@Service` class `final` and rely on `@Cacheable`?
7. Compare `REQUIRED` vs `REQUIRES_NEW` for an audit log that must survive outer rollback.
8. Why are **private** `@Transactional` methods unreliable on proxy mode?

### Mastery scenario

Design a transfer service with idempotency key stored **request-scoped**, transaction boundary at `transfer`, and audit log in `REQUIRES_NEW`. List which calls must go through injected collaborators to avoid self-invocation.

## 19. Knowledge Checks

1. List five stages of bean lifecycle between definition and first use.
2. Name two `Aware` interfaces and what they provide.
3. What does `BeanPostProcessor.postProcessAfterInitialization` have to do with `@Transactional`?
4. Write a pointcut expression that matches all public methods in `com.example.billing` services.
5. What output does `JdkProxyDemo` print on JDK 21 (verify locally)?

Answers: try yourself first; use §6, §8, §11, and the demo output in this chapter.

## 20. References

- Spring Framework documentation — [AOP](https://docs.spring.io/spring-framework/reference/core/aop.html), [Bean lifecycle](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html), [Transactions](https://docs.spring.io/spring-framework/reference/data-access/transaction.html)
- Spring Boot reference — `spring.aop.proxy-target-class` and transaction auto-configuration
- Jakarta Common Annotations — `@PostConstruct`, `@PreDestroy`
- Java SE — `java.lang.reflect.Proxy`, `InvocationHandler` (JDK 21)

## 21. Chapter Summary

Spring beans move from **registered definitions** through **instantiation**, **dependency injection**, optional **`Aware`** callbacks, **`BeanPostProcessor`** phases, **initialization**, and often **proxy wrapping** before the application uses them. **Shutdown** callbacks run for **singleton** beans when the context stops; **prototype** instances are your responsibility after hand-off.

**AOP** centralizes cross-cutting behavior. Spring implements it with **JDK dynamic proxies** (interface delegation) or **CGLIB** (subclass interception). Spring Boot 3 typically **favors class-based proxies** unless configured otherwise. **`@Transactional`** is advice around method execution—understand **propagation**, **isolation**, **rollback rules**, and **self-invocation** before debugging “transaction not rolling back” in production.

The [jdk-dynamic-proxy-demo](../../examples/backend/jdk-dynamic-proxy-demo/) is the minimal JDK picture of what Spring’s interceptor chain does before your business method runs.

## 22. Next Steps

[03-spring-data-mvc-security-overview.md](03-spring-data-mvc-security-overview.md)

## Review Checklist

- [x] Mandatory Depth Review Levels 1–4 applied to lifecycle, proxies, `@Transactional`
- [x] Ten-question teaching review answered for major concepts (see below)
- [x] Proxy demo verified on Temurin JDK 21.0.12.1; output recorded
- [x] Boot 3 / Framework 6 proxy defaults stated accurately (no “Boot defaults to JDK if interface” claim)
- [x] `@Transactional` visibility qualified (public boundary; protected/package with class proxies; private not advised)
- [x] Diagram omissions documented explicitly
- [x] Revision and Mastery (dual audience); exercises; mistakes; trade-offs preserved

### Claimed depth level

| Topic area | Level |
|------------|-------|
| Bean lifecycle overview | 3 |
| Lifecycle stages (registration → destroy) | **4** |
| Scopes (singleton/prototype/request/session) | 3 |
| AOP vocabulary and motivation | 3 |
| JDK / CGLIB proxy mechanisms | **4** |
| Spring AOP vs AspectJ | 3 |
| `@Transactional` (propagation, isolation, rollback) | **4** |
| Production proxy pitfalls | 3 |

### Ten-question teaching review (major concepts)

| # | Lifecycle & AOP | Answer |
|---|-----------------|--------|
| 1 | Proper definitions? | Yes — stages and AOP terms defined in prose, not one-liners |
| 2 | Purpose/motivation? | Yes — §3, §4, §7 |
| 3 | Why exists? | Yes — cross-cutting and container-managed teardown |
| 4 | Mechanism at required depth? | Yes — §6, §8–§10, §13 |
| 5 | Terms before use? | Yes — §5 before deep examples |
| 6 | Progressive examples? | Yes — §11 minimal → realistic |
| 7 | Examples explained? | Yes — demo mapping table, transaction contrast |
| 8 | Mistakes/limits? | Yes — §9, §14–§15 |
| 9 | Beginner standalone? | Yes — prerequisites refreshed; web scopes in §6.11 |
| 10 | Track B depth? | Yes — §18 Revision and Mastery |

## 23. Exercises

1. Add an **`afterReturning`** log line to `LoggingInvocationHandler` in the demo; re-run and paste output.
2. List three cross-cutting concerns in a project you know that fit AOP better than copy-paste.
3. Draw: **Client → `Greeter` proxy → `LoggingInvocationHandler` → `SimpleGreeter`**.
4. Write a `@Service` with `@PostConstruct` that fails if a required property is null; predict when Spring fails relative to injection.
5. Given `spring.aop.proxy-target-class=true`, explain whether `PaymentServiceImpl` needs an interface for `@Transactional` to work on the class.
