# EncodingPerfDemo

Module 11 Ch.04 — UTF-16 `length()` vs UTF-8 bytes; builder for repeated append.

```bash
javac EncodingPerfDemo.java
java EncodingPerfDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
chars=4
utf8Bytes=5
roundtripOk=true
built=012
```

Concepts: supplementary characters, `StandardCharsets.UTF_8`, `getBytes` / `new String(byte[])`.

**Note:** Console must use UTF-8 to display `é` in `roundtrip=`; source uses `\u00e9`.
