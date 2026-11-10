# Solutions: Module 49

1. e.g. **Processes** stateless; **Port binding** via embedded server; **Config** externalized; **Logs** to stdout.
2. **ConfigMap** — non-sensitive config; **Secret** — sensitive data (still protect at rest/in etcd).
3. **Service** (ClusterIP/LoadBalancer).
4. JVM + Spring startup exceeds few seconds; avoid traffic before app ready.
5. Build-time analysis; unreachable code/reflection may be omitted unless configured.
6. `ClassNotFoundException`, missing resource, failed reflective Hibernate/JPA path.
7. Peak JIT throughput workloads; heavy dynamic agents/reflection; long-running batch with mature JIT.
8. Generates bean/factory metadata at build time reducing runtime classpath scanning.
9. Prefer no server session in JVM; JWT or external session store if needed.
10. RDS: ops/backups; self-hosted: more control, you operate patching/HA.

11. Ingress → **Service** → Pod(s).
12. jdk-dynamic-proxy-demo, JPA entity reflection, Spring `@Autowired` scanning paths.
