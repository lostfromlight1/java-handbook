# IoC and Dependency Injection

> **Status:** Completed  
> **Module:** 36 — Spring Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [manual-di-container](../../examples/backend/manual-di-container/)

## 1. Learning Objectives

- Define **Inversion of Control (IoC)** and **Dependency Injection (DI)** and why enterprise Java uses them.
- Wire collaborators via **constructor injection** in plain Java before trusting `@Autowired`.
- Relate Spring’s **ApplicationContext** to interfaces, reflection, and object graphs you could build manually.
- Spot **tight coupling** and **service locator** anti-patterns.

## 2. Prerequisites

- **Modules 05–07** — interfaces, polymorphism, composition over inheritance.
- **Module 06 / reflection** — `Class`, constructors, `newInstance` patterns (Module 21+ if you studied reflection).
- **Module 35** — JPA entities; Spring Data builds on the same persistence ideas later in this module.

## 3. Why This Concept Exists

Large backends contain hundreds of types: repositories, validators, mail senders, payment clients. If each class calls `new ConcreteDependency()` internally:

- You cannot substitute a test double without subclass hacks.
- Configuration is scattered (`new` appears in constructors everywhere).
- Swapping implementations (SMTP vs API mail) requires code edits, not wiring.

**Inversion of Control** means the **framework or composition root** decides which concrete types exist and how they connect, instead of each class hunting its own dependencies. **Dependency Injection** is the common IoC style: dependencies are **passed in** (usually via constructor) rather than created inside the consumer.

Spring Framework popularized this in Java. Under the hood it is still **Java objects, interfaces, and reflection**—not magic reserved for annotations.

## 4. Conceptual Explanation

| Term | Meaning |
|------|---------|
| **Dependency** | Another object a class needs to do its job (collaborator). |
| **Injection** | Supplying that collaborator from outside. |
| **Composition root** | One place that builds the object graph (Spring `ApplicationContext`, or your `main`, or a demo container). |
| **Bean** | Spring name for an object managed by the container (singleton by default in Spring; our demo creates new instances per `get` unless you extend it). |

**Injection styles:**

| Style | When to prefer |
|-------|----------------|
| **Constructor** | Required dependencies; immutable fields; easiest testing (industry default). |
| **Setter** | Optional dependencies; legacy frameworks. |
| **Field** | `@Autowired` on fields works but hides dependencies and complicates tests—avoid in new code. |

**Spring wiring (conceptual—requires Spring on classpath):**

```java
@Configuration
public class AppConfig {
    @Bean
    public GreetingService greetingService() {
        return new EnglishGreetingService();
    }

    @Bean
    public GreetingController greetingController(GreetingService service) {
        return new GreetingController(service); // constructor injection
    }
}
```

`@Configuration` and `@Bean` tell Spring which `@Bean` methods contribute objects to the context. Method parameters on `@Bean` methods are themselves resolved from the context—constructor injection at the factory level.

## 5. Step-by-Step Examples

### 5.1 Manual composition (no container)

```java
GreetingService service = new EnglishGreetingService();
GreetingController controller = new GreetingController(service);
```

You **explicitly** choose implementations—fine for tiny apps; scales poorly when graphs are deep.

### 5.2 Minimal container (runnable)

Demo: [manual-di-container](../../examples/backend/manual-di-container/).

The `MiniContainer` maps `GreetingService` → `EnglishGreetingService`, then reflects on `GreetingController`’s constructor to inject `GreetingService`:

**Verified output:**

```text
greet=Hello, Lin
diContainer=ok
```

Important lines:

- `container.bind(GreetingService.class, EnglishGreetingService.class)` — registration (like Spring’s type-to-impl binding).
- `container.get(GreetingController.class)` — resolves constructor parameters recursively (like Spring autowiring).

### 5.3 Spring component scanning (conceptual)

```java
@Service
public class EnglishGreetingService implements GreetingService { ... }

@RestController
public class GreetingController {
    private final GreetingService greetingService;
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
}
```

`@Component`, `@Service`, `@Repository`, `@Controller` are **stereotypes**—marker annotations so scanning finds classes and registers them as beans. Spring still ends up calling constructors with resolved dependencies.

## 6. Internal Behavior (Spring, labeled framework)

On context refresh, Spring:

1. Parses configuration (`@Configuration`, XML, or component scan).
2. Registers **bean definitions** (name, type, scope, factory).
3. Instantiates beans in **dependency order** (cycles require special handling).
4. Applies **BeanPostProcessors** (preview for Ch.02—wrapping/proxies).

This is **framework behavior**, not a JLS guarantee. Your pure-Java container teaches the same **graph resolution** idea without the full lifecycle.

## 7. Real-World Use Cases

- Testable services: pass `Clock`, `Random`, or mock repositories via constructors.
- Profile-specific beans (`@Profile("prod")`) in Module 37.
- Library boundaries: depend on interfaces in domain modules; bind JDBC or HTTP implementations in infrastructure.

## 8. Edge Cases and Limitations

- **Circular dependencies** — two beans each need the other; constructor injection fails at startup; fix by redesign or (reluctantly) setter/lazy injection.
- **Too many constructor parameters** — signal to split the class (violates SRP; Module 41).
- **Static singletons** — hide dependencies; harder than Spring to test.

## 9. Common Mistakes

### Incorrect — service locator inside business code

```java
public class OrderService {
    public void place(Order o) {
        PaymentClient pay = ServiceRegistry.get(PaymentClient.class); // hidden global
        pay.charge(o);
    }
}
```

### Corrected — constructor injection

```java
public class OrderService {
    private final PaymentClient paymentClient;
    public OrderService(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }
}
```

### Incorrect — `new` for every dependency in `@Service`

Duplicates configuration and prevents interface substitution.

### Corrected — single binding in configuration or constructor injection from Spring.

## 10. Design Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| Manual `new` in `main` | Simple, explicit | Does not scale |
| DIY container (demo) | Teaches mechanics | Not production-grade |
| Spring / Micronaut / etc. | Mature lifecycle, ecosystem | Learning curve, startup cost |

## 11. Security Considerations

DI does not fix security. **Do not** inject request-scoped user identity into singleton beans without scoped proxies—risk of **cross-request data leakage**. Use explicit request/session scope in Spring Web (Module 37–39).

## 12. Revision and Mastery

**Gaps to close:**

- Confusing **DI** with **factory pattern** only—DI is who **calls** the factory (the container at the root).
- Assuming `@Autowired` is required—**constructor injection + final fields** works without field injection when there is a single constructor (Spring 4.3+).

**You should answer:**

- Why is constructor injection preferred over field injection?
- What problem does IoC solve that interfaces alone do not?

## 13. Exercises

1. Extend `MiniContainer` to cache singleton instances per type.
2. Add a `GreetingService` implementation that returns Burmese/Unicode greetings; rebind without changing `GreetingController`.
3. Sketch a three-layer graph: Controller → Service → Repository interface with one `main` composition root.

## 14. Chapter Summary

IoC moves object-graph decisions to a composition root; DI passes collaborators in, especially via constructors. Spring’s context automates what `MiniContainer` demonstrates with interfaces and reflection. Understand plain Java wiring first; annotations describe that wiring.

## 15. Next Steps

[02-bean-lifecycle-and-aop.md](02-bean-lifecycle-and-aop.md)

## Review Checklist

- [x] Depth gate: IoC/DI motivations, progressive examples, mistakes, trade-offs
- [x] Verified `manual-di-container` on Temurin 21.0.12.1
- [x] Spring snippets labeled conceptual without Maven in this repo
