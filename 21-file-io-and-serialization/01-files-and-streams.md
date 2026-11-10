# Files and Streams

> **Status:** Completed  
> **Module:** 21 — File I/O and Serialization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Read and write text with **`Files`** (NIO.2) and understand classic **streams** (`Reader`/`Writer`, `InputStream`/`OutputStream`).
- Use **try-with-resources** for automatic close.
- Keep examples **self-contained** with temp files or in-memory streams.

## 2. Prerequisites

- Module **14** try-with-resources; Module **20** I/O overview.

## 3. Why This Concept Exists

Programs persist and exchange data. Java offers both stream-oriented (`java.io`) and file-centric (`java.nio.file`) APIs; modern code favors **NIO.2** for paths while streams remain essential for sockets and serialization.

## 4. Conceptual Explanation

### 4.1 Stream model

A **stream** is a sequence of bytes or characters processed sequentially—good for large data without loading all into memory.

### 4.2 NIO.2 first for files

`Files.writeString`, `Files.readString`, `Files.lines` operate on `Path` with explicit `Charset` (prefer `StandardCharsets.UTF_8`).

### 4.3 Bridging

`Files.newInputStream` / `newOutputStream` connect NIO.2 paths to stream APIs. `BufferedReader` improves line-oriented reading.

## 5. Step-by-Step Examples

Demo: [files-streams-demo](../../examples/core-java/files-streams-demo/).

**Verified output:**

```text
tempLines=3
bufferedTransform=ONE
TWO
readBytes=17
```

Creates a **temp file**, writes three lines, counts with `Files.lines`. In-memory `StringReader`/`StringWriter` demonstrate classic buffering. `readBytes=` shows binary read length.

## 6. Real-World Use Cases

- Exporting CSV reports to temp files for upload.
- Parsing config line-by-line with `BufferedReader`.
- Streaming large logs without `readAllBytes` on huge files.

## 7. Edge Cases and Limitations

- Default charset is UTF-8 on modern JDKs—still specify charset in libraries.
- `Files.lines` must be closed (try-with-resources on Stream).
- Windows vs Unix line endings (`\r\n` vs `\n`) affect line counts.

## 8. Common Mistakes

### Incorrect

Forgetting to close streams (use try-with-resources).

### Corrected

```java
try (var lines = Files.lines(path)) { ... }
```

## 9. Best Practices

- Prefer **`Path`** over string paths.
- Delete temp files in `finally` or try-with-resources patterns.
- For huge files, stream; for small config, `readString` is fine.

## 10. Performance Considerations

`BufferedReader` reduces syscalls; `readAllBytes` loads entire file—avoid for multi-GB inputs.

## 11. Security Considerations

Validate paths resolve under expected directories (see Ch.02 `normalize` / `startsWith` checks) to block path traversal.

## 12. Chapter Summary

Use **NIO.2 `Files`** for path-based file work; combine with **streams** for transformation pipelines; always close resources.

## 13. Next Steps

[02-nio2.md](02-nio2.md)

## Review Checklist

- [x] NIO.2 preferred  
- [x] Temp file self-contained demo verified
