# Serialization and Data Formats

> **Status:** Completed  
> **Module:** 21 — File I/O and Serialization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Serialize graphs with **`ObjectOutputStream`** / **`ObjectInputStream`** when appropriate—and know **risks**.
- Use **`Properties`** for simple key-value persistence.
- Compare Java serialization to **JSON/XML** interchange formats conceptually.

## 2. Prerequisites

- Ch.01–02; classes and records (Module 15).

## 3. Why This Concept Exists

Systems exchange data as bytes. Java **native serialization** preserves object graphs but is **fragile and insecure** against untrusted input. Modern services prefer JSON/Protobuf; serialization remains relevant for caches, RMI legacy, and some frameworks.

## 4. Conceptual Explanation

### 4.1 Java serialization

Types implement **`Serializable`** (marker). `serialVersionUID` stabilizes version evolution. **Records** can implement `Serializable` like classes.

### 4.2 Deserialization attacks

Untrusted byte streams can instantiate dangerous gadget chains—**do not deserialize untrusted data** (see Oracle serialization filters, JEP 415).

### 4.3 Properties format

`.properties` files: ISO-8859-1 with Unicode escapes; `Properties.store` / `load` for simple config.

### 4.4 JSON and others

Libraries (Jackson, Gson) map objects to JSON—preferred for APIs. Demo shows byte length of JSON **string** without external deps.

## 5. Step-by-Step Examples

Demo: [serialization-formats-demo](../../examples/core-java/serialization-formats-demo/).

**Verified output:**

```text
serializedBytes=99
deserialized=Ada:97
propertiesStored=true
jsonBytes=25
```

Round-trip serializes `UserDto` record; stores properties to bytes; measures UTF-8 JSON string length.

## 6. Real-World Use Cases

- Session replication (legacy) — prefer DTO + JSON.
- Feature flags in `.properties` for ops.
- Cross-language APIs — never Java serialization on the wire.

## 7. Edge Cases and Limitations

- **Enum**, **transient** fields, custom `writeObject` — evolution complexity.
- **Records** — components serialized by name rules; still need security review.

## 8. Common Mistakes

### Incorrect

Deserializing network data without allowlists.

### Corrected

Use `ObjectInputFilter` / avoid Java serialization for external input.

## 9. Best Practices

- Prefer **versioned DTOs** and text formats for APIs.
- If serialization required: explicit `serialVersionUID`, minimal class set, filters.
- Document **compatibility** when changing fields.

## 10. Security Considerations

Treat Java deserialization as **dangerous** with untrusted bytes; mandatory filters or alternative formats.

## 11. Chapter Summary

Know Java serialization mechanics and **when not to use them**; use Properties for trivial config; default to JSON/text for interoperability.

## 12. Next Steps

Module 22 — `java.time`.

## Review Checklist

- [x] Security warnings included  
- [x] Verified demo output
