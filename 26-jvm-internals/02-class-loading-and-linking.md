# Class Loading and Linking

> **Status:** Completed  
> **Module:** 26 — JVM Internals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Describe **loading → linking → initialization** (JVMS).
- Identify **bootstrap**, **platform**, and **application** class loaders (Java API / HotSpot).
- Use **`Class.forName`** vs static initializers safely.

## 2. Prerequisites

- Module **01** packages/classpath; Module **13** static init.

## 3. Conceptual Explanation

**Loading** finds bytes and defines `Class<?>`. **Linking** verifies bytecode, prepares static fields, resolves symbolic references. **Initialization** runs `<clinit>` when a class is first actively used.

`ClassLoader` hierarchy (JDK 9+ module system):

- **Bootstrap** — core `java.*` (`null` loader for those classes).
- **Platform** — JDK modules.
- **Application** — classpath/module path application code.

## 4. Step-by-Step Examples

Demo: [class-loading-demo](../../examples/jvm/class-loading-demo/).

**Verified output:**

```text
appLoader=jdk.internal.loader.ClassLoaders$AppClassLoader
stringLoader=null
demoLoaded=true
```

`stringLoader=null` means bootstrap loaded `java.lang.String`.

## 5. Edge Cases

- **LinkageError** / **NoClassDefFoundError** — missing deps at runtime.
- **Initialization deadlock** — static blocks circular load — rare design smell.

## 6. Chapter Summary

Class loading is specified by JVMS; loader names above are HotSpot/JDK details on Temurin 21.

## 7. Next Steps

[03-bytecode-and-execution.md](03-bytecode-and-execution.md)

## Review Checklist

- [x] Verified demo
