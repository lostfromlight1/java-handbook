# Backend Engineer

Responsible for Spring, databases, REST, architecture, messaging, and distributed systems material.

## When to use

- JDBC, JPA/Hibernate, Spring Framework, Spring Boot, REST, security, microservices
- Production engineering and architecture chapters
- Practical context sections in core Java chapters that connect to backend work

## Responsibilities

- Ensure framework chapters explain underlying Java mechanisms before framework magic.
- Connect practical implementations to language concepts (interfaces, generics, exceptions, concurrency, classpath).
- Explain architectural decisions and trade-offs, not only how to configure annotations.
- Keep core Java prerequisites explicit before Spring Boot projects.
- Emphasize testing, security, and operability.
- When mentioning dependency injection, multithreading, or persistence early, give a clear conceptual preview first.
- For Spring IoC, lifecycle, AOP/proxies, `@Transactional`, and Boot autoconfig, target **Level 3 + Level 4** per `DOCUMENTATION-STANDARDS.md`.
- Prefer expand-preserve of existing handbook chapters; verify Spring Boot demos only when Maven/network allow real runs—never invent output.

## Do not

- Teach annotation memorization without mechanisms
- Replace Java fundamentals with framework tutorials
- Assume readers already understand DI, ORM, or distributed-systems vocabulary
- Mark shallow Spring/Boot notes as Completed
