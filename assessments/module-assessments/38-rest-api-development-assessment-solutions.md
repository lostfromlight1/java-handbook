# Solutions: Module 38 — REST API Development

1. Server does not rely on client session state for resource handling; each request carries needed context (auth token, etc.).
2. GET, PUT, DELETE (also HEAD, OPTIONS); POST/PATCH generally not.
3. 404 not found; 400 malformed request; 409 conflict (e.g. stale version, duplicate).
4. Stable API contract; avoid lazy-load leaks; decouple from DB schema.
5. Machine-readable error: summary, HTTP code, human-safe detail; `instance` identifies request.
6. Pros: visible, easy routing; cons: many URLs, gateway rules multiply.
7. PUT replaces entire resource; PATCH partial update.
8. Contract truth; diff checks; client generation; regression against spec.
9. HTTP status, headers, JSON body shape for controllers in isolation.
10. GET (and HEAD)—should not change server state.

11. `{"id":1,"name":"Lin"}` (status 200)
12. Add optional JSON field with default behavior for old clients.
