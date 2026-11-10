# Solutions: Module 41 — Design Patterns

1. One reason to change per class/module.
2. D: depend on abstractions; DI: technique passing implementations via constructor/container.
3. New exporter kind added by new class + switch/factory branch without editing consumers.
4. Adapter translates interface; Decorator adds responsibilities to same interface.
5. Strategy composes interchangeable algorithm; Template Method superclass defines skeleton with overrides.
6. Hidden global state; prefer container-scoped beans.
7. Builder for many optional steps; record for small immutable bundles.
8. Intercepts method calls for advice (transactions, logging).
9. `observer=events:2` (listener count after two subscriptions).
10. Subtype throws where supertype allows or weakens postconditions.

11. `factory=csv:rows`
12. `adapter=v2:data`
