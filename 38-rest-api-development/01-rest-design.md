# REST Design

> **Status:** Completed  
> **Module:** 38 — REST API Development  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/)

## 1. Learning Objectives

- Apply **REST** constraints: resources, uniform interface, statelessness.
- Choose **HTTP methods** and **status codes** deliberately.
- Design **resource-oriented URLs** and idempotency rules.
- Relate REST to JDK `HttpServer` and Spring MVC controllers.

## 2. Prerequisites

- Module 32 — HTTP, `HttpClient`.
- Module 15 — records for resource representations.

## 3. Why This Concept Exists

Integrations scale when clients use **standard HTTP** against **named resources** instead of custom RPC endpoints per feature. REST (in the practical Richardson sense used in industry) gives conventions for CRUD, caching, and error semantics that proxies, CDNs, and API gateways understand.

## 4. Conceptual Explanation

| Principle | Practice |
|-----------|----------|
| **Resources** | Nouns (`/users`, `/orders/{id}`) not verbs (`/getUser`) |
| **Representations** | JSON (common), XML, etc. |
| **Statelessness** | Server does not store client session in the resource layer (auth tokens aside) |
| **Uniform interface** | GET/POST/PUT/PATCH/DELETE with standard semantics |

### Method semantics

| Method | Safe | Idempotent | Typical use |
|--------|------|------------|-------------|
| GET | Yes | Yes | Read |
| POST | No | No | Create, actions |
| PUT | No | Yes | Replace entire resource |
| PATCH | No | No* | Partial update |
| DELETE | No | Yes | Remove |

*PATCH idempotency depends on patch format—document behavior.

### Status codes (selection)

| Code | Meaning |
|------|---------|
| 200 | OK with body |
| 201 | Created; `Location` header |
| 204 | Success, no body |
| 400 | Client malformed input |
| 401 | Not authenticated |
| 403 | Authenticated but forbidden |
| 404 | Resource not found |
| 409 | Conflict (duplicate, stale version) |
| 422 | Semantically invalid (optional) |
| 500 | Server fault |

## 5. Runnable minimal API

Demo: [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/).

**Verified output:**

```text
GET /users/1 status=200 body={"id":1,"name":"Lin"}
POST /users status=201
restDemo=ok
```

Handlers enforce method allow-lists and return JSON with appropriate status—same decisions you make in `@RestController`.

## 6. Real-World Use Cases

- **Pagination:** `GET /users?page=0&size=20&sort=lastName,asc` or cursor tokens.
- **Filtering:** `GET /orders?status=SHIPPED` with indexed columns.
- **HATEOAS** (optional): links in JSON for discoverability—less common in pure mobile backends.

## 7. Edge Cases

- **GET with body** — discouraged; proxies may strip it.
- **Long-running POST** — return `202 Accepted` + status URL.
- **Soft delete** — expose `DELETE` or `PATCH` with `active=false`; document.

## 8. Common Mistakes

### Incorrect — `GET /deleteUser?id=1`

Non-idempotent action on safe method; breaks caches and retries.

### Incorrect — always `200` with `{error: ...}` inside body

Clients and monitors rely on status codes.

## 9. Design Trade-offs

| RPC style (`POST /userSearch`) | REST resources |
|--------------------------------|----------------|
| Flexible query in one call | Cacheable reads, predictable URLs |
| Harder HTTP semantics | More endpoints |

## 10. Revision and Mastery

- When is POST idempotent anyway (still “no” by definition but why do retries hurt)?
- Difference between 401 and 403?

## 11. Exercises

1. Redesign `RPC /api/cancelOrder` as RESTful routes.
2. Choose status for duplicate email on signup.
3. Extend demo with `PUT /users/1` returning 204.

## 12. Chapter Summary

REST maps business entities to HTTP resources with standard methods and status codes. The JDK demo proves the semantics; Spring MVC adds binding and validation ergonomics.

## 13. Next Steps

[02-dtos-errors-and-versioning.md](02-dtos-errors-and-versioning.md)

## Review Checklist

- [x] Verified rest-httpserver-demo; method/status tables
