# Solutions: Module 37 — Spring Boot

1. `@Configuration`, `@EnableAutoConfiguration`, `@ComponentScan`.
2. Brings Spring MVC, Jackson, embedded Tomcat (web stack defaults).
3. Skips autoconfig bean if user already defined one of that type.
4. `--spring.profiles.active=prod` or env `SPRING_PROFILES_ACTIVE=prod`.
5. Typed, validated, structured binding for prefix trees; `@Value` fine for single keys.
6. Resource successfully created; often includes `Location` header.
7. Runs Jakarta Bean Validation on the DTO after JSON binding.
8. Expose health (and maybe info) publicly; lock down env/metrics via security and exposure config.
9. `@WebMvcTest` loads MVC slice + `@MockBean`; full `@SpringBootTest` loads entire context (slower).
10. `server.shutdown=graceful` with lifecycle timeout for in-flight requests.

11. `201`
12. Boot only automates Spring/Jakarta/servlet stacks—you still need Java, HTTP, JPA, and security semantics underneath.
