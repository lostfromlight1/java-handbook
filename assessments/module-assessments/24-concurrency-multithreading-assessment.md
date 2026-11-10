# Assessment: Module 24 — Concurrency and Multithreading

> **Solutions:** [24-concurrency-multithreading-assessment-solutions.md](24-concurrency-multithreading-assessment-solutions.md)

## Part A

1. `start()` vs calling `run()` directly?
2. What does `join()` establish (JMM)?
3. Why did `unsafeCounter` miss 40_000 in the demo?
4. `AtomicInteger` vs `synchronized` for one counter?
5. `ExecutorService` try-with-resources does what?
6. `Future.get` throws what on task failure?
7. `ConcurrentHashMap` null keys allowed?
8. Virtual threads final in which Java version?
9. `isVirtual()` true on which thread in the demo?
10. Structured concurrency status on Java 21?

## Part B — Predict

11. `ThreadsRunnableDemo` `afterJoinAlive=`?
12. `futureResult=` in executors demo?
13. After concurrent collections merge, `Ada=`?

## Part C

14. One million CPU-bound tasks — virtual threads or fixed platform pool?
15. `putIfAbsent` vs check-then-put on concurrent map?

## Scoring: 85%+ Module 25; else Ch.02 sync + Ch.05 virtual threads.
