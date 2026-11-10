# Solutions: Module 21

1. **`Files.readString(path, StandardCharsets.UTF_8)`** (or equivalent UTF-8).
2. Stream holds **open directory/file handles**—must close to release resources.
3. **`resolve`** is **platform-correct** and handles separators; concat breaks on Windows/semantic `..`.
4. **Gadget chain / remote code execution** risk—use filters or avoid.
5. **`.properties`** text (ISO-8859-1 with Unicode escapes).
6. **Isolated** writable file without touching project tree.
7. **`StandardCharsets.UTF_8`** (explicit).
8. **Memory exhaustion** / OOM—stream instead.
9. **Yes** (like classes).
10. **JSON** (or protobuf)—not Java native serialization.

11. `3`
12. `2`
13. `Ada:97`

14. Normalize to absolute path and verify **`startsWith(base)`** after resolving `..`.
15. **No** without strict **`ObjectInputFilter`** allowlist—or use signed tokens instead.
