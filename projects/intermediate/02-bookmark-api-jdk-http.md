# Project: Bookmark REST API (JDK HttpServer)

> **Level:** Intermediate  
> **Status:** Completed  
> **Prerequisites:** Modules 32, 38, 14, 12  
> **Last reviewed:** 2026-11-10

## Goals

Expose bookmarks over HTTP using `com.sun.net.httpserver.HttpServer` — no Spring required. JSON bodies via manual parsing or small helper (Jackson optional).

## Learning objectives

- Map routes: `GET /bookmarks`, `POST /bookmarks`, `DELETE /bookmarks/{id}`.
- Status codes 200/201/404/400 aligned with Module 38.
- In-memory `ConcurrentHashMap` repository for thread-safe demos.

## Architecture

Mirror [rest-httpserver-demo](../../examples/backend/rest-httpserver-demo/): one class registers contexts; DTO record for `Bookmark`.

## API design

| Method | Path | Body | Response |
|--------|------|------|----------|
| GET | `/bookmarks` | — | 200 JSON array |
| POST | `/bookmarks` | `{url, title}` | 201 + id |
| DELETE | `/bookmarks/{id}` | — | 204 or 404 |

## Milestones

1. GET empty list.
2. POST creates; GET returns item.
3. DELETE removes; repeat DELETE 404.
4. Concurrent POST stress smoke (10 threads) without lost updates.

## Acceptance criteria

- [ ] `Content-Type: application/json` on JSON responses.
- [ ] Invalid URL (no scheme) → 400 problem-style plain JSON `{error:"..."}`.
- [ ] README curl examples verified on Temurin 21.

## Verification steps

```bash
javac -d out ...
java -cp out com.handbook.bookmarks.Server &
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/bookmarks
curl -s -X POST -H "Content-Type: application/json" \
  -d '{"url":"https://openjdk.org","title":"OpenJDK"}' \
  http://localhost:8080/bookmarks
```

## Extension

OpenAPI YAML describing endpoints (Module 38).
