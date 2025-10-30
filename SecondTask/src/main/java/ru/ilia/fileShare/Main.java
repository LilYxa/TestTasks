package ru.ilia.fileShare;

import com.sun.net.httpserver.HttpServer;
import ru.ilia.fileShare.config.AppConfig;
import ru.ilia.fileShare.handler.DownloadHandler;
import ru.ilia.fileShare.handler.StaticFileHandler;
import ru.ilia.fileShare.handler.StatsHandler;
import ru.ilia.fileShare.handler.UploadHandler;
import ru.ilia.fileShare.model.FileData;
import ru.ilia.fileShare.service.FileCleanUpService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Main {
    private static final Map<String, FileData> fileRegistry = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        Files.createDirectories(AppConfig.getUploadPath());
        Files.createDirectories(Paths.get("static"));

        FileCleanUpService cleanupService = new FileCleanUpService(fileRegistry);
        cleanupService.start();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new StaticFileHandler());
        server.createContext("/upload", new UploadHandler(fileRegistry));
        server.createContext("/download/", new DownloadHandler(fileRegistry));
        server.createContext("/stats", new StatsHandler(fileRegistry));
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("Сервер запущен: http://localhost:8080");

    }
}