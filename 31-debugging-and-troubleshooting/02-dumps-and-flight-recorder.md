# Dumps and Flight Recorder

> **Status:** Completed  
> **Module:** 31 — Debugging and Troubleshooting  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (diagnostics-awareness-demo; `jcmd` commands documented)

## 1. Learning Objectives

- List JVMs with **`jcmd`** and capture **thread dump** / **heap dump**.
- Describe **JFR** (JDK Flight Recorder) events vs continuous logging.
- Know when dumps are safe in production (disk, privacy).

## 2. Prerequisites

- Module **26** Ch.05 — diagnostics overview.
- Module **24** — threads.

## 3. Why This Concept Exists

When you cannot attach an IDE, **JDK tools** inspect live JVMs or analyze recordings post-incident. **Thread dumps** show stack traces for every thread; **heap dumps** show object graphs (Module **27** leak analysis). **JFR** records low-overhead events (HotSpot product feature).

## 4. Conceptual Explanation

| Tool | Output |
|------|--------|
| `jcmd <pid> Thread.print` | Thread dump to stdout |
| `jcmd <pid> GC.heap_dump /path/heap.hprof` | Heap snapshot file |
| `jcmd <pid> JFR.start ...` | Start flight recording |
| `jcmd <pid> JFR.dump ...` | Write `.jfr` file |
| **JDK Mission Control** | GUI for JFR files |

**JVMS** does not mandate JFR; it ships with HotSpot builds (Temurin, Oracle JDK). OpenJDK builds generally include JFR on Java 21.

## 5. Step-by-Step Examples

Demo: [diagnostics-awareness-demo](../../examples/debugging/diagnostics-awareness-demo/).

**Verified output:**

```text
jvmName=OpenJDK 64-Bit Server VM
inputArgsCount=0
threadCount=7
note=Use jcmd/jfr for dumps; see Module 31
```

While that demo runs (or any Java process):

```bash
jcmd
jcmd <pid> Thread.print
```

Example `Thread.print` excerpt shape (live process; lines vary):

```text
"main" #1 prio=5 os_prio=0 cpu=120.00ms elapsed=1.23s tid=... nid=... runnable
   java.lang.Thread.sleep(...)
```

**Do not fabricate** full dumps in documentation — capture on your machine when practicing.

### JFR start (HotSpot)

```bash
jcmd <pid> JFR.start name=handbook settings=profile duration=60s filename=run.jfr
```

## 6. Real-World Use Cases

- Stuck threads waiting on a lock — thread dump shows `BLOCKED` frames.
- `OutOfMemoryError: Java heap space` — heap dump + MAT/VisualVM.
- Latency spikes — JFR `jdk.ExecutionSample` and GC events.

## 7. Edge Cases and Limitations

- Heap dumps can be **huge** — ensure disk space and data retention policy.
- Dumps may contain **PII** in object fields — scrub before sharing.
- `jcmd` requires permission to attach to target process (same user / elevation on Windows).

## 8. Best Practices

- Automate periodic thread dumps during incidents (with rate limits).
- Correlate dumps with **timestamps** and deployment version.
- Practice locally before production firefighting.

## 9. Chapter Summary

`jcmd` thread/heap dumps and JFR extend Module 26 diagnostics into production troubleshooting workflows.

## 10. Next Steps

[03-production-incidents.md](03-production-incidents.md)

## Review Checklist

- [x] diagnostics-awareness-demo verified
- [x] jcmd/JFR labeled HotSpot/JDK tooling
