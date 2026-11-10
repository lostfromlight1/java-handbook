# Sockets and TCP

> **Status:** Completed  
> **Module:** 32 — Networking and HTTP  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Explain **TCP** reliable byte streams vs **UDP** datagrams.
- Use **`ServerSocket`** and **`Socket`** for a minimal client/server.
- Understand **blocking I/O** and why servers use thread pools or NIO at scale.

## 2. Prerequisites

- Module **20** — `java.net` survey.
- Module **24** — threads (demo uses virtual thread for server).

## 3. Why This Concept Exists

HTTP and databases ride on **TCP**. Knowing sockets explains timeouts, connection resets, and why **one request per connection** patterns differ from **keep-alive**. Java exposes TCP via **`java.net`** (blocking API) and **NIO** channels for selector-based designs.

## 4. Conceptual Explanation

| Concept | Meaning |
|---------|---------|
| IP address | Host locator (`127.0.0.1` loopback) |
| Port | 16-bit endpoint on a host (0–65535; privileged & ephemeral ranges vary by OS) |
| Socket | Endpoint for sending/receiving bytes |
| `ServerSocket` | Listens on a port, `accept()` returns connected `Socket` |
| Blocking I/O | `read()` waits until data arrives or connection closes |

**UDP** (`DatagramSocket`) — connectionless, loss possible; used for DNS-like or streaming media patterns, not typical REST.

## 5. Step-by-Step Examples

Demo: [tcp-loopback-demo](../../examples/networking/tcp-loopback-demo/).

**Verified output:**

```text
echo=echo:ping
port=19042
```

Client sends `ping`; server echoes `echo:ping`. Server handler runs on a **virtual thread** (`Thread.startVirtualThread`) while client uses blocking streams.

## 6. Real-World Use Cases

- Health-check TCP probes on load balancers.
- Custom binary protocols before HTTP migration.
- Understanding JDBC socket timeouts to PostgreSQL (Module **33**).

## 7. Edge Cases and Limitations

- **Port already in use** — `BindException`.
- **Firewall/NAT** — local success ≠ production reachability.
- One-thread-per-connection does not scale to millions without NIO/async redesign.

## 8. Common Mistakes

### Incorrect

Calls `ServerSocket.accept()` on the main UI thread in a desktop app — freezes UI.

### Corrected

Dedicated executor or virtual threads for accept loops.

## 9. Best Practices

- Set **`SoTimeout`** on blocking reads where appropriate.
- Close sockets in **try-with-resources**.
- Log **remote address** and correlation IDs at accept boundary.

## 10. Chapter Summary

TCP sockets are the foundation under HTTP and JDBC; the loopback demo shows minimal blocking client/server semantics.

## 11. Next Steps

[02-http-and-httpclient.md](02-http-and-httpclient.md)

## Review Checklist

- [x] Verified tcp-loopback-demo
