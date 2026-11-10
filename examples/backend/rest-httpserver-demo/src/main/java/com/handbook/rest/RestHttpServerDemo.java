package com.handbook.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

public final class RestHttpServerDemo {
  private static final AtomicLong ID_SEQ = new AtomicLong(1);

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
    int port = server.getAddress().getPort();

    server.createContext("/users", new UsersHandler());
    server.createContext("/users/", new UserByIdHandler());
    server.start();

    try (HttpClient client = HttpClient.newHttpClient()) {
      HttpResponse<String> getOne = client.send(
          HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/users/1")).GET().build(),
          HttpResponse.BodyHandlers.ofString());
      System.out.println("GET /users/1 status=" + getOne.statusCode() + " body=" + getOne.body());

      String postBody = "{\"name\":\"New\"}";
      HttpResponse<String> post = client.send(
          HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/users"))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(postBody))
              .build(),
          HttpResponse.BodyHandlers.ofString());
      System.out.println("POST /users status=" + post.statusCode());
    } finally {
      server.stop(0);
    }
    System.out.println("restDemo=ok");
  }

  static final class UsersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
        respond(exchange, 405, "{\"error\":\"method not allowed\"}");
        return;
      }
      long id = ID_SEQ.getAndIncrement();
      respond(exchange, 201, "{\"id\":" + id + ",\"name\":\"New\"}");
    }
  }

  static final class UserByIdHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
        respond(exchange, 405, "{\"error\":\"method not allowed\"}");
        return;
      }
      String path = exchange.getRequestURI().getPath();
      String idPart = path.substring("/users/".length());
      if (!"1".equals(idPart)) {
        respond(exchange, 404, "{\"error\":\"not found\"}");
        return;
      }
      respond(exchange, 200, "{\"id\":1,\"name\":\"Lin\"}");
    }
  }

  private static void respond(HttpExchange exchange, int status, String body) throws IOException {
    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().set("Content-Type", "application/json");
    exchange.sendResponseHeaders(status, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }
}
