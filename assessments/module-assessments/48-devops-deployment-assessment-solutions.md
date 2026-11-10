# Solutions: Module 48 — DevOps and Deployment

1. Containers share the host kernel and use cgroup memory limits; JVM must size heap from **container** limit, not host RAM.
2. Build with JDK in one stage; runtime image carries only JAR + JRE → smaller, fewer attack surfaces.
3. Sets max heap as a **percentage of container-detected RAM** (with container support enabled).
4. **CI** — automated build/test on each change; **CD** — automated packaging/promotion toward production (delivery or deployment).
5. `verify` runs tests and checks; `package` can skip tests if misconfigured.
6. e.g. **Rolling** — default K8s gradual replace; **Blue/green** — instant switch, fast rollback, double capacity cost.
7. **Readiness** removes pod from service; **liveness** restarts pod.
8. Platform sends SIGTERM for graceful shutdown; JVM/Spring should drain before exit.
9. Same container image digest / same built JAR; config/env may differ per stage.
10. Each pod opens up to `poolMax` connections; total must stay under DB `max_connections`.

11. Metaspace, thread stacks, direct/Netty buffers, native libraries.
12. checkout → JDK 21 setup → cache deps → compile/test → static scan → package → (optional) image build+scan → deploy with strategy + probes.
