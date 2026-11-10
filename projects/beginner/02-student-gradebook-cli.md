# Project: Student Gradebook CLI

> **Level:** Beginner  
> **Status:** Completed  
> **Prerequisites:** Modules 02–04, 11 (strings), 12 (`Map`/`List`)  
> **Last reviewed:** 2026-11-10

## Goals

Manage students and assignment scores from the terminal; compute averages and letter grades using the same rules as Module 03 conditionals.

## Learning objectives

- Model entities with `record Student(String id, String name)` and mutable grade book per id.
- Use `HashMap<String, List<Integer>>` or nested records.
- Apply grading thresholds with `switch` or `if-else` chain.

## Architecture

`GradebookApp` → `GradebookService` → in-memory store only (optional export).

## Milestones

1. Register student; add score 0–100.
2. `report <id>` — average and letter grade.
3. `top` — highest average among students (tie-break by name).
4. Unit tests for grade boundary 89/90/90.

## Acceptance criteria

- [ ] Reject duplicate student id on register.
- [ ] Empty score list → clear message, not exception to user.
- [ ] Letter grade table documented in README.

## Starter hints

- Reuse [scoreboard](../../examples/beginner/scoreboard/) averaging pattern.
- `Collections.max` with `Comparator` for top student.

## Verification steps

```text
register ada
add ada 88
add ada 92
report ada   → average 90.0, letter per your table
```

## Extension

Persist JSON lines; validate with handbook testing fundamentals demo style asserts.
