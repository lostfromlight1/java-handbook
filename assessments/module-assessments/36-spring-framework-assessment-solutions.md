# Solutions: Module 36 — Spring Framework

1. **IoC** inverts who creates/wires objects (container vs classes). **DI** supplies collaborators from outside (constructor). Interfaces alone still allow `new Concrete()` inside classes.
2. Explicit dependencies, immutability (`final`), testability without reflection, compile-time clarity.
3. Single place building the object graph (`main`, `@Configuration`, or container).
4. Definition → instantiation → dependency injection → init (`@PostConstruct`) → use → destroy (`@PreDestroy`) (accept reasonable variants).
5. JDK when bean implements interface(s); CGLIB subclass when no interface; `final` blocks CGLIB.
6. Spring AOP proxies public methods on the proxied bean; private methods are not intercepted.
7. Centralizes cross-cutting concerns (logging, transactions, security) without duplicating in every method.
8. Maps HTTP to Java methods with automatic message conversion (JSON); servlet is lower-level request/response API.
9. Lazy loading, schema coupling, leaking persistence model; use DTOs.
10. Security and other servlet filters run before `DispatcherServlet` reaches controllers.

11. `greet=Hello, Lin`
12. `before=greet`
