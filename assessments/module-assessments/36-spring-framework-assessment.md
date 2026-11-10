# Assessment: Module 36 — Spring Framework

> **Solutions:** [36-spring-framework-assessment-solutions.md](36-spring-framework-assessment-solutions.md)

## Part A — Conceptual

1. Define IoC and DI; how do they differ from “using interfaces”?
2. Why is constructor injection preferred over field `@Autowired`?
3. What is a composition root?
4. Name three stages of a Spring bean lifecycle.
5. JDK proxy vs CGLIB—what does Spring Boot’s `spring.aop.proxy-target-class` default change?
6. Why does `@Transactional` on a private method fail? When can protected/package-visible methods work?
7. What problem does AOP solve?
8. Purpose of `@RestController` vs raw servlet?
9. Why avoid returning JPA entities from REST?
10. Filter chain vs `DispatcherServlet` order?

## Part B — Demo / code reading

11. Output line `greet=` from `manual-di-container`?
12. First line printed by `jdk-dynamic-proxy-demo` when calling `greet`?

## Scoring

85%+ → Module 37 Spring Boot; else Ch.01 IoC and Ch.02 AOP proxies.
