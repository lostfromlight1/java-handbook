# Threads and Runnables

> **Status:** Completed  
> **Module:** 24 — Concurrency and Multithreading  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Deep-dive  
> **Depth target:** Levels 3–4 (thread lifecycle, JVMS stacks vs shared heap, start/join contracts)  
> **Examples verified with:** Oracle JDK 21.0.9 (third deepen pass 2026-11-11; Temurin 21.0.12.1 not installed on this host). Output matches prior Temurin 21.0.12.1 recordings.

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. For omitted sections, record reasons in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs |
| Common mistakes | Compatibility details |
| Summary and references | — |

**Editorial judgment:** Level 4 on **`start`/`join` happens-before** and the **JVMS stack vs heap** picture. Application code should prefer executors (Ch.03); this chapter builds the mental model those APIs sit on.

## 1. Learning Objectives *(mandatory)*

After completing this chapter, you should be able to:

- Start asynchronous work with **`Thread`** and **`Runnable`** and explain why **`start()`** must not be replaced by direct **`run()`** calls.
- Use **`join()`** to wait for completion and cite the **happens-before** edge from worker actions to the joining thread (JLS §17.4.5).
- Name threads and interpret **daemon** vs **non-daemon** behavior at JVM shutdown (API).
- Distinguish **platform threads** (one-to-one with OS threads in HotSpot’s usual mapping) from tasks that still run on the **caller’s thread**.
- Map **locals and stack frames** to the current thread while **heap objects** are shared (JVMS).
- Explain why unbounded `new Thread()` per request fails at scale and point to executors / virtual threads for the next steps.

## 2. Prerequisites *(mandatory)*

- **Module 00** Ch.02 — process vs thread; call stack intuition.
- **Module 16** — lambdas implementing `Runnable`.
- **Module 04** — stack frames and pass-by-value of references (what gets copied between threads).

**Refresher:** A **reference** is a value on the stack pointing at a heap object. Two threads with references to the same object see **shared mutable state** unless you coordinate (Ch.02, Module 25).

**Refresher — process vs thread:** A **process** is an OS-managed instance of a program with its own address space. A **thread** is a unit of execution inside a process. The JVM process hosts many Java threads that share one heap.

## 3. Why This Concept Exists *(mandatory)*

Single-threaded `main` cannot overlap a long database call with serving another user or keep a UI responsive while reading files. **Concurrency** runs multiple threads of control in one JVM **process**. Each thread advances its own bytecode sequence with its own **stack**; the **heap** is shared—so every design question about threads eventually becomes a question about **safe sharing**.

`Runnable` separates **what to run** from **how the JVM schedules it**, enabling executors (Ch.03), virtual threads (Ch.05), and testable task objects. Learning raw `Thread` first is not so you ship `new Thread` in production for every request—it is so you understand what pools and virtual threads are scheduling.

## 4. Conceptual Explanation *(mandatory)*

### 4.1 Thread as an execution carrier

A **thread** is a thread of execution in the JVM. From the **Java SE API**, you create a `Thread`, associate a `Runnable`, call **`start()`**, and the JVM causes `run()` to execute on a **new** thread of execution.

Calling **`run()`** directly executes the task on the **current** thread—no new concurrency. That mistake is silent: the program “works” but never overlaps work.

**First appearance — platform thread:** In the traditional model, a Java `Thread` corresponds to an operating-system thread (**HotSpot** usual mapping). These are relatively expensive. Ch.05 introduces **virtual threads**, which are also `Thread` instances in the API but are scheduled differently by the JVM.

### 4.2 `Runnable` and lambda tasks

`Runnable` is a functional interface:

```java
@FunctionalInterface
public interface Runnable {
    void run();
}
```

Typical pattern:

```java
Thread worker = new Thread(() -> { /* task */ }, "handbook-worker");
worker.start();
```

Subclassing `Thread` and overriding `run()` is possible but couples the task to the thread type; **passing a Runnable** is idiomatic and works unchanged with executors later.

### 4.3 Lifecycle (simplified API model)

| Phase | Meaning |
|-------|---------|
| New | `Thread` constructed, not started |
| Runnable / running | After `start()`; OS/JVM scheduler chooses when CPU runs the thread |
| Terminated | `run()` completed; `isAlive()` false |

Exact state names differ in JVM TI / documentation; use this model for application code. Calling `start()` twice on the same `Thread` instance throws `IllegalThreadStateException`.

### 4.4 Daemon threads

`setDaemon(true)` **before** `start()` marks a **daemon** thread. When only daemon threads remain, the JVM may exit. **Non-daemon** threads (like default `main`) keep the JVM alive until they finish.

This is **API behavior** for shutdown—not a happens-before lesson by itself. Background helpers (cache cleaners, etc.) are sometimes daemons; request workers usually are not—you want an orderly drain on shutdown (Ch.03).

### 4.5 Interruption (orientation)

`Thread.interrupt()` sets the interrupt status. Blocking calls such as `join`, `sleep`, and many I/O waits may throw `InterruptedException` and clear the status. Cooperative cancellation is the model: tasks must notice interruption. Deep cancellation design appears with executors (`shutdownNow`) and later modules; here, know that ignoring interrupts makes shutdown unreliable.

## 5. Mechanism and Execution Flow *(mandatory)*

### 5.1 `start()` vs `run()`

```mermaid
sequenceDiagram
    participant Main
    participant JVM
    participant Worker as Worker thread

    Main->>JVM: new Thread(task)
    Main->>JVM: start()
    JVM->>Worker: schedule run()
    Note over Main,Worker: Main continues without waiting
    Main->>Worker: join() optional
    Worker-->>Main: run() finished; join returns
```

**JLS happens-before:** `Thread.start()` **happens-before** any action in the started thread. **`join()`** completion **happens-before** actions after `join` in the joining thread (JLS §17.4.5).

### 5.2 Memory model picture (JVMS + JLS)

```mermaid
flowchart LR
    subgraph T1["Thread A stack"]
        R1[ref to Obj]
        L1[local x]
    end
    subgraph T2["Thread B stack"]
        R2[ref to Obj]
    end
    subgraph Heap
        Obj[shared object fields]
    end
    R1 --> Obj
    R2 --> Obj
```

Locals are thread-confined; heap fields reachable from multiple threads require synchronization (Ch.02). Passing a reference into a `Runnable` lambda does **not** copy the object—only the reference value is captured for the worker to use.

### 5.3 Demo trace

Demo: [threads-runnable-demo](../../examples/core-java/threads-runnable-demo/).

1. Main prints its thread name (`main`) and default daemon flag (`false`).
2. Worker thread prints its name (`handbook-worker`) and `isAlive()==true` while running.
3. `join()` blocks main until worker finishes; then `afterJoinAlive=false`.

**Compile and run:**

```text
javac ThreadsRunnableDemo.java
java ThreadsRunnableDemo
```

**Verified output (Oracle JDK 21.0.9; matches prior Temurin 21.0.12.1):**

```text
mainThread=main
daemonDefault=false
workerName=handbook-worker
workerAlive=true
afterJoinAlive=false
```

**Important lines:** Constructor `new Thread(Runnable, String)` sets the name **before** `start()`. `join()` is what makes printing `afterJoinAlive` safe after worker completion (happens-before). Without `join`, main could observe the worker still alive or exit depending on timing—and reading worker-written shared fields without another HB edge would be racy.

## 6. Terminology *(mandatory)*

| Term | Meaning |
|------|---------|
| Platform thread | `Thread` backed by an OS thread (traditional HotSpot model) |
| Virtual thread | Lightweight `Thread` scheduled onto carriers (Ch.05; final in Java 21) |
| Task | `Runnable` (or later `Callable`) body executed by a thread |
| `start()` | JVM begins concurrent execution of `run()` |
| `join()` | Wait until target thread terminates; establishes HB |
| Daemon | Background thread that does not prevent JVM exit when alone |
| Interrupt | Cooperative cancellation signal (`interrupt` / `InterruptedException`) |

## 7. Syntax and Language Rules *(include when the topic is language/API surface)*

```java
Thread t = new Thread(() -> doWork());
t.setName("worker-1"); // optional if not passed to constructor
t.start();
t.join();              // throws InterruptedException — handle or declare
```

- Uncaught exceptions on non-main threads invoke `UncaughtExceptionHandler`; they do **not** automatically terminate the whole JVM.
- `join(long millis)` waits with a timeout; returning does not by itself prove the worker finished—check `isAlive()` or use a protocol.
- Prefer setting daemon status and names **before** `start()`.

## 8. Step-by-Step Examples *(mandatory)*

### 8.1 Minimal — lambda worker (**Verified** in demo)

See demo source: named worker, `start`, `join`.

### 8.2 Incorrect — calling `run()` on main thread

```java
Thread t = new Thread(() -> System.out.println(Thread.currentThread().getName()));
t.run(); // prints "main" — no new thread
```

### 8.3 Corrected

```java
t.start(); // prints the worker thread's name
t.join();
```

### 8.4 Realistic — wrapping checked exceptions

`Runnable.run()` cannot throw checked exceptions. Wrap in try/catch or use `Callable` + executor (Ch.03). Swallowing exceptions without logging makes worker failures invisible.

### 8.5 Advanced — two workers *(Illustrative exercise target)*

Start two named workers, `join` both, then read a result published under a safe protocol (Ch.02 atomics or Module 25 visibility). Ordering of the two workers relative to each other is undefined unless you synchronize them.

## 9. Real-World Use Cases *(conditional)*

- Background refresh in a desktop client (still prefer executors for lifecycle).
- Test harness spinning minimal threads to reproduce races (Ch.02 demo pattern).
- Understanding **virtual threads** as `Thread` values with `isVirtual()` (Ch.05).
- Reading thread dumps: named workers turn “who is stuck?” into an answerable question.

## 10. Internal Behavior *(conditional)*

- **JVMS:** Each Java thread has a Java Virtual Machine stack; heap is shared (JVMS §2.5).
- **JLS:** `start` and `join` contribute happens-before edges (JLS §17.4.5).
- **HotSpot:** Platform Java threads map to OS threads; scheduling is OS + JVM safepoints—**not** a correctness tool for your algorithms. Do not insert “sleep to fix the race.”

## 11. Edge Cases and Limitations *(strongly encouraged)*

- Unbounded `new Thread()` per request can exhaust memory or OS thread limits.
- `join()` without bound can hang forever if the worker never finishes.
- **Thread-local** state does not cross threads unless inheritance rules apply (`InheritableThreadLocal`—use sparingly).
- Priority (`setPriority`) is a weak hint; never rely on it for correctness.
- After termination, you cannot restart the same `Thread` instance.

## 12. Common Mistakes *(mandatory)*

### Incorrect

Subclass `Thread`, override `run()`, forget `start()`.

### Corrected

`start()` after construction; prefer `Runnable` + executor in applications.

### Incorrect

Shared mutable fields without coordination because “each thread has its own stack.”

### Corrected

Stacks isolate **locals**; **heap fields** are shared.

### Incorrect

Catching `InterruptedException`, clearing the problem by empty catch, and continuing as if cancellation never happened.

### Corrected

Restore interrupt status or exit the task; treat interruption as a signal.

## 13. Best Practices *(include when guidance adds value)*

- Use **executors** (Ch.03) for application thread pools.
- **Name threads** for logs and thread dumps.
- Treat shared data as unsafe until proven otherwise.
- Prefer structured lifecycle (join, pools, scopes) over fire-and-forget raw threads in servers.

## 14. Design Trade-offs *(conditional)*

| Raw `Thread` | ExecutorService | Virtual thread (Ch.05) |
|--------------|-----------------|-------------------------|
| Simple for learning | Pooling, policies, shutdown | Cheap blocking concurrency |
| Manual lifecycle | Bounded parallelism | Still a `Thread` in the API |
| Easy to leak | Central place to manage | Different scheduling cost model |

## 15. Performance Considerations *(conditional)*

Thread creation has OS/JVM cost; pooling amortizes it (Ch.03). Virtual threads reduce cost for massive **blocking** workloads (Ch.05). Context switching and false sharing on hot heap fields dominate long before micro-optimizing `start` itself.

## 16. Security Considerations *(conditional)*

*Omitted — no direct security API surface.* Untrusted code that can create threads can still cause resource exhaustion—sandboxing and container limits are operational controls.

## 17. Java Version and Compatibility Notes *(conditional)*

- `Thread` virtual-thread factory APIs finalized in Java 21 (Ch.05).
- `Thread.ofVirtual()` / `startVirtualThread()` are standard in Java 21.
- Classic `new Thread(Runnable)` remains valid and is the right teaching surface here.

## 18. Related Language Comparisons *(conditional)*

*Omitted.*

## 19. Revision and Mastery *(major / Technical / Deep-dive chapters)*

**Gaps:** Confusing `run` vs `start`; assuming stack isolation implies heap safety; using sleeps as synchronization; forgetting named threads in production incidents.

**Questions:**

1. What happens-before edge does `join()` give?
2. Why name threads in production?
3. What does `isAlive()` tell you after a timed `join` returns?
4. Why is `Runnable` preferred over subclassing `Thread`?

## 20. Exercises *(include for major chapters)*

### Beginner

Modify demo to start two workers and `join` both.

### Intermediate

Demonstrate `run()` vs `start()` naming on `currentThread()`.

### Advanced

Install an `UncaughtExceptionHandler` on a worker that throws unchecked and confirm main still completes after `join`.

## 21. Interview Questions *(conditional)*

- `start()` vs `run()`?
- Daemon thread effect on JVM exit?
- How do stacks and the heap relate across threads?

## 22. Solutions *(or link to separate solutions file)*

Beginner: second worker joined in sequence or store in array and `join` each. Intermediate: `run` prints `main`; `start` prints worker name. Advanced: handler logs; `join` still returns after the worker dies from the exception.

## 23. Chapter Summary *(mandatory)*

**`Thread` + `Runnable` + `start()`** begin concurrent work; **`join()`** waits and establishes **happens-before** with the worker. Stacks confine locals; the **heap** is shared—coordinate in Ch.02 and Module 25. Prefer executors for real systems; learn raw threads so pools and virtual threads make sense.

## 24. References and Further Reading *(mandatory)*

- [JLS §17.4.5 — Thread start and join](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html#jls-17.4.5)
- [JVMS §2.5 — Runtime data areas](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.5)
- [Java SE `Thread`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html)
- [Java SE `Runnable`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Runnable.html)

## 25. Next Steps

[02-synchronization-and-atomics.md](02-synchronization-and-atomics.md)

## Review Checklist

- [x] Profile Deep-dive; Levels 3–4 on start/join/HB and JVMS heap picture
- [x] Mechanism with mermaid; Verified demo output (Oracle 21.0.9; matches prior Temurin)
- [x] JLS/JVMS vs HotSpot labeled
- [x] Revision and Mastery present; third deepen pass expansions
- [x] Omissions: Security, language comparisons — not central
