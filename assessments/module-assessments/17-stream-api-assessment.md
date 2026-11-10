# Assessment: Module 17 — Stream API

> **Type:** Module  
> **Maps to:** Module 17 chapters 01–04  
> **Time guide:** 45–60 minutes  
> **Solutions:** [17-stream-api-assessment-solutions.md](17-stream-api-assessment-solutions.md)

## Instructions

Predict demos first; verify with Temurin JDK 21 as needed.

## Part A — Conceptual

1. Stream vs **Collection** — one sentence each role.
2. What triggers **lazy** intermediates to run?
3. Can you call two terminals on one stream?
4. `map` vs `flatMap`?
5. `reduce` with identity `0` and `Integer::sum` on empty stream?
6. Default map value type for `groupingBy` without downstream?
6b. Name the four conceptual parts of a `Collector` (supplier / …).
7. Which pool backs default `parallelStream()`? Is its parallelism a JLS guarantee?
8. Is `forEach` on parallel stream ordered?
9. Why `toList()` terminal vs `collect(toList())` difference (Java 21)?
10. Short-circuit terminal example?

## Part B — Predict (StreamFundamentalsDemo)

11. `rangeSum=`?
12. Lines printed containing `filter:` (count)?
13. `firstL=`?

## Part C — Predict (StreamOperationsDemo)

14. `evensDouble=`?
15. `sum=`?
16. `anyBig=`?

## Part D — Predict (CollectorsGroupingDemo)

17. Size of `eng` list in `byDept`?
18. `counts` eng value?

## Part E — Pitfalls

19. `list.parallelStream().forEach(shared::add)` — problem?
20. Blocking HTTP inside `parallelStream().map` — problem?

## Scoring guide

| Score | Focus |
|-------|--------|
| 85–100% | Module 18 |
| 60–84% | Ch.02 terminals + Ch.03 collectors |
| Below 60% | Ch.01 laziness, redo fundamentals demo |
