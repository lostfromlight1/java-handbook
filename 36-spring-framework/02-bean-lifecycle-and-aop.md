# Bean Lifecycle and AOP

> **Status:** Completed  
> **Module:** 36 — Spring Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [jdk-dynamic-proxy-demo](../../examples/backend/jdk-dynamic-proxy-demo/)

## 1. Learning Objectives

- Outline a Spring **bean lifecycle** from definition to destruction.
- Explain **Aspect-Oriented Programming (AOP)** and **cross-cutting concerns**.
- Relate Spring AOP to **JDK dynamic proxies** and **CGLIB** subclass proxies.
- Choose `@Transactional` placement with lifecycle and proxy boundaries in mind.

## 2. Prerequisites

- Ch.01 — beans, constructor injection.
- **Module 21** (reflection) — `Proxy`, `InvocationHandler`.
- **Module 25** — threads/visibility if you add async `@Async` later.

## 3. Why This Concept Exists

**Logging, security, transactions, metrics** repeat across many methods. Copy-pasting them violates DRY and mixes infrastructure with business logic. **AOP** centralizes **advice** (what to do around many join points) while **business classes** stay focused.

Spring manages **when** objects are created, initialized, and destroyed—important for opening/closing resources, caches, and **transaction boundaries**.

## 4. Conceptual Explanation

### Bean lifecycle (simplified)

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

| Hook | Typical use |
|------|-------------|
| `@PostConstruct` | Validate config, warm caches after injection |
| `InitializingBean` | Legacy callback; prefer `@PostConstruct` |
| `@PreDestroy` | Close connections, flush buffers on shutdown |

**Scopes** (framework):

| Scope | Behavior |
|-------|----------|
| `singleton` (default) | One shared instance per container |
| `prototype` | New instance per injection/get |
| `request` / `session` | Web scopes (Module 37) |

### AOP vocabulary

| Term | Meaning |
|------|---------|
| **Join point** | Point in execution (method call in Spring AOP) |
| **Pointcut** | Predicate matching join points |
| **Advice** | Before, after, around, after-throwing |
| **Aspect** | Pointcut + advice bundle |

Spring AOP is **proxy-based**: callers hold a reference to a **proxy**, not the raw target. The proxy runs advice, then (for around advice) calls the target.

## 5. Step-by-Step Examples

### 5.1 JDK dynamic proxy (runnable AOP analog)

Demo: [jdk-dynamic-proxy-demo](../../examples/backend/jdk-dynamic-proxy-demo/).

`Proxy.newProxyInstance` implements `Greeter` and delegates to `SimpleGreeter` after printing `before=greet`.

**Verified output:**

```text
before=greet
result=Hello, Ada
proxyAop=ok
```

This mirrors Spring’s **JDK proxy** path when the target **implements interfaces**.

### 5.2 Spring `@Aspect` (conceptual)

```java
@Aspect
@Component
public class TimingAspect {
    @Around("execution(* com.example..*Service.*(..))")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.println(pjp.getSignature() + " took " + ms + "ms");
        }
    }
}
```

`pjp.proceed()` calls the **real** method on the target (inside the proxy stack).

### 5.3 `@Transactional` is AOP (conceptual)

Spring wraps the bean in a proxy that begins/commits/rolls back a transaction **around** annotated methods. **Self-invocation** (`this.save()` inside the same class) **bypasses** the proxy—common production bug. Correct pattern: call through another bean or refactor.

## 6. Internal Behavior (framework)

| Target type | Default proxy (Spring Boot 2.x+) |
|-------------|----------------------------------|
| Implements interface(s) | JDK dynamic proxy |
| No interface | CGLIB subclass (cannot proxy `final` classes/methods) |

**Limitation:** only **public** methods on Spring beans are intercepted for typical annotation-driven AOP. **Internal calls** are not advised unless you use AspectJ weaving (compile-time/load-time), a separate deployment mode.

## 7. Real-World Use Cases

- Centralized **audit logging** for service layer entry/exit.
- **Retry** and **circuit breaker** advice (often via libraries like Resilience4j integrated with Spring).
- **Read-only vs read-write** transaction routing.

## 8. Edge Cases

- **`final` methods** — not overridden by CGLIB; advice may not apply.
- **Async `@Async`** — runs on another thread; transaction context does not propagate unless you configure it.
- **Prototype bean** injected into singleton — Spring injects a **single** prototype instance unless you use `ObjectProvider` or `@Scope(PROTOTYPE)` with scoped proxy.

## 9. Common Mistakes

### Incorrect — expecting `@Transactional` on private method

Proxies call **public** methods on the wrapper; private methods are not advised.

### Incorrect — logging inside every repository method

Use one aspect or filter; keep domain code clean.

## 10. Design Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| Proxy AOP (default) | No bytecode weaving in build | Self-invocation traps |
| AspectJ weaving | Stronger join points | Build/deploy complexity |
| Manual decorator | Explicit, no proxy surprise | Boilerplate |

## 11. Revision and Mastery

- Explain why `orderService.place()` works with `@Transactional` but an internal `this.persist()` might not.
- When does Spring choose JDK proxy vs CGLIB?

## 12. Exercises

1. Add an `afterReturning` logger to the JDK proxy handler in the demo.
2. List three cross-cutting concerns in your current project better handled with AOP than copy-paste.
3. Draw the object diagram for: Client → `Greeter` proxy → `SimpleGreeter`.

## 13. Chapter Summary

Spring beans pass through defined initialization and shutdown hooks; AOP applies advice via proxies built with JDK or CGLIB mechanisms rooted in Java’s reflection and subclassing. The proxy demo is the mental model for `@Transactional` and `@Around`.

## 14. Next Steps

[03-spring-data-mvc-security-overview.md](03-spring-data-mvc-security-overview.md)

## Review Checklist

- [x] Proxy demo verified; framework behavior labeled
- [x] Lifecycle, scopes, self-invocation covered
