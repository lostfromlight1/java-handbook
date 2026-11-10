# OpenAPI and API Testing

> **Status:** Completed  
> **Module:** 38 — REST API Development  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Author and consume **OpenAPI 3** documents as the API contract.
- Generate or document Spring controllers with **springdoc-openapi** (conceptual).
- Test APIs with **contract tests**, `MockMvc`, and manual tools (curl, Postman).
- Tie contract tests to CI governance (Module 30).

## 2. Prerequisites

- Ch.01–02 — REST, DTOs, errors.
- Module 30 — automated testing.

## 3. Why This Concept Exists

Without a machine-readable contract, frontend and backend teams **drift**: renamed fields break clients silently. **OpenAPI** describes paths, schemas, and responses so you can generate docs, client SDKs, and **contract verification** in CI.

## 4. OpenAPI essentials

```yaml
openapi: 3.0.3
info:
  title: Handbook Users API
  version: 1.0.0
paths:
  /api/v1/users/{id}:
    get:
      summary: Get user by id
      parameters:
        - name: id
          in: path
          required: true
          schema: { type: integer, format: int64 }
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserDto'
        '404':
          description: Not found
components:
  schemas:
    UserDto:
      type: object
      required: [id, name]
      properties:
        id: { type: integer, format: int64 }
        name: { type: string }
```

**Single source of truth:** either generate OpenAPI from annotations or write YAML first and generate stubs—pick one team policy.

### springdoc (conceptual)

Add `springdoc-openapi-starter-webmvc-ui` → `/swagger-ui.html` and `/v3/api-docs`. Annotations like `@Operation` enrich generated spec.

## 5. Testing pyramid for APIs

| Level | Tool | Validates |
|-------|------|-----------|
| Unit | JUnit + mocks | Service rules |
| Web slice | `MockMvc` | Status, JSON shape, security |
| Contract | OpenAPI + Dredd/Schemathesis/Postman | Spec vs running app |
| E2E | Test against deployed env | Real infra |

**MockMvc example (conceptual):**

```java
mvc.perform(get("/api/v1/users/1"))
   .andExpect(status().isOk())
   .andExpect(jsonPath("$.name").value("Lin"));
```

Align `jsonPath` with OpenAPI schema fields.

## 6. Plain-Java contract discipline

Even [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/) benefits from a checked mini-spec: expected status 200 body fields `id`, `name`. Document in README next to runnable output—**executable documentation**.

## 7. Consumer-driven contracts (overview)

Clients publish expected interactions (e.g. Pact). Provider verifies in CI—prevents breaking partners when you rename fields.

## 8. Common Mistakes

### Incorrect — OpenAPI generated once and never updated in CI

Spec lies; use diff checks on PR.

### Incorrect — testing only happy paths

Include 400, 401, 404, 409 cases from OpenAPI `responses`.

## 9. Security in API tests

- Never commit bearer tokens; use test fixtures and ephemeral realms.
- Validate auth-required endpoints return 401 without credentials.

## 10. Revision and Mastery

- Difference between contract test and `@WebMvcTest`?
- How does OpenAPI relate to JSON Schema in `components`?

## 11. Exercises

1. Write OpenAPI for POST `/users` with validation error 400 schema.
2. List five `MockMvc` assertions for pagination response.
3. Propose CI step failing when undocumented endpoint appears.

## 12. Chapter Summary

OpenAPI makes REST contracts explicit and testable; combine generated docs with MockMvc and contract tools. JDK demos can still carry minimal spec + verified output when Spring is not on the classpath.

## 13. Next Steps

Module **39** — authentication and authorization.

## Review Checklist

- [x] OpenAPI sample; testing layers; CI mention
