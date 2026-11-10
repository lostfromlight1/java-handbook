# IO, NIO, Net, and Time Overview

> **Status:** Completed  
> **Module:** 20 — Java Standard Library  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Map major JDK packages: **`java.io`**, **`java.nio`**, **`java.net`**, **`java.time`**.
- Know which module deepens each area (21 file I/O, 22 date/time).
- Use **`Path`**, **`ByteBuffer`**, **`URI`**, and **`Instant`** at survey level.

## 2. Prerequisites

- Module **14** — try-with-resources preview for I/O.

## 3. Why This Concept Exists

The JDK is large. A **survey** prevents guessing package names and clarifies migration paths (NIO.2 vs legacy streams, `java.time` vs legacy date).

## 4. Conceptual Explanation

| Package | Role | Handbook depth |
|---------|------|----------------|
| `java.io` | byte/char streams, readers/writers, serialization | Module 21 |
| `java.nio` | buffers, channels, charset, file API (`java.nio.file`) | Module 21 |
| `java.net` | sockets, URLs, HTTP client (`java.net.http` since 11) | Networking modules later |
| `java.time` | ISO-8601 domain model, no legacy `Date` in new code | Module 22 |

**NIO.2** (`java.nio.file.Path`, `Files`) is the preferred file API for new code (Module 21).

## 5. Step-by-Step Examples

Demo: [stdlib-overview-demo](../../examples/core-java/stdlib-overview-demo/).

**Verified output:**

```text
pathFileName=notes.txt
byteBufferPos=4
uriHost=example.com
instantEpoch=true
```

`pathFileName=` — `Path.of` parses a path string. `byteBufferPos=` — after `putInt`, position advances. `uriHost=` — `URI` parser. `instantEpoch=` — `Instant.now()` has positive epoch seconds.

## 6. Real-World Use Cases

- Config paths with `Path` instead of string concatenation.
- Network clients with `java.net.http.HttpClient`.
- Logging timestamps as `Instant` in UTC.

## 7. Edge Cases and Limitations

- `java.io.File` still appears in legacy code—prefer `Path`.
- NIO channels shine for bulk I/O; simple text may use `Files.readString`.
- Default charset is UTF-8 since Java 18—still specify `StandardCharsets.UTF_8` in APIs.

## 8. Common Mistakes

Mixing **`Date`/`Calendar`** with `java.time` in one domain model—pick `java.time` (Module 22).

## 9. Best Practices

- **Try-with-resources** for streams/channels.
- Store **UTC** instants; convert at UI with `ZonedDateTime`.
- Use **`Files`** for one-shot file reads/writes.

## 10. Chapter Summary

Standard library I/O, NIO, networking, and time packages each have a dedicated module; this chapter orients you before depth work.

## 11. Next Steps

[03-reflection-annotations-and-process-apis.md](03-reflection-annotations-and-process-apis.md); Modules 21–22.

## Review Checklist

- [x] Survey + verified demo
