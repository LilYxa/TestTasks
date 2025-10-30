package ru.ilia.fileShare.service;

import ru.ilia.fileShare.config.AppConfig;
import ru.ilia.fileShare.model.FileData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileCleanUpService {

    private final Map<String, FileData> fileDataMap;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "FileCleanUpService");
        thread.setDaemon(true);
        return thread;
    });

    public FileCleanUpService(Map<String, FileData> fileDataMap) {
        this.fileDataMap = fileDataMap;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
                this::cleanUpExpiredFiles,
                AppConfig.CLEANUP_INTERVAL_MILLIS,
                AppConfig.CLEANUP_INTERVAL_MILLIS,
                TimeUnit.MILLISECONDS
        );
    }

    public void stop() {
        scheduler.shutdown();
    }

    private void cleanUpExpiredFiles() {
        long now = System.currentTimeMillis();
        List<String> filesToRemove = new ArrayList<>();

        synchronized (fileDataMap) {
            for (var entry : fileDataMap.entrySet()) {
                FileData file = entry.getValue();
                if (now - file.getLastAccessed() > AppConfig.EXPIRY_MILLIS) {
                    try {
                        Files.deleteIfExists(Paths.get(file.getFilePath()));
                        filesToRemove.add(entry.getKey());
                        System.out.printf("Устаревший файл %s был удален",  file.getFilePath());
                    } catch (IOException e) {
                        System.err.println("Ошибка при удалении файла: " + file.getFilePath());
                        e.printStackTrace();
                    }
                }
            }
            filesToRemove.forEach(fileDataMap::remove);
        }
    }
}
