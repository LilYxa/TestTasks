package ru.ilia.fileShare.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ilia.fileShare.model.FileData;
import ru.ilia.fileShare.config.AppConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class StatsHandler implements HttpHandler {

    private Map<String, FileData> fileDataMap;

    public StatsHandler(Map<String, FileData> fileDataMap) {
        this.fileDataMap = fileDataMap;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Path folder = AppConfig.getUploadPath();
        try {
//            long fileCount = Files.walk(folder)
//                    .filter(Files::isRegularFile)
//                    .count();

            long fileCount = fileDataMap.size();

            long totalSize = 0L;
            if (Files.exists(folder)) {
                try (var stream = Files.list(folder)) {
                    totalSize = stream
                            .filter(Files::isRegularFile)
                            .mapToLong(path -> {
                                try {
                                    return Files.size(path);
                                } catch (IOException e) {
                                    return 0;
                                }
                            })
                            .sum();
                }
            }

            long latestUpload = fileDataMap.values().stream()
                    .mapToLong(FileData::getUploadTime)
                    .max()
                    .orElse(0);

            long latestAccess = fileDataMap.values().stream()
                    .mapToLong(FileData::getLastAccessed)
                    .max()
                    .orElse(0);

            String jsonResponse = String.format(
                    "{%n" +
                            "  \"fileCount\": %d,%n" +
                            "  \"totalSizeBytes\": %d,%n" +
                            "  \"latestUploadTime\": %d,%n" +
                            "  \"latestAccessTime\": %d%n" +
                            "}",
                    fileCount, totalSize, latestUpload, latestAccess
            );

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes("UTF-8").length);

            try (OutputStream os = exchange.getResponseBody();
                exchange
            ) {
                os.write(jsonResponse.getBytes("UTF-8"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
