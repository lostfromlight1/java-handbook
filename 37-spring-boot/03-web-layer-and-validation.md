# Web Layer and Validation

> **Status:** Completed  
> **Module:** 37 — Spring Boot  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/) (HTTP semantics without Spring)

## 1. Learning Objectives

- Map requests with **`@RestController`**, path variables, and request bodies.
- Validate input with **Jakarta Bean Validation** (`@Valid`, `@NotNull`, etc.).
- Return consistent **HTTP status** and **error bodies** (preview Module 38).
- Understand **content negotiation** and JSON mapping (Jackson).

## 2. Prerequisites

- Module 32 — HTTP; Module 36 — MVC overview.
- Module 38 Ch.02 for error envelope depth.

## 3. Why This Concept Exists

APIs fail gracefully when **bad input never reaches business logic**. Declarative validation on DTOs plus MVC’s binding layer reduce boilerplate `if (name == null)` chains and align with HTTP status semantics clients expect.

## 4. Conceptual Explanation

### Request mapping

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserRequest request) {
        UserDto created = userService.create(request);
        URI location = URI.create("/api/v1/users/" + created.id());
        return ResponseEntity.created(location).body(created);
    }
}
```

`@RequestBody` — Jackson deserializes JSON to `CreateUserRequest`.  
`@Valid` — triggers Bean Validation after binding.

### Validation annotations (Jakarta)

| Annotation | Use |
|------------|-----|
| `@NotBlank` | String not empty/whitespace |
| `@Email` | Email format |
| `@Size` | String/collection bounds |
| `@Min` / `@Max` | Numeric bounds |

Validation lives on **DTOs**, not JPA entities—keeps persistence rules separate from API contract.

### Exception handling (conceptual)

```java
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Validation failed");
    }
}
```

Spring 6+ **`ProblemDetail`** (RFC 7807 style) for structured errors.

## 5. Plain-Java parallel (verified)

[rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/) shows:

```text
GET /users/1 status=200 body={"id":1,"name":"Lin"}
POST /users status=201
```

Same status discipline Boot applies automatically when you use `ResponseEntity` and exception handlers.

## 6. Real-World Use Cases

- Public signup API with validated email/password DTOs.
- File upload with size limits (`MultipartFile` + validation).
- Pagination: `@RequestParam(defaultValue = "0") int page`.

## 7. Common Mistakes

### Incorrect — validation only on client

Always re-validate server-side.

### Incorrect — returning stack traces in JSON errors

Information disclosure; use problem details with safe messages.

## 8. Design Trade-offs

| `@Controller` + `@ResponseBody` | `@RestController` |
|--------------------------------|-------------------|
| Mixed HTML + JSON apps | JSON APIs |

## 9. Revision and Mastery

- What runs first: JSON binding or `@Valid`?
- When to use `201 Created` vs `200 OK`?

## 10. Exercises

1. Design `CreateUserRequest` with three validation rules.
2. Add handler mapping for `ConstraintViolationException` on `@RequestParam`.
3. Compare demo HttpServer handler status codes to REST chapter guidance.

## 11. Chapter Summary

Boot’s web layer binds HTTP to Java objects, validates DTOs, and maps outcomes to status codes—Jackson and Bean Validation on top of servlet MVC. Understand raw HTTP from the JDK demo, then use Boot for ergonomics.

## 12. Next Steps

[04-actuator-testing-and-production.md](04-actuator-testing-and-production.md)

## Review Checklist

- [x] Validation, ProblemDetail, REST demo cross-link
