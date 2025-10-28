package ru.ilia.fileShare.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ilia.fileShare.model.FileData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class DownloadHandler implements HttpHandler {

    private final Map<String, FileData> fileDataMap;

    public DownloadHandler(Map<String, FileData> fileDataMap) {
        this.fileDataMap = fileDataMap;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();
        if (!uri.startsWith("/download/")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        String fileId = uri.substring("/download/".length());
        if (fileId.isEmpty()) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        FileData fileData = fileDataMap.get(fileId);
        if (fileData == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        Path filePath = Paths.get(fileData.getFilePath()).normalize();
        if (!Files.exists(filePath)) {
            fileDataMap.remove(fileId);
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        fileData.setLastAccessed(System.currentTimeMillis());
        exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
        exchange.sendResponseHeaders(200, Files.size(filePath));

        try (OutputStream os = exchange.getResponseBody();
            InputStream is = Files.newInputStream(filePath);
            exchange) {
                is.transferTo(os);
        }
    }
}
