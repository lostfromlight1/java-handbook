# JDK dynamic proxy demo (AOP teaching analog)

`java.lang.reflect.Proxy` wraps an interface implementation with cross-cutting **before/after** logging — same mechanism Spring AOP uses for interface-based JDK proxies.

```bash
javac -d out src/main/java/com/handbook/proxy/*.java
java -cp out com.handbook.proxy.JdkProxyDemo
```

**Verified (Temurin 21.0.12.1):** `before=greet`; `result=Hello, Ada`; `proxyAop=ok`
