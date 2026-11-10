# HTTP and HttpClient

> **Status:** Completed  
> **Module:** 32 — Networking and HTTP  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (live HTTPS to example.com)

## 1. Learning Objectives

- Map **HTTP/1.1** request/response structure (method, status, headers, body).
- Use **`java.net.http.HttpClient`** (Java 11+) synchronously and know async option.
- Configure **timeouts** and **redirect** policy.

## 2. Prerequisites

- Ch.01 — TCP foundation.
- Module **14** — `IOException` handling.

## 3. Why This Concept Exists

**HTTP** is the dominant application protocol for APIs and web pages. The JDK **`HttpClient`** provides HTTP/2-capable client without Apache HttpClient dependency — suitable for microservices and scripts.

## 4. Conceptual Explanation

| Piece | Role |
|-------|------|
| Method | `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, … |
| Status code | `2xx` success, `4xx` client error, `5xx` server error |
| Headers | Metadata (`Content-Type`, `Authorization`, `User-Agent`) |
| Body | Optional payload (JSON, HTML, bytes) |

**HttpClient** is immutable and thread-safe; build once, reuse. **`HttpRequest`** is per-call. **`HttpResponse.BodyHandlers`** choose String, bytes, or streaming body.

**Sync:** `client.send(request, BodyHandlers.ofString())` blocks the calling thread — fine on virtual threads for many concurrent calls.

**Async:** `client.sendAsync` returns `CompletableFuture` (Module **24** Ch.03).

## 5. Step-by-Step Examples

Demo: [http-client-demo](../../examples/networking/http-client-demo/).

**Verified output (2026-11-10 run):**

```text
status=200
bodyStartsWith=true
headerServer=cloudflare
```

`https://example.com` returns HTML; `bodyStartsWith=true` checks `<!doctype html` after `strip()`. **`headerServer`** varies by CDN — value recorded from real response, not universal.

## 6. Real-World Use Cases

- Calling internal REST services (Module **38** builds servers).
- Downloading configuration from HTTPS endpoints.
- Webhooks and integration polling with timeouts.

## 7. Edge Cases and Limitations

- No built-in **retry** — implement with policy (Ch.03).
- Certificate trust uses JVM **trust store** — custom CAs need import.
- Large bodies should use **`BodyHandlers.ofInputStream()`** to avoid OOME.

## 8. Common Mistakes

### Incorrect

Default client with **no timeouts** — hangs on bad networks.

### Corrected

`connectTimeout` and per-request `timeout`.

## 9. Best Practices

- Reuse one **`HttpClient`** per application.
- Set explicit **`Content-Type`** on POST bodies.
- Parse JSON with Jackson in app code (later modules) — HttpClient delivers bytes/String only.

## 10. Chapter Summary

HttpClient is the modern JDK HTTP stack; the demo proves HTTPS GET with status and body checks on real network I/O.

## 11. Next Steps

[03-tls-and-resilience-basics.md](03-tls-and-resilience-basics.md)

## Review Checklist

- [x] Real network output recorded
