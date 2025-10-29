package ru.ilia.weatherService.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ilia.weatherService.utils.Constants;
import ru.ilia.weatherService.utils.FileUtils;
import ru.ilia.weatherService.utils.HttpHandlerUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticFileHandler implements HttpHandler {

    private static final Map<String, String> MIME_TYPES = new ConcurrentHashMap<>();
    static {
        MIME_TYPES.put(".html", "text/html");
        MIME_TYPES.put(".htm", "text/html");
        MIME_TYPES.put(".css", "text/css");
        MIME_TYPES.put(".js", "application/javascript");
        MIME_TYPES.put(".json", "application/json");
        MIME_TYPES.put(".png", "image/png");
        MIME_TYPES.put(".jpg", "image/jpeg");
        MIME_TYPES.put(".jpeg", "image/jpeg");
        MIME_TYPES.put(".gif", "image/gif");
        MIME_TYPES.put(".ico", "image/x-icon");
        MIME_TYPES.put(".svg", "image/svg+xml");
        MIME_TYPES.put("", "application/octet-stream");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();

        String relativePath = requestPath;
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }

        if (relativePath.isEmpty()) {
            relativePath = "index.html";
        }

        Path baseDir = Paths.get(Constants.STATIC_BASE_DIR).toAbsolutePath().normalize();
        Path requestedFile = baseDir.resolve(relativePath).normalize();

        if (!requestedFile.startsWith(baseDir)) {
            HttpHandlerUtils.sendText(exchange, 403, "Forbidden");
            return;
        }

        if (!Files.exists(requestedFile) || Files.isDirectory(requestedFile)) {
            HttpHandlerUtils.sendText(exchange, 404, "Not Found");
            return;
        }

        String extension = FileUtils.getFileExtension(requestedFile.getFileName().toString());
        String mimeType = MIME_TYPES.getOrDefault(extension, "application/octet-stream");

        exchange.getResponseHeaders().set("Content-Type", mimeType);
        exchange.sendResponseHeaders(200, Files.size(requestedFile));

        try (OutputStream out = exchange.getResponseBody();
             InputStream in = Files.newInputStream(requestedFile);
             exchange) {
            in.transferTo(out);
        }
    }
}
