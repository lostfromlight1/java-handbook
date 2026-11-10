# Web Layer and Validation

> **Status:** Completed  
> **Module:** 37 — Spring Boot  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Depth target:** Level 3 (MVC mapping, validation pipeline, error handling, REST semantics)  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/); [spring-boot-minimal](../../examples/backend/spring-boot-minimal/) (`@WebMvcTest` MockMvc)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Map HTTP requests with **`@RestController`**, path variables, query parameters, and request bodies.
- Explain the **binding → validation → controller** order for `@RequestBody` DTOs.
- Apply **Jakarta Bean Validation** on API DTOs (`@Valid`, constraint annotations).
- Return appropriate **HTTP status codes** and structured error bodies (`ProblemDetail`).
- Relate Boot’s web stack to **REST** resource design without assuming prior Module 38 completion.
- Compare Boot behavior to the verified **JDK HttpServer** demo.

## 2. Prerequisites

- **Module 32 — HTTP:** methods, status codes, request/response bodies.
- **Module 36 — MVC overview:** `DispatcherServlet`, DTO vs entity, filter chain position.
- **Jakarta namespace (Boot 3):** validation artifacts use `jakarta.validation.*`.

## 3. Why This Concept Exists

Public APIs receive untrusted input. If invalid data reaches business logic, you leak inconsistent behavior and security holes. **Server-side validation** is mandatory even when clients validate too.

Spring MVC’s **data binding** converts JSON to Java objects; **Bean Validation** asserts constraints declaratively. Boot adds **Jackson** and validation auto-configuration on the classpath so you focus on DTOs and handlers—not parser plumbing.

## 4. Conceptual Explanation

### 4.1 REST in one paragraph (for this chapter)

**REST** (Representational State Transfer) as practiced in Java services means:

- **Resources** identified by URLs (`/api/v1/users/42`)
- **Representations** exchanged as JSON (or other media types)
- **HTTP verbs** express intent (`GET` read, `POST` create, `PUT`/`PATCH` update, `DELETE` remove)
- **Status codes** communicate outcome (200 OK, 201 Created, 400 Bad Request, 404 Not Found, 409 Conflict)

Spring Boot does not mandate REST purity (HATEOAS, etc.—Module 38), but it **supports** these conventions through MVC annotations and `ResponseEntity`.

### 4.1.1 `DispatcherServlet` request flow (servlet stack in prose)

With `spring-boot-starter-web`, Boot auto-registers **`DispatcherServlet`** mapped to `/` (by default). Every HTTP request hitting Tomcat passes through filters, then enters Spring MVC’s **front controller**:

```text
Client HTTP request
  → Tomcat connector / Coyote parses request line + headers
  → Servlet filter chain (security, tracing, cors — Module 36)
  → DispatcherServlet.doDispatch()
       1. Lookup HandlerMapping chain → HandlerExecutionChain (controller method + interceptors)
       2. If no handler → 404 (NoHandlerFoundException if configured)
       3. HandlerAdapter invokes handler (RequestMappingHandlerAdapter for @RequestMapping methods)
       4. Argument resolvers bind @PathVariable, @RequestBody, etc.
       5. Return value processed by HandlerMethodReturnValueHandler
            → @ResponseBody / ResponseEntity → HttpMessageConverter (Jackson) writes JSON
  → Response flushed to client
```

**Why this matters for APIs:** validation exceptions and binding errors are thrown **inside** the adapter, before your controller body runs. `@RestControllerAdvice` beans sit adjacent to this pipeline, converting exceptions to HTTP responses without try/catch in every method.

**Async and error handling:** if a controller returns `Callable` or `DeferredResult`, dispatch may resume on another thread; exception handling still flows through the same `HandlerExceptionResolver` chain—including `@ExceptionHandler` methods on advice beans.

**Boot-specific defaults:** `spring.mvc.throw-exception-if-no-handler-found` defaults differ by version; for SPA fallbacks some teams enable static resource handling and custom `ErrorController`—API-only services often rely on 404 JSON from advice.

### 4.2 Request mapping

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserRequest request) {
        UserDto created = userService.create(request);
        URI location = URI.create("/api/v1/users/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        return userService.list(page, size);
    }
}
```

| Annotation | Role |
|------------|------|
| `@RequestMapping` on class | Shared path prefix |
| `@PostMapping` / `@GetMapping` | HTTP method + optional path |
| `@PathVariable` | URI template variables |
| `@RequestParam` | Query string parameters |
| `@RequestBody` | Deserialize JSON body via **Jackson** `HttpMessageConverter` |

### 4.3 Binding and validation order

For `@Valid @RequestBody CreateUserRequest request`:

1. **DispatcherServlet** selects the handler method.
2. **RequestResponseBodyMethodProcessor** reads the input stream.
3. **Jackson** binds JSON fields to the DTO (types must be compatible—wrong type may yield 400 before validation).
4. **`@Valid`** triggers **Bean Validation** on the populated object.
5. If validation fails → `MethodArgumentNotValidException` (handled by advice—see below).
6. If success → your controller method runs.

**Answer for interviews:** binding generally precedes Bean Validation on the bound object; both occur **before** business logic.

### 4.4 Jakarta Bean Validation on DTOs

```java
public record CreateUserRequest(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Email String email,
    @Size(min = 8, max = 72) String password
) {}
```

| Annotation | Meaning |
|------------|---------|
| `@NotBlank` | String not null, not empty, not only whitespace |
| `@Email` | Email format (reasonable heuristic) |
| `@Size` | String/collection length bounds |
| `@Min` / `@Max` | Numeric bounds |
| `@Pattern` | Regex constraint |

Put constraints on **DTOs**, not JPA entities, to keep **API contract** separate from **database schema** (Module 36).

Method-parameter validation:

```java
@GetMapping("/search")
public List<UserDto> search(@RequestParam @NotBlank @Size(max = 50) String q) {
    return userService.search(q);
}
```

Requires **`@Validated`** on the controller class for Spring to validate `@RequestParam` (method-level validation path uses `ConstraintViolationException`).

### 4.5 Response shape and status codes

| Situation | Typical status |
|-----------|----------------|
| Successful read | `200 OK` + body |
| Created resource | `201 Created` + `Location` header + body |
| Accepted async work | `202 Accepted` |
| Validation failure | `400 Bad Request` |
| Missing resource | `404 Not Found` |
| Conflict (duplicate email) | `409 Conflict` |

`ResponseEntity` makes status explicit; return types without `ResponseEntity` default to **200** with serialized body.

## 5. Error Handling

### 5.0 Why centralized handling beats per-controller try/catch

Without advice, every handler duplicates:

```java
try {
    return userService.getById(id);
} catch (UserNotFoundException ex) {
    // map to 404 JSON
}
```

That drifts: one endpoint returns plain text errors, another returns different JSON keys. **`@RestControllerAdvice`** (stereotype combining `@ControllerAdvice` + `@ResponseBody`) registers **`@ExceptionHandler` methods** applicable **across** `@RestController` beans in chosen packages.

**Scope controls:**

| Annotation attribute | Effect |
|---------------------|--------|
| `basePackages` | Only controllers in listed packages |
| `assignableTypes` | Only specific controller classes |
| `annotations` | Only controllers marked with a marker annotation |

Place advice in `com.handbook.web.advice` and restrict `basePackages = "com.handbook.web.api"` when admin controllers need different error shapes.

**Order:** multiple advice beans apply by `@Order` or `Ordered` interface; the first matching handler wins for a given exception type—put more specific handlers in higher-priority advice.

**Interaction with validation:** `MethodArgumentNotValidException` (body) and `ConstraintViolationException` (params) require **different handlers** unless you normalize both to the same `ProblemDetail` schema.

### 5.1 `@RestControllerAdvice`

Centralizes exception-to-HTTP mapping:

```java
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");
        problem.setDetail("One or more fields are invalid");
        // Optionally map ex.getBindingResult().getFieldErrors() into properties
        return problem;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFound(UserNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
```

**`ProblemDetail`** (Spring 6 / RFC 7807 style) gives a consistent JSON error envelope—Module 38 expands API-wide error contracts.

**Field-level errors:** map `ex.getBindingResult().getFieldErrors()` into a `properties` map or custom `errors[]` list so clients can highlight invalid form fields:

```java
problem.setProperty("fieldErrors", ex.getBindingResult().getFieldErrors().stream()
    .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
    .toList());
```

**`ResponseEntityExceptionHandler`:** extend this base class in `@RestControllerAdvice` to override Spring MVC’s default handling for `HttpRequestMethodNotSupportedException`, `MissingServletRequestParameterException`, etc., keeping consistent `ProblemDetail` for framework-generated failures.

### 5.2 Security of error responses

**Incorrect — returning stack traces in JSON:** information disclosure.

**Corrected — stable problem type, safe message, correlation id in logs only.

## 6. Plain-Java Parallel (verified)

[rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/) implements routing and status without Spring:

```text
GET /users/1 status=200 body={"id":1,"name":"Lin"}
POST /users status=201
restDemo=ok
```

Boot adds:

- Content negotiation and Jackson modules
- Validation integration
- `@ExceptionHandler` dispatch

Semantics remain: **201** for create, **404** when the handler chooses not found.

### 6.1 Boot slice test (verified)

[spring-boot-minimal](../../examples/backend/spring-boot-minimal/) `HelloControllerTest`:

```text
Tests run: 1, Failures: 0, Errors: 0
GET /hello → {"message":"bootMinimal=ok"}
```

`@WebMvcTest` loads MVC infrastructure and mocks missing beans—fast controller tests (Ch.04).

### 6.2 What `MockMvc` exercises from the dispatcher flow

`MockMvc.perform(get("/hello"))` builds a **mock** `HttpServletRequest` and runs it through the **same** `DispatcherServlet` + `HandlerMapping` stack as embedded Tomcat—without sockets. That validates:

- URL pattern matching and trailing slash behavior
- Status code from `ResponseEntity` or implicit 200
- JSON serialization via configured `ObjectMapper` (including Java 21 record support)
- `@ControllerAdvice` handlers when exceptions are triggered

It does **not** validate TLS termination, HTTP/2 prioritization, or real Tomcat thread pool exhaustion—those belong in load tests or full `@SpringBootTest(webEnvironment = RANDOM_PORT)` scenarios.

## 7. Content Negotiation and JSON

### 7.1 Default JSON path

With `spring-boot-starter-web`, **`MappingJackson2HttpMessageConverter`** maps `application/json` to Java types. Custom serializers or `@JsonIgnore` live on DTOs when you must hide fields.

`produces` / `consumes` on mappings restrict media types for specialized endpoints:

```java
@GetMapping(value = "/export", produces = "text/csv")
public String exportCsv() { ... }

@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public void upload(@RequestPart MultipartFile file) { ... }
```

### 7.2 How content negotiation chooses a representation

For return types that are not raw `String`/`byte[]`, Spring MVC consults **`ContentNegotiationManager`**:

1. **`Accept` header** — client sends `Accept: application/json` vs `application/xml`.
2. **Path extension** — legacy `?format=json` or `.json` suffix if configured (`favor-path-extension` largely discouraged today).
3. **`produces` on mapping** — narrows what the handler can emit.
4. **`defaultContentType`** — usually `application/json` in Boot apps.

**Registered converters:** Jackson for JSON/XML (if `jackson-dataformat-xml` present), `StringHttpMessageConverter` for `text/plain`. If no converter can write the negotiated type, client may see **406 Not Acceptable**.

**Practical API rule:** document that clients must send `Accept: application/json` and `Content-Type: application/json` for JSON APIs; avoid relying on extension negotiation in public REST APIs—it complicates caching and security filters.

**`charset`:** JSON responses use UTF-8; do not hand-build JSON strings in controllers unless escaping is carefully controlled—prefer DTO + Jackson.

### 7.3 Validation groups (optional advanced topic)

Sometimes **create** and **update** DTOs share a type but need different rules (password required on create, optional on patch). **Jakarta Validation groups** label constraints:

```java
public interface OnCreate {}
public interface OnUpdate {}

public record UserPatchRequest(
    @NotBlank(groups = OnCreate.class)
  String email,
    @Size(min = 8, groups = OnCreate.class)
    String password
) {}
```

Controller:

```java
@PostMapping
public UserDto create(@Validated(OnCreate.class) @RequestBody UserPatchRequest request) { ... }

@PatchMapping("/{id}")
public UserDto patch(@Validated(OnUpdate.class) @RequestBody UserPatchRequest request) { ... }
```

**`@Valid` vs `@Validated`:** `@Valid` (Jakarta) triggers default group only; Spring’s `@Validated` activates specified groups and is required on class-level for method-parameter validation.

**Failure mode:** forgetting to specify the group on the controller → constraints in non-default groups are **skipped**, silently accepting invalid data. Test both paths with MockMvc.

**Alternative pattern:** separate `CreateUserRequest` and `PatchUserRequest` records—clearer for public APIs than group-heavy single DTOs.

## 8. Real-World Use Cases

- **Signup API** with validated email/password DTOs and hashed password only in service layer.
- **Admin CSV export** returning `text/csv` with different `produces`.
- **File upload** with `MultipartFile` + size validation in service or DTO wrapper.
- **Pagination** with `@RequestParam` defaults and max size caps to prevent abuse.

## 9. Edge Cases

### 9.1 Binding edge cases

- **Empty body on `@RequestBody`:** often 400; use `Optional` or nullable types carefully.
- **Unknown JSON fields:** default Jackson fails on unknown properties only if `FAIL_ON_UNKNOWN_PROPERTIES` enabled.
- **Type mismatch in JSON:** `{"id":"not-a-number"}` may fail during binding before `@Valid` runs—return 400 with different exception type (`HttpMessageNotReadableException`); handle in advice separately from validation errors.
- **Optional fields:** distinguish JSON `null` vs missing key; Jackson + record components may treat them differently depending on `@JsonProperty` settings.

### 9.2 Servlet and security interaction

- **CSRF:** browser form posts need tokens; stateless JSON APIs often disable CSRF for `/api/**` when using bearer tokens—misconfiguration yields 403 with empty body unless advice maps it.
- **CORS preflight:** `OPTIONS` requests hit `DispatcherServlet` before `POST`; ensure security permits OPTIONS for cross-origin SPAs.

## 10. Common Mistakes

### Incorrect — client-only validation

Malicious clients bypass browsers; always validate server-side.

### Incorrect — validation on entities exposed to JSON

Persistence annotations and API rules diverge; use DTOs.

### Incorrect — 200 OK with error payload

Breaks HTTP-aware clients and caches; use proper 4xx/5xx.

## 11. Design Trade-offs

| `@Controller` + `@ResponseBody` per method | `@RestController` |
|--------------------------------------------|-------------------|
| Mixed HTML + JSON legacy apps | JSON-first APIs |

| Synchronous MVC | WebFlux (reactive) |
|-----------------|-------------------|
| Familiar servlet threads | Non-blocking when entire stack is reactive |

## 12. Revision and Mastery

1. What runs first: JSON binding or `@Valid`?
2. When use `201 Created` vs `200 OK`?
3. Why validate DTOs instead of entities?
4. Which exception type signals `@RequestBody` field errors?
5. How does `rest-httpserver-demo` mirror Boot status discipline?

## 13. Exercises

1. Design `CreateUserRequest` with three validation rules and explain failure response shape.
2. Add handler narrative for `ConstraintViolationException` on `@RequestParam`.
3. Compare demo `POST /users` 201 behavior to `ResponseEntity.created(...)`.
4. Write a MockMvc test expectation for 400 on invalid email (conceptual or in spring-boot-minimal style).

## 14. References

- [Spring Framework — Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Jakarta Bean Validation 3.0](https://jakarta.ee/specifications/bean-validation/3.0/)
- [RFC 7807 — Problem Details](https://www.rfc-editor.org/rfc/rfc7807)

## 15. Chapter Summary

Boot’s web layer binds HTTP to Java DTOs, validates input with **Bean Validation**, and maps outcomes to **status codes** and **ProblemDetail** errors. Understand raw HTTP from the JDK demo; use Boot for binding, validation, and consistent REST ergonomics. Module **38** deepens resource modeling and contract tests.

## 16. Next Steps

[04-actuator-testing-and-production.md](04-actuator-testing-and-production.md)

## Review Checklist

- [x] Level 3: REST explained in chapter; validation pipeline taught
- [x] Verified rest-httpserver-demo and spring-boot-minimal citations
