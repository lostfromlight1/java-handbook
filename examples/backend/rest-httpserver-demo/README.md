# REST-style API on JDK HttpServer

No Spring — `com.sun.net.httpserver.HttpServer` exposes JSON-ish responses for teaching HTTP verbs and status codes.

```bash
javac -d out src/main/java/com/handbook/rest/*.java
java -cp out com.handbook.rest.RestHttpServerDemo
```

**Verified (Temurin 21.0.12.1):** `GET /users/1 status=200 body={"id":1,"name":"Lin"}`; `POST /users status=201`; `restDemo=ok`
