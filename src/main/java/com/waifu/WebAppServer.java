package com.waifu;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/** Servidor opcional para ejecutar la nueva interfaz web sin modificar la aplicación Swing existente. */
public final class WebAppServer {
    private static final int DEFAULT_PORT = 8080;
    private static Path webRoot;

    private WebAppServer() {}

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        webRoot = args.length > 1 ? Paths.get(args[1]).toAbsolutePath().normalize()
                : Paths.get("src/main/resources/web").toAbsolutePath().normalize();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", WebAppServer::handle);
        server.setExecutor(null);
        server.start();
        System.out.println("Waifu Nexus Web iniciado en http://localhost:" + port);
        System.out.println("Presioná Ctrl+C para detener el servidor.");
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String request = exchange.getRequestURI().getPath();
        if (request.equals("/")) request = "/index.html";
        Path file = webRoot.resolve(request.substring(1)).normalize();
        if (webRoot == null || !file.startsWith(webRoot) || !Files.isRegularFile(file)) {
            send(exchange, 404, "text/plain; charset=utf-8", "404 - Recurso no encontrado");
            return;
        }
        String contentType = contentType(file);
        byte[] data = Files.readAllBytes(file);
        exchange.getResponseHeaders().set("Cache-Control", "no-cache");
        exchange.sendResponseHeaders(200, data.length);
        try (OutputStream out = exchange.getResponseBody()) { out.write(data); }
    }

    private static String contentType(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        Map<String,String> types = new HashMap<>();
        types.put("html", "text/html; charset=utf-8"); types.put("css", "text/css; charset=utf-8");
        types.put("js", "application/javascript; charset=utf-8"); types.put("png", "image/png");
        types.put("jpg", "image/jpeg"); types.put("jpeg", "image/jpeg"); types.put("webp", "image/webp");
        types.put("mp4", "video/mp4"); types.put("webm", "video/webm"); types.put("ogg", "video/ogg");
        int dot = name.lastIndexOf('.');
        return dot >= 0 ? types.getOrDefault(name.substring(dot + 1), "application/octet-stream") : "application/octet-stream";
    }

    private static void send(HttpExchange exchange, int status, String type, String body) throws IOException {
        byte[] data = body.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.sendResponseHeaders(status, data.length);
        try (OutputStream out = exchange.getResponseBody()) { out.write(data); }
    }
}
