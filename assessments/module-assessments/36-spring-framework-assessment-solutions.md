# Solutions: Module 36 — Spring Framework

1. **IoC** inverts who creates/wires objects (container vs classes). **DI** supplies collaborators from outside (constructor). Interfaces alone still allow `new Concrete()` inside classes.
2. Explicit dependencies, immutability (`final`), testability without reflection, compile-time clarity.
3. Single place building the object graph (`main`, `@Configuration`, or container).
4. Definition → instantiation → dependency injection → init (`@PostConstruct`) → use → destroy (`@PreDestroy`) (accept reasonable variants).
5. Spring AOP supports both JDK (interface) and CGLIB (subclass) proxies. Spring Boot typically sets `spring.aop.proxy-target-class=true`, favoring **class-based (CGLIB)** proxies even when interfaces exist. With `proxy-target-class=false`, interface beans tend to get JDK proxies. `final` classes/methods block CGLIB subclassing of those types/methods.
6. Private methods are not join points for proxy-based Spring AOP/`@Transactional`. Prefer public service methods. With **class-based proxies** (Boot’s usual default), Spring Framework 6 can advise **protected** and **package-visible** `@Transactional` methods in proxy mode; private still fails.
7. Centralizes cross-cutting concerns (logging, transactions, security) without duplicating in every method.
8. Maps HTTP to Java methods with automatic message conversion (JSON); servlet is lower-level request/response API.
9. Lazy loading, schema coupling, leaking persistence model; use DTOs.
10. Security and other servlet filters run before `DispatcherServlet` reaches controllers.

11. `greet=Hello, Lin`
12. `before=greet`
