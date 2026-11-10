# Project: CLI Expense Tracker

> **Level:** Beginner  
> **Status:** Completed  
> **Prerequisites:** Modules 02–05, 14 (exceptions), 21 (files optional)  
> **Last reviewed:** 2026-11-10

## Goals

Build a command-line app that records expenses, lists them by month, and prints category totals. Reinforces types, collections, control flow, and basic file persistence.

## Learning objectives

- Parse `args` or simple text commands (`add`, `list`, `summary`).
- Use `Map`/`List` in memory with clear domain types (`Expense`, `Money` as `long` cents).
- Persist to CSV or JSON lines under `user.home` with try-with-resources.
- Handle invalid input without crashing (`NumberFormatException`, friendly message).

## Architecture

```text
main → ExpenseApp (loop) → ExpenseService → ExpenseRepository (file)
```

| Layer | Responsibility |
|-------|----------------|
| `Expense` | id, date (ISO local date string), category, amountCents, note |
| `ExpenseService` | validation, aggregation |
| `ExpenseRepository` | load/save all |

No frameworks; single module under `src/` or handbook `examples/`-style layout.

## Data model

| Field | Type | Rules |
|-------|------|-------|
| amountCents | `long` | > 0 |
| category | `String` | non-blank, max 32 chars |
| date | `String` or `LocalDate` | not future by default |

## Milestones

1. **M1 — In-memory add/list** — `add food 12.50 coffee` stores and prints table.
2. **M2 — Summary** — `summary 2026-11` prints per-category totals.
3. **M3 — Persistence** — restart reloads from `~/.handbook-expenses.csv`.
4. **M4 — Hardening** — unknown command help; corrupt line skip with warning.

## Acceptance criteria

- [ ] All commands documented in `--help`.
- [ ] Amounts printed with 2 decimal places from integer cents (no float money).
- [ ] File created on first save; append-safe or rewrite documented.
- [ ] At least three JUnit 5 tests on `ExpenseService` (validation + summary).

## Starter hints

- Store cents: `long cents = Math.round(Double.parseDouble(dollars) * 100);` — validate range.
- `DateTimeFormatter.ISO_LOCAL_DATE` for parsing dates.
- Start from [args-echo](../../examples/beginner/args-echo/) for CLI patterns.

## Verification steps

```bash
javac -d out src/**/*.java   # or your layout
java -cp out com.handbook.expense.ExpenseApp add food 10.00 lunch
java -cp out com.handbook.expense.ExpenseApp summary 2026-11
./mvnw -q test   # if Maven wrapper added
```

Expected: second run after restart still lists prior expense; summary matches manual sum.

## Security considerations

Local-only; no network. Sanitize paths — store file under user home, not arbitrary paths from input.

## Extension challenges

- Export `summary` as CSV.
- Subcommands with `switch` expression (Module 03).
