package ru.ilia.fileShare.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ilia.fileShare.config.AppConfig;
import ru.ilia.fileShare.model.FileData;
import ru.ilia.fileShare.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

public class UploadHandler implements HttpHandler {

    private final Map<String, FileData> fileDataMap;

    public UploadHandler(Map<String, FileData> fileDataMap) {
        this.fileDataMap = fileDataMap;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String ext = exchange.getRequestHeaders().getFirst("X-File-Extension");
        if (ext != null) {
            ext = ext.trim();
            if (!ext.matches("^\\.[a-zA-Z0-9]{1,10}$")) {
                ext = "";
            }
        } else {
            ext = "";
        }

        try (InputStream is = exchange.getRequestBody(); exchange) {
            String filename = "file_" + UUID.randomUUID() + ext;
            Path uploadDir = AppConfig.getUploadPath();
            Path filePath = uploadDir.resolve(filename);

            if (!filePath.startsWith(uploadDir)) {
                exchange.sendResponseHeaders(403, -1);
                return;
            }

            Files.createDirectories(uploadDir);
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);

            fileDataMap.put(filename, new FileData(filePath.toString()));

            String link = Constants.DOWNLOAD_LINK_BASE + filename;
            byte[] bytes = link.getBytes();

            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
        }
    }
}
