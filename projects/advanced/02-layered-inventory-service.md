# Project: Layered Inventory Service

> **Level:** Advanced  
> **Status:** Completed  
> **Prerequisites:** Modules 36–40 concepts, 33 JDBC, 38 REST  
> **Last reviewed:** 2026-11-10

## Goals

Implement a **hexagonal** inventory API: domain core, ports, adapters — JDBC repository and HTTP adapter. Spring optional; manual DI acceptable (Module 36 `manual-di-container` style).

## Learning objectives

- Domain: `Product`, `Sku`, invariants (non-negative stock).
- Port: `InventoryPort` with `adjustStock`, `findBySku`.
- Adapters: `JdbcInventoryAdapter`, `HttpInventoryController` on JDK HttpServer or Boot if Maven present.
- Application service coordinates transactions.

## Architecture

```text
HTTP → InventoryController → InventoryService → InventoryPort ← JdbcAdapter
```

## Milestones

1. Domain + in-memory adapter + tests.
2. Swap JDBC adapter; same tests via port.
3. HTTP `PUT /stock/{sku}` with JSON `{delta: -5}`.
4. Architecture test or documented package rules: domain does not import JDBC.

## Acceptance criteria

- [ ] Negative stock rejected in domain with typed exception → HTTP 409.
- [ ] JDBC uses transactions for adjust (Module 33).
- [ ] README diagram matches Module 40 hexagonal narrative.
- [ ] At least 8 unit/integration tests.

## Verification steps

```bash
./mvnw -q test
# manual: adjust stock, verify GET reflects change, concurrent adjust does not go negative
```

## Extension

Redis read-through cache with invalidation on write.
